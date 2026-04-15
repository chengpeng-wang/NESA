package com.cleanmaster.ui.app.market;

import android.text.TextUtils;
import com.picksinit.c;

/* compiled from: ParseUrlUtils */
class k implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ j b;

    k(j jVar, String str) {
        this.b = jVar;
        this.a = str;
    }

    public void run() {
        if (TextUtils.isEmpty(this.a) || c.a(this.a)) {
            this.b.d.c(this.a);
            return;
        }
        n nVar = new n(c.getInstance().getContext());
        nVar.a(new l(this));
        nVar.a(this.a, this.b.a, this.b.b, this.b.c);
    }
}
