package com.tealeaf.leona.mvc.flow;

import brave.Span;
import brave.Tracer;
import com.tealeaf.leona.mvc.components.EventType;
import com.tealeaf.leona.mvc.components.MdcLoggingConstants;
import com.tealeaf.leona.mvc.components.captures.CaptureElement;
import com.tealeaf.leona.mvc.components.captures.DefaultCapturePlan;
import com.tealeaf.leona.mvc.components.captures.PersistentCapturer;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

class DefaultOnRequestEntryCapturePlan extends DefaultCapturePlan<HttpServletRequest> implements OnRequestEntryCapturePlan {
    private final Tracer tracer;

    public DefaultOnRequestEntryCapturePlan(Tracer tracer) {
        this.tracer = tracer;
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.ENTRY);
        persistent(MdcLoggingConstants.TRACE_ID).capture(this::extractTraceId);
        persistent(MdcLoggingConstants.ENDPOINT).capture(HttpServletRequest::getServletPath);
    }

    DefaultOnRequestEntryCapturePlan(List<CaptureElement<HttpServletRequest>> captureElements, Tracer tracer) {
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
        String trace = servletRequest.getHeader("x-b3-traceid");
        if (trace != null) return trace;

        trace = servletRequest.getHeader("trace-id");
        if (trace != null) return trace;

        trace = servletRequest.getHeader("traceId");
        if (trace != null) return trace;

        Span span = tracer.currentSpan();
        if (span == null) span = tracer.nextSpan();
        return span.context().traceIdString();
    }
}
