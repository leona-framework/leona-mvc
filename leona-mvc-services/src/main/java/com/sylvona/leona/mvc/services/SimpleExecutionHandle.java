package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

class SimpleExecutionHandle<T> implements SynchronousExecutionHandle<T> {
    private final Supplier<T> executable;
    private final List<ServiceExecutionFilter> executionFilters;
    private final ServiceMetadata serviceMetadata;
    private final Context context;
    private MutableServiceExecutionResult<T> result;

    SimpleExecutionHandle(Supplier<T> executable, List<ServiceExecutionFilter> executionFilters, ServiceMetadata serviceMetadata) {
        this.executable = executable;
        this.executionFilters = executionFilters;
        this.serviceMetadata = serviceMetadata;
        this.context = new ThreadContext();
    }

    @Override
    public T cached(Function<ExecutionView<T>, T> resolver) {
        return result != null ? resolver.apply(result) : get(resolver);
    }

    @Override
    public T cached() {
        return result != null ? result.result() : get();
    }

    @Override
    public T get(Function<ExecutionView<T>, T> resolver) {
        return resolver.apply(result = execute());
    }

    @Override
    public <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(SynchronousExecutionHandle<T2> handle) {
        return new SimpleExecutionHandle<>(() -> new Tuple<>(executable.get(), handle.get()), executionFilters, serviceMetadata);
    }

    @Override
    public <T2> SynchronousExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier) {
        return new SimpleExecutionHandle<>(() -> new Tuple<>(executable.get(), supplier.get()), executionFilters, serviceMetadata);
    }

    @Override
    public <TR> SynchronousExecutionHandle<TR> map(Function<T, TR> mapper) {
        return new SimpleExecutionHandle<>(() -> mapper.apply(executable.get()), executionFilters, serviceMetadata);
    }

    @Override
    public SynchronousExecutionHandle<T> log(Function<ExecutionView<T>, String> message) {
        context.put("LOGGING_PROVIDER", message);
        return this;
    }

    @Override
    public AsyncExecutionHandle<T> toAsync() {
        return new MonoBackedExecutionHandle<>(executable, executionFilters, serviceMetadata, context);
    }

    @Override
    public Mono<T> toMono() {
        return Mono.fromSupplier(this::execute).map(Either::result);
    }

    protected MutableServiceExecutionResult<T> execute() {
        long startTime = System.nanoTime();
        Supplier<T> supplier = executable;

        context.put(ExecutionType.class, ExecutionType.SYNCHRONOUS);
        for (ServiceExecutionFilter filter : executionFilters) {
            supplier = filter.beforeExecution(serviceMetadata, supplier, context);
        }

        try {
            return doPostExecutionFilters(new MutableServiceExecutionResult<>(supplier.get(), Duration.ofNanos(System.nanoTime() - startTime), serviceMetadata, context));
        } catch (Exception exception) {
            doPostExecutionFilters(new MutableServiceExecutionResult<>(exception, Duration.ofNanos(System.nanoTime() - startTime), serviceMetadata, context));
            throw exception;
        }
    }

    private MutableServiceExecutionResult<T> doPostExecutionFilters(MutableServiceExecutionResult<T> result) {
        for (ServiceExecutionFilter filter : executionFilters) {
            filter.afterExecution(result);
        }
        return result;
    }
}
