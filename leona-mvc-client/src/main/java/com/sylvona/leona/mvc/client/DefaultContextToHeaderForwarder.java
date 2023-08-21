package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.flow.ContextToHeaderForwarder;
import com.sylvona.leona.mvc.client.flow.FlowCaptor;

import java.util.List;

class DefaultContextToHeaderForwarder implements ContextToHeaderForwarder {
    private final List<FlowCaptor> forwarderList;

    DefaultContextToHeaderForwarder(List<FlowCaptor> forwarderList) {
        this.forwarderList = forwarderList;
    }

    @Override
    public List<FlowCaptor> getForwardedProperties() {
        return this.forwarderList;
    }
}
