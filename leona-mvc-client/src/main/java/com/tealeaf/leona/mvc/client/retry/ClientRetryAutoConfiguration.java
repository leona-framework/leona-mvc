package org.lyora.leona.mvc.client.retry;

import org.lyora.leona.mvc.client.ClientAutoConfigurationSource;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RetryAutoConfiguration.class)
@AutoConfigureAfter(RetryAutoConfiguration.class)
public class ClientRetryAutoConfiguration {
    private final ApplicationContext applicationContext;

    public ClientRetryAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(RetryLogger.class)
    @ConditionalOnProperty(value = "leona.client.retry.logging.enabled", matchIfMissing = true)
    public RetryLogger getDefaultRetryLogger(ClientAutoConfigurationSource clientAutoConfigurationSource) {
        RetryLoggingConfiguration loggerConfiguration = clientAutoConfigurationSource.getRetry().getLogging();
        if (loggerConfiguration.getRetryLogger() != null) return applicationContext.getBean(loggerConfiguration.getRetryLogger());
        return new DefaultRetryLogger(loggerConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(Retryer.class)
    @ConditionalOnProperty(value = "leona.client.retry.enabled", matchIfMissing = true)
    public Retryer getDefaultRetryer(ClientAutoConfigurationSource configurationSource, RetryRegistry registry, RetryLogger retryLogger) {
        return new DefaultStatusCodeRetryer(configurationSource.getRetry(), registry, retryLogger);
    }
}
