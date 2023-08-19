package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.captures.CapturePlan;
import org.lyora.leona.mvc.components.captures.CaptureRepository;
import jakarta.servlet.http.HttpServletRequest;

public interface OnRequestEntryCapturePlan extends CapturePlan<HttpServletRequest>, CaptureRepository<HttpServletRequest> {
    Capturer<HttpServletRequest> persistent(String key);
}
