package com.sylvona.leona.mvc.observability.service;

import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceMetadata;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Supplier;

@RequiredArgsConstructor
class ServiceObservabilityFilter implements ServiceExecutionFilter {
    private static final String NONE = "NONE";
    private final ObservationRegistry observationRegistry;

    @Override
    public <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        return Observation.createNotStarted("leona.services.executions", observationRegistry)
                .lowCardinalityKeyValue("serviceName", serviceMetadata.serviceName())
                .lowCardinalityKeyValue("executionTarget", Objects.requireNonNullElse(serviceMetadata.executionTarget(), NONE))
                .wrap(supplier);
    }
}
