package com.sylvona.leona.mvc.components.captures;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

/**
 * A default implementation of {@link Captor}
 * @param <T> the capture item type
 */
@RequiredArgsConstructor
public class DefaultCaptor<T> implements Captor<T> {
    private final CapturePlan<T> plan;
    private Function<T, Object> function;

    @Override
    public CapturePlan<T> capture(Function<T, Object> capture) {
        this.function = capture;
        return plan;
    }

    @Override
    public Object apply(T t) {
        return function.apply(t);
    }
}