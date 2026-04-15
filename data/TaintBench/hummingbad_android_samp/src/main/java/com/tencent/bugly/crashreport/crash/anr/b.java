package com.tencent.bugly.crashreport.crash.anr;

import android.app.ActivityManager;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.FileObserver;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.picksinit.ErrorInfo;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.d;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: BUGLY */
public class b implements com.tencent.bugly.crashreport.common.strategy.b {
    private final int a = 0;
    private final int b = 1;
    private final int c = 2;
    private final int d = 3;
    private AtomicInteger e = new AtomicInteger(0);
    private long f = -1;
    private final Context g;
    private final a h;
    private final y i;
    private final c j;
    private final String k;
    private final q l;
    private final com.tencent.bugly.crashreport.crash.b m;
    private final CrashHandleCallback n;
    private FileObserver o;
    private boolean p = true;

    public b(Context context, c cVar, a aVar, y yVar, q qVar, com.tencent.bugly.crashreport.crash.b bVar, CrashHandleCallback crashHandleCallback) {
        this.g = ag.a(context);
        this.k = context.getDir("bugly", 0).getAbsolutePath();
        this.h = aVar;
        this.i = yVar;
        this.j = cVar;
        this.l = qVar;
        this.m = bVar;
        this.n = crashHandleCallback;
    }

    /* access modifiers changed from: protected */
    public void a(a aVar) {
    }

    /* access modifiers changed from: protected */
    public void a(CrashDetailBean crashDetailBean) {
    }

    /* access modifiers changed from: protected */
    public ProcessErrorStateInfo a(Context context, long j) {
        if (j < 0) {
            j = 0;
        }
        z.c("to find!", new Object[0]);
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        long j2 = j / 500;
        int i = 0;
        while (true) {
            z.c("waiting!", new Object[0]);
            List<ProcessErrorStateInfo> processesInErrorState = activityManager.getProcessesInErrorState();
            if (processesInErrorState != null) {
                for (ProcessErrorStateInfo processErrorStateInfo : processesInErrorState) {
                    if (processErrorStateInfo.condition == 2) {
                        z.c("found!", new Object[0]);
                        return processErrorStateInfo;
                    }
                }
            }
            ag.a(500);
            int i2 = i + 1;
            if (((long) i) >= j2) {
                z.c("end!", new Object[0]);
                return null;
            }
            i = i2;
        }
    }

    /* access modifiers changed from: protected */
    public a a(Context context, ProcessErrorStateInfo processErrorStateInfo, long j, Map<String, String> map) {
        File file = new File(context.getFilesDir(), "bugly/bugly_trace_" + j + ".txt");
        a aVar = new a();
        aVar.c = j;
        aVar.d = file.getAbsolutePath();
        aVar.a = processErrorStateInfo.processName;
        aVar.f = processErrorStateInfo.shortMsg;
        aVar.e = processErrorStateInfo.longMsg;
        aVar.b = map;
        if (map != null) {
            for (String str : map.keySet()) {
                if (str.startsWith("main(")) {
                    aVar.g = (String) map.get(str);
                }
            }
        }
        String str2 = "anr tm:%d\ntr:%s\nproc:%s\nsMsg:%s\n lMsg:%s\n threads:%d";
        Object[] objArr = new Object[6];
        objArr[0] = Long.valueOf(aVar.c);
        objArr[1] = aVar.d;
        objArr[2] = aVar.a;
        objArr[3] = aVar.f;
        objArr[4] = aVar.e;
        objArr[5] = Integer.valueOf(aVar.b == null ? 0 : aVar.b.size());
        z.c(str2, objArr);
        return aVar;
    }

