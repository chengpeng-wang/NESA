package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zze;
import com.google.android.gms.internal.zzgw.zza;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzgy implements zza<zze> {
    private final boolean zzHc;
    private final boolean zzHd;

    public zzgy(boolean z, boolean z2) {
        this.zzHc = z;
        this.zzHd = z2;
    }

    /* renamed from: zzc */
    public zze zza(zzgw zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        List<zzjg> zza = zzgw.zza(jSONObject, "images", true, this.zzHc, this.zzHd);
        zzjg zza2 = zzgw.zza(jSONObject, "secondary_image", false, this.zzHc);
        zzjg zzf = zzgw.zzf(jSONObject);
        ArrayList arrayList = new ArrayList();
        for (zzjg zzjg : zza) {
            arrayList.add(zzjg.get());
        }
        return new zze(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), (zzch) zza2.get(), jSONObject.getString("call_to_action"), jSONObject.getString("advertiser"), (com.google.android.gms.ads.internal.formats.zza) zzf.get(), new Bundle());
    }
}
