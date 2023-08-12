package com.tealeaf.leona.mvc.components.captures;

public interface CaptureProvider {
    <T> CapturePlan<T> createCaptures(CapturePlan<T> capturePlan, Object target);
}
