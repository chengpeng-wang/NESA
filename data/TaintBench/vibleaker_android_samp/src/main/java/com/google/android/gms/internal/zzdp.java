package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;

@zzhb
public class zzdp extends zzim {
    final zzjp zzpD;
    final zzdr zzzJ;
    private final String zzzK;

    zzdp(zzjp zzjp, zzdr zzdr, String str) {
        this.zzpD = zzjp;
        this.zzzJ = zzdr;
        this.zzzK = str;
        zzr.zzbR().zza(this);
    }

    public void onStop() {
        this.zzzJ.abort();
    }

    public void zzbr() {
        try {
            this.zzzJ.zzU(this.zzzK);
        } finally {
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzr.zzbR().zzb(zzdp.this);
                }
            });
        }
    }
}
