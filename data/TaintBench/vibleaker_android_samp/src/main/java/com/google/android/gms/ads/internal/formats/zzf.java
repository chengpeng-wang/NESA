package com.google.android.gms.ads.internal.formats;

import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzcp.zza;
import com.google.android.gms.internal.zzhb;
import java.util.Arrays;
import java.util.List;

@zzhb
public class zzf extends zza implements zzh.zza {
    private final Object zzpV = new Object();
    private final zza zzye;
    private zzh zzyf;
    private final String zzyi;
    private final SimpleArrayMap<String, zzc> zzyj;
    private final SimpleArrayMap<String, String> zzyk;

    public zzf(String str, SimpleArrayMap<String, zzc> simpleArrayMap, SimpleArrayMap<String, String> simpleArrayMap2, zza zza) {
        this.zzyi = str;
        this.zzyj = simpleArrayMap;
        this.zzyk = simpleArrayMap2;
        this.zzye = zza;
    }

    public List<String> getAvailableAssetNames() {
        int i = 0;
        String[] strArr = new String[(this.zzyj.size() + this.zzyk.size())];
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzyj.size(); i3++) {
            strArr[i2] = (String) this.zzyj.keyAt(i3);
            i2++;
        }
        while (i < this.zzyk.size()) {
            strArr[i2] = (String) this.zzyk.keyAt(i);
            i++;
            i2++;
        }
        return Arrays.asList(strArr);
    }

    public String getCustomTemplateId() {
        return this.zzyi;
    }

    public void performClick(String assetName) {
        synchronized (this.zzpV) {
            if (this.zzyf == null) {
                zzb.e("Attempt to call performClick before ad initialized.");
                return;
            }
            this.zzyf.zza(assetName, null, null, null);
        }
    }

    public void recordImpression() {
        synchronized (this.zzpV) {
            if (this.zzyf == null) {
                zzb.e("Attempt to perform recordImpression before ad initialized.");
                return;
            }
            this.zzyf.recordImpression();
        }
    }

    public String zzO(String str) {
        return (String) this.zzyk.get(str);
    }

    public zzch zzP(String str) {
        return (zzch) this.zzyj.get(str);
    }

    public void zzb(zzh zzh) {
        synchronized (this.zzpV) {
            this.zzyf = zzh;
        }
    }

    public String zzdM() {
        return "3";
    }

    public zza zzdN() {
        return this.zzye;
    }
}
