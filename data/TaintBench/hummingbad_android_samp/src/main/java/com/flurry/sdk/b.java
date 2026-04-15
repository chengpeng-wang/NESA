package com.flurry.sdk;

import android.content.Context;
import java.util.Map;

public final class b {
    public final dg a;
    public final Map b;
    final Context c;
    final af d;
    public final ct e;

    public b(dg dgVar, Map map, Context context, af afVar, ct ctVar) {
        this.a = dgVar;
        this.b = map;
        this.c = context;
        this.d = afVar;
        this.e = ctVar;
    }

    public final dq a() {
        ct ctVar = this.e;
        return ctVar.a(ctVar.d);
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("event=").append(this.a.toString());
        stringBuilder.append(",params=").append(this.b);
        if (this.e.a.b != null) {
            stringBuilder.append(",adspace=").append(this.e.a.b);
        }
        return stringBuilder.toString();
    }
}
