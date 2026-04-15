package com.flurry.sdk;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

public abstract class bx {
    /* access modifiers changed from: private|static|final */
    public static final String g = bx.class.getSimpleName();
    ci a;
    /* access modifiers changed from: 0000 */
    public String b;
    int c = 40000;
    /* access modifiers changed from: 0000 */
    public ck d;
    /* access modifiers changed from: 0000 */
    public long e;
    /* access modifiers changed from: 0000 */
    public boolean f;
    /* access modifiers changed from: private|final */
    public final long h = Long.MAX_VALUE;
    /* access modifiers changed from: private */
    public boolean i;
    /* access modifiers changed from: private */
    public int j;
    /* access modifiers changed from: private|final */
    public final long k = 102400;
    /* access modifiers changed from: private */
    public int l;
    /* access modifiers changed from: private */
    public boolean m;

    /* access modifiers changed from: private */
    public long a(jg jgVar) {
        List a = jgVar.a("Content-Length");
        if (!(a == null || a.isEmpty())) {
            try {
                return Long.parseLong((String) a.get(0));
            } catch (NumberFormatException e) {
                iw.a(3, g, "Downloader: could not determine content length for url: " + this.b);
            }
        }
        return -1;
    }

    private String a(int i) {
        return String.format(Locale.US, "%s__%03d", new Object[]{this.b, Integer.valueOf(i)});
    }

    static /* synthetic */ boolean a(bx bxVar) {
        return bxVar.d != null;
    }

    static /* synthetic */ void b(bx bxVar) {
        if (!bxVar.m) {
            jc jcVar = new jc();
            jcVar.e = bxVar.b;
            jcVar.f = jj.kHead;
            jcVar.a = new cb(bxVar);
            iw.a(3, g, "Downloader: requesting HTTP HEAD for url: " + bxVar.b);
            hy.a().a((Object) bxVar, (ma) jcVar);
        }
    }

    /* access modifiers changed from: private */
    public void e() {
        if (!this.m) {
            iw.a(3, g, "Downloader: Requesting file from url: " + this.b);
            jg jgVar = new jg();
            jgVar.e = this.b;
            jgVar.f = jj.kGet;
            jgVar.j = this.c;
            jgVar.h = new bz(this);
            hy.a().a((Object) this, (ma) jgVar);
        }
    }

