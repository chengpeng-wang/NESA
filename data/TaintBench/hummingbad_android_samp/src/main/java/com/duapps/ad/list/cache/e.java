package com.duapps.ad.list.cache;

import com.duapps.ad.list.AdListArrivalListener;
import java.util.List;

/* compiled from: NativeListRequestManagerProxy */
final class e implements Runnable {
    private /* synthetic */ List a;
    private /* synthetic */ d b;

    e(d dVar, List list) {
        this.b = dVar;
        this.a = list;
    }

    public final void run() {
        AdListArrivalListener a = this.b.a.j;
        if (a != null) {
            a.onAdLoaded(this.a);
        }
    }
}
