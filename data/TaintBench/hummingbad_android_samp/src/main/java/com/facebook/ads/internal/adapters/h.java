package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.internal.dto.d;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.c;
import com.facebook.ads.internal.util.f;
import com.facebook.ads.internal.view.a;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class h extends BannerAdapter {
    /* access modifiers changed from: private|static|final */
    public static final String a = h.class.getSimpleName();
    private a b;
    /* access modifiers changed from: private */
    public l c;
    /* access modifiers changed from: private */
    public BannerAdapterListener d;
    private Map<String, Object> e;
    private j f;
    /* access modifiers changed from: private */
    public Context g;
    /* access modifiers changed from: private */
    public long h;
    /* access modifiers changed from: private */
    public b.a i;

    private void a(d dVar) {
        this.h = 0;
        this.i = null;
        final k a = k.a((JSONObject) this.e.get("data"));
        if (f.a(this.g, (f.a) a)) {
            this.d.onBannerError(this, AdError.NO_FILL);
            return;
        }
        this.b = new a(this.g, new a.a() {
            public void a() {
                h.this.c.c();
            }

            public void a(int i) {
                if (i == 0 && h.this.h > 0 && h.this.i != null) {
                    c.a(b.a(h.this.h, h.this.i, a.i()));
                    h.this.h = 0;
                    h.this.i = null;
                }
            }

            public void a(String str) {
                if (h.this.d != null) {
                    h.this.d.onBannerAdClicked(h.this);
                }
                com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(h.this.g, Uri.parse(str));
                if (a != null) {
                    try {
                        h.this.i = a.a();
                        h.this.h = System.currentTimeMillis();
                        a.b();
                    } catch (Exception e) {
                        Log.e(h.a, "Error executing action", e);
                    }
                }
            }

            public void b() {
                h.this.onViewableImpression();
            }
        }, dVar.e());
        this.b.a(dVar.f(), dVar.g());
        this.c = new l(this.g, this.b, new b() {
            public void d() {
                if (h.this.d != null) {
                    h.this.d.onBannerLoggingImpression(h.this);
                }
            }
        });
        this.c.a(a);
        this.b.loadDataWithBaseURL(com.facebook.ads.internal.util.h.a(), a.d(), "text/html", "utf-8", null);
        if (this.d != null) {
            this.d.onBannerAdLoaded(this, this.b);
        }
    }

    public void loadBannerAd(Context context, AdSize adSize, BannerAdapterListener bannerAdapterListener, Map<String, Object> map) {
        this.g = context;
        this.d = bannerAdapterListener;
        this.e = map;
        a((d) map.get("definition"));
    }

    public void onDestroy() {
        if (this.b != null) {
            com.facebook.ads.internal.util.h.a(this.b);
            this.b.destroy();
            this.b = null;
        }
        if (this.f != null) {
            this.f.onDestroy();
            this.f = null;
        }
    }

    public void onViewableImpression() {
        if (this.c != null) {
            this.c.a();
        } else if (this.f != null) {
            Map hashMap = new HashMap();
            hashMap.put("mil", Boolean.valueOf(false));
            this.f.a(hashMap);
        }
    }
}
