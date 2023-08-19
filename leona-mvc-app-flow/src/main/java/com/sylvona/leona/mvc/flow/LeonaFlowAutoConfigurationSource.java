package com.sylvona.leona.mvc.flow;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.function.Function;

@Data
@ConfigurationProperties("leona.flow")
public class LeonaFlowAutoConfigurationSource {
    @NestedConfigurationProperty
    private RequestInterceptorConfig requestInterceptor = new RequestInterceptorConfig();

    @Data
    public static class RequestInterceptorConfig {
        private boolean enabled = true;
        private boolean useDefaultCapturers = true;
        private List<HttpServletRequestCapturer> requestCapturers;
        private Class<? extends Function<HttpServletRequest, String>> onEntryMessage;
        private Class<? extends Function<InterceptedRequestView, String>> onExitMessage;
        private Level level = Level.INFO;
    }
}
