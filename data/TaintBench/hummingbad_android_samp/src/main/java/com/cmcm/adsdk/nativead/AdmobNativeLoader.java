package com.cmcm.adsdk.nativead;

import android.content.Context;
import android.text.TextUtils;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmobNativeLoader extends e implements NativeAdapterListener {
    private static final String TAG = "AdmobNativeLoader";
    private String mAdmobUniId;
    private INativeAd mCacheAd;
    private long mLoadStartTime;

    public AdmobNativeLoader(Context context, String posId, String admobUniId, String adTypeName) {
        super(context, posId, adTypeName);
        this.mAdmobUniId = admobUniId;
    }

    public void loadAd() {
        a.a(TAG, "loadAd admob");
        if (this.mCacheAd != null && !this.mCacheAd.hasExpired()) {
            this.mNativeAdListener.adLoaded(getAdTypeName());
        } else if (TextUtils.isEmpty(this.mAdmobUniId)) {
            this.mNativeAdListener.adFailedToLoad(getAdTypeName(), "ssp adtype configured incorrectly");
        } else {
            new com.cmcm.adsdk.adapter.a().a(this.mContext, this, getLoadExtras(this.mAdmobUniId));
            this.mLoadStartTime = System.currentTimeMillis();
            doLoadReport();
        }
    }

    private Map<String, Object> getLoadExtras(String placementId) {
        HashMap hashMap = new HashMap();
        hashMap.put(CMNativeAd.KEY_PLACEMENT_ID, placementId);
        hashMap.put(CMNativeAd.KEY_JUHE_POSID, this.mPositionId);
        long adTypeCacheTime = CMAdManager.getAdTypeCacheTime(Const.KEY_AB);
        String str = CMNativeAd.KEY_CACHE_TIME;
        if (adTypeCacheTime == 0) {
            adTypeCacheTime = 3600000;
        }
        hashMap.put(str, Long.valueOf(adTypeCacheTime));
        hashMap.put(CMNativeAd.KEY_REPORT_RES, Integer.valueOf(3002));
        hashMap.put(CMNativeAd.KEY_REPORT_PKGNAME, "com.admob.native");
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
