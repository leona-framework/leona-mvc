package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.logging.ClientCapturePlan;
import com.tealeaf.leona.mvc.client.logging.ClientLogger;
import com.tealeaf.leona.mvc.client.logging.MdcClientCaptureFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ClientAutoConfiguration {
    private ApplicationContext applicationContext;

    public ClientAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ClientAutoConfigurationSource configurationSource() {
        return new ClientAutoConfigurationSource();
    }

    @Bean
    @ConditionalOnMissingBean(ClientCapturePlan.class)
    public ClientCapturePlan defaultCapturePlan() {
        return new MdcClientCaptures();
    }

    @Bean
    @ConditionalOnMissingBean(MdcClientCaptureFilter.class)
    public MdcClientCaptureFilter defaultCaptureFilter(ClientCapturePlan clientCapturePlan) {
        return new MdcClientCaptureFilter(clientCapturePlan);
    }

    @Bean
    @ConditionalOnMissingBean(ClientLogger.class)
    public ClientLogger defaultClientLogger(ClientAutoConfigurationSource configurationSource) {
        ClientAutoConfigurationSource.LoggerConfiguration loggerConfiguration = configurationSource.getLogging();
        if (loggerConfiguration.getClientLogger() != null) return applicationContext.getBean(loggerConfiguration.getClientLogger());
        return new DefaultClientLogger(loggerConfiguration);
    }
}
