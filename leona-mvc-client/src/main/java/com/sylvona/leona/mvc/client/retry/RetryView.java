package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.ClientExecutionView;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;

import java.time.Duration;

public interface RetryView extends ClientExecutionView {
    RetryOnRetryEvent event();
    Retryer retryer();
    default int currentAttempt() {
        return event().getNumberOfRetryAttempts() + 1;
    }
    default int maxAttempts() {
        return retryer().getRetry().getRetryConfig().getMaxAttempts();
    }
    default Duration waitInterval() {
        return event().getWaitInterval();
    }
}
