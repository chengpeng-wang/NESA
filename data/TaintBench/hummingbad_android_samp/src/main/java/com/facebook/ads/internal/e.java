package com.facebook.ads.internal;

import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import org.xbill.DNS.WKSRecord.Service;

public enum e {
    UNKNOWN(0),
    WEBVIEW_BANNER_LEGACY(4),
    WEBVIEW_BANNER_50(5),
    WEBVIEW_BANNER_90(6),
    WEBVIEW_BANNER_250(7),
    WEBVIEW_INTERSTITIAL_UNKNOWN(100),
    WEBVIEW_INTERSTITIAL_HORIZONTAL(Service.HOSTNAME),
    WEBVIEW_INTERSTITIAL_VERTICAL(Service.ISO_TSAP),
    WEBVIEW_INTERSTITIAL_TABLET(Service.X400),
    NATIVE_UNKNOWN(CtaButton.WIDTH_DIPS),
    NATIVE_250(201);
    
    private final int l;

    private e(int i) {
        this.l = i;
    }

    public final int a() {
        return this.l;
    }
}
