package com.sylvona.leona.mvc.components.captures;

import jakarta.annotation.Nullable;

import java.util.function.Predicate;

/**
 * A generic implementation of {@link DefaultConditionalCaptor}
 * @param <T> the capture item type
 */
public class DefaultConditionalCaptor<T> extends DefaultCaptor<T> implements ConditionalCaptor<T> {
    private Predicate<T> predicate;

    public DefaultConditionalCaptor(CapturePlan<T> plan) {
        super(plan);
    }

    @Override
    public Captor<T> condition(@Nullable Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean test(T t) {
        return predicate == null || predicate.test(t);
    }
}