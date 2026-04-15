package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.internal.zzr;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@zzhb
public class zzcb {
    private final Object zzpV = new Object();
    private final Map<String, String> zzxA = new LinkedHashMap();
    private String zzxB;
    private zzbz zzxC;
    private zzcb zzxD;
    boolean zzxi;
    private final List<zzbz> zzxz = new LinkedList();

    public zzcb(boolean z, String str, String str2) {
        this.zzxi = z;
        this.zzxA.put("action", str);
        this.zzxA.put("ad_format", str2);
    }

    public void zzN(String str) {
        if (this.zzxi) {
            synchronized (this.zzpV) {
                this.zzxB = str;
            }
        }
    }

    public boolean zza(zzbz zzbz, long j, String... strArr) {
        synchronized (this.zzpV) {
            for (String zzbz2 : strArr) {
                this.zzxz.add(new zzbz(j, zzbz2, zzbz));
            }
        }
        return true;
    }

    public boolean zza(zzbz zzbz, String... strArr) {
        return (!this.zzxi || zzbz == null) ? false : zza(zzbz, zzr.zzbG().elapsedRealtime(), strArr);
    }

    public zzbz zzb(long j) {
        return !this.zzxi ? null : new zzbz(j, null, null);
    }

    public void zzc(zzcb zzcb) {
        synchronized (this.zzpV) {
            this.zzxD = zzcb;
        }
    }

    public void zzc(String str, String str2) {
        if (this.zzxi && !TextUtils.isEmpty(str2)) {
            zzbv zzhb = zzr.zzbF().zzhb();
            if (zzhb != null) {
                synchronized (this.zzpV) {
                    zzhb.zzL(str).zza(this.zzxA, str, str2);
                }
            }
        }
    }

    public zzbz zzdB() {
        return zzb(zzr.zzbG().elapsedRealtime());
    }

    public void zzdC() {
        synchronized (this.zzpV) {
            this.zzxC = zzdB();
        }
    }

    public String zzdD() {
        String stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        synchronized (this.zzpV) {
            for (zzbz zzbz : this.zzxz) {
                long time = zzbz.getTime();
                String zzdy = zzbz.zzdy();
                zzbz zzbz2 = zzbz2.zzdz();
                if (zzbz2 != null && time > 0) {
                    stringBuilder2.append(zzdy).append('.').append(time - zzbz2.getTime()).append(',');
                }
            }
            this.zzxz.clear();
            if (!TextUtils.isEmpty(this.zzxB)) {
                stringBuilder2.append(this.zzxB);
            } else if (stringBuilder2.length() > 0) {
                stringBuilder2.setLength(stringBuilder2.length() - 1);
            }
            stringBuilder = stringBuilder2.toString();
        }
        return stringBuilder;
    }

    public zzbz zzdE() {
        zzbz zzbz;
        synchronized (this.zzpV) {
            zzbz = this.zzxC;
        }
        return zzbz;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> zzn() {
        Map<String, String> map;
        synchronized (this.zzpV) {
            zzbv zzhb = zzr.zzbF().zzhb();
            if (zzhb == null || this.zzxD == null) {
                map = this.zzxA;
            } else {
                map = zzhb.zza(this.zzxA, this.zzxD.zzn());
            }
        }
        return map;
    }
}
