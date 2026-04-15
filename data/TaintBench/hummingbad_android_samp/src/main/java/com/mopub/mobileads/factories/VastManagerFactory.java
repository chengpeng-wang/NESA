package com.mopub.mobileads.factories;

import android.content.Context;
import com.mopub.mobileads.VastManager;

public class VastManagerFactory {
    protected static VastManagerFactory instance = new VastManagerFactory();

    public static VastManager create(Context context) {
        return instance.internalCreate(context, true);
    }

    public static VastManager create(Context context, boolean preCacheVideo) {
        return instance.internalCreate(context, preCacheVideo);
    }

    public VastManager internalCreate(Context context, boolean preCacheVideo) {
        return new VastManager(context, preCacheVideo);
    }

    @Deprecated
    public static void setInstance(VastManagerFactory factory) {
        instance = factory;
    }
}
