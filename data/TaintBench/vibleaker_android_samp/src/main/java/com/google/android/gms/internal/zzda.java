package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import gr.georkouk.kastorakiacounter_new.MyVariables;
import java.util.Map;

@zzhb
public final class zzda implements zzdf {
    private final zzdb zzyW;

    public zzda(zzdb zzdb) {
        this.zzyW = zzdb;
    }

    public void zza(zzjp zzjp, Map<String, String> map) {
        String str = (String) map.get(MyVariables.KEY_NAME);
        if (str == null) {
            zzb.zzaK("App event with no name parameter.");
        } else {
            this.zzyW.onAppEvent(str, (String) map.get("info"));
        }
    }
}
