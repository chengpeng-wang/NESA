package com.cmcm.adsdk.adapter;

import com.cmcm.baseapi.ads.INativeAd;
import java.util.List;

public abstract class BaseNativeAdapter {

    public interface NativeAdapterListener {
        void onNativeAdClick(INativeAd iNativeAd);

        void onNativeAdFailed(String str);

        void onNativeAdLoaded(INativeAd iNativeAd);

        void onNativeAdLoaded(List<INativeAd> list);
    }
}
