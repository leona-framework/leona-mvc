package com.sylvona.leona.mvc.components.containers;

import java.util.function.Function;

public interface Either<OK, ERR extends Throwable> {
    OK result();
    ERR error();

    default <T> T produce(Function<OK, T> resultProducer, Function<ERR, T> errorProducer) {
        return isError() ? errorProducer.apply(error()) : resultProducer.apply(result());
    }

    default boolean isError() {
        return error() != null;
    }

    default boolean isSuccess() {
        return error() == null;
    }
}
