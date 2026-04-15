package com.duapps.ad.list.a;

import android.os.SystemClock;
import com.duapps.ad.base.f;
import com.duapps.ad.entity.strategy.NativeAd;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager.Listener;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.util.HashSet;
import java.util.Iterator;

/* compiled from: FacebookAdsMananger */
final class d implements Listener {
    private e a = null;
    private /* synthetic */ c b;

    d(c cVar) {
        this.b = cVar;
    }

    public final void onAdsLoaded() {
        long currentTimeMillis = System.currentTimeMillis();
        int uniqueNativeAdCount = this.b.h.getUniqueNativeAdCount();
        int a = this.b.i - this.b.b();
        HashSet hashSet = new HashSet(uniqueNativeAdCount);
        for (int i = 0; i < uniqueNativeAdCount; i++) {
            this.a = new e(this.b.h.nextNativeAd(), this.b.e, this.b.f, currentTimeMillis);
            hashSet.add(this.a);
        }
        synchronized (this.b.j) {
            Iterator it = hashSet.iterator();
            int i2 = 0;
            while (it.hasNext() && i2 < a) {
                NativeAd nativeAd = (NativeAd) it.next();
                if (nativeAd == null) {
                    it.remove();
                } else {
                    this.b.j.add(nativeAd);
                    f.c("FacebookAdsMananger", nativeAd.getAdTitle() + "  has arrival.");
                    i2++;
                }
            }
        }
        this.b.b = false;
        com.duapps.ad.base.d.a(this.b.e, this.b.f, (int) CtaButton.WIDTH_DIPS, SystemClock.elapsedRealtime() - this.b.l);
        f.c("FacebookAdsMananger", "FB on ads loaded:" + this.b.m);
    }

    public final void onAdError(AdError adError) {
        this.b.a = true;
        this.b.b = false;
        com.duapps.ad.base.d.a(this.b.e, this.b.f, adError.getErrorCode(), SystemClock.elapsedRealtime() - this.b.l);
    }
}
