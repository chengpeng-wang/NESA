package com.cmcm.adsdk.bannerad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.lang.ref.WeakReference;

public class CMAdView extends FrameLayout {
    /* access modifiers changed from: private|static|final */
    public static final String TAG = CMAdView.class.getSimpleName();
    private CMBannerAdSize adSize;
    private d bannerLoadManager;
    /* access modifiers changed from: private */
    public CMBannerAdListener listener;
    private WeakReference<Context> mContextRef;
    private String posid;

    private class a implements IBannerRequestCallBack {
        private a() {
        }

        /* synthetic */ a(CMAdView cMAdView, byte b) {
            this();
        }

        public final void onAdLoaded(c loader) {
            com.cmcm.adsdk.requestconfig.log.a.a(CMAdView.TAG, "onAdLoaded");
            CMAdView.this.removeAllViews();
            CMAdView.this.addView(loader.c());
            if (CMAdView.this.listener != null) {
                CMAdView.this.listener.onAdLoaded(CMAdView.this);
            }
        }

        public final void onAdLoadFailed() {
            com.cmcm.adsdk.requestconfig.log.a.a(CMAdView.TAG, "onAdLoadFailed");
            if (CMAdView.this.listener != null) {
                CMAdView.this.listener.adFailedToLoad();
            }
        }

        public final void onAdClicked() {
            com.cmcm.adsdk.requestconfig.log.a.a(CMAdView.TAG, "onAdClicked");
            if (CMAdView.this.listener != null) {
                CMAdView.this.listener.onAdClicked(CMAdView.this);
            }
        }
    }

    public CMAdView(Context context) {
        this(context, null);
    }

    public CMAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CMAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContextRef = new WeakReference(context);
    }

    public void setAdListener(CMBannerAdListener listener) {
        this.listener = listener;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public void setAdSize(CMBannerAdSize adSize) {
        this.adSize = adSize;
    }

    public void loadAd() {
        com.cmcm.adsdk.requestconfig.log.a.a(TAG, "loadAd");
        if (this.bannerLoadManager == null && this.mContextRef.get() != null) {
            this.bannerLoadManager = new d(this.posid, (Context) this.mContextRef.get(), this.adSize);
        }
        this.bannerLoadManager.a(new a(this, (byte) 0));
        this.bannerLoadManager.a();
    }

    public void onDestroy() {
        com.cmcm.adsdk.requestconfig.log.a.a(TAG, "onDestroy");
        if (this.bannerLoadManager != null) {
            this.bannerLoadManager.b();
        }
    }
}
