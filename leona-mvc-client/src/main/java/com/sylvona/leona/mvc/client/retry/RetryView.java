package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.ClientExecutionView;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;

import java.time.Duration;

/**
 * Interface representing the view of a retry operation, providing information about the retry process.
 */
public interface RetryView extends ClientExecutionView {
    /**
     * Retrieves the event associated with the retry operation.
     *
     * @return The {@link RetryOnRetryEvent} providing information about the retry event.
     */
    RetryOnRetryEvent event();

    /**
     * Retrieves the {@link Retryer} instance used for the retry operation.
     *
     * @return The Retryer instance responsible for the retry.
     */
    Retryer retryer();

    /**
     * Returns the current attempt number of the retry operation.
     *
     * @return The current attempt number.
     */
    default int currentAttempt() {
        return event().getNumberOfRetryAttempts() + 1;
    }

    /**
     * Returns the maximum number of attempts allowed for the retry operation.
     *
     * @return The maximum number of retry attempts.
     */
    default int maxAttempts() {
        return retryer().getRetry().getRetryConfig().getMaxAttempts();
    }

    /**
     * Returns the interval to wait before the next retry attempt.
     *
     * @return The duration of the wait interval.
     */
    default Duration waitInterval() {
        return event().getWaitInterval();
    }
}
