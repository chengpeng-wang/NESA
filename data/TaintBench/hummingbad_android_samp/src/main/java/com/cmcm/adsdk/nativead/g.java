package com.cmcm.adsdk.nativead;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.adapter.BaseNativeAdapter.NativeAdapterListener;
import com.cmcm.adsdk.adapter.e;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* compiled from: PicksNativeLoader */
public final class g extends e implements NativeAdapterListener {
    private List<INativeAd> a;
    private long b;

    protected g(Context context, String str) {
        super(context, str, Const.KEY_CM);
        this.a = null;
        this.a = new ArrayList();
    }

    public final void loadAd() {
        a.a("PicksNativeLoader", "loadAd");
        synchronized (this.a) {
            removeExpiredAds(this.a);
        }
        if (this.a.isEmpty()) {
            loadAds(10);
        } else {
            this.mNativeAdListener.adLoaded(getAdTypeName());
        }
    }

    public final INativeAd getAd() {
        a.a("PicksNativeLoader", "getAd");
        INativeAd iNativeAd = null;
        synchronized (this.a) {
            removeExpiredAds(this.a);
            if (!this.a.isEmpty()) {
                iNativeAd = (INativeAd) this.a.remove(0);
                iNativeAd.setReUseAd();
            }
        }
        return iNativeAd;
    }

    public final List<INativeAd> getAdList(int num) {
        return a(false, num);
    }

    private List<INativeAd> a(boolean z, int i) {
        ArrayList arrayList = new ArrayList();
        synchronized (this.a) {
            removeExpiredAds(this.a);
            int size = this.a.size();
            for (int i2 = 0; i2 < size; i2++) {
                INativeAd iNativeAd = (INativeAd) this.a.get(i2);
                if (!z || iNativeAd.isPriority()) {
                    iNativeAd.setReUseAd();
                    arrayList.add(iNativeAd);
                    if (arrayList.size() >= i) {
                        break;
                    }
                }
            }
            this.a.removeAll(arrayList);
        }
        return arrayList;
    }

    public final List<INativeAd> a(int i) {
        return a(true, i);
    }

    public final void onNativeAdLoaded(INativeAd nativeAd) {
        synchronized (this.a) {
            this.a.add(nativeAd);
        }
        this.mNativeAdListener.adLoaded(getAdTypeName());
        doLoadSuccessReport(System.currentTimeMillis() - this.b);
    }

    public final void onNativeAdFailed(String errorInfo) {
        this.mNativeAdListener.adFailedToLoad(getAdTypeName(), errorInfo);
        doLoadFailReport(System.currentTimeMillis() - this.b, errorInfo);
    }

    public final void onNativeAdClick(INativeAd nativeAd) {
        this.mNativeAdListener.adClicked(nativeAd);
    }

    public final void onNativeAdLoaded(List<INativeAd> list) {
        synchronized (this.a) {
            this.a.addAll(list);
        }
        this.mNativeAdListener.adLoaded(getAdTypeName());
        doLoadSuccessReport(System.currentTimeMillis() - this.b);
    }

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            return false;
     */
    public final boolean a() {
        /*
        r4 = this;
        r1 = 0;
        r2 = r4.a;
        monitor-enter(r2);
        r0 = r4.a;	 Catch:{ all -> 0x0021 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0021 }
        if (r0 != 0) goto L_0x001e;
    L_0x000c:
        r0 = r4.a;	 Catch:{ all -> 0x0021 }
        r3 = 0;
        r0 = r0.get(r3);	 Catch:{ all -> 0x0021 }
        r0 = (com.cmcm.baseapi.ads.INativeAd) r0;	 Catch:{ all -> 0x0021 }
        r0 = r0.isPriority();	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x001e;
    L_0x001b:
        r0 = 1;
        monitor-exit(r2);	 Catch:{ all -> 0x0021 }
    L_0x001d:
        return r0;
    L_0x001e:
        monitor-exit(r2);	 Catch:{ all -> 0x0021 }
        r0 = r1;
        goto L_0x001d;
    L_0x0021:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0021 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.nativead.g.a():boolean");
    }

    public final void loadAds(int num) {
        e eVar = new e();
        Context context = this.mContext;
        HashMap hashMap = new HashMap();
        hashMap.put(CMNativeAd.KEY_PLACEMENT_ID, this.mPositionId);
        hashMap.put(CMNativeAd.KEY_JUHE_POSID, this.mPositionId);
        hashMap.put(CMNativeAd.KEY_LOAD_SIZE, Integer.valueOf(num));
        eVar.a(context, this, hashMap);
        this.b = System.currentTimeMillis();
        doLoadReport();
    }
}
