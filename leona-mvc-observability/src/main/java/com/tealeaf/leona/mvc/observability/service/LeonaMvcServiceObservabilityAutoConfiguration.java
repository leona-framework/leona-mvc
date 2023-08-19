package org.lyora.leona.mvc.observability.service;

import org.lyora.leona.mvc.services.MetadataHolder;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = ObservationAutoConfiguration.class)
@ConditionalOnClass(MetadataHolder.class)
@ConditionalOnProperty("leona.service.observability.enabled")
@EnableConfigurationProperties(LeonaServiceObservabilityConfigurationSource.class)
public class LeonaMvcServiceObservabilityAutoConfiguration {

    @Bean
    @ConditionalOnBean(ObservationRegistry.class)
    ServiceObservabilityFilter serviceObservabilityFilter(ObservationRegistry observationRegistry) {
        return new ServiceObservabilityFilter(observationRegistry);
    }

}
