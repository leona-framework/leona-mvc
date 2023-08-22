package com.sylvona.leona.mvc.client;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.mvc.client.properties.RestClientConfig;
import com.sylvona.leona.mvc.client.properties.RestTemplateConfigInjectorHelper;
import com.sylvona.leona.mvc.client.retry.Retryer;
import com.sylvona.leona.mvc.components.MvcLeonaConstants;
import com.sylvona.leona.mvc.components.containers.*;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Abstract base class for executing HTTP requests using a customized RestTemplate configuration.
 * This class is designed to work seamlessly with the Leona Client library and is typically used
 * to interact with remote services.
 * <p>
 * The configuration for the client's RestTemplate is defined in the application YAML file
 * under the {@code "leona.clients.configuration"} section. The properties defined in the configuration
 * are used during the creation of instances of this class.
 * <p>
 * In addition to the core functionality, this class also supports customization through various
 * hooks:
 * <ul>
 *   <li>{@link ClientInitializationHook}: Allows custom initialization of the client.</li>
 *   <li>{@link PreExchangeExecutionFilter}: Allows applying filters before sending the HTTP request.</li>
 *   <li>{@link PostExchangeExecutionFilter}: Allows applying filters after receiving the HTTP response.</li>
 * </ul>
 * These hooks enable modularization and further customization of the client's behavior.
 * @see GenericRestClient
 * @see TypedRestClient
 */
public abstract class RestClient implements ApplicationContextAware {
    static final RequestBuilder REQUEST_BUILDER = Request.Builder::build;
    private final String clientName;
    private final Context context = new ThreadContext();
    private final AddOnlyArrayList<PreExchangeExecutionFilter> preExecutionFilters = new AddOnlyArrayList<>();
    private final AddOnlyArrayList<PostExchangeExecutionFilter> postExecutionFilters = new AddOnlyArrayList<>();
    final RestClientConfig clientConfig;
    final Request preconfiguredRequestEntity;

    private Retryer retryer;
    private RestTemplate restTemplate;

    /**
     * Creates a new {@link RestClient} instance with a provided RestTemplate and client configuration.
     *
     * @param restTemplate The RestTemplate instance used for executing requests.
     * @param clientConfig The configuration for the REST client.
     */
    public RestClient(RestTemplate restTemplate, RestClientConfig clientConfig) {
        this.restTemplate = restTemplate;
        this.clientConfig = clientConfig;

        String clientName = clientConfig.getClientName();
        if (StringUtils.isBlank(clientName)) {
            clientName = this.getClass().getSimpleName();
        }

        this.clientName = clientName;
        this.preconfiguredRequestEntity = produceRequestEntity(clientConfig);
    }

    /**
     * Creates a new {@link RestClient} instance with a provided RestTemplate. The RestTemplate must be an <b>accurately qualified RestTemplate</b> to properly work.
     *
     * @param restTemplate The <b>preconfigured</b> RestTemplate instance used for executing requests.
     */
    public RestClient(RestTemplate restTemplate) {
        this(restTemplate, RestTemplateConfigInjectorHelper.extractProperties(restTemplate));
    }

    /**
     * Returns the name of the client.
     *
     * @return The name of the client.
     */
    public final String getName() {
        return this.clientName;
    }

    /**
     * Returns the configuration of the client.
     *
     * @return The configuration of the client.
     */
    public final RestClientConfig getConfig() {
        return this.clientConfig;
    }

    /**
     * Callback method for custom initialization of the client.
     * Use this method to customize the client's behavior during initialization.
     *
     * @param initializer The initialization hook to customize the client.
     */
    protected void onInitializer(@NotNull ClientInitializationHook initializer) {}

    final <ResponseType> ResponseEntity<ResponseType> execute(Request request, Function<RequestEntity.BodyBuilder, RequestEntity<?>> entityFunction, ParameterizedTypeReference<ResponseType> responseTypeReference) {
        Context residualContext = ResidualContext.from(context);
        RestClientExecution execution = new RestClientExecution(this, request, residualContext);

        for (PreExchangeExecutionFilter preExchangeExecutionFilter : preExecutionFilters) {
            request = preExchangeExecutionFilter.filter(request);
        }

        final Request finalRequest = request;

        if (retryer != null) {
            return retryer.execute(() -> restExchange(entityFunction.apply(Request.toEntityBuilder(finalRequest)), execution, responseTypeReference, residualContext));
        }
        return restExchange(entityFunction.apply(Request.toEntityBuilder(request)), execution, responseTypeReference, residualContext);
    }

