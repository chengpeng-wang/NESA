package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.utils.Assure;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener;
import com.google.android.gms.ads.formats.NativeContentAdView;
import java.util.Map;

/* compiled from: AdmobNativeAdapter */
public final class a extends BaseNativeAdapter {

    /* compiled from: AdmobNativeAdapter */
    private class a extends CMNativeAd implements OnAppInstallAdLoadedListener, OnContentAdLoadedListener {
        private NativeAd b;
        /* access modifiers changed from: private */
        public NativeAdapterListener c;
        private Map<String, Object> d;
        private Context e;

        public a(Context context, @NonNull NativeAdapterListener nativeAdapterListener, @Nullable Map<String, Object> map) {
            this.e = context.getApplicationContext();
            this.c = nativeAdapterListener;
            this.d = map;
        }

        public final void a() {
            String str = (String) this.d.get(CMNativeAd.KEY_PLACEMENT_ID);
            setCacheTime(((Long) this.d.get(CMNativeAd.KEY_CACHE_TIME)).longValue());
            setJuhePosid((String) this.d.get(CMNativeAd.KEY_JUHE_POSID));
            setReportPkgName((String) this.d.get(CMNativeAd.KEY_REPORT_PKGNAME));
            setReportRes(((Integer) this.d.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            Assure.checkNotNull(str);
            new Builder(this.e, str).forAppInstallAd(this).forContentAd(this).withAdListener(new AdListener() {
                public final void onAdFailedToLoad(int errorCode) {
                    if (a.this.c != null) {
                        a.this.c.onNativeAdFailed(String.valueOf(errorCode));
                    }
                }

                public final void onAdOpened() {
                    a.this.recordClick();
                    if (a.this.c != null) {
                        a.this.c.onNativeAdClick(a.this);
                    }
                    if (a.this.mInnerClickListener != null) {
                        a.this.mInnerClickListener.onClickStart(false);
                        a.this.mInnerClickListener.onClickFinish(false);
                    }
                }
            }).withNativeAdOptions(new NativeAdOptions.Builder().setReturnUrlsForImageAssets(true).build()).build().loadAd(new AdRequest.Builder().build());
        }

        public final String getAdTypeName() {
            return Const.KEY_AB;
        }

        public final void registerViewForInteraction(View view) {
            if ((view instanceof NativeContentAdView) && (this.b instanceof NativeContentAd)) {
                ((NativeContentAdView) view).setNativeAd(this.b);
            } else if ((view instanceof NativeAppInstallAdView) && (this.b instanceof NativeAppInstallAd)) {
                ((NativeAppInstallAdView) view).setNativeAd(this.b);
            }
            recordImpression();
        }

        public final void unregisterView() {
        }

        public final Object getAdObject() {
            return this.b;
        }

        public final void handleClick() {
        }

        public final void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
            a((NativeAd) nativeAppInstallAd);
            this.b = nativeAppInstallAd;
            this.c.onNativeAdLoaded((INativeAd) this);
        }

        public final void onContentAdLoaded(NativeContentAd nativeContentAd) {
            a((NativeAd) nativeContentAd);
            this.b = nativeContentAd;
            this.c.onNativeAdLoaded((INativeAd) this);
        }

        private void a(@NonNull NativeAd nativeAd) {
            if (nativeAd instanceof NativeContentAd) {
                NativeContentAd nativeContentAd = (NativeContentAd) nativeAd;
                setTitle(nativeContentAd.getHeadline().toString());
                setAdBody(nativeContentAd.getBody().toString());
                if (!(nativeContentAd.getImages() == null || nativeContentAd.getImages().get(0) == null || ((Image) nativeContentAd.getImages().get(0)).getUri() == null)) {
                    setAdCoverImageUrl(((Image) nativeContentAd.getImages().get(0)).getUri().toString());
                }
                if (!(nativeContentAd.getLogo() == null || nativeContentAd.getLogo().getUri() == null)) {
                    setAdIconUrl(nativeContentAd.getLogo().getUri().toString());
                }
                setAdCallToAction(nativeContentAd.getCallToAction().toString());
                setIsDownloadApp(Boolean.valueOf(false));
                setAdStarRate(0.0d);
            } else if (nativeAd instanceof NativeAppInstallAd) {
                NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) nativeAd;
                setTitle(nativeAppInstallAd.getHeadline().toString());
                setAdBody(nativeAppInstallAd.getBody().toString());
                if (!(nativeAppInstallAd.getImages() == null || nativeAppInstallAd.getImages().get(0) == null || ((Image) nativeAppInstallAd.getImages().get(0)).getUri() == null)) {
                    setAdCoverImageUrl(((Image) nativeAppInstallAd.getImages().get(0)).getUri().toString());
                }
                if (!(nativeAppInstallAd.getIcon() == null || nativeAppInstallAd.getIcon().getUri() == null)) {
                    setAdIconUrl(nativeAppInstallAd.getIcon().getUri().toString());
                }
                setAdCallToAction(nativeAppInstallAd.getCallToAction().toString());
                setIsDownloadApp(Boolean.valueOf(true));
                try {
                    setAdStarRate(nativeAppInstallAd.getStarRating().doubleValue());
                } catch (Exception e) {
                    setAdStarRate(0.0d);
                }
            }
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        new a(context, nativeAdapterListener, map).a();
    }
}
