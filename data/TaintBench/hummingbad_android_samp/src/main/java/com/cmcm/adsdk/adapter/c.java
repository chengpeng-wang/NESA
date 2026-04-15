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
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.ImpressionListener;
import com.facebook.ads.NativeAd;
import java.util.Map;

/* compiled from: FacebookNativeAdapter */
public final class c extends BaseNativeAdapter {

    /* compiled from: FacebookNativeAdapter */
    private class a extends CMNativeAd implements AdListener, ImpressionListener {
        private NativeAd b;
        private NativeAdapterListener c;
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
            setPlacementId(str);
            setJuhePosid((String) this.d.get(CMNativeAd.KEY_JUHE_POSID));
            Assure.checkNotNull(str);
            setReportRes(((Integer) this.d.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            setReportPkgName((String) this.d.get(CMNativeAd.KEY_REPORT_PKGNAME));
            this.b = new NativeAd(this.e, str);
            this.b.setAdListener(this);
            this.b.setImpressionListener(this);
            this.b.loadAd();
        }

        public final String getAdTypeName() {
            return Const.KEY_FB;
        }

        public final void registerViewForInteraction(View view) {
            this.b.registerViewForInteraction(view);
        }

        public final void unregisterView() {
            this.b.unregisterView();
        }

        public final Object getAdObject() {
            return this.b;
        }

        public final void handleClick() {
        }

        public final void onError(Ad ad, AdError adError) {
            if (this.c != null) {
                this.c.onNativeAdFailed(adError.getErrorMessage());
            }
        }

        public final void onAdLoaded(Ad ad) {
            if (this.b.equals(ad) && this.b.isAdLoaded()) {
                setTitle(this.b.getAdTitle());
                setAdBody(this.b.getAdBody());
                setAdCoverImageUrl(this.b.getAdCoverImage().getUrl());
                setAdIconUrl(this.b.getAdIcon().getUrl());
                setAdCallToAction(this.b.getAdCallToAction());
                setAdSocialContext(this.b.getAdSocialContext());
                setAdStarRate(this.b.getAdStarRating() != null ? this.b.getAdStarRating().getValue() : 0.0d);
                if (this.c != null) {
                    this.c.onNativeAdLoaded((INativeAd) this);
                    return;
                }
                return;
            }
            this.c.onNativeAdFailed("response is null");
        }

        public final void onAdClicked(Ad ad) {
            if (this.mInnerClickListener != null) {
                this.mInnerClickListener.onClickStart(false);
                this.mInnerClickListener.onClickFinish(false);
            }
            if (this.c != null) {
                this.c.onNativeAdClick(this);
            }
            recordClick();
        }

        public final void onLoggingImpression(Ad ad) {
            recordImpression();
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        new a(context, nativeAdapterListener, map).a();
    }
}
