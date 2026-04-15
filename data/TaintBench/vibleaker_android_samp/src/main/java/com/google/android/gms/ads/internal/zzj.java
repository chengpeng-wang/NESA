package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzr;
import com.google.android.gms.ads.internal.client.zzs.zza;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;

@zzhb
public class zzj extends zza {
    private final Context mContext;
    private zzq zzpK;
    private NativeAdOptionsParcel zzpP;
    private zzx zzpR;
    private final String zzpS;
    private final VersionInfoParcel zzpT;
    private zzcr zzpY;
    private zzcs zzpZ;
    private final zzd zzpm;
    private final zzex zzpn;
    private SimpleArrayMap<String, zzct> zzqa = new SimpleArrayMap();
    private SimpleArrayMap<String, zzcu> zzqb = new SimpleArrayMap();

    public zzj(Context context, String str, zzex zzex, VersionInfoParcel versionInfoParcel, zzd zzd) {
        this.mContext = context;
        this.zzpS = str;
        this.zzpn = zzex;
        this.zzpT = versionInfoParcel;
        this.zzpm = zzd;
    }

    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) {
        this.zzpP = nativeAdOptionsParcel;
    }

    public void zza(zzcr zzcr) {
        this.zzpY = zzcr;
    }

    public void zza(zzcs zzcs) {
        this.zzpZ = zzcs;
    }

    public void zza(String str, zzcu zzcu, zzct zzct) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Custom template ID for native custom template ad is empty. Please provide a valid template id.");
        }
        this.zzqb.put(str, zzcu);
        this.zzqa.put(str, zzct);
    }

    public void zzb(zzq zzq) {
        this.zzpK = zzq;
    }

    public void zzb(zzx zzx) {
        this.zzpR = zzx;
    }

    public zzr zzbn() {
        return new zzi(this.mContext, this.zzpS, this.zzpn, this.zzpT, this.zzpK, this.zzpY, this.zzpZ, this.zzqb, this.zzqa, this.zzpP, this.zzpR, this.zzpm);
    }
}
