package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.containers.Context;

import java.util.function.Supplier;

public interface ServiceExecutionFilter {
    default <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        return supplier;
    }

    default void afterExecution(ServiceExecutionView<?> executionView) {
    }
}
