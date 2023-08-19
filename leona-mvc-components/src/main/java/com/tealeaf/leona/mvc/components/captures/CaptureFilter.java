package org.lyora.leona.mvc.components.captures;

public interface CaptureFilter<T, R> {
    R doFilter(CaptureRepository<T> captures, T item);
}
