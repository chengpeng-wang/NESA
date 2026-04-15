package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public final class zzea {
    public final long zzyA;
    public final String zzyB;
    public final int zzyC;
    public int zzyD;
    public int zzyE;
    public final List<zzdz> zzyu;
    public final long zzyv;
    public final List<String> zzyw;
    public final List<String> zzyx;
    public final List<String> zzyy;
    public final String zzyz;

    public zzea(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (zzb.zzM(2)) {
            zzb.v("Mediation Response JSON: " + jSONObject.toString(2));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        int i = -1;
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            zzdz zzdz = new zzdz(jSONArray.getJSONObject(i2));
            arrayList.add(zzdz);
            if (i < 0 && zza(zzdz)) {
                i = i2;
            }
        }
        this.zzyD = i;
        this.zzyE = jSONArray.length();
        this.zzyu = Collections.unmodifiableList(arrayList);
        this.zzyz = jSONObject.getString("qdata");
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject != null) {
            this.zzyv = optJSONObject.optLong("ad_network_timeout_millis", -1);
            this.zzyw = zzp.zzbJ().zza(optJSONObject, "click_urls");
            this.zzyx = zzp.zzbJ().zza(optJSONObject, "imp_urls");
            this.zzyy = zzp.zzbJ().zza(optJSONObject, "nofill_urls");
            long optLong = optJSONObject.optLong("refresh", -1);
            this.zzyA = optLong > 0 ? 1000 * optLong : -1;
            JSONArray optJSONArray = optJSONObject.optJSONArray("rewards");
            if (optJSONArray == null || optJSONArray.length() == 0) {
                this.zzyB = null;
                this.zzyC = 0;
                return;
            }
            this.zzyB = optJSONArray.getJSONObject(0).optString("rb_type");
            this.zzyC = optJSONArray.getJSONObject(0).optInt("rb_amount");
            return;
        }
        this.zzyv = -1;
        this.zzyw = null;
        this.zzyx = null;
        this.zzyy = null;
        this.zzyA = -1;
        this.zzyB = null;
        this.zzyC = 0;
    }

    private boolean zza(zzdz zzdz) {
        for (String equals : zzdz.zzym) {
            if (equals.equals("com.google.ads.mediation.admob.AdMobAdapter")) {
                return true;
            }
        }
        return false;
    }
}
