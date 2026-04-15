package com.facebook.ads.internal.dto;

import com.facebook.ads.internal.e;

public enum b {
    UNKNOWN,
    BANNER,
    INTERSTITIAL,
    NATIVE;

    public static b a(e eVar) {
        switch (eVar) {
            case NATIVE_UNKNOWN:
                return NATIVE;
            case WEBVIEW_BANNER_50:
            case WEBVIEW_BANNER_90:
            case WEBVIEW_BANNER_LEGACY:
            case WEBVIEW_BANNER_250:
                return BANNER;
            case WEBVIEW_INTERSTITIAL_HORIZONTAL:
            case WEBVIEW_INTERSTITIAL_VERTICAL:
            case WEBVIEW_INTERSTITIAL_TABLET:
            case WEBVIEW_INTERSTITIAL_UNKNOWN:
                return INTERSTITIAL;
            default:
                return UNKNOWN;
        }
    }
}
