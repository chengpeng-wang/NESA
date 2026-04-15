package com.duapps.ad.b;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* compiled from: InMobiDataQueue */
final class k extends Handler {
    private /* synthetic */ j a;

    k(j jVar, Looper looper) {
        this.a = jVar;
        super(looper);
    }

    public final void handleMessage(Message message) {
        if (message.what == 100) {
            for (int i = 1; i <= 3; i++) {
                n nVar = new n(this.a.e);
                nVar.b = i - 1;
                this.a.a.add(nVar);
            }
            if (this.a.g) {
                j.a(this.a, (n) this.a.a.get(0));
            }
        }
    }
}
