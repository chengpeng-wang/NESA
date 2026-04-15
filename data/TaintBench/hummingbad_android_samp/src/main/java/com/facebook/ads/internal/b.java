package com.facebook.ads.internal;

import com.facebook.ads.AdError;
import com.facebook.ads.internal.util.r;

public class b {
    private final AdErrorType a;
    private final String b;

    public b(AdErrorType adErrorType, String str) {
        if (r.a(str)) {
            str = adErrorType.getDefaultErrorMessage();
        }
        this.a = adErrorType;
        this.b = str;
    }

    public AdErrorType a() {
        return this.a;
    }

    public AdError b() {
        return this.a.a() ? new AdError(this.a.getErrorCode(), this.b) : new AdError(AdErrorType.UNKNOWN_ERROR.getErrorCode(), AdErrorType.UNKNOWN_ERROR.getDefaultErrorMessage());
    }
}
