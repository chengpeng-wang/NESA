package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.ImpressionListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.facebook.ads.NativeAdsManager.Listener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* compiled from: FacebookAdsAdapter */
public final class b extends BaseNativeAdapter implements Listener {
    /* access modifiers changed from: private */
    public NativeAdapterListener a;
    private NativeAdsManager b;
    /* access modifiers changed from: private */
    public Map<String, Object> c;

    /* compiled from: FacebookAdsAdapter */
    private class a extends CMNativeAd implements AdListener, ImpressionListener {
        private NativeAd b;

        public a(NativeAd nativeAd) {
            this.b = nativeAd;
            String str = (String) b.this.c.get(CMNativeAd.KEY_PLACEMENT_ID);
            setCacheTime(((Long) b.this.c.get(CMNativeAd.KEY_CACHE_TIME)).longValue());
            setPlacementId(str);
            setJuhePosid((String) b.this.c.get(CMNativeAd.KEY_JUHE_POSID));
            setReportRes(((Integer) b.this.c.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            setReportPkgName((String) b.this.c.get(CMNativeAd.KEY_REPORT_PKGNAME));
            setTitle(this.b.getAdTitle());
            setAdBody(this.b.getAdBody());
            setAdCoverImageUrl(this.b.getAdCoverImage().getUrl());
            setAdIconUrl(this.b.getAdIcon().getUrl());
            setAdCallToAction(this.b.getAdCallToAction());
            setAdSocialContext(this.b.getAdSocialContext());
            setAdStarRate(this.b.getAdStarRating() != null ? this.b.getAdStarRating().getValue() : 0.0d);
            this.b.setAdListener(this);
            this.b.setImpressionListener(this);
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
        }

        public final void onAdLoaded(Ad ad) {
        }

        public final void onAdClicked(Ad ad) {
            recordClick();
            if (this.mInnerClickListener != null) {
                this.mInnerClickListener.onClickStart(false);
                this.mInnerClickListener.onClickFinish(false);
            }
            b.this.a.onNativeAdClick(this);
        }

        public final void onLoggingImpression(Ad ad) {
            recordImpression();
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        this.a = nativeAdapterListener;
        this.c = map;
        this.b = new NativeAdsManager(context, (String) map.get(CMNativeAd.KEY_PLACEMENT_ID), ((Integer) map.get(CMNativeAd.KEY_LOAD_SIZE)).intValue());
        this.b.setListener(this);
        this.b.loadAds();
    }

    public final void onAdsLoaded() {
        int uniqueNativeAdCount = this.b.getUniqueNativeAdCount();
        List arrayList = new ArrayList();
        for (int i = 0; i < uniqueNativeAdCount; i++) {
            NativeAd nextNativeAd = this.b.nextNativeAd();
            if (nextNativeAd == null) {
                break;
            }
            arrayList.add(new a(nextNativeAd));
        }
        if (this.a != null) {
            this.a.onNativeAdLoaded(arrayList);
        }
    }

    public final void onAdError(AdError adError) {
        this.a.onNativeAdFailed(adError.toString());
    }
}
