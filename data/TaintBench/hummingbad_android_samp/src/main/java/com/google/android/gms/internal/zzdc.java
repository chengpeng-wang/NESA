package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.Map;

@zzgk
public final class zzdc implements zzdg {
    private final zzdd zzwH;

    public zzdc(zzdd zzdd) {
        this.zzwH = zzdd;
    }

    public void zza(zzip zzip, Map<String, String> map) {
        String str = (String) map.get("name");
        if (str == null) {
            zzb.zzaE("App event with no name parameter.");
        } else {
            this.zzwH.onAppEvent(str, (String) map.get("info"));
        }
    }
}
