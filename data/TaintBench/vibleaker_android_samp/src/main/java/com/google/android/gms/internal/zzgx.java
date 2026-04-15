package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zzd;
import com.google.android.gms.internal.zzgw.zza;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzgx implements zza<zzd> {
    private final boolean zzHc;
    private final boolean zzHd;

    public zzgx(boolean z, boolean z2) {
        this.zzHc = z;
        this.zzHd = z2;
    }

    /* renamed from: zzb */
    public zzd zza(zzgw zzgw, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        List<zzjg> zza = zzgw.zza(jSONObject, "images", true, this.zzHc, this.zzHd);
        zzjg zza2 = zzgw.zza(jSONObject, "app_icon", true, this.zzHc);
        zzjg zzf = zzgw.zzf(jSONObject);
        ArrayList arrayList = new ArrayList();
        for (zzjg zzjg : zza) {
            arrayList.add(zzjg.get());
        }
        return new zzd(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), (zzch) zza2.get(), jSONObject.getString("call_to_action"), jSONObject.optDouble("rating", -1.0d), jSONObject.optString("store"), jSONObject.optString("price"), (com.google.android.gms.ads.internal.formats.zza) zzf.get(), new Bundle());
    }
}
