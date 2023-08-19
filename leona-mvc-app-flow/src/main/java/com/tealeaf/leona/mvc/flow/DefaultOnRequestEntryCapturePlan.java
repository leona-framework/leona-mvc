package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.EventType;
import org.lyora.leona.mvc.components.MdcLoggingConstants;
import org.lyora.leona.mvc.components.captures.CaptureElement;
import org.lyora.leona.mvc.components.captures.DefaultCapturePlan;
import org.lyora.leona.mvc.components.captures.PersistentCapturer;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.util.List;

class DefaultOnRequestEntryCapturePlan extends DefaultCapturePlan<HttpServletRequest> implements OnRequestEntryCapturePlan {
    private final BraveTracer tracer;

    public DefaultOnRequestEntryCapturePlan(BraveTracer tracer) {
        this.tracer = tracer;
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.ENTRY);
        persistent(MdcLoggingConstants.TRACE_ID).capture(this::extractTraceId);
        persistent(MdcLoggingConstants.ENDPOINT).capture(HttpServletRequest::getServletPath);
    }

    DefaultOnRequestEntryCapturePlan(List<CaptureElement<HttpServletRequest>> captureElements, BraveTracer tracer) {
        this.tracer = tracer;
        getCaptures().clear();
        getCaptures().addAll(captureElements);
    }

    @Override
    public Capturer<HttpServletRequest> persistent(String key) {
        PersistentCapturer<HttpServletRequest> capturer = new PersistentCapturer<>(this, true);
        getCaptures().add(new CaptureElement<>(capturer, key));
        return capturer;
    }

    private String extractTraceId(HttpServletRequest servletRequest) {
        Span span = tracer.currentSpan();
        return span == null ? null : span.context().traceId();
    }
}
