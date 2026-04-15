package com.duapps.ad.list.cache;

import com.duapps.ad.AdError;
import com.duapps.ad.list.AdListArrivalListener;

/* compiled from: NativeListRequestManagerProxy */
final class f implements Runnable {
    private /* synthetic */ AdError a;
    private /* synthetic */ d b;

    f(d dVar, AdError adError) {
        this.b = dVar;
        this.a = adError;
    }

    public final void run() {
        AdListArrivalListener a = this.b.a.j;
        if (a != null) {
            a.onAdError(this.a);
        }
    }
}
