package com.google.android.gms.internal;

import com.google.android.gms.internal.zzej.zza;

@zzgk
public final class zzec extends zza {
    private final Object zzpc = new Object();
    private zzee.zza zzyF;
    private zzeb zzyG;

    public void onAdClicked() {
        synchronized (this.zzpc) {
            if (this.zzyG != null) {
                this.zzyG.zzaX();
            }
        }
    }

    public void onAdClosed() {
        synchronized (this.zzpc) {
            if (this.zzyG != null) {
                this.zzyG.zzaY();
            }
        }
    }

    public void onAdFailedToLoad(int error) {
        synchronized (this.zzpc) {
            if (this.zzyF != null) {
                this.zzyF.zzq(error == 3 ? 1 : 2);
                this.zzyF = null;
            }
        }
    }

    public void onAdLeftApplication() {
        synchronized (this.zzpc) {
            if (this.zzyG != null) {
                this.zzyG.zzaZ();
            }
        }
    }

    /* JADX WARNING: Missing block: B:16:?, code skipped:
            return;
     */
    public void onAdLoaded() {
        /*
        r3 = this;
        r1 = r3.zzpc;
        monitor-enter(r1);
        r0 = r3.zzyF;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r3.zzyF;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r0.zzq(r2);	 Catch:{ all -> 0x001d }
        r0 = 0;
        r3.zzyF = r0;	 Catch:{ all -> 0x001d }
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r3.zzyG;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r0 = r3.zzyG;	 Catch:{ all -> 0x001d }
        r0.zzbb();	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        goto L_0x0011;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzec.onAdLoaded():void");
    }

    public void onAdOpened() {
        synchronized (this.zzpc) {
            if (this.zzyG != null) {
                this.zzyG.zzba();
            }
        }
    }

    public void zza(zzeb zzeb) {
        synchronized (this.zzpc) {
            this.zzyG = zzeb;
        }
    }

    public void zza(zzee.zza zza) {
        synchronized (this.zzpc) {
            this.zzyF = zza;
        }
    }
}
