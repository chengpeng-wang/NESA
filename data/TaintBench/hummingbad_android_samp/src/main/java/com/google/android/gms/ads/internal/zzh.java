package com.google.android.gms.ads.internal;

import android.content.Context;
import android.view.MotionEvent;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzaj;
import com.google.android.gms.internal.zzam;
import com.google.android.gms.internal.zzby;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzht;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@zzgk
class zzh implements zzaj, Runnable {
    private final List<Object[]> zzoP = new Vector();
    private final AtomicReference<zzaj> zzoQ = new AtomicReference();
    CountDownLatch zzoR = new CountDownLatch(1);
    private zzq zzos;

    public zzh(zzq zzq) {
        this.zzos = zzq;
        if (zzk.zzcE().zzgI()) {
            zzht.zza((Runnable) this);
        } else {
            run();
        }
    }

    private void zzbh() {
        if (!this.zzoP.isEmpty()) {
            for (Object[] objArr : this.zzoP) {
                if (objArr.length == 1) {
                    ((zzaj) this.zzoQ.get()).zza((MotionEvent) objArr[0]);
                } else if (objArr.length == 3) {
                    ((zzaj) this.zzoQ.get()).zza(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue());
                }
            }
            this.zzoP.clear();
        }
    }

    private Context zzp(Context context) {
        if (!((Boolean) zzby.zzuh.get()).booleanValue()) {
            return context;
        }
        Context applicationContext = context.getApplicationContext();
        return applicationContext != null ? applicationContext : context;
    }

    public void run() {
        try {
            boolean z = !((Boolean) zzby.zzut.get()).booleanValue() || this.zzos.zzqb.zzIC;
            zza(zzb(this.zzos.zzqb.zzIz, zzp(this.zzos.context), z));
        } finally {
            this.zzoR.countDown();
            this.zzos = null;
        }
    }

    public void zza(int i, int i2, int i3) {
        zzaj zzaj = (zzaj) this.zzoQ.get();
        if (zzaj != null) {
            zzbh();
            zzaj.zza(i, i2, i3);
            return;
        }
        this.zzoP.add(new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
    }

    public void zza(MotionEvent motionEvent) {
        zzaj zzaj = (zzaj) this.zzoQ.get();
        if (zzaj != null) {
            zzbh();
            zzaj.zza(motionEvent);
            return;
        }
        this.zzoP.add(new Object[]{motionEvent});
    }

    /* access modifiers changed from: protected */
    public void zza(zzaj zzaj) {
        this.zzoQ.set(zzaj);
    }

    /* access modifiers changed from: protected */
    public zzaj zzb(String str, Context context, boolean z) {
        return zzam.zza(str, context, z);
    }

    public String zzb(Context context) {
        if (zzbg()) {
            zzaj zzaj = (zzaj) this.zzoQ.get();
            if (zzaj != null) {
                zzbh();
                return zzaj.zzb(zzp(context));
            }
        }
        return "";
    }

    public String zzb(Context context, String str) {
        if (zzbg()) {
            zzaj zzaj = (zzaj) this.zzoQ.get();
            if (zzaj != null) {
                zzbh();
                return zzaj.zzb(zzp(context), str);
            }
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public boolean zzbg() {
        try {
            this.zzoR.await();
            return true;
        } catch (InterruptedException e) {
            zzb.zzd("Interrupted during GADSignals creation.", e);
            return false;
        }
    }
}
