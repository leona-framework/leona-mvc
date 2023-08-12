package com.tealeaf.leona.mvc.flow;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import com.tealeaf.leona.mvc.components.streams.LINQStream;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InterceptedRequestView {
    private final long startTime;
    private final Map<String, String> persistentMDC = new HashMap<>();

    @Getter
    private final HttpServletRequest request;

    @Getter @Setter(AccessLevel.PACKAGE)
    private HttpServletResponse response;
    private Duration executionTime;

    void put(String key, String value) {
        persistentMDC.put(key, value);
    }

    public LINQStream<Map.Entry<String, String>> getPersistentMdcEntries() {
        return LINQ.stream(persistentMDC.entrySet());
    }

    public Duration getElapsedTime() {
        if (executionTime != null) return executionTime;
        return Duration.ofNanos(System.nanoTime() - startTime);
    }

    Duration getElapsedTime(boolean finalize) {
        if (!finalize) return getElapsedTime();
        return executionTime = Duration.ofNanos(System.nanoTime() - startTime);
    }
}
