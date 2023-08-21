package com.sylvona.leona.mvc.components.containers;

import java.util.function.Function;

/**
 * An interface representing an "either" type that holds either a successful result or an error.
 *
 * @param <OK> The type of the successful result.
 * @param <ERR> The type of the error, which is a subtype of Throwable.
 */
public interface Either<OK, ERR extends Throwable> {

    /**
     * Retrieves the successful result held by this "either" instance.
     *
     * @return The successful result.
     */
    OK result();

    /**
     * Retrieves the error held by this "either" instance.
     *
     * @return The error.
     */
    ERR error();

    /**
     * Produces a value based on the content of the "either" instance using the provided functions.
     * If the instance holds an error, the errorProducer function is applied to produce a value.
     * If the instance holds a successful result, the resultProducer function is applied.
     *
     * @param resultProducer The function to produce a value based on the successful result.
     * @param errorProducer The function to produce a value based on the error.
     * @param <T> The type of the produced value.
     * @return The produced value based on the content of the "either" instance.
     */
    default <T> T produce(Function<OK, T> resultProducer, Function<ERR, T> errorProducer) {
        return isError() ? errorProducer.apply(error()) : resultProducer.apply(result());
    }

    /**
     * Checks if the "either" instance holds an error.
     *
     * @return {@code true} if the instance holds an error, {@code false} if it holds a successful result.
     */
    default boolean isError() {
        return error() != null;
    }

    /**
     * Checks if the "either" instance holds a successful result.
     *
     * @return {@code true} if the instance holds a successful result, {@code false} if it holds an error.
     */
    default boolean isSuccess() {
        return error() == null;
    }
}
