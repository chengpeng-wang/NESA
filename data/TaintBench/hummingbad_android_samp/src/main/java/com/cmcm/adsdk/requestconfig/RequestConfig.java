package com.cmcm.adsdk.requestconfig;

import android.content.Context;
import android.os.AsyncTask;
import com.cmcm.adsdk.CMAdManager;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.request.RequestAction;
import com.cmcm.adsdk.requestconfig.request.RequestAction.RequestListener;
import com.cmcm.adsdk.requestconfig.util.b;
import com.cmcm.baseapi.utils.Assure;
import com.cmcm.baseapi.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RequestConfig {
    private static RequestConfig f;
    com.cmcm.adsdk.requestconfig.db.a a = null;
    boolean b = false;
    private RequestAction c;
    private Context d;
    private String e;
    private String g = null;
    private boolean h = false;
    /* access modifiers changed from: private */
    public Map<String, com.cmcm.adsdk.requestconfig.data.b.a> i = new HashMap();
    private volatile boolean j = false;
    private List<a> k = new ArrayList();

    public interface ICallBack {
        void onConfigLoaded(String str, List<PosBean> list);
    }

    static class a {
        String a;
        ICallBack b;

        a(String str, ICallBack iCallBack) {
            this.a = str;
            this.b = iCallBack;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0052  */
    /* JADX WARNING: Missing block: B:9:0x0043, code skipped:
            if (r1 != false) goto L_0x0045;
     */
    static /* synthetic */ com.cmcm.adsdk.requestconfig.data.b b(com.cmcm.adsdk.requestconfig.RequestConfig r9, java.lang.String r10) {
        /*
        r7 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = 0;
        r2 = 1;
        r1 = 0;
        r3 = "RequestConfig";
        r4 = new java.lang.StringBuilder;
        r5 = "request config done.,save time :";
        r4.<init>(r5);
        r5 = java.lang.System.currentTimeMillis();
        r5 = r5 / r7;
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r3, r4);
        r3 = java.lang.System.currentTimeMillis();
        r3 = r3 / r7;
        r5 = "config_loaded_time";
        com.cmcm.adsdk.requestconfig.util.b.a(r5, r3);
        r3 = r9.h;
        if (r3 != 0) goto L_0x0045;
    L_0x002c:
        r3 = android.text.TextUtils.isEmpty(r10);
        if (r3 == 0) goto L_0x004c;
    L_0x0032:
        r3 = r9.g;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x0043;
    L_0x003a:
        r3 = "default_config";
        r3 = com.cmcm.adsdk.requestconfig.util.b.b(r3, r1);
        if (r3 != 0) goto L_0x0043;
    L_0x0042:
        r1 = r2;
    L_0x0043:
        if (r1 == 0) goto L_0x004c;
    L_0x0045:
        r1 = "default_config";
        com.cmcm.adsdk.requestconfig.util.b.a(r1, r2);
        r10 = r9.g;
    L_0x004c:
        r1 = android.text.TextUtils.isEmpty(r10);
        if (r1 == 0) goto L_0x005a;
    L_0x0052:
        r1 = "RequestConfig";
        r2 = "request server config and default config failed, update config failed";
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
    L_0x0059:
        return r0;
    L_0x005a:
        r1 = "";
        r1 = com.cmcm.adsdk.requestconfig.util.b.a(r1);
        r2 = "RequestConfig";
        r3 = new java.lang.StringBuilder;
        r4 = "local config :";
        r3.<init>(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r2, r3);
        r2 = r9.h;
        if (r2 != 0) goto L_0x007e;
    L_0x0078:
        r1 = r1.equals(r10);
        if (r1 != 0) goto L_0x00bf;
    L_0x007e:
        com.cmcm.adsdk.requestconfig.util.b.b(r10);
        r0 = com.cmcm.adsdk.requestconfig.data.b.a(r10);
        r1 = "RequestConfig";
        r2 = new java.lang.StringBuilder;
        r3 = "reponse:";
        r2.<init>(r3);
        r2 = r2.append(r0);
        r2 = r2.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
        r9.c();
        r1 = r0.b();
        r2 = r9.d;
        r2 = com.cmcm.adsdk.requestconfig.db.a.a(r2);
        r1 = r2.a(r1);
        r2 = "RequestConfig";
        r3 = new java.lang.StringBuilder;
        r4 = "save satus:";
        r3.<init>(r4);
        r1 = r3.append(r1);
        r1 = r1.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r2, r1);
        goto L_0x0059;
    L_0x00bf:
        r1 = "RequestConfig";
        r2 = new java.lang.StringBuilder;
        r3 = "config no changed ";
        r2.<init>(r3);
        r2 = r2.append(r10);
        r3 = ",time:";
        r2 = r2.append(r3);
        r3 = java.lang.System.currentTimeMillis();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r1, r2);
        goto L_0x0059;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.requestconfig.RequestConfig.b(com.cmcm.adsdk.requestconfig.RequestConfig, java.lang.String):com.cmcm.adsdk.requestconfig.data.b");
    }

    public final void a(Context context, String str) {
        this.d = context;
        this.e = str;
        b.a(context, str);
        this.c = RequestAction.a();
        this.a = com.cmcm.adsdk.requestconfig.db.a.a(this.d);
        a.a(context).a(this.e);
    }

    public static RequestConfig a() {
        if (f == null) {
            f = new RequestConfig();
        }
        return f;
    }

    private RequestConfig() {
    }

    public final void a(String str, boolean z) {
        this.g = str;
        this.h = z;
    }

    public final void a(boolean z) {
        if (this.d != null) {
            if (b.b("config_loaded_time", 0) > 0 && !this.b) {
                b();
            }
            if (!(this.h || z)) {
                boolean z2;
                if (this.h) {
                    z2 = true;
                } else {
                    long currentTimeMillis = (System.currentTimeMillis() / 1000) - b.b("config_loaded_time", 0);
                    if (currentTimeMillis >= 7200) {
                        com.cmcm.adsdk.requestconfig.log.a.a("RequestConfig", "time:" + currentTimeMillis);
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                }
                if (!z2) {
                    return;
                }
            }
            if (this.c != null) {
                String str;
                this.j = true;
                RequestAction requestAction = this.c;
                if (CMAdManager.isChinaVersion()) {
                    str = Const.CONFIG_URL_CN;
                } else {
                    str = Const.CONFIG_URL;
                }
                String str2 = this.e;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("action=pos_config");
                stringBuilder.append("&postype=1");
                stringBuilder.append("&mid=" + str2);
                stringBuilder.append("&posid=");
                stringBuilder.append("&cver=" + CMAdManager.getAppVersionCode());
                stringBuilder.append("&lan=" + com.cmcm.adsdk.requestconfig.util.a.a());
                stringBuilder.append("&v=15");
                stringBuilder.append("&sdkv=2");
                requestAction.a(str, stringBuilder.toString(), new RequestListener() {
                    public final void onFailed(String reson) {
                        com.cmcm.adsdk.requestconfig.log.a.d("RequestConfig", "request failed..." + reson);
                        RequestConfig.a(RequestConfig.this, null);
                    }

                    public final void onSuccess(String obj) {
                        RequestConfig.a(RequestConfig.this, obj);
                    }
                });
            }
        }
    }

    public final void a(final String str, final ICallBack iCallBack) {
        if (ThreadUtils.runningOnUiThread()) {
            b(str, iCallBack);
        } else {
            ThreadUtils.postOnUiThread(new Runnable() {
                public final void run() {
                    RequestConfig.this.b(str, iCallBack);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void b(String str, ICallBack iCallBack) {
        Assure.checkRunningOnUIThread();
        if (this.b) {
            c(str, iCallBack);
            return;
        }
        b();
        this.k.add(new a(str, iCallBack));
    }

    private void c(String str, ICallBack iCallBack) {
        Assure.checkNotNull(iCallBack);
        Assure.checkNotNull(this.i);
        if (iCallBack != null) {
            List list;
            com.cmcm.adsdk.requestconfig.data.b.a aVar = (com.cmcm.adsdk.requestconfig.data.b.a) this.i.get(str);
            if (aVar != null) {
                list = aVar.c;
            } else {
                list = null;
            }
            iCallBack.onConfigLoaded(str, list);
        }
    }

    private void b() {
        Assure.checkFalse(this.b);
        if (this.a != null && !this.j) {
            this.j = true;
            com.cmcm.adsdk.utils.a.a(new AsyncTask<Void, Void, List<PosBean>>() {
                /* access modifiers changed from: protected|final|synthetic */
                public final /* synthetic */ void onPostExecute(Object x0) {
                    List<PosBean> list = (List) x0;
                    RequestConfig requestConfig = RequestConfig.this;
                    Map hashMap = new HashMap();
                    if (list != null) {
                        for (PosBean posBean : list) {
                            if (posBean.weight.intValue() > 0) {
                                com.cmcm.adsdk.requestconfig.data.b.a aVar;
                                String valueOf = String.valueOf(posBean.placeid);
                                if (hashMap.containsKey(valueOf)) {
                                    aVar = (com.cmcm.adsdk.requestconfig.data.b.a) hashMap.get(valueOf);
                                } else {
                                    aVar = new com.cmcm.adsdk.requestconfig.data.b.a();
                                    hashMap.put(valueOf, aVar);
                                }
                                aVar.c.add(posBean);
                            }
                        }
                        for (Entry value : hashMap.entrySet()) {
                            Collections.sort(((com.cmcm.adsdk.requestconfig.data.b.a) value.getValue()).c);
                        }
                    }
                    requestConfig.i = hashMap;
                    RequestConfig.a(RequestConfig.this);
                }

                /* access modifiers changed from: protected|final|synthetic */
                public final /* synthetic */ Object doInBackground(Object[] x0) {
                    return RequestConfig.this.a.a();
                }
            }, new Void[0]);
        }
    }

    private void c() {
        List<PosBean> a = this.a.a();
        com.cmcm.adsdk.requestconfig.log.a.a("RequestConfig", "old posbean list:" + a.toString());
        ArrayList arrayList = new ArrayList();
        for (PosBean posBean : a) {
            arrayList.add(Integer.valueOf(posBean.placeid));
        }
        com.cmcm.adsdk.requestconfig.log.a.a("RequestConfig", "delete mPositionId data status:" + this.a.b(arrayList));
    }

    static /* synthetic */ void a(RequestConfig requestConfig) {
        Assure.checkRunningOnUIThread();
        Assure.checkNotNull(requestConfig.i);
        requestConfig.j = false;
        requestConfig.b = true;
        Assure.checkRunningOnUIThread();
        for (a aVar : requestConfig.k) {
            if (aVar.b != null) {
                requestConfig.c(aVar.a, aVar.b);
            }
        }
        requestConfig.k.clear();
    }

    static /* synthetic */ void a(RequestConfig requestConfig, final String str) {
        com.cmcm.adsdk.requestconfig.log.a.a("RequestConfig", "update config in db");
        com.cmcm.adsdk.utils.a.a(new AsyncTask<Void, Void, com.cmcm.adsdk.requestconfig.data.b>() {
            /* access modifiers changed from: protected|final|synthetic */
            public final /* synthetic */ void onPostExecute(Object x0) {
                com.cmcm.adsdk.requestconfig.data.b bVar = (com.cmcm.adsdk.requestconfig.data.b) x0;
                com.cmcm.adsdk.requestconfig.log.a.a("RequestConfig", "onPostExecute isSuccess:" + bVar);
                if (!(bVar == null || bVar.a() == null)) {
                    RequestConfig.this.i = bVar.a();
                }
                RequestConfig.a(RequestConfig.this);
            }

            /* access modifiers changed from: protected|final|synthetic */
            public final /* synthetic */ Object doInBackground(Object[] x0) {
                return RequestConfig.b(RequestConfig.this, str);
            }
        }, new Void[0]);
    }
}
