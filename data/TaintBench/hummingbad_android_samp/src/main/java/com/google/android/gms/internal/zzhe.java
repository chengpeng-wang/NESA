package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzhj.zza;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Future;

@zzgk
public class zzhe extends zzhq implements zzhd {
    private final Context mContext;
    private final zza zzCF;
    private final HashSet<String> zzGA = new HashSet();
    /* access modifiers changed from: private|final */
    public final zzgx zzGB;
    private final String zzGh;
    private final ArrayList<Future> zzGy = new ArrayList();
    private final ArrayList<String> zzGz = new ArrayList();
    private final Object zzpc = new Object();

    public zzhe(Context context, String str, zza zza, zzgx zzgx) {
        this.mContext = context;
        this.zzGh = str;
        this.zzCF = zza;
        this.zzGB = zzgx;
    }

    /* JADX WARNING: Missing block: B:16:?, code skipped:
            return;
     */
    private void zzk(java.lang.String r10, java.lang.String r11) {
        /*
        r9 = this;
        r8 = r9.zzpc;
        monitor-enter(r8);
        r0 = r9.zzGB;	 Catch:{ all -> 0x0037 }
        r6 = r0.zzar(r10);	 Catch:{ all -> 0x0037 }
        if (r6 == 0) goto L_0x0017;
    L_0x000b:
        r0 = r6.zzfR();	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0017;
    L_0x0011:
        r0 = r6.zzfQ();	 Catch:{ all -> 0x0037 }
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
    L_0x0018:
        return;
    L_0x0019:
        r0 = new com.google.android.gms.internal.zzgz;	 Catch:{ all -> 0x0037 }
        r1 = r9.mContext;	 Catch:{ all -> 0x0037 }
        r3 = r9.zzGh;	 Catch:{ all -> 0x0037 }
        r5 = r9.zzCF;	 Catch:{ all -> 0x0037 }
        r2 = r10;
        r4 = r11;
        r7 = r9;
        r0.m3720init(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x0037 }
        r1 = r9.zzGy;	 Catch:{ all -> 0x0037 }
        r0 = r0.zzgn();	 Catch:{ all -> 0x0037 }
        r1.add(r0);	 Catch:{ all -> 0x0037 }
        r0 = r9.zzGz;	 Catch:{ all -> 0x0037 }
        r0.add(r10);	 Catch:{ all -> 0x0037 }
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
        goto L_0x0018;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0037 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhe.zzk(java.lang.String, java.lang.String):void");
    }

    public void onStop() {
    }

    public void zzas(String str) {
        synchronized (this.zzpc) {
            this.zzGA.add(str);
        }
    }

    public void zzb(String str, int i) {
    }

    public void zzdG() {
        final zzhj zzhj;
        for (zzdz zzdz : this.zzCF.zzGG.zzyu) {
            String str = zzdz.zzyr;
            for (String zzk : zzdz.zzym) {
                zzk(zzk, str);
            }
        }
        int i = 0;
        while (i < this.zzGy.size()) {
            try {
                ((Future) this.zzGy.get(i)).get();
                synchronized (this.zzpc) {
                    if (this.zzGA.contains(this.zzGz.get(i))) {
                        zzhj = new zzhj(this.zzCF.zzGL.zzDy, null, this.zzCF.zzGM.zzyw, -2, this.zzCF.zzGM.zzyx, this.zzCF.zzGM.zzDZ, this.zzCF.zzGM.orientation, this.zzCF.zzGM.zzyA, this.zzCF.zzGL.zzDB, this.zzCF.zzGM.zzDX, (zzdz) this.zzCF.zzGG.zzyu.get(i), null, (String) this.zzGz.get(i), this.zzCF.zzGG, null, this.zzCF.zzGM.zzDY, this.zzCF.zzqf, this.zzCF.zzGM.zzDW, this.zzCF.zzGI, this.zzCF.zzGM.zzEb, this.zzCF.zzGM.zzEc, this.zzCF.zzGF, null, this.zzCF.zzGL.zzDO);
                        com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
                            public void run() {
                                zzhe.this.zzGB.zzb(zzhj);
                            }
                        });
                        return;
                    }
                }
            } catch (InterruptedException e) {
            } catch (Exception e2) {
            }
        }
        zzhj = new zzhj(this.zzCF.zzGL.zzDy, null, this.zzCF.zzGM.zzyw, 3, this.zzCF.zzGM.zzyx, this.zzCF.zzGM.zzDZ, this.zzCF.zzGM.orientation, this.zzCF.zzGM.zzyA, this.zzCF.zzGL.zzDB, this.zzCF.zzGM.zzDX, null, null, null, this.zzCF.zzGG, null, this.zzCF.zzGM.zzDY, this.zzCF.zzqf, this.zzCF.zzGM.zzDW, this.zzCF.zzGI, this.zzCF.zzGM.zzEb, this.zzCF.zzGM.zzEc, this.zzCF.zzGF, null, this.zzCF.zzGL.zzDO);
        com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
            public void run() {
                zzhe.this.zzGB.zzb(zzhj);
            }
        });
        return;
        i++;
    }
}
