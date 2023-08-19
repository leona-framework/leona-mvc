package org.lyora.leona.mvc.components.containers;

import java.time.Duration;

public interface ExecutionView<T> extends Either<T, Throwable> {
    Duration executionTime();
}
