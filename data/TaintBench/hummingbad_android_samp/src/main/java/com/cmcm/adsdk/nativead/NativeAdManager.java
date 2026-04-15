package com.cmcm.adsdk.nativead;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.cmcm.adsdk.CMRequestParams;
import com.cmcm.adsdk.IHookLoader;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAdLoader.INativeAdLoaderListener;
import java.util.HashMap;
import java.util.List;

public class NativeAdManager {
    private Context mContext;
    j requestAd = null;
    public CMRequestParams requestParams;

    public NativeAdManager(Context context, String posid) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        if (context instanceof Activity) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        if (TextUtils.isEmpty(posid)) {
            throw new IllegalArgumentException("posid can't be null");
        }
        this.requestAd = new j(this.mContext, posid);
    }

    public void setRequestParams(CMRequestParams params) {
        this.requestParams = params;
    }

    public void setNativeAdListener(INativeAdLoaderListener listener) {
        if (this.requestAd != null) {
            this.requestAd.setAdListener(listener);
        }
    }

    public void preloadAd() {
        requestAd(true);
    }

    public void loadAd() {
        requestAd(false);
    }

    /* access modifiers changed from: protected */
    public void requestAd(boolean isPreload) {
        if (this.requestParams != null) {
            this.requestAd.setRequestParams(this.requestParams);
        }
        this.requestAd.a(isPreload);
        this.requestAd.loadAd();
    }

    public INativeAd getAd() {
        if (this.requestAd != null) {
            return this.requestAd.getAd();
        }
        return null;
    }

    public List<INativeAd> getAdList(int num) {
        if (this.requestAd != null) {
            return this.requestAd.getAdList(num);
        }
        return null;
    }

    public String getRequestErrorInfo() {
        if (this.requestAd != null) {
            return this.requestAd.a();
        }
        return null;
    }

    public void addHook(String key, IHookLoader loader) {
        if (this.requestAd != null) {
            this.requestAd.a(key, loader);
        }
    }

    public void addHooks(HashMap<String, IHookLoader> loads) {
        if (this.requestAd != null) {
            this.requestAd.a((HashMap) loads);
        }
    }

    public void setOpenPriority(boolean openPriority) {
        this.requestAd.b(openPriority);
    }
}
