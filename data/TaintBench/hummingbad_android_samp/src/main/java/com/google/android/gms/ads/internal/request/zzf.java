package com.google.android.gms.ads.internal.request;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;

public class zzf implements Creator<AdRequestInfoParcel> {
    static void zza(AdRequestInfoParcel adRequestInfoParcel, Parcel parcel, int i) {
        int zzak = zzb.zzak(parcel);
        zzb.zzc(parcel, 1, adRequestInfoParcel.versionCode);
        zzb.zza(parcel, 2, adRequestInfoParcel.zzDx, false);
        zzb.zza(parcel, 3, adRequestInfoParcel.zzDy, i, false);
        zzb.zza(parcel, 4, adRequestInfoParcel.zzqf, i, false);
        zzb.zza(parcel, 5, adRequestInfoParcel.zzpZ, false);
        zzb.zza(parcel, 6, adRequestInfoParcel.applicationInfo, i, false);
        zzb.zza(parcel, 7, adRequestInfoParcel.zzDz, i, false);
        zzb.zza(parcel, 8, adRequestInfoParcel.zzDA, false);
        zzb.zza(parcel, 9, adRequestInfoParcel.zzDB, false);
        zzb.zza(parcel, 10, adRequestInfoParcel.zzDC, false);
        zzb.zza(parcel, 11, adRequestInfoParcel.zzqb, i, false);
        zzb.zza(parcel, 12, adRequestInfoParcel.zzDD, false);
        zzb.zzc(parcel, 13, adRequestInfoParcel.zzDE);
        zzb.zzb(parcel, 14, adRequestInfoParcel.zzqv, false);
        zzb.zza(parcel, 15, adRequestInfoParcel.zzDF, false);
        zzb.zza(parcel, 17, adRequestInfoParcel.zzDH, i, false);
        zzb.zza(parcel, 16, adRequestInfoParcel.zzDG);
        zzb.zzc(parcel, 19, adRequestInfoParcel.zzDJ);
        zzb.zzc(parcel, 18, adRequestInfoParcel.zzDI);
        zzb.zza(parcel, 21, adRequestInfoParcel.zzDL, false);
        zzb.zza(parcel, 20, adRequestInfoParcel.zzDK);
        zzb.zzc(parcel, 23, adRequestInfoParcel.zzDN);
        zzb.zza(parcel, 22, adRequestInfoParcel.zzDM);
        zzb.zza(parcel, 25, adRequestInfoParcel.zzDP);
        zzb.zza(parcel, 24, adRequestInfoParcel.zzDO, false);
        zzb.zzb(parcel, 27, adRequestInfoParcel.zzDR, false);
        zzb.zza(parcel, 26, adRequestInfoParcel.zzDQ, false);
        zzb.zza(parcel, 29, adRequestInfoParcel.zzqt, i, false);
        zzb.zza(parcel, 28, adRequestInfoParcel.zzpY, false);
        zzb.zza(parcel, 31, adRequestInfoParcel.zzDT);
        zzb.zzb(parcel, 30, adRequestInfoParcel.zzDS, false);
        zzb.zza(parcel, 32, adRequestInfoParcel.zzDU, i, false);
        zzb.zzH(parcel, zzak);
    }

    /* renamed from: zzC */
    public AdRequestInfoParcel[] newArray(int i) {
        return new AdRequestInfoParcel[i];
    }

