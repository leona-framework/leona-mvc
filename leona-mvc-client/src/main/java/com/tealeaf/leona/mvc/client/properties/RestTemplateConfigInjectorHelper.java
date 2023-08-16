package com.tealeaf.leona.mvc.client.properties;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import jakarta.validation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RestTemplateConfigInjectorHelper {
    private static Validator validator;

    public static RestClientConfig extractProperties(RestTemplate restTemplate) {
        LeonaClientConfigHolder configHolder = getAttachedConfigHolder(restTemplate).orElseThrow(); // TODO custom exception
        List<RestClientConfig> boundConfigs = configHolder.stream().toList();

        final RestClientConfig mergedConfig = ClientConfigMerger.mergeConfigs(boundConfigs, new RestClientConfig());

        Set<ConstraintViolation<RestClientConfig>> violations = getValidator().validate(mergedConfig);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

        Resilience4JClientRetryConfig retryConfig = mergedConfig.getRetry();
        if (retryConfig.getConfigName() != null) retryConfig.isConfigNamePopulated = true;
        else retryConfig.setConfigName(getBeanName(restTemplate));

        return mergedConfig;
    }

    static Optional<LeonaClientConfigHolder> getAttachedConfigHolder(RestTemplate restTemplate) {
        return LINQ.ofType(restTemplate.getClientHttpRequestInitializers().stream(), LeonaClientConfigHolder.class).findFirst();
    }

    static Optional<RestClientConfig> getAttachedConfig(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestConfig);
    }

    static String getBeanName(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestBeanName).orElseThrow();
    }

    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            factory.close();
        }
        return validator;
    }
}
