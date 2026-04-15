package com.google.android.gms.internal;

import android.content.Context;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzif.zza;

@zzhb
public class zzgp extends zzgn {
    private zzgo zzGs;

    zzgp(Context context, zza zza, zzjp zzjp, zzgr.zza zza2) {
        super(context, zza, zzjp, zza2);
    }

    /* access modifiers changed from: protected */
    public void zzgb() {
        int i;
        int i2;
        AdSizeParcel zzaN = this.zzpD.zzaN();
        if (zzaN.zzui) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            i = displayMetrics.widthPixels;
            i2 = displayMetrics.heightPixels;
        } else {
            i = zzaN.widthPixels;
            i2 = zzaN.heightPixels;
        }
        this.zzGs = new zzgo(this, this.zzpD, i, i2);
        this.zzpD.zzhU().zza((zzjq.zza) this);
        this.zzGs.zza(this.zzGe);
    }

    /* access modifiers changed from: protected */
    public int zzgc() {
        if (!this.zzGs.zzgg()) {
            return !this.zzGs.zzgh() ? 2 : -2;
        } else {
            zzb.zzaI("Ad-Network indicated no fill with passback URL.");
            return 3;
        }
    }
}
