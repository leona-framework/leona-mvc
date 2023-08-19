package com.sylvona.leona.mvc.client.flow;

import com.sylvona.leona.mvc.client.ClientExecuter;
import com.sylvona.leona.mvc.client.ClientInitializationHook;
import com.sylvona.leona.mvc.client.PreExchangeExecutionFilter;
import com.sylvona.leona.mvc.client.Request;
import com.sylvona.leona.mvc.components.containers.ThreadContext;
import com.sylvona.leona.mvc.flow.InterceptedRequestView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpHeaders;

import java.util.List;

@ConditionalOnClass(InterceptedRequestView.class)
public interface ContextToHeaderForwarder extends PreExchangeExecutionFilter, ClientInitializationHook {

    @Override
    default void onInitialize(ClientExecuter.Modifier clientModifier) {
        clientModifier.getPreExecutionFilters().add(this);
    }

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

        for (FlowCapturer capturer : getForwardedProperties()) {
            String value = (String) capturer.apply(requestView);
            headers.addIfAbsent(capturer.getHeaderName(), value);
        }

        return request;
    }

    List<FlowCapturer> getForwardedProperties();
}
