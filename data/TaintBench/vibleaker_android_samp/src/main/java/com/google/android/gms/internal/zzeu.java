package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@zzhb
public class zzeu implements zzem {
    private final Context mContext;
    private final zzeo zzCf;
    private final AdRequestInfoParcel zzCu;
    /* access modifiers changed from: private|final */
    public final long zzCv;
    /* access modifiers changed from: private|final */
    public final long zzCw;
    private final int zzCx;
    /* access modifiers changed from: private */
    public boolean zzCy = false;
    /* access modifiers changed from: private|final */
    public final Map<zzjg<zzes>, zzer> zzCz = new HashMap();
    /* access modifiers changed from: private|final */
    public final Object zzpV = new Object();
    private final zzex zzpn;
    private final boolean zzsA;
    private final boolean zzuS;

    public zzeu(Context context, AdRequestInfoParcel adRequestInfoParcel, zzex zzex, zzeo zzeo, boolean z, boolean z2, long j, long j2, int i) {
        this.mContext = context;
        this.zzCu = adRequestInfoParcel;
        this.zzpn = zzex;
        this.zzCf = zzeo;
        this.zzsA = z;
        this.zzuS = z2;
        this.zzCv = j;
        this.zzCw = j2;
        this.zzCx = i;
    }

    private void zza(final zzjg<zzes> zzjg) {
        zzir.zzMc.post(new Runnable() {
            public void run() {
                for (zzjg zzjg : zzeu.this.zzCz.keySet()) {
                    if (zzjg != zzjg) {
                        ((zzer) zzeu.this.zzCz.get(zzjg)).cancel();
                    }
                }
            }
        });
    }

