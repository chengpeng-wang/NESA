package com.cmcm.adsdk.nativead;

import android.content.Context;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.List;

public class NativeAdListManager {
    private d mRequest;

    public interface INativeAdListListener {
        void onAdClicked(INativeAd iNativeAd);

        void onLoadFail(int i);

        void onLoadFinish();

        void onLoadProcess();
    }

    public NativeAdListManager(Context context, String posid, INativeAdListListener listener) {
        this.mRequest = new d(context, posid);
        this.mRequest.a(listener);
    }

    public void loadAds(int num) {
        this.mRequest.a(num);
    }

    public List<INativeAd> getAdList() {
        return this.mRequest.a();
    }
}
