package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.internal.zzga.zza;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@zzgk
public class zzge extends zzhq {
    /* access modifiers changed from: private|final */
    public final zza zzCD;
    private final zzhj.zza zzCF;
    private final AdResponseParcel zzCG;
    private final zzgf zzCP;
    private Future<zzhj> zzCQ;
    private final Object zzpc;

    public zzge(Context context, zzn zzn, zzbc zzbc, zzhj.zza zza, zzan zzan, zza zza2) {
        this(zza, zza2, new zzgf(context, zzn, zzbc, new zzhy(context), zzan, zza));
    }

    zzge(zzhj.zza zza, zza zza2, zzgf zzgf) {
        this.zzpc = new Object();
        this.zzCF = zza;
        this.zzCG = zza.zzGM;
        this.zzCD = zza2;
        this.zzCP = zzgf;
    }

    private zzhj zzA(int i) {
        return new zzhj(this.zzCF.zzGL.zzDy, null, null, i, null, null, this.zzCG.orientation, this.zzCG.zzyA, this.zzCF.zzGL.zzDB, false, null, null, null, null, null, this.zzCG.zzDY, this.zzCF.zzqf, this.zzCG.zzDW, this.zzCF.zzGI, this.zzCG.zzEb, this.zzCG.zzEc, this.zzCF.zzGF, null, this.zzCF.zzGL.zzDO);
    }

    public void onStop() {
        synchronized (this.zzpc) {
            if (this.zzCQ != null) {
                this.zzCQ.cancel(true);
            }
        }
    }

    public void zzdG() {
        zzhj zzhj;
        int i;
        try {
            synchronized (this.zzpc) {
                this.zzCQ = zzht.zza(this.zzCP);
            }
            zzhj = (zzhj) this.zzCQ.get(BuglyBroadcastRecevier.UPLOADLIMITED, TimeUnit.MILLISECONDS);
            i = -2;
        } catch (TimeoutException e) {
            zzb.zzaE("Timed out waiting for native ad.");
            this.zzCQ.cancel(true);
            i = 2;
            zzhj = null;
        } catch (ExecutionException e2) {
            i = 0;
            zzhj = null;
        } catch (InterruptedException e3) {
            zzhj = null;
            i = -1;
        } catch (CancellationException e4) {
            zzhj = null;
            i = -1;
        }
        if (zzhj == null) {
            zzhj = zzA(i);
        }
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                zzge.this.zzCD.zzb(zzhj);
            }
        });
    }
}
