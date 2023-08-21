package com.sylvona.leona.mvc.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

import java.util.Map;
import java.util.function.Consumer;

/**
 * The {@code Request} interface represents an HTTP request, providing methods to configure and build a request entity
 * with specific details such as method, headers, URI, query parameters, and variables.
 */
public interface Request {
    /**
     * Creates a {@link RequestEntity.BodyBuilder} instance for constructing a Spring {@link RequestEntity} from the given request.
     *
     * @param request The request containing method, headers, and URI information.
     * @return A {@code  RequestEntity.BodyBuilder} instance to build into a Spring {@link RequestEntity}.
     */
    static RequestEntity.BodyBuilder toEntityBuilder(Request request) {
        return RequestEntity.method(request.getMethod(), request.getUriString()).headers(request.getHeaders());
    }

    /**
     * Returns a new instance of the {@link Builder} interface based on the values of this request for configuring and building a new request.
     *
     * @return A new {@code Builder} instance to configure and build the request.
     */
    Builder asBuilder();

    /**
     * Returns the {@link HttpHeaders} of the request.
     *
     * @return The headers of the request.
     */
    HttpHeaders getHeaders();

    /**
     * Returns the {@link HttpMethod} of the request.
     *
     * @return The HTTP method of the request.
     */
    HttpMethod getMethod();

    /**
     * Returns the {@link UriComponents} of the request.
     *
     * @return The URI components of the request.
     */
    UriComponents getUri();

    /**
     * Returns the URI string representation of the request.
     *
     * @return The URI string of the request.
     */
    String getUriString();

    /**
     * The {@code Builder} interface defines methods for configuring and building a {@link Request}.
     */
    interface Builder {
        /**
         * Appends the specified path to the existing URI path.
         *
         * @param path The path to append.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder appendPath(String path);

        /**
         * Sets the headers of the request using the provided {@link HttpHeaders} instance.
         *
         * @param headers The headers to set.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder headers(HttpHeaders headers);

        /**
         * Sets the headers of the request using the provided {@link MultiValueMap}.
         *
         * @param headers The headers to set.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder headers(MultiValueMap<String, String> headers);
        /**
         * Adds a header with the given name and values to the request.
         *
         * @param name   The name of the header.
         * @param values The values of the header.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder header(String name, Object... values);
        /**
         * Sets the HTTP method of the request.
         *
         * @param httpMethod The HTTP method to set.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder httpMethod(HttpMethod httpMethod);
        /**
         * Adds a query parameter with the given name and values to the request.
         *
         * @param name   The name of the query parameter.
         * @param values The values of the query parameter.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder queryParam(String name, Object... values);

        /**
         * Sets the query parameters of the request using the provided {@link MultiValueMap}.
         *
         * @param queryParameters The query parameters to set.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder queryParameters(MultiValueMap<String, String> queryParameters);

        /**
         * Replaces the existing query parameters with the provided {@link MultiValueMap} of query parameters.
         *
         * @param queryParameters The query parameters to replace with.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder replaceQueryParameters(MultiValueMap<String, String> queryParameters);

        /**
         * Replaces variables in the request's URL template with the provided key-value pairs.
         *
         * @param variables The variables to replace in the URL template.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder replaceVariables(Map<String, Object> variables);

        /**
         * Adds a single variable to the request's URL template.
         *
         * @param key   The key of the variable.
         * @param value The value of the variable.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder variable(String key, Object value);

        /**
         * Sets multiple variables in the request's URL template using the provided key-value pairs.
         *
         * @param variables The variables to set in the URL template.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder variables(Map<String, Object> variables);

        /**
         * Provides a view of the current request builder settings through the provided {@link Consumer}.
         *
         * @param viewConsumer The consumer to apply the view to.
         * @return This {@code Builder} instance for method chaining.
         */
        Builder view(Consumer<BuilderView> viewConsumer);
        /**
         * Builds the request based on the configured settings and returns the constructed {@link Request} instance.
         *
         * @return The constructed {@code Request} instance.
         */
        Request build();
    }

    /**
     * The {@code BuilderView} interface provides a view of the configured elements in the {@link Builder},
     * allowing introspection of the request configuration.
     */
    interface BuilderView {
        /**
         * Returns the URL string of the request.
         *
         * @return The URL string of the request.
         */
        String getUrl();
        /**
         * Returns the headers configured in the {@link Builder}.
         *
         * @return The headers configured in the {@code Builder}.
         */
        HttpHeaders getHeaders();
        /**
         * Returns the HTTP method configured in the {@link Builder}.
         *
         * @return The HTTP method configured in the {@code Builder}.
         */
        HttpMethod getMethod();
        /**
         * Returns the query parameters configured in the {@link Builder}.
         *
         * @return The query parameters configured in the {@code Builder}.
         */
        MultiValueMap<String, String> getQueryParameters();
        /**
         * Returns the variables configured in the {@link Builder}.
         *
         * @return The variables configured in the {@code Builder}.
         */
        Map<String, Object> getVariables();
    }

}
