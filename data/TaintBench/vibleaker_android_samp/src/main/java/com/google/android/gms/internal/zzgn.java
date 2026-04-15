package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzjq.zza;
import java.util.concurrent.atomic.AtomicBoolean;

@zzhb
public abstract class zzgn implements zzit<Void>, zza {
    protected final Context mContext;
    protected final zzgr.zza zzGc;
    protected final zzif.zza zzGd;
    protected AdResponseParcel zzGe;
    private Runnable zzGf;
    protected final Object zzGg = new Object();
    /* access modifiers changed from: private */
    public AtomicBoolean zzGh = new AtomicBoolean(true);
    protected final zzjp zzpD;

    protected zzgn(Context context, zzif.zza zza, zzjp zzjp, zzgr.zza zza2) {
        this.mContext = context;
        this.zzGd = zza;
        this.zzGe = this.zzGd.zzLe;
        this.zzpD = zzjp;
        this.zzGc = zza2;
    }

    private zzif zzD(int i) {
        AdRequestInfoParcel adRequestInfoParcel = this.zzGd.zzLd;
        return new zzif(adRequestInfoParcel.zzHt, this.zzpD, this.zzGe.zzBQ, i, this.zzGe.zzBR, this.zzGe.zzHV, this.zzGe.orientation, this.zzGe.zzBU, adRequestInfoParcel.zzHw, this.zzGe.zzHT, null, null, null, null, null, this.zzGe.zzHU, this.zzGd.zzrp, this.zzGe.zzHS, this.zzGd.zzKY, this.zzGe.zzHX, this.zzGe.zzHY, this.zzGd.zzKT, null, this.zzGe.zzIj, this.zzGe.zzIk, this.zzGe.zzIl, this.zzGe.zzIm);
    }

    public void cancel() {
        if (this.zzGh.getAndSet(false)) {
            this.zzpD.stopLoading();
            zzr.zzbE().zzi(this.zzpD);
            zzC(-1);
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    /* access modifiers changed from: protected */
    public void zzC(int i) {
        if (i != -2) {
            this.zzGe = new AdResponseParcel(i, this.zzGe.zzBU);
        }
        this.zzpD.zzhO();
        this.zzGc.zzb(zzD(i));
    }

    public void zza(zzjp zzjp, boolean z) {
        zzb.zzaI("WebView finished loading.");
        if (this.zzGh.getAndSet(false)) {
            zzC(z ? zzgc() : -1);
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    /* renamed from: zzga */
    public final Void zzgd() {
        zzx.zzcD("Webview render task needs to be called on UI thread.");
        this.zzGf = new Runnable() {
            public void run() {
                if (zzgn.this.zzGh.get()) {
                    zzb.e("Timed out waiting for WebView to finish loading.");
                    zzgn.this.cancel();
                }
            }
        };
        zzir.zzMc.postDelayed(this.zzGf, ((Long) zzbt.zzwY.get()).longValue());
        zzgb();
        return null;
    }

    public abstract void zzgb();

    /* access modifiers changed from: protected */
    public int zzgc() {
        return -2;
    }
}
