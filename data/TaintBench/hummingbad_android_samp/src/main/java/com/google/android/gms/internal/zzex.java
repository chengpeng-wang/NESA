package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import java.util.Map;

@zzgk
public class zzex {
    private final zzip zzoL;
    private final boolean zzzD;
    private final String zzzE;

    public zzex(zzip zzip, Map<String, String> map) {
        this.zzoL = zzip;
        this.zzzE = (String) map.get("forceOrientation");
        if (map.containsKey("allowOrientationChange")) {
            this.zzzD = Boolean.parseBoolean((String) map.get("allowOrientationChange"));
        } else {
            this.zzzD = true;
        }
    }

    public void execute() {
        if (this.zzoL == null) {
            zzb.zzaE("AdWebView is null");
            return;
        }
        int zzgw = "portrait".equalsIgnoreCase(this.zzzE) ? zzp.zzbz().zzgw() : "landscape".equalsIgnoreCase(this.zzzE) ? zzp.zzbz().zzgv() : this.zzzD ? -1 : zzp.zzbz().zzgx();
        this.zzoL.setRequestedOrientation(zzgw);
    }
}
