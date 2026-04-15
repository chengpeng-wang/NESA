package com.google.android.gms.internal;

import android.content.Context;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

@zzgk
public class zzay implements zzba {
    private final VersionInfoParcel zzpa;
    private final Object zzpc = new Object();
    private final WeakHashMap<zzhj, zzaz> zzqM = new WeakHashMap();
    private final ArrayList<zzaz> zzqN = new ArrayList();
    private final Context zzqO;
    private final zzdv zzqP;

    public zzay(Context context, VersionInfoParcel versionInfoParcel, zzdv zzdv) {
        this.zzqO = context.getApplicationContext();
        this.zzpa = versionInfoParcel;
        this.zzqP = zzdv;
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzhj zzhj) {
        return zza(adSizeParcel, zzhj, zzhj.zzAR.getWebView());
    }

    public zzaz zza(AdSizeParcel adSizeParcel, zzhj zzhj, View view) {
        zzaz zzaz;
        synchronized (this.zzpc) {
            if (zzd(zzhj)) {
                zzaz = (zzaz) this.zzqM.get(zzhj);
            } else {
                zzaz = new zzaz(adSizeParcel, zzhj, this.zzpa, view, this.zzqP);
                zzaz.zza((zzba) this);
                this.zzqM.put(zzhj, zzaz);
                this.zzqN.add(zzaz);
            }
        }
        return zzaz;
    }

    public void zza(zzaz zzaz) {
        synchronized (this.zzpc) {
            if (!zzaz.zzcd()) {
                this.zzqN.remove(zzaz);
                Iterator it = this.zzqM.entrySet().iterator();
                while (it.hasNext()) {
                    if (((Entry) it.next()).getValue() == zzaz) {
                        it.remove();
                    }
                }
            }
        }
    }

    public boolean zzd(zzhj zzhj) {
        boolean z;
        synchronized (this.zzpc) {
            zzaz zzaz = (zzaz) this.zzqM.get(zzhj);
            z = zzaz != null && zzaz.zzcd();
        }
        return z;
    }

    public void zze(zzhj zzhj) {
        synchronized (this.zzpc) {
            zzaz zzaz = (zzaz) this.zzqM.get(zzhj);
            if (zzaz != null) {
                zzaz.zzcb();
            }
        }
    }

    public void zzf(zzhj zzhj) {
        synchronized (this.zzpc) {
            zzaz zzaz = (zzaz) this.zzqM.get(zzhj);
            if (zzaz != null) {
                zzaz.stop();
            }
        }
    }

    public void zzg(zzhj zzhj) {
        synchronized (this.zzpc) {
            zzaz zzaz = (zzaz) this.zzqM.get(zzhj);
            if (zzaz != null) {
                zzaz.pause();
            }
        }
    }

    public void zzh(zzhj zzhj) {
        synchronized (this.zzpc) {
            zzaz zzaz = (zzaz) this.zzqM.get(zzhj);
            if (zzaz != null) {
                zzaz.resume();
            }
        }
    }
}
