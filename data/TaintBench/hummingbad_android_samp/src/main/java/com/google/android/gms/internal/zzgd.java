package com.google.android.gms.internal;

import android.content.Context;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.internal.zzhj.zza;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;

@zzgk
public class zzgd extends zzfz {
    private zzdy zzCN;
    protected zzee zzCO;
    private final zzcd zzon;
    private zzeh zzow;
    private zzea zzye;

    zzgd(Context context, zza zza, zzip zzip, zzeh zzeh, zzga.zza zza2, zzcd zzcd) {
        super(context, zza, zzip, zza2);
        this.zzow = zzeh;
        this.zzye = zza.zzGG;
        this.zzon = zzcd;
    }

    public void onStop() {
        synchronized (this.zzCE) {
            super.onStop();
            if (this.zzCN != null) {
                this.zzCN.cancel();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzh(long j) throws zza {
        synchronized (this.zzCE) {
            this.zzCN = new zzdy(this.mContext, this.zzCF.zzGL, this.zzow, this.zzye, this.zzCG.zzsJ);
        }
        this.zzCO = this.zzCN.zza(j, BuglyBroadcastRecevier.UPLOADLIMITED, this.zzon);
        switch (this.zzCO.zzyP) {
            case 0:
                return;
            case 1:
                throw new zza("No fill from any mediation ad networks.", 3);
            default:
                throw new zza("Unexpected mediation result: " + this.zzCO.zzyP, 0);
        }
    }

    /* access modifiers changed from: protected */
    public zzhj zzz(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzCF.zzGL;
        return new zzhj(adRequestInfoParcel.zzDy, this.zzoL, this.zzCG.zzyw, i, this.zzCG.zzyx, this.zzCG.zzDZ, this.zzCG.orientation, this.zzCG.zzyA, adRequestInfoParcel.zzDB, this.zzCG.zzDX, this.zzCO != null ? this.zzCO.zzyQ : null, this.zzCO != null ? this.zzCO.zzyR : null, this.zzCO != null ? this.zzCO.zzyS : AdMobAdapter.class.getName(), this.zzye, this.zzCO != null ? this.zzCO.zzyT : null, this.zzCG.zzDY, this.zzCF.zzqf, this.zzCG.zzDW, this.zzCF.zzGI, this.zzCG.zzEb, this.zzCG.zzEc, this.zzCF.zzGF, null, adRequestInfoParcel.zzDO);
    }
}
