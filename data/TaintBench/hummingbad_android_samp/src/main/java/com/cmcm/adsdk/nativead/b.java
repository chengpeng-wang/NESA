package com.cmcm.adsdk.nativead;

import android.content.Context;
import android.text.TextUtils;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.adsdk.utils.FaceBookInfomation;
import com.cmcm.baseapi.ads.INativeAd;
import com.duapps.ad.AdError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* compiled from: FBNativeListLoader */
public final class b extends e implements NativeAdapterListener {
    public String[] a;
    private List<INativeAd> b;
    private long c;

    public b(Context context, String str, String str2, String str3) {
        super(context, str2, str);
        this.a = null;
        this.b = new ArrayList();
        this.b = new ArrayList();
        if (!TextUtils.isEmpty(str3)) {
            this.a = str3.split(",");
        }
    }

    public final void loadAd() {
    }

    public final void loadAds(int num) {
        if (this.a == null) {
            this.mNativeAdListener.adFailedToLoad(getAdTypeName(), "ssp adtype configured incorrectly");
            return;
        }
        for (Object obj : this.a) {
            com.cmcm.adsdk.adapter.b bVar = new com.cmcm.adsdk.adapter.b();
            Context context = this.mContext;
            HashMap hashMap = new HashMap();
            hashMap.put(CMNativeAd.KEY_PLACEMENT_ID, obj);
            hashMap.put(CMNativeAd.KEY_JUHE_POSID, this.mPositionId);
            long adTypeCacheTime = CMAdManager.getAdTypeCacheTime(Const.KEY_FB);
            String str = CMNativeAd.KEY_CACHE_TIME;
            if (adTypeCacheTime == 0) {
                adTypeCacheTime = 3600000;
            }
            hashMap.put(str, Long.valueOf(adTypeCacheTime));
            hashMap.put(CMNativeAd.KEY_REPORT_RES, Integer.valueOf(AdError.TIME_OUT_CODE));
            hashMap.put(CMNativeAd.KEY_REPORT_PKGNAME, FaceBookInfomation.getFBReportPkg(getAdTypeName()));
            hashMap.put(CMNativeAd.KEY_LOAD_SIZE, Integer.valueOf(num));
            bVar.a(context, this, hashMap);
            this.c = System.currentTimeMillis();
            doLoadReport();
        }
    }

    public final void onNativeAdLoaded(INativeAd nativeAd) {
        doLoadSuccessReport(System.currentTimeMillis() - this.c);
    }

    public final void onNativeAdFailed(String errorCode) {
        this.mNativeAdListener.adFailedToLoad(getAdTypeName(), errorCode);
        doLoadFailReport(System.currentTimeMillis() - this.c, errorCode);
    }

    public final void onNativeAdClick(INativeAd nativeAd) {
        this.mNativeAdListener.adClicked(nativeAd);
    }

    public final void onNativeAdLoaded(List<INativeAd> list) {
        synchronized (this.b) {
            this.b.addAll(list);
        }
        this.mNativeAdListener.adLoaded(getAdTypeName());
        doLoadSuccessReport(System.currentTimeMillis() - this.c);
    }

    public final INativeAd getAd() {
        return null;
    }

    public final List<INativeAd> getAdList(int num) {
        a.a("FBNativeListLoader", "getAd");
        ArrayList arrayList = new ArrayList();
        synchronized (this.b) {
            removeExpiredAds(this.b);
            if (!this.b.isEmpty()) {
                int min = Math.min(num, this.b.size());
                for (int i = 0; i < min; i++) {
                    INativeAd iNativeAd = (INativeAd) this.b.get(i);
                    iNativeAd.setReUseAd();
                    arrayList.add(iNativeAd);
                }
            }
            this.b.removeAll(arrayList);
        }
        return arrayList;
    }
}
