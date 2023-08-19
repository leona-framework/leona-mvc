package com.sylvona.leona.mvc.services;

public interface ServiceExecutionResult<T> extends ServiceExecutionView<T> {
    void setResult(T result);
}
