package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.properties.Resilience4JClientRetryConfig;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Set;

/**
 * Configuration class for defining retry behavior and settings across all clients.
 */
@Data
public class RetryConfiguration {
    /**
     * Indicates whether retry functionality is enabled across all clients.
     */
    private boolean enabled = true;

    /**
     * Configuration properties for retry logging.
     */
    @NestedConfigurationProperty
    private RetryLoggingConfiguration logging = new RetryLoggingConfiguration();

    /**
     * Set of HTTP status codes that should never be retried.
     */
    private Set<Integer> neverRetriedStatusCodes = Resilience4JClientRetryConfig.EMPTY_STATUS_CODE_SET;
}
