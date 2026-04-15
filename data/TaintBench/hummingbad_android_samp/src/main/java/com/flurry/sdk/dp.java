package com.flurry.sdk;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;

public final class dp {
    private final HashMap a = new HashMap();

    public final synchronized void a(b bVar) {
        if (bVar != null) {
            ec ecVar;
            List<ec> list = bVar.e.a.e;
            if (list != null) {
                for (ec ecVar2 : list) {
                    if (ed.STREAM.equals(ecVar2.a)) {
                        ecVar = ecVar2;
                        break;
                    }
                }
            }
            ecVar = null;
            if (ecVar != null) {
                String str = ecVar.b;
                if (!TextUtils.isEmpty(str)) {
                    do doVar = (do) this.a.get(str);
                    if (doVar == null) {
                        doVar = new do(str, ecVar.e);
                        this.a.put(str, doVar);
                    }
                    doVar.b = System.currentTimeMillis();
                    if (dg.EV_RENDERED.equals(bVar.a)) {
                        doVar.a = System.currentTimeMillis();
                    }
                    doVar.c = bVar.a.J;
                }
            }
        }
    }
}