    /* access modifiers changed from: private */
    public void f() {
        while (this.l < this.j) {
            if (!this.m) {
                String a = a(this.l);
                int i = this.l;
                String format = String.format("%s=%d-%d", new Object[]{"bytes", Long.valueOf(((long) i) * this.k), Long.valueOf(Math.min(this.e, ((long) (i + 1)) * this.k) - 1)});
                if (this.d.d(a)) {
                    iw.a(3, g, "Downloader: Skipping chunk with range:" + format + " for url: " + this.b + " chunk: " + this.l);
                    this.l++;
                } else {
                    iw.a(3, g, "Downloader: Requesting chunk with range:" + format + " for url: " + this.b + " chunk: " + this.l);
                    jg jgVar = new jg();
                    jgVar.e = this.b;
                    jgVar.f = jj.kGet;
                    jgVar.j = this.c;
                    jgVar.a("Range", format);
                    jgVar.h = new cf(this, a, format);
                    hy.a().a((Object) this, (ma) jgVar);
                    return;
                }
            }
            return;
        }
        g();
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003a  */
    private void g() {
        /*
        r8 = this;
        r3 = 0;
        r0 = 0;
        r7 = 3;
        r1 = r8.m;
        if (r1 == 0) goto L_0x0008;
    L_0x0007:
        return;
    L_0x0008:
        r1 = g;
        r2 = new java.lang.StringBuilder;
        r4 = "Downloader: assembling output file for url: ";
        r2.<init>(r4);
        r4 = r8.b;
        r2 = r2.append(r4);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r7, r1, r2);
        r4 = r8.a();	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r1 = r0;
    L_0x0023:
        r2 = r8.j;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        if (r1 >= r2) goto L_0x0092;
    L_0x0027:
        r2 = r8.m;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        if (r2 == 0) goto L_0x0057;
    L_0x002b:
        r1 = new java.io.IOException;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r2 = "Download cancelled";
        r1.<init>(r2);	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        throw r1;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
    L_0x0033:
        r1 = move-exception;
        r8.b();
        r3 = r1;
    L_0x0038:
        if (r3 != 0) goto L_0x0096;
    L_0x003a:
        r0 = g;
        r1 = new java.lang.StringBuilder;
        r2 = "Downloader: assemble succeeded for url: ";
        r1.<init>(r2);
        r2 = r8.b;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r7, r0, r1);
        r0 = 1;
        r8.f = r0;
    L_0x0053:
        r8.h();
        goto L_0x0007;
    L_0x0057:
        r5 = r8.a(r1);	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r2 = r8.d;	 Catch:{ all -> 0x00ca }
        r2 = r2.a(r5);	 Catch:{ all -> 0x00ca }
        if (r2 != 0) goto L_0x0082;
    L_0x0063:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x0078 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r4 = "Could not create reader for chunk key: ";
        r3.<init>(r4);	 Catch:{ all -> 0x0078 }
        r3 = r3.append(r5);	 Catch:{ all -> 0x0078 }
        r3 = r3.toString();	 Catch:{ all -> 0x0078 }
        r1.<init>(r3);	 Catch:{ all -> 0x0078 }
        throw r1;	 Catch:{ all -> 0x0078 }
    L_0x0078:
        r1 = move-exception;
    L_0x0079:
        com.flurry.sdk.lt.a(r2);	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        throw r1;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
    L_0x007d:
        r0 = move-exception;
        r8.b();
        throw r0;
    L_0x0082:
        r6 = r2.a;	 Catch:{ all -> 0x0078 }
        com.flurry.sdk.lt.a(r6, r4);	 Catch:{ all -> 0x0078 }
        com.flurry.sdk.lt.a(r2);	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r2 = r8.d;	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r2.c(r5);	 Catch:{ IOException -> 0x0033, all -> 0x007d }
        r1 = r1 + 1;
        goto L_0x0023;
    L_0x0092:
        r8.b();
        goto L_0x0038;
    L_0x0096:
        r1 = g;
        r2 = new java.lang.StringBuilder;
        r4 = "Downloader: assemble failed for url: ";
        r2.<init>(r4);
        r4 = r8.b;
        r2 = r2.append(r4);
        r4 = " failed with exception: ";
        r2 = r2.append(r4);
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r7, r1, r2);
    L_0x00b6:
        r1 = r8.j;
        if (r0 >= r1) goto L_0x00c6;
    L_0x00ba:
        r1 = r8.d;
        r2 = r8.a(r0);
        r1.c(r2);
        r0 = r0 + 1;
        goto L_0x00b6;
    L_0x00c6:
        r8.c();
        goto L_0x0053;
    L_0x00ca:
        r1 = move-exception;
        r2 = r3;
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.bx.g():void");
    }

    /* access modifiers changed from: private */
    public void h() {
        if (!this.m && this.a != null) {
            iw.a(3, g, "Downloader: finished -- success: " + this.f + " for url: " + this.b);
            this.a.a(this);
        }
    }

    static /* synthetic */ void k(bx bxVar) {
        int i = 1;
        int i2 = 0;
        if (!bxVar.m) {
            if (bxVar.d == null || !bxVar.i || bxVar.j <= 1) {
                i = 0;
            }
            if (i != 0) {
                while (i2 < bxVar.j) {
                    bxVar.d.d(bxVar.a(i2));
                    i2++;
                }
                bxVar.f();
                return;
            }
            bxVar.e();
        }
    }

    public abstract OutputStream a();

    public abstract void b();

    public abstract void c();
}
