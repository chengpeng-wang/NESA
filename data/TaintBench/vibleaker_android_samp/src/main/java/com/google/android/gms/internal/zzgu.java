package com.google.android.gms.internal;

import android.content.Context;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.zzm;
import com.google.android.gms.internal.zzif.zza;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@zzhb
public class zzgu extends zzgq {
    private zzeo zzCf;
    /* access modifiers changed from: private */
    public boolean zzGA;
    private zzem zzGy;
    protected zzes zzGz;
    /* access modifiers changed from: private|final */
    public final zzjp zzpD;
    private final zzcb zzpe;
    private zzex zzpn;

    zzgu(Context context, zza zza, zzex zzex, zzgr.zza zza2, zzcb zzcb, zzjp zzjp) {
        super(context, zza, zza2);
        this.zzpn = zzex;
        this.zzCf = zza.zzKV;
        this.zzpe = zzcb;
        this.zzpD = zzjp;
    }

    private void zzgk() throws zza {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        zzir.zzMc.post(new Runnable() {
            public void run() {
                synchronized (zzgu.this.zzGg) {
                    zzgu.this.zzGA = zzm.zza(zzgu.this.zzpD, zzgu.this.zzGz, countDownLatch);
                }
            }
        });
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
            synchronized (this.zzGg) {
                if (!this.zzGA) {
                    throw new zza("View could not be prepared", 0);
                } else if (this.zzpD.isDestroyed()) {
                    throw new zza("Assets not loaded, web view is destroyed", 0);
                }
            }
        } catch (InterruptedException e) {
            throw new zza("Interrupted while waiting for latch : " + e, 0);
        }
    }

    public void onStop() {
        synchronized (this.zzGg) {
            super.onStop();
            if (this.zzGy != null) {
                this.zzGy.cancel();
            }
        }
    }

    /* access modifiers changed from: protected */
    public zzif zzD(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzGd.zzLd;
        return new zzif(adRequestInfoParcel.zzHt, this.zzpD, this.zzGe.zzBQ, i, this.zzGe.zzBR, this.zzGe.zzHV, this.zzGe.orientation, this.zzGe.zzBU, adRequestInfoParcel.zzHw, this.zzGe.zzHT, this.zzGz != null ? this.zzGz.zzCp : null, this.zzGz != null ? this.zzGz.zzCq : null, this.zzGz != null ? this.zzGz.zzCr : AdMobAdapter.class.getName(), this.zzCf, this.zzGz != null ? this.zzGz.zzCs : null, this.zzGe.zzHU, this.zzGd.zzrp, this.zzGe.zzHS, this.zzGd.zzKY, this.zzGe.zzHX, this.zzGe.zzHY, this.zzGd.zzKT, null, this.zzGe.zzIj, this.zzGe.zzIk, this.zzGe.zzIl, this.zzGe.zzIm);
    }

    /* access modifiers changed from: protected */
    public void zzh(long j) throws zza {
        synchronized (this.zzGg) {
            this.zzGy = zzi(j);
        }
        this.zzGz = this.zzGy.zzc(this.zzCf.zzBO);
        switch (this.zzGz.zzCo) {
            case 0:
                if (this.zzGz.zzCp != null && this.zzGz.zzCp.zzBJ != null) {
                    zzgk();
                    return;
                }
                return;
            case 1:
                throw new zza("No fill from any mediation ad networks.", 3);
            default:
                throw new zza("Unexpected mediation result: " + this.zzGz.zzCo, 0);
        }
    }

    /* access modifiers changed from: 0000 */
    public zzem zzi(long j) {
        if (this.zzCf.zzBX != -1) {
            return new zzeu(this.mContext, this.zzGd.zzLd, this.zzpn, this.zzCf, this.zzGe.zzuk, this.zzGe.zzum, j, ((Long) zzbt.zzwY.get()).longValue(), 2);
        }
        return new zzev(this.mContext, this.zzGd.zzLd, this.zzpn, this.zzCf, this.zzGe.zzuk, this.zzGe.zzum, j, ((Long) zzbt.zzwY.get()).longValue(), this.zzpe);
    }
}
