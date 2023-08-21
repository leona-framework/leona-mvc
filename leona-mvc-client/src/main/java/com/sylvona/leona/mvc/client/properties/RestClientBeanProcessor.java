package com.sylvona.leona.mvc.client.properties;

import com.google.common.base.CaseFormat;
import com.sylvona.leona.mvc.client.ClientAutoConfigurationSource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;

/**
 * BeanPostProcessor implementation that processes RestTemplate beans to inject client properties.
 * This processor extracts RestClientConfig properties from the environment and associates them
 * with the RestTemplate instances.
 */
@Slf4j
@Configuration
public class RestClientBeanProcessor implements BeanPostProcessor {
    private static final Class<RestClientConfig> CONFIG_CLASS = RestClientConfig.class;

    private final HashSet<String> registeredBeans = new HashSet<>();
    private final ApplicationContext applicationContext;
    private final Binder binder;

    /**
     * Constructs the RestClientBeanProcessor.
     *
     * @param applicationContext The Spring ApplicationContext.
     * @param environment The environment to extract configuration properties from.
     */
    public RestClientBeanProcessor(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;

        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        PropertySourcesPlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment);
        ConversionService conversionService = null;

        if (environment instanceof ConfigurableEnvironment configurableEnvironment)
            conversionService = configurableEnvironment.getConversionService();

        binder = new Binder(sources, resolver, conversionService, null, null, null);
    }

    /**
     * Processes RestTemplate beans before initialization.
     *
     * @param bean The bean instance.
     * @param beanName The name of the bean.
     * @return The processed bean.
     * @throws BeansException If a BeansException occurs during processing.
     */
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (!(bean instanceof RestTemplate restTemplate)) return bean;
        if (registeredBeans.contains(beanName)) return bean;

        log.debug("Attempting to extract client properties from RestTemplate bean \"{}\"", beanName);
        RestClientConfig clientConfig = getProperties(beanName);
        if (clientConfig == null) return bean;

        LeonaClientConfigHolder configHolder = RestTemplateConfigInjectorHelper.getAttachedConfigHolder(restTemplate).orElseGet(() -> {
            LeonaClientConfigHolder holder = new LeonaClientConfigHolder();
            holder.addConfig(null, RestClientConfig.defaults());
            restTemplate.getClientHttpRequestInitializers().add(holder);
            return holder;
        });

        registeredBeans.add(beanName);
        configHolder.addConfig(beanName, clientConfig);

        return restTemplate;
    }

    private RestClientConfig getProperties(String prefix) {
        if (!prefix.startsWith(ClientAutoConfigurationSource.CONFIGURATION_QUALIFIER)) {
            if (prefix.endsWith(".")) prefix = ClientAutoConfigurationSource.CONFIGURATION_QUALIFIER + prefix;
            else prefix = ClientAutoConfigurationSource.CONFIGURATION_QUALIFIER + "." + prefix;
        }

        String beanIdentifier = prefix + "-" + CONFIG_CLASS.getName();
        if (applicationContext.containsBean(beanIdentifier))
            return applicationContext.getBean(beanIdentifier, CONFIG_CLASS);

        return binder.bind(convertCase(prefix), Bindable.of(CONFIG_CLASS)).orElse(null);
    }

    private static String convertCase(String input) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, input);
    }
}
