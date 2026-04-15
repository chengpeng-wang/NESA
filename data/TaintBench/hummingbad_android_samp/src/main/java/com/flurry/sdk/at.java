package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class at {
    /* access modifiers changed from: private|static|final */
    public static final String b = at.class.getSimpleName();
    final cj a;
    private final Map c = new HashMap();
    private final Map d = new LinkedHashMap();
    /* access modifiers changed from: private|final */
    public final Map e = new LinkedHashMap();
    private az f = az.INIT;
    private final int g;

    at(long j) {
        int i = 1;
        kw kwVar = new kw();
        this.a = new cj(j);
        if (Runtime.getRuntime().availableProcessors() > 1) {
            i = 2;
        }
        this.g = i;
    }

    static /* synthetic */ void a(at atVar) {
        for (bg bgVar : atVar.g()) {
            if (!bw.COMPLETE.equals(atVar.c(bgVar))) {
                iw.a(3, b, "Precaching: expiring cached asset: " + bgVar.a + " asset exp: " + bgVar.f + " device epoch: " + System.currentTimeMillis());
                atVar.a(bgVar.a);
            }
        }
    }

    static /* synthetic */ void a(at atVar, bg bgVar) {
        if (bgVar != null) {
            synchronized (atVar.d) {
                atVar.d.remove(bgVar.a);
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    static /* synthetic */ void b(com.flurry.sdk.at r8) {
        /*
        r7 = 3;
        r0 = r8.e();
        if (r0 == 0) goto L_0x0078;
    L_0x0007:
        r0 = b;
        r1 = "Precaching: Download files";
        com.flurry.sdk.iw.a(r7, r0, r1);
        r1 = r8.d;
        monitor-enter(r1);
        r0 = r8.d;	 Catch:{ all -> 0x0051 }
        r0 = r0.values();	 Catch:{ all -> 0x0051 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0051 }
    L_0x001b:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0051 }
        if (r0 == 0) goto L_0x00d5;
    L_0x0021:
        r0 = r2.next();	 Catch:{ all -> 0x0051 }
        r0 = (com.flurry.sdk.bg) r0;	 Catch:{ all -> 0x0051 }
        r3 = r8.a;	 Catch:{ all -> 0x0051 }
        r4 = r0.a;	 Catch:{ all -> 0x0051 }
        r3 = r3.d(r4);	 Catch:{ all -> 0x0051 }
        if (r3 == 0) goto L_0x0054;
    L_0x0031:
        r3 = 3;
        r4 = b;	 Catch:{ all -> 0x0051 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0051 }
        r6 = "Precaching: Asset already cached.  Skipping download:";
        r5.<init>(r6);	 Catch:{ all -> 0x0051 }
        r6 = r0.a;	 Catch:{ all -> 0x0051 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r5 = r5.toString();	 Catch:{ all -> 0x0051 }
        com.flurry.sdk.iw.a(r3, r4, r5);	 Catch:{ all -> 0x0051 }
        r2.remove();	 Catch:{ all -> 0x0051 }
        r3 = com.flurry.sdk.bw.COMPLETE;	 Catch:{ all -> 0x0051 }
        b(r0, r3);	 Catch:{ all -> 0x0051 }
        goto L_0x001b;
    L_0x0051:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
        throw r0;
    L_0x0054:
        r3 = com.flurry.sdk.bw.IN_PROGRESS;	 Catch:{ all -> 0x0051 }
        r4 = r8.c(r0);	 Catch:{ all -> 0x0051 }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x0051 }
        if (r3 != 0) goto L_0x001b;
    L_0x0060:
        r3 = com.flurry.sdk.hy.a();	 Catch:{ all -> 0x0051 }
        r3 = r3.b(r8);	 Catch:{ all -> 0x0051 }
        r5 = r8.g;	 Catch:{ all -> 0x0051 }
        r5 = (long) r5;	 Catch:{ all -> 0x0051 }
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 < 0) goto L_0x0079;
    L_0x006f:
        r0 = 3;
        r2 = b;	 Catch:{ all -> 0x0051 }
        r3 = "Precaching: Download limit reached";
        com.flurry.sdk.iw.a(r0, r2, r3);	 Catch:{ all -> 0x0051 }
        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
    L_0x0078:
        return;
    L_0x0079:
        r3 = com.flurry.sdk.f.a();	 Catch:{ all -> 0x0051 }
        r4 = "precachingDownloadStarted";
        r3.a(r4);	 Catch:{ all -> 0x0051 }
        r3 = 3;
        r4 = b;	 Catch:{ all -> 0x0051 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0051 }
        r6 = "Precaching: Submitting for download: ";
        r5.<init>(r6);	 Catch:{ all -> 0x0051 }
        r6 = r0.a;	 Catch:{ all -> 0x0051 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r5 = r5.toString();	 Catch:{ all -> 0x0051 }
        com.flurry.sdk.iw.a(r3, r4, r5);	 Catch:{ all -> 0x0051 }
        r3 = new com.flurry.sdk.cq;	 Catch:{ all -> 0x0051 }
        r4 = r8.a;	 Catch:{ all -> 0x0051 }
        r5 = r0.a;	 Catch:{ all -> 0x0051 }
        r3.m3369init(r4, r5);	 Catch:{ all -> 0x0051 }
        r4 = r0.a;	 Catch:{ all -> 0x0051 }
        r3.b = r4;	 Catch:{ all -> 0x0051 }
        r4 = 40000; // 0x9c40 float:5.6052E-41 double:1.97626E-319;
        r3.c = r4;	 Catch:{ all -> 0x0051 }
        r4 = r8.a;	 Catch:{ all -> 0x0051 }
        r3.d = r4;	 Catch:{ all -> 0x0051 }
        r4 = new com.flurry.sdk.ax;	 Catch:{ all -> 0x0051 }
        r4.m3352init(r8, r0);	 Catch:{ all -> 0x0051 }
        r3.a = r4;	 Catch:{ all -> 0x0051 }
        r4 = com.flurry.sdk.hz.a;	 Catch:{ all -> 0x0051 }
        r5 = new com.flurry.sdk.by;	 Catch:{ all -> 0x0051 }
        r5.m3357init(r3);	 Catch:{ all -> 0x0051 }
        r4.b(r5);	 Catch:{ all -> 0x0051 }
        r4 = r8.e;	 Catch:{ all -> 0x0051 }
        monitor-enter(r4);	 Catch:{ all -> 0x0051 }
        r5 = r8.e;	 Catch:{ all -> 0x00d2 }
        r6 = r0.a;	 Catch:{ all -> 0x00d2 }
        r5.put(r6, r3);	 Catch:{ all -> 0x00d2 }
        monitor-exit(r4);	 Catch:{ all -> 0x00d2 }
        r3 = com.flurry.sdk.bw.IN_PROGRESS;	 Catch:{ all -> 0x0051 }
        b(r0, r3);	 Catch:{ all -> 0x0051 }
        goto L_0x001b;
    L_0x00d2:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x00d2 }
        throw r0;	 Catch:{ all -> 0x0051 }
    L_0x00d5:
        monitor-exit(r1);	 Catch:{ all -> 0x0051 }
        r0 = b;
        r1 = "Precaching: No more files to download";
        com.flurry.sdk.iw.a(r7, r0, r1);
        goto L_0x0078;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.at.b(com.flurry.sdk.at):void");
    }

    private void b(bg bgVar) {
        if (bgVar != null) {
            bw c = c(bgVar);
            if (!bw.COMPLETE.equals(c)) {
                if (bw.IN_PROGRESS.equals(c) || bw.QUEUED.equals(c)) {
                    synchronized (this.d) {
                        if (!this.d.containsKey(bgVar.a)) {
                            this.d.put(bgVar.a, bgVar);
                        }
                    }
                } else {
                    iw.a(3, b, "Precaching: Queueing asset:" + bgVar.a);
                    f.a().a("precachingDownloadRequested");
                    b(bgVar, bw.QUEUED);
                    synchronized (this.d) {
                        this.d.put(bgVar.a, bgVar);
                    }
                }
                hz.a.b(new aw(this));
            }
        }
    }

    /* access modifiers changed from: private|static */
    public static void b(bg bgVar, bw bwVar) {
        if (bgVar != null && bwVar != null && !bwVar.equals(bgVar.a())) {
            iw.a(3, b, "Asset status changed for asset:" + bgVar.a + " from:" + bgVar.a() + " to:" + bwVar);
            bgVar.a(bwVar);
            bf bfVar = new bf();
            bfVar.a = bgVar.a;
            bfVar.b = bwVar;
            bfVar.a();
        }
    }

    private bg c(String str) {
        if (a() || TextUtils.isEmpty(str)) {
            return null;
        }
        bg bgVar;
        synchronized (this.c) {
            bgVar = (bg) this.c.get(str);
        }
        if (bgVar != null) {
            if (bgVar.b()) {
                iw.a(3, b, "Precaching: expiring cached asset: " + bgVar.a + " asset exp: " + bgVar.f + " device epoch" + System.currentTimeMillis());
                a(bgVar.a);
                bgVar = null;
            } else {
                c(bgVar);
                bgVar.c();
            }
        }
        return bgVar;
    }

    private bw c(bg bgVar) {
        if (bgVar == null) {
            return bw.NONE;
        }
        if (bgVar.b()) {
            return bw.NONE;
        }
        if (bw.COMPLETE.equals(bgVar.a()) && !this.a.d(bgVar.a)) {
            b(bgVar, bw.EVICTED);
        }
        return bgVar.a();
    }

    private synchronized boolean e() {
        return az.ACTIVE.equals(this.f);
    }

    private synchronized boolean f() {
        return az.PAUSED.equals(this.f);
    }

    private List g() {
        ArrayList arrayList;
        synchronized (this.c) {
            arrayList = new ArrayList(this.c.values());
        }
        return arrayList;
    }

    public final synchronized void a(bg bgVar) {
        if (bgVar != null) {
            if (!(TextUtils.isEmpty(bgVar.a) || this.c.containsKey(bgVar.a))) {
                iw.a(3, b, "Precaching: adding cached asset info from persisted storage: " + bgVar.a + " asset exp: " + bgVar.f + " saved time: " + bgVar.c);
                synchronized (this.c) {
                    this.c.put(bgVar.a, bgVar);
                }
            }
        }
    }

    public final void a(String str) {
        if (!a() && !TextUtils.isEmpty(str)) {
            synchronized (this.c) {
                this.c.remove(str);
            }
            this.a.c(str);
        }
    }

    /* access modifiers changed from: final|declared_synchronized */
    public final synchronized boolean a() {
        boolean z;
        z = (az.ACTIVE.equals(this.f) || az.PAUSED.equals(this.f)) ? false : true;
        return z;
    }

    public final boolean a(String str, cr crVar, long j) {
        if (a() || TextUtils.isEmpty(str) || crVar == null) {
            return false;
        }
        if (!cr.IMAGE.equals(crVar) && !cr.VIDEO.equals(crVar)) {
            return false;
        }
        bg c = c(str);
        if (c == null) {
            c = new bg(str, crVar, j);
            synchronized (this.c) {
                this.c.put(c.a, c);
            }
            b(c);
        } else if (!bw.COMPLETE.equals(c(c))) {
            b(c);
        }
        return true;
    }

    public final bw b(String str) {
        return a() ? bw.NONE : c(c(str));
    }

    public final synchronized void b() {
        if (!e()) {
            iw.a(3, b, "Precaching: Starting AssetCache");
            this.a.a();
            hz.a.b(new au(this));
            this.f = az.ACTIVE;
        }
    }

    public final synchronized void c() {
        if (!a()) {
            if (f()) {
                iw.a(3, b, "Precaching: Resuming AssetCache");
                hz.a.b(new av(this));
                this.f = az.ACTIVE;
            }
        }
    }
}
