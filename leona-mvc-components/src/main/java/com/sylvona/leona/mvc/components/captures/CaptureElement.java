package com.sylvona.leona.mvc.components.captures;

import java.util.Objects;

public record CaptureElement<T>(CapturePlan.Capturer<T> capturer, String name) {
    public CaptureElement {
        Objects.requireNonNull(capturer, "capturer");
        Objects.requireNonNull(name, "name");
    }
}
