package com.tealeaf.leona.mvc.components.captures;

import com.tealeaf.leona.mvc.components.captures.CapturePlan;

import java.util.Objects;

public record CaptureElement<T>(CapturePlan.Capturer<T> capturer, String name) {
    public CaptureElement {
        Objects.requireNonNull(capturer, "capturer");
        Objects.requireNonNull(name, "name");
    }
}
