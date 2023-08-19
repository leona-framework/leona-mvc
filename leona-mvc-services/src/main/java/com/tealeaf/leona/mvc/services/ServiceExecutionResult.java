package org.lyora.leona.mvc.services;

public interface ServiceExecutionResult<T> extends ServiceExecutionView<T> {
    void setResult(T result);
}
