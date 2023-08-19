package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.containers.ThreadAware;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InterceptedRequestView extends HashMap<String, String> implements ThreadAware {
    private final long startTime;

    @Getter
    private final HttpServletRequest request;

    @Getter @Setter(AccessLevel.PACKAGE)
    private HttpServletResponse response;
    private Duration executionTime;

    public Duration getElapsedTime() {
        if (executionTime != null) return executionTime;
        return Duration.ofNanos(System.nanoTime() - startTime);
    }

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
}
