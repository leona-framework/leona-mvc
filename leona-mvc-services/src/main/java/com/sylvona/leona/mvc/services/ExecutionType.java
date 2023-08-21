package com.sylvona.leona.mvc.services;

/**
 * An enumeration representing the types of execution for service methods.
 * <p>
 * This enumeration defines three execution types: synchronous, asynchronous, and unknown.
 * It is used to indicate how a service method is executed and can be used for logging or
 * to make decisions based on the execution type.
 */
public enum ExecutionType {
    /**
     * Represents synchronous execution of a service method.
     * Synchronous execution occurs when a method is executed sequentially,
     * and the result is immediately available upon method completion.
     */
    SYNCHRONOUS,

    /**
     * Represents asynchronous execution of a service method.
     * Asynchronous execution allows methods to be executed concurrently or in parallel,
     * and the result may not be immediately available upon method completion.
     */
    ASYNCHRONOUS,

    /**
     * Represents an unknown or undefined execution type.
     * This may occur in scenarios where the execution type cannot be determined or specified.
     */
    UNKNOWN
}
