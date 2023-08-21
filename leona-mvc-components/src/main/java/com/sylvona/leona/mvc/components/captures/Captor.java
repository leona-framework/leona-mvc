package com.sylvona.leona.mvc.components.captures;

import java.util.function.Function;

/**
 * A simple captor that produces a result based on a simple {@link Function}.
 * @param <T> the input type
 */
public interface Captor<T> extends Function<T, Object> {
    /**
     * Sets the {@link Function} to be used during capturing.
     * @param capture the {@link Function} to use for capturing.
     * @return the {@link CapturePlan} used to make this captor.
     */
    CapturePlan<T> capture(Function<T, Object> capture);

    /**
     * Whether the given item is eligible for capturing.
     * @param item the item to check for eligibility.
     * @return true if the capture function should be run
     */
    default boolean isCaptureable(T item) { return true; }
}