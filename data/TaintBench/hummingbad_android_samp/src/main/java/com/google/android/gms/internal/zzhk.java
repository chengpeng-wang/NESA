package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.zzp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

@zzgk
public class zzhk {
    private boolean zzFm;
    private final LinkedList<zza> zzGN;
    private final String zzGO;
    private final String zzGP;
    private long zzGQ;
    private long zzGR;
    private long zzGS;
    private long zzGT;
    private long zzGU;
    private long zzGV;
    private final zzhl zzpN;
    private final Object zzpc;

    @zzgk
    private static final class zza {
        private long zzGW = -1;
        private long zzGX = -1;

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putLong("topen", this.zzGW);
            bundle.putLong("tclose", this.zzGX);
            return bundle;
        }

        public long zzfW() {
            return this.zzGX;
        }

        public void zzfX() {
            this.zzGX = SystemClock.elapsedRealtime();
        }

        public void zzfY() {
            this.zzGW = SystemClock.elapsedRealtime();
        }
    }

    public zzhk(zzhl zzhl, String str, String str2) {
        this.zzpc = new Object();
        this.zzGQ = -1;
        this.zzGR = -1;
        this.zzFm = false;
        this.zzGS = -1;
        this.zzGT = 0;
        this.zzGU = -1;
        this.zzGV = -1;
        this.zzpN = zzhl;
        this.zzGO = str;
        this.zzGP = str2;
        this.zzGN = new LinkedList();
    }

    public zzhk(String str, String str2) {
        this(zzp.zzbA(), str, str2);
    }

    public Bundle toBundle() {
        Bundle bundle;
        synchronized (this.zzpc) {
            bundle = new Bundle();
            bundle.putString("seq_num", this.zzGO);
            bundle.putString("slotid", this.zzGP);
            bundle.putBoolean("ismediation", this.zzFm);
            bundle.putLong("treq", this.zzGU);
            bundle.putLong("tresponse", this.zzGV);
            bundle.putLong("timp", this.zzGR);
            bundle.putLong("tload", this.zzGS);
            bundle.putLong("pcc", this.zzGT);
            bundle.putLong("tfetch", this.zzGQ);
            ArrayList arrayList = new ArrayList();
            Iterator it = this.zzGN.iterator();
            while (it.hasNext()) {
                arrayList.add(((zza) it.next()).toBundle());
            }
            bundle.putParcelableArrayList("tclick", arrayList);
        }
        return bundle;
    }

    public void zzfT() {
        synchronized (this.zzpc) {
            if (this.zzGV != -1 && this.zzGR == -1) {
                this.zzGR = SystemClock.elapsedRealtime();
                this.zzpN.zza(this);
            }
            this.zzpN.zzgb().zzfT();
        }
    }

    public void zzfU() {
        synchronized (this.zzpc) {
            if (this.zzGV != -1) {
                zza zza = new zza();
                zza.zzfY();
                this.zzGN.add(zza);
                this.zzGT++;
                this.zzpN.zzgb().zzfU();
                this.zzpN.zza(this);
            }
        }
    }

    public void zzfV() {
        synchronized (this.zzpc) {
            if (!(this.zzGV == -1 || this.zzGN.isEmpty())) {
                zza zza = (zza) this.zzGN.getLast();
                if (zza.zzfW() == -1) {
                    zza.zzfX();
                    this.zzpN.zza(this);
                }
            }
        }
    }

    public void zzh(AdRequestParcel adRequestParcel) {
        synchronized (this.zzpc) {
            this.zzGU = SystemClock.elapsedRealtime();
            this.zzpN.zzgb().zzb(adRequestParcel, this.zzGU);
        }
    }

    public void zzl(long j) {
        synchronized (this.zzpc) {
            this.zzGV = j;
            if (this.zzGV != -1) {
                this.zzpN.zza(this);
            }
        }
    }

    public void zzm(long j) {
        synchronized (this.zzpc) {
            if (this.zzGV != -1) {
                this.zzGQ = j;
                this.zzpN.zza(this);
            }
        }
    }

    public void zzy(boolean z) {
        synchronized (this.zzpc) {
            if (this.zzGV != -1) {
                this.zzGS = SystemClock.elapsedRealtime();
                if (!z) {
                    this.zzGR = this.zzGS;
                    this.zzpN.zza(this);
                }
            }
        }
    }

    public void zzz(boolean z) {
        synchronized (this.zzpc) {
            if (this.zzGV != -1) {
                this.zzFm = z;
                this.zzpN.zza(this);
            }
        }
    }
}
