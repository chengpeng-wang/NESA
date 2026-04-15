package com.google.android.gms.ads.internal;

import android.content.Context;
import android.view.MotionEvent;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzaj;
import com.google.android.gms.internal.zzam;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zziq;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@zzhb
class zzh implements zzaj, Runnable {
    private final List<Object[]> zzpH = new Vector();
    private final AtomicReference<zzaj> zzpI = new AtomicReference();
    CountDownLatch zzpJ = new CountDownLatch(1);
    private zzs zzpj;

    public zzh(zzs zzs) {
        this.zzpj = zzs;
        if (zzn.zzcS().zzhJ()) {
            zziq.zza((Runnable) this);
        } else {
            run();
        }
    }

    private void zzbk() {
        if (!this.zzpH.isEmpty()) {
            for (Object[] objArr : this.zzpH) {
                if (objArr.length == 1) {
                    ((zzaj) this.zzpI.get()).zza((MotionEvent) objArr[0]);
                } else if (objArr.length == 3) {
                    ((zzaj) this.zzpI.get()).zza(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue());
                }
            }
            this.zzpH.clear();
        }
    }

    private Context zzq(Context context) {
        if (!((Boolean) zzbt.zzvM.get()).booleanValue()) {
            return context;
        }
        Context applicationContext = context.getApplicationContext();
        return applicationContext != null ? applicationContext : context;
    }

    public void run() {
        try {
            boolean z = !((Boolean) zzbt.zzvY.get()).booleanValue() || this.zzpj.zzrl.zzNb;
            zza(zzb(this.zzpj.zzrl.afmaVersion, zzq(this.zzpj.context), z));
        } finally {
            this.zzpJ.countDown();
            this.zzpj = null;
        }
    }

    public void zza(int i, int i2, int i3) {
        zzaj zzaj = (zzaj) this.zzpI.get();
        if (zzaj != null) {
            zzbk();
            zzaj.zza(i, i2, i3);
            return;
        }
        this.zzpH.add(new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
    }

    public void zza(MotionEvent motionEvent) {
        zzaj zzaj = (zzaj) this.zzpI.get();
        if (zzaj != null) {
            zzbk();
            zzaj.zza(motionEvent);
            return;
        }
        this.zzpH.add(new Object[]{motionEvent});
    }

    /* access modifiers changed from: protected */
    public void zza(zzaj zzaj) {
        this.zzpI.set(zzaj);
    }

    /* access modifiers changed from: protected */
    public zzaj zzb(String str, Context context, boolean z) {
        return zzam.zza(str, context, z);
    }

    public String zzb(Context context) {
        if (zzbj()) {
            zzaj zzaj = (zzaj) this.zzpI.get();
            if (zzaj != null) {
                zzbk();
                return zzaj.zzb(zzq(context));
            }
        }
        return "";
    }

    public String zzb(Context context, String str) {
        if (zzbj()) {
            zzaj zzaj = (zzaj) this.zzpI.get();
            if (zzaj != null) {
                zzbk();
                return zzaj.zzb(zzq(context), str);
            }
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public boolean zzbj() {
        try {
            this.zzpJ.await();
            return true;
        } catch (InterruptedException e) {
            zzb.zzd("Interrupted during GADSignals creation.", e);
            return false;
        }
    }
}
