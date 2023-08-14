package com.tealeaf.leona.mvc.components.captures;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultCapturePlan<T> implements CapturePlan<T>, CaptureRepository<T> {
    private final List<CaptureElement<T>> captures = new ArrayList<>();

    @Override
    public Capturer<T> execution(String key) {
        Capturer<T> capturer = new DefaultCapturer<>(this);
        captures.add(new CaptureElement<T>(capturer, key));
        return capturer;
    }

    @Override
    public ConditionalCapturer<T> contingent(String key) {
        ConditionalCapturer<T> capturer = new DefaultConditionalCapturer<>(this);
        captures.add(new CaptureElement<T>(capturer, key));
        return capturer;
    }

    @Override
    public List<CaptureElement<T>> getCaptures() {
        return captures;
    }

    @RequiredArgsConstructor
    public static class DefaultCapturer<T> implements Capturer<T> {
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

    static class DefaultConditionalCapturer<T> extends DefaultCapturer<T> implements ConditionalCapturer<T> {
        private Predicate<T> predicate;

        public DefaultConditionalCapturer(CapturePlan<T> plan) {
            super(plan);
        }

        @Override
        public Capturer<T> condition(@Nullable Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public boolean test(T t) {
            return predicate == null || predicate.test(t);
        }
    }
}
