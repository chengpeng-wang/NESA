package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.picksinit.ErrorInfo;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.b;
import com.tencent.bugly.crashreport.crash.d;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.z;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public class a implements NativeExceptionHandler {
    private final Context a;
    private final b b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final c d;
    private final String e;
    private CrashHandleCallback f;

    public a(Context context, com.tencent.bugly.crashreport.common.info.a aVar, b bVar, c cVar, CrashHandleCallback crashHandleCallback, String str) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = cVar;
        this.f = crashHandleCallback;
        this.e = str;
    }

    /* access modifiers changed from: protected */
    public void a(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        com.tencent.bugly.crashreport.common.info.a a = com.tencent.bugly.crashreport.common.info.a.a(this.a);
        z.e("#++++++++++Simple Record By Bugly++++++++++#", new Object[0]);
        z.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
        z.e("# CRASH REPORT CREATED BY NATIVE VERSION %s", str7);
        z.e("# PKG NAME: %s", a.f());
        z.e("# APP VER: %s", a.e());
        z.e("# CRASH TYPE: NATIVE_CRASH", new Object[0]);
        z.e("# CRASH TIME: %s", ag.a());
        z.e("# CRASH PROCESS: %s", a.E());
        z.e("# CRASH THREAD: %s", str);
        z.e("# CRASH TYPE: %s", str2);
        z.e("# CRASH ADDR: %s", str3);
        z.e("# CRASH STACK:", new Object[0]);
        z.e(str4, new Object[0]);
        z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void a(CrashDetailBean crashDetailBean) {
        com.tencent.bugly.crashreport.common.info.a a = com.tencent.bugly.crashreport.common.info.a.a(this.a);
        z.e("#++++++++++Detail Record By Bugly++++++++++#", new Object[0]);
        z.e("# You can go to Bugly(http:\\\\bugly.qq.com) to see more detail of this Report!", new Object[0]);
        z.e("# CRASH REPORT CREATED BY NATIVE VERSION %s", crashDetailBean.L);
        z.e("# REPORT ID: %s", crashDetailBean.c);
        z.e("# PKG NAME: %s", a.f());
        z.e("# APP VER: %s", a.e());
        z.e("# LAUNCH TIME:%s", ag.a(new Date(crashDetailBean.M)));
        z.e("# CRASH TYPE: %s", "NATIVE_CRASH");
        z.e("# CRASH TIME: %s", ag.a(new Date(crashDetailBean.r)));
        z.e("# CRASH PROCESS: %s", a.E());
        z.e("# CRASH THREAD: %s", crashDetailBean.A);
        String str = "# CRASH DEVICE: %s %s";
        Object[] objArr = new Object[2];
        objArr[0] = a.k();
        objArr[1] = a.C().booleanValue() ? "ROOTED" : "UNROOT";
        z.e(str, objArr);
        z.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
        z.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
        z.e("# EXCEPTION FIRED BY %s %s", crashDetailBean.K, crashDetailBean.J);
        z.e("# EXCEPTION TYPE: %s", crashDetailBean.n);
        z.e("# EXCEPTION MSG: %s", crashDetailBean.o);
        z.e("# EXCEPTION STACK:\n %s", crashDetailBean.q);
        z.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
    }

    public CrashDetailBean packageCrashDatas(String processName, String threadName, long crashTime, String crashType, String crashAddr, String crashStack, String sendingType, String sendingProcessName, String coreDumpFile, String nativeVersion, byte[] userLog, Map<String, String> userKeyValue, boolean isHappenNow) {
        boolean l = com.tencent.bugly.crashreport.crash.c.a().l();
        String str = l ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (l) {
            z.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.b = 1;
        crashDetailBean.e = this.c.n();
        crashDetailBean.f = this.c.e();
        crashDetailBean.g = this.c.A();
        crashDetailBean.m = this.c.m();
        crashDetailBean.n = crashType;
        crashDetailBean.o = str;
        crashDetailBean.p = crashAddr;
        crashDetailBean.q = crashStack;
        crashDetailBean.r = crashTime;
        crashDetailBean.u = ag.a(crashDetailBean.q.getBytes());
        crashDetailBean.z = processName;
        crashDetailBean.A = threadName;
        crashDetailBean.H = this.c.D();
        crashDetailBean.h = this.c.z();
        crashDetailBean.v = coreDumpFile;
        crashDetailBean.J = sendingProcessName;
        crashDetailBean.K = sendingType;
        crashDetailBean.L = nativeVersion;
        crashDetailBean.E = this.c.u();
        crashDetailBean.F = this.c.t();
        crashDetailBean.G = this.c.v();
        if (isHappenNow) {
            crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.i();
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.k();
            crashDetailBean.w = d.a(this.a, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            crashDetailBean.M = this.c.b();
            crashDetailBean.P = this.c.N();
            crashDetailBean.Q = this.c.O();
            crashDetailBean.R = this.c.J();
            crashDetailBean.S = this.c.M();
            crashDetailBean.y = d.a((int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT, false);
            if (processName == null) {
                crashDetailBean.z = this.c.E();
            }
            this.b.a(crashDetailBean, this.f);
            crashDetailBean.x = ab.a(true);
        } else {
            crashDetailBean.B = -1;
            crashDetailBean.C = -1;
            crashDetailBean.D = -1;
            crashDetailBean.w = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.";
            crashDetailBean.M = -1;
            crashDetailBean.P = -1;
            crashDetailBean.Q = -1;
            crashDetailBean.R = userKeyValue;
            crashDetailBean.S = null;
            crashDetailBean.y = null;
            if (processName == null) {
                crashDetailBean.z = "unknown(record)";
            }
            if (userLog == null) {
                crashDetailBean.x = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.".getBytes();
            } else {
                crashDetailBean.x = userLog;
            }
        }
        return crashDetailBean;
    }

    public void handleNativeException(int pid, int tid, long exTimesInSecond, long exTimeInMicosecond, String exType, String exAddress, String exStack, String tombPath, int si_code, String si_CodeType, int sendingPid, int sendingUid, int si_errno, String si_errnoMsg, String nativeRQDVersion) {
        z.a("Native Crash Happen v1", new Object[0]);
        handleNativeException2(pid, tid, exTimesInSecond, exTimeInMicosecond, exType, exAddress, exStack, tombPath, si_code, si_CodeType, sendingPid, sendingUid, si_errno, si_errnoMsg, nativeRQDVersion, null);
    }

    public void handleNativeException2(int pid, int tid, long exTimesInSecond, long exTimeInMicosecond, String exType, String exAddress, String exStack, String tombPath, int si_code, String si_CodeType, int sendingPid, int sendingUid, int si_errno, String si_errnoMsg, String nativeRQDVersion, String[] extraMsg) {
        z.a("Native Crash Happen v2", new Object[0]);
        try {
            int i;
            String str;
            String str2;
            this.d.d();
            if (!this.d.b()) {
                z.e("waiting for remote sync", new Object[0]);
                i = 0;
                while (!this.d.b()) {
                    ag.a(500);
                    i += ErrorInfo.ERROR_CODE_PARAMS;
                    if (i >= 5000) {
                        break;
                    }
                }
            }
            long j = (1000 * exTimesInSecond) + (exTimeInMicosecond / 1000);
            String b = b.b(exStack);
            String str3 = Const.CONNECTION_TYPE_UNKNOWN;
            String str4 = "UNKNOWN(" + sendingPid + ")";
            if (si_code > 0) {
                str = "KERNEL";
                str2 = exType + "(" + si_CodeType + ")";
            } else if (sendingPid > 0) {
                str4 = AppInfo.a(this.a, sendingPid);
                str = si_CodeType;
                str2 = exType;
            } else {
                str = si_CodeType;
                str2 = exType;
            }
            if (!this.d.b()) {
                z.d("no remote but still store!", new Object[0]);
            }
            if (this.d.d().d || !this.d.b()) {
                String str5 = null;
                String str6 = null;
                if (extraMsg != null) {
                    HashMap hashMap = new HashMap();
                    for (String split : extraMsg) {
                        String[] split2 = split.split(MASTNativeAdConstants.EQUAL);
                        if (split2.length == 2) {
                            hashMap.put(split2[0], split2[1]);
                        } else {
                            z.e("bad extraMsg %s", split);
                        }
                    }
                    str6 = (String) hashMap.get("ExceptionThreadName");
                    str5 = (String) hashMap.get("ExceptionProcessName");
                } else {
                    z.c("not found extraMsg", new Object[0]);
                }
                if (str5 == null || str5.length() == 0) {
                    str5 = this.c.E();
                } else {
                    z.c("crash process name change to %s", str5);
                }
                if (str6 == null || str6.length() == 0) {
                    str6 = Thread.currentThread().getName();
                } else {
                    z.c("crash thread name change to %s", str6);
                }
                CrashDetailBean packageCrashDatas = packageCrashDatas(str5, str6, j, str2, exAddress, b, str, str4, tombPath, nativeRQDVersion, null, null, true);
                if (packageCrashDatas == null) {
                    z.e("pkg crash datas fail!", new Object[0]);
                    this.d.a("packageFail_dropEXP", false);
                    return;
                }
                a(packageCrashDatas);
                this.d.a(packageCrashDatas);
                if (!this.b.a(packageCrashDatas)) {
                    this.b.a(packageCrashDatas, 5000);
                }
                b.c(this.e);
                return;
            }
            z.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
            a(Thread.currentThread().getName(), str2, exAddress, b, str, str4, nativeRQDVersion);
            ag.c(tombPath);
            this.d.a("remoteClose_dropEXP", false);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
        }
    }
}
