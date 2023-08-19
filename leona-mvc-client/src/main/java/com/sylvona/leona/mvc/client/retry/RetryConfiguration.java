package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.properties.Resilience4JClientRetryConfig;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Set;

@Data
public class RetryConfiguration {
    private boolean enabled = true;

    @NestedConfigurationProperty
    private RetryLoggingConfiguration logging = new RetryLoggingConfiguration();

    private Set<Integer> neverRetriedStatusCodes = Resilience4JClientRetryConfig.EMPTY_STATUS_CODE_SET;
}
