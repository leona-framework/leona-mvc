package com.tealeaf.leona.mvc.services.logging;

import com.tealeaf.leona.mvc.components.MdcContextSupplier;
import com.tealeaf.leona.mvc.components.Priority;
import com.tealeaf.leona.mvc.components.captures.MdcCaptureFilter;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.services.ServiceExecutionFilter;
import com.tealeaf.leona.mvc.services.ServiceExecutionView;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

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
