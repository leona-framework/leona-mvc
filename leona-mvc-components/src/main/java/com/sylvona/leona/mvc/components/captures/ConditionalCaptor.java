package com.sylvona.leona.mvc.components.captures;

import jakarta.annotation.Nullable;

import java.util.function.Predicate;

/**
 * A simple captor that only triggers based on a specific condition.
 * @param <T> the input type
 * @see Captor
 */
public interface ConditionalCaptor<T> extends Captor<T>, Predicate<T> {
    /**
     * The condition to check if a capture can be applied to the provided item.
     * @param predicate the condition.
     * @return the generic {@link Captor} superclass.
     */
    Captor<T> condition(@Nullable Predicate<T> predicate);

    @Override
    default boolean isCaptureable(T item) {
        return test(item);
    }
}
