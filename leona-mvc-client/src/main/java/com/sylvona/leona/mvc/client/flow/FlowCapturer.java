package com.sylvona.leona.mvc.client.flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sylvona.leona.mvc.client.serializers.FlowCapturerDeserializer;
import com.sylvona.leona.mvc.components.captures.DefaultCapturePlan;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonDeserialize(using = FlowCapturerDeserializer.class)
public class FlowCapturer extends DefaultCapturePlan.DefaultCapturer<Map<String, String>> {
    private String contextKey;
    private String headerName;

    public FlowCapturer(String contextKey, String headerName) {
        super(null);
        this.contextKey = contextKey;
        this.headerName = headerName;
    }

    public FlowCapturer(String headerNameAndKey) {
        this(headerNameAndKey, headerNameAndKey);
    }

    @Override
    public Object apply(Map<String, String> map) {
        return map.get(contextKey);
    }
}
