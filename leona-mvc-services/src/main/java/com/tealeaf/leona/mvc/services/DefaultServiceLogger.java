package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.containers.ExecutionView;
import com.tealeaf.leona.mvc.components.utils.ClassConstructor;
import com.tealeaf.leona.mvc.services.logging.LoggerConfiguration;
import com.tealeaf.leona.mvc.services.logging.MdcAwareServiceLogger;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

class DefaultServiceLogger implements MdcAwareServiceLogger {
    private final Function<ServiceExecutionView<?>, String> message;
    private final Predicate<ServiceExecutionView<?>> predicate;
    private final Level level;

    public DefaultServiceLogger(LoggerConfiguration loggerConfiguration) {
        Class<? extends Predicate<ServiceExecutionView<?>>> logPredicate = loggerConfiguration.getLogPredicate();
        Class<? extends Function<ServiceExecutionView<?>, String>> messageSupplier = loggerConfiguration.getMessageSupplier();

        if (logPredicate != null) {
            predicate = ClassConstructor.createInstance(logPredicate);
        } else {
            predicate = ignored -> true;
        }

        if (messageSupplier != null) {
            message = ClassConstructor.createInstance(messageSupplier);
        } else {
            message = cev -> "%s - execution complete".formatted(cev.metadata().serviceName());
        }

        level = loggerConfiguration.getLevel();
    }

    @Override
    public void log(ServiceExecutionView<?> executionView) {
        if (!predicate.test(executionView)) return;

        Optional<Function<ExecutionView<?>, String>> messageProvider = executionView.context().getOrEmpty("LOGGING_PROVIDER");

        if (executionView.isError()) executionView.metadata().logger().error("Encountered exception", executionView.error());
        else if (messageProvider.isPresent()) executionView.metadata().logger().atLevel(level).log(messageProvider.get().apply(executionView));
        else executionView.metadata().logger().atLevel(level).log(message.apply(executionView));
    }
}