    /* access modifiers changed from: protected */
    public CrashDetailBean b(a aVar) {
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.i();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.k();
        crashDetailBean.E = this.h.u();
        crashDetailBean.F = this.h.t();
        crashDetailBean.G = this.h.v();
        crashDetailBean.w = d.a(this.g, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        crashDetailBean.x = ab.a(true);
        crashDetailBean.b = 3;
        crashDetailBean.e = this.h.n();
        crashDetailBean.f = this.h.e();
        crashDetailBean.g = this.h.A();
        crashDetailBean.m = this.h.m();
        crashDetailBean.n = "ANR_EXCEPTION";
        crashDetailBean.o = aVar.f;
        crashDetailBean.q = aVar.g;
        crashDetailBean.O = new HashMap();
        crashDetailBean.O.put("BUGLY_CR_01", aVar.e);
        int indexOf = crashDetailBean.q.indexOf(MASTNativeAdConstants.NEWLINE);
        crashDetailBean.p = indexOf > 0 ? crashDetailBean.q.substring(0, indexOf) : "GET_FAIL";
        crashDetailBean.r = aVar.c;
        crashDetailBean.u = ag.a(crashDetailBean.q.getBytes());
        crashDetailBean.y = aVar.b;
        crashDetailBean.z = this.h.E();
        crashDetailBean.A = "main";
        crashDetailBean.H = this.h.D();
        crashDetailBean.h = this.h.z();
        crashDetailBean.v = aVar.d;
        crashDetailBean.L = this.h.G();
        crashDetailBean.M = this.h.b();
        crashDetailBean.P = this.h.N();
        crashDetailBean.Q = this.h.O();
        crashDetailBean.R = this.h.J();
        crashDetailBean.S = this.h.M();
        return crashDetailBean;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0183 A:{Catch:{ all -> 0x01ef }} */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01b6 A:{SYNTHETIC, Splitter:B:53:0x01b6} */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01de A:{SYNTHETIC, Splitter:B:71:0x01de} */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01de A:{SYNTHETIC, Splitter:B:71:0x01de} */
    public boolean a(java.lang.String r12, java.lang.String r13, java.lang.String r14) {
        /*
        r11 = this;
        r10 = 3;
        r4 = 2;
        r2 = 1;
        r3 = 0;
        r5 = com.tencent.bugly.crashreport.crash.anr.c.a(r14, r12, r2);
        if (r5 == 0) goto L_0x0016;
    L_0x000a:
        r0 = r5.d;
        if (r0 == 0) goto L_0x0016;
    L_0x000e:
        r0 = r5.d;
        r0 = r0.size();
        if (r0 > 0) goto L_0x0021;
    L_0x0016:
        r0 = "not found trace dump for %s";
        r1 = new java.lang.Object[r2];
        r1[r3] = r14;
        com.tencent.bugly.proguard.z.e(r0, r1);
        r0 = r3;
    L_0x0020:
        return r0;
    L_0x0021:
        r0 = new java.io.File;
        r0.<init>(r13);
        r1 = r0.exists();	 Catch:{ Exception -> 0x0057 }
        if (r1 != 0) goto L_0x0040;
    L_0x002c:
        r1 = r0.getParentFile();	 Catch:{ Exception -> 0x0057 }
        r1 = r1.exists();	 Catch:{ Exception -> 0x0057 }
        if (r1 != 0) goto L_0x003d;
    L_0x0036:
        r1 = r0.getParentFile();	 Catch:{ Exception -> 0x0057 }
        r1.mkdirs();	 Catch:{ Exception -> 0x0057 }
    L_0x003d:
        r0.createNewFile();	 Catch:{ Exception -> 0x0057 }
    L_0x0040:
        r1 = r0.exists();
        if (r1 == 0) goto L_0x004c;
    L_0x0046:
        r1 = r0.canWrite();
        if (r1 != 0) goto L_0x0091;
    L_0x004c:
        r0 = "backup file create fail %s";
        r1 = new java.lang.Object[r2];
        r1[r3] = r13;
        com.tencent.bugly.proguard.z.e(r0, r1);
        r0 = r3;
        goto L_0x0020;
    L_0x0057:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x0061;
    L_0x005e:
        r0.printStackTrace();
    L_0x0061:
        r1 = "backup file create error! %s  %s";
        r4 = new java.lang.Object[r4];
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = r0.getClass();
        r6 = r6.getName();
        r5 = r5.append(r6);
        r6 = ":";
        r5 = r5.append(r6);
        r0 = r0.getMessage();
        r0 = r5.append(r0);
        r0 = r0.toString();
        r4[r3] = r0;
        r4[r2] = r13;
        com.tencent.bugly.proguard.z.e(r1, r4);
        r0 = r3;
        goto L_0x0020;
    L_0x0091:
        r1 = 0;
        r4 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x01f2, all -> 0x01da }
        r6 = new java.io.FileWriter;	 Catch:{ IOException -> 0x01f2, all -> 0x01da }
        r7 = 0;
        r6.<init>(r0, r7);	 Catch:{ IOException -> 0x01f2, all -> 0x01da }
        r4.<init>(r6);	 Catch:{ IOException -> 0x01f2, all -> 0x01da }
        r0 = r5.d;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = "main";
        r0 = r0.get(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = (java.lang.String[]) r0;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r0 == 0) goto L_0x00e8;
    L_0x00a9:
        r1 = r0.length;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r1 < r10) goto L_0x00e8;
    L_0x00ac:
        r1 = 0;
        r1 = r0[r1];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r6 = 1;
        r6 = r0[r6];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r7 = 2;
        r0 = r0[r7];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r7.<init>();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r8 = "\"main\" tid=";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r7.append(r0);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r7 = " :\n";
        r0 = r0.append(r7);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = "\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.append(r6);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = "\n\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.toString();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r4.write(r0);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r4.flush();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
    L_0x00e8:
        r0 = r5.d;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.entrySet();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r5 = r0.iterator();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
    L_0x00f2:
        r0 = r5.hasNext();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r0 == 0) goto L_0x01bc;
    L_0x00f8:
        r0 = r5.next();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = r0.getKey();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = (java.lang.String) r1;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r6 = "main";
        r1 = r1.equals(r6);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r1 != 0) goto L_0x00f2;
    L_0x010c:
        r1 = r0.getValue();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r1 == 0) goto L_0x00f2;
    L_0x0112:
        r1 = r0.getValue();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = (java.lang.String[]) r1;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = r1.length;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        if (r1 < r10) goto L_0x00f2;
    L_0x011b:
        r1 = r0.getValue();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = (java.lang.String[]) r1;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r6 = 0;
        r6 = r1[r6];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = r0.getValue();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = (java.lang.String[]) r1;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r7 = 1;
        r7 = r1[r7];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = r0.getValue();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = (java.lang.String[]) r1;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r8 = 2;
        r1 = r1[r8];	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r8.<init>();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r9 = "\"";
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.getKey();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = (java.lang.String) r0;	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r8.append(r0);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r8 = "\" tid=";
        r0 = r0.append(r8);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = " :\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.append(r6);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = "\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.append(r7);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r1 = "\n\n";
        r0 = r0.append(r1);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r0 = r0.toString();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r4.write(r0);	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        r4.flush();	 Catch:{ IOException -> 0x017b, all -> 0x01ed }
        goto L_0x00f2;
    L_0x017b:
        r0 = move-exception;
        r1 = r4;
    L_0x017d:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x01ef }
        if (r2 != 0) goto L_0x0186;
    L_0x0183:
        r0.printStackTrace();	 Catch:{ all -> 0x01ef }
    L_0x0186:
        r2 = "dump trace fail %s";
        r4 = 1;
        r4 = new java.lang.Object[r4];	 Catch:{ all -> 0x01ef }
        r5 = 0;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01ef }
        r6.<init>();	 Catch:{ all -> 0x01ef }
        r7 = r0.getClass();	 Catch:{ all -> 0x01ef }
        r7 = r7.getName();	 Catch:{ all -> 0x01ef }
        r6 = r6.append(r7);	 Catch:{ all -> 0x01ef }
        r7 = ":";
        r6 = r6.append(r7);	 Catch:{ all -> 0x01ef }
        r0 = r0.getMessage();	 Catch:{ all -> 0x01ef }
        r0 = r6.append(r0);	 Catch:{ all -> 0x01ef }
        r0 = r0.toString();	 Catch:{ all -> 0x01ef }
        r4[r5] = r0;	 Catch:{ all -> 0x01ef }
        com.tencent.bugly.proguard.z.e(r2, r4);	 Catch:{ all -> 0x01ef }
        if (r1 == 0) goto L_0x01b9;
    L_0x01b6:
        r1.close();	 Catch:{ IOException -> 0x01cf }
    L_0x01b9:
        r0 = r3;
        goto L_0x0020;
    L_0x01bc:
        if (r4 == 0) goto L_0x01c1;
    L_0x01be:
        r4.close();	 Catch:{ IOException -> 0x01c4 }
    L_0x01c1:
        r0 = r2;
        goto L_0x0020;
    L_0x01c4:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x01c1;
    L_0x01cb:
        r0.printStackTrace();
        goto L_0x01c1;
    L_0x01cf:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);
        if (r1 != 0) goto L_0x01b9;
    L_0x01d6:
        r0.printStackTrace();
        goto L_0x01b9;
    L_0x01da:
        r0 = move-exception;
        r4 = r1;
    L_0x01dc:
        if (r4 == 0) goto L_0x01e1;
    L_0x01de:
        r4.close();	 Catch:{ IOException -> 0x01e2 }
    L_0x01e1:
        throw r0;
    L_0x01e2:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x01e1;
    L_0x01e9:
        r1.printStackTrace();
        goto L_0x01e1;
    L_0x01ed:
        r0 = move-exception;
        goto L_0x01dc;
    L_0x01ef:
        r0 = move-exception;
        r4 = r1;
        goto L_0x01dc;
    L_0x01f2:
        r0 = move-exception;
        goto L_0x017d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.b.a(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    public boolean a() {
        return this.e.get() != 0;
    }

    /* access modifiers changed from: protected */
    public boolean a(Context context, String str, ProcessErrorStateInfo processErrorStateInfo, long j, Map<String, String> map) {
        this.j.d();
        if (!this.j.b()) {
            z.e("waiting for remote sync", new Object[0]);
            int i = 0;
            while (!this.j.b()) {
                ag.a(500);
                i += ErrorInfo.ERROR_CODE_PARAMS;
                if (i >= 5000) {
                    break;
                }
            }
        }
        a a = a(context, processErrorStateInfo, j, map);
        if (!this.j.b()) {
            z.e("crash report sync remote fail, will not upload to Bugly , print local for helpful!", new Object[0]);
            a(a);
            this.j.a("noRemoteStrategy_dropANR", false);
            return false;
        } else if (this.j.d().g) {
            z.a("found visiable anr , start to upload!", new Object[0]);
            CrashDetailBean b = b(a);
            if (b == null) {
                z.e("pack anr fail!", new Object[0]);
                return false;
            }
            this.l.b(b);
            if (b.a >= 0) {
                z.a("backup anr record success!", new Object[0]);
            } else {
                z.d("backup anr record fail!", new Object[0]);
            }
            if (str != null && new File(str).exists()) {
                this.e.set(3);
                if (a(str, a.d, a.a)) {
                    z.a("backup trace success", new Object[0]);
                }
            }
            a(b);
            if (!this.m.a(b)) {
                this.m.a(b, 5000);
            }
            if (this.n != null) {
                try {
                    Map onCrashHandleStart = this.n.onCrashHandleStart(4, b.n, b.o, b.q);
                    if (onCrashHandleStart != null && onCrashHandleStart.size() > 0) {
                        z.d("anr will not attach user data size:%d", Integer.valueOf(onCrashHandleStart.size()));
                    }
                } catch (Throwable th) {
                    if (!z.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            z.d("ANR Report is closed!", new Object[0]);
            return false;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Missing block: B:10:?, code skipped:
            com.tencent.bugly.proguard.z.c("read trace first dump for create time!", new java.lang.Object[0]);
            r2 = com.tencent.bugly.crashreport.crash.anr.c.a(r11, false);
     */
    /* JADX WARNING: Missing block: B:11:0x002c, code skipped:
            if (r2 == null) goto L_0x0119;
     */
    /* JADX WARNING: Missing block: B:12:0x002e, code skipped:
            r4 = r2.c;
     */
    /* JADX WARNING: Missing block: B:14:0x0032, code skipped:
            if (r4 != -1) goto L_0x0045;
     */
    /* JADX WARNING: Missing block: B:15:0x0034, code skipped:
            com.tencent.bugly.proguard.z.d("trace dump fail could not get time!", new java.lang.Object[0]);
            r4 = new java.util.Date().getTime();
     */
    /* JADX WARNING: Missing block: B:17:0x004f, code skipped:
            if (java.lang.Math.abs(r4 - r10.f) >= 10000) goto L_0x006b;
     */
    /* JADX WARNING: Missing block: B:18:0x0051, code skipped:
            com.tencent.bugly.proguard.z.d("should not process ANR too Fre in %d", java.lang.Integer.valueOf(10000));
     */
    /* JADX WARNING: Missing block: B:19:0x0062, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            r10.f = r4;
            r10.e.set(1);
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            r6 = com.tencent.bugly.crashreport.crash.d.a((int) com.nostra13.universalimageloader.core.download.BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT, false);
     */
    /* JADX WARNING: Missing block: B:29:0x007a, code skipped:
            if (r6 == null) goto L_0x0082;
     */
    /* JADX WARNING: Missing block: B:32:0x0080, code skipped:
            if (r6.size() > 0) goto L_0x00a3;
     */
    /* JADX WARNING: Missing block: B:33:0x0082, code skipped:
            com.tencent.bugly.proguard.z.d("can't get all thread skip this anr", new java.lang.Object[0]);
     */
    /* JADX WARNING: Missing block: B:34:0x008a, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:35:0x0090, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:37:?, code skipped:
            com.tencent.bugly.proguard.z.a(r0);
            com.tencent.bugly.proguard.z.e("get all thread stack fail!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Missing block: B:38:0x009c, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            r3 = a(r10.g, 10000);
     */
    /* JADX WARNING: Missing block: B:41:0x00ab, code skipped:
            if (r3 != null) goto L_0x00bc;
     */
    /* JADX WARNING: Missing block: B:42:0x00ad, code skipped:
            com.tencent.bugly.proguard.z.c("proc state is unvisiable!", new java.lang.Object[0]);
     */
    /* JADX WARNING: Missing block: B:43:0x00b5, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:46:0x00c2, code skipped:
            if (r3.pid == android.os.Process.myPid()) goto L_0x00d8;
     */
    /* JADX WARNING: Missing block: B:47:0x00c4, code skipped:
            com.tencent.bugly.proguard.z.c("not mind proc!", r3.processName);
     */
    /* JADX WARNING: Missing block: B:48:0x00d1, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            com.tencent.bugly.proguard.z.a("found visiable anr , start to process!", new java.lang.Object[0]);
            a(r10.g, r11, r3, r4, r6);
     */
    /* JADX WARNING: Missing block: B:51:0x00e7, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:52:0x00ee, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:55:0x00f3, code skipped:
            if (com.tencent.bugly.proguard.z.a(r0) == false) goto L_0x00f5;
     */
    /* JADX WARNING: Missing block: B:56:0x00f5, code skipped:
            r0.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:57:0x00f8, code skipped:
            com.tencent.bugly.proguard.z.e("handle anr error %s", r0.getClass().toString());
     */
    /* JADX WARNING: Missing block: B:59:0x0113, code skipped:
            r10.e.set(0);
     */
    /* JADX WARNING: Missing block: B:60:0x0119, code skipped:
            r4 = -1;
     */
    /* JADX WARNING: Missing block: B:65:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:66:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:67:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:68:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:69:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:70:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:71:?, code skipped:
            return;
     */
    public final void a(java.lang.String r11) {
        /*
        r10 = this;
        r8 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = -1;
        r7 = 0;
        monitor-enter(r10);
        r2 = r10.e;	 Catch:{ all -> 0x0068 }
        r2 = r2.get();	 Catch:{ all -> 0x0068 }
        if (r2 == 0) goto L_0x0018;
    L_0x000e:
        r0 = "trace started return ";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x0068 }
        com.tencent.bugly.proguard.z.c(r0, r1);	 Catch:{ all -> 0x0068 }
        monitor-exit(r10);	 Catch:{ all -> 0x0068 }
    L_0x0017:
        return;
    L_0x0018:
        r2 = r10.e;	 Catch:{ all -> 0x0068 }
        r3 = 1;
        r2.set(r3);	 Catch:{ all -> 0x0068 }
        monitor-exit(r10);	 Catch:{ all -> 0x0068 }
        r2 = "read trace first dump for create time!";
        r3 = 0;
        r3 = new java.lang.Object[r3];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.c(r2, r3);	 Catch:{ Throwable -> 0x00ee }
        r2 = 0;
        r2 = com.tencent.bugly.crashreport.crash.anr.c.a(r11, r2);	 Catch:{ Throwable -> 0x00ee }
        if (r2 == 0) goto L_0x0119;
    L_0x002e:
        r4 = r2.c;	 Catch:{ Throwable -> 0x00ee }
    L_0x0030:
        r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r0 != 0) goto L_0x0045;
    L_0x0034:
        r0 = "trace dump fail could not get time!";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.d(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = new java.util.Date;	 Catch:{ Throwable -> 0x00ee }
        r0.<init>();	 Catch:{ Throwable -> 0x00ee }
        r4 = r0.getTime();	 Catch:{ Throwable -> 0x00ee }
    L_0x0045:
        r0 = r10.f;	 Catch:{ Throwable -> 0x00ee }
        r0 = r4 - r0;
        r0 = java.lang.Math.abs(r0);	 Catch:{ Throwable -> 0x00ee }
        r0 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r0 >= 0) goto L_0x006b;
    L_0x0051:
        r0 = "should not process ANR too Fre in %d";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        r2 = 0;
        r3 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ Throwable -> 0x00ee }
        r1[r2] = r3;	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.d(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x0068:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0068 }
        throw r0;
    L_0x006b:
        r10.f = r4;	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;	 Catch:{ Throwable -> 0x00ee }
        r1 = 1;
        r0.set(r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r1 = 0;
        r6 = com.tencent.bugly.crashreport.crash.d.a(r0, r1);	 Catch:{ Throwable -> 0x0090 }
        if (r6 == 0) goto L_0x0082;
    L_0x007c:
        r0 = r6.size();	 Catch:{ Throwable -> 0x00ee }
        if (r0 > 0) goto L_0x00a3;
    L_0x0082:
        r0 = "can't get all thread skip this anr";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.d(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x0090:
        r0 = move-exception;
        com.tencent.bugly.proguard.z.a(r0);	 Catch:{ Throwable -> 0x00ee }
        r0 = "get all thread stack fail!";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.e(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x00a3:
        r0 = r10.g;	 Catch:{ Throwable -> 0x00ee }
        r1 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3 = r10.a(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        if (r3 != 0) goto L_0x00bc;
    L_0x00ad:
        r0 = "proc state is unvisiable!";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.c(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x00bc:
        r0 = r3.pid;	 Catch:{ Throwable -> 0x00ee }
        r1 = android.os.Process.myPid();	 Catch:{ Throwable -> 0x00ee }
        if (r0 == r1) goto L_0x00d8;
    L_0x00c4:
        r0 = "not mind proc!";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        r2 = 0;
        r3 = r3.processName;	 Catch:{ Throwable -> 0x00ee }
        r1[r2] = r3;	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.c(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x00d8:
        r0 = "found visiable anr , start to process!";
        r1 = 0;
        r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x00ee }
        com.tencent.bugly.proguard.z.a(r0, r1);	 Catch:{ Throwable -> 0x00ee }
        r1 = r10.g;	 Catch:{ Throwable -> 0x00ee }
        r0 = r10;
        r2 = r11;
        r0.a(r1, r2, r3, r4, r6);	 Catch:{ Throwable -> 0x00ee }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x00ee:
        r0 = move-exception;
        r1 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x0112 }
        if (r1 != 0) goto L_0x00f8;
    L_0x00f5:
        r0.printStackTrace();	 Catch:{ all -> 0x0112 }
    L_0x00f8:
        r1 = "handle anr error %s";
        r2 = 1;
        r2 = new java.lang.Object[r2];	 Catch:{ all -> 0x0112 }
        r3 = 0;
        r0 = r0.getClass();	 Catch:{ all -> 0x0112 }
        r0 = r0.toString();	 Catch:{ all -> 0x0112 }
        r2[r3] = r0;	 Catch:{ all -> 0x0112 }
        com.tencent.bugly.proguard.z.e(r1, r2);	 Catch:{ all -> 0x0112 }
        r0 = r10.e;
        r0.set(r7);
        goto L_0x0017;
    L_0x0112:
        r0 = move-exception;
        r1 = r10.e;
        r1.set(r7);
        throw r0;
    L_0x0119:
        r4 = r0;
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.b.a(java.lang.String):void");
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void b() {
        if (d()) {
            z.d("start when started!", new Object[0]);
        } else {
            this.o = new FileObserver("/data/anr/", 8) {
                public void onEvent(int event, String path) {
                    if (path != null) {
                        String str = "/data/anr/" + path;
                        if (str.contains("trace")) {
                            b.this.a(str);
                            return;
                        }
                        z.d("not anr file %s", str);
                    }
                }
            };
            try {
                this.o.startWatching();
                z.a("start anr monitor!", new Object[0]);
                this.i.b(new Runnable() {
                    public void run() {
                        b.this.f();
                    }
                });
            } catch (Throwable th) {
                this.o = null;
                z.d("start anr monitor failed!", new Object[0]);
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void c() {
        if (d()) {
            try {
                this.o.stopWatching();
                this.o = null;
                z.d("close anr monitor!", new Object[0]);
            } catch (Throwable th) {
                z.d("stop anr monitor failed!", new Object[0]);
                if (!z.a(th)) {
                    th.printStackTrace();
                }
            }
        } else {
            z.d("close when closed!", new Object[0]);
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized boolean d() {
        return this.o != null;
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void a(boolean z) {
        if (z) {
            b();
        } else {
            c();
        }
    }

    public synchronized boolean e() {
        return this.p;
    }

    private synchronized void c(boolean z) {
        if (this.p != z) {
            z.a("user change anr %b", Boolean.valueOf(z));
            this.p = z;
        }
    }

    public void b(boolean z) {
        c(z);
        boolean z2 = c.a().d().g && e();
        if (z2 != d()) {
            z.a("anr changed to %b", Boolean.valueOf(z2));
            a(z2);
        }
    }

    /* access modifiers changed from: protected */
    public void f() {
        long b = ag.b() - 604800000;
        File file = new File(this.k);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length != 0) {
                String str = "bugly_trace_";
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

    public synchronized void a(StrategyBean strategyBean) {
        boolean z = true;
        synchronized (this) {
            if (strategyBean != null) {
                if (strategyBean.g != d()) {
                    z.d("server anr changed to %b", Boolean.valueOf(strategyBean.g));
                }
            }
            if (!(strategyBean.g && e())) {
                z = false;
            }
            if (z != d()) {
                z.a("anr changed to %b", Boolean.valueOf(z));
                a(z);
            }
        }
    }

    public void g() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.tencent.bugly.anr.testor");
            this.g.registerReceiver(new BuglyTestANR_Reciver(), intentFilter);
            this.g.sendBroadcast(new Intent("com.tencent.bugly.anr.testor"));
            z.a("try to make a test ANR", new Object[0]);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
        }
    }
}
