package com.sylvona.leona.mvc.client.retry;

import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Configuration class that defines the logging behavior for retry operations.
 */
@Data
public class RetryLoggingConfiguration {
    /**
     * Determines whether retry logging is enabled.
     */
    private boolean enabled = true;

    /**
     * The log level at which retry logging will occur.
     */
    private Level level = Level.INFO;

    /**
     * The class implementing the RetryLogger interface for custom retry logging, if not specified uses a default implementation.
     */
    private Class<? extends RetryLogger> retryLogger;

    /**
     * The class implementing the Predicate interface to determine whether a retry event should be logged.
     */
    private Class<? extends Predicate<RetryView>> logPredicate;

    /**
     * The class implementing the Function interface to generate log messages for retry events.
     */
    private Class<? extends Function<RetryView, String>> message;
}
