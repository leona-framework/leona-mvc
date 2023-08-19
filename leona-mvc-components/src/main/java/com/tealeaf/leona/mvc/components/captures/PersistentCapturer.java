package org.lyora.leona.mvc.components.captures;

import lombok.Getter;

@Getter
public class PersistentCapturer<T> extends DefaultCapturePlan.DefaultCapturer<T> {
    private final boolean isPersistent;

    public PersistentCapturer(CapturePlan<T> plan, boolean isPersistent) {
        super(plan);
        this.isPersistent = isPersistent;
    }
}
