package com.tealeaf.leona.mvc.components.captures;

import org.slf4j.MDC;

import java.util.List;
import java.util.Objects;

public interface MdcCaptureFilter<T> extends CaptureFilter<T, List<MDC.MDCCloseable>> {
    @Override
    default List<MDC.MDCCloseable> doFilter(CaptureRepository<T> captures, T item) {
        return captures.getCaptures().stream()
                .filter(c -> c.capturer().isCaptureable(item))
                .map(c ->  MDC.putCloseable(c.name(), Objects.toString(c.capturer().apply(item)))).toList();
    }
}
