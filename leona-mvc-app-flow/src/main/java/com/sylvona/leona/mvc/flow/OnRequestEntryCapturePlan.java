package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;
import com.sylvona.leona.mvc.components.captures.Captor;
import jakarta.servlet.http.HttpServletRequest;

/**
 * An implementation of {@link CapturePlan} and {@link CaptureRepository} responsible for containing inbound-request captors.
 */
public interface OnRequestEntryCapturePlan extends CapturePlan<HttpServletRequest>, CaptureRepository<HttpServletRequest> {
    Captor<HttpServletRequest> persistent(String key);
}
