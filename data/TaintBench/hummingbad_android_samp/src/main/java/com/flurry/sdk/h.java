package com.flurry.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.mopub.common.Constants;
import java.util.Collections;

final class h extends lz {
    final /* synthetic */ String a;
    final /* synthetic */ Context b;
    final /* synthetic */ boolean c;
    final /* synthetic */ af d;
    final /* synthetic */ boolean e;
    final /* synthetic */ g f;

    h(g gVar, String str, Context context, boolean z, af afVar, boolean z2) {
        this.f = gVar;
        this.a = str;
        this.b = context;
        this.c = z;
        this.d = afVar;
        this.e = z2;
    }

    public final void safeRun() {
        int i = 1;
        if (TextUtils.isEmpty(this.a)) {
            iw.a(6, g.b, "Failed to launch: " + this.a);
            return;
        }
        String a = mb.a(this.a);
        if (!TextUtils.isEmpty(a)) {
            boolean a2 = mb.c(a) ? hd.a(this.b, a) : false;
            if (!a2 && mb.d(a)) {
                a2 = hd.b(this.b, a);
            }
            if (!a2) {
                int i2;
                if (TextUtils.isEmpty(a)) {
                    i2 = 0;
                } else {
                    Uri parse = Uri.parse(a);
                    i2 = (parse.getScheme() == null || !(parse.getScheme().equals(Constants.HTTP) || parse.getScheme().equals(Constants.HTTPS))) ? 0 : 1;
                }
                if (i2 != 0) {
                    Context context = this.b;
                    Intent a3 = hb.a(context, a);
                    if (!(a3 != null && lt.a(a3) && hd.a(context, a3))) {
                        i = 0;
                    }
                    if (i == 0 && this.c) {
                        ct j = this.d.j();
                        if (j.d()) {
                            ij heVar = new he();
                            gx.a(dg.INTERNAL_EV_APP_EXIT, Collections.emptyMap(), this.b, this.d, j, 0);
                            il.a().a(heVar);
                            return;
                        }
                        gx.a(dg.INTERNAL_EV_APP_EXIT, Collections.emptyMap(), this.b, this.d, j, 0);
                        return;
                    }
                    hd.c(this.b, a);
                }
            }
            boolean i3 = a2;
            if (i3 == 0) {
            }
            hd.c(this.b, a);
        }
    }
}
