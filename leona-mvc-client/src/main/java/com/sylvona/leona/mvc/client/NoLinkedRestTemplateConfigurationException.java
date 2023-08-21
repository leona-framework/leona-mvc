package com.sylvona.leona.mvc.client;

import org.springframework.web.client.RestTemplate;

/**
 * Exception thrown when a {@link RestClient} cannot find a valid configuration linked to its provided {@link RestTemplate}.
 */
public class NoLinkedRestTemplateConfigurationException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public NoLinkedRestTemplateConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The cause of the exception.
     */
    public NoLinkedRestTemplateConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}