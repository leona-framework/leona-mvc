package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.services.ServiceExecutionView;
import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A configuration class that holds settings for customizing service method logging behavior.
 * <p>
 * Instances of this class provide various configuration options to control how service method executions are logged.
 */
@Data
public class LoggerConfiguration {
    /**
     * Determines whether service method logging is enabled.
     */
    private boolean enabled = true;

    /**
     * The class that implements the {@link ServiceLogger} interface for custom service method logging.
     */
    private Class<? extends ServiceLogger> logger;

    /**
     * The class that implements the {@link Predicate} interface which determines if a given execution should be logged.
     */
    private Class<? extends Predicate<ServiceExecutionView<?>>> logPredicate;

    /**
     * The class that implements the {@link Function} interface to supply custom log messages based on service method executions.
     */
    private Class<? extends Function<ServiceExecutionView<?>, String>> messageSupplier;

    /**
     * The log level at which service method executions should be logged.
     */
    private Level level = Level.INFO;
}