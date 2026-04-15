package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.crash.anr.b;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.ah;
import com.tencent.bugly.proguard.ai;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.Map;

/* compiled from: BUGLY */
public class c {
    private static c a;
    /* access modifiers changed from: private|final */
    public final e b;
    private final NativeCrashHandler c;
    private final com.tencent.bugly.crashreport.common.strategy.c d;
    /* access modifiers changed from: private|final */
    public final ak e;
    /* access modifiers changed from: private|final */
    public final ah f;
    /* access modifiers changed from: private|final */
    public final ai g;
    private final y h;
    private final Context i;
    private final b j;

    protected c(Context context, q qVar, com.tencent.bugly.crashreport.common.strategy.c cVar, w wVar, a aVar, y yVar, boolean z, CrashHandleCallback crashHandleCallback) {
        Context a = ag.a(context);
        this.i = a;
        this.d = cVar;
        this.h = yVar;
        b bVar = new b(a, wVar, qVar, cVar, crashHandleCallback);
        this.b = new e(a, bVar, cVar, aVar, crashHandleCallback);
        this.e = new ak(a, bVar, cVar, aVar, crashHandleCallback);
        this.c = NativeCrashHandler.a(a, aVar, bVar, cVar, crashHandleCallback, yVar, z);
        this.j = new b(a, cVar, aVar, yVar, qVar, bVar, crashHandleCallback);
        this.f = new ah(a, bVar, cVar, aVar, crashHandleCallback);
        this.g = new ai(a, bVar, cVar, aVar, crashHandleCallback);
        cVar.a(this.b);
        cVar.a(this.c);
        cVar.a(this.j);
        cVar.a(this.f);
    }

    public static synchronized c a(Context context, q qVar, com.tencent.bugly.crashreport.common.strategy.c cVar, w wVar, a aVar, y yVar, boolean z, CrashHandleCallback crashHandleCallback) {
        c cVar2;
        synchronized (c.class) {
            if (a == null) {
                a = new c(context, qVar, cVar, wVar, aVar, yVar, z, crashHandleCallback);
            }
            cVar2 = a;
        }
        return cVar2;
    }

    public static c a() {
        return a;
    }

    public boolean b() {
        return this.d.h();
    }

    public void c() {
        this.b.b();
    }

    public void d() {
        this.b.a();
    }

    public void e() {
        this.c.c(false);
    }

    public void f() {
        this.c.c(true);
    }

    public void g() {
        this.j.b(true);
    }

    public void h() {
        this.j.b(false);
    }

    public synchronized void i() {
        c();
        e();
        h();
    }

    public synchronized void j() {
        this.c.c();
    }

    public synchronized void k() {
        this.j.g();
    }

    public boolean l() {
        return this.j.a();
    }

    public void a(Thread thread, String str, String str2, String str3) {
        final Thread thread2 = thread;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        this.h.b(new Runnable() {
            public void run() {
                try {
                    c.this.e.c(thread2, str4, str5, str6);
                } catch (Throwable th) {
                    if (!z.b(th)) {
                        th.printStackTrace();
                    }
                    z.e("u3d crash error %s %s %s", str4, str5, str6);
                }
            }
        });
    }

    public void a(Thread thread, int i, String str, String str2, String str3) {
        final Thread thread2 = thread;
        final int i2 = i;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        this.h.b(new Runnable() {
            public void run() {
                try {
                    c.this.f.b(thread2, i2, str4, str5, str6);
                } catch (Throwable th) {
                    if (!z.b(th)) {
                        th.printStackTrace();
                    }
                    z.e("cocos2d-x crash error %s %s %s", str4, str5, str6);
                }
            }
        });
    }

    public void a(Thread thread, String str, String str2, String str3, Map<String, String> map) {
        final Thread thread2 = thread;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final Map<String, String> map2 = map;
        this.h.b(new Runnable() {
            public void run() {
                try {
                    c.this.g.b(thread2, str4, str5, str6, map2);
                } catch (Throwable th) {
                    if (!z.b(th)) {
                        th.printStackTrace();
                    }
                    z.e("H5 crash error %s %s %s", str4, str5, str6);
                }
            }
        });
    }

    public void a(final Thread thread, final Throwable th, final boolean z) {
        this.h.b(new Runnable() {
            public void run() {
                try {
                    z.c("post a throwable %b", Boolean.valueOf(z));
                    c.this.b.c(thread, th, false);
                } catch (Throwable th) {
                    if (!z.b(th)) {
                        th.printStackTrace();
                    }
                    z.e("java catch error: %s", th.toString());
                }
            }
        });
    }
}
