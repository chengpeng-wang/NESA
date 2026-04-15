package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzp;

@zzgk
public class zzdp extends zzhq {
    final zzip zzoL;
    final zzdr zzxr;
    private final String zzxs;

    zzdp(zzip zzip, zzdr zzdr, String str) {
        this.zzoL = zzip;
        this.zzxr = zzdr;
        this.zzxs = str;
        zzp.zzbK().zza(this);
    }

    public void onStop() {
        this.zzxr.abort();
    }

    public void zzdG() {
        try {
            this.zzxr.zzZ(this.zzxs);
        } finally {
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzp.zzbK().zzb(zzdp.this);
                }
            });
        }
    }
}
