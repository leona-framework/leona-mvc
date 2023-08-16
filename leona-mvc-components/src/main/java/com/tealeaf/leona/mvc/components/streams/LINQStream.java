package com.tealeaf.leona.mvc.components.streams;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LINQStream<T> implements Stream<T> {
    private final Stream<T> stream;

    @Override
    public LINQStream<T> filter(Predicate<? super T> predicate) {
        return new LINQStream<>(stream.filter(predicate));
    }

    @Override
    public <R> LINQStream<R> map(Function<? super T, ? extends R> mapper) {
        return new LINQStream<>(stream.map(mapper));
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper);
    }

    @Override
    public <R> LINQStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new LINQStream<>(stream.flatMap(mapper));
    }

    @Override
    public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }

    @Override
    public LINQStream<T> distinct() {
        return new LINQStream<>(stream.distinct());
    }

    @Override
    public LINQStream<T> sorted() {
        return new LINQStream<>(stream.sorted());
    }

    @Override
    public LINQStream<T> sorted(Comparator<? super T> comparator) {
        return new LINQStream<>(stream.sorted(comparator));
    }

    @Override
    public LINQStream<T> peek(Consumer<? super T> action) {
        return new LINQStream<>(stream.peek(action));
    }

    @Override
    public LINQStream<T> limit(long maxSize) {
        return new LINQStream<>(stream.limit(maxSize));
    }

    @Override
    public LINQStream<T> skip(long n) {
        return new LINQStream<>(stream.skip(n));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        stream.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super T> action) {
        stream.forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return stream.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream.toArray(generator);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream.collect(collector);
    }

    @Override
    public Optional<T> min(Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    @Override
    public long count() {
        return stream.count();
    }

    public long count(Predicate<T> predicate) {
        return LINQ.count(stream, predicate);
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    public Optional<T> findFirst(Predicate<T> predicate) {
        return LINQ.findFirst(stream, predicate);
    }

    @Override
    public Optional<T> findAny() {
        return stream.findAny();
    }

    public T first() {
        return LINQ.first(stream);
    }

    public T first(Predicate<T> predicate) {
        return LINQ.first(stream, predicate);
    }

    public T firstOrDefault(Predicate<T> predicate, @Nullable T fallback) {
        return LINQ.firstOrDefault(stream, predicate, fallback);
    }

    public T firstOrDefault(Predicate<T> predicate) {
        return LINQ.firstOrDefault(stream, predicate);
    }

    public T firstOrDefault() {
        return LINQ.firstOrDefault(stream);
    }

    public T firstOrGet(Predicate<T> predicate, Supplier<T> fallback) {
        return LINQ.firstOrGet(stream, predicate, fallback);
    }

    public Optional<T> findLast() {
        return LINQ.findLast(stream);
    }

    public Optional<T> findLast(Predicate<T> predicate) {
        return LINQ.findLast(stream, predicate);
    }

    public T last() {
        return LINQ.last(stream);
    }

    public T last(Predicate<T> predicate) {
        return LINQ.last(stream, predicate);
    }

    public T lastOrDefault(Predicate<T> predicate, @Nullable T fallback) {
        return LINQ.lastOrDefault(stream, predicate, fallback);
    }

    public T lastOrDefault(@Nullable T fallback) {
        return LINQ.lastOrDefault(stream, fallback);
    }

    public T lastOrDefault(Predicate<T> predicate) {
        return LINQ.lastOrDefault(stream, predicate);
    }

    public T lastOrDefault() {
        return LINQ.lastOrDefault(stream);
    }

    public T lastOrGet(Predicate<T> predicate, Supplier<T> fallback) {
        return LINQ.lastOrGet(stream, predicate, fallback);
    }

    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    @Override
    public boolean isParallel() {
        return stream.isParallel();
    }

    @Override
    public LINQStream<T> sequential() {
        return new LINQStream<>(stream.sequential());
    }

    @Override
    public LINQStream<T> parallel() {
        return new LINQStream<>(stream.parallel());
    }

    @Override
    public LINQStream<T> unordered() {
        return new LINQStream<>(stream.unordered());
    }

    @Override
    public LINQStream<T> onClose(Runnable closeHandler) {
        return new LINQStream<>(stream.onClose(closeHandler));
    }

    @Override
    public void close() {
        stream.close();
    }

    public LINQStream<T> concat(Stream<? extends T> other) {
        return LINQ.concat(stream, other);
    }

    public LINQStream<T> concat(Collection<? extends T> collection) {
        return LINQ.concat(stream, collection);
    }

    @SafeVarargs
    public final LINQStream<T> join(Stream<? extends T>... streams) {
        return LINQ.join(stream, streams);
    }

    public <R> LINQStream<R> ofType(Class<R> targetClass) {
        return LINQ.ofType(stream, targetClass);
    }

    public LINQStream<T> reverse() {
        return LINQ.reverse(stream);
    }

    public List<T> toList(Predicate<T> predicate) {
        return LINQ.toList(stream, predicate);
    }

    public <TKey, TValue> Map<TKey, TValue> toMap(Function<T, TKey> keyFunction, Function<T, TValue> valueFunction) {
        return LINQ.toMap(stream, keyFunction, valueFunction);
    }

    public <TKey, TValue> Map<TKey, TValue> toMap(Function<T, TKey> keyFunction, Function<T, TValue> valueFunction, Supplier<Map<TKey, TValue>> mapSupplier) {
        return LINQ.toMap(stream, keyFunction, valueFunction, mapSupplier);
    }
}
