package com.sylvona.leona.mvc.client;

public interface ClientInitializationHook {
    void onInitialize(ClientExecuter.Modifier clientModifier);
}
