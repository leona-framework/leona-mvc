package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.ClientAutoConfigurationSource;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for setting up client retry mechanisms in the Leona framework.
 * This configuration integrates with Resilience4j's retry mechanisms and provides beans
 * for retry logger and retryer based on the client configuration properties.
 */
@Configuration
@Import(RetryAutoConfiguration.class)
@AutoConfigureAfter(RetryAutoConfiguration.class)
public class ClientRetryAutoConfiguration {
    private final ApplicationContext applicationContext;

    /**
     * Constructor to create an instance of {@code ClientRetryAutoConfiguration}.
     *
     * @param applicationContext The Spring application context.
     */
    public ClientRetryAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Creates a default {@link RetryLogger} bean if missing and if retry logging is enabled.
     *
     * @param clientAutoConfigurationSource The configuration source for client settings.
     * @return A {@link RetryLogger} bean for logging retry attempts.
     */
    @Bean
    @ConditionalOnMissingBean(RetryLogger.class)
    @ConditionalOnProperty(value = "leona.client.retry.logging.enabled", matchIfMissing = true)
    public RetryLogger getDefaultRetryLogger(ClientAutoConfigurationSource clientAutoConfigurationSource) {
        RetryLoggingConfiguration loggerConfiguration = clientAutoConfigurationSource.getRetry().getLogging();
        if (loggerConfiguration.getRetryLogger() != null) return applicationContext.getBean(loggerConfiguration.getRetryLogger());
        return new DefaultRetryLogger(loggerConfiguration);
    }

    /**
     * Creates a default {@link Retryer} bean if missing and if client retry is enabled.
     *
     * @param configurationSource The autoconfiguration source for client settings.
     * @param registry The Resilience4j retry registry.
     * @param retryLogger The retry logger bean for logging retry attempts.
     * @return A retryer bean for managing retry operations.
     */
    @Bean
    @ConditionalOnMissingBean(Retryer.class)
    @ConditionalOnProperty(value = "leona.client.retry.enabled", matchIfMissing = true)
    public Retryer getDefaultRetryer(ClientAutoConfigurationSource configurationSource, RetryRegistry registry, RetryLogger retryLogger) {
        return new DefaultStatusCodeRetryer(configurationSource.getRetry(), registry, retryLogger);
    }
}
