package com.google.android.gms.ads.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzo;
import com.google.android.gms.ads.internal.client.zzp.zza;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzcw;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzlh;

@zzgk
public class zzj extends zza {
    private final Context mContext;
    private zzn zzoS;
    private NativeAdOptionsParcel zzoX;
    private final String zzoZ;
    private final zzeh zzow;
    private final VersionInfoParcel zzpa;
    private zzct zzpf;
    private zzcu zzpg;
    private zzlh<String, zzcv> zzph = new zzlh();
    private zzlh<String, zzcw> zzpi = new zzlh();

    public zzj(Context context, String str, zzeh zzeh, VersionInfoParcel versionInfoParcel) {
        this.mContext = context;
        this.zzoZ = str;
        this.zzow = zzeh;
        this.zzpa = versionInfoParcel;
    }

    public void zza(NativeAdOptionsParcel nativeAdOptionsParcel) {
        this.zzoX = nativeAdOptionsParcel;
    }

    public void zza(zzct zzct) {
        this.zzpf = zzct;
    }

    public void zza(zzcu zzcu) {
        this.zzpg = zzcu;
    }

    public void zza(String str, zzcw zzcw, zzcv zzcv) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Custom template ID for native custom template ad is empty. Please provide a valid template id.");
        }
        this.zzpi.put(str, zzcw);
        this.zzph.put(str, zzcv);
    }

    public void zzb(zzn zzn) {
        this.zzoS = zzn;
    }

    public zzo zzbk() {
        return new zzi(this.mContext, this.zzoZ, this.zzow, this.zzpa, this.zzoS, this.zzpf, this.zzpg, this.zzpi, this.zzph, this.zzoX);
    }
}
