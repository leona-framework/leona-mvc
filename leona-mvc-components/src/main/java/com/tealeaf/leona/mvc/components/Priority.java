package com.tealeaf.leona.mvc.components;

public record Priority(int value) {
    public static final int FIRST_VALUE = 10000;
    public static final int VERY_HIGH_VALUE = 8000;
    public static final int HIGH_VALUE = 6500;
    public static final int NORMAL_VALUE = 5000;
    public static final int LOW_VALUE = 4500;
    public static final int VERY_LOW_VALUE = 2000;
    public static final int LAST_VALUE = 0;

    public static final Priority FIRST = new Priority(FIRST_VALUE);
    public static final Priority VERY_HIGH = new Priority(VERY_HIGH_VALUE);
    public static final Priority HIGH = new Priority(HIGH_VALUE);
    public static final Priority NORMAL = new Priority(NORMAL_VALUE);
    public static final Priority LOW = new Priority(LOW_VALUE);
    public static final Priority VERY_LOW = new Priority(VERY_LOW_VALUE);
    public static final Priority LAST = new Priority(LAST_VALUE);

    public static Priority of(int value) {
        if (value < 0 || value > 10000) throw new IllegalArgumentException("Priority value cannot be less than zero or greater than 10,000.");
        return switch (value) {
            case FIRST_VALUE -> FIRST;
            case VERY_HIGH_VALUE -> VERY_HIGH;
            case HIGH_VALUE -> HIGH;
            case NORMAL_VALUE -> NORMAL;
            case LOW_VALUE -> LOW;
            case VERY_LOW_VALUE -> VERY_LOW;
            case LAST_VALUE -> LAST;
            default -> new Priority(value);
        };
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Priority priority = (Priority) o;
        return value == priority.value;
    }
}
