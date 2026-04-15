package com.mopub.mobileads.factories;

import com.mopub.mobileads.CustomEventBanner;
import java.lang.reflect.Constructor;

public class CustomEventBannerFactory {
    private static CustomEventBannerFactory instance = new CustomEventBannerFactory();

    public static CustomEventBanner create(String className) throws Exception {
        return instance.internalCreate(className);
    }

    @Deprecated
    public static void setInstance(CustomEventBannerFactory factory) {
        instance = factory;
    }

    /* access modifiers changed from: protected */
    public CustomEventBanner internalCreate(String className) throws Exception {
        Constructor declaredConstructor = Class.forName(className).asSubclass(CustomEventBanner.class).getDeclaredConstructor(null);
        declaredConstructor.setAccessible(true);
        return (CustomEventBanner) declaredConstructor.newInstance(new Object[0]);
    }
}
