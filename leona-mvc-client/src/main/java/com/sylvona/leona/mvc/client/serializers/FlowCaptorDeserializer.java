package com.sylvona.leona.mvc.client.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.sylvona.leona.mvc.client.flow.FlowCaptor;

import java.io.IOException;

/**
 * Provides custom deserialization for the {@link FlowCaptor} class
 */
public class FlowCaptorDeserializer extends StdDeserializer<FlowCaptor> {
    /**
     * Sets up the deserializer for the {@link FlowCaptor} class.
     */
    protected FlowCaptorDeserializer() {
        super(FlowCaptor.class);
    }

    @Override
    public FlowCaptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return new FlowCaptor(p.readValueAs(String.class));
        }
        return p.readValueAs(FlowCaptor.class);
    }
}
