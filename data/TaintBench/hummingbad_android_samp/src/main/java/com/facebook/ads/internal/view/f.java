package com.facebook.ads.internal.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.internal.adapters.k;
import com.facebook.ads.internal.adapters.l;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.c;
import com.facebook.ads.internal.util.h;
import com.facebook.ads.internal.view.h.a;

public class f implements h {
    /* access modifiers changed from: private|static|final */
    public static final String a = f.class.getSimpleName();
    /* access modifiers changed from: private */
    public a b;
    private a c;
    private k d;
    /* access modifiers changed from: private */
    public l e;
    private long f = System.currentTimeMillis();
    /* access modifiers changed from: private */
    public long g;
    /* access modifiers changed from: private */
    public b.a h;

    public f(final InterstitialAdActivity interstitialAdActivity, a aVar) {
        this.b = aVar;
        this.c = new a(interstitialAdActivity, new a.a() {
            public void a() {
                f.this.e.c();
            }

            public void a(int i) {
            }

            public void a(String str) {
                Uri parse = Uri.parse(str);
                if ("fbad".equals(parse.getScheme()) && "close".equals(parse.getAuthority())) {
                    interstitialAdActivity.finish();
                    return;
                }
                f.this.b.a("com.facebook.ads.interstitial.clicked");
                com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(interstitialAdActivity, parse);
                if (a != null) {
                    try {
                        f.this.h = a.a();
                        f.this.g = System.currentTimeMillis();
                        a.b();
                    } catch (Exception e) {
                        Log.e(f.a, "Error executing action", e);
                    }
                }
            }

            public void b() {
                f.this.e.a();
            }
        }, 1);
        this.c.setId(100001);
        this.c.setLayoutParams(new LayoutParams(-1, -1));
        this.e = new l(interstitialAdActivity, this.c, new com.facebook.ads.internal.adapters.b() {
            public void d() {
                f.this.b.a("com.facebook.ads.interstitial.impression.logged");
            }
        });
        this.e.d();
        aVar.a(this.c);
    }

    public void a() {
        if (this.c != null) {
            this.c.onPause();
        }
    }

    public void a(Intent intent, Bundle bundle) {
        if (bundle == null || !bundle.containsKey("dataModel")) {
            this.d = k.b(intent);
            if (this.d != null) {
                this.e.a(this.d);
                this.c.loadDataWithBaseURL(h.a(), this.d.d(), "text/html", "utf-8", null);
                this.c.a(this.d.j(), this.d.k());
                return;
            }
            return;
        }
        this.d = k.a(bundle.getBundle("dataModel"));
        if (this.d != null) {
            this.c.loadDataWithBaseURL(h.a(), this.d.d(), "text/html", "utf-8", null);
            this.c.a(this.d.j(), this.d.k());
        }
    }

    public void a(Bundle bundle) {
        if (this.d != null) {
            bundle.putBundle("dataModel", this.d.l());
        }
    }

    public void b() {
        if (!(this.g <= 0 || this.h == null || this.d == null)) {
            c.a(b.a(this.g, this.h, this.d.i()));
        }
        if (this.c != null) {
            this.c.onResume();
        }
    }

    public void c() {
        if (this.d != null) {
            c.a(b.a(this.f, b.a.XOUT, this.d.i()));
        }
        if (this.c != null) {
            h.a(this.c);
            this.c.destroy();
            this.c = null;
        }
    }
}
