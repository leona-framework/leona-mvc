package com.sylvona.leona.mvc.services;

import com.sylvona.leona.core.commons.containers.Either;
import com.sylvona.leona.core.commons.containers.ExecutionView;
import com.sylvona.leona.core.commons.containers.Tuple;
import com.sylvona.leona.mvc.components.containers.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class MonoBackedExecutionHandle<T> implements AsyncExecutionHandle<T> {
    private final List<ServiceExecutionFilter> executionFilters;
    private final ServiceMetadata serviceMetadata;
    private final Context context;
    private Supplier<T> executable;
    private Mono<MutableServiceExecutionResult<T>> mono;
    private MutableServiceExecutionResult<T> result;

    MonoBackedExecutionHandle(Supplier<T> executable, List<ServiceExecutionFilter> executionFilters, ServiceMetadata serviceMetadata, Context context) {
        this(executionFilters, serviceMetadata, new SimpleContext(new ThreadAwareMap<>(context)));
        this.executable = executable;
        this.mono = setupMono(() -> Mono.fromSupplier(() -> doPreExecutionFilters(executable)));
    }

    // This is one of the two constructors used by Async Service;
    MonoBackedExecutionHandle(Mono<Supplier<T>> mono, List<ServiceExecutionFilter> executionFilters, ServiceMetadata serviceMetadata, Context context) {
        this(executionFilters, serviceMetadata, new SimpleContext(context.asMap()));
        this.executable = () -> mono.map(Supplier::get).block();
        this.mono = setupMono(() -> mono);
    }

    private MonoBackedExecutionHandle(List<ServiceExecutionFilter> executionFilters, ServiceMetadata serviceMetadata, Supplier<T> executable, Mono<MutableServiceExecutionResult<T>> mono, Context context) {
        this(executionFilters, serviceMetadata, context);
        this.executable = executable;
        this.mono = mono;
    }

    private MonoBackedExecutionHandle(List<ServiceExecutionFilter> executionFilters, ServiceMetadata serviceMetadata, Context context) {
        this.executionFilters = executionFilters;
        this.serviceMetadata = serviceMetadata;
        this.context = context;
    }

    @Override
    public AsyncExecutionHandle<T> addSubscriber(Consumer<ExecutionView<T>> subscriber) {
        this.mono = mono.doOnNext(subscriber);
        return this;
    }

    @Override
    public <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(AsyncExecutionHandle<T2> handle) {
        Scheduler parallel = Schedulers.parallel();
        Mono<MutableServiceExecutionResult<Tuple<T, T2>>> executionViewMono = this.mono.subscribeOn(parallel).zipWith(handle.toMono().subscribeOn(parallel))
                .map(t -> t.getT1().map(result -> new Tuple<>(result, t.getT2())));

        return new MonoBackedExecutionHandle<>(executionFilters, serviceMetadata, () -> new Tuple<>(executable.get(), handle.get()), executionViewMono, context);
    }

    @Override
    public <T2> AsyncExecutionHandle<Tuple<T, T2>> concat(Supplier<T2> supplier) {
        Scheduler parallel = Schedulers.parallel();

        Mono<MutableServiceExecutionResult<Tuple<T, T2>>> executionViewMono = this.mono.subscribeOn(parallel).zipWith(Mono.fromSupplier(supplier).subscribeOn(parallel))
                .map(t -> t.getT1().map(result -> new Tuple<>(result, t.getT2())));

        return new MonoBackedExecutionHandle<>(executionFilters, serviceMetadata, () -> new Tuple<>(executable.get(), supplier.get()), executionViewMono, context);
    }

    @Override
    public <TR> AsyncExecutionHandle<TR> map(Function<T, TR> mapper) {
        return new MonoBackedExecutionHandle<>(executionFilters, serviceMetadata, () -> mapper.apply(executable.get()), mono.map(ev -> ev.map(mapper)), context);
    }

    @Override
    public AsyncExecutionHandle<T> log(Function<ExecutionView<T>, String> message) {
        context.put("LOGGING_PROVIDER", message);
        return this;
    }

    @Override
    public void supplyAsync(Consumer<ExecutionView<T>> callback) {
        supplyAsync(Schedulers.parallel(), callback);
    }

    @Override
    public void supplyAsync(Executor executor, Consumer<ExecutionView<T>> callback) {
        supplyAsync(Schedulers.fromExecutor(executor), callback);
    }

    private void supplyAsync(Scheduler scheduler, Consumer<ExecutionView<T>> callback) {
        this.mono.subscribeOn(scheduler).doOnNext(callback).subscribe();
    }

    @Override
    public T cached(Function<ExecutionView<T>, T> resolver) {
        return result != null ? resolver.apply(result) : get(resolver);
    }

    @Override
    public T cached() {
        return result != null ? result.left() : get();
    }

    @Override
    public T get(Function<ExecutionView<T>, T> resolver) {
        return mono.map(r -> resolver.apply(result = r)).block();
    }

    @Override
    public SynchronousExecutionHandle<T> toSync() {
        return new SimpleExecutionHandle<>(executable, executionFilters, serviceMetadata);
    }

    @Override
    public Mono<T> toMono() {
        return mono.map(Either::left);
    }

    private Mono<MutableServiceExecutionResult<T>> setupMono(Supplier<Mono<Supplier<T>>> monoSupplier) {
        return Mono.defer(monoSupplier)
                .map(Supplier::get)
                .materialize()
                .timed()
                .map(timedSignal -> {
                    Duration executionTime = timedSignal.elapsed();
                    Signal<T> signal = timedSignal.get();
                    if (signal.hasError()) return doPostExecutionFilters(new MutableServiceExecutionResult<>(signal.getThrowable(), executionTime, serviceMetadata, context));
                    return doPostExecutionFilters(new MutableServiceExecutionResult<>(signal.get(), executionTime, serviceMetadata, context));
                });
    }

    private Supplier<T> doPreExecutionFilters(Supplier<T> supplier) {
        context.put(ExecutionType.class, ExecutionType.ASYNCHRONOUS);
        for (ServiceExecutionFilter executionFilter : executionFilters) {
            supplier = executionFilter.beforeExecution(serviceMetadata, supplier, context);
        }
        return supplier;
    }

    private MutableServiceExecutionResult<T> doPostExecutionFilters(MutableServiceExecutionResult<T> result) {
        for (ServiceExecutionFilter filter : executionFilters) {
            filter.afterExecution(result);
        }
        return result;
    }
}
