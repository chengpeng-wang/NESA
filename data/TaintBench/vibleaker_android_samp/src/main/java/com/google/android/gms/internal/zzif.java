package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

@zzhb
public class zzif {
    public final int errorCode;
    public final int orientation;
    public final List<String> zzBQ;
    public final List<String> zzBR;
    public final long zzBU;
    public final zzen zzCp;
    public final zzey zzCq;
    public final String zzCr;
    public final zzeq zzCs;
    public final zzjp zzED;
    public final long zzHS;
    public final boolean zzHT;
    public final long zzHU;
    public final List<String> zzHV;
    public final String zzHY;
    public final AdRequestParcel zzHt;
    public final String zzHw;
    @Nullable
    public final RewardItemParcel zzIj;
    @Nullable
    public final List<String> zzIl;
    public final boolean zzIm;
    public final JSONObject zzKT;
    public boolean zzKU;
    public final zzeo zzKV;
    public final AdSizeParcel zzKW;
    @Nullable
    public final List<String> zzKX;
    public final long zzKY;
    public final long zzKZ;
    public final com.google.android.gms.ads.internal.formats.zzh.zza zzLa;
    public boolean zzLb;
    public boolean zzLc;

    @zzhb
    public static final class zza {
        public final int errorCode;
        public final JSONObject zzKT;
        public final zzeo zzKV;
        public final long zzKY;
        public final long zzKZ;
        public final AdRequestInfoParcel zzLd;
        public final AdResponseParcel zzLe;
        public final AdSizeParcel zzrp;

        public zza(AdRequestInfoParcel adRequestInfoParcel, AdResponseParcel adResponseParcel, zzeo zzeo, AdSizeParcel adSizeParcel, int i, long j, long j2, JSONObject jSONObject) {
            this.zzLd = adRequestInfoParcel;
            this.zzLe = adResponseParcel;
            this.zzKV = zzeo;
            this.zzrp = adSizeParcel;
            this.errorCode = i;
            this.zzKY = j;
            this.zzKZ = j2;
            this.zzKT = jSONObject;
        }
    }

    public zzif(AdRequestParcel adRequestParcel, zzjp zzjp, List<String> list, int i, List<String> list2, List<String> list3, int i2, long j, String str, boolean z, zzen zzen, zzey zzey, String str2, zzeo zzeo, zzeq zzeq, long j2, AdSizeParcel adSizeParcel, long j3, long j4, long j5, String str3, JSONObject jSONObject, com.google.android.gms.ads.internal.formats.zzh.zza zza, RewardItemParcel rewardItemParcel, List<String> list4, List<String> list5, boolean z2) {
        this.zzLb = false;
        this.zzLc = false;
        this.zzHt = adRequestParcel;
        this.zzED = zzjp;
        this.zzBQ = zzj(list);
        this.errorCode = i;
        this.zzBR = zzj(list2);
        this.zzHV = zzj(list3);
        this.orientation = i2;
        this.zzBU = j;
        this.zzHw = str;
        this.zzHT = z;
        this.zzCp = zzen;
        this.zzCq = zzey;
        this.zzCr = str2;
        this.zzKV = zzeo;
        this.zzCs = zzeq;
        this.zzHU = j2;
        this.zzKW = adSizeParcel;
        this.zzHS = j3;
        this.zzKY = j4;
        this.zzKZ = j5;
        this.zzHY = str3;
        this.zzKT = jSONObject;
        this.zzLa = zza;
        this.zzIj = rewardItemParcel;
        this.zzKX = zzj(list4);
        this.zzIl = zzj(list5);
        this.zzIm = z2;
    }

    public zzif(zza zza, zzjp zzjp, zzen zzen, zzey zzey, String str, zzeq zzeq, com.google.android.gms.ads.internal.formats.zzh.zza zza2) {
        this(zza.zzLd.zzHt, zzjp, zza.zzLe.zzBQ, zza.errorCode, zza.zzLe.zzBR, zza.zzLe.zzHV, zza.zzLe.orientation, zza.zzLe.zzBU, zza.zzLd.zzHw, zza.zzLe.zzHT, zzen, zzey, str, zza.zzKV, zzeq, zza.zzLe.zzHU, zza.zzrp, zza.zzLe.zzHS, zza.zzKY, zza.zzKZ, zza.zzLe.zzHY, zza.zzKT, zza2, zza.zzLe.zzIj, zza.zzLe.zzIk, zza.zzLe.zzIk, zza.zzLe.zzIm);
    }

    @Nullable
    private static <T> List<T> zzj(@Nullable List<T> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public boolean zzcv() {
        return (this.zzED == null || this.zzED.zzhU() == null) ? false : this.zzED.zzhU().zzcv();
    }
}
