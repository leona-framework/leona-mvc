package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.ClientExecutionView;
import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;


/**
 * The {@code LoggerConfiguration} class represents the configuration settings for client logging.
 * It encapsulates various properties related to client logger behavior, such as enabling or disabling
 * logging, specifying the logger implementation, defining log predicates, message suppliers, and log levels.
 */
@Data
public class LoggerConfiguration {
    /**
     * Specifies whether client logging is enabled or disabled.
     */
    private boolean enabled = true;
    /**
     * The class representing the client logger implementation to be used for logging, if not specified uses a default logger class.
     */
    private Class<? extends ClientLogger> clientLogger;
    /**
     * The class representing the predicate used to determine whether a client execution should be logged.
     */
    private Class<? extends Predicate<ClientExecutionView>> logPredicate;
    /**
     * The class representing the function that supplies log messages for client executions.
     */
    private Class<? extends Function<ClientExecutionView, String>> messageSupplier;
    /**
     * The log level at which client executions will be logged.
     */
    private Level level = Level.INFO;
}