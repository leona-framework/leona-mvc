package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.client.logging.ClientLogger;

import static com.sylvona.leona.mvc.client.RestClient.Modifier;

/**
 * Interface that automatically registers implementer to be pulled into any {@link RestClient} initializations.
 * This hook is the basis for modularizing and customizing variations of the {@link RestClient}. Hook initialization itself is not
 * ordered, outside the order in which implementing beans are created. However, this hook is often used in conjunction with a execution filter
 * <p>
 * - {@link PreExchangeExecutionFilter}
 * <p>
 * - {@link PostExchangeExecutionFilter}
 * <p>
 * which allows for the soft-ordering of filters.
 * @see ClientLogger
 * @see PreExchangeExecutionFilter
 * @see PostExchangeExecutionFilter
 */
public interface ClientInitializationHook {
    /**
     * This method is called when this hook is being attached to a form of {@link RestClient}.
     * @param clientModifier the {@link Modifier} responsible for configuring the invoking {@link RestClient}.
     */
    void onInitialize(RestClient.Modifier clientModifier);
}
