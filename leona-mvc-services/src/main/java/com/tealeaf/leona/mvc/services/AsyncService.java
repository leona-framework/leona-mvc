package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.Decorators;
import com.tealeaf.leona.mvc.components.VoidLike;
import com.tealeaf.leona.mvc.components.containers.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Supplier;

public interface AsyncService extends LeonaService {
    default <T> AsyncExecutionHandle<T> handleAsync(Supplier<T> supplier) {
        ServiceMetadata metadata = LeonaService.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = LeonaService.getExecutionFilters(this);
        return new MonoBackedExecutionHandle<>(supplier, executionFilters, metadata, new ThreadContext());
    }

    default AsyncExecutionHandle<VoidLike> handleAsync(Runnable runnable) {
        return handleAsync(Decorators.toSupplier(runnable));
    }

    default <T1, T2> AsyncExecutionHandle<Tuple<T1, T2>> handleAsync(Supplier<T1> supplier1, Supplier<T2> supplier2) {
        ServiceMetadata metadata = LeonaService.getMetadataFor(this);
        List<ServiceExecutionFilter> executionFilters = LeonaService.getExecutionFilters(this);

        ThreadContext threadContext = new ThreadContext();
        Context simpleContext = new SimpleContext(new ThreadAwareMap<>(threadContext));

        Mono<T1> mono1 = Mono.fromSupplier(doPreExecutionFilters(supplier1, metadata, executionFilters, simpleContext)).subscribeOn(Schedulers.parallel());
        Mono<T2> mono2 = Mono.fromSupplier(doPreExecutionFilters(supplier2, metadata, executionFilters, simpleContext)).subscribeOn(Schedulers.parallel());
        Mono<Supplier<Tuple<T1, T2>>> supplierMono = Mono.zip(mono1, mono2).map(tuple -> () -> new Tuple<>(tuple.getT1(), tuple.getT2()));
        return new MonoBackedExecutionHandle<>(supplierMono, executionFilters, metadata, threadContext);
    }

    private <T> Supplier<T> doPreExecutionFilters(Supplier<T> supplier, ServiceMetadata serviceMetadata, List<ServiceExecutionFilter> executionFilters, Context context) {
        for (ServiceExecutionFilter executionFilter : executionFilters) {
            supplier = executionFilter.beforeExecution(serviceMetadata, supplier, context);
        }
        return supplier;
    }
}
