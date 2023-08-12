package com.tealeaf.leona.mvc.flow;

import brave.Tracer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableAspectJAutoProxy
@Import({BraveAutoConfiguration.class})
public class LeonaFlowAutoConfiguration implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    public LeonaFlowAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(OnRequestEntryCapturePlan.class)
    public OnRequestEntryCapturePlan defaultRequestEntryCapturePlan(Tracer tracer) {
        return new DefaultOnRequestEntryCapturePlan(tracer);
    }

    @Bean
    @ConditionalOnMissingBean(OnRequestExitCapturePlan.class)
    public OnRequestExitCapturePlan defaultRequestExitCapturePlan() {
        return new DefaultOnRequestExitCapturePlan();
    }

    @Bean
    @ConditionalOnProperty(value = "leona.flow.request-interceptor.enabled", matchIfMissing = true)
    @ConditionalOnMissingBean(LeonaFlowRequestInterceptor.class)
    public LeonaFlowRequestInterceptor defaultRequestInterceptor(
            LeonaFlowAutoConfigurationSource autoConfigurationSource,
            OnRequestEntryCapturePlan entryCapturePlan,
            OnRequestExitCapturePlan exitCapturePlan,
            Tracer tracer
    ) {
        return new SharedContextRequestInterceptor(autoConfigurationSource.getRequestInterceptor(), entryCapturePlan, exitCapturePlan, tracer);
    }

    @Bean
    public LeonaFlowAutoConfigurationSource getLoggingSource() {
        return new LeonaFlowAutoConfigurationSource();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        applicationContext.getBeansOfType(LeonaFlowRequestInterceptor.class).values().forEach(registry::addInterceptor);
    }
}
