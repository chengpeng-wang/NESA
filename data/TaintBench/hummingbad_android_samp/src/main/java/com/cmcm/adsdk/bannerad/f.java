package com.cmcm.adsdk.bannerad;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.cmcm.adsdk.requestconfig.log.a;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;
import com.picksinit.c;

/* compiled from: MopubBannerLoader */
public final class f extends c implements BannerAdListener {
    private final int a = 3003;
    private IBannerRequestCallBack b;
    private MoPubView c;
    private String d;

    public f(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str)) {
            a.a("MopubBannerLoader", "incomplete parameter");
            return;
        }
        this.c = new MoPubView(context);
        this.c.setAutorefreshEnabled(true);
        this.c.setBannerAdListener(this);
        this.c.setAdUnitId(str);
        this.d = str2;
    }

    public final void a() {
        a.a("MopubBannerLoader", "loadAd");
        if (this.c != null) {
            this.c.loadAd();
            return;
        }
        a.a("MopubBannerLoader", "adview object is null,  to notify failed");
        if (this.b != null) {
            a.a("MopubBannerLoader", "onAdFailedToLoad adError:  init failed");
            this.b.onAdLoadFailed();
        }
    }

    public final void b() {
        a.a("MopubBannerLoader", "onDestroy");
        if (this.c != null) {
            this.c.destroy();
        }
    }

    public final void a(IBannerRequestCallBack iBannerRequestCallBack) {
        this.b = iBannerRequestCallBack;
    }

    public final View c() {
        return this.c;
    }

    public final void onBannerLoaded(MoPubView moPubView) {
        a.a("MopubBannerLoader", "onAdLoaded Ad");
        if (!TextUtils.isEmpty(this.d)) {
            c.getInstance().doRecommendAdViewReport("com.mopub.ad.banner", this.d, 3003);
        }
        if (this.b != null) {
            this.b.onAdLoaded(this);
        }
    }

    public final void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
        a.a("MopubBannerLoader", "onBannerFailed Ad errorcoede:" + moPubErrorCode.toString());
        if (this.b != null) {
            this.b.onAdLoadFailed();
        }
    }

    public final void onBannerClicked(MoPubView moPubView) {
        a.a("MopubBannerLoader", "onBannerClicked Ad");
        if (!TextUtils.isEmpty(this.d)) {
            c.getInstance().doRecommendAdClickReport("com.mopub.ad.banner", this.d, 3003);
        }
        if (this.b != null) {
            this.b.onAdClicked();
        }
    }

    public final void onBannerExpanded(MoPubView moPubView) {
    }

    public final void onBannerCollapsed(MoPubView moPubView) {
    }
}
