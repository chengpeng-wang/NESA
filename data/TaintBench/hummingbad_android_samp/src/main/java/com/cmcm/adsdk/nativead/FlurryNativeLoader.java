package com.cmcm.adsdk.nativead;

import android.content.Context;
import android.text.TextUtils;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.adapter.g;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlurryNativeLoader extends e implements NativeAdapterListener {
    private static final String TAG = "FlurryNativeLoader";
    private String mAdSpace;
    private String mApiKey;
    private INativeAd mCacheAd;
    private long mLoadStartTime;

    public FlurryNativeLoader(Context context, String posId, String flurryParameters, String adTypeName) {
        super(context, posId, adTypeName);
        initParameters(flurryParameters);
    }

    private void initParameters(String params) {
        try {
            if (!TextUtils.isEmpty(params) && params.contains(";")) {
                String[] split = params.split(";");
                if (split.length >= 2) {
                    this.mApiKey = split[0];
                    this.mAdSpace = split[1];
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void loadAd() {
        a.a(TAG, "loadAd flurry");
        if (this.mCacheAd != null && !this.mCacheAd.hasExpired()) {
            this.mNativeAdListener.adLoaded(getAdTypeName());
        } else if (TextUtils.isEmpty(this.mAdSpace) || TextUtils.isEmpty(this.mApiKey)) {
            this.mNativeAdListener.adFailedToLoad(getAdTypeName(), "ssp adtype configured incorrectly");
        } else {
            new g().a(this.mContext, this, getLoadExtras());
            this.mLoadStartTime = System.currentTimeMillis();
            doLoadReport();
        }
    }

    private Map<String, Object> getLoadExtras() {
        HashMap hashMap = new HashMap();
        hashMap.put(CMNativeAd.KEY_APP_ID, this.mApiKey);
        hashMap.put(CMNativeAd.KEY_PLACEMENT_ID, this.mAdSpace);
        hashMap.put(CMNativeAd.KEY_JUHE_POSID, this.mPositionId);
        hashMap.put(CMNativeAd.KEY_REPORT_RES, Integer.valueOf(3008));
        hashMap.put(CMNativeAd.KEY_REPORT_PKGNAME, "com.yahoo.ad");
        long adTypeCacheTime = CMAdManager.getAdTypeCacheTime(Const.KEY_YH);
        String str = CMNativeAd.KEY_CACHE_TIME;
        if (adTypeCacheTime == 0) {
            adTypeCacheTime = 4500000;
        }
        hashMap.put(str, Long.valueOf(adTypeCacheTime));
        return hashMap;
    }

    public INativeAd getAd() {
        if (this.mCacheAd == null || this.mCacheAd.hasExpired()) {
            return null;
        }
        INativeAd iNativeAd = this.mCacheAd;
        this.mCacheAd = null;
        return iNativeAd;
    }

    public List<INativeAd> getAdList(int num) {
        INativeAd ad = getAd();
        if (ad == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(ad);
        return arrayList;
    }

    public void onNativeAdLoaded(INativeAd nativeAd) {
        this.mCacheAd = nativeAd;
        this.mNativeAdListener.adLoaded(getAdTypeName());
        doLoadSuccessReport(System.currentTimeMillis() - this.mLoadStartTime);
    }

    public void onNativeAdFailed(String errorCode) {
        this.mNativeAdListener.adFailedToLoad(getAdTypeName(), errorCode);
        doLoadFailReport(System.currentTimeMillis() - this.mLoadStartTime, errorCode);
    }

    public void onNativeAdClick(INativeAd nativeAd) {
        this.mNativeAdListener.adClicked(nativeAd);
    }

    public void onNativeAdLoaded(List<INativeAd> list) {
        doLoadSuccessReport(System.currentTimeMillis() - this.mLoadStartTime);
    }

    public void loadAds(int num) {
        loadAd();
    }
}
