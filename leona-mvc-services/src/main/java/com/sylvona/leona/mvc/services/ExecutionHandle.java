package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.Either;
import com.sylvona.leona.mvc.components.containers.ExecutionView;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ExecutionHandle<T> {
    T get(Function<ExecutionView<T>, T> resolver);

    default T get() {
        return get(Either::result);
    }

    Mono<T> toMono();
}
