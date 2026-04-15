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
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzgh;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zzin;
import com.google.android.gms.internal.zzir;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@zzhb
public class zzc extends zzim implements ServiceConnection {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public zzgh zzAK;
    private boolean zzFB;
    private zzb zzFC;
    private zzh zzFD;
    private List<zzf> zzFE;
    /* access modifiers changed from: private */
    public zzk zzFF;
    private final Object zzpV;

    public zzc(Context context, zzgh zzgh, zzk zzk) {
        this(context, zzgh, zzk, new zzb(context), zzh.zzy(context.getApplicationContext()));
    }

    zzc(Context context, zzgh zzgh, zzk zzk, zzb zzb, zzh zzh) {
        this.zzpV = new Object();
        this.zzFB = false;
        this.zzFE = null;
        this.mContext = context;
        this.zzAK = zzgh;
        this.zzFF = zzk;
        this.zzFC = zzb;
        this.zzFD = zzh;
        this.zzFE = this.zzFD.zzg(10);
    }

    private void zze(long j) {
        do {
            if (!zzf(j)) {
                zzin.v("Timeout waiting for pending transaction to be processed.");
            }
        } while (!this.zzFB);
    }

    private boolean zzf(long j) {
        long elapsedRealtime = 60000 - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpV.wait(elapsedRealtime);
        } catch (InterruptedException e) {
            zzb.zzaK("waitWithTimeout_lock interrupted");
        }
        return true;
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (this.zzpV) {
            this.zzFC.zzN(service);
            zzfW();
            this.zzFB = true;
            this.zzpV.notify();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        zzb.zzaJ("In-app billing service disconnected.");
        this.zzFC.destroy();
    }

    public void onStop() {
        synchronized (this.zzpV) {
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, this);
            this.zzFC.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void zza(final zzf zzf, String str, String str2) {
        final Intent intent = new Intent();
        zzr.zzbM();
        intent.putExtra("RESPONSE_CODE", 0);
        zzr.zzbM();
        intent.putExtra("INAPP_PURCHASE_DATA", str);
        zzr.zzbM();
        intent.putExtra("INAPP_DATA_SIGNATURE", str2);
        zzir.zzMc.post(new Runnable() {
            public void run() {
                try {
                    if (zzc.this.zzFF.zza(zzf.zzFQ, -1, intent)) {
                        zzc.this.zzAK.zza(new zzg(zzc.this.mContext, zzf.zzFR, true, -1, intent, zzf));
                    } else {
                        zzc.this.zzAK.zza(new zzg(zzc.this.mContext, zzf.zzFR, false, -1, intent, zzf));
                    }
                } catch (RemoteException e) {
                    zzb.zzaK("Fail to verify and dispatch pending transaction");
                }
            }
        });
    }

    public void zzbr() {
        synchronized (this.zzpV) {
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage(zze.GOOGLE_PLAY_STORE_PACKAGE);
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, intent, (ServiceConnection) this, 1);
            zze(SystemClock.elapsedRealtime());
            com.google.android.gms.common.stats.zzb.zzrP().zza(this.mContext, this);
            this.zzFC.destroy();
        }
    }

    /* access modifiers changed from: protected */
    public void zzfW() {
        if (!this.zzFE.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (zzf zzf : this.zzFE) {
                hashMap.put(zzf.zzFR, zzf);
            }
            String str = null;
            while (true) {
                Bundle zzi = this.zzFC.zzi(this.mContext.getPackageName(), str);
                if (zzi == null || zzr.zzbM().zzd(zzi) != 0) {
                    break;
                }
                ArrayList stringArrayList = zzi.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList stringArrayList2 = zzi.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList stringArrayList3 = zzi.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String string = zzi.getString("INAPP_CONTINUATION_TOKEN");
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
                        if (zzf2.zzFQ.equals(zzr.zzbM().zzaq(str2))) {
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
                this.zzFD.zza((zzf) hashMap.get(str4));
            }
        }
    }
}
