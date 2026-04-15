package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.utils.Assure;
import com.mopub.nativeads.BaseNativeAd.NativeEventListener;
import com.mopub.nativeads.ImpressionTracker;
import com.mopub.nativeads.MoPubCustomEventNative.MoPubStaticNativeAd;
import com.mopub.nativeads.MoPubNativeContext;
import com.mopub.nativeads.MoPubNativeContext.MoPubNativeNetworkListener;
import com.mopub.nativeads.NativeClickHandler;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.network.AdResponse;
import java.util.Map;

/* compiled from: MopubNativeAdapter */
public final class d extends BaseNativeAdapter {

    /* compiled from: MopubNativeAdapter */
    private class a extends CMNativeAd implements OnClickListener, OnTouchListener, NativeEventListener, MoPubNativeNetworkListener {
        private MoPubStaticNativeAd b;
        private NativeAdapterListener c;
        private Map<String, Object> d;
        private Context e;
        private View f;

        public a(Context context, @NonNull NativeAdapterListener nativeAdapterListener, @Nullable Map<String, Object> map) {
            this.e = context.getApplicationContext();
            this.c = nativeAdapterListener;
            this.d = map;
        }

        public final void a() {
            String str = (String) this.d.get(CMNativeAd.KEY_PLACEMENT_ID);
            setCacheTime(((Long) this.d.get(CMNativeAd.KEY_CACHE_TIME)).longValue());
            Assure.checkNotNull(str);
            setJuhePosid((String) this.d.get(CMNativeAd.KEY_JUHE_POSID));
            setReportRes(((Integer) this.d.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            setReportPkgName((String) this.d.get(CMNativeAd.KEY_REPORT_PKGNAME));
            new MoPubNativeContext(this.e, str, this).makeRequest();
        }

        public final String getAdTypeName() {
            return Const.KEY_MP;
        }

        public final void registerViewForInteraction(View view) {
            recordImpression();
            ImpressionTracker impressionTracker = new ImpressionTracker(view.getRootView());
            impressionTracker.addView(view, this.b);
            this.b.setImpressionTracker(impressionTracker);
            this.f = view;
            addClickListener(this.f, this, this);
        }

        public final void unregisterView() {
            if (this.f != null) {
                this.b.clear(this.f);
                this.b.destroy();
                clearClickListener(this.f);
                this.f = null;
            }
        }

        public final Object getAdObject() {
            return this.b;
        }

        public final void handleClick() {
        }

        private MoPubStaticNativeAd a(AdResponse adResponse) {
            if (adResponse != null) {
                try {
                    if (adResponse.getJsonBody() != null) {
                        return new MoPubStaticNativeAd(this.e, adResponse.getJsonBody(), new NativeClickHandler(this.e));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        public final void onAdImpressed() {
            recordImpression();
        }

        public final void onAdClicked() {
        }

        public final void onNativeLoad(AdResponse adResponse) {
            this.b = a(adResponse);
            if (this.b == null) {
                onNativeFail(NativeErrorCode.UNSPECIFIED);
                return;
            }
            setTitle(this.b.getTitle());
            setAdBody(this.b.getText());
            setAdCallToAction(this.b.getCallToAction());
            setAdCoverImageUrl(this.b.getMainImageUrl());
            setAdIconUrl(this.b.getIconImageUrl());
            String str = this.b.getStarRating();
            if (str.equals("null")) {
                str = "0";
            }
            setAdStarRate(Double.parseDouble(str));
            this.c.onNativeAdLoaded((INativeAd) this);
        }

        public final void onNativeFail(NativeErrorCode nativeErrorCode) {
            if (this.c != null) {
                this.c.onNativeAdFailed(nativeErrorCode.toString());
            }
        }

        public final void onClick(View view) {
            this.b.handleClick(view);
            recordClick();
            if (this.mInnerClickListener != null) {
                this.mInnerClickListener.onClickStart(false);
                this.mInnerClickListener.onClickFinish(false);
            }
            if (this.c != null) {
                this.c.onNativeAdClick(this);
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        new a(context, nativeAdapterListener, map).a();
    }
}
