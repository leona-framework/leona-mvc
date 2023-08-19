package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.services.logging.LoggerConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties("leona.service")
public class ServiceAutoConfigurationSource {
    @NestedConfigurationProperty
    private LoggerConfiguration logging = new LoggerConfiguration();
}
