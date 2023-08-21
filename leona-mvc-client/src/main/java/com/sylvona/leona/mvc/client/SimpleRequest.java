package com.sylvona.leona.mvc.client;

import com.sylvona.leona.core.commons.streams.LINQ;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple default implementation of {@link Request}
 */
@lombok.Builder(access = AccessLevel.PRIVATE, builderClassName = "CopyBuilder", toBuilder = true)
public class SimpleRequest implements Request {
    private HttpMethod httpMethod;
    @Getter private HttpHeaders headers = new HttpHeaders();
    private UriComponentsBuilder uriComponentsBuilder;

    public Builder asBuilder() {
        return new BuilderImpl(this);
    }

    @Override
    public HttpMethod getMethod() {
        return httpMethod;
    }

    @Override
    public UriComponents getUri() {
        return uriComponentsBuilder.build();
    }

    @Override
    public String getUriString() {
        return uriComponentsBuilder.toUriString();
    }

    /**
     * Creates a {@link Request.Builder} based on a {@link UriComponentsBuilder}
     * @param componentsBuilder a default components builder
     * @return a preconfigured Builder
     */
    public static Builder builder(UriComponentsBuilder componentsBuilder) {
        BuilderImpl builderImpl = new BuilderImpl();
        builderImpl.uriComponentsBuilder = componentsBuilder;
        return builderImpl;
    }

    /**
     * A simple default implementation of {@link Builder}
     */
    @Accessors(fluent = true)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    protected static class BuilderImpl implements Builder, BuilderView {
        @Setter
        private HttpMethod httpMethod = HttpMethod.GET;
        private HttpHeaders headers = new HttpHeaders();
        private UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        private Map<String, Object> uriVariables = new HashMap<>();

        private BuilderImpl(SimpleRequest simpleRequestEntity) {
            this.httpMethod = simpleRequestEntity.httpMethod;
            this.headers = new HttpHeaders(new LinkedMultiValueMap<>(simpleRequestEntity.headers));
            this.uriComponentsBuilder = simpleRequestEntity.uriComponentsBuilder.cloneBuilder();
        }

        @Override
        public Builder appendPath(String path) {
            uriComponentsBuilder.pathSegment(path);
            return this;
        }

        @Override
        public Builder headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public Builder headers(MultiValueMap<String, String> headers) {
            this.headers = new HttpHeaders(headers);
            return this;
        }

        @Override
        public Builder header(String name, Object... values) {
            this.headers.addAll(name, LINQ.stream(values).map(v -> v == null ? null : v.toString()).toList());
            return this;
        }

        @Override
        public Builder queryParam(String name, Object... values) {
            this.uriComponentsBuilder.queryParam(name, values);
            return this;
        }

        @Override
        public Builder queryParameters(MultiValueMap<String, String> queryParameters) {
            this.uriComponentsBuilder.queryParams(queryParameters);
            return this;
        }

        @Override
        public Builder replaceQueryParameters(MultiValueMap<String, String> queryParameters) {
            this.uriComponentsBuilder.replaceQueryParams(queryParameters);
            return this;
        }

        @Override
        public Builder replaceVariables(Map<String, Object> variables) {
            uriVariables = variables;
            return this;
        }

        @Override
        public Builder variable(String key, Object value) {
            uriVariables.put(key, value);
            return this;
        }

        @Override
        public Builder variables(Map<String, Object> variables) {
            uriVariables.putAll(variables);
            return this;
        }

        @Override
        public Builder view(Consumer<BuilderView> viewConsumer) {
            viewConsumer.accept(this);
            return this;
        }

        @Override
        public SimpleRequest build() {
            UriComponentsBuilder componentsBuilder = uriComponentsBuilder.cloneBuilder();
            componentsBuilder.uriVariables(uriVariables);
            return new SimpleRequest(httpMethod, headers, componentsBuilder);
        }

        @Override
        public String getUrl() {
            return uriComponentsBuilder.toUriString();
        }

        @Override
        public HttpHeaders getHeaders() {
            return new HttpHeaders(this.headers);
        }

        @Override
        public HttpMethod getMethod() {
            return this.httpMethod;
        }

        @Override
        public MultiValueMap<String, String> getQueryParameters() {
            return this.uriComponentsBuilder.build().getQueryParams();
        }

        @Override
        public Map<String, Object> getVariables() {
            return this.uriVariables;
        }
    }
}
