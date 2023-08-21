package com.sylvona.leona.mvc.client.retry;

import org.slf4j.Logger;

/**
 * Interface for logging retry-related information during request retries.
 */
public interface RetryLogger {
    /**
     * Logs retry-related information using the provided retry view.
     *
     * @param retryView The view containing information about the retry.
     */
    void log(RetryView retryView);

    /**
     * Sets the {@link Logger} to be used for logging retry-related information.
     *
     * @param logger The logger instance to be set for logging.
     */
    void setLogger(Logger logger);
}
