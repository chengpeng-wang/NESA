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

/* compiled from: FBNativeLoader */
public final class c extends e implements NativeAdapterListener {
    public String[] a;
    private List<INativeAd> b;
    private List<INativeAd> c;
    private int d;
    private int e;
    private long f;

    public c(Context context, String str, String str2, String str3) {
        super(context, str2, str);
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = 0;
        this.e = 0;
        this.b = new ArrayList();
        this.c = new ArrayList();
        if (!TextUtils.isEmpty(str3)) {
            this.a = str3.split(",");
        }
    }

    public final void loadAd() {
        if (this.a == null || this.d >= this.a.length) {
            this.mNativeAdListener.adFailedToLoad(getAdTypeName(), "ssp adtype configured incorrectly");
            return;
        }
        synchronized (this.b) {
            removeExpiredAds(this.b);
        }
        if (this.b.isEmpty()) {
            Object obj = this.a[this.d];
            a.b("FBNativeLoader", "loadAd:mPlancementIdIndex=" + this.d + ":|mReqCount" + this.e + ":|placementIds.length" + this.a.length);
            this.d++;
            this.e++;
            a.b("FBNativeLoader", "loadAd:mPlancementIdIndex ++ =" + this.d + "|mReqCount++:" + this.e + "|placementIds.length" + this.a.length);
            com.cmcm.adsdk.adapter.c cVar = new com.cmcm.adsdk.adapter.c();
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
            cVar.a(context, this, hashMap);
            this.f = System.currentTimeMillis();
            doLoadReport();
            return;
        }
        this.mNativeAdListener.adLoaded(getAdTypeName());
    }

    public final void onNativeAdLoaded(INativeAd nativeAd) {
        a.b("FBNativeLoader", "onNativeAdLoaded");
        synchronized (this.b) {
            this.b.add(nativeAd);
        }
        a("", true, false);
        doLoadSuccessReport(System.currentTimeMillis() - this.f);
    }

    public final void onNativeAdFailed(String errorCode) {
        if (this.e < this.a.length) {
            a.b("FBNativeLoader", "onNativeAdFailed:mReqCount=" + this.e);
            a("", false, true);
            loadAd();
            doLoadFailReport(System.currentTimeMillis() - this.f, errorCode);
            return;
        }
        a(errorCode, false, false);
    }

    private void a(String str, boolean z, boolean z2) {
        if (this.d >= this.a.length) {
            a.b("FBNativeLoader", "onNativeAdFailed:mPlacementIdIndex=" + this.d);
            this.d = 0;
            a.b("FBNativeLoader", "onNativeAdFailed:Set mPlancementIdIndex = 0:" + this.d);
        }
        if (!z2) {
            a.b("FBNativeLoader", "onNativeAdFailed:All FBNative Failed Set mPlancementIdIndex = 0:" + this.d);
            this.e = 0;
            a.b("FBNativeLoader", "onNativeAdFailed:All FBNative Failed Set mReqCount = 0:" + this.e);
            if (z || !this.c.isEmpty()) {
                this.mNativeAdListener.adLoaded(getAdTypeName());
            } else {
                this.mNativeAdListener.adFailedToLoad(getAdTypeName(), str);
            }
        }
    }

    public final void onNativeAdClick(INativeAd nativeAd) {
        this.mNativeAdListener.adClicked(nativeAd);
    }

    public final void onNativeAdLoaded(List<INativeAd> list) {
    }

    public final INativeAd getAd() {
        INativeAd iNativeAd;
        a.a("FBNativeLoader", "getAd");
        synchronized (this.b) {
            removeExpiredAds(this.b);
            if (this.b.isEmpty()) {
                iNativeAd = null;
            } else {
                iNativeAd = (INativeAd) this.b.remove(0);
                a.a("FBNativeLoader", "adpool remove ad title: " + iNativeAd.getAdTitle() + ",adpool size:" + this.b.size());
                this.c.add(iNativeAd);
                a.a("FBNativeLoader", "ShowedAdPool add ad title: " + iNativeAd.getAdTitle() + ",ShowedAdPool size:" + this.c.size());
            }
        }
        if (iNativeAd == null) {
            removeExpiredAds(this.c);
            if (this.c == null || this.c.isEmpty()) {
                iNativeAd = null;
            } else {
                iNativeAd = (INativeAd) this.c.remove(0);
                this.c.add(this.c.size(), iNativeAd);
            }
        }
        if (iNativeAd != null) {
            iNativeAd.setReUseAd();
        }
        return iNativeAd;
    }

    public final List<INativeAd> getAdList(int num) {
        int i;
        INativeAd iNativeAd;
        a.a("FBNativeLoader", "getAd");
        ArrayList arrayList = new ArrayList();
        synchronized (this.b) {
            removeExpiredAds(this.b);
            if (!this.b.isEmpty()) {
                int min = Math.min(num, this.b.size());
                for (i = 0; i < min; i++) {
                    iNativeAd = (INativeAd) this.b.get(i);
                    iNativeAd.setReUseAd();
                    arrayList.add(iNativeAd);
                }
            }
            this.b.removeAll(arrayList);
        }
        removeExpiredAds(this.c);
        if (arrayList.size() < num && !this.c.isEmpty()) {
            int min2 = Math.min(num - arrayList.size(), this.c.size());
            for (i = 0; i < min2; i++) {
                iNativeAd = (INativeAd) this.c.remove(0);
                iNativeAd.setReUseAd();
                arrayList.add(iNativeAd);
            }
        }
        this.c.addAll(arrayList);
        return arrayList;
    }

    public final void loadAds(int num) {
    }
}
