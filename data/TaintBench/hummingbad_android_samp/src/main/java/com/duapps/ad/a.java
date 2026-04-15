package com.duapps.ad;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import com.duapps.ad.base.c;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.entity.strategy.NativeAd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: ADRequestController */
public class a implements Callback, IDuAdController {
    private static final String a = a.class.getSimpleName();
    private Context b;
    private int c;
    private Handler d;
    /* access modifiers changed from: private */
    public DuAdDataCallBack e;
    private boolean f;
    private ConcurrentHashMap<String, com.duapps.ad.entity.strategy.a<NativeAd>> g = new ConcurrentHashMap();
    private List<String> h = Collections.synchronizedList(new ArrayList());
    private List<String> i = Collections.synchronizedList(new ArrayList());
    private long j;
    private HandlerThread k;
    private volatile boolean l;
    /* access modifiers changed from: private|volatile */
    public volatile boolean m;
    private DuAdDataCallBack n = new b(this);

    public a(Context context, int i, int i2) {
        this.b = context;
        this.c = i;
        this.h.addAll(c.a(com.duapps.ad.c.a.a.a(this.b).a(this.c, true).a, this.b, this.c));
        this.j = c.a(this.b, this.c, i2, this.h, this.g);
        a();
        this.k = new HandlerThread("adRequest", 10);
        this.k.start();
        this.d = new Handler(this.k.getLooper(), this);
    }

    private void a() {
        for (String str : this.h) {
            if (c(str)) {
                ((com.duapps.ad.entity.strategy.a) this.g.get(str)).a(this.n);
            }
        }
    }

    public void fill() {
        if (d.b(this.b)) {
            for (String str : this.g.keySet()) {
                if (c(str)) {
                    ((com.duapps.ad.entity.strategy.a) this.g.get(str)).a();
                }
            }
            this.f = true;
        }
    }

    public final void a(DuAdDataCallBack duAdDataCallBack) {
        this.e = duAdDataCallBack;
    }

    public void load() {
        if (!d.b(this.b)) {
            this.n.onAdError(AdError.NETWORK_ERROR);
        } else if (this.m) {
            f.c(a, "Current task is already refreshing.");
        } else {
            boolean z;
            this.l = false;
            this.m = true;
            if (this.f) {
                this.f = false;
                int i = 0;
                for (String str : this.h) {
                    if (c(str)) {
                        com.duapps.ad.entity.strategy.a aVar = (com.duapps.ad.entity.strategy.a) this.g.get(str);
                        int b = aVar.b();
                        if (i == 0 && "facebook".equals(str) && b == 0 && !aVar.a) {
                            aVar.c = false;
                            this.d.sendEmptyMessage(100);
                            break;
                        }
                        i++;
                        if (b > 0) {
                            NativeAd nativeAd = (NativeAd) aVar.d();
                            if (nativeAd != null) {
                                this.n.onAdLoaded(nativeAd);
                                f.c(a, "onAdLoaded in load method");
                                z = true;
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
            z = false;
            if (!z) {
                this.i.clear();
                for (String str2 : this.h) {
                    if (c(str2)) {
                        ((com.duapps.ad.entity.strategy.a) this.g.get(str2)).a = false;
                        ((com.duapps.ad.entity.strategy.a) this.g.get(str2)).c = false;
                    }
                }
                this.d.sendEmptyMessage(100);
            }
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 100:
                this.d.removeMessages(100);
                f.c(a, "scanResult");
                long elapsedRealtime = SystemClock.elapsedRealtime();
                Object obj = null;
                while (obj == null && !this.l) {
                    Object obj2;
                    synchronized (this.h) {
                        for (String str : this.h) {
                            if (this.l) {
                                f.c(a, "Current action has been canceled~");
                                obj2 = obj;
                            } else if (!this.i.contains(str)) {
                                SystemClock.sleep(10);
                                long elapsedRealtime2 = SystemClock.elapsedRealtime() - elapsedRealtime;
                                if (elapsedRealtime2 > this.j) {
                                    obj2 = 1;
                                    this.n.onAdError(AdError.TIME_OUT_ERROR);
                                } else if (this.g.containsKey(str) || this.g.get(str) != null) {
                                    com.duapps.ad.entity.strategy.a aVar = (com.duapps.ad.entity.strategy.a) this.g.get(str);
                                    f.c(a, "----------------------------" + str);
                                    if (aVar.a) {
                                        if (this.g.keySet().size() <= 1) {
                                            obj2 = 1;
                                            this.n.onAdError(AdError.NO_FILL);
                                        } else {
                                            a(str);
                                            if (!this.i.contains(str)) {
                                                this.i.add(str);
                                            }
                                            f.c(a, "channel:" + str + " is error and removed");
                                        }
                                    } else if (aVar.b() > 0) {
                                        long b = b(str);
                                        f.c(a, "channel:" + str + "-->[" + b + "," + this.j + "]");
                                        obj2 = (elapsedRealtime2 <= b || elapsedRealtime2 >= this.j) ? null : 1;
                                        if (obj2 != null) {
                                            NativeAd nativeAd = (NativeAd) aVar.d();
                                            if (nativeAd != null) {
                                                this.n.onAdLoaded(nativeAd);
                                                f.c(a, "onAdLoaded in load method");
                                                obj2 = 1;
                                            }
                                        } else {
                                            continue;
                                        }
                                    } else if (!(aVar.b || aVar.c)) {
                                        aVar.a();
                                        f.c(a, str + " is refreshing...");
                                    }
                                }
                            }
                        }
                        obj2 = obj;
                    }
                    obj = obj2;
                }
                break;
        }
        return false;
    }

    private void a(String str) {
        int indexOf = this.h.indexOf(str);
        int size = this.h.size();
        if (indexOf != size - 1) {
            for (int i = size - 1; i > indexOf; i--) {
                if (i - 1 >= 0) {
                    String str2 = (String) this.h.get(i);
                    String str3 = (String) this.h.get(i - 1);
                    com.duapps.ad.entity.strategy.a aVar = (com.duapps.ad.entity.strategy.a) this.g.get(str2);
                    com.duapps.ad.entity.strategy.a aVar2 = (com.duapps.ad.entity.strategy.a) this.g.get(str3);
                    if (!(aVar == null || aVar2 == null)) {
                        aVar.d = aVar2.d;
                        f.c(a, "channel :" + str2 + ", used --> channel :" + str3);
                    }
                }
            }
        }
    }

    private long b(String str) {
        int indexOf = this.h.indexOf(str);
        long j = 0;
        int i = 0;
        while (i < indexOf) {
            long j2;
            com.duapps.ad.entity.strategy.a aVar = (com.duapps.ad.entity.strategy.a) this.g.get(this.h.get(i));
            if (aVar == null) {
                j2 = j;
            } else {
                j2 = j + aVar.d;
            }
            i++;
            j = j2;
        }
        return j;
    }

    public void clearCache() {
        for (String str : this.h) {
            if (c(str)) {
                ((com.duapps.ad.entity.strategy.a) this.g.get(str)).c();
            }
        }
    }

    private boolean c(String str) {
        return this.g.containsKey(str) && this.g.get(str) != null;
    }

    public void destroy() {
        this.l = true;
    }

    public final void a(List<String> list) {
        if (this.g != null && c("facebook")) {
            com.duapps.ad.entity.strategy.a aVar = (com.duapps.ad.entity.strategy.a) this.g.get("facebook");
            if (aVar != null) {
                ((com.duapps.ad.entity.f) aVar).a((List) list);
            }
        }
    }
}
