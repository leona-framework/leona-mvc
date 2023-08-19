package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.flow.ContextToHeaderForwarder;
import com.sylvona.leona.mvc.client.flow.FlowCapturer;

import java.util.List;

class DefaultContextToHeaderForwarder implements ContextToHeaderForwarder {
    private final List<FlowCapturer> forwarderList;

    DefaultContextToHeaderForwarder(List<FlowCapturer> forwarderList) {
        this.forwarderList = forwarderList;
    }

    @Override
    public List<FlowCapturer> getForwardedProperties() {
        return this.forwarderList;
    }
}
