package com.cmcm.adsdk.nativead;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.IHookLoader;
import com.cmcm.adsdk.b;
import com.cmcm.adsdk.requestconfig.RequestConfig;
import com.cmcm.adsdk.requestconfig.RequestConfig.ICallBack;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.baseapi.ads.INativeAd;
import com.cmcm.baseapi.ads.INativeAdLoader;
import com.cmcm.baseapi.ads.INativeAdLoader.INativeAdLoaderListener;
import com.cmcm.baseapi.utils.Assure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: RequestNativeAd */
public final class j extends e implements INativeReqeustCallBack {
    private int a = CMAdManager.getPicksProtectTime();
    /* access modifiers changed from: private */
    public Handler b = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public List<PosBean> c;
    /* access modifiers changed from: private|final */
    public final Object d = new Object();
    /* access modifiers changed from: private|volatile */
    public volatile boolean e = true;
    private INativeAdLoaderListener f;
    /* access modifiers changed from: private */
    public boolean g = false;
    private Timer h = null;
    private boolean i = false;
    private int j;
    private long k;
    private boolean l = false;
    private boolean m = false;
    private boolean n = false;
    private Map<String, INativeAdLoader> o = new HashMap();
    /* access modifiers changed from: private */
    public Map<String, b> p = new HashMap();
    private Map<String, IHookLoader> q = new HashMap();
    private boolean r = false;
    private Runnable s = new Runnable() {
        public final void run() {
            synchronized (j.this.p) {
                if (j.this.p.isEmpty()) {
                    j.this.b.postDelayed(this, 4000);
                    com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "now load no callback , to issue next ");
                    j.this.d();
                } else {
                    com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "has callback,remove no callback task");
                    j.this.b.removeCallbacks(this);
                }
            }
        }
    };
    private Runnable t = new Runnable() {
        public final void run() {
            j.h(j.this);
        }
    };

    /* compiled from: RequestNativeAd */
    class a extends TimerTask {
        a() {
        }

        public final void run() {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "timeout, to check this load finish");
            j.this.g = true;
            j.this.e();
        }
    }

    static /* synthetic */ void d(j jVar) {
        int i;
        int i2 = 0;
        jVar.p.clear();
        jVar.n = false;
        jVar.k = System.currentTimeMillis();
        jVar.j = 0;
        jVar.i = false;
        jVar.g = false;
        try {
            jVar.h = new Timer();
            jVar.h.schedule(new a(), 8000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jVar.c.size() > 1) {
            jVar.b.postDelayed(jVar.s, 4000);
            jVar.l = true;
        }
        if (jVar.c == null || jVar.c.isEmpty()) {
            i = 0;
        } else if (jVar.m) {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "is preload,request size = 1");
            i = Math.min(jVar.c.size(), 1);
        } else if (!jVar.r) {
            i = Math.min(jVar.c.size(), 2);
        } else if (jVar.b() + 1 <= 2) {
            i = Math.min(jVar.c.size(), 2);
        } else {
            i = jVar.c.size();
        }
        while (i2 < i) {
            PosBean posBean;
            synchronized (jVar.d) {
                posBean = (PosBean) jVar.c.get(i2);
            }
            jVar.a(posBean);
            i2++;
        }
    }

    static /* synthetic */ void h(j jVar) {
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "check finish");
        if (jVar.e) {
            com.cmcm.adsdk.requestconfig.log.a.c(Const.TAG, "already finished");
            return;
        }
        if (jVar.i) {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "remove delay task");
            jVar.b.removeCallbacks(jVar.t);
        }
        if (jVar.l) {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "remove no callback task");
            jVar.b.removeCallbacks(jVar.s);
        }
        if (jVar.r) {
            INativeAdLoader iNativeAdLoader = (INativeAdLoader) jVar.o.get(Const.KEY_CM);
            if (iNativeAdLoader != null && (iNativeAdLoader instanceof g) && ((g) iNativeAdLoader).a()) {
                jVar.e = true;
                jVar.f();
            }
        }
        if (!jVar.e) {
            for (PosBean posBean : jVar.c) {
                b bVar = (b) jVar.p.get(posBean.name);
                if (bVar != null || jVar.g) {
                    if (bVar != null && bVar.a()) {
                        jVar.e = true;
                        jVar.f();
                        break;
                    }
                } else {
                    com.cmcm.adsdk.requestconfig.log.a.c(Const.TAG, "is timeout:" + jVar.g + "...wait");
                    return;
                }
            }
        }
        if (!jVar.e) {
            if (jVar.g) {
                jVar.a(10004);
            } else {
                jVar.a(10002);
            }
        }
        try {
            if (jVar.h != null) {
                jVar.e = true;
                jVar.h.cancel();
                jVar.h = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public j(Context context, String str) {
        super(context, str, Const.KEY_JUHE);
    }

    public final void loadAds(int num) {
    }

    public final void setAdListener(INativeAdLoaderListener adListener) {
        this.f = adListener;
    }

    public final void a(boolean z) {
        this.m = z;
    }

    public final void loadAd() {
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "posid " + this.mPositionId + " loadAd...");
        if (!CMAdManager.isSDKWork()) {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "sdk has stop work");
        } else if (this.e) {
            doLoadReport();
            this.e = false;
            RequestConfig.a().a(this.mPositionId, new ICallBack() {
                public final void onConfigLoaded(String posId, List<PosBean> beans) {
                    if (beans == null || beans.isEmpty()) {
                        com.cmcm.adsdk.requestconfig.log.a.d(Const.TAG, "the posid:" + j.this.mPositionId + "no config, may be has closed");
                        j.this.a(10001);
                        j.this.e = true;
                        return;
                    }
                    synchronized (j.this.d) {
                        j.this.c = beans;
                    }
                    j.c(j.this);
                    j.d(j.this);
                }
            });
        } else {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "wait and reuse for last result");
        }
    }

    private int b() {
        synchronized (this.d) {
            for (int i = 0; i < this.c.size(); i++) {
                if (((PosBean) this.c.get(i)).name.equalsIgnoreCase(Const.KEY_CM)) {
                    return i;
                }
            }
            return -1;
        }
    }

    private boolean a(PosBean posBean) {
        this.j++;
        INativeAdLoader a = a(this.mContext, posBean);
        String str = posBean.name;
        if (a != null) {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "to load " + str);
            if (str.equals(Const.KEY_CM)) {
                this.n = true;
            }
            a.loadAd();
            return true;
        }
        adFailedToLoad(str, "10005");
        return false;
    }

    private INativeAdLoader a(Context context, PosBean posBean) {
        if (!(posBean == null || TextUtils.isEmpty(posBean.name))) {
            Object obj;
            if (posBean.weight.intValue() > 0) {
                obj = 1;
            } else {
                obj = null;
            }
            if (obj != null) {
                if (this.q.containsKey(posBean.name)) {
                    IHookLoader iHookLoader = (IHookLoader) this.q.get(posBean.name);
                    if (iHookLoader != null) {
                        iHookLoader.setLoadCallBack(this);
                        return iHookLoader;
                    }
                }
                if (this.o.containsKey(posBean.name)) {
                    return (INativeAdLoader) this.o.get(posBean.name);
                }
                INativeAdLoader a = f.a(context, posBean);
                if (a == null) {
                    return a;
                }
                this.o.put(posBean.name, a);
                a.setLoadCallBack(this);
                a.setRequestParams(this.requestParams);
                return a;
            }
        }
        return null;
    }

    /* JADX WARNING: Missing block: B:16:0x002d, code skipped:
            com.cmcm.adsdk.requestconfig.log.a.a(com.cmcm.adsdk.Const.TAG, "has delay to check finish :" + r4.i);
     */
    /* JADX WARNING: Missing block: B:17:0x0045, code skipped:
            if (r4.i != false) goto L_?;
     */
    /* JADX WARNING: Missing block: B:21:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            return true;
     */
    private boolean c() {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.o;
        r2 = "cm";
        r1 = r1.containsKey(r2);
        if (r1 != 0) goto L_0x0013;
    L_0x000b:
        r1 = "CMCMADSDK";
        r2 = "now loaders no picks, no need to delay";
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
    L_0x0012:
        return r0;
    L_0x0013:
        r1 = r4.p;
        monitor-enter(r1);
        r2 = r4.p;	 Catch:{ all -> 0x0029 }
        r3 = "cm";
        r2 = r2.containsKey(r3);	 Catch:{ all -> 0x0029 }
        if (r2 == 0) goto L_0x002c;
    L_0x0020:
        r2 = "CMCMADSDK";
        r3 = "result map has picks, no need to delay";
        com.cmcm.adsdk.requestconfig.log.a.a(r2, r3);	 Catch:{ all -> 0x0029 }
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        goto L_0x0012;
    L_0x0029:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        throw r0;
    L_0x002c:
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        r1 = "CMCMADSDK";
        r2 = new java.lang.StringBuilder;
        r3 = "has delay to check finish :";
        r2.<init>(r3);
        r3 = r4.i;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
        r1 = r4.i;
        if (r1 != 0) goto L_0x0012;
    L_0x0047:
        r0 = 1;
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.nativead.j.c():boolean");
    }

    /* access modifiers changed from: private */
    public void d() {
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "issue to load low priority ad");
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "next load index :" + this.j + ",config size:" + this.c.size());
        if (this.j < this.c.size()) {
            PosBean posBean;
            synchronized (this.d) {
                posBean = (PosBean) this.c.get(this.j);
            }
            a(posBean);
            return;
        }
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "the load index is last one,remove no callback task");
        this.b.removeCallbacks(this.t);
    }

    private boolean a(String str, boolean z, String str2) {
        boolean z2 = false;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.p) {
                if (this.p.containsKey(str)) {
                    com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, str + "has callback result,may be this is a error");
                } else {
                    com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "push " + str + " to result map ,is scuccess:" + z);
                    this.p.put(str, new b(z, str2));
                    z2 = true;
                }
            }
        }
        return z2;
    }

    public final void adLoaded(String adTypeName) {
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, adTypeName + " load success");
        if (a(adTypeName, true, null) && !this.e) {
            if (this.m && this.r && !this.n) {
                int b = b();
                if (b != -1) {
                    a((PosBean) this.c.get(b));
                    return;
                }
            }
            if (c()) {
                this.i = true;
                long currentTimeMillis = System.currentTimeMillis() - this.k;
                com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "load spend time: " + currentTimeMillis + ",picks protect time :" + this.a);
                if (currentTimeMillis > ((long) this.a)) {
                    currentTimeMillis = 0;
                } else {
                    currentTimeMillis = ((long) this.a) - currentTimeMillis;
                }
                com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "delay to check finish , need delay time :" + currentTimeMillis);
                this.b.postDelayed(this.t, currentTimeMillis);
                return;
            }
            e();
        }
    }

    public final void adFailedToLoad(String adTypeName, String errorString) {
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, adTypeName + " load fail :error" + errorString);
        if (a(adTypeName, false, errorString) && !this.e) {
            d();
            e();
        }
    }

    public final void adClicked(INativeAd ad) {
        if (this.f != null) {
            this.f.adClicked(ad);
        }
    }

    /* access modifiers changed from: private */
    public void e() {
        this.b.post(this.t);
    }

    private void f() {
        Assure.checkEqual(this.b.getLooper(), Looper.myLooper());
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "notifyAdLoaded");
        if (this.f != null) {
            this.f.adLoaded();
        }
    }

    /* access modifiers changed from: private */
    public void a(int i) {
        Assure.checkEqual(this.b.getLooper(), Looper.myLooper());
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "notifyAdFailed");
        if (this.f != null) {
            this.f.adFailedToLoad(i);
        }
    }

    /* JADX WARNING: Missing block: B:48:?, code skipped:
            return r1;
     */
    public final com.cmcm.baseapi.ads.INativeAd getAd() {
        /*
        r6 = this;
        r3 = 0;
        r1 = "CMCMADSDK";
        r2 = "getAd";
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
        r1 = com.cmcm.adsdk.CMAdManager.isSDKWork();
        if (r1 != 0) goto L_0x0017;
    L_0x000e:
        r1 = "CMCMADSDK";
        r2 = "sdk has stop work";
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
        r1 = r3;
    L_0x0016:
        return r1;
    L_0x0017:
        r4 = r6.d;
        monitor-enter(r4);
        r1 = r6.c;	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0083;
    L_0x001e:
        r1 = r6.c;	 Catch:{ all -> 0x004e }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x004e }
        if (r1 != 0) goto L_0x0083;
    L_0x0026:
        r1 = r6.o;	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0083;
    L_0x002a:
        r1 = r6.r;	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0051;
    L_0x002e:
        r1 = r6.o;	 Catch:{ all -> 0x004e }
        r2 = "cm";
        r1 = r1.get(r2);	 Catch:{ all -> 0x004e }
        r1 = (com.cmcm.baseapi.ads.INativeAdLoader) r1;	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0051;
    L_0x003a:
        r2 = r1 instanceof com.cmcm.adsdk.nativead.g;	 Catch:{ all -> 0x004e }
        if (r2 == 0) goto L_0x0051;
    L_0x003e:
        r0 = r1;
        r0 = (com.cmcm.adsdk.nativead.g) r0;	 Catch:{ all -> 0x004e }
        r2 = r0;
        r2 = r2.a();	 Catch:{ all -> 0x004e }
        if (r2 == 0) goto L_0x0051;
    L_0x0048:
        r1 = r1.getAd();	 Catch:{ all -> 0x004e }
        monitor-exit(r4);	 Catch:{ all -> 0x004e }
        goto L_0x0016;
    L_0x004e:
        r1 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x004e }
        throw r1;
    L_0x0051:
        r1 = r6.c;	 Catch:{ all -> 0x004e }
        r5 = r1.iterator();	 Catch:{ all -> 0x004e }
        r2 = r3;
    L_0x0058:
        r1 = r5.hasNext();	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x007e;
    L_0x005e:
        r1 = r5.next();	 Catch:{ all -> 0x004e }
        r1 = (com.cmcm.adsdk.requestconfig.data.PosBean) r1;	 Catch:{ all -> 0x004e }
        r3 = r6.o;	 Catch:{ all -> 0x004e }
        r1 = r1.name;	 Catch:{ all -> 0x004e }
        r1 = r3.get(r1);	 Catch:{ all -> 0x004e }
        r1 = (com.cmcm.baseapi.ads.INativeAdLoader) r1;	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x0081;
    L_0x0070:
        r1 = r1.getAd();	 Catch:{ all -> 0x004e }
        if (r1 == 0) goto L_0x007c;
    L_0x0076:
        r2 = r1.getAdObject();	 Catch:{ all -> 0x004e }
        if (r2 != 0) goto L_0x007f;
    L_0x007c:
        r2 = r1;
        goto L_0x0058;
    L_0x007e:
        r1 = r2;
    L_0x007f:
        monitor-exit(r4);	 Catch:{ all -> 0x004e }
        goto L_0x0016;
    L_0x0081:
        r1 = r2;
        goto L_0x007c;
    L_0x0083:
        r1 = r3;
        goto L_0x007f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.nativead.j.getAd():com.cmcm.baseapi.ads.INativeAd");
    }

    public final List<INativeAd> getAdList(int num) {
        List list = null;
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "getAdList");
        if (CMAdManager.isSDKWork()) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.d) {
                if (this.c != null && !this.c.isEmpty() && this.o != null) {
                    INativeAdLoader iNativeAdLoader;
                    if (this.r) {
                        iNativeAdLoader = (INativeAdLoader) this.o.get(Const.KEY_CM);
                        if (iNativeAdLoader != null && (iNativeAdLoader instanceof g)) {
                            list = ((g) iNativeAdLoader).a(num);
                        }
                        if (!(list == null || list.isEmpty())) {
                            arrayList.addAll(list);
                        }
                    }
                    for (PosBean posBean : this.c) {
                        iNativeAdLoader = (INativeAdLoader) this.o.get(posBean.name);
                        if (iNativeAdLoader != null) {
                            List adList = iNativeAdLoader.getAdList(num - arrayList.size());
                            if (!(adList == null || adList.isEmpty())) {
                                arrayList.addAll(adList);
                                com.cmcm.adsdk.requestconfig.log.a.b(Const.TAG, "this mAdList size =" + arrayList.size());
                            }
                        }
                        if (arrayList.size() >= num) {
                            break;
                        }
                    }
                }
            }
            return arrayList;
        }
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "sdk has stop work");
        return null;
    }

    public final String a() {
        String jSONArray;
        JSONArray jSONArray2 = new JSONArray();
        synchronized (this.p) {
            for (String jSONArray3 : this.p.keySet()) {
                b bVar = (b) this.p.get(jSONArray3);
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("Adtype", jSONArray3);
                    jSONObject.put("IsSuccess", bVar.a());
                    jSONObject.put("ErrorInfo", bVar.b());
                    jSONArray2.put(jSONObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jSONArray3 = jSONArray2.toString();
        }
        return jSONArray3;
    }

    public final void a(String str, IHookLoader iHookLoader) {
        if (!TextUtils.isEmpty(str) && iHookLoader != null) {
            synchronized (this.d) {
                this.q.put(str, iHookLoader);
                this.o.put(str, iHookLoader);
            }
        }
    }

    public final void a(HashMap<String, IHookLoader> hashMap) {
        if (hashMap != null && !hashMap.isEmpty()) {
            synchronized (this.d) {
                this.q.putAll(hashMap);
                for (String str : hashMap.keySet()) {
                    this.o.put(str, hashMap.get(str));
                }
            }
        }
    }

    public final void b(boolean z) {
        this.r = z;
    }

    static /* synthetic */ void c(j jVar) {
        for (PosBean posBean : jVar.c) {
            jVar.o.put(posBean.name, jVar.a(jVar.mContext, posBean));
        }
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "mConfigBeans size:" + jVar.c.size());
        com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "mLoaderCache size:" + jVar.o.size());
    }
}
