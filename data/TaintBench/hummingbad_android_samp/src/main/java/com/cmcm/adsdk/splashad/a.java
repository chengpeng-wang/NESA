package com.cmcm.adsdk.splashad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.cleanmaster.ui.app.splashad.BitmapListener;
import com.cleanmaster.ui.app.splashad.ImageDownloadListener;
import com.cleanmaster.ui.app.splashad.SplashAd;
import com.cleanmaster.ui.app.splashad.SplashAdActionListener;
import com.cleanmaster.ui.app.splashad.SplashAdConfig;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.splashad.SplashBaseAdapter.OnSplashAdapterResultListener;
import com.cmcm.adsdk.splashad.listener.ImageListener;
import com.cmcm.adsdk.splashad.listener.ImageLoadListener;
import com.duapps.ad.AdError;

/* compiled from: PicksSplashAdapter */
public final class a extends SplashBaseAdapter {
    private Context b;
    private String c;
    /* access modifiers changed from: private */
    public ViewGroup d;
    /* access modifiers changed from: private */
    public OnSplashAdapterResultListener e;
    /* access modifiers changed from: private */
    public ImageLoadListener f;
    /* access modifiers changed from: private */
    public SplashAd g;

    /* compiled from: PicksSplashAdapter */
    class a implements ImageDownloadListener {
        a() {
        }

        public final void getBitmap(String url, final BitmapListener bitmapListener) {
            if (a.this.f != null) {
                a.this.f.getBitmap(url, new ImageListener() {
                    public final void onFailed(String errorMessage) {
                        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm getBitmap ,and errorMessage = " + errorMessage);
                        if (errorMessage != null) {
                            bitmapListener.onFailed(errorMessage);
                        }
                    }

                    public final void onSuccessed(Bitmap bitmap) {
                        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm getBitmap success");
                        if (bitmap != null) {
                            bitmapListener.onSuccessed(bitmap);
                        }
                    }
                });
            }
        }
    }

    /* compiled from: PicksSplashAdapter */
    class b implements SplashAdActionListener {
        b() {
        }

        public final void onClicked() {
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm load splash ad,and onClicked");
            if (a.this.e != null) {
                a.this.e.onClicked(Const.KEY_CM);
            }
        }

        public final void onFailed(String errorMessage) {
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm load splash ad,and onFailed");
            if (a.this.e != null) {
                a.this.e.onAdFailed(Const.KEY_CM, errorMessage);
                a aVar = a.this;
                SplashBaseAdapter.a(Const.KEY_CM, errorMessage);
            }
        }

        public final void onLoaded(View view) {
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm load splash ad,and onLoaded success");
            if (view != null) {
                if (a.this.g != null) {
                    a.this.g.actionShowed();
                }
                a.this.d.addView(view);
                if (a.this.e != null) {
                    a.this.e.onAdPresent(Const.KEY_CM);
                }
                a aVar = a.this;
                SplashBaseAdapter.b(Const.KEY_CM);
            }
        }

        public final void onShowedFinish() {
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm load splash ad,and onShowedFinish");
            if (a.this.e != null) {
                a.this.e.onAdDismissed(Const.KEY_CM);
            }
        }
    }

    public a(String str, int i) {
        SplashBaseAdapter.a = i;
        this.c = str;
    }

    public final String a() {
        return Const.KEY_CM;
    }

    public final void a(ImageLoadListener imageLoadListener) {
        this.f = imageLoadListener;
    }

    public final void b() {
        this.g = null;
    }

    /* access modifiers changed from: protected|final */
    public final void a(@NonNull Activity activity, @NonNull OnSplashAdapterResultListener onSplashAdapterResultListener, @NonNull ViewGroup viewGroup) {
        this.b = activity.getApplicationContext();
        this.d = viewGroup;
        this.e = onSplashAdapterResultListener;
        if (this.b != null && this.d != null && this.c != null) {
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "cm load splash ad,and the placeid = " + this.c);
            SplashAdConfig splashAdConfig = new SplashAdConfig(Integer.valueOf(this.c).intValue(), 1, 5, 1000, CMAdManager.isChinaVersion() ? 60001 : 60004);
            splashAdConfig.setWaitTime(Integer.valueOf(AdError.TIME_OUT_CODE));
            this.g = new SplashAd(splashAdConfig, new b(), new a());
            this.g.loadSplashAd();
            SplashBaseAdapter.a(Const.KEY_CM);
        } else if (this.e != null) {
            this.e.onAdFailed(Const.KEY_CM, "ssp adtype configured incorrectly");
            SplashBaseAdapter.a(Const.KEY_CM, "ssp adtype configured incorrectly");
        }
    }
}
