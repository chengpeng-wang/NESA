package com.facebook.ads;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.c;
import com.facebook.ads.internal.h;
import com.facebook.ads.internal.util.g;

public class AdView extends RelativeLayout implements Ad {
    private static final c a = c.ADS;
    /* access modifiers changed from: private|final */
    public final DisplayMetrics b;
    /* access modifiers changed from: private|final */
    public final AdSize c;
    /* access modifiers changed from: private */
    public h d;
    private volatile boolean e;
    /* access modifiers changed from: private */
    public AdListener f;
    /* access modifiers changed from: private */
    public ImpressionListener g;
    /* access modifiers changed from: private */
    public View h;
    /* access modifiers changed from: private */
    public boolean i = false;

    public AdView(Context context, String str, AdSize adSize) {
        super(context);
        if (adSize == null || adSize == AdSize.INTERSTITIAL) {
            throw new IllegalArgumentException("adSize");
        }
        this.b = getContext().getResources().getDisplayMetrics();
        this.c = adSize;
        this.d = new h(context, str, g.a(adSize), adSize, a, 1, false);
        this.d.a(new a() {
            public void a() {
                if (AdView.this.d != null) {
                    AdView.this.d.c();
                }
            }

            public void a(View view) {
                if (view == null) {
                    throw new IllegalStateException("Cannot present null view");
                }
                AdView.this.i = true;
                AdView.this.h = view;
                AdView.this.removeAllViews();
                AdView.this.addView(AdView.this.h);
                if (AdView.this.h instanceof com.facebook.ads.internal.view.a) {
                    g.a(AdView.this.b, AdView.this.h, AdView.this.c);
                }
                if (AdView.this.f != null) {
                    AdView.this.f.onAdLoaded(AdView.this);
                }
            }

            public void a(b bVar) {
                if (AdView.this.f != null) {
                    AdView.this.f.onError(AdView.this, bVar.b());
                }
            }

            public void b() {
                if (AdView.this.f != null) {
                    AdView.this.f.onAdClicked(AdView.this);
                }
            }

            public void c() {
                if (AdView.this.g != null) {
                    AdView.this.g.onLoggingImpression(AdView.this);
                }
                if ((AdView.this.f instanceof ImpressionListener) && AdView.this.f != AdView.this.g) {
                    ((ImpressionListener) AdView.this.f).onLoggingImpression(AdView.this);
                }
            }
        });
    }

    public void destroy() {
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
        removeAllViews();
        this.h = null;
    }

    public void disableAutoRefresh() {
        if (this.d != null) {
            this.d.h();
        }
    }

    public void loadAd() {
        if (!this.e) {
            this.d.b();
            this.e = true;
        } else if (this.d != null) {
            this.d.g();
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.h != null) {
            g.a(this.b, this.h, this.c);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.d != null) {
            if (i == 0) {
                this.d.f();
            } else if (i == 8) {
                this.d.e();
            }
        }
    }

    public void setAdListener(AdListener adListener) {
        this.f = adListener;
    }

    public void setImpressionListener(ImpressionListener impressionListener) {
        this.g = impressionListener;
    }
}
