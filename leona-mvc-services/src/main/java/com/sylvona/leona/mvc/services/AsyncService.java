package com.sylvona.leona.mvc.services;

import com.sylvona.leona.core.commons.containers.Tuple;
import com.sylvona.leona.mvc.components.Decorators;
import com.sylvona.leona.core.commons.VoidLike;
import com.sylvona.leona.mvc.components.containers.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Supplier;

/**
 * An interface that provides useful methods for asynchronous service execution capabilities.
 * The default methods implemented by this class allow for the decoration of synchronous functions, transforming and deferring them
 * via a returned {@link AsyncExecutionHandle}.
 *
 * @see AsyncExecutionHandle
 */
public interface AsyncService extends ServiceComponent {
    /**
     * Creates an {@link AsyncExecutionHandle} for asynchronously executing a supplied task.
     * <p>
     * This method enables the handling and manipulation of an asynchronous execution of a task provided as a {@link Supplier}.
     *
     * @param supplier The supplier providing the task to be executed asynchronously.
     * @param <T>      The type of the result produced by the asynchronous execution.
     * @return An asynchronous execution handle for the supplied task.
     */
    default <T> AsyncExecutionHandle<T> handleAsync(Supplier<T> supplier) {
        ServiceMetadata metadata = ServiceComponent.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = ServiceComponent.getExecutionFilters(this);
        return new MonoBackedExecutionHandle<>(supplier, executionFilters, metadata, new ThreadContext());
    }

    /**
     * Creates an {@link AsyncExecutionHandle} for asynchronously executing a runnable task.
     * <p>
     * This method is a convenient shorthand for asynchronously executing a {@link Runnable}.
     *
     * @param runnable The runnable task to be executed asynchronously.
     * @return An asynchronous execution handle for the runnable task.
     */
    default AsyncExecutionHandle<VoidLike> handleAsync(Runnable runnable) {
        return handleAsync(Decorators.toSupplier(runnable));
    }

    /**
     * Creates an {@link AsyncExecutionHandle} for asynchronously executing two supplied tasks and producing a tuple of result.
     * <p>
     * This method is used to handle the asynchronous execution of two tasks provided as suppliers. he tasks are executed simultaneously,
     * and the execution handle produces a tuple of results after both tasks complete.
     *
     * @param supplier1 The supplier providing the first task to be executed asynchronously.
     * @param supplier2 The supplier providing the second task to be executed asynchronously.
     * @param <T1>      The type of the result produced by the first asynchronous execution.
     * @param <T2>      The type of the result produced by the second asynchronous execution.
     * @return An asynchronous execution handle producing a tuple of results from the two tasks.
     */
    default <T1, T2> AsyncExecutionHandle<Tuple<T1, T2>> handleAsync(Supplier<T1> supplier1, Supplier<T2> supplier2) {
        ServiceMetadata metadata = ServiceComponent.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = ServiceComponent.getExecutionFilters(this);

        ThreadContext threadContext = new ThreadContext();
        Context simpleContext = new SimpleContext(new ThreadAwareMap<>(threadContext));

        Mono<T1> mono1 = Mono.fromSupplier(doPreExecutionFilters(supplier1, metadata, executionFilters, simpleContext)).subscribeOn(Schedulers.parallel());
        Mono<T2> mono2 = Mono.fromSupplier(doPreExecutionFilters(supplier2, metadata, executionFilters, simpleContext)).subscribeOn(Schedulers.parallel());
        Mono<Supplier<Tuple<T1, T2>>> supplierMono = Mono.zip(mono1, mono2).map(tuple -> () -> new Tuple<>(tuple.getT1(), tuple.getT2()));
        return new MonoBackedExecutionHandle<>(supplierMono, executionFilters, metadata, threadContext);
    }

    private <T> Supplier<T> doPreExecutionFilters(Supplier<T> supplier, ServiceMetadata serviceMetadata, List<ServiceExecutionFilter> executionFilters, Context context) {
        context.put(ExecutionType.class, ExecutionType.ASYNCHRONOUS);
        for (ServiceExecutionFilter executionFilter : executionFilters) {
            supplier = executionFilter.beforeExecution(serviceMetadata, supplier, context);
        }
        return supplier;
    }
}
