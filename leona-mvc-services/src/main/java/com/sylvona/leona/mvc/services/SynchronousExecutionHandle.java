package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.ExecutionView;
import com.sylvona.leona.mvc.components.containers.Tuple;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An interface representing a handle for synchronously executing deferred functions.
 * <p>
 * This interface is returned by the {@link SynchronousService} interface, allowing the synchronous manipulation and chaining of function calls.
 *
 * @param <T> The type of the result produced by the deferred execution.
 * @see ExecutionHandle
 * @see SynchronousService
 */
public interface SynchronousExecutionHandle<T> extends ExecutionHandle<T> {
    /**
     * Concatenates this synchronous execution handle with another synchronous execution handle,
     * producing a deferred synchronous handle for both results. Concatenated executions will be simultaneously invoked.
     *
     * @param handle The other synchronous execution handle.
     * @param <T2>   The type of the result produced by the other synchronous execution handle.
     * @return A synchronous execution handle producing a tuple of the results.
     */
    <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(SynchronousExecutionHandle<T2> handle);

    /**
     * Concatenates this synchronous execution handle with a supplier, producing a deferred synchronous handle for both results.
     * Concatenated executions will be simultaneously invoked.
     *
     * @param supplier The supplier providing the other result.
     * @param <T2>     The type of the result provided by the supplier.
     * @return A synchronous execution handle producing a tuple of the results.
     */
    <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier);

    /**
     * Lazily maps the result of execution using the provided mapper function.
     *
     * @param mapper The function used to map the result.
     * @param <TR>   The type of the mapped result.
     * @return A new synchronous execution handle that emits the mapped result.
     */
    <TR> SynchronousExecutionHandle<TR> map(Function<T, TR> mapper);

    /**
     * Changes the default logging message displayed after execution.
     *
     * @param message The function responsible for generating a log message from the {@link ExecutionView}.
     * @return The modified synchronous execution handle.
     */
    SynchronousExecutionHandle<T> log(Function<ExecutionView<T>, String> message);

    /**
     * Changes the default logging message displayed after execution.
     *
     * @param message The message to be displayed after execution.
     * @return The modified synchronous execution handle.
     */
    default SynchronousExecutionHandle<T> log(String message) {
        return log(ignored -> message);
    }

    /**
     * Converts the synchronous execution handle to an {@link AsyncExecutionHandle}.
     *
     * @return An asynchronous execution handle producing the same result.
     */
    AsyncExecutionHandle<T> toAsync();
}
