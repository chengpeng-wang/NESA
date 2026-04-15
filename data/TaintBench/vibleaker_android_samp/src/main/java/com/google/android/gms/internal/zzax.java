package com.google.android.gms.internal;

import android.content.Context;
import android.view.View;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzau.zza;
import com.google.android.gms.internal.zzau.zzd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

@zzhb
public class zzax implements zzay {
    private final VersionInfoParcel zzpT;
    private final Object zzpV = new Object();
    private final WeakHashMap<zzif, zzau> zzsB = new WeakHashMap();
    private final ArrayList<zzau> zzsC = new ArrayList();
    private final zzeg zzsD;
    private final Context zzsa;

    public zzax(Context context, VersionInfoParcel versionInfoParcel, zzeg zzeg) {
        this.zzsa = context.getApplicationContext();
        this.zzpT = versionInfoParcel;
        this.zzsD = zzeg;
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif zzif) {
        return zza(adSizeParcel, zzif, zzif.zzED.getView());
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif zzif, View view) {
        return zza(adSizeParcel, zzif, new zzd(view, zzif), null);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif zzif, View view, zzeh zzeh) {
        return zza(adSizeParcel, zzif, new zzd(view, zzif), zzeh);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif zzif, zzh zzh) {
        return zza(adSizeParcel, zzif, new zza(zzh), null);
    }

    public zzau zza(AdSizeParcel adSizeParcel, zzif zzif, zzbb zzbb, zzeh zzeh) {
        zzau zzau;
        synchronized (this.zzpV) {
            if (zzh(zzif)) {
                zzau = (zzau) this.zzsB.get(zzif);
            } else {
                if (zzeh != null) {
                    zzau = new zzaz(this.zzsa, adSizeParcel, zzif, this.zzpT, zzbb, zzeh);
                } else {
                    zzau = new zzba(this.zzsa, adSizeParcel, zzif, this.zzpT, zzbb, this.zzsD);
                }
                zzau.zza((zzay) this);
                this.zzsB.put(zzif, zzau);
                this.zzsC.add(zzau);
            }
        }
        return zzau;
    }

    public void zza(zzau zzau) {
        synchronized (this.zzpV) {
            if (!zzau.zzch()) {
                this.zzsC.remove(zzau);
                Iterator it = this.zzsB.entrySet().iterator();
                while (it.hasNext()) {
                    if (((Entry) it.next()).getValue() == zzau) {
                        it.remove();
                    }
                }
            }
        }
    }

    public boolean zzh(zzif zzif) {
        boolean z;
        synchronized (this.zzpV) {
            zzau zzau = (zzau) this.zzsB.get(zzif);
            z = zzau != null && zzau.zzch();
        }
        return z;
    }

    public void zzi(zzif zzif) {
        synchronized (this.zzpV) {
            zzau zzau = (zzau) this.zzsB.get(zzif);
            if (zzau != null) {
                zzau.zzcf();
            }
        }
    }

    public void zzj(zzif zzif) {
        synchronized (this.zzpV) {
            zzau zzau = (zzau) this.zzsB.get(zzif);
            if (zzau != null) {
                zzau.stop();
            }
        }
    }

    public void zzk(zzif zzif) {
        synchronized (this.zzpV) {
            zzau zzau = (zzau) this.zzsB.get(zzif);
            if (zzau != null) {
                zzau.pause();
            }
        }
    }

    public void zzl(zzif zzif) {
        synchronized (this.zzpV) {
            zzau zzau = (zzau) this.zzsB.get(zzif);
            if (zzau != null) {
                zzau.resume();
            }
        }
    }
}
