package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.flow.FlowCapturer;
import com.tealeaf.leona.mvc.client.logging.LoggerConfiguration;
import com.tealeaf.leona.mvc.client.properties.RestClientConfig;
import com.tealeaf.leona.mvc.client.retry.RetryConfiguration;
import com.tealeaf.leona.mvc.components.MdcLoggingConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties("leona.client")
public class ClientAutoConfigurationSource {
    public static final String CONFIGURATION_QUALIFIER = "leona.client.configurations";

    private Map<String, RestClientConfig> configurations;

    @NestedConfigurationProperty
    private LoggerConfiguration logging = new LoggerConfiguration();

    @NestedConfigurationProperty
    private ForwardingConfiguration forwarding = new ForwardingConfiguration();

    @NestedConfigurationProperty
    private RetryConfiguration retry = new RetryConfiguration();

    @Data
    public static class ForwardingConfiguration {
        private static final FlowCapturer TRACE_ID_FORWARDER = new FlowCapturer(MdcLoggingConstants.TRACE_ID, "trace-id");

        private boolean enabled = true;
        private List<FlowCapturer> forwardedProperties = new ArrayList<>(Collections.singletonList(TRACE_ID_FORWARDER));
    }
}
