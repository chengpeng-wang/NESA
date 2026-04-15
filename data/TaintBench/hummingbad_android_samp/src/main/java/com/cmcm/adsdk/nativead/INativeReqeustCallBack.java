package com.cmcm.adsdk.nativead;

import com.cmcm.baseapi.ads.INativeAd;

public interface INativeReqeustCallBack {
    void adClicked(INativeAd iNativeAd);

    void adFailedToLoad(String str, String str2);

    void adLoaded(String str);
}
