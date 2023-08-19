package org.lyora.leona.mvc.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ServiceExecutionFilterRepository {
    static ServiceExecutionFilterRepository instance;
    private final List<ServiceExecutionFilter> serviceExecutionFilters;

    public ServiceExecutionFilterRepository(ServiceExecutionFilter[] serviceExecutionFilters) {
        this.serviceExecutionFilters = Collections.unmodifiableList(Arrays.asList(serviceExecutionFilters));
        instance = this;
    }

    public List<ServiceExecutionFilter> getFilters() {
        return serviceExecutionFilters;
    }
}
