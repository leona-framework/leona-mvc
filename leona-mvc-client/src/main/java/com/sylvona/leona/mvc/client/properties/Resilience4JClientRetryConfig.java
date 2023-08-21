package com.sylvona.leona.mvc.client.properties;

import io.github.resilience4j.common.retry.configuration.CommonRetryConfigurationProperties;
import io.github.resilience4j.common.utils.ConfigUtils;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration properties class for defining retry behavior specific to the Resilience4J client.
 * Extends the common retry configuration properties provided by Resilience4J.
 */
@Data
@MergeCandidate
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Resilience4JClientRetryConfig extends CommonRetryConfigurationProperties.InstanceProperties {
    /**
     * An empty set of status codes.
     */
    public static final Set<Integer> EMPTY_STATUS_CODE_SET = Collections.unmodifiableSet(new HashSet<>());

    /**
     * Default value for the 'enabled' property.
     */
    @SuppressWarnings("removal")
    private static final Boolean DEFAULT_ENABLED_VALUE = new Boolean(false);

    /**
     * Flag indicating whether the configuration name has been populated.
     */
    public boolean isConfigNamePopulated;

    /**
     * Flag indicating whether retry should be enabled.
     */
    @Builder.Default
    private Boolean enabled = DEFAULT_ENABLED_VALUE;

    /**
     * The name of a Resilience4j retry configuration.
     */
    @Nullable
    private String configName;

    /**
     * Set of status codes for which retry should be attempted.
     */
    @Builder.Default
    private Set<Integer> retryStatusCodes = EMPTY_STATUS_CODE_SET;

    /**
     * Set of status codes that should be ignored for retry.
     */
    @Builder.Default
    private Set<Integer> ignoredStatusCodes = EMPTY_STATUS_CODE_SET;

    /**
     * Merges two instances of {@link Resilience4JClientRetryConfig}.
     *
     * @param config1 The first configuration to merge.
     * @param config2 The second configuration to merge.
     * @return The merged configuration.
     */
    @MergeCandidate.MergeMethod
    public static Resilience4JClientRetryConfig merge(Resilience4JClientRetryConfig config1, Resilience4JClientRetryConfig config2) {
        ConfigUtils.mergePropertiesIfAny(config1, config2);

        if (config2.enabled == DEFAULT_ENABLED_VALUE)
            config2.enabled = config1.enabled;

        if (config2.enabled == null)
            config2.configName = config1.configName;

        if (config2.retryStatusCodes == EMPTY_STATUS_CODE_SET)
            config2.retryStatusCodes = config1.retryStatusCodes;

        if (config2.ignoredStatusCodes == EMPTY_STATUS_CODE_SET)
            config2.ignoredStatusCodes = config1.ignoredStatusCodes;

        return config2;
    }

}
