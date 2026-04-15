package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhj.zza;

@zzgk
public class zzfy extends zzfw {
    zzfy(Context context, zza zza, zzip zzip, zzga.zza zza2) {
        super(context, zza, zzip, zza2);
    }

    /* access modifiers changed from: protected */
    public void zzh(long j) throws zza {
        int i;
        int i2;
        AdSizeParcel zzaN = this.zzoL.zzaN();
        if (zzaN.zzsH) {
            i = this.mContext.getResources().getDisplayMetrics().widthPixels;
            i2 = this.mContext.getResources().getDisplayMetrics().heightPixels;
        } else {
            i = zzaN.widthPixels;
            i2 = zzaN.heightPixels;
        }
        final zzfx zzfx = new zzfx(this, this.zzoL, i, i2);
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                synchronized (zzfy.this.zzpc) {
                    if (zzfy.this.zzCG.errorCode != -2) {
                        return;
                    }
                    zzfy.this.zzoL.zzgS().zza(zzfy.this);
                    zzfx.zza(zzfy.this.zzCG);
                }
            }
        });
        zzg(j);
        if (zzfx.zzfn()) {
            zzb.zzaC("Ad-Network indicated no fill with passback URL.");
            throw new zza("AdNetwork sent passback url", 3);
        } else if (!zzfx.zzfo()) {
            throw new zza("AdNetwork timed out", 2);
        }
    }
}
