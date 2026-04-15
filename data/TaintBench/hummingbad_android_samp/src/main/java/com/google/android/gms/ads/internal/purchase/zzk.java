package com.google.android.gms.ads.internal.purchase;

import android.content.Intent;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzgk;

@zzgk
public class zzk {
    private final String zztn;

    public zzk(String str) {
        this.zztn = str;
    }

    public boolean zza(String str, int i, Intent intent) {
        if (str == null || intent == null) {
            return false;
        }
        String zze = zzp.zzbH().zze(intent);
        String zzf = zzp.zzbH().zzf(intent);
        if (zze == null || zzf == null) {
            return false;
        }
        if (!str.equals(zzp.zzbH().zzal(zze))) {
            zzb.zzaE("Developer payload not match.");
            return false;
        } else if (this.zztn == null || zzl.zzc(this.zztn, zze, zzf)) {
            return true;
        } else {
            zzb.zzaE("Fail to verify signature.");
            return false;
        }
    }

    public String zzfk() {
        return zzp.zzbx().zzgs();
    }
}
