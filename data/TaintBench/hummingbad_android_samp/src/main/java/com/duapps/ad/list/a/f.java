package com.duapps.ad.list.a;

import com.duapps.ad.base.d;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;

/* compiled from: NativeFBWrapper */
final class f implements AdListener {
    private /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    public final void onError(Ad ad, AdError adError) {
        if (adError == null) {
            if (this.a.c != null) {
                this.a.c.onAdError(com.duapps.ad.AdError.UNKNOW_ERROR);
            }
        } else if (this.a.c != null) {
            this.a.c.onAdError(new com.duapps.ad.AdError(adError.getErrorCode(), adError.getErrorMessage()));
        }
    }

    public final void onAdLoaded(Ad ad) {
    }

    public final void onAdClicked(Ad ad) {
        if (this.a.c != null) {
            this.a.c.onAdClick();
        }
        d.f(this.a.b, this.a.d);
    }
}
