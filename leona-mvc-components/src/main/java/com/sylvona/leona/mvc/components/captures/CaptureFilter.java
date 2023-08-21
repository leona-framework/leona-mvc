package com.sylvona.leona.mvc.components.captures;

/**
 * Filter responsible for running a series of {@link Captor} on a given item
 * @param <T> the input item type
 * @param <R> the expected return type
 */
public interface CaptureFilter<T, R> {
    /**
     * Executes all captors in a {@link CaptureRepository} on a given item
     * @param captures a {@link CaptureRepository} containing a series of {@link Captor}
     * @param item the item to run {@link Captor} on
     * @return the aggregate result
     */
    R doFilter(CaptureRepository<T> captures, T item);
}
