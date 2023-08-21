package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

/**
 * An interface for logging method executions.
 * <p>
 * Service loggers are used to log information about method executions. They implement the
 * {@link #log(ServiceExecutionView)} method, allowing you to customize the logging behavior.
 * By default, service loggers log execution information after the service method is invoked.
 * You can extend this interface to create custom logging logic for your service methods.
 */
public interface ServiceLogger extends ServiceExecutionFilter {
    /**
     * Logs information about a method execution.
     * <p>
     * Implement this method to define the logging behavior for service method executions.
     * You can log various details such as execution time, results, and more.
     *
     * @param executionView The execution view containing information about the service method execution.
     */
    void log(ServiceExecutionView<?> executionView);

    /**
     * Overrides the default behavior to log service method executions after invocation.
     * <p>
     * By default, this method delegates to the {@link #log(ServiceExecutionView)} method to log service method executions
     * after they are invoked. You can customize the logging behavior by implementing the {@link #log(ServiceExecutionView)} method.
     *
     * @param executionView The execution view containing information about the service method execution.
     */
    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        log(executionView);
    }
}
