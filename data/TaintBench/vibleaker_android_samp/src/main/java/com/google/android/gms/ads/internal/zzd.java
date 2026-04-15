package com.google.android.gms.ads.internal;

import com.google.android.gms.ads.internal.overlay.zzj;
import com.google.android.gms.ads.internal.overlay.zzm;
import com.google.android.gms.ads.internal.overlay.zzn;
import com.google.android.gms.ads.internal.overlay.zzr;
import com.google.android.gms.internal.zzdc;
import com.google.android.gms.internal.zzdt;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzd {
    public final zzdt zzpw;
    public final zzj zzpx;
    public final zzm zzpy;

    public zzd(zzdt zzdt, zzj zzj, zzm zzm) {
        this.zzpw = zzdt;
        this.zzpx = zzj;
        this.zzpy = zzm;
    }

    public static zzd zzbg() {
        return new zzd(new zzdc(), new zzn(), new zzr());
    }
}
