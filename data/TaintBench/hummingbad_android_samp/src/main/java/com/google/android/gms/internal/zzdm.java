package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zze;
import java.util.HashMap;
import java.util.Map;

@zzgk
public class zzdm implements zzdg {
    static final Map<String, Integer> zzxl = new HashMap();
    private final zze zzxj;
    private final zzew zzxk;

    static {
        zzxl.put("resize", Integer.valueOf(1));
        zzxl.put("playVideo", Integer.valueOf(2));
        zzxl.put("storePicture", Integer.valueOf(3));
        zzxl.put("createCalendarEvent", Integer.valueOf(4));
        zzxl.put("setOrientationProperties", Integer.valueOf(5));
        zzxl.put("closeResizedAd", Integer.valueOf(6));
    }

    public zzdm(zze zze, zzew zzew) {
        this.zzxj = zze;
        this.zzxk = zzew;
    }

    public void zza(zzip zzip, Map<String, String> map) {
        int intValue = ((Integer) zzxl.get((String) map.get("a"))).intValue();
        if (intValue == 5 || this.zzxj == null || this.zzxj.zzbe()) {
            switch (intValue) {
                case 1:
                    this.zzxk.zzg(map);
                    return;
                case 3:
                    new zzey(zzip, map).execute();
                    return;
                case 4:
                    new zzev(zzip, map).execute();
                    return;
                case 5:
                    new zzex(zzip, map).execute();
                    return;
                case 6:
                    this.zzxk.zzn(true);
                    return;
                default:
                    zzb.zzaD("Unknown MRAID command called.");
                    return;
            }
        }
        this.zzxj.zzp(null);
    }
}
