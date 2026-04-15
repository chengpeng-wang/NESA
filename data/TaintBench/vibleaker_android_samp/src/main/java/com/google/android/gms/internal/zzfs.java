package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzfs {
    private final String zzDJ;
    private final zzjp zzpD;

    public zzfs(zzjp zzjp) {
        this(zzjp, "");
    }

    public zzfs(zzjp zzjp, String str) {
        this.zzpD = zzjp;
        this.zzDJ = str;
    }

    public void zza(int i, int i2, int i3, int i4, float f, int i5) {
        try {
            this.zzpD.zzb("onScreenInfoChanged", new JSONObject().put("width", i).put("height", i2).put("maxSizeWidth", i3).put("maxSizeHeight", i4).put("density", (double) f).put("rotation", i5));
        } catch (JSONException e) {
            zzb.zzb("Error occured while obtaining screen information.", e);
        }
    }

    public void zzam(String str) {
        try {
            this.zzpD.zzb("onError", new JSONObject().put("message", str).put("action", this.zzDJ));
        } catch (JSONException e) {
            zzb.zzb("Error occurred while dispatching error event.", e);
        }
    }

    public void zzan(String str) {
        try {
            this.zzpD.zzb("onReadyEventReceived", new JSONObject().put("js", str));
        } catch (JSONException e) {
            zzb.zzb("Error occured while dispatching ready Event.", e);
        }
    }

    public void zzao(String str) {
        try {
            this.zzpD.zzb("onStateChanged", new JSONObject().put("state", str));
        } catch (JSONException e) {
            zzb.zzb("Error occured while dispatching state change.", e);
        }
    }

    public void zzb(int i, int i2, int i3, int i4) {
        try {
            this.zzpD.zzb("onSizeChanged", new JSONObject().put("x", i).put("y", i2).put("width", i3).put("height", i4));
        } catch (JSONException e) {
            zzb.zzb("Error occured while dispatching size change.", e);
        }
    }

    public void zzc(int i, int i2, int i3, int i4) {
        try {
            this.zzpD.zzb("onDefaultPositionReceived", new JSONObject().put("x", i).put("y", i2).put("width", i3).put("height", i4));
        } catch (JSONException e) {
            zzb.zzb("Error occured while dispatching default position.", e);
        }
    }
}
