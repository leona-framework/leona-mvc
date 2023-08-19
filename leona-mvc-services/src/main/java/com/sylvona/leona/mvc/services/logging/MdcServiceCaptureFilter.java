package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.components.MdcContextSupplier;
import com.sylvona.leona.mvc.components.captures.MdcCaptureFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

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
