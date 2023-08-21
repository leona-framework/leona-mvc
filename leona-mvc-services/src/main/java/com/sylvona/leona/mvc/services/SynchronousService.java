package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.Decorators;
import com.sylvona.leona.core.commons.VoidLike;

import java.util.List;
import java.util.function.Supplier;

/**
 * SynchronousService is an interface that provides methods for creating and handling synchronous service executions.
 * The default methods implemented by this interface allow for the decoration of synchronous functions, transforming them
 * into executable tasks via a returned {@link SynchronousExecutionHandle}.
 *
 * @see SynchronousExecutionHandle
 */
public interface SynchronousService extends ServiceComponent {
    /**
     * Creates a {@link SynchronousExecutionHandle} for executing a supplied task synchronously.
     * <p>
     * This method is used to handle and manipulate the synchronous execution of a task provided as a {@link Supplier}.
     *
     * @param supplier The supplier providing the task to be executed synchronously.
     * @param <T>      The type of the result produced by the synchronous execution.
     * @return A synchronous execution handle for the supplied task.
     */
    default <T> SynchronousExecutionHandle<T> handle(Supplier<T> supplier) {
        ServiceMetadata metadata = ServiceComponent.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = ServiceComponent.getExecutionFilters(this);
        return new SimpleExecutionHandle<>(supplier, executionFilters, metadata);
    }

    /**
     * Creates a {@link SynchronousExecutionHandle} for executing a runnable task synchronously.
     * <p>
     * This method is a convenient shorthand for synchronously executing a {@link Runnable}.
     *
     * @param runnable The runnable task to be executed synchronously.
     * @return A synchronous execution handle for the runnable task.
     */
    default SynchronousExecutionHandle<VoidLike> handle(Runnable runnable) {
        return handle(Decorators.toSupplier(runnable));
    }
}

