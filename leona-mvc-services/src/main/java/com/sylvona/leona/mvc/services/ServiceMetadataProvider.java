package com.sylvona.leona.mvc.services;

import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ServiceMetadataProvider is a utility class for creating and providing metadata related to a service component.
 * It offers methods to define service metadata with various configurations and defaults.
 *
 * @see ServiceMetadata
 */
@Getter(AccessLevel.PACKAGE)
public class ServiceMetadataProvider {
    /**
     * The service related to this metadata
     */
    private final ServiceComponent service;
    /**
     * The {@link Logger} for the given service
     */
    private final Logger logger;
    /**
     * The {@link ServiceExecutionFilterRepository} containing all {@link ServiceExecutionFilter} to be attached to execution handles.
     */
    private final ServiceExecutionFilterRepository executionFilterRepository;

    /**
     * Constructs a ServiceMetadataProvider instance for the given service and execution filter repository.
     *
     * @param service                 The service component associated with this metadata provider.
     * @param executionFilterRepository The repository for service execution filters.
     */
    ServiceMetadataProvider(ServiceComponent service, ServiceExecutionFilterRepository executionFilterRepository) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(service.getClass());
        this.executionFilterRepository = executionFilterRepository;
    }

    /**
     * Constructs a ServiceMetadataProvider instance for the given service.
     *
     * @param service The service component associated with this metadata provider.
     */
    ServiceMetadataProvider(ServiceComponent service) {
        this(service, null);
    }

    /**
     * Defines and creates a ServiceMetadata instance with the specified service name and execution target.
     *
     * @param serviceName     The name of the service.
     * @param executionTarget The execution target of the service.
     * @return A ServiceMetadata instance with the specified parameters.
     */
    public ServiceMetadata define(String serviceName, String executionTarget) {
        return new ServiceMetadata(service, logger, serviceName, executionTarget);
    }

    /**
     * Defines and creates a ServiceMetadata instance with the specified service name and no execution target.
     *
     * @param serviceName The name of the service.
     * @return A ServiceMetadata instance with the specified service name.
     */
    public ServiceMetadata define(String serviceName) {
        return define(serviceName, null);
    }

    /**
     * Defines and creates a ServiceMetadata instance with the default service name derived from the service class.
     *
     * @return A ServiceMetadata instance with a default service name.
     */
    public ServiceMetadata defaults() {
        return define(service.getClass().getSimpleName());
    }
}
