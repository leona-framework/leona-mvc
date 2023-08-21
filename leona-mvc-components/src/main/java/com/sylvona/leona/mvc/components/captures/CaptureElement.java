package com.sylvona.leona.mvc.components.captures;

import java.util.Objects;

/**
 * A record representing a captor and it's bound key
 * @param captor the capture to bind
 * @param key the key to bind
 * @param <T> the target item of the captor
 */
public record CaptureElement<T>(Captor<T> captor, String key) {
    /**
     * Validation constructor for the element
     * @param captor the bound captor
     * @param key the bound key
     */
    public CaptureElement {
        Objects.requireNonNull(captor, "captor");
        Objects.requireNonNull(key, "key");
    }
}