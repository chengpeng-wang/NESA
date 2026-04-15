package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchase;

@zzgk
public class zzfu implements InAppPurchase {
    private final zzfl zzBZ;

    public zzfu(zzfl zzfl) {
        this.zzBZ = zzfl;
    }

    public String getProductId() {
        try {
            return this.zzBZ.getProductId();
        } catch (RemoteException e) {
            zzb.zzd("Could not forward getProductId to InAppPurchase", e);
            return null;
        }
    }

    public void recordPlayBillingResolution(int billingResponseCode) {
        try {
            this.zzBZ.recordPlayBillingResolution(billingResponseCode);
        } catch (RemoteException e) {
            zzb.zzd("Could not forward recordPlayBillingResolution to InAppPurchase", e);
        }
    }

    public void recordResolution(int resolution) {
        try {
            this.zzBZ.recordResolution(resolution);
        } catch (RemoteException e) {
            zzb.zzd("Could not forward recordResolution to InAppPurchase", e);
        }
    }
}
