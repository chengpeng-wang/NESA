package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.reward.client.zzf;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.internal.zzcx;
import com.google.android.gms.internal.zzgk;

@zzgk
public class zzk {
    private static final Object zzpm = new Object();
    private static zzk zzsM;
    private final zza zzsN = new zza();
    private final zze zzsO = new zze();
    private final zzl zzsP = new zzl();
    private final zzac zzsQ = new zzac();
    private final zzcx zzsR = new zzcx();
    private final zzf zzsS = new zzf();

    static {
        zza(new zzk());
    }

    protected zzk() {
    }

    protected static void zza(zzk zzk) {
        synchronized (zzpm) {
            zzsM = zzk;
        }
    }

    private static zzk zzcD() {
        zzk zzk;
        synchronized (zzpm) {
            zzk = zzsM;
        }
        return zzk;
    }

    public static zza zzcE() {
        return zzcD().zzsN;
    }

    public static zze zzcF() {
        return zzcD().zzsO;
    }

    public static zzl zzcG() {
        return zzcD().zzsP;
    }

    public static zzac zzcH() {
        return zzcD().zzsQ;
    }

    public static zzcx zzcI() {
        return zzcD().zzsR;
    }

    public static zzf zzcJ() {
        return zzcD().zzsS;
    }
}
