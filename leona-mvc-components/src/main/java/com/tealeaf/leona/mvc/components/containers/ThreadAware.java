package com.tealeaf.leona.mvc.components.containers;

public interface ThreadAware {
    default void onThreadCopy() {}
}
