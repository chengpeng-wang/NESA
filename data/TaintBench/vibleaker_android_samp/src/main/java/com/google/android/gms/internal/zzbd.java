package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzhb
public class zzbd {
    private final Object zzpV = new Object();
    private int zzsW;
    private List<zzbc> zzsX = new LinkedList();

    public boolean zza(zzbc zzbc) {
        boolean z;
        synchronized (this.zzpV) {
            if (this.zzsX.contains(zzbc)) {
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public boolean zzb(zzbc zzbc) {
        boolean z;
        synchronized (this.zzpV) {
            Iterator it = this.zzsX.iterator();
            while (it.hasNext()) {
                zzbc zzbc2 = (zzbc) it.next();
                if (zzbc != zzbc2 && zzbc2.zzcy().equals(zzbc.zzcy())) {
                    it.remove();
                    z = true;
                    break;
                }
            }
            z = false;
        }
        return z;
    }

    public void zzc(zzbc zzbc) {
        synchronized (this.zzpV) {
            if (this.zzsX.size() >= 10) {
                zzb.zzaI("Queue is full, current size = " + this.zzsX.size());
                this.zzsX.remove(0);
            }
            int i = this.zzsW;
            this.zzsW = i + 1;
            zzbc.zzh(i);
            this.zzsX.add(zzbc);
        }
    }

    public zzbc zzcF() {
        zzbc zzbc = null;
        synchronized (this.zzpV) {
            zzbc zzbc2;
            if (this.zzsX.size() == 0) {
                zzb.zzaI("Queue empty");
                return null;
            } else if (this.zzsX.size() >= 2) {
                int i = Integer.MIN_VALUE;
                for (zzbc zzbc22 : this.zzsX) {
                    zzbc zzbc3;
                    int i2;
                    int score = zzbc22.getScore();
                    if (score > i) {
                        int i3 = score;
                        zzbc3 = zzbc22;
                        i2 = i3;
                    } else {
                        i2 = i;
                        zzbc3 = zzbc;
                    }
                    i = i2;
                    zzbc = zzbc3;
                }
                this.zzsX.remove(zzbc);
                return zzbc;
            } else {
                zzbc22 = (zzbc) this.zzsX.get(0);
                zzbc22.zzcA();
                return zzbc22;
            }
        }
    }
}
