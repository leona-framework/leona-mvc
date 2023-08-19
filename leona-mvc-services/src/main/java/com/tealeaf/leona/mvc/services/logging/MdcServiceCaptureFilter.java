package org.lyora.leona.mvc.services.logging;

import org.lyora.leona.mvc.components.MdcContextSupplier;
import org.lyora.leona.mvc.components.captures.MdcCaptureFilter;
import org.lyora.leona.mvc.services.ServiceExecutionFilter;
import org.lyora.leona.mvc.services.ServiceExecutionView;

public class MdcServiceCaptureFilter implements ServiceExecutionFilter, MdcCaptureFilter<ServiceExecutionView<?>> {
    private final ServiceCapturePlan serviceCapturePlan;

    public MdcServiceCaptureFilter(ServiceCapturePlan serviceCapturePlan) {
        this.serviceCapturePlan = serviceCapturePlan;
    }

    @Override
    public void afterExecution(ServiceExecutionView<?> executionView) {
        MdcContextSupplier closeables = () -> doFilter(serviceCapturePlan, executionView);
        executionView.context().put(MdcContextSupplier.class, closeables);
    }
}
