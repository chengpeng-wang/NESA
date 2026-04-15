package com.flurry.sdk;

import android.support.v4.os.EnvironmentCompat;
import com.moceanmobile.mast.MASTNativeAdConstants;

public enum dh {
    BANNER("banner"),
    TAKEOVER("takeover"),
    STREAM("stream"),
    NATIVE(MASTNativeAdConstants.RESPONSE_NATIVE_STRING),
    UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN);
    
    private final String f;

    private dh(String str) {
        this.f = str;
    }

    public final String toString() {
        return this.f;
    }
}
