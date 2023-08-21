package com.sylvona.leona.mvc.client.properties;

import com.sylvona.leona.core.commons.streams.LINQ;
import jakarta.validation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * Helper class for extracting and manipulating configuration properties for RestTemplate instances.
 */
public class RestTemplateConfigInjectorHelper {
    private static Validator validator;

    /**
     * Extracts and merges RestClientConfig properties from the attached LeonaClientConfigHolder and performs validation.
     *
     * @param restTemplate The RestTemplate instance from which to extract configuration properties.
     * @return The merged and validated RestClientConfig instance.
     * @throws ConstraintViolationException If validation of the merged configuration properties fails.
     */
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

    /**
     * Retrieves the attached LeonaClientConfigHolder from a RestTemplate instance.
     *
     * @param restTemplate The RestTemplate instance to check for an attached LeonaClientConfigHolder.
     * @return An Optional containing the attached LeonaClientConfigHolder if found, otherwise empty.
     */
    static Optional<LeonaClientConfigHolder> getAttachedConfigHolder(RestTemplate restTemplate) {
        return LINQ.ofType(restTemplate.getClientHttpRequestInitializers().stream(), LeonaClientConfigHolder.class).findFirst();
    }

    /**
     * Retrieves the attached RestClientConfig from a RestTemplate instance.
     *
     * @param restTemplate The RestTemplate instance to retrieve the attached RestClientConfig from.
     * @return An Optional containing the attached RestClientConfig if found, otherwise empty.
     */
    static Optional<RestClientConfig> getAttachedConfig(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestConfig);
    }

    /**
     * Retrieves the bean name associated with a RestTemplate instance.
     *
     * @param restTemplate The RestTemplate instance to retrieve the bean name for.
     * @return The bean name associated with the RestTemplate instance.
     * @throws NoSuchElementException If the bean name is not found.
     */
    static String getBeanName(RestTemplate restTemplate) {
        return getAttachedConfigHolder(restTemplate).map(LeonaClientConfigHolder::getHighestBeanName).orElseThrow();
    }

    /**
     * Retrieves the shared Validator instance, creating it if it doesn't exist.
     *
     * @return The shared Validator instance.
     */
    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            factory.close();
        }
        return validator;
    }
}
