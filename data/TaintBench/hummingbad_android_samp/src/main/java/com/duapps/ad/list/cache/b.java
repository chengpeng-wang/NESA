package com.duapps.ad.list.cache;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import com.duapps.ad.AdError;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.duapps.ad.base.t;
import com.duapps.ad.c.a.a;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.list.AdListArrivalListener;
import com.duapps.ad.list.DuNativeAdsManager;
import com.duapps.ad.list.a.c;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: NativeListRequestManagerProxy */
public final class b implements Callback, INativeListRequest {
    private static final String a = DuNativeAdsManager.class.getSimpleName();
    private static AdListArrivalListener l = new c();
    private Context b;
    private int c;
    private Handler d;
    private ConcurrentHashMap<String, com.duapps.ad.entity.strategy.b<List<NativeAd>>> e = new ConcurrentHashMap();
    private String[] f;
    private long g;
    private HandlerThread h;
    private volatile boolean i;
    /* access modifiers changed from: private */
    public AdListArrivalListener j;
    private int k;
    private AdListArrivalListener m = new d(this);

    public b(Context context, int i, int i2) {
        this.b = context;
        this.c = i;
        this.k = i2;
        com.duapps.ad.c.a.b a = a.a(this.b.getApplicationContext()).a(this.c);
        List list = a.a;
        this.f = (String[]) list.toArray(new String[list.size()]);
        long a2 = a.a("facebook");
        long a3 = a.a("download");
        this.e.put("download", new com.duapps.ad.list.a.a(this.b, this.c, a3, i2));
        this.g += a3;
        if (!TextUtils.isEmpty(t.a(this.b).b(this.c))) {
            this.e.put("facebook", new c(this.b, this.c, a2, i2));
            this.g += a2;
        }
        this.h = new HandlerThread("adRequest", 10);
        this.h.start();
        this.d = new Handler(this.h.getLooper(), this);
    }

    public final void setListener(AdListArrivalListener adListArrivalListener) {
        this.j = adListArrivalListener;
    }

    public final void fillList() {
        for (String str : this.e.keySet()) {
            ((com.duapps.ad.entity.strategy.b) this.e.get(str)).a();
        }
    }

    public final void loadList() {
        if (!d.b(this.b)) {
            this.m.onAdError(AdError.NETWORK_ERROR);
        } else if (i.g(this.b)) {
            i.h(this.b);
            ArrayList a = a(this.f, this.k);
            if (a == null || a.size() <= 0) {
                this.i = false;
                for (String str : this.f) {
                    if (b(str)) {
                        com.duapps.ad.entity.strategy.b bVar = (com.duapps.ad.entity.strategy.b) this.e.get(str);
                        bVar.a = false;
                        bVar.c = false;
                    }
                }
                this.d.sendEmptyMessage(100);
                return;
            }
            this.m.onAdLoaded(a);
        } else {
            this.m.onAdError(AdError.LOAD_TOO_FREQUENTLY);
        }
    }

