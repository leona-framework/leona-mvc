package com.sylvona.leona.mvc.components;

/**
 * A class representing different types of event categories.
 * Each event type is identified by its associated string value.
 */
public record EventType(String value) {

    /**
     * Represents an "ENTRY" event type.
     */
    public static final EventType ENTRY = new EventType("ENTRY");

    /**
     * Represents an "EXIT" event type.
     */
    public static final EventType EXIT = new EventType("EXIT");

    /**
     * Represents an "INTERIM" event type.
     */
    public static final EventType INTERIM = new EventType("INTERIM");

    /**
     * Represents a "SERVICE" event type.
     */
    public static final EventType SERVICE = new EventType("SERVICE");

    /**
     * Returns the string representation of the event type.
     *
     * @return The string value associated with the event type.
     */
    @Override
    public String toString() {
        return value;
    }
}
