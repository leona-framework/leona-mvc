package com.tealeaf.leona.mvc.services;

import io.micrometer.common.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MetadataHolder extends ApplicationContextAware {
    Map<Class<?>, ServiceMetadataProvider> METADATA_PROVIDERS = new HashMap<>();
    Map<Class<?>, ServiceMetadata> METADATA = new HashMap<>();
    default ServiceMetadata metadata(ServiceMetadataProvider metadataProvider) {
        return metadataProvider.defaults();
    }

    static ServiceMetadata getMetadataFor(MetadataHolder service) {
        Class<?> cls = service.getClass();
        ServiceMetadata metadata = METADATA.get(cls);
        if (metadata != null) return metadata;

        LeonaService serviceAnnotation = cls.getDeclaredAnnotation(LeonaService.class);
        if (serviceAnnotation != null) {
            String serviceName = serviceAnnotation.name();
            String executionTarget = serviceAnnotation.target();

            if (StringUtils.isBlank(serviceName)) serviceName = cls.getSimpleName();
            if (StringUtils.isBlank(executionTarget)) executionTarget = null;

            metadata = new ServiceMetadata(service, LoggerFactory.getLogger(cls), serviceName, executionTarget);
            METADATA.put(cls, metadata);
            return metadata;
        }
        ServiceMetadataProvider metadataProvider = METADATA_PROVIDERS.computeIfAbsent(cls, ignored -> new ServiceMetadataProvider(service));
        return service.metadata(metadataProvider);
    }

    static List<ServiceExecutionFilter> getExecutionFilters(MetadataHolder service) {
        ServiceMetadataProvider provider = METADATA_PROVIDERS.get(service.getClass());
        if (provider == null) return new ArrayList<>();
        ServiceExecutionFilterRepository filterRepository = provider.getExecutionFilterRepository();
        if (filterRepository != null) return filterRepository.getFilters();
        if (ServiceExecutionFilterRepository.instance != null) return ServiceExecutionFilterRepository.instance.getFilters();
        return new ArrayList<>();
    }

    @Override
    default void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceExecutionFilterRepository executionFilterRepository;
        try {
            executionFilterRepository = applicationContext.getBean(ServiceExecutionFilterRepository.class);
        } catch (NoSuchBeanDefinitionException ex) {
            executionFilterRepository = ServiceExecutionFilterRepository.instance;
            if (executionFilterRepository == null) executionFilterRepository = new ServiceExecutionFilterRepository(new ServiceExecutionFilter[0]);
        }
        METADATA_PROVIDERS.put(this.getClass(), new ServiceMetadataProvider(this, executionFilterRepository));
    }
}
