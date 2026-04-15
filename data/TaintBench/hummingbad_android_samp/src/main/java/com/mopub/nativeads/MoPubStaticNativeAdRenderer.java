package com.mopub.nativeads;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import java.util.WeakHashMap;

public class MoPubStaticNativeAdRenderer implements MoPubAdRenderer<StaticNativeAd> {
    @NonNull
    private final ViewBinder mViewBinder;
    @NonNull
    @VisibleForTesting
    final WeakHashMap<View, StaticNativeViewHolder> mViewHolderMap = new WeakHashMap();

    public MoPubStaticNativeAdRenderer(@NonNull ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
    }

    @NonNull
    public View createAdView(@NonNull Activity activity, @Nullable ViewGroup parent) {
        return LayoutInflater.from(activity).inflate(this.mViewBinder.layoutId, parent, false);
    }

    public void renderAdView(@NonNull View view, @NonNull StaticNativeAd staticNativeAd) {
        StaticNativeViewHolder staticNativeViewHolder = (StaticNativeViewHolder) this.mViewHolderMap.get(view);
        if (staticNativeViewHolder == null) {
            staticNativeViewHolder = StaticNativeViewHolder.fromViewBinder(view, this.mViewBinder);
            this.mViewHolderMap.put(view, staticNativeViewHolder);
        }
        update(staticNativeViewHolder, staticNativeAd);
        NativeRendererHelper.updateExtras(staticNativeViewHolder.mainView, this.mViewBinder.extras, staticNativeAd.getExtras());
        setViewVisibility(staticNativeViewHolder, 0);
    }

    public boolean supports(@NonNull BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof StaticNativeAd;
    }

    private void update(@NonNull StaticNativeViewHolder staticNativeViewHolder, @NonNull StaticNativeAd staticNativeAd) {
        NativeRendererHelper.addTextView(staticNativeViewHolder.titleView, staticNativeAd.getTitle());
        NativeRendererHelper.addTextView(staticNativeViewHolder.textView, staticNativeAd.getText());
        NativeRendererHelper.addTextView(staticNativeViewHolder.callToActionView, staticNativeAd.getCallToAction());
        NativeImageHelper.loadImageView(staticNativeAd.getMainImageUrl(), staticNativeViewHolder.mainImageView);
        NativeImageHelper.loadImageView(staticNativeAd.getIconImageUrl(), staticNativeViewHolder.iconImageView);
        NativeRendererHelper.addPrivacyInformationIcon(staticNativeViewHolder.privacyInformationIconImageView, staticNativeAd.getPrivacyInformationIconClickThroughUrl());
    }

    private void setViewVisibility(@NonNull StaticNativeViewHolder staticNativeViewHolder, int visibility) {
        if (staticNativeViewHolder.mainView != null) {
            staticNativeViewHolder.mainView.setVisibility(visibility);
        }
    }
}
