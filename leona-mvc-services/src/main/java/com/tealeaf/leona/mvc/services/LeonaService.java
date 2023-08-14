package com.tealeaf.leona.mvc.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LeonaService extends ApplicationContextAware {
    Map<Class<?>, ServiceMetadataProvider> metadataProviders = new HashMap<>();

    ServiceMetadata metadata(ServiceMetadataProvider metadataProvider);

    static ServiceMetadata getMetadataFor(LeonaService service) {
        ServiceMetadataProvider metadataProvider =  metadataProviders.computeIfAbsent(service.getClass(), cls -> new ServiceMetadataProvider(service));
        return service.metadata(metadataProvider);
    }

    static List<ServiceExecutionFilter> getExecutionFilters(LeonaService service) {
        ServiceMetadataProvider provider = metadataProviders.get(service.getClass());
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
        metadataProviders.put(this.getClass(), new ServiceMetadataProvider(this, executionFilterRepository));
    }
}
