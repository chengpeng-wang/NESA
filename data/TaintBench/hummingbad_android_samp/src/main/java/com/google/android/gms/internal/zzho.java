package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.zzp;

@zzgk
public class zzho {
    private final String zzGP;
    private int zzHq;
    private int zzHr;
    private final zzhl zzpN;
    private final Object zzpc;

    zzho(zzhl zzhl, String str) {
        this.zzpc = new Object();
        this.zzpN = zzhl;
        this.zzGP = str;
    }

    public zzho(String str) {
        this(zzp.zzbA(), str);
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.zzpc) {
            bundle = new Bundle();
            bundle.putInt("pmnli", this.zzHq);
            bundle.putInt("pmnll", this.zzHr);
        }
        return bundle;
    }

    public void zzf(int i, int i2) {
        synchronized (this.zzpc) {
            this.zzHq = i;
            this.zzHr = i2;
            this.zzpN.zza(this.zzGP, this);
        }
    }
}
