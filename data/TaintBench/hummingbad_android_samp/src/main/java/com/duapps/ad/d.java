package com.duapps.ad;

/* compiled from: ADRequestController */
final class d implements Runnable {
    private /* synthetic */ AdError a;
    private /* synthetic */ b b;

    d(b bVar, AdError adError) {
        this.b = bVar;
        this.a = adError;
    }

    public final void run() {
        DuAdDataCallBack a = this.b.a.e;
        if (a != null) {
            a.onAdError(this.a);
        }
    }
}
