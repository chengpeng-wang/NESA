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

public class MoPubVideoNativeAdRenderer implements MoPubAdRenderer<VideoNativeAd> {
    @NonNull
    private final MediaViewBinder mMediaViewBinder;
    @NonNull
    @VisibleForTesting
    final WeakHashMap<View, MediaViewHolder> mMediaViewHolderMap = new WeakHashMap();

    public MoPubVideoNativeAdRenderer(@NonNull MediaViewBinder mediaViewBinder) {
        this.mMediaViewBinder = mediaViewBinder;
    }

    @NonNull
    public View createAdView(@NonNull Activity activity, @Nullable ViewGroup parent) {
        return LayoutInflater.from(activity).inflate(this.mMediaViewBinder.layoutId, parent, false);
    }

    public void renderAdView(@NonNull View view, @NonNull VideoNativeAd videoNativeAd) {
        MediaViewHolder mediaViewHolder = (MediaViewHolder) this.mMediaViewHolderMap.get(view);
        if (mediaViewHolder == null) {
            mediaViewHolder = MediaViewHolder.fromViewBinder(view, this.mMediaViewBinder);
            this.mMediaViewHolderMap.put(view, mediaViewHolder);
        }
        update(mediaViewHolder, videoNativeAd);
        NativeRendererHelper.updateExtras(mediaViewHolder.mainView, this.mMediaViewBinder.extras, videoNativeAd.getExtras());
        setViewVisibility(mediaViewHolder, 0);
        videoNativeAd.render((MediaLayout) view.findViewById(this.mMediaViewBinder.mediaLayoutId));
    }

    public boolean supports(@NonNull BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof VideoNativeAd;
    }

    private void update(@NonNull MediaViewHolder mediaViewHolder, @NonNull VideoNativeAd videoNativeAd) {
        NativeRendererHelper.addTextView(mediaViewHolder.titleView, videoNativeAd.getTitle());
        NativeRendererHelper.addTextView(mediaViewHolder.textView, videoNativeAd.getText());
        NativeRendererHelper.addCtaButton(mediaViewHolder.callToActionView, mediaViewHolder.mainView, videoNativeAd.getCallToAction());
        NativeImageHelper.loadImageView(videoNativeAd.getMainImageUrl(), mediaViewHolder.mediaLayout.getMainImageView());
        NativeImageHelper.loadImageView(videoNativeAd.getIconImageUrl(), mediaViewHolder.iconImageView);
        NativeRendererHelper.addPrivacyInformationIcon(mediaViewHolder.privacyInformationIconImageView, videoNativeAd.getPrivacyInformationIconClickThroughUrl());
    }

    private void setViewVisibility(@NonNull MediaViewHolder mediaViewHolder, int visibility) {
        if (mediaViewHolder.mainView != null) {
            mediaViewHolder.mainView.setVisibility(visibility);
        }
    }
}
