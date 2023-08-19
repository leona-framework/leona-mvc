package com.sylvona.leona.mvc.client.properties;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.core.commons.streams.LINQStream;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;

import java.util.ArrayList;
import java.util.List;

class LeonaClientConfigHolder implements ClientHttpRequestInitializer {
    private final List<ClientConfigInfo> configurations = new ArrayList<>();

    @Override
    public void initialize(@NotNull ClientHttpRequest request) {}

    public void addConfig(String beanName, RestClientConfig config) {
        configurations.add(new ClientConfigInfo(beanName, config));
    }

    public String getHighestBeanName() {
        ClientConfigInfo configInfo = getHighestPropertyInfo();
        return configInfo == null ? null : configInfo.bean;
    }

    public RestClientConfig getHighestConfig() {
        ClientConfigInfo configInfo = getHighestPropertyInfo();
        return configInfo == null ? null : configInfo.config;
    }

    public LINQStream<RestClientConfig> stream() {
        return LINQ.stream(configurations).map(info -> info.config);
    }

    private ClientConfigInfo getHighestPropertyInfo() {
        return configurations.isEmpty() ? null : configurations.get(configurations.size() - 1);
    }

    record ClientConfigInfo(String bean, RestClientConfig config) {}
}
