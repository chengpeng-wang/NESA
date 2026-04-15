package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public class zzdl implements zzdg {
    final HashMap<String, zzie<JSONObject>> zzxi = new HashMap();

    public Future<JSONObject> zzW(String str) {
        zzie zzie = new zzie();
        this.zzxi.put(str, zzie);
        return zzie;
    }

    public void zzX(String str) {
        zzie zzie = (zzie) this.zzxi.get(str);
        if (zzie == null) {
            zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        if (!zzie.isDone()) {
            zzie.cancel(true);
        }
        this.zzxi.remove(str);
    }

    public void zza(zzip zzip, Map<String, String> map) {
        zze((String) map.get("request_id"), (String) map.get("fetched_ad"));
    }

    public void zze(String str, String str2) {
        zzb.zzaC("Received ad from the cache.");
        zzie zzie = (zzie) this.zzxi.get(str);
        if (zzie == null) {
            zzb.e("Could not find the ad request for the corresponding ad response.");
            return;
        }
        try {
            zzie.zzf(new JSONObject(str2));
        } catch (JSONException e) {
            zzb.zzb("Failed constructing JSON object from value passed from javascript", e);
            zzie.zzf(null);
        } finally {
            this.zzxi.remove(str);
        }
    }
}
