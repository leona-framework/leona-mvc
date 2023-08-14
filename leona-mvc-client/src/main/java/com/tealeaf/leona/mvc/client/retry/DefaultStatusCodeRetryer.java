package com.tealeaf.leona.mvc.client.retry;

import com.google.common.collect.Sets;
import com.tealeaf.leona.mvc.client.ClientExecuter;
import com.tealeaf.leona.mvc.client.ClientExecutionView;
import com.tealeaf.leona.mvc.client.ClientInitializationHook;
import com.tealeaf.leona.mvc.client.PostExchangeExecutionFilter;
import com.tealeaf.leona.mvc.client.properties.Resilience4JClientRetryConfig;
import com.tealeaf.leona.mvc.components.Priority;
import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.retry.configuration.CommonRetryConfigurationProperties;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

class DefaultStatusCodeRetryer implements StatusCodeRetryer, PostExchangeExecutionFilter {
    private static final CommonRetryConfigurationProperties COMMON_RETRY_CONFIGURATION_PROPERTIES = new CommonRetryConfigurationProperties();
    private static final CompositeCustomizer<RetryConfigCustomizer> COMPOSITE_CUSTOMIZER = new CompositeCustomizer<>(null);
    private final Queue<ClientExecutionView> executionViewQueue = new LinkedList<>();
    private final RetryLogger logger;
    private final RetryRegistry registry;
    @Getter private Retry retry;
    private Set<Integer> retriedStatusCodes;
    private Set<Integer> ignoredStatusCodes;

    public DefaultStatusCodeRetryer(RetryConfiguration retryConfiguration, RetryRegistry registry, RetryLogger logger) {
        this.logger = logger;
        this.registry = registry;
        ignoredStatusCodes = new HashSet<>(retryConfiguration.getNeverRetriedStatusCodes());
    }

    @Override
    public Set<Integer> retriedStatusCodes() {
        return this.retriedStatusCodes;
    }

    @Override
    public Set<Integer> ignoredStatusCodes() {
        return this.ignoredStatusCodes;
    }

    @Override
    public void onRetry(RetryView retryView) {
        logger.log(retryView);
    }

    @Override
    public void onInitialize(ClientExecuter.Modifier clientModifier) {
        logger.setLogger(clientModifier.getLogger());

        Resilience4JClientRetryConfig resilience4JClientRetryConfig = clientModifier.getClient().getConfig().getRetry();
        retriedStatusCodes = resilience4JClientRetryConfig.getRetryStatusCodes();
        ignoredStatusCodes.addAll(resilience4JClientRetryConfig.getIgnoredStatusCodes());

        validateStatusCodeConstraint(retriedStatusCodes, ignoredStatusCodes);

        RetryConfig retryConfig;

        String configName = resilience4JClientRetryConfig.getConfigName();
        if (configName == null) throw new IllegalStateException("Configuration value \"configName\" cannot be null.");
        if (resilience4JClientRetryConfig.isConfigNamePopulated) {
            retryConfig = registry.retry(configName).getRetryConfig();
        } else {
            retryConfig = COMMON_RETRY_CONFIGURATION_PROPERTIES.createRetryConfig(resilience4JClientRetryConfig, COMPOSITE_CUSTOMIZER, configName);
        }
        retryConfig = RetryConfig.<ResponseEntity<?>>from(retryConfig)
                .retryOnResult(this::isRetryable)
                .retryOnException(this::isRetryable)
                .build();

        retry = RetryRegistry.of(retryConfig).retry(configName);
        retry.getEventPublisher().onRetry(r -> onRetry(new ClientEchoedRetryView(executionViewQueue.remove(), r, this)));
        clientModifier.getPostExecutionFilters().add(this);
        clientModifier.setRetryer(this);
    }

    @Nullable
    @Override
    public <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response) {
        executionViewQueue.add(clientExecutionView);
        return response;
    }

    private void validateStatusCodeConstraint(Set<Integer> retryableStatusCodes, Set<Integer> ignoredStatusCodes) {
        Set<Integer> intersection = Sets.intersection(retryableStatusCodes, ignoredStatusCodes);
        if (intersection.isEmpty()) return;
        throw new IllegalArgumentException("%s cannot exist as both retryable and ignored status codes.".formatted(ignoredStatusCodes));
    }

    @Override
    public Priority postExecutionPriority() {
        return Priority.VERY_LOW;
    }
}
