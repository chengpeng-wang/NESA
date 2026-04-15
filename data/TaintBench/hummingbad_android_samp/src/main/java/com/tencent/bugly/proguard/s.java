package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.UserInfoBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: BUGLY */
public class s {
    public static bc a(UserInfoBean userInfoBean, a aVar) {
        if (userInfoBean == null) {
            return null;
        }
        bc bcVar = new bc();
        bcVar.a = userInfoBean.e;
        bcVar.d = userInfoBean.c;
        bcVar.c = userInfoBean.d;
        bcVar.g = v.a();
        bcVar.h = userInfoBean.i == 1;
        switch (userInfoBean.b) {
            case 1:
                bcVar.b = (byte) 1;
                break;
            case 2:
                bcVar.b = (byte) 4;
                break;
            case 3:
                bcVar.b = (byte) 2;
                break;
            case 4:
                bcVar.b = (byte) 3;
                break;
            default:
                if (userInfoBean.b >= 10 && userInfoBean.b < 20) {
                    bcVar.b = (byte) userInfoBean.b;
                    break;
                }
                z.e("unknown uinfo type %d ", Integer.valueOf(userInfoBean.b));
                return null;
        }
        bcVar.f = new HashMap();
        if (userInfoBean.j >= 0) {
            bcVar.f.put("C01", "" + userInfoBean.j);
        }
        if (userInfoBean.k >= 0) {
            bcVar.f.put("C02", "" + userInfoBean.k);
        }
        if (userInfoBean.l != null && userInfoBean.l.size() > 0) {
            for (Entry entry : userInfoBean.l.entrySet()) {
                bcVar.f.put("C03_" + ((String) entry.getKey()), entry.getValue());
            }
        }
        if (userInfoBean.m != null && userInfoBean.m.size() > 0) {
            for (Entry entry2 : userInfoBean.m.entrySet()) {
                bcVar.f.put("C04_" + ((String) entry2.getKey()), entry2.getValue());
            }
        }
        z.c("summary type %d vm:%d", Byte.valueOf(bcVar.b), Integer.valueOf(bcVar.f.size()));
        return bcVar;
    }

    public static bd a(List<UserInfoBean> list, a aVar, int i) {
        if (list == null || list.size() == 0) {
            return null;
        }
        bd bdVar = new bd();
        bdVar.b = aVar.E();
        bdVar.c = aVar.n();
        ArrayList arrayList = new ArrayList();
        for (UserInfoBean a : list) {
            bc a2 = a(a, aVar);
            if (a2 != null) {
                arrayList.add(a2);
            }
        }
        bdVar.d = arrayList;
        bdVar.e = new HashMap();
        bdVar.e.put("A7", "" + aVar.j());
        bdVar.e.put("A6", "" + aVar.x());
        bdVar.e.put("A5", "" + aVar.w());
        bdVar.e.put("A2", "" + aVar.u());
        bdVar.e.put("A1", "" + aVar.u());
        bdVar.e.put("A24", "" + aVar.l());
        bdVar.e.put("A17", "" + aVar.v());
        bdVar.e.put("A15", "" + aVar.A());
        bdVar.e.put("A13", "" + aVar.C());
        switch (i) {
            case 1:
                bdVar.a = (byte) 1;
                break;
            case 2:
                bdVar.a = (byte) 2;
                break;
            default:
                z.e("unknown up type %d ", Integer.valueOf(i));
                return null;
        }
        return bdVar;
    }

