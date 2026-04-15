package com.google.android.gms.ads.internal.formats;

import android.os.Bundle;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcj;
import com.google.android.gms.internal.zzcn.zza;
import com.google.android.gms.internal.zzgk;
import java.util.List;

@zzgk
public class zzd extends zza implements zzh.zza {
    private final Bundle mExtras;
    private final Object zzpc = new Object();
    private final String zzvK;
    private final List<zzc> zzvL;
    private final String zzvM;
    private final zzcj zzvN;
    private final String zzvO;
    private final double zzvP;
    private final String zzvQ;
    private final String zzvR;
    private final zza zzvS;
    private zzh zzvT;

    public zzd(String str, List list, String str2, zzcj zzcj, String str3, double d, String str4, String str5, zza zza, Bundle bundle) {
        this.zzvK = str;
        this.zzvL = list;
        this.zzvM = str2;
        this.zzvN = zzcj;
        this.zzvO = str3;
        this.zzvP = d;
        this.zzvQ = str4;
        this.zzvR = str5;
        this.zzvS = zza;
        this.mExtras = bundle;
    }

    public String getBody() {
        return this.zzvM;
    }

    public String getCallToAction() {
        return this.zzvO;
    }

    public String getCustomTemplateId() {
        return "";
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public String getHeadline() {
        return this.zzvK;
    }

    public List getImages() {
        return this.zzvL;
    }

    public String getPrice() {
        return this.zzvR;
    }

    public double getStarRating() {
        return this.zzvP;
    }

    public String getStore() {
        return this.zzvQ;
    }

    public void zza(zzh zzh) {
        synchronized (this.zzpc) {
            this.zzvT = zzh;
        }
    }

    public zzcj zzds() {
        return this.zzvN;
    }

    public com.google.android.gms.dynamic.zzd zzdt() {
        return zze.zzx(this.zzvT);
    }

    public String zzdu() {
        return "2";
    }

    public zza zzdv() {
        return this.zzvS;
    }
}
