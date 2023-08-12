package com.tealeaf.leona.mvc.client;

import java.util.List;

final class DefaultInitializers {
    public static List<ClientInitializer> initializers() {
        return List.of(
                new ExecutionDurationRequestInterceptor()
        );
    }
}
