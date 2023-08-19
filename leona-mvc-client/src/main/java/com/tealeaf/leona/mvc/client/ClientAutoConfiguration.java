package org.lyora.leona.mvc.client;

import org.lyora.leona.mvc.client.flow.ContextToHeaderForwarder;
import org.lyora.leona.mvc.client.logging.ClientCapturePlan;
import org.lyora.leona.mvc.client.logging.ClientLogger;
import org.lyora.leona.mvc.client.logging.LoggerConfiguration;
import org.lyora.leona.mvc.client.logging.MdcClientCaptureFilter;
import org.lyora.leona.mvc.client.properties.RestClientBeanProcessor;
import org.lyora.leona.mvc.client.retry.ClientRetryAutoConfiguration;
import org.lyora.leona.mvc.flow.InterceptedRequestView;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureAfter(ClientRetryAutoConfiguration.class)
@Import({RestClientBeanProcessor.class, ClientRetryAutoConfiguration.class})
class ClientAutoConfiguration {
    private final ApplicationContext applicationContext;

    public ClientAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnBean(RestTemplateBuilder.class)
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate leonaConfiguredRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
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
    @ConditionalOnClass(InterceptedRequestView.class)
    public ContextToHeaderForwarder defaultContextForwarder(ClientAutoConfigurationSource configurationSource) {
        return new DefaultContextToHeaderForwarder(configurationSource.getForwarding().getForwardedProperties());
    }
}
