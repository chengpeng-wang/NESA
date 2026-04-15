package com.google.android.gms.internal;

import java.util.concurrent.Future;

@zzgk
public abstract class zzhq {
    /* access modifiers changed from: private|volatile */
    public volatile Thread zzHt;
    private final Runnable zzx = new Runnable() {
        public final void run() {
            zzhq.this.zzHt = Thread.currentThread();
            zzhq.this.zzdG();
        }
    };

    public final void cancel() {
        onStop();
        if (this.zzHt != null) {
            this.zzHt.interrupt();
        }
    }

    public abstract void onStop();

    public abstract void zzdG();

    public final Future zzgn() {
        return zzht.zza(this.zzx);
    }

    public final void zzgo() {
        zzht.zza(1, this.zzx);
    }
}
