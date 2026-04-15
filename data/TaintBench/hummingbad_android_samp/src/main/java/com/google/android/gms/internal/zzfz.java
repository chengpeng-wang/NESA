package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;

@zzgk
public abstract class zzfz extends zzhq {
    protected final Context mContext;
    protected final com.google.android.gms.internal.zzga.zza zzCD;
    protected final Object zzCE = new Object();
    protected final com.google.android.gms.internal.zzhj.zza zzCF;
    protected AdResponseParcel zzCG;
    protected final zzip zzoL;
    protected final Object zzpc = new Object();

    protected static final class zza extends Exception {
        private final int zzCI;

        public zza(String str, int i) {
            super(str);
            this.zzCI = i;
        }

        public int getErrorCode() {
            return this.zzCI;
        }
    }

    protected zzfz(Context context, com.google.android.gms.internal.zzhj.zza zza, zzip zzip, com.google.android.gms.internal.zzga.zza zza2) {
        this.mContext = context;
        this.zzCF = zza;
        this.zzCG = zza.zzGM;
        this.zzoL = zzip;
        this.zzCD = zza2;
    }

    public void onStop() {
    }

    public void zzdG() {
        synchronized (this.zzpc) {
            zzb.zzaC("AdRendererBackgroundTask started.");
            int i = this.zzCF.errorCode;
            try {
                zzh(SystemClock.elapsedRealtime());
            } catch (zza e) {
                int errorCode = e.getErrorCode();
                if (errorCode == 3 || errorCode == -1) {
                    zzb.zzaD(e.getMessage());
                } else {
                    zzb.zzaE(e.getMessage());
                }
                if (this.zzCG == null) {
                    this.zzCG = new AdResponseParcel(errorCode);
                } else {
                    this.zzCG = new AdResponseParcel(errorCode, this.zzCG.zzyA);
                }
                zzhu.zzHK.post(new Runnable() {
                    public void run() {
                        zzfz.this.onStop();
                    }
                });
                i = errorCode;
            }
            final zzhj zzz = zzz(i);
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    synchronized (zzfz.this.zzpc) {
                        zzfz.this.zzi(zzz);
                    }
                }
            });
        }
    }

    public abstract void zzh(long j) throws zza;

    /* access modifiers changed from: protected */
    public void zzi(zzhj zzhj) {
        this.zzCD.zzb(zzhj);
    }

    /* access modifiers changed from: protected */
    public zzhj zzz(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzCF.zzGL;
        return new zzhj(adRequestInfoParcel.zzDy, this.zzoL, this.zzCG.zzyw, i, this.zzCG.zzyx, this.zzCG.zzDZ, this.zzCG.orientation, this.zzCG.zzyA, adRequestInfoParcel.zzDB, this.zzCG.zzDX, null, null, null, null, null, this.zzCG.zzDY, this.zzCF.zzqf, this.zzCG.zzDW, this.zzCF.zzGI, this.zzCG.zzEb, this.zzCG.zzEc, this.zzCF.zzGF, null, adRequestInfoParcel.zzDO);
    }
}
