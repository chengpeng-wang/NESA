package com.cmcm.adsdk.nativead;

import android.content.Context;
import com.cmcm.adsdk.nativead.NativeAdListManager.INativeAdListListener;
import com.cmcm.adsdk.requestconfig.RequestConfig;
import com.cmcm.adsdk.requestconfig.RequestConfig.ICallBack;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.ads.INativeAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: NativeAdListRequest */
public final class d implements INativeReqeustCallBack {
    private Context a;
    private String b;
    /* access modifiers changed from: private */
    public List<PosBean> c;
    private int d;
    /* access modifiers changed from: private */
    public Object e;
    private List<INativeAd> f;
    /* access modifiers changed from: private */
    public INativeAdListListener g;
    /* access modifiers changed from: private|volatile */
    public volatile boolean h;
    private Map<String, e> i;

    public d(Context context, String str) {
        this.c = new ArrayList();
        this.e = new Object();
        this.f = new ArrayList();
        this.i = new HashMap();
        this.f = new ArrayList();
        this.a = context.getApplicationContext();
        this.b = str;
    }

    public final void a(int i) {
        a.a("NativeAdListRequest", this.b + " loadAds num:" + i);
        if (!this.h) {
            this.d = i;
            this.h = true;
            this.f.clear();
            this.c.clear();
            RequestConfig.a().a(this.b, new ICallBack() {
                public final void onConfigLoaded(String posId, List<PosBean> beans) {
                    if (beans == null || beans.isEmpty()) {
                        a.a("NativeAdListRequest", "config is empty");
                        d.this.g.onLoadFail(10001);
                        d.this.h = false;
                        return;
                    }
                    synchronized (d.this.e) {
                        d.this.c.addAll(beans);
                    }
                    d.this.b();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        while (!this.c.isEmpty()) {
            PosBean posBean;
            e eVar;
            synchronized (this.e) {
                posBean = (PosBean) this.c.remove(0);
            }
            if (posBean == null) {
                eVar = null;
            } else if (this.i.containsKey(posBean.name)) {
                eVar = (e) this.i.get(posBean.name);
            } else {
                posBean.mIsLoadList = true;
                eVar = f.a(this.a, posBean);
                this.i.put(posBean.name, eVar);
            }
            int size = this.d - this.f.size();
            if (eVar != null) {
                eVar.setLoadCallBack(this);
                a.a("NativeAdListRequest", "to load:" + posBean.name + "load size:" + size);
                eVar.loadAds(size);
                return;
            }
        }
        if (this.f.isEmpty()) {
            a.a("NativeAdListRequest", "all request finish, all fail callback onLoadFail");
            this.g.onLoadFail(10002);
        } else {
            a.a("NativeAdListRequest", "all request finish, callback onLoadFinish");
            this.g.onLoadFinish();
        }
        this.h = false;
    }

    public final void adLoaded(String adTypeName) {
        if (this.h) {
            a.a("NativeAdListRequest", adTypeName + " load success");
            e eVar = (e) this.i.get(adTypeName);
            if (eVar != null) {
                List adList = eVar.getAdList(this.d);
                if (adList != null) {
                    a.a("NativeAdListRequest", adTypeName + " load success size :" + adList.size());
                }
                a(adList);
            }
            if (this.f.size() < this.d) {
                a.a("NativeAdListRequest", "success size < expect size, callback onLoadProcess");
                b();
                this.g.onLoadProcess();
                return;
            }
            this.h = false;
            a.a("NativeAdListRequest", "success size >= expect size, callback onLoadFinish");
            this.g.onLoadFinish();
            return;
        }
        a.a("NativeAdListRequest", "load task has finish");
    }

    public final void adFailedToLoad(String adTypeName, String errorInfo) {
        a.a("NativeAdListRequest", adTypeName + " load fail error:" + errorInfo);
        if (this.h) {
            b();
        } else {
            a.a("NativeAdListRequest", "load task has finish");
        }
    }

    public final void adClicked(INativeAd ad) {
        this.g.onAdClicked(ad);
    }

    private void a(List<INativeAd> list) {
        if (list != null && !list.isEmpty()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (a((INativeAd) it.next())) {
                    it.remove();
                }
            }
            synchronized (this.f) {
                this.f.addAll(list);
            }
        }
    }

    private boolean a(INativeAd iNativeAd) {
        synchronized (this.f) {
            for (INativeAd iNativeAd2 : this.f) {
                if (iNativeAd2.getAdTitle().equals(iNativeAd.getAdTitle())) {
                    a.a("NativeAdListRequest", "ad :" + iNativeAd2.getAdTitle() + "has in list");
                    return true;
                }
            }
            return false;
        }
    }

    public final List<INativeAd> a() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f);
        return arrayList;
    }

    public final void a(INativeAdListListener iNativeAdListListener) {
        this.g = iNativeAdListListener;
    }
}
