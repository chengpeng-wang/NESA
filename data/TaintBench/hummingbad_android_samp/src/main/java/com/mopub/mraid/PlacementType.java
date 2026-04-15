package com.mopub.mraid;

import java.util.Locale;

public enum PlacementType {
    INLINE,
    INTERSTITIAL;

    /* access modifiers changed from: final */
    public final String toJavascriptString() {
        return toString().toLowerCase(Locale.US);
    }
}
