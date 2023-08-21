package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;

/**
 * An implementation of {@link CapturePlan} and {@link CaptureRepository} responsible for containing outbound-request captors.
 */
public interface OnRequestExitCapturePlan extends CapturePlan<InterceptedRequestView>, CaptureRepository<InterceptedRequestView> {
}
