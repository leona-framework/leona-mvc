package com.tealeaf.leona.mvc.client.serializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tealeaf.leona.mvc.client.flow.FlowCapturer;

import java.io.IOException;

public class FlowCapturerDeserializer extends StdDeserializer<FlowCapturer> {
    protected FlowCapturerDeserializer() {
        super(FlowCapturer.class);
    }

    @Override
    public FlowCapturer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return new FlowCapturer(p.readValueAs(String.class));
        }
        return p.readValueAs(FlowCapturer.class);
    }
}
