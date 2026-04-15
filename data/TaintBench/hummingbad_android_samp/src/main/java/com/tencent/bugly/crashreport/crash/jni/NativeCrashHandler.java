package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.b;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.File;

/* compiled from: BUGLY */
public class NativeCrashHandler implements b {
    private static NativeCrashHandler a;
    private static boolean l = false;
    private final int b = 1;
    /* access modifiers changed from: private|final */
    public final Context c;
    private final a d;
    private final y e;
    /* access modifiers changed from: private|final */
    public final NativeExceptionHandler f;
    /* access modifiers changed from: private|final */
    public final String g;
    private final boolean h;
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;

    protected static native String removeNativeKeyValue(String str);

    public native boolean appendNativeLog(String str, String str2, String str3);

    public native boolean appendWholeNativeLog(String str);

    public native String getNativeKeyValueList();

    public native String getNativeLog();

    public native boolean putNativeKeyValue(String str, String str2);

    public native String regist(String str, boolean z, int i);

    public native void testCrash();

    public native String unregist();

    private static void a(String str) {
        z.c("Check extra jni for Bugly NDK v%s", str);
        String replace = "2.1.1".replace(".", "");
        String replace2 = str.replace(".", "");
        if (replace2.length() == 2) {
            replace2 = replace2 + "0";
        } else if (replace2.length() == 1) {
            replace2 = replace2 + "00";
        }
        try {
            if (Integer.parseInt(replace2) >= Integer.parseInt(replace)) {
                l = true;
            }
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
        }
        if (l) {
            z.a("Extra bugly jni can be accessed.", new Object[0]);
        } else {
            z.d("Extra bugly jni can not be accessed.", new Object[0]);
        }
    }

    protected NativeCrashHandler(Context context, a comInfo, com.tencent.bugly.crashreport.crash.b crashHandler, c strategyManager, CrashHandleCallback crashHandleCallback, y asyncHandler, boolean isDebug) {
        String absolutePath;
        this.c = ag.a(context);
        try {
            absolutePath = context.getDir("bugly", 0).getAbsolutePath();
        } catch (Throwable th) {
            absolutePath = "/data/data/" + a.a(context).f() + "/app_bugly";
        }
        this.g = absolutePath;
        this.d = comInfo;
        this.f = new a(context, comInfo, crashHandler, strategyManager, crashHandleCallback, this.g);
        this.e = asyncHandler;
        this.h = isDebug;
    }

    public static synchronized NativeCrashHandler a(Context context, a aVar, com.tencent.bugly.crashreport.crash.b bVar, c cVar, CrashHandleCallback crashHandleCallback, y yVar, boolean z) {
        NativeCrashHandler nativeCrashHandler;
        synchronized (NativeCrashHandler.class) {
            if (a == null) {
                a = new NativeCrashHandler(context, aVar, bVar, cVar, crashHandleCallback, yVar, z);
            }
            nativeCrashHandler = a;
        }
        return nativeCrashHandler;
    }

