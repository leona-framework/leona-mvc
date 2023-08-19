package org.lyora.leona.mvc.client.properties;

import io.github.resilience4j.common.retry.configuration.CommonRetryConfigurationProperties;
import io.github.resilience4j.common.utils.ConfigUtils;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@MergeCandidate
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Resilience4JClientRetryConfig extends CommonRetryConfigurationProperties.InstanceProperties {
    public static final Set<Integer> EMPTY_STATUS_CODE_SET = Collections.unmodifiableSet(new HashSet<>());
    @SuppressWarnings("removal")
    private static final Boolean DEFAULT_ENABLED_VALUE = new Boolean(false);

    public boolean isConfigNamePopulated;

    @Builder.Default
    private Boolean enabled = DEFAULT_ENABLED_VALUE;

    @Nullable
    private String configName;

    @Builder.Default
    private Set<Integer> retryStatusCodes = EMPTY_STATUS_CODE_SET;

    @Builder.Default
    private Set<Integer> ignoredStatusCodes = EMPTY_STATUS_CODE_SET;

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
