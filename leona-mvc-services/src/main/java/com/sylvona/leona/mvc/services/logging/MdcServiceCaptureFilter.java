package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.components.MdcSupplier;
import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.MdcCaptureFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

/**
 * A service execution filter that captures and manages Mapped Diagnostic Context (MDC) information
 * for service method executions, following the {@link MdcCaptureFilter} contract.
 */
public class MdcServiceCaptureFilter implements ServiceExecutionFilter, MdcCaptureFilter<ServiceExecutionView<?>> {
    /**
     * The {@link CapturePlan} for capturing MDC information.
     */
    private final ServiceCapturePlan serviceCapturePlan;

    /**
     * Constructs an instance of {@code MdcServiceCaptureFilter} with the provided service capture plan.
     *
     * @param serviceCapturePlan The plan for capturing MDC information.
     */
    public MdcServiceCaptureFilter(ServiceCapturePlan serviceCapturePlan) {
        this.serviceCapturePlan = serviceCapturePlan;
    }

    /**
     * Captures MDC information after the execution of a service method.
     *
     * @param executionView The execution view representing the completed service method execution.
     */
    @Override
    public void afterExecution(ServiceExecutionView<?> executionView) {
        MdcSupplier closeables = () -> doFilter(serviceCapturePlan, executionView);
        executionView.context().put(MdcSupplier.class, closeables);
    }
}