    public static aw a(Context context, CrashDetailBean crashDetailBean, a aVar) {
        boolean z = true;
        if (context == null || crashDetailBean == null || aVar == null) {
            z.d("enExp args == null", new Object[0]);
            return null;
        }
        at atVar;
        boolean z2;
        aw awVar = new aw();
        switch (crashDetailBean.b) {
            case 0:
                awVar.a = crashDetailBean.j ? "200" : "100";
                break;
            case 1:
                awVar.a = crashDetailBean.j ? "201" : "101";
                break;
            case 2:
                awVar.a = crashDetailBean.j ? "202" : "102";
                break;
            case 3:
                awVar.a = crashDetailBean.j ? "203" : "103";
                break;
            case 4:
                awVar.a = crashDetailBean.j ? "204" : "104";
                break;
            case 5:
                awVar.a = crashDetailBean.j ? "207" : "107";
                break;
            case 6:
                awVar.a = crashDetailBean.j ? "206" : "106";
                break;
            default:
                z.e("crash type error! %d", Integer.valueOf(crashDetailBean.b));
                break;
        }
        awVar.b = crashDetailBean.r;
        awVar.c = crashDetailBean.n;
        awVar.d = crashDetailBean.o;
        awVar.e = crashDetailBean.p;
        awVar.g = crashDetailBean.q;
        awVar.h = crashDetailBean.y;
        awVar.i = crashDetailBean.c;
        awVar.j = null;
        awVar.l = crashDetailBean.m;
        awVar.m = crashDetailBean.e;
        awVar.f = crashDetailBean.A;
        awVar.t = v.a();
        awVar.n = null;
        if (crashDetailBean.i != null && crashDetailBean.i.size() > 0) {
            awVar.o = new ArrayList();
            for (Entry entry : crashDetailBean.i.entrySet()) {
                atVar = new at();
                atVar.a = ((PlugInBean) entry.getValue()).a;
                atVar.c = ((PlugInBean) entry.getValue()).c;
                atVar.e = ((PlugInBean) entry.getValue()).b;
                awVar.o.add(atVar);
            }
        }
        if (crashDetailBean.h != null && crashDetailBean.h.size() > 0) {
            awVar.p = new ArrayList();
            for (Entry entry2 : crashDetailBean.h.entrySet()) {
                atVar = new at();
                atVar.a = ((PlugInBean) entry2.getValue()).a;
                atVar.c = ((PlugInBean) entry2.getValue()).c;
                atVar.e = ((PlugInBean) entry2.getValue()).b;
                awVar.p.add(atVar);
            }
        }
        if (crashDetailBean.j) {
            int size;
            awVar.k = crashDetailBean.t;
            if (crashDetailBean.s != null && crashDetailBean.s.length() > 0) {
                if (awVar.q == null) {
                    awVar.q = new ArrayList();
                }
                try {
                    awVar.q.add(new av((byte) 1, "alltimes.txt", crashDetailBean.s.getBytes("utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    awVar.q = null;
                }
            }
            String str = "crashcount:%d sz:%d";
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(awVar.k);
            if (awVar.q != null) {
                size = awVar.q.size();
            } else {
                size = 0;
            }
            objArr[1] = Integer.valueOf(size);
            z.c(str, objArr);
        }
        if (crashDetailBean.w != null) {
            if (awVar.q == null) {
                awVar.q = new ArrayList();
            }
            try {
                awVar.q.add(new av((byte) 1, "log.txt", crashDetailBean.w.getBytes("utf-8")));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                awVar.q = null;
            }
        }
        av avVar = new av((byte) 2, "buglylog.zip", crashDetailBean.x);
        if (avVar != null) {
            z.c("attach user log", new Object[0]);
            if (awVar.q == null) {
                awVar.q = new ArrayList();
            }
            awVar.q.add(avVar);
        }
        if (crashDetailBean.b == 3) {
            if (awVar.q == null) {
                awVar.q = new ArrayList();
            }
            if (crashDetailBean.O != null && crashDetailBean.O.containsKey("BUGLY_CR_01")) {
                try {
                    awVar.q.add(new av((byte) 1, "anrMessage.txt", ((String) crashDetailBean.O.get("BUGLY_CR_01")).getBytes("utf-8")));
                    z.c("attach anr message", new Object[0]);
                } catch (UnsupportedEncodingException e22) {
                    e22.printStackTrace();
                    awVar.q = null;
                }
                crashDetailBean.O.remove("BUGLY_CR_01");
            }
            if (crashDetailBean.v != null) {
                avVar = a("trace.zip", context, crashDetailBean.v);
                if (avVar != null) {
                    z.c("attach traces", new Object[0]);
                    awVar.q.add(avVar);
                }
            }
        }
        if (crashDetailBean.b == 1) {
            if (awVar.q == null) {
                awVar.q = new ArrayList();
            }
            if (crashDetailBean.v != null) {
                avVar = a("tomb.zip", context, crashDetailBean.v);
                if (avVar != null) {
                    z.c("attach tombs", new Object[0]);
                    awVar.q.add(avVar);
                }
            }
        }
        if (crashDetailBean.T != null && crashDetailBean.T.length > 0) {
            if (awVar.q == null) {
                awVar.q = new ArrayList();
            }
            awVar.q.add(new av((byte) 1, "userExtraByteData", crashDetailBean.T));
            z.c("attach extraData", new Object[0]);
        }
        awVar.r = new HashMap();
        awVar.r.put("A9", "" + crashDetailBean.B);
        awVar.r.put("A11", "" + crashDetailBean.C);
        awVar.r.put("A10", "" + crashDetailBean.D);
        awVar.r.put("A23", "" + crashDetailBean.f);
        awVar.r.put("A7", "" + aVar.j());
        awVar.r.put("A6", "" + aVar.x());
        awVar.r.put("A5", "" + aVar.w());
        awVar.r.put("A22", "" + aVar.n());
        awVar.r.put("A2", "" + crashDetailBean.F);
        awVar.r.put("A1", "" + crashDetailBean.E);
        awVar.r.put("A24", "" + aVar.l());
        awVar.r.put("A17", "" + crashDetailBean.G);
        awVar.r.put("A3", "" + aVar.p());
        awVar.r.put("A16", "" + aVar.r());
        awVar.r.put("A25", "" + aVar.s());
        awVar.r.put("A14", "" + aVar.q());
        awVar.r.put("A15", "" + aVar.A());
        awVar.r.put("A13", "" + aVar.C());
        awVar.r.put("A34", "" + crashDetailBean.z);
        try {
            awVar.r.put("A26", "" + URLEncoder.encode(crashDetailBean.H, "utf-8"));
        } catch (UnsupportedEncodingException e222) {
            e222.printStackTrace();
        }
        if (crashDetailBean.b == 1) {
            awVar.r.put("A27", "" + crashDetailBean.K);
            awVar.r.put("A28", "" + crashDetailBean.J);
            awVar.r.put("A29", "" + crashDetailBean.k);
        }
        awVar.r.put("A30", "" + crashDetailBean.L);
        awVar.r.put("A18", "" + crashDetailBean.M);
        Map map = awVar.r;
        String str2 = "A36";
        StringBuilder append = new StringBuilder().append("");
        if (crashDetailBean.N) {
            z2 = false;
        } else {
            z2 = true;
        }
        map.put(str2, append.append(z2).toString());
        if (crashDetailBean.P >= 0) {
            awVar.r.put("C01", "" + crashDetailBean.P);
        }
        if (crashDetailBean.Q >= 0) {
            awVar.r.put("C02", "" + crashDetailBean.Q);
        }
        if (crashDetailBean.R != null && crashDetailBean.R.size() > 0) {
            for (Entry entry22 : crashDetailBean.R.entrySet()) {
                awVar.r.put("C03_" + ((String) entry22.getKey()), entry22.getValue());
            }
        }
        if (crashDetailBean.S != null && crashDetailBean.S.size() > 0) {
            for (Entry entry222 : crashDetailBean.S.entrySet()) {
                awVar.r.put("C04_" + ((String) entry222.getKey()), entry222.getValue());
            }
        }
        awVar.s = null;
        if (crashDetailBean.O != null && crashDetailBean.O.size() > 0) {
            awVar.s = crashDetailBean.O;
            z.a("setted message size %d", Integer.valueOf(awVar.s.size()));
        }
        String str3 = "%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d";
        Object[] objArr2 = new Object[12];
        objArr2[0] = crashDetailBean.n;
        objArr2[1] = crashDetailBean.c;
        objArr2[2] = aVar.a();
        objArr2[3] = Long.valueOf((crashDetailBean.r - crashDetailBean.M) / 1000);
        objArr2[4] = Boolean.valueOf(crashDetailBean.k);
        objArr2[5] = Boolean.valueOf(crashDetailBean.N);
        objArr2[6] = Boolean.valueOf(crashDetailBean.j);
        if (crashDetailBean.b != 1) {
            z = false;
        }
        objArr2[7] = Boolean.valueOf(z);
        objArr2[8] = Integer.valueOf(crashDetailBean.t);
        objArr2[9] = crashDetailBean.s;
        objArr2[10] = Boolean.valueOf(crashDetailBean.d);
        objArr2[11] = Integer.valueOf(awVar.r.size());
        z.c(str3, objArr2);
        return awVar;
    }

    public static ax a(Context context, List<CrashDetailBean> list, a aVar) {
        if (context == null || list == null || list.size() == 0 || aVar == null) {
            z.d("enEXPPkg args == null!", new Object[0]);
            return null;
        }
        ax axVar = new ax();
        axVar.a = new ArrayList();
        for (CrashDetailBean a : list) {
            axVar.a.add(a(context, a, aVar));
        }
        return axVar;
    }

    public static bb a(byte[] bArr) {
        try {
            bb bbVar = new bb();
            k kVar = new k(bArr);
            kVar.a("utf-8");
            bbVar.a(kVar);
            return bbVar;
        } catch (Throwable th) {
            if (!z.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static ay a(Context context, int i, byte[] bArr, a aVar, StrategyBean strategyBean) {
        if (aVar == null || strategyBean == null) {
            z.e("illigle access to create req pkg!", new Object[0]);
            return null;
        }
        try {
            ay ayVar = new ay();
            synchronized (aVar) {
                ayVar.a = aVar.c();
                ayVar.b = aVar.d();
                ayVar.c = aVar.f();
                ayVar.d = aVar.e();
                ayVar.e = aVar.B();
                ayVar.f = aVar.h();
                ayVar.g = i;
                ayVar.h = bArr == null ? "".getBytes() : bArr;
                ayVar.i = aVar.k();
                ayVar.j = aVar.l();
                ayVar.k = new HashMap();
                ayVar.l = aVar.a();
                ayVar.m = strategyBean.l;
                ayVar.o = aVar.n();
                ayVar.p = b.e(context);
                ayVar.q = System.currentTimeMillis();
                ayVar.r = aVar.p();
                ayVar.s = v.b();
                ayVar.t = aVar.r();
                ayVar.u = aVar.q();
                ayVar.v = aVar.s();
                ayVar.w = ayVar.p;
                ayVar.n = aVar.g();
            }
            if (bArr != null) {
                ayVar.h = ag.a(ayVar.h, 2, 1, strategyBean.p);
                if (ayVar.h == null) {
                    z.e("reqPkg sbuffer error!", new Object[0]);
                    return null;
                }
            }
            return ayVar;
        } catch (Throwable th) {
            if (z.b(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static byte[] a(ay ayVar) {
        try {
            e eVar = new e();
            eVar.b();
            eVar.a("utf-8");
            eVar.a(1);
            eVar.b("RqdServer");
            eVar.c("sync");
            eVar.a("detail", ayVar);
            return eVar.a();
        } catch (Throwable th) {
            if (!z.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static az a(byte[] bArr, StrategyBean strategyBean) {
        if (bArr != null) {
            try {
                az azVar;
                e eVar = new e();
                eVar.b();
                eVar.a("utf-8");
                eVar.a(bArr);
                Object b = eVar.b("detail", new az());
                if (az.class.isInstance(b)) {
                    azVar = (az) az.class.cast(b);
                } else {
                    azVar = null;
                }
                if (azVar == null || azVar.c == null || azVar.c.length <= 0) {
                    return azVar;
                }
                z.c("resp buf %d", Integer.valueOf(azVar.c.length));
                azVar.c = ag.b(azVar.c, 2, 1, StrategyBean.a);
                if (azVar.c != null) {
                    return azVar;
                }
                z.e("resp sbuffer error!", new Object[0]);
                return null;
            } catch (Throwable th) {
                if (!z.b(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] a(m mVar) {
        try {
            l lVar = new l();
            lVar.a("utf-8");
            mVar.a(lVar);
            return lVar.b();
        } catch (Throwable th) {
            if (!z.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0059 A:{Catch:{ all -> 0x00e8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005e A:{SYNTHETIC, Splitter:B:23:0x005e} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c7 A:{SYNTHETIC, Splitter:B:50:0x00c7} */
    public static com.tencent.bugly.proguard.av a(java.lang.String r9, android.content.Context r10, java.lang.String r11) {
        /*
        r2 = 1;
        r0 = 0;
        r8 = 0;
        if (r11 == 0) goto L_0x0007;
    L_0x0005:
        if (r10 != 0) goto L_0x000f;
    L_0x0007:
        r1 = "rqdp{  createZipAttachment sourcePath == null || context == null ,pls check}";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.d(r1, r2);
    L_0x000e:
        return r0;
    L_0x000f:
        r1 = "zip %s";
        r2 = new java.lang.Object[r2];
        r2[r8] = r11;
        com.tencent.bugly.proguard.z.c(r1, r2);
        r1 = new java.io.File;
        r1.<init>(r11);
        r3 = new java.io.File;
        r2 = r10.getCacheDir();
        r3.<init>(r2, r9);
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r1 = com.tencent.bugly.proguard.ag.a(r1, r3, r2);
        if (r1 != 0) goto L_0x0036;
    L_0x002e:
        r1 = "zip fail!";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.d(r1, r2);
        goto L_0x000e;
    L_0x0036:
        r1 = new java.io.ByteArrayOutputStream;
        r1.<init>();
        r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x00ea, all -> 0x00c2 }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x00ea, all -> 0x00c2 }
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = new byte[r4];	 Catch:{ Throwable -> 0x0052 }
    L_0x0044:
        r5 = r2.read(r4);	 Catch:{ Throwable -> 0x0052 }
        if (r5 <= 0) goto L_0x0074;
    L_0x004a:
        r6 = 0;
        r1.write(r4, r6, r5);	 Catch:{ Throwable -> 0x0052 }
        r1.flush();	 Catch:{ Throwable -> 0x0052 }
        goto L_0x0044;
    L_0x0052:
        r1 = move-exception;
    L_0x0053:
        r4 = com.tencent.bugly.proguard.z.a(r1);	 Catch:{ all -> 0x00e8 }
        if (r4 != 0) goto L_0x005c;
    L_0x0059:
        r1.printStackTrace();	 Catch:{ all -> 0x00e8 }
    L_0x005c:
        if (r2 == 0) goto L_0x0061;
    L_0x005e:
        r2.close();	 Catch:{ IOException -> 0x00b7 }
    L_0x0061:
        if (r3 == 0) goto L_0x000e;
    L_0x0063:
        r1 = r3.exists();
        if (r1 == 0) goto L_0x000e;
    L_0x0069:
        r1 = "del tmp";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
        r3.delete();
        goto L_0x000e;
    L_0x0074:
        r4 = r1.toByteArray();	 Catch:{ Throwable -> 0x0052 }
        r1 = "read bytes :%d";
        r5 = 1;
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x0052 }
        r6 = 0;
        r7 = r4.length;	 Catch:{ Throwable -> 0x0052 }
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ Throwable -> 0x0052 }
        r5[r6] = r7;	 Catch:{ Throwable -> 0x0052 }
        com.tencent.bugly.proguard.z.c(r1, r5);	 Catch:{ Throwable -> 0x0052 }
        r1 = new com.tencent.bugly.proguard.av;	 Catch:{ Throwable -> 0x0052 }
        r5 = 2;
        r6 = r3.getName();	 Catch:{ Throwable -> 0x0052 }
        r1.m4027init(r5, r6, r4);	 Catch:{ Throwable -> 0x0052 }
        if (r2 == 0) goto L_0x0097;
    L_0x0094:
        r2.close();	 Catch:{ IOException -> 0x00ac }
    L_0x0097:
        if (r3 == 0) goto L_0x00a9;
    L_0x0099:
        r0 = r3.exists();
        if (r0 == 0) goto L_0x00a9;
    L_0x009f:
        r0 = "del tmp";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r0, r2);
        r3.delete();
    L_0x00a9:
        r0 = r1;
        goto L_0x000e;
    L_0x00ac:
        r0 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r0);
        if (r2 != 0) goto L_0x0097;
    L_0x00b3:
        r0.printStackTrace();
        goto L_0x0097;
    L_0x00b7:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x0061;
    L_0x00be:
        r1.printStackTrace();
        goto L_0x0061;
    L_0x00c2:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x00c5:
        if (r2 == 0) goto L_0x00ca;
    L_0x00c7:
        r2.close();	 Catch:{ IOException -> 0x00dd }
    L_0x00ca:
        if (r3 == 0) goto L_0x00dc;
    L_0x00cc:
        r1 = r3.exists();
        if (r1 == 0) goto L_0x00dc;
    L_0x00d2:
        r1 = "del tmp";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
        r3.delete();
    L_0x00dc:
        throw r0;
    L_0x00dd:
        r1 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r1);
        if (r2 != 0) goto L_0x00ca;
    L_0x00e4:
        r1.printStackTrace();
        goto L_0x00ca;
    L_0x00e8:
        r0 = move-exception;
        goto L_0x00c5;
    L_0x00ea:
        r1 = move-exception;
        r2 = r0;
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.s.a(java.lang.String, android.content.Context, java.lang.String):com.tencent.bugly.proguard.av");
    }
}
