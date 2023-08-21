package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.Context;

import java.util.function.Supplier;

/**
 * An interface used by an {@link ExecutionHandle} that defines methods for intercepting and deferred executions before and after their invocations.
 * <p>
 * Service execution filters can be implemented to perform pre-execution operations, such as modifying parameters or context,
 * and post-execution operations, such as logging or modifying the execution view.
 * <p>
 * <b>Important:</b> beans implementing this interface are automatically wired into the execution of {@link ExecutionHandle}.
 * This means that implementers must be valid Spring beans to be considered during service execution.
 */
public interface ServiceExecutionFilter {
    /**
     * Intercepts and performs pre-execution operations before invocation of the deferred method via an {@link ExecutionHandle}.
     * <p>
     * This method allows you to modify the service supplier or the execution context before the method is invoked.
     * By returning a modified supplier, you can manipulate the parameters or behavior of the service method execution.
     *
     * @param serviceMetadata The metadata of the service method being executed.
     * @param supplier        The original supplier of the service method to be executed.
     * @param context         The context of the current execution.
     * @param <T>             The type of the result produced by the service method.
     * @return The modified supplier for the service method.
     */
    default <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        return supplier;
    }

    /**
     * Performs post-execution operations after invocation of the deferred method via an {@link ExecutionHandle}.
     * <p>
     * This method is called after the service method has been executed. It allows you to process the execution result
     * or perform any necessary cleanup or logging operations based on the execution view.
     *
     * @param executionView The execution view containing the result and metadata of the service method execution.
     */
    default void afterExecution(ServiceExecutionView<?> executionView) {
    }
}
