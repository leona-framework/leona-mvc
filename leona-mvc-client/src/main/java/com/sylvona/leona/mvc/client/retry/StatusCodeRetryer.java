package com.sylvona.leona.mvc.client.retry;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Set;

/**
 * Interface representing a {@link Retryer} that considers HTTP status codes for retryability decisions.
 */
public interface StatusCodeRetryer extends Retryer {
    /**
     * Retrieves the set of HTTP status codes that trigger retries.
     *
     * @return The set of retried HTTP status codes.
     */
    Set<Integer> retriedStatusCodes();

    /**
     * Retrieves the set of HTTP status codes that are ignored for retrying.
     *
     * @return The set of ignored HTTP status codes.
     */
    Set<Integer> ignoredStatusCodes();

    /**
     * Checks whether a response with the given HTTP status code should be retried.
     *
     * @param response The response entity with the HTTP status code.
     * @return True if the response should be retried, otherwise false.
     */
    default boolean isRetryable(ResponseEntity<?> response) {
        return isRetryable(response.getStatusCode());
    }

    /**
     * Checks whether a throwable should trigger a retry.
     *
     * @param throwable The throwable instance.
     * @return True if the throwable should trigger a retry, otherwise false.
     */
    default boolean isRetryable(Throwable throwable) {
        return !(throwable instanceof HttpStatusCodeException statusCodeException)
                || isRetryable(statusCodeException.getStatusCode());
    }

    /**
     * Checks whether an HTTP status code should trigger a retry.
     *
     * @param statusCode The HTTP status code.
     * @return True if the HTTP status code should trigger a retry, otherwise false.
     */
    default boolean isRetryable(HttpStatusCode statusCode) {
        int statusCodeValue = statusCode.value();
        
        if (ignoredStatusCodes().contains(statusCodeValue)) return false;
        if (retriedStatusCodes().contains(statusCodeValue)) return true;
        
        return statusCode.isError();
    }
}