    private ArrayList<NativeAd> a(String[] strArr, int i) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        ArrayList<NativeAd> arrayList = new ArrayList(i);
        for (String str : strArr) {
            if (b(str)) {
                com.duapps.ad.entity.strategy.b bVar = (com.duapps.ad.entity.strategy.b) this.e.get(str);
                if (bVar.b() > 0) {
                    List list = (List) bVar.d();
                    if (arrayList.size() == 0) {
                        synchronized (arrayList) {
                            arrayList.addAll(list);
                        }
                    } else {
                        int size = i - arrayList.size();
                        if (size > 0 && a(arrayList, list, size) <= 0) {
                            f.c(a, "No data added........");
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return arrayList;
    }

    private static int a(List<NativeAd> list, List<NativeAd> list2, int i) {
        ArrayList arrayList = new ArrayList(i);
        int i2 = 0;
        for (NativeAd nativeAd : list2) {
            String adTitle = nativeAd.getAdTitle();
            if (!TextUtils.isEmpty(adTitle)) {
                int i3;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    String adTitle2 = ((NativeAd) list.get(i4)).getAdTitle();
                    if (adTitle2 != null && adTitle2.equals(adTitle)) {
                        break;
                    }
                }
                if (i2 < i) {
                    arrayList.add(nativeAd);
                    i3 = i2 + 1;
                } else {
                    i3 = i2;
                }
                i2 = i3;
            }
        }
        if (i2 > 0) {
            synchronized (list) {
                list.addAll(arrayList);
            }
        }
        return i2;
    }

    public final boolean handleMessage(Message message) {
        switch (message.what) {
            case 100:
                this.d.removeMessages(100);
                long elapsedRealtime = SystemClock.elapsedRealtime();
                boolean z = false;
                while (!z && !this.i) {
                    int length = this.f.length;
                    int i = 0;
                    while (i < length) {
                        if (this.i) {
                            f.c(a, "Current action has been canceled~");
                        } else {
                            String str = this.f[i];
                            SystemClock.sleep(10);
                            long elapsedRealtime2 = SystemClock.elapsedRealtime() - elapsedRealtime;
                            if (elapsedRealtime2 > this.g) {
                                this.m.onAdError(AdError.TIME_OUT_ERROR);
                                z = true;
                            } else {
                                if (this.e.containsKey(str) || this.e.get(str) != null) {
                                    com.duapps.ad.entity.strategy.b bVar = (com.duapps.ad.entity.strategy.b) this.e.get(str);
                                    f.c(a, "channel:" + str + ",isError:" + bVar.a);
                                    if (!bVar.a) {
                                        f.c(a, "validCount:" + bVar.b() + ",ttl-->" + elapsedRealtime2);
                                        if (bVar.b() > 0) {
                                            if (a(str, elapsedRealtime2)) {
                                                List list = (List) bVar.d();
                                                if (list.size() > 0) {
                                                    this.m.onAdLoaded(list);
                                                    f.c(a, "onAdLoaded in load method");
                                                    z = true;
                                                }
                                            } else {
                                                continue;
                                            }
                                        } else if (!(bVar.b || bVar.c)) {
                                            bVar.a();
                                            f.c(a, str + " is refreshing!");
                                        }
                                    } else if (this.e.keySet().size() <= 1) {
                                        this.m.onAdError(AdError.TIME_OUT_ERROR);
                                        z = true;
                                    }
                                }
                                i++;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    private boolean a(String str, long j) {
        long[] a;
        long[] a2 = a(str);
        int a3 = a(this.f, str);
        int i = a3 - 1;
        int i2 = -1;
        while (i >= 0) {
            int i3;
            if (b(this.f[i]) && ((com.duapps.ad.entity.strategy.a) this.e.get(this.f[i])).a) {
                i3 = a3 - i > 1 ? i == 0 ? 0 : i - 1 : i;
                f.c(a, "Current channel:" + str + " , Use error channel" + this.f[i3] + " WT.");
            } else {
                i3 = i2;
            }
            i--;
            i2 = i3;
        }
        if (i2 != -1) {
            a = a(this.f[i2]);
        } else {
            a = a2;
        }
        if (j < a[0] || j > a[0] + a[1]) {
            return false;
        }
        return true;
    }

    private long[] a(String str) {
        int a = a(this.f, str);
        int length = this.f.length;
        long[] jArr = new long[2];
        for (int i = 0; i < length; i++) {
            if (b(this.f[i])) {
                if (i < a) {
                    jArr[0] = jArr[0] + ((com.duapps.ad.entity.strategy.b) this.e.get(this.f[i])).d;
                } else {
                    jArr[1] = jArr[1] + ((com.duapps.ad.entity.strategy.b) this.e.get(this.f[i])).d;
                }
            }
        }
        return jArr;
    }

    private static int a(String[] strArr, String str) {
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            if (strArr[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public final void clearCache() {
        for (String str : this.f) {
            if (b(str)) {
                ((com.duapps.ad.entity.strategy.b) this.e.get(str)).c();
            }
        }
    }

    private boolean b(String str) {
        return this.e.containsKey(str) && this.e.get(str) != null;
    }

    public final void destroy() {
        this.i = true;
        this.m = l;
        this.h.quit();
    }
}
