package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.Context;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.function.Function;


@Getter @Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class MutableServiceExecutionResult<T> implements ServiceExecutionView<T> {
    private final Context context;
    private final Duration executionTime;
    private final ServiceMetadata metadata;
    private T result;
    private Throwable error;
    private boolean isError;

    MutableServiceExecutionResult(T result, Duration executionTime, ServiceMetadata metadata, Context context) {
        this.result = result;
        this.executionTime = executionTime;
        this.metadata = metadata;
        this.context = context;
    }

    MutableServiceExecutionResult(Throwable error, Duration executionTime, ServiceMetadata metadata, Context context) {
        this.error = error;
        this.executionTime = executionTime;
        this.isError = true;
        this.metadata = metadata;
        this.context = context;
    }

    public void setResult(T result) {
        this.result = result;
        this.isError = false;
    }

    @Override
    public boolean isError() {
        return isError;
    }

    @Override
    public boolean isSuccess() {
        return !isError;
    }

    <R> MutableServiceExecutionResult<R> map(Function<T, R> mapper) {
        return new MutableServiceExecutionResult<>(context, executionTime, metadata, mapper.apply(result), error, isError);
    }
}
