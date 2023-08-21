package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;
import com.sylvona.leona.mvc.services.ServiceComponent;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

/**
 * ServiceCapturePlan is an interface that represents a plan for capturing service execution data.
 * It extends the CapturePlan and CaptureRepository interfaces to provide methods for configuring
 * and managing captured service execution views.
 */
public interface ServiceCapturePlan extends CapturePlan<ServiceExecutionView<?>>, CaptureRepository<ServiceExecutionView<?>> {
    /**
     * Configures the capture plan for a specific service component.
     * This method can be overridden to customize the capture configuration for a particular service.
     *
     * @param service The service component to configure the capture plan for.
     */
    default void configureFor(ServiceComponent service) {}
}
