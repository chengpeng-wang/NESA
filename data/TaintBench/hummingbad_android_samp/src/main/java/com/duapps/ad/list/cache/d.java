package com.duapps.ad.list.cache;

import com.duapps.ad.AdError;
import com.duapps.ad.c.b.a;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.list.AdListArrivalListener;
import java.util.List;

/* compiled from: NativeListRequestManagerProxy */
final class d implements AdListArrivalListener {
    final /* synthetic */ b a;

    d(b bVar) {
        this.a = bVar;
    }

    public final void onAdLoaded(List<NativeAd> list) {
        if (a.a()) {
            AdListArrivalListener a = this.a.j;
            if (a != null) {
                a.onAdLoaded(list);
                return;
            }
            return;
        }
        a.a(new e(this, list));
    }

    public final void onAdError(AdError adError) {
        if (a.a()) {
            AdListArrivalListener a = this.a.j;
            if (a != null) {
                a.onAdError(adError);
                return;
            }
            return;
        }
        a.a(new f(this, adError));
    }
}
