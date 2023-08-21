package com.sylvona.leona.mvc.components.captures;

import lombok.Getter;

/**
 * This class captures data according to a specified {@link CapturePlan} and indicates whether
 * the captured data should be considered persistent.
 *
 * @param <T> The type of data being captured.
 */
@Getter
public class PersistentCaptor<T> extends DefaultCaptor<T> {
    /**
     * Indicates whether the captured data should be considered persistent.
     */
    private final boolean isPersistent;

    /**
     * Constructs a new {@code PersistentCaptor} instance with the provided capture plan
     * and persistence flag.
     *
     * @param plan The capture plan specifying how data is to be captured.
     * @param isPersistent {@code true} if the captured data is persistent, {@code false} otherwise.
     */
    public PersistentCaptor(CapturePlan<T> plan, boolean isPersistent) {
        super(plan);
        this.isPersistent = isPersistent;
    }
}
