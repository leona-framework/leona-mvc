package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.properties.RestClientConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract class for creating a generic REST client that works with various response types
 * and request bodies. This class extends {@link RestClient} and simplifies sending HTTP
 * requests and processing responses using the provided RestTemplate configuration.
 * <p>
 * This class is typically used as a foundation for creating specific REST client implementations
 * that interact with remote services.
 */
@Client
public abstract class GenericRestClient extends RestClient {
    /**
     * Constructs a GenericRestClient with a specified RestTemplate, and RestClientConfig.
     *
     * @param restTemplate The RestTemplate instance to use for making requests.
     * @param clientConfig The configuration for the REST client.
     */
    public GenericRestClient(RestTemplate restTemplate, RestClientConfig clientConfig) {
        super(restTemplate, clientConfig);
    }

    /**
     * Constructs a GenericRestClient with a specified RestTemplate.
     * The RestTemplate must be an <b>accurately qualified RestTemplate</b> to properly work.
     *
     * @param restTemplate The RestTemplate instance to use for making requests.
     */
    public GenericRestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Sends an HTTP request with default configuration and expects a response of the specified response class type.
     *
     * @param responseClass The class type of the expected response.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TResponse> ResponseEntity<TResponse> send(Class<TResponse> responseClass) {
        return send(ParameterizedTypeReference.forType(responseClass));
    }

    /**
     * Sends an HTTP request with default configuration and expects a response of the specified response type reference.
     *
     * @param responseTypeReference The ParameterizedTypeReference representing the expected response type.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TResponse> ResponseEntity<TResponse> send(ParameterizedTypeReference<TResponse> responseTypeReference) {
        return send(responseTypeReference, REQUEST_BUILDER);
    }

    /**
     * Sends an HTTP request with a request body and default configuration, expecting a response of the specified response class type.
     *
     * @param responseClass The class type of the expected response.
     * @param requestBody The request body.
     * @param <TRequest>  the request body type.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest, TResponse> ResponseEntity<TResponse> send(Class<TResponse> responseClass, TRequest requestBody) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBody);
    }

    /**
     * Sends an HTTP request with a request body and default configuration, expecting a response of the specified response type reference.
     *
     * @param responseTypeReference The ParameterizedTypeReference representing the expected response type.
     * @param requestBody The request body.
     * @param <TRequest>  the request body type.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest, TResponse> ResponseEntity<TResponse> send(ParameterizedTypeReference<TResponse> responseTypeReference, TRequest requestBody) {
        return send(responseTypeReference, REQUEST_BUILDER, requestBody);
    }

    /**
     * Sends an HTTP request with a specified response class type and a custom request builder.
     *
     * @param responseClass The class type of the expected response.
     * @param requestBuilder The <b>preconfigured</b> custom request builder.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TResponse> ResponseEntity<TResponse> send(Class<TResponse> responseClass, RequestBuilder requestBuilder) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBuilder);
    }

    /**
     * Sends an HTTP request with a specified response type reference and a custom request builder.
     *
     * @param responseTypeReference The ParameterizedTypeReference representing the expected response type.
     * @param requestBuilder The <b>preconfigured</b> custom request builder.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TResponse> ResponseEntity<TResponse> send(ParameterizedTypeReference<TResponse> responseTypeReference, RequestBuilder requestBuilder) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType()).build(), responseTypeReference);
    }

    /**
     * Sends an HTTP request with a specified response class type, a custom request builder, and a request body.
     *
     * @param responseClass The class type of the expected response.
     * @param requestBuilder The <b>preconfigured</b> custom request builder.
     * @param requestBody The request body.
     * @param <TRequest>  the request body type.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest, TResponse> ResponseEntity<TResponse> send(Class<TResponse> responseClass, RequestBuilder requestBuilder, TRequest requestBody) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBuilder, requestBody);
    }

    /**
     * Sends an HTTP request with a specified response type reference, a custom request builder, and a request body.
     *
     * @param responseTypeReference The ParameterizedTypeReference representing the expected response type.
     * @param requestBuilder The <b>preconfigured</b> custom request builder.
     * @param requestBody The request body.
     * @param <TRequest>  the request body type.
     * @param <TResponse> the expected response type.
     * @return The ResponseEntity containing the response.
     */
    protected final <TRequest, TResponse> ResponseEntity<TResponse> send(ParameterizedTypeReference<TResponse> responseTypeReference, RequestBuilder requestBuilder, TRequest requestBody) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType())
                .contentType(clientConfig.getContentMedia())
                .body(requestBody), responseTypeReference);
    }
}
