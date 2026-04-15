package com.cleanmaster.ui.app.market;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* compiled from: ParseWebViewUrlUtils */
class o extends Handler {
    final /* synthetic */ n a;

    o(n nVar, Looper looper) {
        this.a = nVar;
        super(looper);
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                this.a.b = true;
                this.a.a((String) msg.obj);
                return;
            case 2:
                if (this.a.a != null) {
                    this.a.a.stopLoading();
                    return;
                }
                return;
            default:
                return;
        }
    }
}
