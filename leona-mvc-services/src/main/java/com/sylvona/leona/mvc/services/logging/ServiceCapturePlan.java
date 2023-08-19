package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;
import com.sylvona.leona.mvc.services.MetadataHolder;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

public interface ServiceCapturePlan extends CapturePlan<ServiceExecutionView<?>>, CaptureRepository<ServiceExecutionView<?>> {
    default void configureFor(MetadataHolder service) {}
}
