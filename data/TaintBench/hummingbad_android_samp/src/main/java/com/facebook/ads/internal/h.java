package com.facebook.ads.internal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.adapters.AdAdapter;
import com.facebook.ads.internal.adapters.BannerAdapter;
import com.facebook.ads.internal.adapters.BannerAdapterListener;
import com.facebook.ads.internal.adapters.InterstitialAdapter;
import com.facebook.ads.internal.adapters.InterstitialAdapterListener;
import com.facebook.ads.internal.adapters.n;
import com.facebook.ads.internal.adapters.o;
import com.facebook.ads.internal.dto.d;
import com.facebook.ads.internal.dto.e;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.r;
import com.facebook.ads.internal.util.t;
import java.util.HashMap;

public class h implements com.facebook.ads.internal.server.a.a {
    private static final String b = h.class.getSimpleName();
    protected a a;
    private final Context c;
    private final String d;
    private final com.facebook.ads.internal.server.a e = new com.facebook.ads.internal.server.a();
    /* access modifiers changed from: private|final */
    public final Handler f;
    /* access modifiers changed from: private|final */
    public final Runnable g;
    private final Runnable h;
    /* access modifiers changed from: private|volatile */
    public volatile boolean i;
    /* access modifiers changed from: private */
    public boolean j;
    /* access modifiers changed from: private|volatile */
    public volatile boolean k;
    /* access modifiers changed from: private */
    public AdAdapter l;
    /* access modifiers changed from: private */
    public View m;
    /* access modifiers changed from: private */
    public com.facebook.ads.internal.dto.c n;
    /* access modifiers changed from: private */
    public e o;
    private e p;
    private c q;
    private AdSize r;
    private int s;
    private final c t = new c(this, null);
    private boolean u;

    class c extends BroadcastReceiver {
        private c() {
        }