    /* JADX WARNING: Missing block: B:8:0x0010, code skipped:
            r2 = r5.iterator();
     */
    /* JADX WARNING: Missing block: B:10:0x0018, code skipped:
            if (r2.hasNext() == false) goto L_0x003a;
     */
    /* JADX WARNING: Missing block: B:11:0x001a, code skipped:
            r0 = (com.google.android.gms.internal.zzjg) r2.next();
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r1 = (com.google.android.gms.internal.zzes) r0.get();
     */
    /* JADX WARNING: Missing block: B:14:0x0026, code skipped:
            if (r1 == null) goto L_0x0014;
     */
    /* JADX WARNING: Missing block: B:16:0x002a, code skipped:
            if (r1.zzCo != 0) goto L_0x0014;
     */
    /* JADX WARNING: Missing block: B:17:0x002c, code skipped:
            zza(r0);
     */
    /* JADX WARNING: Missing block: B:18:0x0030, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:19:0x0031, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Exception while processing an adapter; continuing with other adapters", r0);
     */
    /* JADX WARNING: Missing block: B:24:0x003a, code skipped:
            zza(null);
     */
    /* JADX WARNING: Missing block: B:35:?, code skipped:
            return new com.google.android.gms.internal.zzes(1);
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            return r1;
     */
    private com.google.android.gms.internal.zzes zzd(java.util.List<com.google.android.gms.internal.zzjg<com.google.android.gms.internal.zzes>> r5) {
        /*
        r4 = this;
        r2 = r4.zzpV;
        monitor-enter(r2);
        r0 = r4.zzCy;	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x000f;
    L_0x0007:
        r1 = new com.google.android.gms.internal.zzes;	 Catch:{ all -> 0x0037 }
        r0 = -1;
        r1.m3163init(r0);	 Catch:{ all -> 0x0037 }
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
    L_0x000e:
        return r1;
    L_0x000f:
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        r2 = r5.iterator();
    L_0x0014:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x003a;
    L_0x001a:
        r0 = r2.next();
        r0 = (com.google.android.gms.internal.zzjg) r0;
        r1 = r0.get();	 Catch:{ InterruptedException | ExecutionException -> 0x0030, ExecutionException -> 0x0045 }
        r1 = (com.google.android.gms.internal.zzes) r1;	 Catch:{ InterruptedException | ExecutionException -> 0x0030, ExecutionException -> 0x0045 }
        if (r1 == 0) goto L_0x0014;
    L_0x0028:
        r3 = r1.zzCo;	 Catch:{ InterruptedException | ExecutionException -> 0x0030, ExecutionException -> 0x0045 }
        if (r3 != 0) goto L_0x0014;
    L_0x002c:
        r4.zza(r0);	 Catch:{ InterruptedException | ExecutionException -> 0x0030, ExecutionException -> 0x0045 }
        goto L_0x000e;
    L_0x0030:
        r0 = move-exception;
    L_0x0031:
        r1 = "Exception while processing an adapter; continuing with other adapters";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0);
        goto L_0x0014;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        throw r0;
    L_0x003a:
        r0 = 0;
        r4.zza(r0);
        r1 = new com.google.android.gms.internal.zzes;
        r0 = 1;
        r1.m3163init(r0);
        goto L_0x000e;
    L_0x0045:
        r0 = move-exception;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeu.zzd(java.util.List):com.google.android.gms.internal.zzes");
    }

    /* JADX WARNING: Missing block: B:8:0x0010, code skipped:
            r4 = -1;
            r3 = null;
            r2 = null;
     */
    /* JADX WARNING: Missing block: B:9:0x001b, code skipped:
            if (r15.zzCf.zzBY == -1) goto L_0x007e;
     */
    /* JADX WARNING: Missing block: B:10:0x001d, code skipped:
            r0 = r15.zzCf.zzBY;
     */
    /* JADX WARNING: Missing block: B:11:0x0021, code skipped:
            r8 = r16.iterator();
     */
    /* JADX WARNING: Missing block: B:35:0x007e, code skipped:
            r0 = 10000;
     */
    private com.google.android.gms.internal.zzes zze(java.util.List<com.google.android.gms.internal.zzjg<com.google.android.gms.internal.zzes>> r16) {
        /*
        r15 = this;
        r1 = r15.zzpV;
        monitor-enter(r1);
        r0 = r15.zzCy;	 Catch:{ all -> 0x007b }
        if (r0 == 0) goto L_0x000f;
    L_0x0007:
        r2 = new com.google.android.gms.internal.zzes;	 Catch:{ all -> 0x007b }
        r0 = -1;
        r2.m3163init(r0);	 Catch:{ all -> 0x007b }
        monitor-exit(r1);	 Catch:{ all -> 0x007b }
    L_0x000e:
        return r2;
    L_0x000f:
        monitor-exit(r1);	 Catch:{ all -> 0x007b }
        r4 = -1;
        r3 = 0;
        r2 = 0;
        r0 = r15.zzCf;
        r0 = r0.zzBY;
        r6 = -1;
        r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x007e;
    L_0x001d:
        r0 = r15.zzCf;
        r0 = r0.zzBY;
    L_0x0021:
        r8 = r16.iterator();
        r6 = r0;
    L_0x0026:
        r0 = r8.hasNext();
        if (r0 == 0) goto L_0x00b4;
    L_0x002c:
        r0 = r8.next();
        r0 = (com.google.android.gms.internal.zzjg) r0;
        r1 = com.google.android.gms.ads.internal.zzr.zzbG();
        r10 = r1.currentTimeMillis();
        r12 = 0;
        r1 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r1 != 0) goto L_0x0081;
    L_0x0040:
        r1 = r0.isDone();	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        if (r1 == 0) goto L_0x0081;
    L_0x0046:
        r1 = r0.get();	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        r1 = (com.google.android.gms.internal.zzes) r1;	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
    L_0x004c:
        if (r1 == 0) goto L_0x00c7;
    L_0x004e:
        r5 = r1.zzCo;	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        if (r5 != 0) goto L_0x00c7;
    L_0x0052:
        r5 = r1.zzCt;	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        if (r5 == 0) goto L_0x00c7;
    L_0x0056:
        r9 = r5.zzeD();	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        if (r9 <= r4) goto L_0x00c7;
    L_0x005c:
        r2 = r5.zzeD();	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        r14 = r1;
        r1 = r0;
        r0 = r14;
    L_0x0063:
        r3 = com.google.android.gms.ads.internal.zzr.zzbG();
        r4 = r3.currentTimeMillis();
        r4 = r4 - r10;
        r4 = r6 - r4;
        r6 = 0;
        r4 = java.lang.Math.max(r4, r6);
        r3 = r1;
        r14 = r0;
        r0 = r4;
        r4 = r2;
        r2 = r14;
    L_0x0079:
        r6 = r0;
        goto L_0x0026;
    L_0x007b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x007b }
        throw r0;
    L_0x007e:
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        goto L_0x0021;
    L_0x0081:
        r1 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        r1 = r0.get(r6, r1);	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        r1 = (com.google.android.gms.internal.zzes) r1;	 Catch:{ InterruptedException -> 0x00c1, ExecutionException -> 0x00c3, RemoteException | InterruptedException | ExecutionException | TimeoutException -> 0x008a, TimeoutException -> 0x00c5 }
        goto L_0x004c;
    L_0x008a:
        r0 = move-exception;
    L_0x008b:
        r1 = "Exception while processing an adapter; continuing with other adapters";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0);	 Catch:{ all -> 0x00a2 }
        r0 = com.google.android.gms.ads.internal.zzr.zzbG();
        r0 = r0.currentTimeMillis();
        r0 = r0 - r10;
        r0 = r6 - r0;
        r6 = 0;
        r0 = java.lang.Math.max(r0, r6);
        goto L_0x0079;
    L_0x00a2:
        r0 = move-exception;
        r1 = com.google.android.gms.ads.internal.zzr.zzbG();
        r2 = r1.currentTimeMillis();
        r2 = r2 - r10;
        r2 = r6 - r2;
        r4 = 0;
        java.lang.Math.max(r2, r4);
        throw r0;
    L_0x00b4:
        r15.zza(r3);
        if (r2 != 0) goto L_0x000e;
    L_0x00b9:
        r2 = new com.google.android.gms.internal.zzes;
        r0 = 1;
        r2.m3163init(r0);
        goto L_0x000e;
    L_0x00c1:
        r0 = move-exception;
        goto L_0x008b;
    L_0x00c3:
        r0 = move-exception;
        goto L_0x008b;
    L_0x00c5:
        r0 = move-exception;
        goto L_0x008b;
    L_0x00c7:
        r0 = r2;
        r1 = r3;
        r2 = r4;
        goto L_0x0063;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeu.zze(java.util.List):com.google.android.gms.internal.zzes");
    }

    public void cancel() {
        synchronized (this.zzpV) {
            this.zzCy = true;
            for (zzer cancel : this.zzCz.values()) {
                cancel.cancel();
            }
        }
    }

    public zzes zzc(List<zzen> list) {
        zzb.zzaI("Starting mediation.");
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        List arrayList = new ArrayList();
        for (zzen zzen : list) {
            zzb.zzaJ("Trying mediation network: " + zzen.zzBA);
            for (String zzer : zzen.zzBB) {
                final zzer zzer2 = new zzer(this.mContext, zzer, this.zzpn, this.zzCf, zzen, this.zzCu.zzHt, this.zzCu.zzrp, this.zzCu.zzrl, this.zzsA, this.zzuS, this.zzCu.zzrD, this.zzCu.zzrH);
                zzjg zza = zziq.zza(newCachedThreadPool, new Callable<zzes>() {
                    /* renamed from: zzeE */
                    public zzes call() throws Exception {
                        synchronized (zzeu.this.zzpV) {
                            if (zzeu.this.zzCy) {
                                return null;
                            }
                            return zzer2.zza(zzeu.this.zzCv, zzeu.this.zzCw);
                        }
                    }
                });
                this.zzCz.put(zza, zzer2);
                arrayList.add(zza);
            }
        }
        switch (this.zzCx) {
            case 2:
                return zze(arrayList);
            default:
                return zzd(arrayList);
        }
    }
}
