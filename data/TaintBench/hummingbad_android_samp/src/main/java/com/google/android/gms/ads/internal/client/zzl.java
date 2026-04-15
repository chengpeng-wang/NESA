package com.google.android.gms.ads.internal.client;

import com.google.android.gms.ads.internal.client.zzu.zza;
import java.util.Random;

public class zzl extends zza {
    private Object zzpc = new Object();
    private final Random zzsT = new Random();
    private long zzsU;

    public zzl() {
        zzcK();
    }

    public long getValue() {
        return this.zzsU;
    }

    public void zzcK() {
        synchronized (this.zzpc) {
            int i = 3;
            long j = 0;
            while (true) {
                i--;
                if (i <= 0) {
                    break;
                }
                j = ((long) this.zzsT.nextInt()) + 2147483648L;
                if (j != this.zzsU && j != 0) {
                    break;
                }
            }
            this.zzsU = j;
        }
    }
}
