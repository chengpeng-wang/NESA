package com.google.android.gms.ads.internal;

import android.os.Build.VERSION;
import com.google.android.gms.ads.internal.overlay.zze;
import com.google.android.gms.ads.internal.purchase.zzi;
import com.google.android.gms.ads.internal.request.zza;
import com.google.android.gms.internal.zzbv;
import com.google.android.gms.internal.zzbw;
import com.google.android.gms.internal.zzbx;
import com.google.android.gms.internal.zzcb;
import com.google.android.gms.internal.zzdq;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.internal.zzga;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzgs;
import com.google.android.gms.internal.zzhl;
import com.google.android.gms.internal.zzhu;
import com.google.android.gms.internal.zzhv;
import com.google.android.gms.internal.zzhz;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzlm;
import com.google.android.gms.internal.zzlo;

@zzgk
public class zzp {
    private static zzp zzpF;
    private static final Object zzpm = new Object();
    private final zza zzpG = new zza();
    private final com.google.android.gms.ads.internal.overlay.zza zzpH = new com.google.android.gms.ads.internal.overlay.zza();
    private final zze zzpI = new zze();
    private final zzga zzpJ = new zzga();
    private final zzhu zzpK = new zzhu();
    private final zzir zzpL = new zzir();
    private final zzhv zzpM = zzhv.zzL(VERSION.SDK_INT);
    private final zzhl zzpN = new zzhl(this.zzpK);
    private final zzlm zzpO = new zzlo();
    private final zzcb zzpP = new zzcb();
    private final zzgs zzpQ = new zzgs();
    private final zzbw zzpR = new zzbw();
    private final zzbv zzpS = new zzbv();
    private final zzbx zzpT = new zzbx();
    private final zzi zzpU = new zzi();
    private final zzhz zzpV = new zzhz();
    private final zzef zzpW = new zzef();
    private final zzdq zzpX = new zzdq();

    static {
        zza(new zzp());
    }

    protected zzp() {
    }

    protected static void zza(zzp zzp) {
        synchronized (zzpm) {
            zzpF = zzp;
        }
    }

    public static zzhl zzbA() {
        return zzbs().zzpN;
    }

    public static zzlm zzbB() {
        return zzbs().zzpO;
    }

    public static zzcb zzbC() {
        return zzbs().zzpP;
    }

    public static zzgs zzbD() {
        return zzbs().zzpQ;
    }

    public static zzbw zzbE() {
        return zzbs().zzpR;
    }

    public static zzbv zzbF() {
        return zzbs().zzpS;
    }

    public static zzbx zzbG() {
        return zzbs().zzpT;
    }

    public static zzi zzbH() {
        return zzbs().zzpU;
    }

    public static zzhz zzbI() {
        return zzbs().zzpV;
    }

    public static zzef zzbJ() {
        return zzbs().zzpW;
    }

    public static zzdq zzbK() {
        return zzbs().zzpX;
    }

    private static zzp zzbs() {
        zzp zzp;
        synchronized (zzpm) {
            zzp = zzpF;
        }
        return zzp;
    }

    public static zza zzbt() {
        return zzbs().zzpG;
    }

    public static com.google.android.gms.ads.internal.overlay.zza zzbu() {
        return zzbs().zzpH;
    }

    public static zze zzbv() {
        return zzbs().zzpI;
    }

    public static zzga zzbw() {
        return zzbs().zzpJ;
    }

    public static zzhu zzbx() {
        return zzbs().zzpK;
    }

    public static zzir zzby() {
        return zzbs().zzpL;
    }

    public static zzhv zzbz() {
        return zzbs().zzpM;
    }
}
