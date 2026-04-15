package com.flurry.sdk;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.TextUtils;
import android.webkit.WebSettings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gq {
    private static final String d = gq.class.getSimpleName();
    public dp a;
    public g b;
    public String c;
    private final Map e;
    private j f;
    private ii g;
    private volatile long h;
    private volatile long i;
    private volatile long j;

    public gq() {
        this.e = Collections.synchronizedMap(new HashMap());
        this.h = 0;
        this.i = 0;
        this.j = 0;
    }

    public gq(Context context) {
        String str = null;
        this.e = Collections.synchronizedMap(new HashMap());
        this.h = 0;
        this.i = 0;
        this.j = 0;
        this.a = new dp();
        this.f = new j();
        this.b = new g();
        this.g = new ii(context.getFileStreamPath(".yflurryadlog." + Long.toString(lt.f(hz.a.d), 16)), ".yflurryadlog.", 1, new gr(this));
        if (context != null) {
            if (context != null && VERSION.SDK_INT >= 17) {
                str = WebSettings.getDefaultUserAgent(context);
            }
            if (TextUtils.isEmpty(str)) {
                str = System.getProperty("http.agent");
            }
        }
        this.c = str;
        hz.a.b(new gt(this));
        j jVar = this.f;
        iw.a(3, j.a, "Registered Event Handler ");
        il.a().a("com.flurry.android.impl.ads.AdEvent", jVar.b);
        n.a().f.b();
        hz.a.b(new gu(this));
        hz.a.b(new gv(this));
        if ((ie.a().a != null ? 1 : 0) == 0) {
            n.a().b.b(context);
        }
        this.h = System.currentTimeMillis();
        this.i = SystemClock.elapsedRealtime();
        hz.a.b(new gw(this));
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void b() {
        Object obj;
        List a = ha.a(new ArrayList(this.e.values()));
        if (a.isEmpty()) {
            iw.a(3, d, "List of adLogs is empty");
            obj = null;
        } else {
            String str = hz.a.d;
            List e = ha.e();
            em emVar = new em();
            emVar.a = str;
            emVar.b = e;
            emVar.c = a;
            emVar.f = false;
            emVar.d = System.currentTimeMillis();
            emVar.e = Integer.toString(ia.a());
            iw.a(3, d, "Got ad log request:" + emVar.toString());
            em obj2 = emVar;
        }
        if (obj2 != null) {
            fd fdVar = n.a().c;
            u a2 = u.a();
            StringBuilder stringBuilder = new StringBuilder();
            String str2 = a2.b != null ? a2.b : u.b() ? "https://adlog.flurry.com" : "http://adlog.flurry.com";
            String stringBuilder2 = stringBuilder.append(str2).append("/v2/postAdLog.do").toString();
            String str3 = hz.a.d;
            String str4 = ia.a();
            if (TextUtils.isEmpty(stringBuilder2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
                iw.a(6, fdVar.b, "Ad log that has to be sent is EMPTY or NULL");
            } else {
                byte[] a3;
                try {
                    a3 = fdVar.a.a(obj2);
                } catch (Exception e2) {
                    iw.a(5, fdVar.b, "Failed to encode sdk log request: " + e2);
                    a3 = null;
                }
                if (a3 != null) {
                    a3 = fd.a(a3, stringBuilder2);
                    if (a3.length == 0) {
                        iw.a(6, fdVar.b, "Report that has to be sent is EMPTY or NULL");
                    } else {
                        hz.a.b(new ka(fdVar, a3, str3, str4));
                        fdVar.b();
                    }
                }
            }
        }
        this.e.clear();
        this.g.b();
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void c() {
        iw.a(4, d, "Loading AdLog data.");
        List<cz> list = (List) this.g.a();
        if (list != null) {
            for (cz czVar : list) {
                this.e.put(czVar.c, czVar);
            }
        }
    }

    public final cz a(String str) {
        cz czVar = (cz) this.e.get(str);
        if (czVar == null) {
            czVar = new cz(str);
            if (this.e.size() < 32767) {
                this.e.put(czVar.c, czVar);
            }
        }
        return czVar;
    }

    public final synchronized void a() {
        hz.a.b(new gs(this));
    }

    public final synchronized void a(String str, dg dgVar, boolean z, Map map) {
        if (dgVar != null) {
            iw.a(3, d, "logAdEvent(" + str + ", " + dgVar + ", " + z + ", " + map + ")");
            cz a = a(str);
            String str2 = dgVar.J;
            hg a2 = hg.a();
            long elapsedRealtime = SystemClock.elapsedRealtime() - a2.b;
            if (elapsedRealtime <= a2.c) {
                elapsedRealtime = a2.c + 1;
                a2.c = elapsedRealtime;
            }
            a2.c = elapsedRealtime;
            a.d.add(new cv(str2, z, a2.c, map));
        }
    }
}