        /* synthetic */ c(h hVar, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                h.this.q();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                h.this.p();
            }
        }
    }

    static final class a extends t<h> {
        public a(h hVar) {
            super(hVar);
        }

        public final void run() {
            h hVar = (h) a();
            if (hVar != null) {
                hVar.i = false;
                hVar.m();
            }
        }
    }

    static final class b extends t<h> {
        public b(h hVar) {
            super(hVar);
        }

        public final void run() {
            h hVar = (h) a();
            if (hVar != null) {
                hVar.p();
            }
        }
    }

    public h(Context context, String str, e eVar, AdSize adSize, c cVar, int i, boolean z) {
        this.c = context;
        this.d = str;
        this.p = eVar;
        this.r = adSize;
        this.q = cVar;
        this.s = i;
        this.e.a((com.facebook.ads.internal.server.a.a) this);
        this.f = new Handler();
        this.g = new a(this);
        this.h = new b(this);
        this.j = z;
        i();
    }

    /* access modifiers changed from: private */
    public void a(AdAdapter adAdapter) {
        if (adAdapter != null) {
            adAdapter.onDestroy();
        }
    }

    private void i() {
        if (!this.j) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.c.registerReceiver(this.t, intentFilter);
            this.u = true;
        }
    }

    private void j() {
        if (this.u) {
            try {
                this.c.unregisterReceiver(this.t);
                this.u = false;
            } catch (Exception e) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(e, "Error unregistering screen state receiever"));
            }
        }
    }

    /* access modifiers changed from: private */
    public void k() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Adapter listener must be called on the main thread.");
        }
    }

    /* access modifiers changed from: private */
    public AdPlacementType l() {
        return this.r == null ? AdPlacementType.NATIVE : this.r == AdSize.INTERSTITIAL ? AdPlacementType.INTERSTITIAL : AdPlacementType.BANNER;
    }

    /* access modifiers changed from: private */
    public void m() {
        this.o = new e(this.c, this.d, this.r, this.p, this.q, this.s, AdSettings.isTestMode(this.c));
        this.e.a(this.c, this.o);
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void n() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                try {
                    h.this.o();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void o() {
        com.facebook.ads.internal.dto.c cVar = this.n;
        com.facebook.ads.internal.dto.a c = cVar.c();
        if (c == null) {
            this.a.a(AdErrorType.NO_FILL.getAdErrorWrapper(""));
            p();
            return;
        }
        String str = c.b;
        AdAdapter a = com.facebook.ads.internal.adapters.e.a(str, cVar.a().a());
        if (a == null) {
            Log.e(b, "Adapter does not exist: " + str);
            n();
        } else if (l() != a.getPlacementType()) {
            this.a.a(AdErrorType.INTERNAL_ERROR.getAdErrorWrapper(""));
        } else {
            HashMap hashMap = new HashMap();
            d a2 = cVar.a();
            hashMap.put("data", c.c);
            hashMap.put("definition", a2);
            if (this.o == null) {
                this.a.a(AdErrorType.UNKNOWN_ERROR.getAdErrorWrapper("environment is empty"));
            }
            switch (a.getPlacementType()) {
                case INTERSTITIAL:
                    final InterstitialAdapter interstitialAdapter = (InterstitialAdapter) a;
                    final AnonymousClass6 anonymousClass6 = new Runnable() {
                        public void run() {
                            h.this.a(interstitialAdapter);
                            h.this.n();
                        }
                    };
                    this.f.postDelayed(anonymousClass6, 10000);
                    interstitialAdapter.loadInterstitialAd(this.c, new InterstitialAdapterListener() {
                        public void onInterstitialAdClicked(InterstitialAdapter interstitialAdapter, String str, boolean z) {
                            h.this.k();
                            h.this.a.b();
                            Object obj = !r.a(str) ? 1 : null;
                            if (z && obj != null) {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                if (!(h.this.o.d instanceof Activity)) {
                                    intent.addFlags(268435456);
                                }
                                intent.setData(Uri.parse(str));
                                h.this.o.d.startActivity(intent);
                            }
                        }

                        public void onInterstitialAdDismissed(InterstitialAdapter interstitialAdapter) {
                            h.this.k();
                            h.this.a.e();
                        }

                        public void onInterstitialAdDisplayed(InterstitialAdapter interstitialAdapter) {
                            h.this.k();
                            h.this.a.d();
                        }

                        public void onInterstitialAdLoaded(InterstitialAdapter interstitialAdapter) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass6);
                            h.this.l = interstitialAdapter;
                            h.this.a.a();
                            h.this.p();
                        }

                        public void onInterstitialError(InterstitialAdapter interstitialAdapter, AdError adError) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass6);
                            h.this.a((AdAdapter) interstitialAdapter);
                            h.this.n();
                        }

                        public void onInterstitialLoggingImpression(InterstitialAdapter interstitialAdapter) {
                            h.this.k();
                            h.this.a.c();
                        }
                    }, hashMap);
                    return;
                case BANNER:
                    final BannerAdapter bannerAdapter = (BannerAdapter) a;
                    final AnonymousClass4 anonymousClass4 = new Runnable() {
                        public void run() {
                            h.this.a(bannerAdapter);
                            h.this.n();
                        }
                    };
                    this.f.postDelayed(anonymousClass4, 10000);
                    bannerAdapter.loadBannerAd(this.c, this.r, new BannerAdapterListener() {
                        public void onBannerAdClicked(BannerAdapter bannerAdapter) {
                            h.this.k();
                            h.this.a.b();
                        }

                        public void onBannerAdExpanded(BannerAdapter bannerAdapter) {
                            h.this.k();
                            h.this.q();
                        }

                        public void onBannerAdLoaded(BannerAdapter bannerAdapter, View view) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass4);
                            AdAdapter g = h.this.l;
                            h.this.l = bannerAdapter;
                            h.this.m = view;
                            if (h.this.k) {
                                h.this.a.a(view);
                                h.this.a(g);
                                h.this.p();
                                return;
                            }
                            h.this.a.a();
                        }

                        public void onBannerAdMinimized(BannerAdapter bannerAdapter) {
                            h.this.k();
                            h.this.p();
                        }

                        public void onBannerError(BannerAdapter bannerAdapter, AdError adError) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass4);
                            h.this.a((AdAdapter) bannerAdapter);
                            h.this.n();
                        }

                        public void onBannerLoggingImpression(BannerAdapter bannerAdapter) {
                            h.this.k();
                            h.this.a.c();
                        }
                    }, hashMap);
                    return;
                case NATIVE:
                    final n nVar = (n) a;
                    final AnonymousClass8 anonymousClass8 = new Runnable() {
                        public void run() {
                            h.this.a(nVar);
                            h.this.n();
                        }
                    };
                    this.f.postDelayed(anonymousClass8, 10000);
                    nVar.a(this.c, new o() {
                        public void a(n nVar) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass8);
                            h.this.l = nVar;
                            h.this.a.a();
                        }

                        public void a(n nVar, AdError adError) {
                            h.this.k();
                            h.this.f.removeCallbacks(anonymousClass8);
                            h.this.a((AdAdapter) nVar);
                            h.this.n();
                        }
                    }, hashMap);
                    return;
                default:
                    Log.e(b, "attempt unexpected adapter type");
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void p() {
        if (!this.j && !this.i) {
            switch (l()) {
                case INTERSTITIAL:
                    if (!g.a(this.c)) {
                        this.f.postDelayed(this.h, 1000);
                        break;
                    }
                    break;
                case BANNER:
                    int e = this.n == null ? 1 : this.n.a().e();
                    if (!(this.m == null || g.a(this.c, this.m, e))) {
                        this.f.postDelayed(this.h, 1000);
                        return;
                    }
                default:
                    return;
            }
            long b = this.n == null ? 30000 : this.n.a().b();
            if (b > 0) {
                this.f.postDelayed(this.g, b);
                this.i = true;
            }
        }
    }

    /* access modifiers changed from: private */
    public void q() {
        if (this.i) {
            this.f.removeCallbacks(this.g);
            this.i = false;
        }
    }

    public d a() {
        return this.n == null ? null : this.n.a();
    }

    public void a(a aVar) {
        this.a = aVar;
    }

    public synchronized void a(final b bVar) {
        this.f.post(new Runnable() {
            public void run() {
                h.this.a.a(bVar);
                if (!h.this.j && !h.this.i) {
                    switch (bVar.a().getErrorCode()) {
                        case 1000:
                        case 1002:
                            switch (AnonymousClass2.a[h.this.l().ordinal()]) {
                                case 2:
                                    h.this.f.postDelayed(h.this.g, 30000);
                                    h.this.i = true;
                                    return;
                                default:
                                    return;
                            }
                        default:
                            return;
                    }
                }
            }
        });
    }

    public synchronized void a(final com.facebook.ads.internal.server.d dVar) {
        this.f.post(new Runnable() {
            public void run() {
                com.facebook.ads.internal.dto.c b = dVar.b();
                if (b == null || b.a() == null) {
                    throw new IllegalStateException("invalid placement in response");
                }
                h.this.n = b;
                h.this.n();
            }
        });
    }

    public void b() {
        m();
    }

    public void c() {
        if (this.l == null) {
            throw new IllegalStateException("no adapter ready to start");
        } else if (this.k) {
            throw new IllegalStateException("ad already started");
        } else {
            this.k = true;
            switch (this.l.getPlacementType()) {
                case INTERSTITIAL:
                    ((InterstitialAdapter) this.l).show();
                    return;
                case BANNER:
                    if (this.m != null) {
                        this.a.a(this.m);
                        p();
                        return;
                    }
                    return;
                case NATIVE:
                    n nVar = (n) this.l;
                    if (nVar.z()) {
                        this.a.a(nVar);
                        return;
                    }
                    throw new IllegalStateException("ad is not ready or already displayed");
                default:
                    Log.e(b, "start unexpected adapter type");
                    return;
            }
        }
    }

    public void d() {
        j();
        if (this.k) {
            q();
            a(this.l);
            this.m = null;
            this.k = false;
        }
    }

    public void e() {
        if (this.k) {
            q();
        }
    }

    public void f() {
        if (this.k) {
            p();
        }
    }

    public void g() {
        q();
        m();
    }

    public void h() {
        this.j = true;
        q();
    }
}
