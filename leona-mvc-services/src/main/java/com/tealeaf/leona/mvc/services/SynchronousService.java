package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.Decorators;
import com.tealeaf.leona.mvc.components.VoidLike;

import java.util.List;
import java.util.function.Supplier;

public interface SynchronousService extends LeonaService {
    default <T> SynchronousExecutionHandle<T> handle(Supplier<T> supplier) {
        ServiceMetadata metadata = LeonaService.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = LeonaService.getExecutionFilters(this);
        return new SimpleExecutionHandle<>(supplier, executionFilters, metadata);
    }

    default SynchronousExecutionHandle<VoidLike> handle(Runnable runnable) {
        return handle(Decorators.toSupplier(runnable));
    }
}
