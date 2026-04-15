package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.common.stats.zzb;
import com.google.android.gms.internal.zzfp.zza;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class zzg extends zza implements ServiceConnection {
    private Context mContext;
    zzb zzBQ;
    private String zzBW;
    private zzf zzCa;
    private boolean zzCg = false;
    private int zzCh;
    private Intent zzCi;

    public zzg(Context context, String str, boolean z, int i, Intent intent, zzf zzf) {
        this.zzBW = str;
        this.zzCh = i;
        this.zzCi = intent;
        this.zzCg = z;
        this.mContext = context;
        this.zzCa = zzf;
    }

    public void finishPurchase() {
        int zzd = zzp.zzbH().zzd(this.zzCi);
        if (this.zzCh == -1 && zzd == 0) {
            this.zzBQ = new zzb(this.mContext);
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            zzb.zzpD().zza(this.mContext, intent, this, 1);
        }
    }

    public String getProductId() {
        return this.zzBW;
    }

    public Intent getPurchaseData() {
        return this.zzCi;
    }

    public int getResultCode() {
        return this.zzCh;
    }

    public boolean isVerified() {
        return this.zzCg;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaD("In-app billing service connected.");
        this.zzBQ.zzM(service);
        String zzam = zzp.zzbH().zzam(zzp.zzbH().zze(this.zzCi));
        if (zzam != null) {
            if (this.zzBQ.zzi(this.mContext.getPackageName(), zzam) == 0) {
                zzh.zzx(this.mContext).zza(this.zzCa);
            }
            zzb.zzpD().zza(this.mContext, this);
            this.zzBQ.destroy();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaD("In-app billing service disconnected.");
        this.zzBQ.destroy();
    }
}
