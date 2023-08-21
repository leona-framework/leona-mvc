package com.sylvona.leona.mvc.client;

/**
 * Functional interface for building a {@link Request} from a {@link Request.Builder}.
 */
@FunctionalInterface
public interface RequestBuilder {

    /**
     * Builds a {@link Request} using the provided {@link Request.Builder}.
     *
     * @param builder The {@link Request.Builder} used to construct the request.
     * @return A {@link Request} instance.
     */
    Request build(Request.Builder builder);
}
