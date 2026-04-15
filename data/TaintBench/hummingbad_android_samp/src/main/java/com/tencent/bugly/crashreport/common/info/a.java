package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.support.v4.os.EnvironmentCompat;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.af;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* compiled from: BUGLY */
public class a {
    private static a N = null;
    private String A = null;
    private Map<String, PlugInBean> B = null;
    private boolean C = true;
    private String D = null;
    private String E = null;
    private String F = null;
    private String G = null;
    private Boolean H = null;
    private String I = null;
    private String J = null;
    private String K = null;
    private List<String> L = null;
    private List<String> M = null;
    /* access modifiers changed from: private */
    public Object O = new Object();
    private int P = -1;
    private int Q = -1;
    private Map<String, String> R = new HashMap();
    private Map<String, String> S = new HashMap();
    private final Context a;
    /* access modifiers changed from: private */
    public String b;
    private final long c;
    private final String d;
    private final byte e;
    private String f;
    private final String g;
    private final String h = "com.tencent.bugly";
    private final String i = "1.2.8";
    private final String j = "";
    private final String k;
    private final String l;
    private final String m;
    private String n;
    private String o = EnvironmentCompat.MEDIA_UNKNOWN;
    private String p = EnvironmentCompat.MEDIA_UNKNOWN;
    private long q = 0;
    private String r = null;
    private String s = null;
    private String t = null;
    private String u = null;
    private String v = null;
    private long w = -1;
    private long x = -1;
    private long y = -1;
    private String z = null;

    /* compiled from: BUGLY */
    class a implements Runnable {
        a() {
        }

        public void run() {
            synchronized (a.this.O) {
                z.c("created from task", new Object[0]);
                a.this.b = UUID.randomUUID().toString();
            }
        }
    }

