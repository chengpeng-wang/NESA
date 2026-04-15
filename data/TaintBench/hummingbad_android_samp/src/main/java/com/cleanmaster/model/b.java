package com.cleanmaster.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.cleanmaster.common.a;
import com.cleanmaster.gaid.AdvertisingIdHelper;
import com.cleanmaster.util.o;
import com.picksinit.c;

/* compiled from: BuinessPublicData */
public class b {
    public static final int a = c.getInstance().getIntMid();
    public static String b = null;
    public static String c = null;
    String d;
    int e;
    int f;
    String g;
    String h;
    String i = "";
    int j = 0;
    String k = null;
    int l = -1;
    String m = "";
    String n = "";
    String o = "";
    String p = "";
    String q;
    String r;
    String s;
    int t;
    int u;
    String v;
    int w = 0;

    public static b a(String str) {
        return a(str, 50, a);
    }

    public static b b(String str) {
        return a(str, 60, a);
    }

    public static b c(String str) {
        return a(str, 61, a);
    }

    public static b d(String str) {
        return a(str, 62, a);
    }

    public static b a(String str, String str2) {
        return a(str, 36, a).e(str2);
    }

    public static b b(String str, String str2) {
        return a(str, 38, a).e(str2);
    }

    public static b a(String str, String str2, int i) {
        int i2 = 3;
        int b = o.b(c.getInstance().getContext());
        Log.d("report", "test net state :" + b);
        switch (b) {
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 1;
                break;
            case 5:
                i2 = 2;
                break;
        }
        return a(str, str2, a, i2, i);
    }

    public static b a(String str, int i, int i2) {
        b bVar = new b();
        bVar.d = str;
        bVar.e = i2;
        bVar.f = i;
        bVar.g = a.a();
        String j = a.j(c.getInstance().getContext());
        String k = a.k(c.getInstance().getContext());
        bVar.h = String.format("%s_%s", new Object[]{j, k});
        bVar.j = c.getInstance().getmAdResourceRp() == 1 ? a.a(c.getInstance().getContext(), a.c()) : c.getInstance().getmCver();
        bVar.m = a(c.getInstance().getContext());
        bVar.n = AdvertisingIdHelper.getInstance().getGAId();
        bVar.p = b(c.getInstance().getContext());
        bVar.q = c.getInstance().getChannelId();
        return bVar;
    }

    public static b a(String str, String str2, int i, int i2, int i3) {
        b bVar = new b();
        bVar.n = AdvertisingIdHelper.getInstance().getGAId();
        bVar.m = a(c.getInstance().getContext());
        bVar.p = b(c.getInstance().getContext());
        bVar.e = i;
        bVar.d = str;
        bVar.v = str2;
        bVar.t = i2;
        bVar.u = i3;
        return bVar;
    }

    private static String a(Context context) {
        if (TextUtils.isEmpty(b)) {
            String g = a.g(context);
            b = g;
            if (TextUtils.isEmpty(g)) {
                b = "";
            }
        }
        return b;
    }

    private static String b(Context context) {
        if (TextUtils.isEmpty(c)) {
            String h = a.h(context);
            c = h;
            if (TextUtils.isEmpty(h)) {
                c = "";
            }
        }
        return c;
    }

    public static void a(b bVar, int i) {
        bVar.w = i;
    }

    public b e(String str) {
        this.k = str;
        return this;
    }

    public String a() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ac=" + this.f);
        stringBuilder.append("&pos=" + this.d);
        stringBuilder.append("&mid=" + this.e);
        stringBuilder.append("&aid=" + this.g);
        stringBuilder.append("&lan=" + this.h);
        stringBuilder.append("&ext=" + this.i);
        stringBuilder.append("&cmver=" + this.j);
        if (this.k != null) {
            stringBuilder.append("&rf=" + this.k);
        }
        if (this.l != -1) {
            stringBuilder.append("&g_pg=" + this.l);
        }
        stringBuilder.append("&mcc=" + this.m);
        stringBuilder.append("&mnc=" + this.p);
        stringBuilder.append("&gaid=" + this.n);
        stringBuilder.append("&placementid=" + this.o);
        stringBuilder.append("&pl=2");
        stringBuilder.append("&v=18");
        stringBuilder.append("&channelid=" + this.q);
        stringBuilder.append("&lp=" + this.w);
        if (this.t != 0) {
            stringBuilder.append("&nt=" + this.t);
        }
        if (this.u != 0) {
            stringBuilder.append("&type=" + this.u);
            if (this.u == 1) {
                if (this.r != null) {
                    stringBuilder.append("&total=" + this.r);
                }
                if (this.s != null) {
                    stringBuilder.append("&dns=" + this.s);
                }
            } else if (this.u == 2 && this.v != null) {
                stringBuilder.append("&render=" + this.v);
            }
        }
        return stringBuilder.toString();
    }

    public String b() {
        String a = o.a();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&gaid=" + this.n);
        stringBuilder.append("&mcc=" + this.m);
        stringBuilder.append("&mnc=" + this.p);
        if (this.u != 0) {
            stringBuilder.append("&type=" + this.u);
            if (this.u == 1) {
                if (a != null) {
                    stringBuilder.append("&total=" + a);
                }
            } else if (this.u == 2 && this.v != null) {
                stringBuilder.append("&render=" + this.v);
            }
        }
        stringBuilder.append("&mid=" + this.e);
        stringBuilder.append("&pos=" + this.d);
        if (this.t != 0) {
            stringBuilder.append("&nt=" + this.t);
            Log.d("report", "report nt :" + this.t);
        }
        return stringBuilder.toString();
    }
}
