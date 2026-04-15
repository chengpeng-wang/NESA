package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzn;

@zzgk
public class zzga {

    public interface zza {
        void zzb(zzhj zzhj);
    }

    public zzhq zza(Context context, com.google.android.gms.ads.internal.zza zza, com.google.android.gms.internal.zzhj.zza zza2, zzan zzan, zzip zzip, zzeh zzeh, zza zza3, zzcd zzcd) {
        Object zzgd;
        AdResponseParcel adResponseParcel = zza2.zzGM;
        if (adResponseParcel.zzDX) {
            zzgd = new zzgd(context, zza2, zzip, zzeh, zza3, zzcd);
        } else if (!adResponseParcel.zzsJ) {
            zzgd = adResponseParcel.zzEd ? new zzfy(context, zza2, zzip, zza3) : (((Boolean) zzby.zzuM.get()).booleanValue() && zzlv.zzpV() && !zzlv.isAtLeastL() && zzip.zzaN().zzsH) ? new zzgc(context, zza2, zzip, zza3) : new zzgb(context, zza2, zzip, zza3);
        } else if (zza instanceof zzn) {
            zzgd = new zzge(context, (zzn) zza, new zzbc(), zza2, zzan, zza3);
        } else {
            throw new IllegalArgumentException("Invalid NativeAdManager type. Found: " + (zza != null ? zza.getClass().getName() : "null") + "; Required: NativeAdManager.");
        }
        zzb.zzaC("AdRenderer: " + zzgd.getClass().getName());
        zzgd.zzgo();
        return zzgd;
    }
}
