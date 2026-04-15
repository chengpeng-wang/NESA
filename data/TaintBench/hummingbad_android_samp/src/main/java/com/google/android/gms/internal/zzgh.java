package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.formats.zze;
import com.google.android.gms.internal.zzgf.zza;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzgh implements zza<zze> {
    private final boolean zzDl;
    private final boolean zzDm;

    public zzgh(boolean z, boolean z2) {
        this.zzDl = z;
        this.zzDm = z2;
    }

    /* renamed from: zzc */
    public zze zza(zzgf zzgf, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        List<zzih> zza = zzgf.zza(jSONObject, "images", true, this.zzDl, this.zzDm);
        zzih zza2 = zzgf.zza(jSONObject, "secondary_image", false, this.zzDl);
        zzih zze = zzgf.zze(jSONObject);
        ArrayList arrayList = new ArrayList();
        for (zzih zzih : zza) {
            arrayList.add(zzih.get());
        }
        return new zze(jSONObject.getString("headline"), arrayList, jSONObject.getString("body"), (zzcj) zza2.get(), jSONObject.getString("call_to_action"), jSONObject.getString("advertiser"), (com.google.android.gms.ads.internal.formats.zza) zze.get(), new Bundle());
    }
}
