package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.ExecutionView;
import com.sylvona.leona.mvc.components.containers.Tuple;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An interface representing a handle for asynchronously executing deferred functions.
 * <p>
 * This interface is returned by the {@link AsyncService} interface, allowing the asynchronous manipulation and chaining of function calls.
 *
 * @param <T> The type of the result produced by the deferred execution.
 * @see ExecutionHandle
 * @see AsyncService
 */
public interface AsyncExecutionHandle<T> extends ExecutionHandle<T> {
    /**
     * Adds a subscriber to the asynchronous execution handle, allowing observation of the emitted execution result.
     * <p>
     * Note: Subscribers are notified only of the <b>completion</b> of the deferred function.
     * Cache retrievals via {@link #cached()} will not emit events to the handle's subscribers.
     *
     * @param subscriber The consumer that receives the {@link ExecutionView} of the execution result.
     * @return The modified asynchronous execution handle.
     */
    AsyncExecutionHandle<T> addSubscriber(Consumer<ExecutionView<T>> subscriber);

    /**
     * Concatenates this asynchronous execution handle with another asynchronous execution handle,
     * producing a deferred asynchronous handle for both results. Concatenated executions will be simultaneously invoked, with further
     * processing waiting until all concatenated executions have completed.
     *
     * @param handle The other asynchronous execution handle.
     * @param <T2>   The type of the result produced by the other asynchronous execution handle.
     * @return An asynchronous execution handle producing a tuple of the results.
     */
    <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(AsyncExecutionHandle<T2> handle);

    /**
     * Concatenates this asynchronous execution handle with a supplier, producing a deferred asynchronous handle for both results.
     * Concatenated executions will be simultaneously invoked, with further processing waiting until all concatenated executions have completed.
     *
     * @param supplier The supplier providing the other result.
     * @param <T2>     The type of the result provided by the supplier.
     * @return An asynchronous execution handle producing a tuple of the results.
     */
    <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier);

    /**
     * Lazily maps the result of execution using the provided mapper function.
     *
     * @param mapper The function used to map the result.
     * @param <TR>   The type of the mapped result.
     * @return A new asynchronous execution handle that emits the mapped result.
     */
    <TR> AsyncExecutionHandle<TR> map(Function<T, TR> mapper);

    /**
     * Changes the default logging message displayed after execution.
     *
     * @param message The function responsible for generating a log message from the {@link ExecutionView}.
     * @return The modified asynchronous execution handle.
     */
    AsyncExecutionHandle<T> log(Function<ExecutionView<T>, String> message);

    /**
     * Changes the default logging message displayed after execution.
     *
     * @param message The message to be displayed after execution.
     * @return The modified asynchronous execution handle.
     */
    default AsyncExecutionHandle<T> log(String message) {
        return log(ignored -> message);
    }

    /**
     * Initiates asynchronous execution of the deferred function without invoking any callback.
     */
    default void supplyAsync() {
        supplyAsync(this::noOpConsumer);
    }

    /**
     * Initiates asynchronous execution of the deferred function using a specified {@link Executor}, without invoking any callback.
     *
     * @param executor The executor to use for asynchronous execution.
     */
    default void supplyAsync(Executor executor) {
        supplyAsync(executor, this::noOpConsumer);
    }

    /**
     * Initiates asynchronous execution of the deferred function and invokes the provided callback upon completion.
     *
     * @param callback The consumer responsible for processing the emitted {@link ExecutionView}.
     */
    void supplyAsync(Consumer<ExecutionView<T>> callback);

    /**
     * Initiates asynchronous execution of the deferred function using a specified {@link Executor} and invokes the provided callback upon completion.
     *
     * @param executor The executor to use for asynchronous execution.
     * @param callback The consumer responsible for processing the emitted {@link ExecutionView}.
     */
    void supplyAsync(Executor executor, Consumer<ExecutionView<T>> callback);

    /**
     * Converts the asynchronous execution handle to a {@link SynchronousExecutionHandle}.
     *
     * @return A synchronous execution handle producing the same result.
     */
    SynchronousExecutionHandle<T> toSync();

    /**
     * A utility method that serves as a no-op consumer for execution results.
     *
     * @param result The execution result.
     */
    private void noOpConsumer(ExecutionView<T> result) {}
}
