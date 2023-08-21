package com.sylvona.leona.mvc.flow;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.function.Function;

/**
 * The configuration {@code object} used to represent various values used in the library.
 */
@Data
@ConfigurationProperties("leona.flow")
public class LeonaFlowAutoConfigurationSource {
    @NestedConfigurationProperty
    private RequestInterceptorConfig requestInterceptor = new RequestInterceptorConfig();

    /**
     * Configuration for request interception
     */
    @Data
    public static class RequestInterceptorConfig {
        /**
         * Indicates whether requests should be intercepted and tracked.
         */
        private boolean enabled = true;
        /**
         * Indicates whether the default request capturers should be used.
         * <p>
         * <b>Default capturers:</b>
         * <p>{@code eventType - ENTRY/EXIT}
         * <p>{@code traceId - the traceId related to a request}
         * <p>{@code endpoint - the endpoint processing a request}
         */
        private boolean useDefaultCapturers = true;
        /**
         * A list of custom request {@link HttpServletRequestCaptor} capturers to be applied to inbound-requests.
         */
        private List<HttpServletRequestCaptor> requestCapturers;
        /**
         * A custom function for customizing the in-bound request log message.
         */
        private Class<? extends Function<HttpServletRequest, String>> onEntryMessage;
        /**
         * A custom function for customizing the out-bound request log message.
         */
        private Class<? extends Function<InterceptedRequestView, String>> onExitMessage;
        /**
         * The {@link Level} for the inbound/outbound log messages.
         */
        private Level level = Level.INFO;
    }
}