    private a(Context context) {
        z.c("com info create start", new Object[0]);
        this.c = new Date().getTime();
        this.a = ag.a(context);
        y.a().b(new a());
        this.e = (byte) 1;
        this.f = AppInfo.a(context);
        this.g = AppInfo.e(context);
        this.E = AppInfo.b(context);
        this.k = b.m();
        this.l = b.a();
        this.m = "Android " + b.b() + ",level " + b.c();
        this.d = this.l + ";" + this.m;
        this.L = AppInfo.f(context);
        this.M = AppInfo.h(context);
        z.c("com info create end", new Object[0]);
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (N == null) {
                N = new a(context);
            }
            aVar = N;
        }
        return aVar;
    }

    public String a() {
        String str;
        synchronized (this.O) {
            if (this.b == null) {
                this.b = UUID.randomUUID().toString();
            }
            str = this.b;
        }
        return str;
    }

    public long b() {
        return this.c;
    }

    public byte c() {
        return this.e;
    }

    public String d() {
        return this.F;
    }

    public void a(String str) {
        this.F = str;
    }

    public String e() {
        return this.E;
    }

    public String f() {
        return this.f;
    }

    public void b(String str) {
        this.f = str;
    }

    public void c(String str) {
        this.E = str;
    }

    public String g() {
        return "com.tencent.bugly";
    }

    public String h() {
        return "1.2.8";
    }

    public String i() {
        return "";
    }

    public String j() {
        return this.k;
    }

    public String k() {
        return this.l;
    }

    public String l() {
        return this.m;
    }

    public synchronized String m() {
        return this.o;
    }

    public synchronized void d(String str) {
        if (str == null) {
            str = "10000";
        }
        this.o = "" + str;
    }

    public synchronized String n() {
        String str;
        if (this.n != null) {
            str = this.n;
        } else {
            this.n = p() + "|" + r() + "|" + s();
            str = this.n;
        }
        return str;
    }

    public synchronized void e(String str) {
        this.n = str;
    }

    public synchronized void f(String str) {
        this.p = "" + str;
    }

    public synchronized long o() {
        return this.q;
    }

    public synchronized void a(long j) {
        this.q = j;
    }

    public synchronized String p() {
        String str;
        if (y()) {
            if (this.r == null) {
                this.r = b.a(this.a);
            }
            str = this.r;
        } else {
            str = "";
        }
        return str;
    }

    public synchronized String q() {
        String str;
        if (y()) {
            if (this.t == null) {
                this.t = b.d(this.a);
            }
            str = this.t;
        } else {
            str = "";
        }
        return str;
    }

    public synchronized String r() {
        String str;
        if (y()) {
            if (this.u == null) {
                this.u = b.b(this.a);
            }
            str = this.u;
        } else {
            str = "";
        }
        return str;
    }

    public synchronized String s() {
        String str;
        if (y()) {
            if (this.v == null) {
                this.v = b.c(this.a);
            }
            str = this.v;
        } else {
            str = "";
        }
        return str;
    }

    public synchronized long t() {
        if (this.w <= 0) {
            this.w = b.f();
        }
        return this.w;
    }

    public synchronized long u() {
        if (this.x <= 0) {
            this.x = b.h();
        }
        return this.x;
    }

    public synchronized long v() {
        if (this.y <= 0) {
            this.y = b.j();
        }
        return this.y;
    }

    public synchronized String w() {
        if (this.z == null) {
            this.z = b.e();
        }
        return this.z;
    }

    public synchronized String x() {
        if (this.A == null) {
            this.A = b.g(this.a);
        }
        return this.A;
    }

    public synchronized boolean y() {
        return this.C;
    }

    public synchronized Map<String, PlugInBean> z() {
        Map<String, PlugInBean> map;
        if (this.B == null || this.B.size() <= 0) {
            map = null;
        } else {
            map = new HashMap(this.B.size());
            map.putAll(this.B);
        }
        return map;
    }

    public String A() {
        if (this.D == null) {
            this.D = b.l();
        }
        return this.D;
    }

    public synchronized String B() {
        return this.G;
    }

    public synchronized void g(String str) {
        this.G = str;
    }

    public synchronized Boolean C() {
        if (this.H == null) {
            this.H = Boolean.valueOf(af.a(this.a).a());
        }
        return this.H;
    }

    public synchronized String D() {
        if (this.I == null) {
            this.I = "" + b.f(this.a);
            z.a("rom:%s", this.I);
        }
        return this.I;
    }

    public synchronized String E() {
        return this.g;
    }

    public synchronized String F() {
        return this.J;
    }

    public synchronized void h(String str) {
        this.J = str;
    }

    public synchronized String G() {
        return this.K;
    }

    public synchronized void i(String str) {
        this.K = str;
    }

    public List<String> H() {
        return this.L;
    }

    public List<String> I() {
        return this.M;
    }

    public synchronized Map<String, String> J() {
        Map<String, String> map;
        if (this.R.size() <= 0) {
            map = null;
        } else {
            map = new HashMap(this.R);
        }
        return map;
    }

    public synchronized String j(String str) {
        String str2;
        if (ag.b(str)) {
            z.d("key should not be empty %s", "" + str);
            str2 = null;
        } else {
            str2 = (String) this.R.remove(str);
        }
        return str2;
    }

    public synchronized String k(String str) {
        String str2;
        if (ag.b(str)) {
            z.d("key should not be empty %s", "" + str);
            str2 = null;
        } else {
            str2 = (String) this.R.get(str);
        }
        return str2;
    }

    public synchronized void a(String str, String str2) {
        if (ag.b(str) || ag.b(str2)) {
            z.d("key&value should not be empty %s %s", "" + str, "" + str2);
        } else {
            NativeCrashHandler instance = NativeCrashHandler.getInstance();
            if (instance != null) {
                instance.a(str, str2);
            }
            try {
                this.R.put(str, str2);
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    public synchronized int K() {
        return this.R.size();
    }

    public synchronized Set<String> L() {
        return this.R.keySet();
    }

    public synchronized void b(String str, String str2) {
        if (ag.b(str) || ag.b(str2)) {
            z.d("server key&value should not be empty %s %s", "" + str, "" + str2);
        } else {
            this.S.put(str, str2);
        }
    }

    public synchronized Map<String, String> M() {
        Map<String, String> map;
        if (this.S.size() <= 0) {
            map = null;
        } else {
            map = new HashMap(this.S);
        }
        return map;
    }

    public synchronized void a(int i) {
        if (this.P != i) {
            this.P = i;
            z.a("user scene tag %d changed to tag %d", Integer.valueOf(r0), Integer.valueOf(this.P));
        }
    }

    public synchronized int N() {
        return this.P;
    }

    public synchronized int O() {
        return this.Q;
    }

    public synchronized boolean P() {
        return AppInfo.g(this.a);
    }
}
