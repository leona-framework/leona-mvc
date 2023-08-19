package com.sylvona.leona.mvc.components.containers;

public interface ThreadAware {
    default void onThreadCopy() {}
}
