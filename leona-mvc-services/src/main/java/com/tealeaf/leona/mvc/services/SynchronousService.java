package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.Decorators;
import com.tealeaf.leona.mvc.components.VoidLike;

import java.util.List;
import java.util.function.Supplier;

public interface SynchronousService extends MetadataHolder {
    default <T> SynchronousExecutionHandle<T> handle(Supplier<T> supplier) {
        ServiceMetadata metadata = MetadataHolder.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = MetadataHolder.getExecutionFilters(this);
        return new SimpleExecutionHandle<>(supplier, executionFilters, metadata);
    }

    default SynchronousExecutionHandle<VoidLike> handle(Runnable runnable) {
        return handle(Decorators.toSupplier(runnable));
    }
}
