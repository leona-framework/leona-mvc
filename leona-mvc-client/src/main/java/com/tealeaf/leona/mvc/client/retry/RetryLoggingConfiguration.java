package com.tealeaf.leona.mvc.client.retry;

import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class RetryLoggingConfiguration {
    private boolean enabled = true;
    private Level level = Level.INFO;
    private Class<? extends RetryLogger> retryLogger;
    private Class<? extends Predicate<RetryView>> logPredicate;
    private Class<? extends Function<RetryView, String>> message;
}
