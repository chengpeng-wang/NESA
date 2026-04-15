package com.duapps.ad;

/* compiled from: ADRequestController */
final class e implements Runnable {
    private /* synthetic */ b a;

    e(b bVar) {
        this.a = bVar;
    }

    public final void run() {
        DuAdDataCallBack a = this.a.a.e;
        if (a != null) {
            a.onAdClick();
        }
    }
}
