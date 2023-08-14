package com.tealeaf.leona.mvc.services.logging;

import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.CaptureRepository;
import com.tealeaf.leona.mvc.services.LeonaService;
import com.tealeaf.leona.mvc.services.ServiceExecutionView;

public interface ServiceCapturePlan extends CapturePlan<ServiceExecutionView<?>>, CaptureRepository<ServiceExecutionView<?>> {
    default void configureFor(LeonaService service) {}
}
