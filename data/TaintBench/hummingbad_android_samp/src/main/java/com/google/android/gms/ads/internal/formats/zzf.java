package com.google.android.gms.ads.internal.formats;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzcj;
import com.google.android.gms.internal.zzcr.zza;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzlh;
import java.util.Arrays;
import java.util.List;

@zzgk
public class zzf extends zza implements zzh.zza {
    private final Object zzpc = new Object();
    private final zza zzvS;
    private zzh zzvT;
    private final String zzvW;
    private final zzlh<String, zzc> zzvX;
    private final zzlh<String, String> zzvY;

    public zzf(String str, zzlh<String, zzc> zzlh, zzlh<String, String> zzlh2, zza zza) {
        this.zzvW = str;
        this.zzvX = zzlh;
        this.zzvY = zzlh2;
        this.zzvS = zza;
    }

    public List<String> getAvailableAssetNames() {
        int i = 0;
        String[] strArr = new String[(this.zzvX.size() + this.zzvY.size())];
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzvX.size(); i3++) {
            strArr[i2] = (String) this.zzvX.keyAt(i3);
            i2++;
        }
        while (i < this.zzvY.size()) {
            strArr[i2] = (String) this.zzvY.keyAt(i);
            i++;
            i2++;
        }
        return Arrays.asList(strArr);
    }

    public String getCustomTemplateId() {
        return this.zzvW;
    }

    public void performClick(String assetName) {
        synchronized (this.zzpc) {
            if (this.zzvT == null) {
                zzb.e("Attempt to call performClick before ad initialized.");
                return;
            }
            this.zzvT.performClick(assetName);
        }
    }

    public void recordImpression() {
        synchronized (this.zzpc) {
            if (this.zzvT == null) {
                zzb.e("Attempt to perform recordImpression before ad initialized.");
                return;
            }
            this.zzvT.recordImpression();
        }
    }

    public String zzS(String str) {
        return (String) this.zzvY.get(str);
    }

    public zzcj zzT(String str) {
        return (zzcj) this.zzvX.get(str);
    }

    public void zza(zzh zzh) {
        synchronized (this.zzpc) {
            this.zzvT = zzh;
        }
    }

    public String zzdu() {
        return "3";
    }

    public zza zzdv() {
        return this.zzvS;
    }
}
