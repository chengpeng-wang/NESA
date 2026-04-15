package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.picksinit.ErrorInfo;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.b;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.z;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

/* compiled from: BUGLY */
public class e implements b, UncaughtExceptionHandler {
    protected static UncaughtExceptionHandler f;
    private static boolean j;
    private static int k;
    protected final Context a;
    protected final b b;
    protected final c c;
    protected final a d;
    protected UncaughtExceptionHandler e;
    protected CrashHandleCallback g;
    protected boolean h = false;
    private boolean i;

    public e(Context context, b bVar, c cVar, a aVar, CrashHandleCallback crashHandleCallback) {
        this.a = context;
        this.b = bVar;
        this.c = cVar;
        this.d = aVar;
        this.g = crashHandleCallback;
    }

    public synchronized void a() {
        if (k >= 10) {
            z.a("java crash handler over %d, no need set.", Integer.valueOf(10));
        } else {
            UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (!(defaultUncaughtExceptionHandler == null || getClass().getName().equals(defaultUncaughtExceptionHandler.getClass().getName()))) {
                if ("com.android.internal.os.RuntimeInit$UncaughtHandler".equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                    z.a("backup system java handler: %s", defaultUncaughtExceptionHandler.toString());
                    f = defaultUncaughtExceptionHandler;
                    this.e = defaultUncaughtExceptionHandler;
                } else {
                    z.a("backup java handler: %s", defaultUncaughtExceptionHandler.toString());
                    this.e = defaultUncaughtExceptionHandler;
                }
                e eVar = new e(this.a, this.b, this.c, this.d, this.g);
                eVar.a(defaultUncaughtExceptionHandler);
                Thread.setDefaultUncaughtExceptionHandler(eVar);
                k++;
                z.a("registered java monitor: %s", toString());
            }
        }
    }

    public synchronized void b() {
        if (Thread.getDefaultUncaughtExceptionHandler() == this) {
            Thread.setDefaultUncaughtExceptionHandler(this.e);
            z.a("close java monitor!", new Object[0]);
            k--;
        }
    }

