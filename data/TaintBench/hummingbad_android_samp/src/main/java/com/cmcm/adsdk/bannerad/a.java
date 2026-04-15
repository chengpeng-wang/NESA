package com.cmcm.adsdk.bannerad;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.picksinit.c;

/* compiled from: AdmobBannerLoader */
public final class a extends c {
    /* access modifiers changed from: private */
    public IBannerRequestCallBack a;
    private AdView b;
    /* access modifiers changed from: private */
    public String c;

    /* compiled from: AdmobBannerLoader */
    class a extends AdListener {
        a() {
        }

        public final void onAdLoaded() {
            com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "onAdLoaded Ad");
            if (!TextUtils.isEmpty(a.this.c)) {
                c.getInstance().doRecommendAdViewReport("com.admob.banner", a.this.c, 3002);
            }
            if (a.this.a != null) {
                a.this.a.onAdLoaded(a.this);
            }
        }

        public final void onAdFailedToLoad(int errorCode) {
            com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "onAdFailedToLoad Ad errorcode :" + errorCode);
            if (a.this.a != null) {
                a.this.a.onAdLoadFailed();
            }
        }

        public final void onAdOpened() {
            com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "onAdOpened Ad ");
            if (!TextUtils.isEmpty(a.this.c)) {
                c.getInstance().doRecommendAdClickReport("com.admob.banner", a.this.c, 3002);
            }
            if (a.this.a != null) {
                a.this.a.onAdClicked();
            }
        }

        public final void onAdClosed() {
        }

        public final void onAdLeftApplication() {
        }
    }

    public a(Context context, String str, CMBannerAdSize cMBannerAdSize, String str2) {
        if (context == null || TextUtils.isEmpty(str) || cMBannerAdSize == null || TextUtils.isEmpty(str2)) {
            com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "incomplete parameter");
            return;
        }
        AdSize adSize;
        switch (cMBannerAdSize) {
            case BANNER_320_50:
                adSize = AdSize.BANNER;
                break;
            default:
                adSize = AdSize.SMART_BANNER;
                break;
        }
        if (adSize instanceof AdSize) {
            this.b = new AdView(context);
            this.b.setAdUnitId(str);
            this.b.setAdSize(adSize);
            this.b.setAdListener(new a());
        }
        this.c = str2;
    }

    public final void a() {
        com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "loadAd");
        if (this.b != null) {
            this.b.loadAd(new Builder().build());
            return;
        }
        com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "adview object is null,  to notify failed");
        if (this.a != null) {
            com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "onAdFailedToLoad adError: init failed");
            this.a.onAdLoadFailed();
        }
    }

    public final void b() {
        com.cmcm.adsdk.requestconfig.log.a.a("AdmobBannerLoader", "onDestroy");
        if (this.b != null) {
            this.b.destroy();
        }
    }

    public final void a(IBannerRequestCallBack iBannerRequestCallBack) {
        this.a = iBannerRequestCallBack;
    }

    public final View c() {
        return this.b;
    }
}
