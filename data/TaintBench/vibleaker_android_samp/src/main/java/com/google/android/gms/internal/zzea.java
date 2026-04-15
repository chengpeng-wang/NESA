package com.google.android.gms.internal;

import android.content.MutableContextWrapper;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.zzk;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;
import java.util.LinkedList;

@zzhb
class zzea {
    /* access modifiers changed from: private|final */
    public final LinkedList<zza> zzAB = new LinkedList();
    private final int zzAC;
    /* access modifiers changed from: private|final */
    public final String zzpS;
    /* access modifiers changed from: private */
    public AdRequestParcel zzqH;

    class zza {
        zzk zzAD;
        zzdw zzAE = new zzdw();
        long zzAF;
        boolean zzAG;
        boolean zzAH;
        MutableContextWrapper zzAb;

        zza(zzdv zzdv) {
            zzdv zzec = zzdv.zzec();
            this.zzAb = zzdv.zzed();
            this.zzAD = zzec.zzX(zzea.this.zzpS);
            this.zzAE.zzc(this.zzAD);
        }

        private void zzek() {
            if (!this.zzAG && zzea.this.zzqH != null) {
                this.zzAH = this.zzAD.zzb(zzea.this.zzqH);
                this.zzAG = true;
                this.zzAF = zzr.zzbG().currentTimeMillis();
            }
        }

        /* access modifiers changed from: 0000 */
        public void zzc(zzdv zzdv) {
            this.zzAb.setBaseContext(zzdv.zzed().getBaseContext());
        }

        /* access modifiers changed from: 0000 */
        public void zzh(AdRequestParcel adRequestParcel) {
            if (adRequestParcel != null) {
                zzea.this.zzqH = adRequestParcel;
            }
            zzek();
            Iterator it = zzea.this.zzAB.iterator();
            while (it.hasNext()) {
                ((zza) it.next()).zzek();
            }
        }
    }

    zzea(AdRequestParcel adRequestParcel, String str, int i) {
        zzx.zzz(adRequestParcel);
        zzx.zzz(str);
        this.zzqH = adRequestParcel;
        this.zzpS = str;
        this.zzAC = i;
    }

    /* access modifiers changed from: 0000 */
    public String getAdUnitId() {
        return this.zzpS;
    }

    /* access modifiers changed from: 0000 */
    public int getNetworkType() {
        return this.zzAC;
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.zzAB.size();
    }

    /* access modifiers changed from: 0000 */
    public void zzb(zzdv zzdv) {
        zza zza = new zza(zzdv);
        this.zzAB.add(zza);
        zza.zzh(this.zzqH);
    }

    /* access modifiers changed from: 0000 */
    public AdRequestParcel zzei() {
        return this.zzqH;
    }

    /* access modifiers changed from: 0000 */
    public zza zzej() {
        return (zza) this.zzAB.remove();
    }
}
