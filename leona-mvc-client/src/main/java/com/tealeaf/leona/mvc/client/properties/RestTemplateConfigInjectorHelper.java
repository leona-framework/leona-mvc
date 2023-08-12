package com.tealeaf.leona.mvc.client.properties;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import jakarta.validation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RestTemplateConfigInjectorHelper {
    private static Validator validator;

    public static Optional<LeonaClientConfigHolder> getAttachedConfigHolder(RestTemplate restTemplate) {
        return LINQ.ofType(restTemplate.getClientHttpRequestInitializers().stream(), LeonaClientConfigHolder.class).findFirst();
    }

    public static Optional<BeanBackedClientConfig> getAttachedConfig(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestConfig);
    }

    public static String getBeanName(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestBeanName).orElseThrow();
    }

    public static BeanBackedClientConfig extractProperties(RestTemplate restTemplate) {
        LeonaClientConfigHolder configHolder = getAttachedConfigHolder(restTemplate).orElseThrow(); // TODO custom exception
        List<BeanBackedClientConfig> boundConfigs = configHolder.stream().toList();

        final BeanBackedClientConfig mergedConfig = ClientConfigMerger.mergeConfigs(boundConfigs, new BeanBackedClientConfig());

        Set<ConstraintViolation<BeanBackedClientConfig>> violations = getValidator().validate(mergedConfig);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);

        Resilience4JClientRetryConfig retryConfig = mergedConfig.getRetry();
        if (retryConfig.getConfigName() != null) retryConfig.isConfigNamePopulated = true;
        else retryConfig.setConfigName(getBeanName(restTemplate));

        return mergedConfig;
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
