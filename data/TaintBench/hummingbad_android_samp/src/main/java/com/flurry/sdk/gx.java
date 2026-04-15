package com.flurry.sdk;

import android.content.Context;
import java.util.Map;

public final class gx {
    public static void a(dg dgVar, Map map, Context context, af afVar, ct ctVar, int i) {
        if (context != null && afVar != null && ctVar != null) {
            b bVar = new b(dgVar, map, context, afVar, ctVar);
            ij cVar = new c();
            cVar.a = bVar;
            cVar.b = i;
            il.a().a(cVar);
        }
    }
}
