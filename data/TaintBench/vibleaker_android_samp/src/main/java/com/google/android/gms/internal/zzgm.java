package com.google.android.gms.internal;

import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.internal.zzgh.zza;

@zzhb
public final class zzgm extends zza {
    private final PlayStorePurchaseListener zzuP;

    public zzgm(PlayStorePurchaseListener playStorePurchaseListener) {
        this.zzuP = playStorePurchaseListener;
    }

    public boolean isValidPurchase(String productId) {
        return this.zzuP.isValidPurchase(productId);
    }

    public void zza(zzgg zzgg) {
        this.zzuP.onInAppPurchaseFinished(new zzgk(zzgg));
    }
}
