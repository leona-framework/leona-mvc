package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.containers.ThreadAware;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores information for an inbound request, including any key-values put into MDC
 */
@RequiredArgsConstructor
public class InterceptedRequestView extends HashMap<String, String> implements ThreadAware {
    private final long startTime;
    /**
     * The request associated with this view.
     */
    private final HttpServletRequest request;
    /**
     * The response associated with the inbound request (can be null).
     */
    private HttpServletResponse response;
    private Duration executionTime;

    /**
     * Returns a {@link Duration} representing the amount of time since the inbound-request was intercepted.
     * @return the {@link Duration} since an inbound-request
     */
    public Duration getElapsedTime() {
        if (executionTime != null) return executionTime;
        return Duration.ofNanos(System.nanoTime() - startTime);
    }

    /**
     * Returns a {@link Duration} representing the amount of time since the inbound-request was intercepted.
     * @param finalize if the {@link Duration} should be captured and frozen
     * @return the {@link Duration} since an inbound-request
     */
    Duration getElapsedTime(boolean finalize) {
        if (!finalize) return getElapsedTime();
        return executionTime = Duration.ofNanos(System.nanoTime() - startTime);
    }

    @Override
    public void onThreadCopy() {
        for (Map.Entry<String, String> persistentEntries : entrySet()) {
            MDC.put(persistentEntries.getKey(), persistentEntries.getValue());
        }
    }

    /**
     * The request associated with this view.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * The response associated with the inbound request (can be null).
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    void setResponse(@Nullable HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }
}
