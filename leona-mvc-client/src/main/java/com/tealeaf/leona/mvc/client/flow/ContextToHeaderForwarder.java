package com.tealeaf.leona.mvc.client.flow;

import com.tealeaf.leona.mvc.client.ClientExecuter;
import com.tealeaf.leona.mvc.client.ClientInitializationHook;
import com.tealeaf.leona.mvc.client.PreExchangeExecutionFilter;
import com.tealeaf.leona.mvc.client.Request;
import com.tealeaf.leona.mvc.components.containers.ThreadContext;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

@ConditionalOnClass(name = "com.tealeaf.leona.mvc.flow.InterceptedRequestView")
public interface ContextToHeaderForwarder extends PreExchangeExecutionFilter, ClientInitializationHook {
    Class<? extends Map<String, String>> INTERCEPTED_REQUEST_VIEW_CLASS = getInterceptedRequestViewClass();

    @Override
    default void onInitialize(ClientExecuter.Modifier clientModifier) {
        clientModifier.getPreExecutionFilters().add(this);
    }

    @SneakyThrows
    @Override
    default Request filter(Request request) {
        ThreadContext context = new ThreadContext();
        Map<String, String> contextMap = context.get(INTERCEPTED_REQUEST_VIEW_CLASS);
        HttpHeaders headers = request.getHeaders();

        for (FlowCapturer capturer : getForwardedProperties()) {
            String value = (String) capturer.apply(contextMap);
            headers.addIfAbsent(capturer.getHeaderName(), value);
        }

        return request;
    }

    List<FlowCapturer> getForwardedProperties();

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static Class<? extends Map<String, String>> getInterceptedRequestViewClass() {
        return (Class<? extends Map<String, String>>) Class.forName("com.tealeaf.leona.mvc.flow.InterceptedRequestView");
    }
}
