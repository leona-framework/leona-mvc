package com.sylvona.leona.mvc.components.captures;

import java.util.List;

/**
 * An object containing a list of {@link CaptureElement}
 * @param <T> the target item of the captors
 */
public interface CaptureRepository<T> {
    /**
     * Gets all stored {@link CaptureElement}
     * @return the stored collection of {@link CaptureElement}
     */
    List<CaptureElement<T>> getCaptures();
}
