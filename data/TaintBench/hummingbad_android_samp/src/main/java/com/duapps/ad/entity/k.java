package com.duapps.ad.entity;

import com.duapps.ad.base.f;
import com.duapps.ad.base.w;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.net.URL;

/* compiled from: NativeAdDLWrapper */
final class k implements Runnable {
    private /* synthetic */ String a;
    private /* synthetic */ a b;

    k(a aVar, String str) {
        this.b = aVar;
        this.a = str;
    }

    public final void run() {
        try {
            int statusCode = w.a(new URL(this.a), null, true).getStatusLine().getStatusCode();
            if (statusCode != CtaButton.WIDTH_DIPS) {
                f.c(h.a, "click to " + h.this.e.k + " failed!");
            } else if (statusCode == CtaButton.WIDTH_DIPS) {
                f.c(h.a, "click to " + h.this.e.k + " success!");
            }
        } catch (Exception e) {
            f.c(h.a, "click to " + h.this.e.k + " exception!");
        }
    }
}
