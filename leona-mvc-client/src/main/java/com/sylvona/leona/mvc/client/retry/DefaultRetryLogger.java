package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.components.utils.ClassConstructor;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

class DefaultRetryLogger implements RetryLogger {
    private final Level level;
    private final Predicate<RetryView> predicate;
    private final Function<RetryView, String> message;
    private Logger logger;

    public DefaultRetryLogger(RetryLoggingConfiguration loggingConfiguration) {
        level = loggingConfiguration.getLevel();
        Class<? extends Predicate<RetryView>> logPredicate = loggingConfiguration.getLogPredicate();
        if (logPredicate != null) {
            predicate = ClassConstructor.createInstance(logPredicate);
        } else {
            predicate = i -> true;
        }

        Class<? extends Function<RetryView, String>> messageFunction = loggingConfiguration.getMessage();
        if (messageFunction != null) {
            message = ClassConstructor.createInstance(messageFunction);
        } else {
            message = ev -> "Attempting retry %s/%s timeout=%s ms".formatted(ev.currentAttempt(), ev.maxAttempts(), ev.waitInterval().toMillis());
        }
    }

    @Override
    public void log(RetryView retryView) {
        if (predicate.test(retryView))
            logger.atLevel(level).log(message.apply(retryView));
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
