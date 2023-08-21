package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.properties.RestClientConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract class for creating a typed REST client that works with a specific response type.
 * This class extends {@link RestClient} and simplifies sending HTTP requests and processing
 * responses using the provided RestTemplate configuration.
 * <p>
 * This class is designed for cases where the expected response type is known at compile time.
 *
 * @param <TResponse> The type of the expected response.
 */
@Client
public abstract class TypedRestClient<TResponse> extends RestClient {
    private final ParameterizedTypeReference<TResponse> responseTypeReference;

    /**
     * Constructs a TypedRestClient with a specified RestTemplate, RestClientConfig, and response class.
     *
     * @param restTemplate The RestTemplate instance to use for making requests.
     * @param clientConfig The configuration for the REST client.
     * @param responseClass The class type of the expected response.
     */
    public TypedRestClient(RestTemplate restTemplate, RestClientConfig clientConfig, Class<TResponse> responseClass) {
        super(restTemplate, clientConfig);
        this.responseTypeReference = ParameterizedTypeReference.forType(responseClass);
    }

    /**
     * Constructs a TypedRestClient with a specified RestTemplate and response class.
     * The RestTemplate must be an <b>accurately qualified RestTemplate</b> to properly work.
     *
     * @param restTemplate The RestTemplate instance to use for making requests.
     * @param responseClass The class type of the expected response.
     */
    public TypedRestClient(RestTemplate restTemplate, Class<TResponse> responseClass) {
        super(restTemplate);
        this.responseTypeReference = ParameterizedTypeReference.forType(responseClass);
    }

    /**
     * Sends an HTTP request with default configuration and expects a response of the specified response type.
     *
     * @return The ResponseEntity containing the response.
     */
    protected final ResponseEntity<TResponse> send() {
        return send(REQUEST_BUILDER);
    }

    /**
     * Sends an HTTP request with a request body and default configuration, expecting a response of the specified response type.
     *
     * @param requestBody The request body.
     * @param <TRequest> the request body type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest> ResponseEntity<TResponse> send(TRequest requestBody) {
        return send(REQUEST_BUILDER, requestBody);
    }

    /**
     * Sends an HTTP request with a specified response type and a custom request builder.
     *
     * @param requestBuilder The preconfigured custom request builder.
     * @return The ResponseEntity containing the response.
     */
    protected final ResponseEntity<TResponse> send(RequestBuilder requestBuilder) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType()).build(), responseTypeReference);
    }

    /**
     * Sends an HTTP request with a specified response type, a custom request builder, and a request body.
     *
     * @param requestBuilder The preconfigured custom request builder.
     * @param requestBody The request body.
     * @param <TRequest> the request body type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest> ResponseEntity<TResponse> send(RequestBuilder requestBuilder, TRequest requestBody) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType())
                .contentType(clientConfig.getContentMedia())
                .body(requestBody), responseTypeReference);
    }
}
