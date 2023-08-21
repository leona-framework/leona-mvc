package com.sylvona.leona.mvc.components;

import org.slf4j.MDC.MDCCloseable;

import java.util.List;
import java.util.function.Supplier;

/**
 * A specialized {@link Supplier} responsible for returning a list of {@link MDCCloseable}
 */
@FunctionalInterface
public interface MdcSupplier extends Supplier<List<MDCCloseable>> {
    /**
     * Gets a list of {@link MDCCloseable} by filling the MDC context.
     * @return the list of {@link MDCCloseable} after filling MDC.
     */
    List<MDCCloseable> fillContext();

    @Override
    default List<MDCCloseable> get() {
        return fillContext();
    }
}
