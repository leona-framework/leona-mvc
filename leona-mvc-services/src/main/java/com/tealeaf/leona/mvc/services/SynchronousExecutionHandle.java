package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.containers.ExecutionView;
import com.tealeaf.leona.mvc.components.containers.Tuple;

import java.util.function.Function;
import java.util.function.Supplier;

public interface SynchronousExecutionHandle<T> extends ExecutionHandle<T> {
    <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(SynchronousExecutionHandle<T2> handle);
    <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier);
    <TR> SynchronousExecutionHandle<TR> map(Function<T, TR> mapper);
    SynchronousExecutionHandle<T> log(Function<ExecutionView<T>, String> message);
    AsyncExecutionHandle<T> toAsync();
}