    public synchronized boolean c() {
        return Thread.getDefaultUncaughtExceptionHandler() == this;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void a(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.e = uncaughtExceptionHandler;
    }

    /* access modifiers changed from: protected */
    public void a(Thread thread, Throwable th) {
        z.e("current process die", new Object[0]);
        ag.c();
    }

    /* access modifiers changed from: protected */
    public void a(Thread thread, Throwable th, boolean z) {
        a a = a.a(this.a);
        String str;
        Object[] objArr;
        if (z) {
            z.e("#++++++++++Simple Record By Bugly++++++++++#", new Object[0]);
            z.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
            z.e("# PKG NAME: %s", a.f());
            z.e("# APP VER: %s", a.e());
            z.e("# CRASH TYPE: JAVA_CRASH", new Object[0]);
            z.e("# CRASH TIME: %s", ag.a());
            z.e("# CRASH PROCESS: %s", a.E());
            str = "# CRASH THREAD: %s";
            objArr = new Object[1];
            objArr[0] = thread == null ? Const.CONNECTION_TYPE_UNKNOWN : thread.getName();
            z.e(str, objArr);
            z.e("# CRASH STACK: ", new Object[0]);
            z.b(th);
            z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
            return;
        }
        z.d("#++++++++++Simple Record By Bugly++++++++++#", new Object[0]);
        z.d("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        z.d("# PKG NAME: %s", a.f());
        z.d("# APP VER: %s", a.e());
        z.d("# CRASH TYPE: JAVA_CATCH", new Object[0]);
        z.d("# CRASH TIME: %s", ag.a());
        z.d("# CRASH PROCESS: %s", a.E());
        str = "# CRASH THREAD: %s";
        objArr = new Object[1];
        objArr[0] = thread == null ? Const.CONNECTION_TYPE_UNKNOWN : thread.getName();
        z.d(str, objArr);
        z.d("# CRASH STACK: ", new Object[0]);
        z.a(th);
        z.d("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void a(CrashDetailBean crashDetailBean) {
        a a = a.a(this.a);
        String str;
        String str2;
        Object[] objArr;
        if (crashDetailBean.b == 0) {
            z.e("#++++++++++Detail Record By Bugly++++++++++#", new Object[0]);
            z.e("# You can go to Bugly(http:\\\\bugly.qq.com) to see more detail of this Report!", new Object[0]);
            z.e("# REPORT ID: %s", crashDetailBean.c);
            z.e("# PKG NAME: %s", a.f());
            z.e("# APP VER: %s", a.e());
            switch (crashDetailBean.b) {
                case 0:
                    str = "JAVA_CRASH";
                    break;
                case 2:
                    str = "JAVA_CATCHED";
                    break;
                default:
                    str = Const.CONNECTION_TYPE_UNKNOWN;
                    break;
            }
            z.e("# LAUNCH TIME:%s", ag.a(new Date(crashDetailBean.M)));
            z.e("# CRASH TYPE: %s", str);
            z.e("# CRASH TIME: %s", ag.a(new Date(crashDetailBean.r)));
            z.e("# CRASH PROCESS: %s", a.E());
            z.e("# CRASH THREAD: %s", crashDetailBean.A);
            str2 = "# CRASH DEVICE: %s %s";
            objArr = new Object[2];
            objArr[0] = a.k();
            objArr[1] = a.C().booleanValue() ? "ROOTED" : "UNROOT";
            z.e(str2, objArr);
            z.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
            z.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
            z.e("# EXCEPTION TYPE: %s", crashDetailBean.n);
            z.e("# EXCEPTION MSG: %s", crashDetailBean.o);
            z.e("# EXCEPTION STACK:\n %s", crashDetailBean.q);
            z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
            return;
        }
        z.d("#++++++++++Detail Record By Bugly++++++++++#", new Object[0]);
        z.d("# You can go to Bugly(http:\\\\bugly.qq.com) to see more detail of this Report!", new Object[0]);
        z.d("# REPORT ID: %s", crashDetailBean.c);
        z.d("# PKG NAME: %s", a.f());
        z.d("# APP VER: %s", a.e());
        switch (crashDetailBean.b) {
            case 0:
                str = "JAVA_CRASH";
                break;
            case 2:
                str = "JAVA_CATCHED";
                break;
            default:
                str = Const.CONNECTION_TYPE_UNKNOWN;
                break;
        }
        z.d("# LAUNCH TIME:%s", ag.a(new Date(crashDetailBean.M)));
        z.d("# CRASH TYPE: %s", str);
        z.d("# CRASH TIME: %s", ag.a(new Date(crashDetailBean.r)));
        z.d("# CRASH PROCESS: %s", a.E());
        z.d("# CRASH THREAD: %s", crashDetailBean.A);
        str2 = "# CRASH DEVICE: %s %s";
        objArr = new Object[2];
        objArr[0] = a.k();
        objArr[1] = a.C().booleanValue() ? "ROOTED" : "UNROOT";
        z.d(str2, objArr);
        z.d("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
        z.d("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
        z.d("# EXCEPTION TYPE: %s", crashDetailBean.n);
        z.d("# EXCEPTION MSG: %s", crashDetailBean.o);
        z.d("# EXCEPTION STACK:\n %s", crashDetailBean.q);
        z.d("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    public CrashDetailBean b(Thread thread, Throwable th, boolean z) {
        int i;
        boolean l = c.a().l();
        String str = (l && z) ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (l && z) {
            z.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        z.e("3", new Object[0]);
        crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.i();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.k();
        crashDetailBean.E = this.d.u();
        crashDetailBean.F = this.d.t();
        crashDetailBean.G = this.d.v();
        crashDetailBean.w = d.a(this.a, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        String str2 = "user log size:%d";
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(crashDetailBean.x == null ? 0 : crashDetailBean.x.length);
        z.a(str2, objArr);
        if (z) {
            i = 0;
        } else {
            i = 2;
        }
        crashDetailBean.b = i;
        crashDetailBean.e = this.d.n();
        crashDetailBean.f = this.d.e();
        crashDetailBean.g = this.d.A();
        crashDetailBean.m = this.d.m();
        Throwable a = d.a(th);
        if (a == null) {
            z.d("throw null,return", new Object[0]);
            return null;
        }
        String name = th.getClass().getName();
        String a2 = d.a(th, 1000);
        if (a2 == null) {
            a2 = "";
        }
        String str3 = "stack frame :%d, has cause %b";
        Object[] objArr2 = new Object[2];
        objArr2[0] = Integer.valueOf(th.getStackTrace().length);
        objArr2[1] = Boolean.valueOf(th.getCause() != null);
        z.e(str3, objArr2);
        String str4 = "";
        if (th.getStackTrace().length > 0) {
            str4 = th.getStackTrace()[0].toString();
        }
        if (a != th) {
            crashDetailBean.n = a.getClass().getName();
            crashDetailBean.o = d.a(a, 1000);
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = a.getStackTrace()[0].toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(name).append(":").append(a2).append(MASTNativeAdConstants.NEWLINE);
            stringBuilder.append(str4);
            stringBuilder.append("\n......");
            stringBuilder.append("\ncause by:\n");
            stringBuilder.append(crashDetailBean.n).append(":").append(crashDetailBean.o).append(MASTNativeAdConstants.NEWLINE);
            stringBuilder.append(d.b(a, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT));
            crashDetailBean.q = stringBuilder.toString();
        } else {
            crashDetailBean.n = name;
            crashDetailBean.o = a2 + "" + str;
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = str4;
            crashDetailBean.q = d.b(th, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        }
        crashDetailBean.r = new Date().getTime();
        crashDetailBean.u = ag.a(crashDetailBean.q.getBytes());
        try {
            crashDetailBean.y = d.a((int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT, false);
            crashDetailBean.z = this.d.E();
            crashDetailBean.A = thread.getName();
            crashDetailBean.H = this.d.D();
            crashDetailBean.h = this.d.z();
            crashDetailBean.M = this.d.b();
            crashDetailBean.P = this.d.N();
            crashDetailBean.Q = this.d.O();
            crashDetailBean.R = this.d.J();
            crashDetailBean.S = this.d.M();
        } catch (Throwable th2) {
            z.e("handle crash error %s", th2.toString());
        }
        this.b.a(crashDetailBean, this.g);
        crashDetailBean.x = ab.a(z);
        return crashDetailBean;
    }

    public void c(Thread thread, Throwable th, boolean z) {
        if (z) {
            z.e("Java Crash Happen cause by %s(%d)", thread.getName(), Long.valueOf(thread.getId()));
            if (j) {
                z.a("is handled this exception", new Object[0]);
                if (this.i) {
                    z.d("twice in uncaughtException", new Object[0]);
                    if (f != null) {
                        z.a("call system handler", new Object[0]);
                        f.uncaughtException(thread, th);
                    } else {
                        a(thread, th);
                    }
                } else {
                    z.a("twice handled this exception, call next default", new Object[0]);
                    this.e.uncaughtException(thread, th);
                }
                j = true;
                this.i = true;
            }
        } else {
            z.e("Java Catch Happen", new Object[0]);
        }
        try {
            if (!this.c.b()) {
                z.e("waiting for remote sync", new Object[0]);
                int i = 0;
                while (!this.c.b()) {
                    ag.a(500);
                    i += ErrorInfo.ERROR_CODE_PARAMS;
                    if (i >= 5000) {
                        break;
                    }
                }
            }
            if (!this.c.b()) {
                z.d("no remote but still store!", new Object[0]);
            }
            if (this.c.d().d || !this.c.b()) {
                CrashDetailBean b = b(thread, th, z);
                if (b == null) {
                    z.e("pkg crash datas fail!", new Object[0]);
                    this.c.a("packageFail", false);
                    if (!z) {
                        z.e("not to shut down return", new Object[0]);
                        return;
                    } else if (this.e != null && b(this.e)) {
                        z.e("sys default last handle start!", new Object[0]);
                        this.e.uncaughtException(thread, th);
                        z.e("sys default last handle end!", new Object[0]);
                        return;
                    } else if (f != null) {
                        z.e("system handle start!", new Object[0]);
                        f.uncaughtException(thread, th);
                        z.e("system handle end!", new Object[0]);
                        return;
                    } else {
                        z.e("crashreport last handle start!", new Object[0]);
                        a(thread, th);
                        z.e("crashreport last handle end!", new Object[0]);
                        return;
                    }
                }
                a(b);
                if (!this.b.a(b)) {
                    this.b.a(b, 5000);
                }
                if (!z) {
                    z.e("not to shut down return", new Object[0]);
                    return;
                } else if (this.e != null && b(this.e)) {
                    z.e("sys default last handle start!", new Object[0]);
                    this.e.uncaughtException(thread, th);
                    z.e("sys default last handle end!", new Object[0]);
                    return;
                } else if (f != null) {
                    z.e("system handle start!", new Object[0]);
                    f.uncaughtException(thread, th);
                    z.e("system handle end!", new Object[0]);
                    return;
                } else {
                    z.e("crashreport last handle start!", new Object[0]);
                    a(thread, th);
                    z.e("crashreport last handle end!", new Object[0]);
                    return;
                }
            }
            z.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
            a(thread, th, z);
            this.c.a("remoteClose", false);
            if (!z) {
                z.e("not to shut down return", new Object[0]);
            } else if (this.e != null && b(this.e)) {
                z.e("sys default last handle start!", new Object[0]);
                this.e.uncaughtException(thread, th);
                z.e("sys default last handle end!", new Object[0]);
            } else if (f != null) {
                z.e("system handle start!", new Object[0]);
                f.uncaughtException(thread, th);
                z.e("system handle end!", new Object[0]);
            } else {
                z.e("crashreport last handle start!", new Object[0]);
                a(thread, th);
                z.e("crashreport last handle end!", new Object[0]);
            }
        } catch (Throwable th2) {
            if (!z) {
                z.e("not to shut down return", new Object[0]);
            } else if (this.e != null && b(this.e)) {
                z.e("sys default last handle start!", new Object[0]);
                this.e.uncaughtException(thread, th);
                z.e("sys default last handle end!", new Object[0]);
            } else if (f != null) {
                z.e("system handle start!", new Object[0]);
                f.uncaughtException(thread, th);
                z.e("system handle end!", new Object[0]);
            } else {
                z.e("crashreport last handle start!", new Object[0]);
                a(thread, th);
                z.e("crashreport last handle end!", new Object[0]);
            }
        }
    }

    private boolean b(UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler == null) {
            return true;
        }
        String name = uncaughtExceptionHandler.getClass().getName();
        String str = "uncaughtException";
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && str.equals(methodName)) {
                return false;
            }
        }
        return true;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        c(thread, ex, true);
    }

    public synchronized void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.d != c()) {
                z.a("java changed to %b", Boolean.valueOf(strategyBean.d));
                if (strategyBean.d) {
                    a();
                } else {
                    b();
                }
            }
        }
    }
}
