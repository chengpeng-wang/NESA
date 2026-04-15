package com.duapps.ad.b;

import android.content.Context;

/* compiled from: InMobiReport */
public final class l implements Runnable {
    private Context a;
    private boolean b;
    private a c;

    public l(Context context, boolean z, a aVar) {
        this.a = context.getApplicationContext();
        this.b = z;
        this.c = aVar;
    }

    public final void run() {
        if (this.b) {
            j.a(this.a).b(this.c);
        } else {
            j.a(this.a).a(this.c);
        }
    }
}
