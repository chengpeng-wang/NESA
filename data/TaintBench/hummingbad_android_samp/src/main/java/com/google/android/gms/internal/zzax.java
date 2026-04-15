package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import org.json.JSONObject;

@zzgk
public final class zzax {
    private final String zzqH;
    private final JSONObject zzqI;
    private final String zzqJ;
    private final String zzqK;
    private final boolean zzqL;

    public zzax(String str, VersionInfoParcel versionInfoParcel, String str2, JSONObject jSONObject, boolean z) {
        this.zzqK = versionInfoParcel.zzIz;
        this.zzqI = jSONObject;
        this.zzqJ = str;
        this.zzqH = str2;
        this.zzqL = z;
    }

    public String zzbU() {
        return this.zzqH;
    }

    public String zzbV() {
        return this.zzqK;
    }

    public JSONObject zzbW() {
        return this.zzqI;
    }

    public String zzbX() {
        return this.zzqJ;
    }

    public boolean zzbY() {
        return this.zzqL;
    }
}
