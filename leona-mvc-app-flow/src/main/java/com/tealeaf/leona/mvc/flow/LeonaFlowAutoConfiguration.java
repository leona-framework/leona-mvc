package com.tealeaf.leona.mvc.flow;

import io.micrometer.tracing.brave.bridge.BraveTracer;
import org.springframework.boot.actuate.autoconfigure.tracing.ConditionalOnEnabledTracing;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ConditionalOnEnabledTracing
@ConditionalOnClass(BraveTracer.class)
@EnableConfigurationProperties(LeonaFlowAutoConfigurationSource.class)
class LeonaFlowAutoConfiguration implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    public LeonaFlowAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(OnRequestEntryCapturePlan.class)
    public OnRequestEntryCapturePlan defaultRequestEntryCapturePlan(BraveTracer tracer) {
        return new DefaultOnRequestEntryCapturePlan(tracer);
    }

    @Bean
    @ConditionalOnProperty(value = "leona.flow.request-interceptor.use-default-capturers", matchIfMissing = true)
    @ConditionalOnMissingBean(OnRequestExitCapturePlan.class)
    public OnRequestExitCapturePlan defaultRequestExitCapturePlan() {
        return new DefaultOnRequestExitCapturePlan();
    }

    @Bean
    @ConditionalOnProperty(value = "leona.flow.request-interceptor.use-default-capturers", havingValue = "false")
    @ConditionalOnMissingBean(OnRequestExitCapturePlan.class)
    public OnRequestExitCapturePlan noOpExitCapturePlan() {
        return new NoOpExitCapturePlan();
    }

    @Bean
    @ConditionalOnProperty(value = "leona.flow.request-interceptor.enabled", matchIfMissing = true)
    @ConditionalOnMissingBean(LeonaFlowRequestInterceptor.class)
    public LeonaFlowRequestInterceptor defaultRequestInterceptor(
            LeonaFlowAutoConfigurationSource autoConfigurationSource,
            OnRequestEntryCapturePlan entryCapturePlan,
            OnRequestExitCapturePlan exitCapturePlan,
            BraveTracer tracer
    ) {
        return new SharedContextRequestInterceptor(autoConfigurationSource.getRequestInterceptor(), entryCapturePlan, exitCapturePlan, tracer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        applicationContext.getBeansOfType(LeonaFlowRequestInterceptor.class).values().forEach(registry::addInterceptor);
    }
}
