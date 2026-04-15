package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzif.zza;

@zzhb
public class zzhu extends zzim implements zzhv, zzhy {
    /* access modifiers changed from: private|final */
    public final Context mContext;
    /* access modifiers changed from: private|final */
    public final String zzCd;
    private final zza zzGd;
    private int zzGu = 3;
    private final zzia zzKB;
    private final zzhy zzKC;
    /* access modifiers changed from: private|final */
    public final String zzKD;
    private final String zzKE;
    private int zzKF = 0;
    private final Object zzpV;
    /* access modifiers changed from: private|final */
    public final String zzrG;

    public zzhu(Context context, String str, String str2, String str3, String str4, zza zza, zzia zzia, zzhy zzhy) {
        this.mContext = context;
        this.zzCd = str;
        this.zzrG = str2;
        this.zzKD = str3;
        this.zzKE = str4;
        this.zzGd = zza;
        this.zzKB = zzia;
        this.zzpV = new Object();
        this.zzKC = zzhy;
    }

    /* access modifiers changed from: private */
    public void zza(AdRequestParcel adRequestParcel, zzey zzey) {
        try {
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzCd)) {
                zzey.zza(adRequestParcel, this.zzKD, this.zzKE);
            } else {
                zzey.zzb(adRequestParcel, this.zzKD);
            }
        } catch (RemoteException e) {
            zzb.zzd("Fail to load ad from adapter.", e);
            zza(this.zzCd, 0);
        }
    }

    private void zzk(long j) {
        while (true) {
            synchronized (this.zzpV) {
                if (this.zzKF != 0) {
                    return;
                } else if (!zzf(j)) {
                    return;
                }
            }
        }
    }

    public void onStop() {
    }

    public void zzN(int i) {
        zza(this.zzCd, 0);
    }

    public void zza(String str, int i) {
        synchronized (this.zzpV) {
            this.zzKF = 2;
            this.zzGu = i;
            this.zzpV.notify();
        }
    }

    public void zzax(String str) {
        synchronized (this.zzpV) {
            this.zzKF = 1;
            this.zzpV.notify();
        }
    }

    public void zzbr() {
        if (this.zzKB != null && this.zzKB.zzgQ() != null && this.zzKB.zzgP() != null) {
            final zzhx zzgQ = this.zzKB.zzgQ();
            zzgQ.zza((zzhy) this);
            zzgQ.zza((zzhv) this);
            final AdRequestParcel adRequestParcel = this.zzGd.zzLd.zzHt;
            final zzey zzgP = this.zzKB.zzgP();
            try {
                if (zzgP.isInitialized()) {
                    com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
                        public void run() {
                            zzhu.this.zza(adRequestParcel, zzgP);
                        }
                    });
                } else {
                    com.google.android.gms.ads.internal.util.client.zza.zzMS.post(new Runnable() {
                        public void run() {
                            try {
                                zzgP.zza(zze.zzC(zzhu.this.mContext), adRequestParcel, zzhu.this.zzrG, zzgQ, zzhu.this.zzKD);
                            } catch (RemoteException e) {
                                zzb.zzd("Fail to initialize adapter " + zzhu.this.zzCd, e);
                                zzhu.this.zza(zzhu.this.zzCd, 0);
                            }
                        }
                    });
                }
            } catch (RemoteException e) {
                zzb.zzd("Fail to check if adapter is initialized.", e);
                zza(this.zzCd, 0);
            }
            zzk(zzr.zzbG().elapsedRealtime());
            zzgQ.zza(null);
            zzgQ.zza(null);
            if (this.zzKF == 1) {
                this.zzKC.zzax(this.zzCd);
            } else {
                this.zzKC.zza(this.zzCd, this.zzGu);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean zzf(long j) {
        long elapsedRealtime = 20000 - (zzr.zzbG().elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpV.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void zzgN() {
        zza(this.zzGd.zzLd.zzHt, this.zzKB.zzgP());
    }
}
