package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.properties.BeanBackedClientConfig;
import com.tealeaf.leona.mvc.client.properties.RestTemplateConfigInjectorHelper;
import com.tealeaf.leona.mvc.components.containers.AddOnlyArrayList;
import com.tealeaf.leona.mvc.components.containers.AddOnlyList;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.components.containers.ThreadContext;
import com.tealeaf.leona.mvc.components.streams.LINQ;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class ClientExecuter implements ApplicationContextAware {
    static final RequestBuilder REQUEST_BUILDER = Request.Builder::build;

    private final String clientName;
    private final Context context = new ThreadContext();
    private final AddOnlyArrayList<PreExchangeExecutionFilter> preExecutionFilters = new AddOnlyArrayList<>();
    private final AddOnlyArrayList<PostExchangeExecutionFilter> postExecutionFilters = new AddOnlyArrayList<>();
    final BeanBackedClientConfig clientConfig;
    final Request preconfiguredRequestEntity;

    private RestTemplate restTemplate;


    public ClientExecuter(RestTemplate restTemplate, BeanBackedClientConfig clientConfig) {
        this.restTemplate = restTemplate;
        this.clientConfig = clientConfig;

        String clientName = clientConfig.getClientName();
        if (StringUtils.isBlank(clientName)) {
            clientName = Optional.ofNullable(this.getClass().getAnnotation(RestClient.class)).map(RestClient::value)
                    .orElseThrow(() -> new IllegalArgumentException("clientName must be not-null and not empty."));
        }

        this.clientName = clientName;
        this.preconfiguredRequestEntity = produceRequestEntity(clientConfig);
    }

    public ClientExecuter(RestTemplate restTemplate) {
        this(restTemplate, RestTemplateConfigInjectorHelper.extractProperties(restTemplate));
    }

    public final String getName() {
        return this.clientName;
    }


    protected void onInitializer(@NotNull ClientInitializer initializer) {}

    final <ResponseType> ResponseEntity<ResponseType> execute(Request request, Function<RequestEntity.BodyBuilder, RequestEntity<?>> entityFunction, ParameterizedTypeReference<ResponseType> responseTypeReference) {
        RestClientExecution execution = new RestClientExecution(this, request, context);

        for (PreExchangeExecutionFilter preExchangeExecutionFilter : preExecutionFilters) {
            request = preExchangeExecutionFilter.filter(request);
        }

        return restExchange(entityFunction.apply(Request.toEntityBuilder(request)), execution, responseTypeReference);
    }

    private <ResponseType> ResponseEntity<ResponseType> restExchange(RequestEntity<?> request, RestClientExecution execution, ParameterizedTypeReference<ResponseType> responseTypeReference) {
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
            response = postExchangeExecutionFilter.filter(executionView, response);
            executionView.result(response);
        }
        return response;
    }

    private static Request produceRequestEntity(BeanBackedClientConfig config) {
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromUriString(config.getHost()).path(config.getPath());
        Integer port = config.getPort();
        if (port != null) componentsBuilder.port(port);
        return SimpleRequest.builder(componentsBuilder).httpMethod(config.httpMethod()).build();
    }

    @Override
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ClientExecuter.Modifier modifier = new Modifier(this);

        Consumer<ClientInitializer> initFunc = initializer -> {
            onInitializer(initializer);
            initializer.onInitialize(modifier);
        };

        LINQ.stream(DefaultInitializers.initializers()).concat(applicationContext.getBeansOfType(ClientInitializer.class).values()).forEach(initFunc);

        preExecutionFilters.sort(Comparator.<PreExchangeExecutionFilter>comparingInt(e -> e.preExecutionPriority().value()).reversed());
        postExecutionFilters.sort(Comparator.<PostExchangeExecutionFilter>comparingInt(e -> e.postExecutionPriority().value()).reversed());
    }

    @RequiredArgsConstructor
    public static final class Modifier {
        private final ClientExecuter client;

        public Context getContext() {
            return client.context;
        }

        public Logger getLogger() {
            return LoggerFactory.getLogger(client.getClass());
        }

        public AddOnlyList<PreExchangeExecutionFilter> getPreExecutionFilters() {
            return client.preExecutionFilters;
        }

        public AddOnlyList<PostExchangeExecutionFilter> getPostExecutionFilters() {
            return client.postExecutionFilters;
        }

        public Modifier modifyRestTemplate(UnaryOperator<RestTemplate> restTemplateModifier) {
            client.restTemplate = restTemplateModifier.apply(client.restTemplate);
            return this;
        }
    }
}
