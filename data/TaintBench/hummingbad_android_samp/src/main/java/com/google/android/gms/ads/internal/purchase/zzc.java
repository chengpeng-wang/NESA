package com.google.android.gms.ads.internal.purchase;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzfq;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhq;
import com.google.android.gms.internal.zzhu;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@zzgk
public class zzc extends zzhq implements ServiceConnection {
    /* access modifiers changed from: private */
    public Context mContext;
    private boolean zzBO;
    /* access modifiers changed from: private */
    public zzfq zzBP;
    private zzb zzBQ;
    private zzh zzBR;
    private List<zzf> zzBS;
    /* access modifiers changed from: private */
    public zzk zzBT;
    private final Object zzpc;

    public zzc(Context context, zzfq zzfq, zzk zzk) {
        this(context, zzfq, zzk, new zzb(context), zzh.zzx(context.getApplicationContext()));
    }

    zzc(Context context, zzfq zzfq, zzk zzk, zzb zzb, zzh zzh) {
        this.zzpc = new Object();
        this.zzBO = false;
        this.zzBS = null;
        this.mContext = context;
        this.zzBP = zzfq;
        this.zzBT = zzk;
        this.zzBQ = zzb;
        this.zzBR = zzh;
        this.zzBS = this.zzBR.zzf(10);
    }

    private void zzd(long j) {
        do {
            if (!zze(j)) {
                zzb.v("Timeout waiting for pending transaction to be processed.");
            }
        } while (!this.zzBO);
    }

    private boolean zze(long j) {
        long elapsedRealtime = BuglyBroadcastRecevier.UPLOADLIMITED - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpc.wait(elapsedRealtime);
        } catch (InterruptedException e) {
            zzb.zzaE("waitWithTimeout_lock interrupted");
        }
        return true;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (this.zzpc) {
            this.zzBQ.zzM(service);
            zzfg();
            this.zzBO = true;
            this.zzpc.notify();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        zzb.zzaD("In-app billing service disconnected.");
        this.zzBQ.destroy();
    }

    public void onStop() {
        synchronized (this.zzpc) {
            com.google.android.gms.common.stats.zzb.zzpD().zza(this.mContext, this);
            this.zzBQ.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void zza(final zzf zzf, String str, String str2) {
        final Intent intent = new Intent();
        zzp.zzbH();
        intent.putExtra("RESPONSE_CODE", 0);
        zzp.zzbH();
        intent.putExtra("INAPP_PURCHASE_DATA", str);
        zzp.zzbH();
        intent.putExtra("INAPP_DATA_SIGNATURE", str2);
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                try {
                    if (zzc.this.zzBT.zza(zzf.zzCe, -1, intent)) {
                        zzc.this.zzBP.zza(new zzg(zzc.this.mContext, zzf.zzCf, true, -1, intent, zzf));
                    } else {
                        zzc.this.zzBP.zza(new zzg(zzc.this.mContext, zzf.zzCf, false, -1, intent, zzf));
                    }
                } catch (RemoteException e) {
                    zzb.zzaE("Fail to verify and dispatch pending transaction");
                }
            }
        });
    }

    public void zzdG() {
        synchronized (this.zzpc) {
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            com.google.android.gms.common.stats.zzb.zzpD().zza(this.mContext, intent, this, 1);
            zzd(SystemClock.elapsedRealtime());
            com.google.android.gms.common.stats.zzb.zzpD().zza(this.mContext, this);
            this.zzBQ.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void zzfg() {
        if (!this.zzBS.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (zzf zzf : this.zzBS) {
                hashMap.put(zzf.zzCf, zzf);
            }
            String str = null;
            while (true) {
                Bundle zzj = this.zzBQ.zzj(this.mContext.getPackageName(), str);
                if (zzj == null || zzp.zzbH().zzc(zzj) != 0) {
                    break;
                }
                ArrayList stringArrayList = zzj.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList stringArrayList2 = zzj.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList stringArrayList3 = zzj.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String string = zzj.getString("INAPP_CONTINUATION_TOKEN");
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= stringArrayList.size()) {
                        break;
                    }
                    if (hashMap.containsKey(stringArrayList.get(i2))) {
                        str = (String) stringArrayList.get(i2);
                        String str2 = (String) stringArrayList2.get(i2);
                        String str3 = (String) stringArrayList3.get(i2);
                        zzf zzf2 = (zzf) hashMap.get(str);
                        if (zzf2.zzCe.equals(zzp.zzbH().zzal(str2))) {
                            zza(zzf2, str2, str3);
                            hashMap.remove(str);
                        }
                    }
                    i = i2 + 1;
                }
                if (string == null || hashMap.isEmpty()) {
                    break;
                }
                str = string;
            }
            for (String str4 : hashMap.keySet()) {
                this.zzBR.zza((zzf) hashMap.get(str4));
            }
        }
    }
}
