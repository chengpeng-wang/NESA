package com.flurry.sdk;

import android.content.Context;
import java.util.List;
import java.util.Map;

public class n implements iz {
    private static final String j = n.class.getSimpleName();
    public aq a;
    public ae b;
    public fd c;
    public ey d;
    public v e;
    public dn f;
    public ba g;
    public dz h;
    public gq i;
    private final ik k = new o(this);
    private final ik l = new p(this);
    private ii m;
    private ii n;

    public static synchronized n a() {
        n nVar;
        synchronized (n.class) {
            nVar = (n) hz.a.a(n.class);
        }
        return nVar;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(long j, long j2) {
        if (!this.g.a()) {
            iw.a(3, j, "Precaching: initing from FlurryAdModule");
            this.g.a(j, j2);
            this.g.b();
            hz.a.b(new t(this));
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void c() {
        iw.a(4, j, "Loading FreqCap data.");
        List<dj> list = (List) this.m.a();
        if (list != null) {
            for (dj a : list) {
                this.f.a(a);
            }
        }
        this.f.b();
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void d() {
        if (this.g.a()) {
            iw.a(4, j, "Loading CachedAsset data.");
            List<bg> list = (List) this.n.a();
            if (list != null) {
                for (bg bgVar : list) {
                    ba baVar = this.g;
                    if (!(!baVar.a() || bgVar == null || bw.QUEUED.equals(bgVar.a()) || bw.IN_PROGRESS.equals(bgVar.a()))) {
                        baVar.a.a(bgVar);
                    }
                }
            }
        }
    }

    public final void a(Context context) {
        this.a = new aq();
        this.b = new ae();
        this.c = new fd();
        this.d = new ey();
        this.e = new v();
        this.f = new dn();
        this.g = new ba();
        this.h = null;
        il.a().a("com.flurry.android.sdk.ActivityLifecycleEvent", this.k);
        il.a().a("com.flurry.android.sdk.AdConfigurationEvent", this.l);
        this.m = new ii(context.getFileStreamPath(".yflurryfreqcap." + Long.toString(lt.f(hz.a.d), 16)), ".yflurryfreqcap.", 2, new q(this));
        this.n = new ii(context.getFileStreamPath(".yflurrycachedasset" + Long.toString(lt.f(hz.a.d), 16)), ".yflurrycachedasset", 1, new r(this));
        hz.a.b(new s(this));
    }

    public final void a(String str, dg dgVar, boolean z, Map map) {
        if (this.i != null) {
            this.i.a(str, dgVar, z, map);
        }
    }

    public final g b() {
        return this.i != null ? this.i.b : null;
    }
}
