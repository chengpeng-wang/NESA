package com.duapps.ad.entity;

import android.content.Context;
import android.view.View;
import com.cmcm.adsdk.Const;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.DuClickCallback;
import com.duapps.ad.base.d;
import com.duapps.ad.entity.strategy.NativeAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd.Rating;
import java.util.List;

/* compiled from: NativeAdFBWrapper */
public final class l implements NativeAd, AdListener {
    private static final e g = new m();
    private com.facebook.ads.NativeAd a;
    private e b = g;
    private String c;
    private volatile boolean d = false;
    private Context e;
    private int f;
    private long h = 0;

    public final boolean isValid() {
        long currentTimeMillis = System.currentTimeMillis() - this.h;
        return currentTimeMillis < 3300000 && currentTimeMillis > 0;
    }

    public l(Context context, String str, int i) {
        this.e = context;
        this.c = str;
        this.f = i;
        this.a = new com.facebook.ads.NativeAd(context, str);
        this.a.setAdListener(this);
    }

    public final void a() {
        if (this.a.isAdLoaded()) {
            this.b.a();
        } else if (!this.d) {
            this.d = true;
            this.a.loadAd();
        }
    }

    public final void a(e eVar) {
        if (eVar == null) {
            this.b = g;
        } else {
            this.b = eVar;
        }
    }

    public final void onError(Ad ad, AdError adError) {
        this.b.a(adError.getErrorCode(), adError.getErrorMessage());
    }

    public final void onAdLoaded(Ad ad) {
        this.h = System.currentTimeMillis();
        this.b.a();
    }

    public final void onAdClicked(Ad ad) {
        this.b.b();
        d.f(this.e, this.f);
    }

    public final void destroy() {
        this.b = g;
        this.a.destroy();
    }

    public final String b() {
        return this.c;
    }

    public final float getAdStarRating() {
        Rating adStarRating = this.a.getAdStarRating();
        if (adStarRating != null) {
            return (float) adStarRating.getValue();
        }
        return 4.5f;
    }

    public final String getId() {
        return this.a.getId();
    }

    public final String getAdCoverImageUrl() {
        return this.a.getAdCoverImage().getUrl();
    }

    public final String getAdIconUrl() {
        return this.a.getAdIcon().getUrl();
    }

    public final String getAdSocialContext() {
        return this.a.getAdSocialContext();
    }

    public final String getAdCallToAction() {
        return this.a.getAdCallToAction();
    }

    public final String getAdBody() {
        return this.a.getAdBody();
    }

    public final String getAdTitle() {
        return this.a.getAdTitle();
    }

    public final void unregisterView() {
        this.a.unregisterView();
    }

    public final void registerViewForInteraction(View view) {
        this.a.registerViewForInteraction(view);
        d.e(this.e, this.f);
    }

    public final void registerViewForInteraction(View view, List<View> list) {
        this.a.registerViewForInteraction(view, list);
        d.e(this.e, this.f);
    }

    public final void setMobulaAdListener(DuAdDataCallBack duAdDataCallBack) {
    }

    public final int getAdChannelType() {
        return 2;
    }

    public final void setProcessClickUrlCallback(DuClickCallback duClickCallback) {
    }

    public final Object getRealData() {
        return this.a;
    }

    public final String getSourceType() {
        return "facebook";
    }

    public final String getAdSource() {
        return Const.KEY_FB;
    }
}
