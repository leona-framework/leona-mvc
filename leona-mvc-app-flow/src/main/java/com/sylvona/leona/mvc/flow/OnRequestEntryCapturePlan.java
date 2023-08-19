package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;
import jakarta.servlet.http.HttpServletRequest;

public interface OnRequestEntryCapturePlan extends CapturePlan<HttpServletRequest>, CaptureRepository<HttpServletRequest> {
    Capturer<HttpServletRequest> persistent(String key);
}
