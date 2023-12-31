package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.EventType;
import com.sylvona.leona.mvc.components.MdcLoggingConstants;
import com.sylvona.leona.mvc.components.captures.CaptureElement;
import com.sylvona.leona.mvc.components.captures.Captor;
import com.sylvona.leona.mvc.components.captures.DefaultCapturePlan;
import com.sylvona.leona.mvc.components.captures.PersistentCaptor;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import jakarta.servlet.http.HttpServletRequest;

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
    public Captor<HttpServletRequest> persistent(String key) {
        PersistentCaptor<HttpServletRequest> capturer = new PersistentCaptor<>(this, true);
        getCaptures().add(new CaptureElement<>(capturer, key));
        return capturer;
    }

    private String extractTraceId(HttpServletRequest servletRequest) {
        Span span = tracer.currentSpan();
        return span == null ? null : span.context().traceId();
    }
}
