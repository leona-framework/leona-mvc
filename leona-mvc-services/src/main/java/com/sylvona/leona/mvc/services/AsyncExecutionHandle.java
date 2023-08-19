package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.ExecutionView;
import com.sylvona.leona.mvc.components.containers.Tuple;

import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface AsyncExecutionHandle<T> extends ExecutionHandle<T> {
    AsyncExecutionHandle<T> addSubscriber(Consumer<ExecutionView<T>> subscriber);
    <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(AsyncExecutionHandle<T2> handle);
    <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier);
    <TR> AsyncExecutionHandle<TR> map(Function<T, TR> mapper);

    AsyncExecutionHandle<T> log(Function<ExecutionView<T>, String> message);

    default void supplyAsync() {
        supplyAsync(this::noOpConsumer);
    }

    default void supplyAsync(Executor executor) {
        supplyAsync(executor, this::noOpConsumer);
    }
    void supplyAsync(Consumer<ExecutionView<T>> callback);
    void supplyAsync(Executor executor, Consumer<ExecutionView<T>> callback);

    SynchronousExecutionHandle<T> toSync();

    private void noOpConsumer(ExecutionView<T> result) {}
}
