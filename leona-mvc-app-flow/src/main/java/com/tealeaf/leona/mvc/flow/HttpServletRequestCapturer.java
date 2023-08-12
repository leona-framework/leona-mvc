package com.tealeaf.leona.mvc.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.PersistentCapturer;
import com.tealeaf.leona.mvc.flow.serializers.HttpServletRequestCapturerDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.function.Function;

@JsonDeserialize(using = HttpServletRequestCapturerDeserializer.class)
public class HttpServletRequestCapturer extends PersistentCapturer<HttpServletRequest> {
    @Getter private String headerName;
    @Getter private String contextKey;

    @JsonIgnore
    private Function<HttpServletRequest, Object> function;
    @JsonIgnore
    private CapturePlan<HttpServletRequest> capturePlan;

    public HttpServletRequestCapturer(String headerName, String contextKey, boolean isPersistent) {
        super(null, isPersistent);
        this.headerName = headerName;
        this.contextKey = contextKey;
    }

    public HttpServletRequestCapturer(String headerName, String contextKey) {
        this(headerName, contextKey, false);
    }

    public HttpServletRequestCapturer(String headerAndContextName) {
        this(headerAndContextName, headerAndContextName);
    }

    @Override
    public CapturePlan<HttpServletRequest> capture(Function<HttpServletRequest, Object> capture) {
        this.function = servletRequest -> servletRequest.getHeader(headerName);
        return capturePlan;
    }

    @Override
    public boolean isCaptureable(HttpServletRequest item) {
        return function != null && item.getHeader(headerName) != null;
    }

    public void setPlan(CapturePlan<HttpServletRequest> plan) {
        this.capturePlan = plan;
    }

    @Override
    public Object apply(HttpServletRequest servletRequest) {
        return function.apply(servletRequest);
    }
}
