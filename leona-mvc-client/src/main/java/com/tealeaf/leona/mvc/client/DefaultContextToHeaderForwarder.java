package org.lyora.leona.mvc.client;

import org.lyora.leona.mvc.client.flow.ContextToHeaderForwarder;
import org.lyora.leona.mvc.client.flow.FlowCapturer;

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
