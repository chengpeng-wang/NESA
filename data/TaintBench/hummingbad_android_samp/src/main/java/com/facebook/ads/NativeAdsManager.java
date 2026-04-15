package com.facebook.ads;

import android.content.Context;
import com.facebook.ads.NativeAd.MediaCacheFlag;
import com.facebook.ads.internal.adapters.n;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.c;
import com.facebook.ads.internal.e;
import com.facebook.ads.internal.i;
import com.facebook.ads.internal.i.a;
import com.facebook.ads.internal.util.l;
import com.facebook.ads.internal.util.m;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NativeAdsManager {
    private static final c a = c.ADS;
    /* access modifiers changed from: private|final */
    public final Context b;
    private final String c;
    private final int d;
    /* access modifiers changed from: private|final */
    public final List<NativeAd> e;
    /* access modifiers changed from: private */
    public int f = -1;
    /* access modifiers changed from: private */
    public Listener g;
    private i h;
    private boolean i = false;
    /* access modifiers changed from: private */
    public boolean j = false;

    public interface Listener {
        void onAdError(AdError adError);

        void onAdsLoaded();
    }

    public NativeAdsManager(Context context, String str, int i) {
        this.b = context;
        this.c = str;
        this.d = Math.max(i, 0);
        this.e = new ArrayList(i);
    }

    public void disableAutoRefresh() {
        this.i = true;
        if (this.h != null) {
            this.h.c();
        }
    }

    public int getUniqueNativeAdCount() {
        return this.e.size();
    }

    public boolean isLoaded() {
        return this.j;
    }

    public void loadAds() {
        loadAds(EnumSet.of(MediaCacheFlag.NONE));
    }

    public void loadAds(final EnumSet<MediaCacheFlag> enumSet) {
        e eVar = e.NATIVE_UNKNOWN;
        int i = this.d;
        if (this.h != null) {
            this.h.b();
        }
        this.h = new i(this.b, this.c, eVar, null, a, i, enumSet);
        if (this.i) {
            this.h.c();
        }
        this.h.a(new a() {
            public void a(b bVar) {
                if (NativeAdsManager.this.g != null) {
                    NativeAdsManager.this.g.onAdError(bVar.b());
                }
            }

            public void a(List<n> list) {
                int i = 0;
                final NativeAd[] nativeAdArr = new NativeAd[list.size()];
                final int[] iArr = new int[]{0};
                while (i < list.size()) {
                    n nVar = (n) list.get(i);
                    List arrayList = new ArrayList(2);
                    if (enumSet.contains(MediaCacheFlag.ICON) && nVar.k() != null) {
                        arrayList.add(nVar.k().getUrl());
                    }
                    if (enumSet.contains(MediaCacheFlag.IMAGE) && nVar.l() != null) {
                        arrayList.add(nVar.l().getUrl());
                    }
                    final List<n> list2 = list;
                    m.a(NativeAdsManager.this.b, arrayList, new l() {
                        public void a() {
                            nativeAdArr[i] = new NativeAd(NativeAdsManager.this.b, (n) list2.get(i), null);
                            int[] iArr = iArr;
                            iArr[0] = iArr[0] + 1;
                            if (iArr[0] == list2.size()) {
                                NativeAdsManager.this.j = true;
                                NativeAdsManager.this.e.clear();
                                NativeAdsManager.this.f = 0;
                                for (Object obj : nativeAdArr) {
                                    if (obj != null) {
                                        NativeAdsManager.this.e.add(obj);
                                    }
                                }
                                if (NativeAdsManager.this.g != null) {
                                    NativeAdsManager.this.g.onAdsLoaded();
                                }
                            }
                        }
                    });
                    i++;
                }
            }
        });
        this.h.a();
    }

    public NativeAd nextNativeAd() {
        if (this.e.size() == 0) {
            return null;
        }
        int i = this.f;
        this.f = i + 1;
        NativeAd nativeAd = (NativeAd) this.e.get(i % this.e.size());
        return i >= this.e.size() ? new NativeAd(nativeAd) : nativeAd;
    }

    public void setListener(Listener listener) {
        this.g = listener;
    }
}
