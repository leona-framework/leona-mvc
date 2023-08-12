package com.tealeaf.leona.mvc.flow;

import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.CaptureRepository;

public interface OnRequestExitCapturePlan extends CapturePlan<InterceptedRequestView>, CaptureRepository<InterceptedRequestView> {
}
