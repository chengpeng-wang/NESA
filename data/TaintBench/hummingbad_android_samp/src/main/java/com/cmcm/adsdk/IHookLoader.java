package com.cmcm.adsdk;

import com.cmcm.adsdk.nativead.INativeReqeustCallBack;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAdLoader;
import com.cmcm.baseapi.ads.INativeAdLoader.INativeAdLoaderListener;
import java.util.List;

public abstract class IHookLoader implements INativeAdLoader {
    private INativeReqeustCallBack innerCallBack;

    public void setLoadCallBack(INativeReqeustCallBack callBack) {
        this.innerCallBack = callBack;
    }

    public void notifyLoadSuccess(String adTypeName) {
        if (this.innerCallBack != null) {
            this.innerCallBack.adLoaded(adTypeName);
        }
    }

    public void notifyLoadFail(String adTypeName, String error) {
        if (this.innerCallBack != null) {
            this.innerCallBack.adFailedToLoad(adTypeName, error);
        }
    }

    public void setAdListener(INativeAdLoaderListener adListener) {
    }

    public List<INativeAd> getAdList(int num) {
        return null;
    }

    public void removeAdFromPool(INativeAd nativeAd) {
    }
}
