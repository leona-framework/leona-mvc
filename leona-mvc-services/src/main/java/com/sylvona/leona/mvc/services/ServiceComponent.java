package com.sylvona.leona.mvc.services;

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

/**
 * A utility interface used by the {@link AsyncService} and {@link SynchronousService} interfaces to help manage
 * the metadata and filters of their implementers.
 *
 * @see AsyncService
 * @see SynchronousService
 */
public interface ServiceComponent extends ApplicationContextAware {
    /**
     * A map containing metadata providers of each implementer of this interface.
     */
    Map<Class<? extends ServiceComponent>, ServiceMetadataProvider> METADATA_PROVIDERS = new HashMap<>();
    /**
     * A meta containing the metadata of each implementer of this interface.
     */
    Map<Class<? extends ServiceComponent>, ServiceMetadata> METADATA = new HashMap<>();

    /**
     * Returns the metadata for the implementing service.
     *
     * @param metadataProvider a useful builder class for constructing service metadata.
     * @return the metadata for the implementing service.
     */
    default ServiceMetadata metadata(ServiceMetadataProvider metadataProvider) {
        return metadataProvider.defaults();
    }

    /**
     * Returns the metadata associated with a given service.
     * @param service an implementation of {@link ServiceComponent}.
     * @return the metadata for the given service.
     */
    static ServiceMetadata getMetadataFor(ServiceComponent service) {
        Class<? extends ServiceComponent> cls = service.getClass();
        ServiceMetadata metadata = METADATA.get(cls);
        if (metadata != null) return metadata;

        MetaService serviceAnnotation = cls.getDeclaredAnnotation(MetaService.class);
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

    /**
     * Returns the list of execution filters associated with a service.
     * @param service an implementation of {@link ServiceComponent}
     * @return the list of execution filters associated with the service.
     */
    static List<ServiceExecutionFilter> getExecutionFilters(ServiceComponent service) {
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
