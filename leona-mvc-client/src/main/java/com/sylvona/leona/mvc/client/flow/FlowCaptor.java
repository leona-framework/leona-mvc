package com.sylvona.leona.mvc.client.flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sylvona.leona.mvc.client.serializers.FlowCaptorDeserializer;
import com.sylvona.leona.mvc.components.captures.DefaultCaptor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * FlowCaptor is a class that captures a value from a context map and attaches it as a header in an HTTP request.
 * This class is primarily used to capture values from a context and pass them along in the headers of HTTP requests.
 * It provides the context key and the header name to be used during the capture process.
 */
@Getter
@Setter
@JsonDeserialize(using = FlowCaptorDeserializer.class)
public class FlowCaptor extends DefaultCaptor<Map<String, String>> {

    /** The key used to retrieve the captured value from the context map. */
    private String contextKey;

    /** The name of the header where the captured value will be attached in the HTTP request. */
    private String headerName;

    /**
     * Constructs a FlowCaptor instance with the specified context key and header name.
     *
     * @param contextKey The key used to retrieve the captured value from the context map.
     * @param headerName The name of the header where the captured value will be attached in the HTTP request.
     */
    public FlowCaptor(String contextKey, String headerName) {
        super(null);
        this.contextKey = contextKey;
        this.headerName = headerName;
    }

    /**
     * Constructs a FlowCaptor instance with the same value for both the context key and the header name.
     *
     * @param headerNameAndKey The shared name for both the context key and the header name.
     */
    public FlowCaptor(String headerNameAndKey) {
        this(headerNameAndKey, headerNameAndKey);
    }

    /**
     * Retrieves the captured value from the context map based on the context key.
     *
     * @param map The context map containing key-value pairs.
     * @return The captured value from the context map based on the context key.
     */
    @Override
    public Object apply(Map<String, String> map) {
        return map.get(contextKey);
    }
}
