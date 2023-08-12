package com.tealeaf.leona.mvc.flow;

import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.CaptureRepository;
import jakarta.servlet.http.HttpServletRequest;

public interface OnRequestEntryCapturePlan extends CapturePlan<HttpServletRequest>, CaptureRepository<HttpServletRequest> {
    Capturer<HttpServletRequest> persistent(String key);
}
