package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;

public interface OnRequestExitCapturePlan extends CapturePlan<InterceptedRequestView>, CaptureRepository<InterceptedRequestView> {
}
