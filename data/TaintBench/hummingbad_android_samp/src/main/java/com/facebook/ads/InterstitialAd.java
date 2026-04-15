package com.facebook.ads;

import android.content.Context;
import android.view.View;
import com.facebook.ads.internal.a;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.c;
import com.facebook.ads.internal.h;
import com.facebook.ads.internal.util.g;

public class InterstitialAd implements Ad {
    private static final c a = c.ADS;
    private final Context b;
    private final String c;
    /* access modifiers changed from: private */
    public h d;
    /* access modifiers changed from: private */
    public boolean e;
    /* access modifiers changed from: private */
    public boolean f;
    /* access modifiers changed from: private */
    public InterstitialAdListener g;
    /* access modifiers changed from: private */
    public ImpressionListener h;

    public InterstitialAd(Context context, String str) {
        this.b = context;
        this.c = str;
    }

    public void destroy() {
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
    }

    public boolean isAdLoaded() {
        return this.e;
    }

    public void loadAd() {
        this.e = false;
        if (this.f) {
            throw new IllegalStateException("InterstitialAd cannot be loaded while being displayed. Make sure your adapter calls adapterListener.onInterstitialDismissed().");
        }
        if (this.d != null) {
            this.d.d();
            this.d = null;
        }
        AdSize adSize = AdSize.INTERSTITIAL;
        this.d = new h(this.b, this.c, g.a(AdSize.INTERSTITIAL), adSize, a, 1, true);
        this.d.a(new a() {
            public void a() {
                InterstitialAd.this.e = true;
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onAdLoaded(InterstitialAd.this);
                }
            }

            public void a(View view) {
            }

            public void a(b bVar) {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onError(InterstitialAd.this, bVar.b());
                }
            }

            public void b() {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onAdClicked(InterstitialAd.this);
                }
            }

            public void c() {
                if (InterstitialAd.this.h != null) {
                    InterstitialAd.this.h.onLoggingImpression(InterstitialAd.this);
                }
                if ((InterstitialAd.this.g instanceof ImpressionListener) && InterstitialAd.this.g != InterstitialAd.this.h) {
                    ((ImpressionListener) InterstitialAd.this.g).onLoggingImpression(InterstitialAd.this);
                }
            }

            public void d() {
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onInterstitialDisplayed(InterstitialAd.this);
                }
            }

            public void e() {
                InterstitialAd.this.f = false;
                if (InterstitialAd.this.d != null) {
                    InterstitialAd.this.d.d();
                    InterstitialAd.this.d = null;
                }
                if (InterstitialAd.this.g != null) {
                    InterstitialAd.this.g.onInterstitialDismissed(InterstitialAd.this);
                }
            }
        });
        this.d.b();
    }

    public void setAdListener(InterstitialAdListener interstitialAdListener) {
        this.g = interstitialAdListener;
    }

    public void setImpressionListener(ImpressionListener impressionListener) {
        this.h = impressionListener;
    }

    public boolean show() {
        if (this.e) {
            this.d.c();
            this.f = true;
            this.e = false;
            return true;
        } else if (this.g == null) {
            return false;
        } else {
            this.g.onError(this, AdError.INTERNAL_ERROR);
            return false;
        }
    }
}
