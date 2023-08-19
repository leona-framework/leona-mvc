package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.properties.RestClientConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RestClient
public abstract class GenericRestClient extends ClientExecuter {
    public GenericRestClient(RestTemplate restTemplate, RestClientConfig clientConfig) {
        super(restTemplate, clientConfig);
    }

    public GenericRestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    protected final <TResponse> ResponseEntity<?> send(Class<TResponse> responseClass) {
        return send(ParameterizedTypeReference.forType(responseClass));
    }

    protected final <TResponse> ResponseEntity<?> send(ParameterizedTypeReference<TResponse> responseTypeReference) {
        return send(responseTypeReference, REQUEST_BUILDER);
    }

    protected final <TRequest, TResponse> ResponseEntity<?> send(Class<TResponse> responseClass, TRequest requestBody) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBody);
    }

    protected final <TRequest, TResponse> ResponseEntity<?> send(ParameterizedTypeReference<TResponse> responseTypeReference, TRequest requestBody) {
        return send(responseTypeReference, REQUEST_BUILDER, requestBody);
    }

    protected final <TResponse> ResponseEntity<TResponse> send(Class<TResponse> responseClass, RequestBuilder requestBuilder) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBuilder);
    }

    protected final <TResponse> ResponseEntity<TResponse> send(ParameterizedTypeReference<TResponse> responseTypeReference, RequestBuilder requestBuilder) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType()).build(), responseTypeReference);
    }

    protected final <TRequest, TResponse> ResponseEntity<?> send(Class<TResponse> responseClass, RequestBuilder requestBuilder, TRequest requestBody) {
        return send(ParameterizedTypeReference.forType(responseClass), requestBuilder, requestBody);
    }

    protected final <TRequest, TResponse> ResponseEntity<?> send(ParameterizedTypeReference<TResponse> responseTypeReference, RequestBuilder requestBuilder, TRequest requestBody) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType())
                .contentType(clientConfig.getContentMedia())
                .body(requestBody), responseTypeReference);
    }
}
