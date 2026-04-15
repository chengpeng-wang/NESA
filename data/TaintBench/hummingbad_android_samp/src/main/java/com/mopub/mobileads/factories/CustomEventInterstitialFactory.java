package com.mopub.mobileads.factories;

import com.mopub.mobileads.CustomEventInterstitial;
import java.lang.reflect.Constructor;

public class CustomEventInterstitialFactory {
    private static CustomEventInterstitialFactory instance = new CustomEventInterstitialFactory();

    public static CustomEventInterstitial create(String className) throws Exception {
        return instance.internalCreate(className);
    }

    @Deprecated
    public static void setInstance(CustomEventInterstitialFactory factory) {
        instance = factory;
    }

    /* access modifiers changed from: protected */
    public CustomEventInterstitial internalCreate(String className) throws Exception {
        Constructor declaredConstructor = Class.forName(className).asSubclass(CustomEventInterstitial.class).getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);
        return (CustomEventInterstitial) declaredConstructor.newInstance(new Object[0]);
    }
}
