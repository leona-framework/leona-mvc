package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.flow.InterceptedRequestView;
import com.sylvona.leona.mvc.services.flow.FlowSupplierDecorator;
import com.sylvona.leona.mvc.services.logging.LoggerConfiguration;
import com.sylvona.leona.mvc.services.logging.MdcServiceCaptureFilter;
import com.sylvona.leona.mvc.services.logging.ServiceCapturePlan;
import com.sylvona.leona.mvc.services.logging.ServiceLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
class LeonaServiceAutoConfiguration {
    private final ApplicationContext applicationContext;

    LeonaServiceAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnClass(InterceptedRequestView.class)
    @ConditionalOnMissingBean(FlowSupplierDecorator.class)
    public FlowSupplierDecorator getFlowDecorator() {
        return new FlowSupplierDecorator();
    }

    @Bean
    @ConditionalOnMissingBean(ServiceCapturePlan.class)
    public ServiceCapturePlan defaultServiceCapturePlan() {
        return new MdcServiceCaptures();
    }

    @Bean
    @ConditionalOnMissingBean(MdcServiceCaptureFilter.class)
    public MdcServiceCaptureFilter defaultServiceCaptureFilter(ServiceCapturePlan serviceCapturePlan) {
        return new MdcServiceCaptureFilter(serviceCapturePlan);
    }

    @Bean
    @DependsOn("defaultServiceCaptureFilter")
    @ConditionalOnProperty(value = "leona.service.logging.enabled", matchIfMissing = true)
    @ConditionalOnMissingBean(ServiceLogger.class)
    public ServiceLogger defaultServiceLogger(ServiceAutoConfigurationSource configurationSource) {
        LoggerConfiguration loggerConfiguration = configurationSource.getLogging();
        if (loggerConfiguration.getLogger() != null) return applicationContext.getBean(loggerConfiguration.getLogger());
        return new DefaultServiceLogger(loggerConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean(ServiceExecutionFilterRepository.class)
    public ServiceExecutionFilterRepository getFilterRepository(ServiceExecutionFilter[] executionFilters) {
        return new ServiceExecutionFilterRepository(executionFilters);
    }

    @Bean
    public ServiceAutoConfigurationSource getAutoConfigurationSource() {
        return new ServiceAutoConfigurationSource();
    }
}
