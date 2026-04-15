package com.cmcm.adsdk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.utils.Assure;
import com.flurry.android.FlurryInit;
import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.flurry.android.ads.FlurryAdNativeListener;
import java.util.Map;

/* compiled from: YahooNativeAdapter */
public final class g extends BaseNativeAdapter {

    /* compiled from: YahooNativeAdapter */
    private class a extends CMNativeAd implements OnClickListener, OnTouchListener, FlurryAdNativeListener {
        private NativeAdapterListener b;
        private Map<String, Object> c;
        private Context d;
        private FlurryAdNative e;
        private View f;

        public a(Context context, @NonNull NativeAdapterListener nativeAdapterListener, @Nullable Map<String, Object> map) {
            this.d = context.getApplicationContext();
            this.b = nativeAdapterListener;
            this.c = map;
        }

        public final void a() {
            String str = (String) this.c.get(CMNativeAd.KEY_APP_ID);
            String str2 = (String) this.c.get(CMNativeAd.KEY_PLACEMENT_ID);
            Assure.checkNotNull(str);
            FlurryInit.init(this.d, str);
            Assure.checkNotNull(str2);
            setJuhePosid((String) this.c.get(CMNativeAd.KEY_JUHE_POSID));
            setReportRes(((Integer) this.c.get(CMNativeAd.KEY_REPORT_RES)).intValue());
            setReportPkgName((String) this.c.get(CMNativeAd.KEY_REPORT_PKGNAME));
            setCacheTime(((Long) this.c.get(CMNativeAd.KEY_CACHE_TIME)).longValue());
            this.e = new FlurryAdNative(this.d, str2);
            this.e.setListener(this);
            this.e.fetchAd();
        }

        public final String getAdTypeName() {
            return Const.KEY_YH;
        }

        public final void registerViewForInteraction(View view) {
            if (view != null && this.e != null) {
                this.e.setLogControl(true);
                this.e.logImpression();
                this.f = view;
                addClickListener(view, this, this);
            }
        }

        public final void unregisterView() {
            if (this.e != null) {
                this.e.setLogControl(false);
            }
            if (this.f != null) {
                clearClickListener(this.f);
                this.f = null;
            }
        }

        public final Object getAdObject() {
            return this.e;
        }

        public final void handleClick() {
        }

        public final void onShowFullscreen(FlurryAdNative flurryAdNative) {
        }

        public final void onCloseFullscreen(FlurryAdNative flurryAdNative) {
        }

        public final void onAppExit(FlurryAdNative flurryAdNative) {
        }

        public final void onClicked(FlurryAdNative flurryAdNative) {
            if (this.b != null) {
                this.b.onNativeAdClick(this);
            }
        }

        public final void onImpressionLogged(FlurryAdNative flurryAdNative) {
            recordImpression();
        }

        public final void onError(FlurryAdNative flurryAdNative, FlurryAdErrorType flurryAdErrorType, int i) {
            if (this.b != null) {
                this.b.onNativeAdFailed(String.valueOf(i));
            }
        }

        public final void onClick(View view) {
            recordClick();
            if (this.e != null) {
                this.e.logClick();
            }
            if (this.mInnerClickListener != null) {
                this.mInnerClickListener.onClickStart(false);
                this.mInnerClickListener.onClickFinish(false);
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

        public final void onFetched(FlurryAdNative flurryAdNative) {
            boolean z;
            FlurryAdNativeAsset asset = flurryAdNative.getAsset("headline");
            if (asset != null) {
                setTitle(asset.getValue());
            }
            if (flurryAdNative.getAsset("secHqImage") != null) {
                setAdCoverImageUrl(flurryAdNative.getAsset("secHqImage").getValue());
            }
            if (flurryAdNative.getAsset("secImage") != null) {
                setAdIconUrl(flurryAdNative.getAsset("secImage").getValue());
            }
            asset = flurryAdNative.getAsset("callToAction");
            if (asset != null) {
                setAdCallToAction(asset.getValue());
            }
            asset = flurryAdNative.getAsset("summary");
            if (asset != null) {
                setAdBody(asset.getValue());
            }
            try {
                asset = flurryAdNative.getAsset("appRating");
                if (!(asset == null || TextUtils.isEmpty(asset.getValue()))) {
                    setAdStarRate(Double.parseDouble(asset.getValue()));
                }
            } catch (Exception e) {
            }
            asset = flurryAdNative.getAsset("appCategory");
            if (asset == null || TextUtils.isEmpty(asset.getValue())) {
                z = false;
            } else {
                z = true;
            }
            setIsDownloadApp(Boolean.valueOf(z));
            if (this.b != null) {
                this.b.onNativeAdLoaded((INativeAd) this);
            }
        }
    }

    public final void a(@NonNull Context context, @NonNull NativeAdapterListener nativeAdapterListener, @NonNull Map<String, Object> map) {
        new a(context, nativeAdapterListener, map).a();
    }
}
