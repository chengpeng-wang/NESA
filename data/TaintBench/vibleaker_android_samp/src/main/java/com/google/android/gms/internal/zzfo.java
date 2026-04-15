package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import java.util.Map;

@zzhb
public class zzfo {
    private final boolean zzDp;
    private final String zzDq;
    private final zzjp zzpD;

    public zzfo(zzjp zzjp, Map<String, String> map) {
        this.zzpD = zzjp;
        this.zzDq = (String) map.get("forceOrientation");
        if (map.containsKey("allowOrientationChange")) {
            this.zzDp = Boolean.parseBoolean((String) map.get("allowOrientationChange"));
        } else {
            this.zzDp = true;
        }
    }

    public void execute() {
        if (this.zzpD == null) {
            zzb.zzaK("AdWebView is null");
            return;
        }
        int zzhw = "portrait".equalsIgnoreCase(this.zzDq) ? zzr.zzbE().zzhw() : "landscape".equalsIgnoreCase(this.zzDq) ? zzr.zzbE().zzhv() : this.zzDp ? -1 : zzr.zzbE().zzhx();
        this.zzpD.setRequestedOrientation(zzhw);
    }
}
