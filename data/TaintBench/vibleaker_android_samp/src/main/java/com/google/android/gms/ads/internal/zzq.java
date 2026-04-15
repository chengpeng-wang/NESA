package com.google.android.gms.ads.internal;

import android.os.Handler;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzir;
import java.lang.ref.WeakReference;

@zzhb
public class zzq {
    private final zza zzqG;
    /* access modifiers changed from: private */
    public AdRequestParcel zzqH;
    /* access modifiers changed from: private */
    public boolean zzqI;
    private boolean zzqJ;
    private long zzqK;
    private final Runnable zzx;

    public static class zza {
        private final Handler mHandler;

        public zza(Handler handler) {
            this.mHandler = handler;
        }

        public boolean postDelayed(Runnable runnable, long timeFromNowInMillis) {
            return this.mHandler.postDelayed(runnable, timeFromNowInMillis);
        }

        public void removeCallbacks(Runnable runnable) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public zzq(zza zza) {
        this(zza, new zza(zzir.zzMc));
    }

    zzq(zza zza, zza zza2) {
        this.zzqI = false;
        this.zzqJ = false;
        this.zzqK = 0;
        this.zzqG = zza2;
        final WeakReference weakReference = new WeakReference(zza);
        this.zzx = new Runnable() {
            public void run() {
                zzq.this.zzqI = false;
                zza zza = (zza) weakReference.get();
                if (zza != null) {
                    zza.zzd(zzq.this.zzqH);
                }
            }
        };
    }

    public void cancel() {
        this.zzqI = false;
        this.zzqG.removeCallbacks(this.zzx);
    }

    public void pause() {
        this.zzqJ = true;
        if (this.zzqI) {
            this.zzqG.removeCallbacks(this.zzx);
        }
    }

    public void resume() {
        this.zzqJ = false;
        if (this.zzqI) {
            this.zzqI = false;
            zza(this.zzqH, this.zzqK);
        }
    }

    public void zza(AdRequestParcel adRequestParcel, long j) {
        if (this.zzqI) {
            zzb.zzaK("An ad refresh is already scheduled.");
            return;
        }
        this.zzqH = adRequestParcel;
        this.zzqI = true;
        this.zzqK = j;
        if (!this.zzqJ) {
            zzb.zzaJ("Scheduling ad refresh " + j + " milliseconds from now.");
            this.zzqG.postDelayed(this.zzx, j);
        }
    }

    public boolean zzbw() {
        return this.zzqI;
    }

    public void zzg(AdRequestParcel adRequestParcel) {
        zza(adRequestParcel, 60000);
    }
}
