package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.containers.Either;
import com.tealeaf.leona.mvc.components.containers.ExecutionView;
import com.tealeaf.leona.mvc.components.containers.Tuple;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ExecutionHandle<T> {
    T get(Function<ExecutionView<T>, T> resolver);

    default T get() {
        return get(Either::result);
    }
    Mono<T> toMono();
}
