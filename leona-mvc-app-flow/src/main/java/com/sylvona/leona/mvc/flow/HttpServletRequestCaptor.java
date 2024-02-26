package com.sylvona.leona.mvc.flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sylvona.leona.mvc.components.captures.PersistentCaptor;
import com.sylvona.leona.mvc.flow.serializers.HttpServletRequestCapturerDeserializer;
import jakarta.servlet.http.HttpServletRequest;

/**
 * A simple captor that's responsible for extracting values from request headers.
 */
@JsonDeserialize(using = HttpServletRequestCapturerDeserializer.class)
public class HttpServletRequestCaptor extends PersistentCaptor<HttpServletRequest> {
    private String headerName;
    private String contextKey;

    /**
     * Constructs a new HttpServletRequestCapturer instance with the specified parameters.
     *
     * @param headerName   The key of the header to extract a value from.
     * @param contextKey   The key of the key to store the extracted value in.
     * @param isPersistent Indicates whether the captured value should persist in the context. (Default false)
     */
    public HttpServletRequestCaptor(String headerName, String contextKey, boolean isPersistent) {
        super(null, isPersistent);
        this.headerName = headerName;
        this.contextKey = contextKey;
    }

    /**
     * Constructs a new HttpServletRequestCapturer instance with the specified parameters,
     * assuming the captured value is not persistent.
     *
     * @param headerName The key of the header to extract a value from.
     * @param contextKey The key of the key to store the extracted value in.
     */
    public HttpServletRequestCaptor(String headerName, String contextKey) {
        this(headerName, contextKey, false);
    }

    /**
     * Constructs a new HttpServletRequestCapturer instance with the same header key and context key.
     *
     * @param headerAndContextName The key to be used for both the header and context key.
     */
    public HttpServletRequestCaptor(String headerAndContextName) {
        this(headerAndContextName, headerAndContextName);
    }

    /**
     * Captures the value from the specified HttpServletRequest object.
     *
     * @param servletRequest The HttpServletRequest from which the value will be captured.
     * @return The extracted value from the specified header.
     */
    @Override
    public Object apply(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(headerName);
    }

    /**
     * Checks whether the provided HttpServletRequest object is captureable.
     *
     * @param item The HttpServletRequest object to be checked.
     * @return True if the header with the specified key has a non-null value
     */
    @Override
    public boolean isCaptureable(HttpServletRequest item) {
        return item.getHeader(headerName) != null && super.isCaptureable(item);
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public String getContextKey() {
        return this.contextKey;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }
}
