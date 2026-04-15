package com.google.android.gms.ads.internal.purchase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzfl;
import com.google.android.gms.internal.zzfn.zza;
import com.google.android.gms.internal.zzgk;

@zzgk
public class zze extends zza implements ServiceConnection {
    private final Activity mActivity;
    private zzb zzBQ;
    zzh zzBR;
    private zzk zzBT;
    private Context zzBY;
    private zzfl zzBZ;
    private zzf zzCa;
    private zzj zzCb;
    private String zzCc = null;

    public zze(Activity activity) {
        this.mActivity = activity;
        this.zzBR = zzh.zzx(this.mActivity.getApplicationContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            boolean z = false;
            try {
                int zzd = zzp.zzbH().zzd(data);
                if (resultCode == -1) {
                    zzp.zzbH();
                    if (zzd == 0) {
                        if (this.zzBT.zza(this.zzCc, resultCode, data)) {
                            z = true;
                        }
                        this.zzBZ.recordPlayBillingResolution(zzd);
                        this.mActivity.finish();
                        zza(this.zzBZ.getProductId(), z, resultCode, data);
                    }
                }
                this.zzBR.zza(this.zzCa);
                this.zzBZ.recordPlayBillingResolution(zzd);
                this.mActivity.finish();
                zza(this.zzBZ.getProductId(), z, resultCode, data);
            } catch (RemoteException e) {
                zzb.zzaE("Fail to process purchase result.");
                this.mActivity.finish();
            } finally {
                this.zzCc = null;
            }
        }
    }

    public void onCreate() {
        GInAppPurchaseManagerInfoParcel zzc = GInAppPurchaseManagerInfoParcel.zzc(this.mActivity.getIntent());
        this.zzCb = zzc.zzBL;
        this.zzBT = zzc.zzqw;
        this.zzBZ = zzc.zzBJ;
        this.zzBQ = new zzb(this.mActivity.getApplicationContext());
        this.zzBY = zzc.zzBK;
        if (this.mActivity.getResources().getConfiguration().orientation == 2) {
            this.mActivity.setRequestedOrientation(zzp.zzbz().zzgv());
        } else {
            this.mActivity.setRequestedOrientation(zzp.zzbz().zzgw());
        }
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        this.mActivity.bindService(intent, this, 1);
    }

    public void onDestroy() {
        this.mActivity.unbindService(this);
        this.zzBQ.destroy();
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        this.zzBQ.zzM(service);
        try {
            this.zzCc = this.zzBT.zzfk();
            Bundle zzb = this.zzBQ.zzb(this.mActivity.getPackageName(), this.zzBZ.getProductId(), this.zzCc);
            PendingIntent pendingIntent = (PendingIntent) zzb.getParcelable("BUY_INTENT");
            if (pendingIntent == null) {
                int zzc = zzp.zzbH().zzc(zzb);
                this.zzBZ.recordPlayBillingResolution(zzc);
                zza(this.zzBZ.getProductId(), false, zzc, null);
                this.mActivity.finish();
                return;
            }
            this.zzCa = new zzf(this.zzBZ.getProductId(), this.zzCc);
            this.zzBR.zzb(this.zzCa);
            this.mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue());
        } catch (SendIntentException | RemoteException e) {
            zzb.zzd("Error when connecting in-app billing service", e);
            this.mActivity.finish();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
        zzb.zzaD("In-app billing service disconnected.");
        this.zzBQ.destroy();
    }

    /* access modifiers changed from: protected */
    public void zza(String str, boolean z, int i, Intent intent) {
        if (this.zzCb != null) {
            this.zzCb.zza(str, z, i, intent, this.zzCa);
        }
    }
}
