package com.facebook.ads.internal.server;

import android.support.v4.os.EnvironmentCompat;
import com.facebook.ads.internal.util.r;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.AdType;
import java.util.Locale;

public enum AdPlacementType {
    UNKNOWN(EnvironmentCompat.MEDIA_UNKNOWN),
    BANNER("banner"),
    INTERSTITIAL(AdType.INTERSTITIAL),
    NATIVE(MASTNativeAdConstants.RESPONSE_NATIVE_STRING);
    
    private String a;

    private AdPlacementType(String str) {
        this.a = str;
    }

    public static AdPlacementType fromString(String str) {
        if (r.a(str)) {
            return UNKNOWN;
        }
        try {
            return valueOf(str.toUpperCase(Locale.US));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public final String toString() {
        return this.a;
    }
}
