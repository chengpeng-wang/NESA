package com.cleanmaster.ui.app.market.loader;

import android.os.Handler;
import android.os.Message;

/* compiled from: AsyncTaskEx */
class g extends Handler {
    private g() {
    }

    /* synthetic */ g(b bVar) {
        this();
    }

    public void handleMessage(Message msg) {
        f fVar = (f) msg.obj;
        switch (msg.what) {
            case 1:
                fVar.a.e(fVar.b[0]);
                return;
            case 2:
                fVar.a.b(fVar.b);
                return;
            default:
                return;
        }
    }
}
