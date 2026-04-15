package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.formats.zzc;
import com.google.android.gms.ads.internal.formats.zzf;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzgf.zza;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzgi implements zza<zzf> {
    private final boolean zzDl;

    public zzgi(boolean z) {
        this.zzDl = z;
    }

    private void zza(zzgf zzgf, JSONObject jSONObject, zzlh<String, Future<zzc>> zzlh) throws JSONException {
        zzlh.put(jSONObject.getString("name"), zzgf.zza(jSONObject, "image_value", this.zzDl));
    }

    private void zza(JSONObject jSONObject, zzlh<String, String> zzlh) throws JSONException {
        zzlh.put(jSONObject.getString("name"), jSONObject.getString("string_value"));
    }

    private <K, V> zzlh<K, V> zzc(zzlh<K, Future<V>> zzlh) throws InterruptedException, ExecutionException {
        zzlh zzlh2 = new zzlh();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= zzlh.size()) {
                return zzlh2;
            }
            zzlh2.put(zzlh.keyAt(i2), ((Future) zzlh.valueAt(i2)).get());
            i = i2 + 1;
        }
    }

    /* renamed from: zzd */
    public zzf zza(zzgf zzgf, JSONObject jSONObject) throws JSONException, InterruptedException, ExecutionException {
        zzlh zzlh = new zzlh();
        zzlh zzlh2 = new zzlh();
        zzih zze = zzgf.zze(jSONObject);
        JSONArray jSONArray = jSONObject.getJSONArray("custom_assets");
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            String string = jSONObject2.getString("type");
            if ("string".equals(string)) {
                zza(jSONObject2, zzlh2);
            } else if ("image".equals(string)) {
                zza(zzgf, jSONObject2, zzlh);
            } else {
                zzb.zzaE("Unknown custom asset type: " + string);
            }
        }
        return new zzf(jSONObject.getString("custom_template_id"), zzc(zzlh), zzlh2, (com.google.android.gms.ads.internal.formats.zza) zze.get());
    }
}
