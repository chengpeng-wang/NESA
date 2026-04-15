package com.google.android.gms.internal;

import android.support.v4.widget.ExploreByTouchHelper;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzgk
public class zzbi {
    private final Object zzpc = new Object();
    private int zzrM;
    private List<zzbh> zzrN = new LinkedList();

    public boolean zza(zzbh zzbh) {
        boolean z;
        synchronized (this.zzpc) {
            if (this.zzrN.contains(zzbh)) {
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public boolean zzb(zzbh zzbh) {
        boolean z;
        synchronized (this.zzpc) {
            Iterator it = this.zzrN.iterator();
            while (it.hasNext()) {
                zzbh zzbh2 = (zzbh) it.next();
                if (zzbh != zzbh2 && zzbh2.zzcm().equals(zzbh.zzcm())) {
                    it.remove();
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }

    public void zzc(zzbh zzbh) {
        synchronized (this.zzpc) {
            if (this.zzrN.size() >= 10) {
                zzb.zzaC("Queue is full, current size = " + this.zzrN.size());
                this.zzrN.remove(0);
            }
            int i = this.zzrM;
            this.zzrM = i + 1;
            zzbh.zzg(i);
            this.zzrN.add(zzbh);
        }
    }

    public zzbh zzcs() {
        zzbh zzbh = null;
        synchronized (this.zzpc) {
            zzbh zzbh2;
            if (this.zzrN.size() == 0) {
                zzb.zzaC("Queue empty");
                return null;
            } else if (this.zzrN.size() >= 2) {
                int i = ExploreByTouchHelper.INVALID_ID;
                for (zzbh zzbh22 : this.zzrN) {
                    zzbh zzbh3;
                    int i2;
                    int score = zzbh22.getScore();
                    if (score > i) {
                        int i3 = score;
                        zzbh3 = zzbh22;
                        i2 = i3;
                    } else {
                        i2 = i;
                        zzbh3 = zzbh;
                    }
                    i = i2;
                    zzbh = zzbh3;
                }
                this.zzrN.remove(zzbh);
                return zzbh;
            } else {
                zzbh22 = (zzbh) this.zzrN.get(0);
                zzbh22.zzcn();
                return zzbh22;
            }
        }
    }
}
