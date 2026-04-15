package com.flurry.sdk;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;

public class ey extends jo {
    /* access modifiers changed from: private|static|final */
    public static final String b = ey.class.getSimpleName();

    static /* synthetic */ void a(eq eqVar, int i) {
        if (eqVar != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("event", eqVar.a);
            hashMap.put(MASTNativeAdConstants.RESPONSE_URL, eqVar.f);
            hashMap.put("response", String.valueOf(i));
            n.a().a(eqVar.b, dg.EV_SEND_URL_STATUS_RESULT, true, hashMap);
        }
    }

    /* access modifiers changed from: protected|final */
    public final ii a() {
        return new ii(hz.a.b.getFileStreamPath(".yflurryreporter"), ".yflurryreporter", 3, new ez(this));
    }

    /* access modifiers changed from: protected|final */
    public final void a(eq eqVar) {
        iw.a(3, b, "Sending next report for original url: " + eqVar.f + " to current url:" + eqVar.g);
        jc jcVar = new jc();
        jcVar.e = eqVar.g;
        jcVar.j = 100000;
        jcVar.f = jj.kGet;
        jcVar.g = false;
        jcVar.a = new fa(this, eqVar);
        hy.a().a((Object) this, (ma) jcVar);
    }
}
