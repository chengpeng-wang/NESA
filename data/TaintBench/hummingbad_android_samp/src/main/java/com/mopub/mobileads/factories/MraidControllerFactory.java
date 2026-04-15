package com.mopub.mobileads.factories;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mopub.common.AdReport;
import com.mopub.common.VisibleForTesting;
import com.mopub.mraid.MraidController;
import com.mopub.mraid.PlacementType;

public class MraidControllerFactory {
    protected static MraidControllerFactory instance = new MraidControllerFactory();

    @VisibleForTesting
    public static void setInstance(MraidControllerFactory factory) {
        instance = factory;
    }

    public static MraidController create(@NonNull Context context, @NonNull AdReport adReport, @NonNull PlacementType placementType) {
        return instance.internalCreate(context, adReport, placementType);
    }

    /* access modifiers changed from: protected */
    public MraidController internalCreate(@NonNull Context context, @NonNull AdReport adReport, @NonNull PlacementType placementType) {
        return new MraidController(context, adReport, placementType);
    }
}
