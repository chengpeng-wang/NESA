package com.google.android.gms.ads.internal.request;

import android.content.Context;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;

@zzhb
public final class zzc {

    public interface zza {
        void zzb(AdResponseParcel adResponseParcel);
    }

    interface zzb {
        boolean zza(VersionInfoParcel versionInfoParcel);
    }

    public static zzit zza(final Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> zzji, zza zza) {
        return zza(context, versionInfoParcel, zzji, zza, new zzb() {
            public boolean zza(VersionInfoParcel versionInfoParcel) {
                return versionInfoParcel.zzNb || (zze.zzap(context) && !((Boolean) zzbt.zzwb.get()).booleanValue());
            }
        });
    }

    static zzit zza(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> zzji, zza zza, zzb zzb) {
        return zzb.zza(versionInfoParcel) ? zza(context, zzji, zza) : zzb(context, versionInfoParcel, zzji, zza);
    }

    private static zzit zza(Context context, zzji<AdRequestInfoParcel> zzji, zza zza) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Fetching ad response from local ad request service.");
        com.google.android.gms.ads.internal.request.zzd.zza zza2 = new com.google.android.gms.ads.internal.request.zzd.zza(context, zzji, zza);
        zza2.zzgd();
        return zza2;
    }

    private static zzit zzb(Context context, VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> zzji, zza zza) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Fetching ad response from remote ad request service.");
        if (zzn.zzcS().zzU(context)) {
            return new com.google.android.gms.ads.internal.request.zzd.zzb(context, versionInfoParcel, zzji, zza);
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK("Failed to connect to remote ad request service.");
        return null;
    }
}
