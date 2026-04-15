package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd.zza;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzfl;
import com.google.android.gms.internal.zzgk;

@zzgk
public final class GInAppPurchaseManagerInfoParcel implements SafeParcelable {
    public static final zza CREATOR = new zza();
    public final int versionCode;
    public final zzfl zzBJ;
    public final Context zzBK;
    public final zzj zzBL;
    public final zzk zzqw;

    GInAppPurchaseManagerInfoParcel(int versionCode, IBinder wrappedInAppPurchaseVerifier, IBinder wrappedInAppPurchase, IBinder wrappedAppContext, IBinder wrappedOnPlayStorePurchaseFinishedListener) {
        this.versionCode = versionCode;
        this.zzqw = (zzk) zze.zzp(zza.zzbk(wrappedInAppPurchaseVerifier));
        this.zzBJ = (zzfl) zze.zzp(zza.zzbk(wrappedInAppPurchase));
        this.zzBK = (Context) zze.zzp(zza.zzbk(wrappedAppContext));
        this.zzBL = (zzj) zze.zzp(zza.zzbk(wrappedOnPlayStorePurchaseFinishedListener));
    }

    public GInAppPurchaseManagerInfoParcel(Context appContext, zzk inAppPurchaseVerifier, zzfl inAppPurchase, zzj onPlayStorePurchaseFinishedListener) {
        this.versionCode = 2;
        this.zzBK = appContext;
        this.zzqw = inAppPurchaseVerifier;
        this.zzBJ = inAppPurchase;
        this.zzBL = onPlayStorePurchaseFinishedListener;
    }

    public static void zza(Intent intent, GInAppPurchaseManagerInfoParcel gInAppPurchaseManagerInfoParcel) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo", gInAppPurchaseManagerInfoParcel);
        intent.putExtra("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo", bundle);
    }

    public static GInAppPurchaseManagerInfoParcel zzc(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo");
            bundleExtra.setClassLoader(GInAppPurchaseManagerInfoParcel.class.getClassLoader());
            return (GInAppPurchaseManagerInfoParcel) bundleExtra.getParcelable("com.google.android.gms.ads.internal.purchase.InAppPurchaseManagerInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzfc() {
        return zze.zzx(this.zzBL).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzfd() {
        return zze.zzx(this.zzqw).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzfe() {
        return zze.zzx(this.zzBJ).asBinder();
    }

    /* access modifiers changed from: 0000 */
    public IBinder zzff() {
        return zze.zzx(this.zzBK).asBinder();
    }
}
