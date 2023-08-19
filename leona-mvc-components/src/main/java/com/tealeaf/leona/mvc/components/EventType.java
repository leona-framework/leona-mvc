package org.lyora.leona.mvc.components;

public record EventType(String value) {
    public static final EventType ENTRY = new EventType("ENTRY");
    public static final EventType EXIT = new EventType("EXIT");
    public static final EventType INTERIM = new EventType("INTERIM");
    public static final EventType SERVICE = new EventType("SERVICE");

    @Override
    public String toString() {
        return value;
    }
}
