package com.sylvona.leona.mvc.flow;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * The configuration {@code object} used to represent various values used in the library.
 */
@ConfigurationProperties("leona.flow")
public class LeonaFlowAutoConfigurationSource {
    @NestedConfigurationProperty
    private RequestInterceptorConfig requestInterceptor = new RequestInterceptorConfig();

    public RequestInterceptorConfig getRequestInterceptor() {
        return this.requestInterceptor;
    }

    public void setRequestInterceptor(RequestInterceptorConfig requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    /**
     * Configuration for request interception
     */
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

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Level getLevel() {
            return level;
        }

        public void setLevel(Level level) {
            this.level = level;
        }

        public Class<? extends Function<HttpServletRequest, String>> getOnEntryMessage() {
            return onEntryMessage;
        }

        public void setOnEntryMessage(Class<? extends Function<HttpServletRequest, String>> onEntryMessage) {
            this.onEntryMessage = onEntryMessage;
        }

        public Class<? extends Function<InterceptedRequestView, String>> getOnExitMessage() {
            return onExitMessage;
        }

        public void setOnExitMessage(Class<? extends Function<InterceptedRequestView, String>> onExitMessage) {
            this.onExitMessage = onExitMessage;
        }

        public List<HttpServletRequestCaptor> getRequestCapturers() {
            return requestCapturers;
        }

        public void setRequestCapturers(List<HttpServletRequestCaptor> requestCapturers) {
            this.requestCapturers = requestCapturers;
        }

        public boolean isUseDefaultCapturers() {
            return useDefaultCapturers;
        }

        public void setUseDefaultCapturers(boolean useDefaultCapturers) {
            this.useDefaultCapturers = useDefaultCapturers;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RequestInterceptorConfig that)) return false;
            return enabled == that.enabled && useDefaultCapturers == that.useDefaultCapturers && Objects.equals(requestCapturers, that.requestCapturers) && Objects.equals(onEntryMessage, that.onEntryMessage) && Objects.equals(onExitMessage, that.onExitMessage) && level == that.level;
        }

        @Override
        public int hashCode() {
            return Objects.hash(enabled, useDefaultCapturers, requestCapturers, onEntryMessage, onExitMessage, level);
        }
    }
}
