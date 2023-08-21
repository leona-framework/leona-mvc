package com.sylvona.leona.mvc.client.flow;

import com.sylvona.leona.mvc.client.RestClient;
import com.sylvona.leona.mvc.client.ClientInitializationHook;
import com.sylvona.leona.mvc.client.PreExchangeExecutionFilter;
import com.sylvona.leona.mvc.client.Request;
import com.sylvona.leona.mvc.components.containers.ThreadContext;
import com.sylvona.leona.mvc.flow.InterceptedRequestView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * Interface defining a pre-exchange execution filter that forwards context information from a {@link InterceptedRequestView}
 * to HTTP headers in the outgoing request. This allows contextual information to be propagated across microservices.
 * <p>
 * Note: this interface is only applied IF the leona-app-flow library is in the classpath.
 */
@ConditionalOnClass(InterceptedRequestView.class)
public interface ContextToHeaderForwarder extends PreExchangeExecutionFilter, ClientInitializationHook {

    /**
     * Adds the context forwarder to the list of {@link PreExchangeExecutionFilter} during {@link RestClient} initialization.
     *
     * @param clientModifier The client modifier to add the forwarder to.
     */
    @Override
    default void onInitialize(RestClient.Modifier clientModifier) {
        clientModifier.getPreExecutionFilters().add(this);
    }

    /**
     * Processes a {@link Request} extracting context information from the app-flow context and adding it to the outgoing request headers.
     *
     * @param request The prepared {@link Request}.
     * @return The filtered request with added context information in headers.
     */
    @Override
    default Request filter(Request request) {
        ThreadContext context = new ThreadContext();
        InterceptedRequestView requestView = context.get(InterceptedRequestView.class);

        // There's a couple scenarios where the IRV can be null, perhaps a better solution would be to combat these
        // scenarios but one common scenario is Spring's security filter being applied before the request interceptor
        // thus causing context propagation to fail for Leona. This is a very niche scenario and thus ignored for now
        if (requestView == null)
            return request;

        HttpHeaders headers = request.getHeaders();

        for (FlowCaptor capturer : getForwardedProperties()) {
            String value = (String) capturer.apply(requestView);
            headers.addIfAbsent(capturer.getHeaderName(), value);
        }

        return request;
    }

    /**
     * Retrieves the list of {@link FlowCaptor} instances defining the properties to be forwarded as headers.
     *
     * @return The list of forwarded properties captors.
     */
    List<FlowCaptor> getForwardedProperties();
}