    private <ResponseType> ResponseEntity<ResponseType> restExchange(RequestEntity<?> request, RestClientExecution execution, ParameterizedTypeReference<ResponseType> responseTypeReference, Context context) {
        try {
            ResponseEntity<ResponseType> response = restTemplate.exchange(request, responseTypeReference);
            Duration executionTime = context.getOrElse(MvcLeonaConstants.REQUEST_DURATION_KEY, Duration.ZERO);
            execution.executionTime(executionTime).result(response);
            return filterResponse(response, execution);
        } catch (Exception exception) {
            Duration executionTime = context.getOrElse(MvcLeonaConstants.REQUEST_DURATION_KEY, Duration.ZERO);
            execution.executionTime(executionTime).error(exception);
            filterResponse(null, execution);
            throw exception;
        }
    }

    private <ResponseType> ResponseEntity<ResponseType> filterResponse(@Nullable ResponseEntity<ResponseType> response, RestClientExecution executionView) {
        for (PostExchangeExecutionFilter postExchangeExecutionFilter : postExecutionFilters) {
            ResponseEntity<ResponseType> filteredResponse = postExchangeExecutionFilter.filter(executionView, response);
            if (filteredResponse != null) response = filteredResponse;
            executionView.result(response);
        }
        return response;
    }

    private static Request produceRequestEntity(RestClientConfig config) {
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromUriString(config.getHost()).path(config.getPath());
        Integer port = config.getPort();
        if (port != null) componentsBuilder.port(port);
        return SimpleRequest.builder(componentsBuilder).httpMethod(config.httpMethod()).headers(config.getHeaders()).build();
    }


    @Override
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RestClient.Modifier modifier = new Modifier(this);

        Consumer<ClientInitializationHook> initFunc = initializer -> {
            onInitializer(initializer);
            initializer.onInitialize(modifier);
        };

        LINQ.stream(DefaultInitializers.initializers()).concat(applicationContext.getBeansOfType(ClientInitializationHook.class).values()).forEach(initFunc);

        preExecutionFilters.sort(Comparator.<PreExchangeExecutionFilter>comparingInt(e -> e.preExecutionPriority().value()).reversed());
        postExecutionFilters.sort(Comparator.<PostExchangeExecutionFilter>comparingInt(e -> e.postExecutionPriority().value()).reversed());
    }

    /**
     * Modifier class that provides methods for modifying and configuring a {@link RestClient}.
     * This class allows you to customize various aspects of the client's behavior, such as adding
     * initializers, configuring filters, and modifying the RestTemplate instance.
     */
    @Getter
    @RequiredArgsConstructor
    public static final class Modifier {
        private final RestClient client;

        /**
         * Retrieves the {@link Context} associated with the client execution.
         *
         * @return The {@link Context} associated with the client.
         */
        public Context getContext() {
            return client.context;
        }

        /**
         * Retrieves the {@link Logger} instance for the client.
         *
         * @return The {@link Logger} instance.
         */
        public Logger getLogger() {
            return LoggerFactory.getLogger(client.getClass());
        }

        /**
         * Sets the {@link Retryer} to be used for retrying failed requests.
         *
         * @param retryer The {@link Retryer} instance to set.
         */
        public void setRetryer(Retryer retryer) {
            client.retryer = retryer;
        }

        /**
         * Retrieves the list of {@link PreExchangeExecutionFilter} filters for modifying requests before execution.
         *
         * @return The list of {@link PreExchangeExecutionFilter} filters.
         */
        public AddOnlyList<PreExchangeExecutionFilter> getPreExecutionFilters() {
            return client.preExecutionFilters;
        }

        /**
         * Retrieves the list of {@link PostExchangeExecutionFilter} filters for modifying responses after execution.
         *
         * @return The list of {@link PostExchangeExecutionFilter} filters.
         */
        public AddOnlyList<PostExchangeExecutionFilter> getPostExecutionFilters() {
            return client.postExecutionFilters;
        }

        /**
         * Modifies the {@link RestTemplate} instance associated with the client using a custom modifier function.
         *
         * @param restTemplateModifier The modifier function for the RestTemplate.
         * @return The modified Modifier instance.
         */
        public Modifier modifyRestTemplate(UnaryOperator<RestTemplate> restTemplateModifier) {
            client.restTemplate = restTemplateModifier.apply(client.restTemplate);
            return this;
        }
    }
}
