package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.client.properties.BeanBackedClientConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RestClient
public abstract class TypedRestClient<TResponse> extends ClientExecuter {
    private final ParameterizedTypeReference<TResponse> responseTypeReference;

    public TypedRestClient(RestTemplate restTemplate, BeanBackedClientConfig clientConfig, Class<TResponse> responseClass) {
        super(restTemplate, clientConfig);
        this.responseTypeReference = ParameterizedTypeReference.forType(responseClass);
    }

    public TypedRestClient(RestTemplate restTemplate, Class<TResponse> responseClass) {
        super(restTemplate);
        this.responseTypeReference = ParameterizedTypeReference.forType(responseClass);
    }

    protected final ResponseEntity<?> send() {
        return send(REQUEST_BUILDER);
    }

    protected final <TRequest> ResponseEntity<?> send(TRequest requestBody) {
        return send(REQUEST_BUILDER, requestBody);
    }

    protected final ResponseEntity<?> send(RequestBuilder requestBuilder) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType()).build(), responseTypeReference);
    }

    protected final <TRequest> ResponseEntity<?> send(RequestBuilder requestBuilder, TRequest requestBody) {
        Request request = requestBuilder.build(preconfiguredRequestEntity.asBuilder());
        return execute(request, rb -> rb.accept(clientConfig.acceptType())
                .contentType(clientConfig.getContentMedia())
                .body(requestBody), responseTypeReference);
    }
}
