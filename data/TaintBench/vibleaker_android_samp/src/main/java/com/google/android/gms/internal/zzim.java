package com.google.android.gms.internal;

import java.util.concurrent.Future;

@zzhb
public abstract class zzim implements zzit<Future> {
    /* access modifiers changed from: private|volatile */
    public volatile Thread zzLM;
    private boolean zzLN;
    private final Runnable zzx;

    public zzim() {
        this.zzx = new Runnable() {
            public final void run() {
                zzim.this.zzLM = Thread.currentThread();
                zzim.this.zzbr();
            }
        };
        this.zzLN = false;
    }

    public zzim(boolean z) {
        this.zzx = /* anonymous class already generated */;
        this.zzLN = z;
    }

    public final void cancel() {
        onStop();
        if (this.zzLM != null) {
            this.zzLM.interrupt();
        }
    }

    public abstract void onStop();

    public abstract void zzbr();

    /* renamed from: zzhn */
    public final Future zzgd() {
        return this.zzLN ? zziq.zza(1, this.zzx) : zziq.zza(this.zzx);
    }
}
