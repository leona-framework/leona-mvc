package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.flow.FlowCaptor;
import com.sylvona.leona.mvc.client.logging.LoggerConfiguration;
import com.sylvona.leona.mvc.client.properties.RestClientConfig;
import com.sylvona.leona.mvc.client.retry.RetryConfiguration;
import com.sylvona.leona.mvc.components.MdcLoggingConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Main configuration instance for leona-client related properties
 */
@Data
@Component
@ConfigurationProperties("leona.client")
public class ClientAutoConfigurationSource {
    /**
     * Qualifier for the client configurations
     */
    public static final String CONFIGURATION_QUALIFIER = "leona.client.configurations";

    /**
     * Property mapping for {@link RestClientConfig}
     */
    private Map<String, RestClientConfig> configurations;

    /**
     * The global logging configuration applied to all clients.
     */
    @NestedConfigurationProperty
    private LoggerConfiguration logging = new LoggerConfiguration();

    /**
     * The global context-forwarding configuration applied to all clients.
     * <p>
     * This configuration is only relevant if leona-app-flow is present in the classpath.
     */
    @NestedConfigurationProperty
    private ForwardingConfiguration forwarding = new ForwardingConfiguration();

    /**
     * The global retry configuration applied to all clients.
     */
    @NestedConfigurationProperty
    private RetryConfiguration retry = new RetryConfiguration();

    /**
     * A configuration for context forwarding, only applicable if leona-app-flow is in the classpath.
     */
    @Data
    public static class ForwardingConfiguration {
        private static final FlowCaptor TRACE_ID_FORWARDER = new FlowCaptor(MdcLoggingConstants.TRACE_ID, "trace-id");

        /**
         * Whether context forwarding to outbound client requests is enabled
         */
        private boolean enabled = true;
        /**
         * A list of FlowCaptors to forward context values to outbound requests. By default, forwards the traceId.
         */
        private List<FlowCaptor> forwardedProperties = new ArrayList<>(Collections.singletonList(TRACE_ID_FORWARDER));
    }
}
