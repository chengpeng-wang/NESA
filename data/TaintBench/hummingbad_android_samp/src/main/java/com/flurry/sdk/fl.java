package com.flurry.sdk;

import android.text.TextUtils;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class fl {
    /* access modifiers changed from: private|static|final */
    public static final String a = fl.class.getSimpleName();
    private final String b;
    private final TreeSet c;
    private final TreeSet d;
    /* access modifiers changed from: private */
    public gb e;
    /* access modifiers changed from: private */
    public af f;
    /* access modifiers changed from: private */
    public gc g;
    private am h;
    private ct i;
    private int j;
    private long k;
    private long l;
    private long m;
    private final ik n = new fm(this);
    private final ik o = new fr(this);
    private final ik p = new ft(this);

    public fl(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("adSpace cannot be null");
        }
        this.b = str;
        this.c = new TreeSet();
        this.d = new TreeSet();
        this.e = gb.NONE;
        c();
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(ct ctVar, df dfVar) {
        HashMap hashMap = new HashMap();
        hashMap.put("preRender", "true");
        String str = "errorCode";
        if (dfVar == null) {
            dfVar = df.kUnknown;
        }
        hashMap.put(str, Integer.toString(dfVar.m));
        gx.a(dg.EV_RENDER_FAILED, hashMap, this.f.e(), this.f, ctVar, 0);
    }

    private synchronized void a(ct ctVar, String str) {
        iw.a(3, a, "Pre-render: HTTP get for url: " + str);
        jc jcVar = new jc();
        jcVar.e = str;
        jcVar.j = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        jcVar.d = new ld();
        jcVar.a = new fq(this, str, ctVar);
        hy.a().a((Object) this, (ma) jcVar);
    }

    private synchronized void a(gb gbVar) {
        Object gbVar2;
        if (gbVar2 == null) {
            gbVar2 = gb.NONE;
        }
        iw.a(3, a, "Setting state from " + this.e + " to " + gbVar2 + " for adspace: " + this.b);
        if (gb.NONE.equals(this.e) && !gb.NONE.equals(gbVar2)) {
            iw.a(3, a, "Adding fetch listeners for adspace: " + this.b);
            ln.a().a(this.n);
            il.a().a("com.flurry.android.sdk.AssetStatusEvent", this.o);
            il.a().a("com.flurry.android.sdk.AdResponseEvent", this.p);
        } else if (gb.NONE.equals(gbVar2) && !gb.NONE.equals(this.e)) {
            iw.a(3, a, "Removing fetch listeners for adspace: " + this.b);
            ln.a().b(this.n);
            il.a().a(this.o);
            il.a().a(this.p);
        }
        this.e = gbVar2;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(String str, bw bwVar) {
        if (gb.SELECT.equals(this.e)) {
            ba baVar = n.a().g;
            if (ba.a(this.i, str)) {
                iw.a(3, a, "Detected asset status change for asset:" + str + " status:" + bwVar);
                if (bw.COMPLETE.equals(bwVar) || bw.ERROR.equals(bwVar)) {
                    hz.a.b(new fy(this));
                }
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void f() {
        if (gb.REQUEST.equals(this.e)) {
            this.c.addAll(this.h.b());
            if (!this.c.isEmpty()) {
                this.i = (ct) this.c.pollFirst();
            }
            a(gb.SELECT);
            hz.a.b(new fw(this));
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void g() {
        if (this.k > 0 && System.currentTimeMillis() > this.k) {
            gz.a(this.f, df.kUnfilled);
            c();
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void h() {
        if (this.l > 0 && System.currentTimeMillis() > this.l) {
            if (!gb.SELECT.equals(this.e) || this.i == null || this.i.f() || !this.i.e()) {
                k();
                j();
            } else {
                a(gb.PREPARE);
                hz.a.a(new fx(this));
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void i() {
        if (this.m > 0 && System.currentTimeMillis() > this.m) {
            hy.a().a((Object) this);
            a(this.i, df.kPrerenderDownloadTimeout);
            c();
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void j() {
        if (gb.SELECT.equals(this.e)) {
            if (this.f.e() == null) {
                gz.a(this.f, df.kNoContext);
                c();
            } else {
                ba baVar = n.a().g;
                while (this.i != null) {
                    List list = this.i.a.d;
                    if (list != null && !list.isEmpty()) {
                        if (!this.i.f() && !this.i.e()) {
                            iw.a(3, a, "Pre-caching not required for ad");
                            break;
                        }
                        bk a = baVar.a(this.i);
                        long j = (long) this.i.a.p;
                        iw.a(3, a, "Pre-caching required for ad, AdUnitCachedStatus: " + a + ", skip time limit: " + j);
                        if (!bk.COMPLETE.equals(a) && j > 0 && this.l == 0) {
                            iw.a(3, a, "Setting skip timer for " + j + " ms");
                            this.l = System.currentTimeMillis() + j;
                        }
                        if (bk.COMPLETE.equals(a)) {
                            iw.a(3, a, "Pre-caching completed, ad may proceed");
                            break;
                        } else if (bk.IN_PROGRESS.equals(a)) {
                            if (j != 0) {
                                if (j > 0) {
                                    if (System.currentTimeMillis() <= this.l) {
                                        iw.a(3, a, "Waiting for skip timer");
                                        break;
                                    } else {
                                        iw.a(3, a, "Skip timer expired");
                                        k();
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                iw.a(3, a, "No skip timer");
                                k();
                            }
                        } else if (j == 0) {
                            iw.a(3, a, "No skip timer");
                            k();
                        } else {
                            int i = this.j + 1;
                            this.j = i;
                            if (i <= 1) {
                                int b = baVar.b(this.i);
                                if (b > 0) {
                                    iw.a(3, a, "Requesting " + b + " asset(s), attempt #" + this.j);
                                } else {
                                    iw.a(3, a, "No assets to cache");
                                }
                            } else if (this.i.f()) {
                                iw.a(3, a, "Too many precaching attempts, precaching failed");
                                a(this.i, df.kPrecachingDownloadFailed);
                                c();
                            } else if (gb.SELECT.equals(this.e)) {
                                iw.a(3, a, "Too many precaching attempts, precaching failed. Trying streaming now.");
                            } else {
                                iw.a(3, a, "Do nothing. State change request tick must have started prepare.");
                            }
                        }
                    } else {
                        a(this.i, df.kInvalidAdUnit);
                        c();
                        break;
                    }
                }
                if (this.i == null) {
                    n.a().a(null, dg.EV_UNFILLED, true, Collections.emptyMap());
                    gz.a(this.f, df.kUnfilled);
                    c();
                } else {
                    a(gb.PREPARE);
                    hz.a.a(new fz(this));
                }
            }
        }
    }

    private synchronized void k() {
        if (gb.SELECT.equals(this.e)) {
            iw.a(3, a, "Precaching required for incomplete ad unit, skipping ad group -- adspace: " + this.b + " groupId: " + this.i.a.g);
            this.d.add(this.i);
            this.i = null;
            this.d.addAll(this.c);
            this.c.clear();
            this.c.addAll(this.h.b());
            if (!this.c.isEmpty()) {
                this.i = (ct) this.c.pollFirst();
            }
            f.a().a("precachingAdGroupSkipped");
            this.j = 0;
            this.l = 0;
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void l() {
        lt.a();
        if (gb.PREPARE.equals(this.e)) {
            iw.a(3, a, "Preparing ad");
            if (this.f.e() == null) {
                a(this.i, df.kNoContext);
                c();
            } else {
                gx.a(dg.EV_FILLED, Collections.emptyMap(), this.f.e(), this.f, this.i, 1);
                this.f.a(this.i);
                Object obj = null;
                for (a aVar : ha.a((dq) this.i.a.d.get(0), new b(dg.EV_FILLED, null, null, null, null))) {
                    obj = de.AC_VERIFY_PACKAGE.equals(aVar.a) ? 1 : obj;
                }
                if (obj != null) {
                    a(gb.FILLED);
                } else {
                    d();
                }
            }
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void m() {
        if (gb.PRERENDER.equals(this.e)) {
            iw.a(3, a, "Pre-rendering ad");
            List list = this.i.a.d;
            ba baVar = n.a().g;
            if (this.i.f()) {
                iw.a(3, a, "Precaching required for ad, copying assets");
                if (bk.COMPLETE.equals(baVar.a(this.i))) {
                    f.a().a("precachingAdAssetsAvailable");
                    if (!n.a().g.a(this.f, this.i)) {
                        iw.a(3, a, "Could not copy required ad assets");
                        f.a().a("precachingAdAssetCopyFailed");
                        a(this.i, df.kPrecachingCopyFailed);
                        c();
                    }
                } else {
                    iw.a(3, a, "Ad assets incomplete");
                    f.a().a("precachingAdAssetsIncomplete");
                    a(this.i, df.kPrecachingMissingAssets);
                    c();
                }
            } else if (this.i.e()) {
                iw.a(3, a, "Precaching optional for ad, copying assets");
                n.a().g.a(this.f, this.i);
            }
            gx.a(dg.EV_PREPARED, Collections.emptyMap(), this.f.e(), this.f, this.i, 0);
            dq dqVar = (dq) list.get(0);
            if (dqVar.a == dd.b) {
                iw.a(3, a, "Binding is HTML_URL, pre-render required");
                long j = this.i.a.o;
                if (j > 0) {
                    iw.a(3, a, "Setting pre-render timeout for " + j + " ms");
                    this.m = j + System.currentTimeMillis();
                }
                a(this.i, dqVar.b);
            } else {
                gz.a(this.f);
                c();
            }
        }
    }

    public final synchronized void a() {
        c();
        this.c.clear();
    }

    public final synchronized void a(af afVar, gc gcVar, am amVar) {
        if (!(afVar == null || gcVar == null || amVar == null)) {
            iw.a(3, a, "fetchAd: adObject=" + afVar);
            if (gb.NONE.equals(this.e) || gb.FILLED.equals(this.e)) {
                this.f = afVar;
                this.h = amVar;
                this.g = gcVar;
                if (ht.a().a) {
                    n.a().g.c();
                    if (this.c.isEmpty()) {
                        this.c.addAll(this.h.b());
                    }
                    if (this.c.isEmpty()) {
                        a(gb.REQUEST);
                        iw.a(3, a, "Setting ad request timeout for 15000 ms");
                        this.k = System.currentTimeMillis() + 15000;
                        iw.a(3, a, "AdCacheState: Cache empty. Fetching new ad.");
                        this.g.a(this.f, this.h);
                    } else {
                        iw.a(3, a, "AdCacheState: Found " + (this.h.a() + this.c.size()) + " ads in cache. Using 1 now.");
                        this.i = (ct) this.c.pollFirst();
                        a(gb.SELECT);
                        hz.a.b(new fv(this));
                    }
                } else {
                    iw.a(5, a, "There is no network connectivity (ad will not fetch)");
                    gz.a(this.f, df.kNoNetworkConnectivity);
                    c();
                }
            }
        }
    }

    public final synchronized void b() {
        this.c.clear();
    }

    public final synchronized void c() {
        iw.a(3, a, "Fetch finished for adObject:" + this.f + " adSpace:" + this.b);
        hy.a().a((Object) this);
        a(gb.NONE);
        if (this.h != null) {
            this.h.a(this.d);
        }
        this.d.clear();
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = 0;
        this.k = 0;
        this.l = 0;
        this.m = 0;
    }

    public final synchronized void d() {
        a(gb.PRERENDER);
        hz.a.b(new ga(this));
    }
}
