package org.lyora.leona.mvc.components.containers;

public interface ThreadAware {
    default void onThreadCopy() {}
}
