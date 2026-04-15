package com.google.android.gms.ads.internal;

import android.os.Handler;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhu;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import java.lang.ref.WeakReference;

@zzgk
public class zzo {
    /* access modifiers changed from: private */
    public boolean zzpA;
    private boolean zzpB;
    private long zzpC;
    private final zza zzpy;
    /* access modifiers changed from: private */
    public AdRequestParcel zzpz;
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

    public zzo(zza zza) {
        this(zza, new zza(zzhu.zzHK));
    }

    zzo(zza zza, zza zza2) {
        this.zzpA = false;
        this.zzpB = false;
        this.zzpC = 0;
        this.zzpy = zza2;
        final WeakReference weakReference = new WeakReference(zza);
        this.zzx = new Runnable() {
            public void run() {
                zzo.this.zzpA = false;
                zza zza = (zza) weakReference.get();
                if (zza != null) {
                    zza.zzc(zzo.this.zzpz);
                }
            }
        };
    }

    public void cancel() {
        this.zzpA = false;
        this.zzpy.removeCallbacks(this.zzx);
    }

    public void pause() {
        this.zzpB = true;
        if (this.zzpA) {
            this.zzpy.removeCallbacks(this.zzx);
        }
    }

    public void resume() {
        this.zzpB = false;
        if (this.zzpA) {
            this.zzpA = false;
            zza(this.zzpz, this.zzpC);
        }
    }

    public void zza(AdRequestParcel adRequestParcel, long j) {
        if (this.zzpA) {
            zzb.zzaE("An ad refresh is already scheduled.");
            return;
        }
        this.zzpz = adRequestParcel;
        this.zzpA = true;
        this.zzpC = j;
        if (!this.zzpB) {
            zzb.zzaD("Scheduling ad refresh " + j + " milliseconds from now.");
            this.zzpy.postDelayed(this.zzx, j);
        }
    }

    public boolean zzbr() {
        return this.zzpA;
    }

    public void zzf(AdRequestParcel adRequestParcel) {
        zza(adRequestParcel, (long) BuglyBroadcastRecevier.UPLOADLIMITED);
    }
}
