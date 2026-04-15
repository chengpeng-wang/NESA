package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.CrashHandleCallback;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.z;
import com.umeng.analytics.a;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: BUGLY */
public class b {
    protected final Context a;
    protected final w b;
    protected final q c;
    protected final c d;

    public b(Context context, w wVar, q qVar, c cVar, CrashHandleCallback crashHandleCallback) {
        this.a = context;
        this.b = wVar;
        this.c = qVar;
        this.d = cVar;
    }

    /* access modifiers changed from: protected */
    public List<a> a(List<a> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        long b = ag.b();
        ArrayList arrayList = new ArrayList();
        for (a aVar : list) {
            if (aVar.d && aVar.b < b - a.m) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c4  */
    public com.tencent.bugly.crashreport.crash.CrashDetailBean a(java.util.List<com.tencent.bugly.crashreport.crash.a> r12, com.tencent.bugly.crashreport.crash.CrashDetailBean r13) {
        /*
        r11 = this;
        r3 = 0;
        if (r12 == 0) goto L_0x0009;
    L_0x0003:
        r0 = r12.size();
        if (r0 != 0) goto L_0x000b;
    L_0x0009:
        r1 = r13;
    L_0x000a:
        return r1;
    L_0x000b:
        r1 = 0;
        r2 = new java.util.ArrayList;
        r0 = 10;
        r2.<init>(r0);
        r4 = r12.iterator();
    L_0x0017:
        r0 = r4.hasNext();
        if (r0 == 0) goto L_0x002b;
    L_0x001d:
        r0 = r4.next();
        r0 = (com.tencent.bugly.crashreport.crash.a) r0;
        r5 = r0.e;
        if (r5 == 0) goto L_0x0017;
    L_0x0027:
        r2.add(r0);
        goto L_0x0017;
    L_0x002b:
        r0 = r2.size();
        if (r0 <= 0) goto L_0x0163;
    L_0x0031:
        r0 = r11.c;
        r4 = r0.b(r2);
        if (r4 == 0) goto L_0x0163;
    L_0x0039:
        r0 = r4.size();
        if (r0 <= 0) goto L_0x0163;
    L_0x003f:
        java.util.Collections.sort(r4);
        r2 = r3;
    L_0x0043:
        r0 = r4.size();
        if (r2 >= r0) goto L_0x00ad;
    L_0x0049:
        r0 = r4.get(r2);
        r0 = (com.tencent.bugly.crashreport.crash.CrashDetailBean) r0;
        if (r2 != 0) goto L_0x0056;
    L_0x0051:
        r1 = r2 + 1;
        r2 = r1;
        r1 = r0;
        goto L_0x0043;
    L_0x0056:
        r5 = r0.s;
        if (r5 != 0) goto L_0x005c;
    L_0x005a:
        r0 = r1;
        goto L_0x0051;
    L_0x005c:
        r0 = r0.s;
        r5 = "\n";
        r5 = r0.split(r5);
        if (r5 != 0) goto L_0x0068;
    L_0x0066:
        r0 = r1;
        goto L_0x0051;
    L_0x0068:
        r6 = r5.length;
        r0 = r3;
    L_0x006a:
        if (r0 >= r6) goto L_0x0160;
    L_0x006c:
        r7 = r5[r0];
        r8 = r1.s;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "";
        r9 = r9.append(r10);
        r9 = r9.append(r7);
        r9 = r9.toString();
        r8 = r8.contains(r9);
        if (r8 != 0) goto L_0x00aa;
    L_0x0089:
        r8 = r1.t;
        r8 = r8 + 1;
        r1.t = r8;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.s;
        r8 = r8.append(r9);
        r7 = r8.append(r7);
        r8 = "\n";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r1.s = r7;
    L_0x00aa:
        r0 = r0 + 1;
        goto L_0x006a;
    L_0x00ad:
        r0 = r1;
    L_0x00ae:
        if (r0 != 0) goto L_0x015d;
    L_0x00b0:
        r0 = 1;
        r13.j = r0;
        r13.t = r3;
        r0 = "";
        r13.s = r0;
        r1 = r13;
    L_0x00ba:
        r2 = r12.iterator();
    L_0x00be:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x0113;
    L_0x00c4:
        r0 = r2.next();
        r0 = (com.tencent.bugly.crashreport.crash.a) r0;
        r3 = r0.e;
        if (r3 != 0) goto L_0x00be;
    L_0x00ce:
        r3 = r0.d;
        if (r3 != 0) goto L_0x00be;
    L_0x00d2:
        r3 = r1.s;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "";
        r4 = r4.append(r5);
        r5 = r0.b;
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3 = r3.contains(r4);
        if (r3 != 0) goto L_0x00be;
    L_0x00ef:
        r3 = r1.t;
        r3 = r3 + 1;
        r1.t = r3;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r1.s;
        r3 = r3.append(r4);
        r4 = r0.b;
        r0 = r3.append(r4);
        r3 = "\n";
        r0 = r0.append(r3);
        r0 = r0.toString();
        r1.s = r0;
        goto L_0x00be;
    L_0x0113:
        r2 = r1.r;
        r4 = r13.r;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x000a;
    L_0x011b:
        r0 = r1.s;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "";
        r2 = r2.append(r3);
        r3 = r13.r;
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0 = r0.contains(r2);
        if (r0 != 0) goto L_0x000a;
    L_0x0138:
        r0 = r1.t;
        r0 = r0 + 1;
        r1.t = r0;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = r1.s;
        r0 = r0.append(r2);
        r2 = r13.r;
        r0 = r0.append(r2);
        r2 = "\n";
        r0 = r0.append(r2);
        r0 = r0.toString();
        r1.s = r0;
        goto L_0x000a;
    L_0x015d:
        r1 = r0;
        goto L_0x00ba;
    L_0x0160:
        r0 = r1;
        goto L_0x0051;
    L_0x0163:
        r0 = r1;
        goto L_0x00ae;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.a(java.util.List, com.tencent.bugly.crashreport.crash.CrashDetailBean):com.tencent.bugly.crashreport.crash.CrashDetailBean");
    }

    public boolean a(CrashDetailBean crashDetailBean) {
        this.d.a(crashDetailBean);
        z.b("[crash] a crash occur, handling...", new Object[0]);
        List<a> c = this.c.c();
        List list = null;
        if (c != null && c.size() > 0) {
            List arrayList = new ArrayList(10);
            List arrayList2 = new ArrayList(10);
            arrayList.addAll(a((List) c));
            c.removeAll(arrayList);
            if (!CrashReport.isDebug) {
                int i = 0;
                for (a aVar : c) {
                    if (crashDetailBean.u.equals(aVar.c)) {
                        if (aVar.e) {
                            i = true;
                        }
                        arrayList2.add(aVar);
                    }
                    i = i;
                }
                if (i != 0 || arrayList2.size() + 1 >= 5) {
                    z.a("same crash occur too much do merged!", new Object[0]);
                    CrashDetailBean a = a(arrayList2, crashDetailBean);
                    a.a = -1;
                    this.c.b(a);
                    arrayList.addAll(arrayList2);
                    this.c.c(arrayList);
                    z.b("[crash] save crash success. this device crash many times, won't upload crashes immediately", new Object[0]);
                    return true;
                }
            }
            list = arrayList;
        }
        this.c.b(crashDetailBean);
        this.c.c(list);
        z.b("[crash] save crash success", new Object[0]);
        return false;
    }

    public void a(CrashDetailBean crashDetailBean, long j) {
        z.a("try to upload right now", new Object[0]);
        List arrayList = new ArrayList();
        arrayList.add(crashDetailBean);
        this.b.a(arrayList, this.d, j);
    }

    public void a(CrashDetailBean crashDetailBean, CrashHandleCallback crashHandleCallback) {
        if (crashDetailBean == null || crashHandleCallback == null) {
            z.d("handle user callback args are null! %s %s", "" + crashDetailBean, "" + crashHandleCallback);
            return;
        }
        try {
            int i;
            z.a("start notify crashHandleCallback!", new Object[0]);
            int i2 = crashDetailBean.b == 0 ? 0 : 1;
            switch (crashDetailBean.b) {
                case 0:
                    i = 0;
                    break;
                case 1:
                    i = 2;
                    break;
                case 2:
                    i = 1;
                    break;
                case 3:
                    i = 4;
                    break;
                case 4:
                    i = 3;
                    break;
                case 5:
                    i = 5;
                    break;
                case 6:
                    i = 6;
                    break;
                default:
                    i = i2;
                    break;
            }
            Map onCrashHandleStart = crashHandleCallback.onCrashHandleStart(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q);
            if (onCrashHandleStart != null && onCrashHandleStart.size() > 0) {
                crashDetailBean.O = new LinkedHashMap(onCrashHandleStart.size());
                for (Entry entry : onCrashHandleStart.entrySet()) {
                    if (!ag.b((String) entry.getKey())) {
                        Object obj;
                        String str = (String) entry.getKey();
                        if (str.length() > 100) {
                            str = str.substring(0, 100);
                            z.d("setted key length is over limit %d substring to %s", Integer.valueOf(100), str);
                        }
                        String str2 = str;
                        str = "";
                        if (ag.b((String) entry.getValue()) || ((String) entry.getValue()).length() <= 30000) {
                            obj = "" + ((String) entry.getValue());
                        } else {
                            obj = ((String) entry.getValue()).substring(((String) entry.getValue()).length() - 30000);
                            z.d("setted %s value length is over limit %d substring", str2, Integer.valueOf(30000));
                        }
                        crashDetailBean.O.put(str2, obj);
                        z.a("add setted key %s value size:%d", str2, Integer.valueOf(obj.length()));
                    }
                }
            }
            z.a("start notify crashHandleCallback2GetBytes!", new Object[0]);
            crashDetailBean.T = crashHandleCallback.onCrashHandleStart2GetExtraDatas(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q);
            if (crashDetailBean.T != null) {
                if (crashDetailBean.T.length > 30000) {
                    z.d("extra bytes size %d is over limit %d will drop over part", Integer.valueOf(crashDetailBean.T.length), Integer.valueOf(30000));
                    byte[] bArr = new byte[30000];
                    for (int i3 = 0; i3 < 30000; i3++) {
                        bArr[i3] = crashDetailBean.T[i3];
                    }
                }
                z.a("add extra bytes %d ", Integer.valueOf(crashDetailBean.T.length));
            }
        } catch (Throwable th) {
            z.d("crash handle callback somthing wrong! %s", th.getClass().getName());
            if (!z.a(th)) {
                th.printStackTrace();
            }
        }
    }
}
