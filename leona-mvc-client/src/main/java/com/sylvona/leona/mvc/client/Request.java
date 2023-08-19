package com.sylvona.leona.mvc.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

import java.util.Map;
import java.util.function.Consumer;

public interface Request {
    static RequestEntity.BodyBuilder toEntityBuilder(Request request) {
        return RequestEntity.method(request.getMethod(), request.getUriString()).headers(request.getHeaders());
    }
    Builder asBuilder();
    HttpHeaders getHeaders();
    HttpMethod getMethod();
    UriComponents getUri();
    String getUriString();
    interface Builder {
        Builder appendPath(String path);
        Builder headers(HttpHeaders headers);
        Builder headers(MultiValueMap<String, String> headers);
        Builder header(String name, Object... values);
        Builder httpMethod(HttpMethod httpMethod);
        Builder queryParam(String name, Object... values);
        Builder queryParameters(MultiValueMap<String, String> queryParameters);
        Builder replaceQueryParameters(MultiValueMap<String, String> queryParameters);
        Builder replaceVariables(Map<String, Object> variables);
        Builder variable(String key, Object value);
        Builder variables(Map<String, Object> variables);
        Builder view(Consumer<BuilderView> viewConsumer);
        Request build();
    }

    interface BuilderView {
        String getUrl();
        HttpHeaders getHeaders();
        HttpMethod getMethod();
        MultiValueMap<String, String> getQueryParameters();
        Map<String, Object> getVariables();
    }

}
