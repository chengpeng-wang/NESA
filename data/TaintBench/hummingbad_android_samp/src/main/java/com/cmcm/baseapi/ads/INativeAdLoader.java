package com.cmcm.baseapi.ads;

import java.util.List;

public interface INativeAdLoader {

    public interface INativeAdLoaderListener {
        void adClicked(INativeAd iNativeAd);

        void adFailedToLoad(int i);

        void adLoaded();
    }

    INativeAd getAd();

    List<INativeAd> getAdList(int i);

    void loadAd();

    void setAdListener(INativeAdLoaderListener iNativeAdLoaderListener);
}
