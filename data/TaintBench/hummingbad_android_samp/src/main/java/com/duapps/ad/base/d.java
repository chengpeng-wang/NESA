package com.duapps.ad.base;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import com.duapps.ad.stats.ToolStatsCore;
import com.duapps.ad.stats.j;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.umeng.analytics.onlineconfig.a;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/* compiled from: ToolStatsHelper */
public class d {
    public String a;
    public String b;
    public int c = 0;
    public String d;
    public long e;

    public static void a(Editor editor) {
        if (VERSION.SDK_INT <= 8) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static String a(Context context) {
        try {
            Object invoke = Class.forName("com.dianxinos.DXStatService.stat.TokenManager").getMethod("getToken", new Class[]{Context.class}).invoke(null, new Object[]{context});
            if (invoke != null && (invoke instanceof String)) {
                return String.valueOf(invoke);
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static boolean a(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean b(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void a(Context context, j jVar) {
        a(context, "tctc", jVar);
    }

    public static void b(Context context, j jVar) {
        a(context, "tcta", jVar);
    }

    public static void c(Context context, j jVar) {
        a(context, "tct", jVar);
    }

    public static void d(Context context, j jVar) {
        j.a(context).a(jVar);
        a(context, "tctb", jVar);
    }

    public static void e(Context context, j jVar) {
        j.a(context).a(jVar);
        a(context, "tctp", jVar);
    }

    public static void f(Context context, j jVar) {
        a(context, "thi", jVar);
    }

    public static void a(String str, int i, Context context, int i2, long j, String str2, String str3, String str4) {
        if (4 <= i.l(context)) {
            try {
                JSONStringer endObject = new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("re").key("sid").value((long) i2).key("co").value((long) i).key("tsi").value(j).key("stack").value(str4).key(a.c).value(str).endObject();
                JSONStringer jSONStringer = new JSONStringer();
                jSONStringer.array();
                jSONStringer.value(new JSONObject(endObject.toString()));
                jSONStringer.endArray();
                ToolStatsCore.getInstance(context).a(str3, str2, jSONStringer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void g(Context context, j jVar) {
        a(context, "tccu", jVar);
    }

    private static void a(Context context, String str, j jVar) {
        if (1 <= i.l(context)) {
            ToolStatsCore instance = ToolStatsCore.getInstance(context);
            try {
                JSONStringer value = new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value(str).key("ts").value(System.currentTimeMillis());
                value.key("logid").value(jVar.i());
                value.key(MASTNativeAdConstants.ID_STRING).value(jVar.b());
                if (jVar.d() > 0) {
                    d e = jVar.e();
                    if (e != null) {
                        JSONStringer key = value.key("preclick");
                        int i = e.c;
                        Object obj = "";
                        if (i == 0) {
                            obj = "none";
                        } else if (1 == i) {
                            obj = "tctp";
                        } else if (2 == i) {
                            obj = "tctb";
                        } else if (3 == i) {
                            obj = "err";
                        }
                        key.value(obj);
                        value.key("adpkg").value(jVar.a());
                    }
                }
                if (jVar.b.equals("online")) {
                    value.key("adpkg").value(jVar.a());
                }
                String a = DuAdNetwork.a();
                if ("thi".equals(str) && a != null) {
                    value.key(DuAd.DB_REFERRER).value(a);
                }
                value.key("sid").value((long) jVar.a);
                if (str.equals("tctp")) {
                    value.key("directgp").value(jVar.l());
                }
                value.endObject();
                instance.reportEvent(jVar.b, value.toString(), 0);
            } catch (JSONException e2) {
                if (f.a()) {
                    f.a("ToolStatsHelper", "create report content failed.", e2);
                }
            }
        }
    }

    static Method a(Class<?> cls, String str, Class<?>[] clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    static Method a(String str, String str2, Class<?>[] clsArr) {
        try {
            return a(Class.forName(str), str2, (Class[]) clsArr);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    static Object a(Object obj, Method method, Object[] objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(Context context, int i, int i2, long j) {
        if (3 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("fbe").key("sid").value((long) i).key("co").value((long) i2).key("tsi").value(j).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static void a(Context context, String str, int i) {
        if (2 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("fbgr").key("sid").value((long) i).key("st").value(str).key("sid").value((long) i).key("ts").value(System.currentTimeMillis()).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static void h(Context context, j jVar) {
        if (1 <= i.l(context)) {
            ToolStatsCore instance = ToolStatsCore.getInstance(context);
            try {
                JSONStringer value = new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("show").key("sid").value((long) jVar.a).key("logid").value(jVar.i()).key("ts").value(System.currentTimeMillis());
                value.key("ids").array().value(jVar.b()).endArray();
                if (jVar.b.equals("online")) {
                    value.key("adpkg").value(jVar.a());
                }
                value.endObject();
                instance.reportEvent(jVar.b, value.toString(), 1);
            } catch (JSONException e) {
                if (f.a()) {
                    f.a("ToolStatsHelper", "create report content failed.", e);
                }
            }
        }
    }

    public static void a(Context context, String str, int i, int i2, long j) {
        String str2 = "dle";
        if ("high".equals(str)) {
            str2 = "dlhe";
        }
        a(context, i, i2, j, str2);
    }

    public static String a(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }

    public static void b(Context context, int i, int i2, long j) {
        a(context, i, i2, j, "ime");
    }

    public static void e(Context context, int i) {
        if (1 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("facebook", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("show").key("sid").value((long) i).key("ts").value(System.currentTimeMillis()).endObject().toString(), 1);
            } catch (Exception e) {
            }
        }
    }

    public static void c(Context context, int i, int i2, long j) {
        a(context, i, i2, j, "ole");
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    private static void a(Context context, int i, int i2, long j, String str) {
        if (3 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value(str).key("sid").value((long) i).key("co").value((long) i2).key("tsi").value(j).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static void i(Context context, j jVar) {
        a(context, "tcpp", jVar);
    }

    public static void a(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("your sid(" + i + ") is invalid, Please check it ");
        }
    }

    public static void f(Context context, int i) {
        if (1 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("facebook", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("tctc").key("sid").value((long) i).key("ts").value(System.currentTimeMillis()).endObject().toString(), 0);
            } catch (Exception e) {
            }
        }
    }

    public static void a(Context context, com.duapps.ad.entity.a aVar, int i) {
        a(context, aVar, i, "irc");
    }

    public static void b(Context context, com.duapps.ad.entity.a aVar, int i) {
        a(context, aVar, i, "crc");
    }

    private static void a(Context context, com.duapps.ad.entity.a aVar, int i, String str) {
        if (3 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value(str).key(MASTNativeAdConstants.ID_STRING).value(aVar.a).key("sid").value((long) aVar.o).key("co").value((long) i).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static void b(Context context, String str, int i) {
        if (2 <= i.l(context)) {
            a(context, "dlgr", str, i);
        }
    }

    public static void c(Context context, String str, int i) {
        if (2 <= i.l(context)) {
            a(context, "dlhgr", str, i);
        }
    }

    public static void d(Context context, String str, int i) {
        a(context, "imgr", str, i);
    }

    public static void e(Context context, String str, int i) {
        a(context, "olgr", str, i);
    }

    private static void a(Context context, String str, String str2, int i) {
        if (2 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value(str).key("sid").value((long) i).key("st").value(str2).key("ts").value(System.currentTimeMillis()).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }

    public static void a(Context context, int i) {
        if (2 <= i.l(context)) {
            a(context, "dln", Integer.toString(0), i);
        }
    }

    public static void b(Context context, int i) {
        a(context, "imn", Integer.toString(0), i);
    }

    public static void c(Context context, int i) {
        a(context, "oln", Integer.toString(0), i);
    }

    public static void d(Context context, int i) {
        if (2 <= i.l(context)) {
            a(context, "dlhn", Integer.toString(0), i);
        }
    }

    public static void a(Context context, int i, long j) {
        if (3 <= i.l(context)) {
            try {
                ToolStatsCore.getInstance(context).reportEvent("behavior", new JSONStringer().object().key(MASTNativeAdConstants.REQUESTPARAM_KEY).value("srce").key("co").value((long) i).key("tsi").value(j).endObject().toString(), 1);
            } catch (JSONException e) {
            }
        }
    }
}
