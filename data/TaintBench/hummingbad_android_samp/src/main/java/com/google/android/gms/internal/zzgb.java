package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzhj.zza;
import com.moceanmobile.mast.Defaults;

@zzgk
public class zzgb extends zzfw {
    zzgb(Context context, zza zza, zzip zzip, zzga.zza zza2) {
        super(context, zza, zzip, zza2);
    }

    /* access modifiers changed from: protected */
    public void zzfp() {
    }

    /* access modifiers changed from: protected */
    public void zzh(long j) throws zza {
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                synchronized (zzgb.this.zzpc) {
                    if (zzgb.this.zzCG.errorCode != -2) {
                        return;
                    }
                    zzgb.this.zzoL.zzgS().zza(zzgb.this);
                    zzgb.this.zzfp();
                    zzb.v("Loading HTML in WebView.");
                    zzgb.this.zzoL.loadDataWithBaseURL(zzp.zzbx().zzaw(zzgb.this.zzCG.zzAT), zzgb.this.zzCG.body, "text/html", Defaults.ENCODING_UTF_8, null);
                }
            }
        });
        zzg(j);
    }
}
