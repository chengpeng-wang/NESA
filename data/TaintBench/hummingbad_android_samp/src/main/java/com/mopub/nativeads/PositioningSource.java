package com.mopub.nativeads;

import android.support.annotation.NonNull;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;

interface PositioningSource {

    public interface PositioningListener {
        void onFailed();

        void onLoad(@NonNull MoPubClientPositioning moPubClientPositioning);
    }

    void loadPositions(@NonNull String str, @NonNull PositioningListener positioningListener);
}
