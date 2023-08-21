package com.sylvona.leona.mvc.components.captures;

import java.util.ArrayList;
import java.util.List;

/**
 * The standard implementation of {@link CapturePlan} backed by a {@link CaptureRepository}
 * @param <T> the target capture type
 * @see CapturePlan
 * @see CaptureRepository
 */
public class DefaultCapturePlan<T> implements CapturePlan<T>, CaptureRepository<T> {
    private final List<CaptureElement<T>> captures = new ArrayList<>();

    @Override
    public Captor<T> execution(String key) {
        Captor<T> captor = new DefaultCaptor<>(this);
        captures.add(new CaptureElement<>(captor, key));
        return captor;
    }

    @Override
    public ConditionalCaptor<T> contingent(String key) {
        ConditionalCaptor<T> capturer = new DefaultConditionalCaptor<>(this);
        captures.add(new CaptureElement<>(capturer, key));
        return capturer;
    }

    @Override
    public List<CaptureElement<T>> getCaptures() {
        return captures;
    }
}
