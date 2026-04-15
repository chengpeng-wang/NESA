package com.mopub.common.factories;

import com.mopub.common.util.Reflection.MethodBuilder;

public class MethodBuilderFactory {
    protected static MethodBuilderFactory instance = new MethodBuilderFactory();

    @Deprecated
    public static void setInstance(MethodBuilderFactory factory) {
        instance = factory;
    }

    public static MethodBuilder create(Object object, String methodName) {
        return instance.internalCreate(object, methodName);
    }

    /* access modifiers changed from: protected */
    public MethodBuilder internalCreate(Object object, String methodName) {
        return new MethodBuilder(object, methodName);
    }
}
