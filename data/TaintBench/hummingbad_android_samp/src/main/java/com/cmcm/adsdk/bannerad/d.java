package com.cmcm.adsdk.bannerad;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.requestconfig.RequestConfig;
import com.cmcm.adsdk.requestconfig.RequestConfig.ICallBack;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.log.a;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* compiled from: CMBannerLoadManager */
public final class d extends c implements IBannerRequestCallBack {
    private String a;
    private CMBannerAdSize b;
    private WeakReference<Context> c;
    /* access modifiers changed from: private */
    public Object d = new Object();
    /* access modifiers changed from: private */
    public List<PosBean> e;
    /* access modifiers changed from: private */
    public int f = 0;
    /* access modifiers changed from: private */
    public boolean g = false;
    /* access modifiers changed from: private */
    public c h;
    private IBannerRequestCallBack i;

    public d(String str, Context context, CMBannerAdSize cMBannerAdSize) {
        this.a = str;
        this.b = cMBannerAdSize;
        this.c = new WeakReference(context);
        this.e = new ArrayList();
    }

    public final void a() {
        if (!CMAdManager.isSDKWork()) {
            a.a("CMBannerLoadManager", "sdk is stop work");
        } else if (this.g) {
            a.a("CMBannerLoadManager", "alreay is loading, avoid repeat load");
        } else {
            RequestConfig.a().a(this.a, new ICallBack() {
                public final void onConfigLoaded(String posId, List<PosBean> beans) {
                    if (beans == null || beans.isEmpty()) {
                        a.a("CMBannerLoadManager", "get config failed,to notify load failed");
                        d.this.g = false;
                        d.this.onAdLoadFailed();
                        return;
                    }
                    a.a("CMBannerLoadManager", "get config success,to load banner ad");
                    synchronized (d.this.d) {
                        d.this.e = beans;
                    }
                    d.this.h = null;
                    d.this.f = 0;
                    d.this.d();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        c e = e();
        if (e != null) {
            e.a(this);
            this.g = true;
            e.a();
            this.h = e;
            return;
        }
        a.a("CMBannerLoadManager", "no loader,may be posid is invalid");
        this.g = false;
        if (this.i != null) {
            this.i.onAdLoadFailed();
        }
    }

    private c e() {
        if (this.h != null && this.f == 0) {
            return this.h;
        }
        c cVar;
        synchronized (this.d) {
            cVar = null;
            while (cVar == null) {
                c cVar2;
                PosBean posBean = (PosBean) this.e.get(this.f % this.e.size());
                String str = posBean.name;
                Context context = (Context) this.c.get();
                String str2 = posBean.parameter;
                if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || context == null || TextUtils.isEmpty(this.a)) {
                    cVar2 = null;
                } else {
                    cVar2 = b.a(str, context, str2, this.b, this.a);
                }
                if (cVar2 != null) {
                    a.a("CMBannerLoadManager", "type:" + posBean.name + "index:" + this.f);
                    cVar = cVar2;
                    break;
                }
                this.f++;
                if (this.f == this.e.size()) {
                    this.f = 0;
                    cVar = cVar2;
                    break;
                }
                cVar = cVar2;
            }
        }
        return cVar;
    }

    public final void b() {
        a.a("CMBannerLoadManager", "onDestroy");
        if (this.h != null) {
            this.h.b();
            this.h = null;
        }
        if (com.cmcm.adsdk.requestconfig.util.a.a(this.e)) {
            this.e.clear();
            this.e = null;
        }
        if (this.c.get() != null) {
            this.c = null;
        }
        if (this.i != null) {
            this.i = null;
        }
    }

    public final void a(IBannerRequestCallBack iBannerRequestCallBack) {
        this.i = iBannerRequestCallBack;
    }

    public final View c() {
        return null;
    }

    public final void onAdLoaded(c loader) {
        a.a("CMBannerLoadManager", "onAdLoaded,set isLoading flag is false");
        this.g = false;
        if (this.i != null) {
            this.i.onAdLoaded(loader);
        }
    }

    public final void onAdLoadFailed() {
        synchronized (this.d) {
            if (this.f < this.e.size() - 1) {
                this.f++;
                a.a("CMBannerLoadManager", "loadIndex:" + this.f + ",to load next type ad");
                d();
            } else {
                a.a("CMBannerLoadManager", "all loader failed, to notify failed");
                this.g = false;
                this.f = 0;
                this.i.onAdLoadFailed();
            }
        }
    }

    public final void onAdClicked() {
        if (this.i != null) {
            this.i.onAdClicked();
        }
    }
}
