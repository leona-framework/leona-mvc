package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.services.logging.LoggerConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties for customizing Leona service autoconfiguration behavior.
 * This class provides settings related to service logging and other configurations.
 *
 * @see LoggerConfiguration
 */
@Data
@ConfigurationProperties("leona.service")
public class ServiceAutoConfigurationSource {
    /**
     * Configuration properties for service logging customization.
     * Allows enabling/disabling logging, specifying log levels, and more.
     *
     * @see LoggerConfiguration
     */
    @NestedConfigurationProperty
    private LoggerConfiguration logging = new LoggerConfiguration();
}
