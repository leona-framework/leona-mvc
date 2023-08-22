package com.sylvona.leona.mvc.services;

import com.sylvona.leona.core.commons.containers.Either;
import com.sylvona.leona.mvc.components.containers.ExecutionView;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * An interface that functionally represents a deferred function. Variations of this interface are returned by
 * the {@link AsyncService} and {@link SynchronousService} interfaces which allow for the manipulation and chaining
 * of simple function calls.
 * <p>
 * The deferred function can be invoked via either the {@link #get()} methods or cached invocations using the {@link #cached()} methods.
 *
 * @param <T> The type of the result produced by the deferred execution.
 * @see AsyncExecutionHandle
 * @see SynchronousExecutionHandle
 */
public interface ExecutionHandle<T> {
    /**
     * Immediately executes the deferred function if not previously invoked, otherwise returning the cached result of the execution function.
     * Similar to {@link #get(Function)}, this function emits an {@link ExecutionView} containing the result of the deferred function.
     * Under the caching implementation, the {@code ExecutionView} will always be the same once cached.
     * <p>
     * Note: Calls to {@link #get(Function)} and {@link #get()} are stored in the cache, and if previously invoked,
     * will return the cached result of the deferred function.
     *
     * @param resolver The function responsible for processing the emitted {@link ExecutionView} into a standard result.
     * @return The result, or the cached result of the deferred function.
     */
    T cached(Function<ExecutionView<T>, T> resolver);

    /**
     * Immediately executes the deferred function if not previously invoked, otherwise returning the cached result of the execution function.
     * <p>
     * Note: Calls to {@link #get(Function)} and {@link #get()} are stored in the cache, and if previously invoked,
     * will return the cached result of the deferred function.
     *
     * @return The result, or the cached result of the deferred function.
     */
    T cached();

    /**
     * Immediately executes the deferred function, emitting an {@link ExecutionView} to a resolver, before returning the resolved result.
     * <p>
     * If manipulation of the {@link ExecutionView} is unnecessary, it's preferable to call {@link #get()} or to use {@link Either#result()}
     * to extract only the result.
     * <p>
     * Note: This function invokes the deferred function every time it's called. For a cached approach, use any of the {@link #cached()} methods.
     *
     * @param resolver The function responsible for processing the emitted {@link ExecutionView} into a standard result.
     * @return The result of the resolver function.
     */
    T get(Function<ExecutionView<T>, T> resolver);

    /**
     * Immediately executes the deferred function, returning its result.
     * <p>
     * Note: This function invokes the deferred function every time it's called. For a cached approach, use any of the {@link #cached()} methods.
     *
     * @return The result of the deferred function.
     */
    default T get() {
        return get(Either::result);
    }

    /**
     * Converts the deferred execution into a {@link Mono} that represents the deferred function call.
     *
     * @return A {@link Mono} that represents the deferred function call.
     */
    Mono<T> toMono();
}
