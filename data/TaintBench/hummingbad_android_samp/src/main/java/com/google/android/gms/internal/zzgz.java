package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzhj.zza;

@zzgk
public class zzgz extends zzhq implements zzha, zzhd {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    private final zza zzCF;
    private int zzCI = 3;
    /* access modifiers changed from: private|final */
    public final String zzGh;
    private final zzgy zzGp;
    private final zzhd zzGq;
    /* access modifiers changed from: private|final */
    public final String zzGr;
    private int zzGs = 0;
    private final Object zzpc;
    /* access modifiers changed from: private|final */
    public final String zzyH;

    public zzgz(Context context, String str, String str2, String str3, zza zza, zzgy zzgy, zzhd zzhd) {
        this.mContext = context;
        this.zzyH = str;
        this.zzGh = str2;
        this.zzGr = str3;
        this.zzCF = zza;
        this.zzGp = zzgy;
        this.zzpc = new Object();
        this.zzGq = zzhd;
    }

    private void zzk(long j) {
        while (true) {
            synchronized (this.zzpc) {
                if (this.zzGs != 0) {
                    return;
                } else if (!zze(j)) {
                    return;
                }
            }
        }
    }

    public void onStop() {
    }

    public void zzJ(int i) {
        zzb(this.zzyH, 0);
    }

    public void zzas(String str) {
        synchronized (this.zzpc) {
            this.zzGs = 1;
            this.zzpc.notify();
        }
    }

    public void zzb(String str, int i) {
        synchronized (this.zzpc) {
            this.zzGs = 2;
            this.zzCI = i;
            this.zzpc.notify();
        }
    }

    public void zzdG() {
        if (this.zzGp != null && this.zzGp.zzfR() != null && this.zzGp.zzfQ() != null) {
            final zzhc zzfR = this.zzGp.zzfR();
            zzfR.zza((zzhd) this);
            zzfR.zza((zzha) this);
            final AdRequestParcel adRequestParcel = this.zzCF.zzGL.zzDy;
            final zzei zzfQ = this.zzGp.zzfQ();
            try {
                if (zzfQ.isInitialized()) {
                    com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
                        public void run() {
                            try {
                                zzfQ.zza(adRequestParcel, zzgz.this.zzGr);
                            } catch (RemoteException e) {
                                zzb.zzd("Fail to load ad from adapter.", e);
                                zzgz.this.zzb(zzgz.this.zzyH, 0);
                            }
                        }
                    });
                } else {
                    com.google.android.gms.ads.internal.util.client.zza.zzIy.post(new Runnable() {
                        public void run() {
                            try {
                                zzfQ.zza(zze.zzx(zzgz.this.mContext), adRequestParcel, zzgz.this.zzGh, zzfR, zzgz.this.zzGr);
                            } catch (RemoteException e) {
                                zzb.zzd("Fail to initialize adapter " + zzgz.this.zzyH, e);
                                zzgz.this.zzb(zzgz.this.zzyH, 0);
                            }
                        }
                    });
                }
            } catch (RemoteException e) {
                zzb.zzd("Fail to check if adapter is initialized.", e);
                zzb(this.zzyH, 0);
            }
            zzk(zzp.zzbB().elapsedRealtime());
            zzfR.zza(null);
            zzfR.zza(null);
            if (this.zzGs == 1) {
                this.zzGq.zzas(this.zzyH);
            } else {
                this.zzGq.zzb(this.zzyH, this.zzCI);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean zze(long j) {
        long elapsedRealtime = 20000 - (zzp.zzbB().elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpc.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void zzfS() {
        this.zzGp.zzfR();
        AdRequestParcel adRequestParcel = this.zzCF.zzGL.zzDy;
        try {
            this.zzGp.zzfQ().zza(adRequestParcel, this.zzGr);
        } catch (RemoteException e) {
            zzb.zzd("Fail to load ad from adapter.", e);
            zzb(this.zzyH, 0);
        }
    }
}
