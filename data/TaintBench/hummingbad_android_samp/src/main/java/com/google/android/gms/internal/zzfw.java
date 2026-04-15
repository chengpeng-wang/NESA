package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zziq.zza;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;

@zzgk
public abstract class zzfw extends zzfz implements zza {
    private final zziq zzCq;
    protected boolean zzCr = false;
    private boolean zzyg = false;

    protected zzfw(Context context, zzhj.zza zza, zzip zzip, zzga.zza zza2) {
        super(context, zza, zzip, zza2);
        this.zzCq = zzip.zzgS();
    }

    private boolean zze(long j) throws zza {
        long elapsedRealtime = BuglyBroadcastRecevier.UPLOADLIMITED - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 1) {
            return false;
        }
        try {
            this.zzpc.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            throw new zza("Ad request cancelled.", -1);
        }
    }

    public void onStop() {
        synchronized (this.zzCE) {
            this.zzoL.stopLoading();
            zzp.zzbz().zza(this.zzoL.getWebView());
        }
    }

    public void zza(zzip zzip, boolean z) {
        boolean z2 = true;
        synchronized (this.zzpc) {
            zzb.zzaC("WebView finished loading.");
            this.zzCr = true;
            if (z) {
                z2 = false;
            }
            this.zzyg = z2;
            this.zzpc.notify();
        }
    }

    /* access modifiers changed from: protected */
    public void zzg(long j) throws zza {
        while (zze(j)) {
            if (this.zzyg) {
                throw new zza("Received cancellation request from creative.", 0);
            } else if (this.zzCr) {
                return;
            }
        }
        throw new zza("Timed out waiting for WebView to finish loading.", 2);
    }
}
