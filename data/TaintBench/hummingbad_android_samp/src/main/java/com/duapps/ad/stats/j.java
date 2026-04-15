package com.duapps.ad.stats;

import com.duapps.ad.base.d;
import com.duapps.ad.entity.a;
import org.json.JSONObject;

/* compiled from: ToolDataWrapper */
public final class j extends a {
    public a c;
    private String d;
    private long e;
    private int f;
    private String g;
    private int h;
    private d i;
    private String j;
    private int k;
    private boolean l;
    private boolean m;

    public j(a aVar) {
        super(aVar.n, aVar.o, aVar.p);
        this.c = aVar;
        this.e = aVar.a;
        int i = aVar.i;
        this.d = aVar.c;
        this.f = aVar.j;
        this.g = aVar.g;
        this.h = aVar.q;
        this.j = aVar.m;
        this.k = aVar.u;
    }

    public j(com.duapps.ad.b.a aVar) {
        super(aVar.a, aVar.b, aVar.d);
        this.e = aVar.e;
        this.d = null;
        this.f = aVar.f;
        this.g = aVar.n;
        this.h = aVar.g;
        this.j = aVar.c;
    }

    public final String a() {
        return this.d;
    }

    public final long b() {
        return this.e;
    }

    public final int c() {
        return this.f;
    }

    public final int d() {
        return this.h;
    }

    public final void a(d dVar) {
        this.i = dVar;
    }

    public final d e() {
        return this.i;
    }

    public final boolean f() {
        return this.f == 0;
    }

    public final boolean g() {
        return this.f == 1;
    }

    public final String h() {
        return this.g;
    }

    public final String i() {
        return this.j;
    }

    public final int j() {
        return this.k;
    }

    public final boolean k() {
        return this.l;
    }

    public final void a(boolean z) {
        this.l = true;
    }

    public final boolean l() {
        return this.m;
    }

    public final void b(boolean z) {
        this.m = z;
    }

    public static JSONObject a(j jVar) {
        JSONObject jSONObject = new JSONObject();
        if (jVar.c != null) {
            jSONObject.put("data", a.b(jVar.c));
        }
        return jSONObject;
    }

    public static j a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
        if ("download".equals(jSONObject2.optString(com.umeng.analytics.onlineconfig.a.c))) {
            return new j(a.a(jSONObject2));
        }
        if ("inmobi".equals(jSONObject2.optString(com.umeng.analytics.onlineconfig.a.c))) {
            return new j(com.duapps.ad.b.a.a(jSONObject2));
        }
        return null;
    }
}
