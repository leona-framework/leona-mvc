package org.lyora.leona.mvc.components.captures;

import java.util.List;

public interface CaptureRepository<T> {
    List<CaptureElement<T>> getCaptures();
}
