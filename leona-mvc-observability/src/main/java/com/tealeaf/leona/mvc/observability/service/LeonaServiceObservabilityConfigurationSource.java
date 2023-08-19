package org.lyora.leona.mvc.observability.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("leona.service.observability")
public class LeonaServiceObservabilityConfigurationSource {
    private boolean enabled;
}
