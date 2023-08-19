package org.lyora.leona.mvc.client;

public interface ClientInitializationHook {
    void onInitialize(ClientExecuter.Modifier clientModifier);
}
