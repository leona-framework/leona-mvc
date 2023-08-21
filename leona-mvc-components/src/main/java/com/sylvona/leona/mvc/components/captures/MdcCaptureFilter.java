package com.sylvona.leona.mvc.components.captures;

import org.slf4j.MDC;

import java.util.List;
import java.util.Objects;

/**
 * Variation of a {@link CaptureFilter} that produces MDC values given a list of {@link Captor} and an input.
 * @param <T> the input item type
 */
public interface MdcCaptureFilter<T> extends CaptureFilter<T, List<MDC.MDCCloseable>> {
    @Override
    default List<MDC.MDCCloseable> doFilter(CaptureRepository<T> captures, T item) {
        return captures.getCaptures().stream()
                .filter(c -> c.captor().isCaptureable(item))
                .map(c ->  MDC.putCloseable(c.key(), Objects.toString(c.captor().apply(item)))).toList();
    }
}
