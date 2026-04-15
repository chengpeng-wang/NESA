package com.cmcm.adsdk.nativead;

import android.content.Context;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.adapter.f;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubMaticNativeLoader extends e implements NativeAdapterListener {
    private static final String TAG = "PubMaticNativeLoader";
    private INativeAd mCacheAd;
    private long mLoadStartTime;
    private int mZoneId;

    public PubMaticNativeLoader(Context context, String posId, String zoneId, String adTypeName) {
        super(context, posId, adTypeName);
        this.mZoneId = Integer.valueOf(zoneId).intValue();
    }

    public void loadAd() {
        a.a(TAG, "loadAd pubmatic");
        if (this.mCacheAd != null && !this.mCacheAd.hasExpired()) {
            this.mNativeAdListener.adLoaded(getAdTypeName());
        } else if (this.mZoneId == 0) {
            this.mNativeAdListener.adFailedToLoad(getAdTypeName(), "ssp adtype configured incorrectly");
        } else {
            new f().a(this.mContext, this, getLoadExtras());
            this.mLoadStartTime = System.currentTimeMillis();
            doLoadReport();
        }
    }

    private Map<String, Object> getLoadExtras() {
        HashMap hashMap = new HashMap();
        hashMap.put(CMNativeAd.KEY_PLACEMENT_ID, Integer.valueOf(this.mZoneId));
        hashMap.put(CMNativeAd.KEY_JUHE_POSID, this.mPositionId);
        hashMap.put(CMNativeAd.KEY_REPORT_RES, Integer.valueOf(3007));
        hashMap.put(CMNativeAd.KEY_REPORT_PKGNAME, String.format("%s.%s", new Object[]{"com.pubmatic.ad", getAdTypeName()}));
        long adTypeCacheTime = CMAdManager.getAdTypeCacheTime(Const.KEY_PM);
        String str = CMNativeAd.KEY_CACHE_TIME;
        if (adTypeCacheTime == 0) {
            adTypeCacheTime = 3600000;
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

    public void onNativeAdFailed(String errorInfo) {
        this.mNativeAdListener.adFailedToLoad(getAdTypeName(), errorInfo);
        doLoadFailReport(System.currentTimeMillis() - this.mLoadStartTime, errorInfo);
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
