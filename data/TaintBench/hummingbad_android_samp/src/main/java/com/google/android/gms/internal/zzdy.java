package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;

@zzgk
public final class zzdy {
    private final Context mContext;
    private final zzeh zzow;
    private final Object zzpc = new Object();
    private final AdRequestInfoParcel zzyd;
    private final zzea zzye;
    private final boolean zzyf;
    private boolean zzyg = false;
    private zzed zzyh;

    public zzdy(Context context, AdRequestInfoParcel adRequestInfoParcel, zzeh zzeh, zzea zzea, boolean z) {
        this.mContext = context;
        this.zzyd = adRequestInfoParcel;
        this.zzow = zzeh;
        this.zzye = zzea;
        this.zzyf = z;
    }

    public void cancel() {
        synchronized (this.zzpc) {
            this.zzyg = true;
            if (this.zzyh != null) {
                this.zzyh.cancel();
            }
        }
    }

    /* JADX WARNING: Missing block: B:15:0x009f, code skipped:
            r4 = r22.zzyh.zza(r23, r25);
     */
    /* JADX WARNING: Missing block: B:16:0x00ad, code skipped:
            if (r4.zzyP != 0) goto L_0x00f3;
     */
    /* JADX WARNING: Missing block: B:17:0x00af, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Adapter succeeded.");
            r27.zzd("mediation_network_succeed", r6);
     */
    /* JADX WARNING: Missing block: B:18:0x00bf, code skipped:
            if (r16.isEmpty() != false) goto L_0x00d0;
     */
    /* JADX WARNING: Missing block: B:19:0x00c1, code skipped:
            r0 = r27;
            r0.zzd("mediation_networks_fail", android.text.TextUtils.join(",", r16));
     */
    /* JADX WARNING: Missing block: B:20:0x00d0, code skipped:
            r27.zza(r20, "mls");
            r27.zza(r17, "ttm");
     */
    /* JADX WARNING: Missing block: B:25:0x00f3, code skipped:
            r16.add(r6);
            r27.zza(r20, "mlf");
     */
    /* JADX WARNING: Missing block: B:26:0x0109, code skipped:
            if (r4.zzyR == null) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:27:0x010b, code skipped:
            com.google.android.gms.internal.zzhu.zzHK.post(new com.google.android.gms.internal.zzdy.AnonymousClass1(r22));
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            return r4;
     */
    public com.google.android.gms.internal.zzee zza(long r23, long r25, com.google.android.gms.internal.zzcd r27) {
        /*
        r22 = this;
        r4 = "Starting mediation.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r4);
        r16 = new java.util.ArrayList;
        r16.<init>();
        r17 = r27.zzdl();
        r0 = r22;
        r4 = r0.zzye;
        r4 = r4.zzyu;
        r18 = r4.iterator();
    L_0x0018:
        r4 = r18.hasNext();
        if (r4 == 0) goto L_0x0119;
    L_0x001e:
        r9 = r18.next();
        r9 = (com.google.android.gms.internal.zzdz) r9;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Trying mediation network: ";
        r4 = r4.append(r5);
        r5 = r9.zzyl;
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaD(r4);
        r4 = r9.zzym;
        r19 = r4.iterator();
    L_0x0042:
        r4 = r19.hasNext();
        if (r4 == 0) goto L_0x0018;
    L_0x0048:
        r6 = r19.next();
        r6 = (java.lang.String) r6;
        r20 = r27.zzdl();
        r0 = r22;
        r0 = r0.zzpc;
        r21 = r0;
        monitor-enter(r21);
        r0 = r22;
        r4 = r0.zzyg;	 Catch:{ all -> 0x00f0 }
        if (r4 == 0) goto L_0x0067;
    L_0x005f:
        r4 = new com.google.android.gms.internal.zzee;	 Catch:{ all -> 0x00f0 }
        r5 = -1;
        r4.m1513init(r5);	 Catch:{ all -> 0x00f0 }
        monitor-exit(r21);	 Catch:{ all -> 0x00f0 }
    L_0x0066:
        return r4;
    L_0x0067:
        r4 = new com.google.android.gms.internal.zzed;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r5 = r0.mContext;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r7 = r0.zzow;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r8 = r0.zzye;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r10 = r0.zzyd;	 Catch:{ all -> 0x00f0 }
        r10 = r10.zzDy;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r11 = r0.zzyd;	 Catch:{ all -> 0x00f0 }
        r11 = r11.zzqf;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r12 = r0.zzyd;	 Catch:{ all -> 0x00f0 }
        r12 = r12.zzqb;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r13 = r0.zzyf;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r14 = r0.zzyd;	 Catch:{ all -> 0x00f0 }
        r14 = r14.zzqt;	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r15 = r0.zzyd;	 Catch:{ all -> 0x00f0 }
        r15 = r15.zzqv;	 Catch:{ all -> 0x00f0 }
        r4.m3666init(r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r0.zzyh = r4;	 Catch:{ all -> 0x00f0 }
        monitor-exit(r21);	 Catch:{ all -> 0x00f0 }
        r0 = r22;
        r4 = r0.zzyh;
        r0 = r23;
        r2 = r25;
        r4 = r4.zza(r0, r2);
        r5 = r4.zzyP;
        if (r5 != 0) goto L_0x00f3;
    L_0x00af:
        r5 = "Adapter succeeded.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r5);
        r5 = "mediation_network_succeed";
        r0 = r27;
        r0.zzd(r5, r6);
        r5 = r16.isEmpty();
        if (r5 != 0) goto L_0x00d0;
    L_0x00c1:
        r5 = "mediation_networks_fail";
        r6 = ",";
        r0 = r16;
        r6 = android.text.TextUtils.join(r6, r0);
        r0 = r27;
        r0.zzd(r5, r6);
    L_0x00d0:
        r5 = 1;
        r5 = new java.lang.String[r5];
        r6 = 0;
        r7 = "mls";
        r5[r6] = r7;
        r0 = r27;
        r1 = r20;
        r0.zza(r1, r5);
        r5 = 1;
        r5 = new java.lang.String[r5];
        r6 = 0;
        r7 = "ttm";
        r5[r6] = r7;
        r0 = r27;
        r1 = r17;
        r0.zza(r1, r5);
        goto L_0x0066;
    L_0x00f0:
        r4 = move-exception;
        monitor-exit(r21);	 Catch:{ all -> 0x00f0 }
        throw r4;
    L_0x00f3:
        r0 = r16;
        r0.add(r6);
        r5 = 1;
        r5 = new java.lang.String[r5];
        r6 = 0;
        r7 = "mlf";
        r5[r6] = r7;
        r0 = r27;
        r1 = r20;
        r0.zza(r1, r5);
        r5 = r4.zzyR;
        if (r5 == 0) goto L_0x0042;
    L_0x010b:
        r5 = com.google.android.gms.internal.zzhu.zzHK;
        r6 = new com.google.android.gms.internal.zzdy$1;
        r0 = r22;
        r6.m1506init(r4);
        r5.post(r6);
        goto L_0x0042;
    L_0x0119:
        r4 = r16.isEmpty();
        if (r4 != 0) goto L_0x012e;
    L_0x011f:
        r4 = "mediation_networks_fail";
        r5 = ",";
        r0 = r16;
        r5 = android.text.TextUtils.join(r5, r0);
        r0 = r27;
        r0.zzd(r4, r5);
    L_0x012e:
        r4 = new com.google.android.gms.internal.zzee;
        r5 = 1;
        r4.m1513init(r5);
        goto L_0x0066;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdy.zza(long, long, com.google.android.gms.internal.zzcd):com.google.android.gms.internal.zzee");
    }
}
