package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.logging.LoggerConfiguration;
import com.sylvona.leona.mvc.client.logging.MdcAwareClientLogger;
import com.sylvona.leona.mvc.components.utils.ClassConstructor;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

class DefaultClientLogger implements MdcAwareClientLogger {
    private final Function<ClientExecutionView, String> message;
    private final Predicate<ClientExecutionView> predicate;
    private final Level level;
    private Logger logger;

    public DefaultClientLogger(LoggerConfiguration loggerConfiguration) {
        Class<? extends Predicate<ClientExecutionView>> logPredicate = loggerConfiguration.getLogPredicate();
        Class<? extends Function<ClientExecutionView, String>> messageSupplier = loggerConfiguration.getMessageSupplier();

        if (logPredicate != null) {
            predicate = ClassConstructor.createInstance(logPredicate);
        } else {
            predicate = ignored -> true;
        }

        if (messageSupplier != null) {
            message = ClassConstructor.createInstance(messageSupplier);
        } else {
            message = cev -> "Completed client request %s %s".formatted(cev.request().getMethod(), cev.request().getUriString());
        }

        level = loggerConfiguration.getLevel();
    }

    @Override
    public void log(ClientExecutionView executionView) {
        if (!predicate.test(executionView)) return;
        if (executionView.isError()) logger.error("Encountered exception", executionView.error());
        else logger.atLevel(level).log(message.apply(executionView));
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
