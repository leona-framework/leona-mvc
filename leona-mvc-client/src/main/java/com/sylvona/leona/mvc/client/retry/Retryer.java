package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.RestClient;
import com.sylvona.leona.mvc.client.ClientInitializationHook;
import io.github.resilience4j.retry.Retry;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

/**
 * Interface representing a retry mechanism for handling failed requests.
 */
public interface Retryer extends ClientInitializationHook {
    /**
     * Executes the given callable supplier with retry functionality.
     *
     * @param callable The supplier providing the response entity to execute.
     * @param <T> The type of the response entity.
     * @return The response entity after executing with possible retries.
     */
    default <T> ResponseEntity<T> execute(Supplier<ResponseEntity<T>> callable) {
        return getRetry().executeSupplier(callable);
    }

    /**
     * Callback method invoked when a retry attempt occurs.
     *
     * @param retryView The view containing information about the retry.
     */
    void onRetry(RetryView retryView);

    /**
     * Checks if a given {@link ResponseEntity} is retryable based on some criteria.
     *
     * @param response The response entity to check.
     * @return {@code true} if the response entity is retryable, otherwise {@code false}.
     */
    boolean isRetryable(ResponseEntity<?> response);

    /**
     * Checks if a given throwable is retryable based on some criteria.
     *
     * @param throwable The throwable to check.
     * @return {@code true} if the throwable is retryable, otherwise {@code false}.
     */
    boolean isRetryable(Throwable throwable);

    /**
     * Retrieves the Retry instance configured for the retry mechanism.
     *
     * @return The configured Retry instance.
     */
    Retry getRetry();

    @Override
    default void onInitialize(RestClient.Modifier clientModifier) {}
}
