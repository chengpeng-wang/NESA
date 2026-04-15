package com.mopub.nativeads;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.nativeads.PositioningSource.PositioningListener;

class ClientPositioningSource implements PositioningSource {
    @NonNull
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private|final */
    @NonNull
    public final MoPubClientPositioning mPositioning;

    ClientPositioningSource(@NonNull MoPubClientPositioning positioning) {
        this.mPositioning = MoPubNativeAdPositioning.clone(positioning);
    }

    public void loadPositions(@NonNull String adUnitId, @NonNull final PositioningListener listener) {
        this.mHandler.post(new Runnable() {
            public void run() {
                listener.onLoad(ClientPositioningSource.this.mPositioning);
            }
        });
    }
}
