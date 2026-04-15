package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzgr.zza;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzhb
public class zzgv extends zzim {
    private final zzgw zzGC;
    private Future<zzif> zzGD;
    /* access modifiers changed from: private|final */
    public final zza zzGc;
    private final zzif.zza zzGd;
    private final AdResponseParcel zzGe;
    private final Object zzpV;

    public zzgv(Context context, zzp zzp, zzee zzee, zzif.zza zza, zzan zzan, zza zza2) {
        this(zza, zza2, new zzgw(context, zzp, zzee, new zziw(context), zzan, zza));
    }

    zzgv(zzif.zza zza, zza zza2, zzgw zzgw) {
        this.zzpV = new Object();
        this.zzGd = zza;
        this.zzGe = zza.zzLe;
        this.zzGc = zza2;
        this.zzGC = zzgw;
    }

    private zzif zzE(int i) {
        return new zzif(this.zzGd.zzLd.zzHt, null, null, i, null, null, this.zzGe.orientation, this.zzGe.zzBU, this.zzGd.zzLd.zzHw, false, null, null, null, null, null, this.zzGe.zzHU, this.zzGd.zzrp, this.zzGe.zzHS, this.zzGd.zzKY, this.zzGe.zzHX, this.zzGe.zzHY, this.zzGd.zzKT, null, null, null, null, this.zzGd.zzLe.zzIm);
    }

    public void onStop() {
        synchronized (this.zzpV) {
            if (this.zzGD != null) {
                this.zzGD.cancel(true);
            }
        }
    }

    public void zzbr() {
        zzif zzif;
        int i;
        try {
            synchronized (this.zzpV) {
                this.zzGD = zziq.zza(this.zzGC);
            }
            zzif = (zzif) this.zzGD.get(60000, TimeUnit.MILLISECONDS);
            i = -2;
        } catch (TimeoutException e) {
            zzb.zzaK("Timed out waiting for native ad.");
            this.zzGD.cancel(true);
            i = 2;
            zzif = null;
        } catch (ExecutionException e2) {
            i = 0;
            zzif = null;
        } catch (InterruptedException e3) {
            zzif = null;
            i = -1;
        } catch (CancellationException e4) {
            zzif = null;
            i = -1;
        }
        if (zzif == null) {
            zzif = zzE(i);
        }
        zzir.zzMc.post(new Runnable() {
            public void run() {
                zzgv.this.zzGc.zzb(zzif);
            }
        });
    }
}
