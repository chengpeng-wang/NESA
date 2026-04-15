package com.cmcm.adsdk.bannerad;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.cmcm.adsdk.requestconfig.log.a;
import com.duapps.ad.AdError;
import com.facebook.ads.Ad;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.picksinit.c;

/* compiled from: FacebookBannerLoader */
public final class e extends c implements AdListener {
    private final int a = AdError.TIME_OUT_CODE;
    private final String b = "com.facebook.ad.banner";
    private IBannerRequestCallBack c;
    private AdView d;
    private String e;

    public e(Context context, String str, CMBannerAdSize cMBannerAdSize, String str2) {
        AdSize adSize = AdSize.BANNER_HEIGHT_50;
        switch (cMBannerAdSize) {
            case BANNER_320_50:
                adSize = AdSize.BANNER_320_50;
                break;
            case BANNER_HEIGHT_50:
                adSize = AdSize.BANNER_HEIGHT_50;
                break;
            case BANNER_HEIGHT_90:
                adSize = AdSize.BANNER_HEIGHT_90;
                break;
        }
        if (adSize != null && (adSize instanceof AdSize)) {
            this.d = new AdView(context, str, adSize);
            this.d.setAdListener(this);
        }
        this.e = str2;
    }

    public final void a() {
        a.a("FacebookBannerLoader", "loadAd");
        if (this.d != null) {
            this.d.loadAd();
            return;
        }
        a.a("FacebookBannerLoader", "adview object is null,  to notify failed");
        if (this.c != null) {
            a.a("FacebookBannerLoader", "onError adError: init failed");
            this.c.onAdLoadFailed();
        }
    }

    public final void b() {
        a.a("FacebookBannerLoader", "onDestroy");
        if (this.d != null) {
            this.d.destroy();
        }
    }

    public final void a(IBannerRequestCallBack iBannerRequestCallBack) {
        this.c = iBannerRequestCallBack;
    }

    public final View c() {
        return this.d;
    }

    public final void onError(Ad ad, com.facebook.ads.AdError adError) {
        a.a("FacebookBannerLoader", "onError adError:" + adError.getErrorMessage().toString());
        if (this.c != null) {
            this.c.onAdLoadFailed();
        }
    }

    public final void onAdLoaded(Ad ad) {
        a.a("FacebookBannerLoader", "onAdLoaded Ad:" + ad.toString());
        if (!TextUtils.isEmpty(this.e)) {
            c.getInstance().doRecommendAdViewReport("com.facebook.ad.banner", this.e, AdError.TIME_OUT_CODE);
        }
        if (this.c != null) {
            this.c.onAdLoaded(this);
        }
    }

    public final void onAdClicked(Ad ad) {
        a.a("FacebookBannerLoader", "onAdClicked Ad:" + ad.toString());
        if (!TextUtils.isEmpty(this.e)) {
            c.getInstance().doRecommendAdClickReport("com.facebook.ad.banner", this.e, AdError.TIME_OUT_CODE);
        }
        if (this.c != null) {
            this.c.onAdClicked();
        }
    }
}
