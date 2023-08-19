package com.sylvona.leona.mvc.client;

import java.util.List;

final class DefaultInitializers {
    public static List<ClientInitializationHook> initializers() {
        return List.of(
                new ExecutionDurationRequestInterceptor()
        );
    }
}
