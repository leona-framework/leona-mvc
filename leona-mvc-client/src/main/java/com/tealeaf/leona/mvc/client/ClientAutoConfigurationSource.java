package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.logging.ClientLogger;
import com.tealeaf.leona.mvc.client.properties.BeanBackedClientConfig;
import lombok.Data;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
@Component
@ConfigurationProperties("leona.client")
public class ClientAutoConfigurationSource {
    private Map<String, BeanBackedClientConfig> configurations;

    @NestedConfigurationProperty
    private LoggerConfiguration logging = new LoggerConfiguration();

    @Data
    public static class LoggerConfiguration {
        private Class<? extends ClientLogger> clientLogger;
        private Class<? extends Predicate<ClientExecutionView>> logPredicate;
        private Class<? extends Function<ClientExecutionView, String>> messageSupplier;
        private Level level = Level.INFO;
    }
}
