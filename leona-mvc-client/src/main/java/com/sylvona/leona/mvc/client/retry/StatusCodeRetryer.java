package com.sylvona.leona.mvc.client.retry;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Set;

public interface StatusCodeRetryer extends Retryer {
    Set<Integer> retriedStatusCodes();
    Set<Integer> ignoredStatusCodes();

    default boolean isRetryable(ResponseEntity<?> response) {
        return isRetryable(response.getStatusCode());
    }

    default boolean isRetryable(Throwable throwable) {
        return !(throwable instanceof HttpStatusCodeException statusCodeException)
                || isRetryable(statusCodeException.getStatusCode());
    }
    
    default boolean isRetryable(HttpStatusCode statusCode) {
        int statusCodeValue = statusCode.value();
        
        if (ignoredStatusCodes().contains(statusCodeValue)) return false;
        if (retriedStatusCodes().contains(statusCodeValue)) return true;
        
        return statusCode.isError();
    }
}