    public static synchronized NativeCrashHandler getInstance() {
        NativeCrashHandler nativeCrashHandler;
        synchronized (NativeCrashHandler.class) {
            nativeCrashHandler = a;
        }
        return nativeCrashHandler;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void a(boolean z) {
        if (this.j) {
            z.d("native already registed!", new Object[0]);
        } else {
            try {
                String regist = regist(this.g, z, 1);
                if (regist != null) {
                    z.a("Native Crash Report enable!", new Object[0]);
                    a(regist);
                    this.d.i(regist);
                    this.j = true;
                }
            } catch (Throwable th) {
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
            this.i = false;
        }
    }

    public synchronized void a() {
        if (this.i) {
            a(this.h);
        } else {
            if (this.d.F() == null) {
                try {
                    System.loadLibrary("Bugly");
                    this.i = true;
                    this.e.b(new Runnable() {
                        public void run() {
                            CrashDetailBean a = b.a(NativeCrashHandler.this.c, NativeCrashHandler.this.g, NativeCrashHandler.this.f);
                            if (a != null) {
                                q.a(NativeCrashHandler.this.c).b(a);
                                z.a("get cr from record!", new Object[0]);
                                b.c(NativeCrashHandler.this.g);
                            }
                            NativeCrashHandler.this.d();
                        }
                    });
                } catch (Throwable th) {
                    String str = "Load \"libBugly.so\" fail. Native Exception will not upload to bugly.";
                    String str2 = "If you added the so file already, try update to latest version. Download at:\nhttp://bugly.qq.com/whitebook";
                    z.d(str, new Object[0]);
                    z.b(str, new Object[0]);
                    if (!this.d.i().equals("")) {
                        z.d(str2, new Object[0]);
                        z.b(str2, new Object[0]);
                    }
                }
            } else {
                try {
                    System.loadLibrary(this.d.F());
                    this.i = true;
                } catch (Throwable th2) {
                    z.e("libBugly.so can't be loaded from %s ,  Native Exception will not upload to bugly!", this.d.F());
                }
            }
            if (this.i) {
                a(this.h);
            }
        }
        return;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void b() {
        if (this.j) {
            try {
                if (unregist() != null) {
                    z.a("Native Crash Report close!", new Object[0]);
                    this.j = false;
                }
            } catch (Throwable th) {
                if (!z.b(th)) {
                    th.printStackTrace();
                }
            }
            this.i = false;
        } else {
            z.d("native already unregisted!", new Object[0]);
        }
    }

    public void c() {
        if (this.i) {
            testCrash();
        } else {
            z.d("libBugly.so has not been load! so fail!", new Object[0]);
        }
    }

    public boolean a(String str, String str2, String str3) {
        boolean z = false;
        if (!this.i || !l || str == null || str2 == null || str3 == null) {
            return z;
        }
        try {
            return appendNativeLog(str, str2, str3);
        } catch (UnsatisfiedLinkError e) {
            l = z;
            return z;
        } catch (Throwable th) {
            if (z.a(th)) {
                return z;
            }
            th.printStackTrace();
            return z;
        }
    }

    public boolean a(String str, String str2) {
        boolean z = false;
        if (!this.i || !l || str == null || str2 == null) {
            return z;
        }
        try {
            return putNativeKeyValue(str, str2);
        } catch (UnsatisfiedLinkError e) {
            l = z;
            return z;
        } catch (Throwable th) {
            if (z.a(th)) {
                return z;
            }
            th.printStackTrace();
            return z;
        }
    }

    public NativeExceptionHandler getNativeExceptionHandler() {
        return this.f;
    }

    /* access modifiers changed from: protected */
    public void d() {
        long b = ag.b() - 604800000;
        File file = new File(this.g);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length != 0) {
                String str = "tomb_";
                String str2 = ".txt";
                int length = str.length();
                int i = 0;
                for (File file2 : listFiles) {
                    String name = file2.getName();
                    if (name.startsWith(str)) {
                        try {
                            int indexOf = name.indexOf(str2);
                            if (indexOf > 0 && Long.parseLong(name.substring(length, indexOf)) >= b) {
                            }
                        } catch (Throwable th) {
                            z.e("tomb format error delete %s", name);
                        }
                        if (file2.delete()) {
                            i++;
                        }
                    }
                }
                z.c("clean tombs %d", Integer.valueOf(i));
            }
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void b(boolean z) {
        if (z) {
            a();
        } else {
            b();
        }
    }

    public synchronized boolean e() {
        return this.k;
    }

    private synchronized void d(boolean z) {
        if (this.k != z) {
            z.a("user change anr %b", Boolean.valueOf(z));
            this.k = z;
        }
    }

    public void c(boolean z) {
        d(z);
        boolean z2 = c.a().d().d && e();
        if (z2 != this.j) {
            z.a("native changed to %b", Boolean.valueOf(z2));
            b(z2);
        }
    }

    public synchronized void a(StrategyBean strategyBean) {
        boolean z = true;
        synchronized (this) {
            if (strategyBean != null) {
                if (strategyBean.d != this.j) {
                    z.d("server native changed to %b", Boolean.valueOf(strategyBean.d));
                }
            }
            if (!(c.a().d().d && this.k)) {
                z = false;
            }
            if (z != this.j) {
                z.a("native changed to %b", Boolean.valueOf(z));
                b(z);
            }
        }
    }
}
