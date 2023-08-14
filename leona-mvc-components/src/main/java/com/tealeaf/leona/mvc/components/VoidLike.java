package com.tealeaf.leona.mvc.components;

public class VoidLike {
    public static final VoidLike INSTANCE = new VoidLike();

    private VoidLike() {
    }

    public VoidLike newInstance() {
        return new VoidLike();
    }
}
