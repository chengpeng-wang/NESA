package com.flurry.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.Collections;

public abstract class ab implements af {
    private static final String e = ab.class.getSimpleName();
    public final int a;
    public final fl b;
    ct c;
    ct d;
    private final WeakReference f;
    private final WeakReference g;
    private final String h;
    private boolean i = false;
    private final ik j = new ac(this);
    private final ik k = new ad(this);

    ab(Context context, String str) {
        n a = n.a();
        if (a == null) {
            throw new IllegalStateException("A session must be started before ad objects may be instantiated.");
        }
        this.a = ha.a();
        this.f = new WeakReference(context);
        this.g = new WeakReference(null);
        this.h = str;
        this.b = new fl(str);
        a.b.a(context, this);
        m();
        il.a().a("com.flurry.android.impl.ads.AdStateEvent", this.k);
    }

    private void m() {
        ln.a().a(this.j);
    }

    private void n() {
        ln.a().b(this.j);
    }

    public final void a() {
        n();
        il.a().a(this.k);
        this.i = false;
        n.a().b.b(e(), this);
        if (n.a().g != null) {
            ba.a((af) this);
        }
        this.b.a();
    }

    public final void a(ct ctVar) {
        this.c = ctVar;
    }

    /* access modifiers changed from: 0000 */
    public void a(d dVar) {
        if (e.kOnFetched.equals(dVar.b) || e.kOnFetchFailed.equals(dVar.b)) {
            int a = i().a();
            if (a == 0) {
                iw.a(3, e, "Starting ad request from EnsureCacheNotEmpty size: " + a);
                h().a((af) this, i());
            }
        }
        if (e.kOnAppExit.equals(dVar.b) && dVar.a.equals(this)) {
            this.i = true;
            ct ctVar = this.d;
            String str = dg.EV_AD_CLOSED.J;
            cu cuVar = (cu) ctVar.b.get(ctVar.d);
            if (!TextUtils.isEmpty(str) && cuVar.c.containsKey(str)) {
                cuVar.c.put(str, Boolean.FALSE);
            }
        }
    }

    public final void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.b.b();
            i().a(str);
        }
    }

    public final void a(boolean z) {
        iw.a(3, "VerifyPackageLog", "Getting nextAdUnit...  current cacheSize: " + i().a());
        this.b.c();
        if (i().a() != 0 || z) {
            this.b.a((af) this, h(), i());
            return;
        }
        iw.a(3, "VerifyPackageLog", "nextAdUnit() cacheSize is empty");
        d dVar = new d();
        dVar.a = this;
        dVar.b = e.kOnFetchFailed;
        dVar.a();
    }

    public final void b() {
        n();
    }

    public final void c() {
        l();
        m();
    }

    public final int d() {
        return this.a;
    }

    public final Context e() {
        return (Context) this.f.get();
    }

    public final ViewGroup f() {
        return (ViewGroup) this.g.get();
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        super.finalize();
        a();
    }

    public final fl g() {
        return this.b;
    }

    public final gc h() {
        return n.a().a.a(this.h).a;
    }

    public final am i() {
        return n.a().a.a(this.h).b;
    }

    public final ct j() {
        return this.d;
    }

    /* access modifiers changed from: 0000 */
    public void k() {
    }

    /* access modifiers changed from: protected */
    public void l() {
        ct ctVar = this.d;
        String str = dg.EV_AD_CLOSED.J;
        cu cuVar = (cu) ctVar.b.get(ctVar.d);
        int i = (TextUtils.isEmpty(str) || (cuVar.c.containsKey(str) && ((Boolean) cuVar.c.get(str)).equals(Boolean.TRUE))) ? 0 : 1;
        if (i != 0) {
            gx.a(dg.EV_AD_CLOSED, Collections.emptyMap(), e(), this, this.d, 0);
            ctVar = this.d;
            str = dg.EV_AD_CLOSED.J;
            cuVar = (cu) ctVar.b.get(ctVar.d);
            if (!TextUtils.isEmpty(str) && cuVar.c.containsKey(str)) {
                cuVar.c.put(str, Boolean.TRUE);
            }
        }
    }
}
