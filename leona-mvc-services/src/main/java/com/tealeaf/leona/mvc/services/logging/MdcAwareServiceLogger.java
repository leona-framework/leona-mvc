package com.tealeaf.leona.mvc.services.logging;

import com.tealeaf.leona.mvc.components.MdcContextSupplier;
import com.tealeaf.leona.mvc.services.ServiceExecutionView;
import org.slf4j.MDC.MDCCloseable;

import java.util.List;

public interface MdcAwareServiceLogger extends ServiceLogger {
    default void log(ServiceExecutionView<?> executionView, MdcContextSupplier contextSupplier) {
        List<MDCCloseable> closeables = contextSupplier.fillContext();
        log(executionView);
        closeables.forEach(MDCCloseable::close);
    }

    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        MdcContextSupplier contextSupplier = executionView.context().get(MdcContextSupplier.class);
        if (contextSupplier == null) log(executionView);
        else log(executionView, contextSupplier);
    }
}
