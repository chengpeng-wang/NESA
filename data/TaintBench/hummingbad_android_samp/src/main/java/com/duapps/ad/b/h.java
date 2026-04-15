package com.duapps.ad.b;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.duapps.ad.base.f;
import com.moceanmobile.mast.Defaults;

/* compiled from: InMobiDataExecutor */
final class h extends Handler {
    private /* synthetic */ g a;

    h(g gVar, Looper looper) {
        this.a = gVar;
        super(looper);
    }

    public final void handleMessage(Message message) {
        if (message.what == 100) {
            g.a(this.a);
            f.c("InMobiDataExecutor", "loading js:" + this.a.g);
            if (this.a.c != null) {
                this.a.c.a.loadData(this.a.g, "text/html", Defaults.ENCODING_UTF_8);
            }
        }
    }
}
