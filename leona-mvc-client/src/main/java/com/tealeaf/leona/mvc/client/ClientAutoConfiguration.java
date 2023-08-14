package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.flow.ContextToHeaderForwarder;
import com.tealeaf.leona.mvc.client.logging.ClientCapturePlan;
import com.tealeaf.leona.mvc.client.logging.ClientLogger;
import com.tealeaf.leona.mvc.client.logging.LoggerConfiguration;
import com.tealeaf.leona.mvc.client.logging.MdcClientCaptureFilter;
import com.tealeaf.leona.mvc.client.retry.ClientRetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(ClientRetryAutoConfiguration.class)
@Import(ClientRetryAutoConfiguration.class)
class ClientAutoConfiguration {
    private final ApplicationContext applicationContext;

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
    @ConditionalOnProperty(value = "leona.client.logging.enabled", matchIfMissing = true)
    @ConditionalOnMissingBean(ClientLogger.class)
    public ClientLogger defaultClientLogger(ClientAutoConfigurationSource configurationSource) {
        LoggerConfiguration loggerConfiguration = configurationSource.getLogging();
        if (loggerConfiguration.getClientLogger() != null) return applicationContext.getBean(loggerConfiguration.getClientLogger());
        return new DefaultClientLogger(loggerConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(ContextToHeaderForwarder.class)
    @ConditionalOnProperty(value = "leona.client.forwarding.enabled", matchIfMissing = true)
    @ConditionalOnClass(name = "com.tealeaf.leona.mvc.flow.InterceptedRequestView")
    public ContextToHeaderForwarder defaultContextForwarder(ClientAutoConfigurationSource configurationSource) {
        return new DefaultContextToHeaderForwarder(configurationSource.getForwarding().getForwardedProperties());
    }
}
