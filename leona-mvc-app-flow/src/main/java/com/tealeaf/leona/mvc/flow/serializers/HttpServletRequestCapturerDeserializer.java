package org.lyora.leona.mvc.flow.serializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.lyora.leona.mvc.flow.HttpServletRequestCapturer;

import java.io.IOException;

public class HttpServletRequestCapturerDeserializer extends StdDeserializer<HttpServletRequestCapturer> {
    public HttpServletRequestCapturerDeserializer() {
        super(HttpServletRequestCapturer.class);
    }

    @Override
    public HttpServletRequestCapturer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return new HttpServletRequestCapturer(p.readValueAs(String.class));
        }
        return p.readValueAs(HttpServletRequestCapturer.class);
    }
}
