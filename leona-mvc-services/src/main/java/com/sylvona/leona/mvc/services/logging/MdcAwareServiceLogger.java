package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.components.MdcSupplier;
import com.sylvona.leona.mvc.services.ServiceExecutionView;
import org.slf4j.MDC.MDCCloseable;

import java.util.List;

/**
 * An extension of {@link ServiceLogger} that provides MDC suppliers from the current execution chain.
 */
public interface MdcAwareServiceLogger extends ServiceLogger {
    /**
     * Logs information about a service method execution with MDC context support.
     * <p>
     * Implement this method to define the logging behavior for service method executions while managing the MDC context.
     * You can log various details such as method execution time, results, and more, with the added benefit of MDC context management.
     *
     * @param executionView The execution view containing information about the service method execution.
     * @param contextSupplier The supplier responsible for filling and managing the MDC context.
     */
    default void log(ServiceExecutionView<?> executionView, MdcSupplier contextSupplier) {
        List<MDCCloseable> closeables = contextSupplier.fillContext();
        log(executionView);
        closeables.forEach(MDCCloseable::close);
    }

    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        MdcSupplier contextSupplier = executionView.context().get(MdcSupplier.class);
        if (contextSupplier == null) log(executionView);
        else log(executionView, contextSupplier);
    }
}
