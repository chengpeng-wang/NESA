package com.google.android.gms.ads.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzae implements Creator<SearchAdRequestParcel> {
    static void zza(SearchAdRequestParcel searchAdRequestParcel, Parcel parcel, int i) {
        int zzak = zzb.zzak(parcel);
        zzb.zzc(parcel, 1, searchAdRequestParcel.versionCode);
        zzb.zzc(parcel, 2, searchAdRequestParcel.zztA);
        zzb.zzc(parcel, 3, searchAdRequestParcel.backgroundColor);
        zzb.zzc(parcel, 4, searchAdRequestParcel.zztB);
        zzb.zzc(parcel, 5, searchAdRequestParcel.zztC);
        zzb.zzc(parcel, 6, searchAdRequestParcel.zztD);
        zzb.zzc(parcel, 7, searchAdRequestParcel.zztE);
        zzb.zzc(parcel, 8, searchAdRequestParcel.zztF);
        zzb.zzc(parcel, 9, searchAdRequestParcel.zztG);
        zzb.zza(parcel, 10, searchAdRequestParcel.zztH, false);
        zzb.zzc(parcel, 11, searchAdRequestParcel.zztI);
        zzb.zza(parcel, 12, searchAdRequestParcel.zztJ, false);
        zzb.zzc(parcel, 13, searchAdRequestParcel.zztK);
        zzb.zzc(parcel, 14, searchAdRequestParcel.zztL);
        zzb.zza(parcel, 15, searchAdRequestParcel.zztM, false);
        zzb.zzH(parcel, zzak);
    }

    /* renamed from: zze */
    public SearchAdRequestParcel createFromParcel(Parcel parcel) {
        int zzaj = zza.zzaj(parcel);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        String str = null;
        int i10 = 0;
        String str2 = null;
        int i11 = 0;
        int i12 = 0;
        String str3 = null;
        while (parcel.dataPosition() < zzaj) {
            int zzai = zza.zzai(parcel);
            switch (zza.zzbH(zzai)) {
                case 1:
                    i = zza.zzg(parcel, zzai);
                    break;
                case 2:
                    i2 = zza.zzg(parcel, zzai);
                    break;
                case 3:
                    i3 = zza.zzg(parcel, zzai);
                    break;
                case 4:
                    i4 = zza.zzg(parcel, zzai);
                    break;
                case 5:
                    i5 = zza.zzg(parcel, zzai);
                    break;
                case 6:
                    i6 = zza.zzg(parcel, zzai);
                    break;
                case 7:
                    i7 = zza.zzg(parcel, zzai);
                    break;
                case 8:
                    i8 = zza.zzg(parcel, zzai);
                    break;
                case 9:
                    i9 = zza.zzg(parcel, zzai);
                    break;
                case 10:
                    str = zza.zzo(parcel, zzai);
                    break;
                case 11:
                    i10 = zza.zzg(parcel, zzai);
                    break;
                case 12:
                    str2 = zza.zzo(parcel, zzai);
                    break;
                case 13:
                    i11 = zza.zzg(parcel, zzai);
                    break;
                case 14:
                    i12 = zza.zzg(parcel, zzai);
                    break;
                case 15:
                    str3 = zza.zzo(parcel, zzai);
                    break;
                default:
                    zza.zzb(parcel, zzai);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaj) {
            return new SearchAdRequestParcel(i, i2, i3, i4, i5, i6, i7, i8, i9, str, i10, str2, i11, i12, str3);
        }
        throw new zza.zza("Overread allowed size end=" + zzaj, parcel);
    }

    /* renamed from: zzo */
    public SearchAdRequestParcel[] newArray(int i) {
        return new SearchAdRequestParcel[i];
    }
}
