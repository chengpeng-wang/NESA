package com.umeng.analytics.onlineconfig;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.f;
import java.util.Iterator;
import org.json.JSONObject;
import u.aly.ai;
import u.aly.aj;
import u.aly.ao;
import u.aly.ap;
import u.aly.av;

/* compiled from: OnlineConfigAgent */
public class a {
    public static final String a = "type";
    public static final String b = "package";
    public static final String c = "channel";
    public static final String d = "idmd5";
    public static final String e = "version_code";
    public static final String f = "appkey";
    public static final String g = "sdk_version";
    private final String h = "last_config_time";
    private final String i = "report_policy";
    private final String j = "online_config";
    private UmengOnlineConfigureListener k = null;
    /* access modifiers changed from: private */
    public c l = null;

    /* compiled from: OnlineConfigAgent */
    public class a extends ap {
        private JSONObject e;

        public a(JSONObject jSONObject) {
            super(null);
            this.e = jSONObject;
        }

        public JSONObject a() {
            return this.e;
        }

        public String b() {
            return this.d;
        }
    }

    /* compiled from: OnlineConfigAgent */
    public class b extends ao implements Runnable {
        Context a;

        public b(Context context) {
            this.a = context.getApplicationContext();
        }

        public void run() {
            try {
                b();
            } catch (Exception e) {
                a.this.a(null);
                aj.c(com.umeng.analytics.a.e, "reques update error", e);
            }
        }

        public boolean a() {
            return false;
        }

        private void b() {
            a aVar = new a(a.this.b(this.a));
            String[] strArr = com.umeng.analytics.a.g;
            b bVar = null;
            for (String a : strArr) {
                aVar.a(a);
                bVar = (b) a((ap) aVar, b.class);
                if (bVar != null) {
                    break;
                }
            }
            if (bVar == null) {
                a.this.a(null);
            } else if (bVar.b) {
                if (a.this.l != null) {
                    a.this.l.a(bVar.c, (long) bVar.d);
                }
                a.this.a(this.a, bVar);
                a.this.b(this.a, bVar);
                a.this.a(bVar.a);
            } else {
                a.this.a(null);
            }
        }
    }

    public void a(Context context) {
        if (context == null) {
            try {
                aj.b(com.umeng.analytics.a.e, "unexpected null context in updateOnlineConfig");
                return;
            } catch (Exception e) {
                aj.b(com.umeng.analytics.a.e, "exception in updateOnlineConfig");
                return;
            }
        }
        new Thread(new b(context.getApplicationContext())).start();
    }

    public void a(UmengOnlineConfigureListener umengOnlineConfigureListener) {
        this.k = umengOnlineConfigureListener;
    }

    public void a() {
        this.k = null;
    }

    public void a(c cVar) {
        this.l = cVar;
    }

    public void b() {
        this.l = null;
    }

    /* access modifiers changed from: private */
    public void a(JSONObject jSONObject) {
        if (this.k != null) {
            this.k.onDataReceived(jSONObject);
        }
    }

    /* access modifiers changed from: private */
    public JSONObject b(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", "online_config");
            jSONObject.put(f, AnalyticsConfig.getAppkey(context));
            jSONObject.put(e, ai.c(context));
            jSONObject.put(b, ai.u(context));
            jSONObject.put(g, com.umeng.analytics.a.c);
            jSONObject.put(d, av.b(ai.f(context)));
            jSONObject.put(c, AnalyticsConfig.getChannel(context));
            jSONObject.put("report_policy", f.a(context).a()[0]);
            jSONObject.put("last_config_time", c(context));
            return jSONObject;
        } catch (Exception e) {
            aj.b(com.umeng.analytics.a.e, "exception in onlineConfigInternal");
            return null;
        }
    }

    private String c(Context context) {
        return f.a(context).g().getString(com.umeng.analytics.a.j, "");
    }

    /* access modifiers changed from: private */
    public void a(Context context, b bVar) {
        Editor edit = f.a(context).g().edit();
        if (!TextUtils.isEmpty(bVar.e)) {
            edit.putString(com.umeng.analytics.a.j, bVar.e);
            edit.commit();
        }
        if (bVar.c != -1) {
            f.a(context).a(bVar.c, bVar.d);
        }
    }

    /* access modifiers changed from: private */
    public void b(Context context, b bVar) {
        if (bVar.a != null && bVar.a.length() != 0) {
            Editor edit = f.a(context).g().edit();
            try {
                JSONObject jSONObject = bVar.a;
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    edit.putString(str, jSONObject.getString(str));
                }
                edit.commit();
                aj.a(com.umeng.analytics.a.e, "get online setting params: " + jSONObject);
            } catch (Exception e) {
                aj.c(com.umeng.analytics.a.e, "save online config params", e);
            }
        }
    }
}
