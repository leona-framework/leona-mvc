package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.components.containers.ExecutionView;

/**
 * An interface representing a view of the execution of a service method.
 * It extends the {@link ExecutionView} interface and provides additional methods
 * to retrieve metadata and context information related to the service execution.
 *
 * @param <T> The type of the result produced by the service execution.
 * @see ExecutionView
 * @see ServiceMetadata
 * @see Context
 */
public interface ServiceExecutionView<T> extends ExecutionView<T> {
    /**
     * Retrieves the metadata associated with the executed service method.
     *
     * @return The metadata of the service method.
     */
    ServiceMetadata metadata();

    /**
     * Retrieves the context associated with the executed service method.
     * The context holds additional information related to the service execution.
     *
     * @return The context of the service method execution.
     */
    Context context();
}