    /* renamed from: zzj */
    public AdRequestInfoParcel createFromParcel(Parcel parcel) {
        int zzaj = zza.zzaj(parcel);
        int i = 0;
        Bundle bundle = null;
        AdRequestParcel adRequestParcel = null;
        AdSizeParcel adSizeParcel = null;
        String str = null;
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        VersionInfoParcel versionInfoParcel = null;
        Bundle bundle2 = null;
        int i2 = 0;
        List list = null;
        Bundle bundle3 = null;
        boolean z = false;
        Messenger messenger = null;
        int i3 = 0;
        int i4 = 0;
        float f = 0.0f;
        String str5 = null;
        boolean z2 = false;
        int i5 = 0;
        String str6 = null;
        long j = 0;
        String str7 = null;
        List list2 = null;
        String str8 = null;
        NativeAdOptionsParcel nativeAdOptionsParcel = null;
        List list3 = null;
        long j2 = 0;
        CapabilityParcel capabilityParcel = null;
        while (parcel.dataPosition() < zzaj) {
            int zzai = zza.zzai(parcel);
            switch (zza.zzbH(zzai)) {
                case 1:
                    i = zza.zzg(parcel, zzai);
                    break;
                case 2:
                    bundle = zza.zzq(parcel, zzai);
                    break;
                case 3:
                    adRequestParcel = (AdRequestParcel) zza.zza(parcel, zzai, AdRequestParcel.CREATOR);
                    break;
                case 4:
                    adSizeParcel = (AdSizeParcel) zza.zza(parcel, zzai, AdSizeParcel.CREATOR);
                    break;
                case 5:
                    str = zza.zzo(parcel, zzai);
                    break;
                case 6:
                    applicationInfo = (ApplicationInfo) zza.zza(parcel, zzai, ApplicationInfo.CREATOR);
                    break;
                case 7:
                    packageInfo = (PackageInfo) zza.zza(parcel, zzai, PackageInfo.CREATOR);
                    break;
                case 8:
                    str2 = zza.zzo(parcel, zzai);
                    break;
                case 9:
                    str3 = zza.zzo(parcel, zzai);
                    break;
                case 10:
                    str4 = zza.zzo(parcel, zzai);
                    break;
                case 11:
                    versionInfoParcel = (VersionInfoParcel) zza.zza(parcel, zzai, VersionInfoParcel.CREATOR);
                    break;
                case 12:
                    bundle2 = zza.zzq(parcel, zzai);
                    break;
                case 13:
                    i2 = zza.zzg(parcel, zzai);
                    break;
                case 14:
                    list = zza.zzC(parcel, zzai);
                    break;
                case 15:
                    bundle3 = zza.zzq(parcel, zzai);
                    break;
                case 16:
                    z = zza.zzc(parcel, zzai);
                    break;
                case 17:
                    messenger = (Messenger) zza.zza(parcel, zzai, Messenger.CREATOR);
                    break;
                case 18:
                    i3 = zza.zzg(parcel, zzai);
                    break;
                case 19:
                    i4 = zza.zzg(parcel, zzai);
                    break;
                case 20:
                    f = zza.zzl(parcel, zzai);
                    break;
                case 21:
                    str5 = zza.zzo(parcel, zzai);
                    break;
                case 22:
                    z2 = zza.zzc(parcel, zzai);
                    break;
                case 23:
                    i5 = zza.zzg(parcel, zzai);
                    break;
                case 24:
                    str6 = zza.zzo(parcel, zzai);
                    break;
                case 25:
                    j = zza.zzi(parcel, zzai);
                    break;
                case 26:
                    str7 = zza.zzo(parcel, zzai);
                    break;
                case 27:
                    list2 = zza.zzC(parcel, zzai);
                    break;
                case 28:
                    str8 = zza.zzo(parcel, zzai);
                    break;
                case 29:
                    nativeAdOptionsParcel = (NativeAdOptionsParcel) zza.zza(parcel, zzai, NativeAdOptionsParcel.CREATOR);
                    break;
                case 30:
                    list3 = zza.zzC(parcel, zzai);
                    break;
                case 31:
                    j2 = zza.zzi(parcel, zzai);
                    break;
                case 32:
                    capabilityParcel = (CapabilityParcel) zza.zza(parcel, zzai, CapabilityParcel.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzai);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaj) {
            return new AdRequestInfoParcel(i, bundle, adRequestParcel, adSizeParcel, str, applicationInfo, packageInfo, str2, str3, str4, versionInfoParcel, bundle2, i2, list, bundle3, z, messenger, i3, i4, f, str5, z2, i5, str6, j, str7, list2, str8, nativeAdOptionsParcel, list3, j2, capabilityParcel);
        }
        throw new zza.zza("Overread allowed size end=" + zzaj, parcel);
    }
}
