package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.ads.internal.purchase.zze;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zzg;

@zzgk
public final class zzfs extends zzg<zzfo> {
    private static final zzfs zzCo = new zzfs();

    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    private zzfs() {
        super("com.google.android.gms.ads.InAppPurchaseManagerCreatorImpl");
    }

    private static boolean zzc(Activity activity) throws zza {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.purchase.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.purchase.useClientJar", false);
        }
        throw new zza("InAppPurchaseManager requires the useClientJar flag in intent extras.");
    }

    public static zzfn zze(Activity activity) {
        try {
            if (!zzc(activity)) {
                return zzCo.zzf(activity);
            }
            zzb.zzaC("Using AdOverlay from the client jar.");
            return new zze(activity);
        } catch (zza e) {
            zzb.zzaE(e.getMessage());
            return null;
        }
    }

    private zzfn zzf(Activity activity) {
        try {
            return com.google.android.gms.internal.zzfn.zza.zzP(((zzfo) zzar(activity)).zzf(com.google.android.gms.dynamic.zze.zzx(activity)));
        } catch (RemoteException e) {
            zzb.zzd("Could not create remote InAppPurchaseManager.", e);
            return null;
        } catch (com.google.android.gms.dynamic.zzg.zza e2) {
            zzb.zzd("Could not create remote InAppPurchaseManager.", e2);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzT */
    public zzfo zzd(IBinder iBinder) {
        return com.google.android.gms.internal.zzfo.zza.zzQ(iBinder);
    }
}
