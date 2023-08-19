package org.lyora.leona.mvc.services.logging;

import org.lyora.leona.mvc.components.captures.CapturePlan;
import org.lyora.leona.mvc.components.captures.CaptureRepository;
import org.lyora.leona.mvc.services.MetadataHolder;
import org.lyora.leona.mvc.services.ServiceExecutionView;

public interface ServiceCapturePlan extends CapturePlan<ServiceExecutionView<?>>, CaptureRepository<ServiceExecutionView<?>> {
    default void configureFor(MetadataHolder service) {}
}
