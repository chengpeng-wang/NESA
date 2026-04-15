package com.tencent.bugly.proguard;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.picksinit.ErrorInfo;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.b;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.d;
import java.util.Date;

/* compiled from: BUGLY */
public class ah implements b {
    protected final Context a;
    protected final com.tencent.bugly.crashreport.crash.b b;
    protected final c c;
    protected final a d;
    protected CrashHandleCallback e;
    public boolean f = true;

    public ah(Context context, com.tencent.bugly.crashreport.crash.b bVar, c cVar, a aVar, CrashHandleCallback crashHandleCallback) {
        this.a = context;
        this.b = bVar;
        this.c = cVar;
        this.d = aVar;
        this.e = crashHandleCallback;
    }

    /* access modifiers changed from: protected */
    public void a(Thread thread, String str, String str2, String str3) {
        a a = a.a(this.a);
        z.e("#++++++++++Simple Record By Bugly++++++++++#", new Object[0]);
        z.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        z.e("# PKG NAME: %s", a.f());
        z.e("# APP VER: %s", a.e());
        z.e("# CRASH TYPE: Cocos2dx", new Object[0]);
        z.e("# CRASH TIME: %s", ag.a());
        z.e("# CRASH PROCESS: %s", a.E());
        String str4 = "# CRASH THREAD: %s";
        Object[] objArr = new Object[1];
        objArr[0] = thread == null ? Const.CONNECTION_TYPE_UNKNOWN : thread.getName();
        z.e(str4, objArr);
        z.e("# CRASH STACK: ", new Object[0]);
        z.e(str + MASTNativeAdConstants.NEWLINE + str2 + MASTNativeAdConstants.NEWLINE + str3, new Object[0]);
        z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void a(CrashDetailBean crashDetailBean) {
        String str;
        a a = a.a(this.a);
        z.e("#++++++++++Detail Record By Bugly++++++++++#", new Object[0]);
        z.e("# You can go to Bugly(http:\\\\bugly.qq.com) to see more detail of this Report!", new Object[0]);
        z.e("# REPORT ID: %s", crashDetailBean.c);
        z.e("# PKG NAME: %s", a.f());
        z.e("# APP VER: %s", a.e());
        switch (crashDetailBean.b) {
            case 5:
            case 6:
                str = "COCOS2DX_CRASH";
                break;
            default:
                str = Const.CONNECTION_TYPE_UNKNOWN;
                break;
        }
        z.e("# LAUNCH TIME:%s", ag.a(new Date(crashDetailBean.M)));
        z.e("# CRASH TYPE: %s", str);
        z.e("# CRASH TIME: %s", ag.a(new Date(crashDetailBean.r)));
        z.e("# CRASH PROCESS: %s", crashDetailBean.z);
        z.e("# CRASH THREAD: %s", crashDetailBean.A);
        String str2 = "# CRASH DEVICE: %s %s";
        Object[] objArr = new Object[2];
        objArr[0] = a.k();
        objArr[1] = a.C().booleanValue() ? "ROOTED" : "UNROOT";
        z.e(str2, objArr);
        z.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
        z.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
        z.e("# EXCEPTION TYPE: %s", crashDetailBean.n);
        z.e("# EXCEPTION MSG: %s", crashDetailBean.o);
        z.e("# EXCEPTION STACK:\n %s", crashDetailBean.q);
        z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    public CrashDetailBean a(Thread thread, int i, String str, String str2, String str3) {
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.i();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.k();
        crashDetailBean.E = this.d.u();
        crashDetailBean.F = this.d.t();
        crashDetailBean.G = this.d.v();
        crashDetailBean.w = d.a(this.a, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        crashDetailBean.x = ab.a(false);
        crashDetailBean.b = i;
        crashDetailBean.e = this.d.n();
        crashDetailBean.f = this.d.e();
        crashDetailBean.g = this.d.A();
        crashDetailBean.m = this.d.m();
        crashDetailBean.n = "" + str;
        crashDetailBean.o = "" + str2;
        String str4 = "";
        if (str3 != null) {
            String[] split = str3.split(MASTNativeAdConstants.NEWLINE);
            if (split != null && split.length > 0) {
                str4 = split[0];
            }
        }
        crashDetailBean.p = str4;
        crashDetailBean.q = str3;
        crashDetailBean.r = new Date().getTime();
        crashDetailBean.u = ag.a(crashDetailBean.q.getBytes());
        crashDetailBean.y = d.a((int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT, false);
        crashDetailBean.z = this.d.E();
        crashDetailBean.A = thread.getName();
        crashDetailBean.H = this.d.D();
        crashDetailBean.h = this.d.z();
        crashDetailBean.M = this.d.b();
        crashDetailBean.N = AppInfo.d(this.a).booleanValue();
        crashDetailBean.P = this.d.N();
        crashDetailBean.Q = this.d.O();
        crashDetailBean.R = this.d.J();
        crashDetailBean.S = this.d.M();
        this.b.a(crashDetailBean, this.e);
        return crashDetailBean;
    }

    public void b(Thread thread, int i, String str, String str2, String str3) {
        z.e("Cocos2d-x Crash Happen", new Object[0]);
        try {
            this.c.d();
            if (!this.c.b()) {
                z.e("waiting for remote sync", new Object[0]);
                int i2 = 0;
                while (!this.c.b()) {
                    ag.a(500);
                    i2 += ErrorInfo.ERROR_CODE_PARAMS;
                    if (i2 >= 5000) {
                        break;
                    }
                }
            }
            if (!this.c.b()) {
                z.d("no remote but still store!", new Object[0]);
            }
            StrategyBean d = this.c.d();
            if (!d.d && this.c.b()) {
                z.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                a(thread, str, str2, str3);
                this.c.a("remoteClose", false);
                z.e("handle end", new Object[0]);
            } else if (d.h) {
                CrashDetailBean a = a(thread, i, str, str2, str3);
                if (a == null) {
                    z.e("pkg crash datas fail!", new Object[0]);
                    this.c.a("packageFail", false);
                    z.e("handle end", new Object[0]);
                    return;
                }
                a(a);
                if (!this.b.a(a)) {
                    this.b.a(a, 5000);
                }
                z.e("handle end", new Object[0]);
            } else {
                z.e("cocos report is disabled.", new Object[0]);
                z.e("handle end", new Object[0]);
            }
        } catch (Throwable th) {
            z.e("handle end", new Object[0]);
        }
    }

    public void a(StrategyBean strategyBean) {
        this.f = strategyBean.h;
    }
}
