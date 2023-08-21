package com.sylvona.leona.mvc.components.captures;

/**
 * Represents a series of capturers that perform actions upon a given input to produce an aggregated output.
 * @param <T> the input type
 * @see Captor
 */
public interface CapturePlan<T> {
    /**
     * Creates a {@link Captor} bound to the provided key.
     * @param key the key to bind the capture to.
     * @return a {@link Captor} bound to the given key.
     */
    Captor<T> execution(String key);

    /**
     * Creates a {@link ConditionalCaptor} bound to the provided key.
     * @param key the key to the bind the capture to.
     * @return a {@link ConditionalCaptor} bound to the given key.
     */
    ConditionalCaptor<T> contingent(String key);
}
