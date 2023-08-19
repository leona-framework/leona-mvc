package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.captures.CapturePlan;
import org.lyora.leona.mvc.components.captures.CaptureRepository;

public interface OnRequestExitCapturePlan extends CapturePlan<InterceptedRequestView>, CaptureRepository<InterceptedRequestView> {
}
