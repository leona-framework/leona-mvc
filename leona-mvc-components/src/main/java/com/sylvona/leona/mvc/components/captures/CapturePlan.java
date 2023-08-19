package com.sylvona.leona.mvc.components.captures;

import jakarta.annotation.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public interface CapturePlan<T> {
    Capturer<T> execution(String key);
    ConditionalCapturer<T> contingent(String key);

    interface Capturer<T> extends Function<T, Object> {
        CapturePlan<T> capture(Function<T, Object> capture);
        default boolean isCaptureable(T item) { return true; }
    }

    interface ConditionalCapturer<T> extends Capturer<T>, Predicate<T> {
        Capturer<T> condition(@Nullable Predicate<T> predicate);

        @Override
        default boolean isCaptureable(T item) {
            return test(item);
        }
    }
}
