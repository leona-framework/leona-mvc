package com.sylvona.leona.mvc.flow.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.sylvona.leona.mvc.flow.HttpServletRequestCaptor;

import java.io.IOException;

public class HttpServletRequestCapturerDeserializer extends StdDeserializer<HttpServletRequestCaptor> {
    public HttpServletRequestCapturerDeserializer() {
        super(HttpServletRequestCaptor.class);
    }

    @Override
    public HttpServletRequestCaptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return new HttpServletRequestCaptor(p.readValueAs(String.class));
        }
        return p.readValueAs(HttpServletRequestCaptor.class);
    }
}
