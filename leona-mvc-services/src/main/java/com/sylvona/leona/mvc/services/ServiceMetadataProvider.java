package com.sylvona.leona.mvc.services;

import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter(AccessLevel.PACKAGE)
public class ServiceMetadataProvider {
    private final MetadataHolder service;
    private final Logger logger;
    private ServiceExecutionFilterRepository executionFilterRepository;

    ServiceMetadataProvider(MetadataHolder service, ServiceExecutionFilterRepository executionFilterRepository) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(service.getClass());
        this.executionFilterRepository = executionFilterRepository;
    }

    ServiceMetadataProvider(MetadataHolder service) {
        this(service, null);
    }

    public ServiceMetadata define(String serviceName, String executionTarget) {
        return new ServiceMetadata(service, logger, serviceName, executionTarget);
    }

    public ServiceMetadata define(String serviceName) {
        return define(serviceName, null);
    }

    public ServiceMetadata defaults() {
        return define(service.getClass().getSimpleName());
    }
}
