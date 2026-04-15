package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zzd;
import com.google.android.gms.internal.zzgf.zza;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzgg implements zza<zzd> {
    private final boolean zzDl;
    private final boolean zzDm;

    public zzgg(boolean z, boolean z2) {
        this.zzDl = z;
        this.zzDm = z2;
    }

    /* renamed from: zzb */
    public zzd zza(zzgf zzgf, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        List<zzih> zza = zzgf.zza(jSONObject, "images", true, this.zzDl, this.zzDm);
        zzih zza2 = zzgf.zza(jSONObject, "app_icon", true, this.zzDl);
        zzih zze = zzgf.zze(jSONObject);
        ArrayList arrayList = new ArrayList();
        for (zzih zzih : zza) {
            arrayList.add(zzih.get());
        }
        return new zzd(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), (zzcj) zza2.get(), jSONObject.getString("call_to_action"), jSONObject.optDouble("rating", -1.0d), jSONObject.optString("store"), jSONObject.optString("price"), (com.google.android.gms.ads.internal.formats.zza) zze.get(), new Bundle());
    }
}
