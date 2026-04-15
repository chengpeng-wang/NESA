package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.overlay.zzm;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzde {
    public static final zzdf zzyX = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
        }
    };
    public static final zzdf zzyY = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            String str = (String) map.get("urls");
            if (TextUtils.isEmpty(str)) {
                zzb.zzaK("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] split = str.split(",");
            Map hashMap = new HashMap();
            PackageManager packageManager = zzjp.getContext().getPackageManager();
            for (String str2 : split) {
                String[] split2 = str2.split(";", 2);
                hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(split2[0].trim())), 65536) != null));
            }
            zzjp.zza("openableURLs", hashMap);
        }
    };
    public static final zzdf zzyZ = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            PackageManager packageManager = zzjp.getContext().getPackageManager();
            try {
                try {
                    JSONArray jSONArray = new JSONObject((String) map.get("data")).getJSONArray("intents");
                    JSONObject jSONObject = new JSONObject();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            String optString = jSONObject2.optString("id");
                            String optString2 = jSONObject2.optString("u");
                            String optString3 = jSONObject2.optString("i");
                            String optString4 = jSONObject2.optString("m");
                            String optString5 = jSONObject2.optString("p");
                            String optString6 = jSONObject2.optString("c");
                            jSONObject2.optString("f");
                            jSONObject2.optString("e");
                            Intent intent = new Intent();
                            if (!TextUtils.isEmpty(optString2)) {
                                intent.setData(Uri.parse(optString2));
                            }
                            if (!TextUtils.isEmpty(optString3)) {
                                intent.setAction(optString3);
                            }
                            if (!TextUtils.isEmpty(optString4)) {
                                intent.setType(optString4);
                            }
                            if (!TextUtils.isEmpty(optString5)) {
                                intent.setPackage(optString5);
                            }
                            if (!TextUtils.isEmpty(optString6)) {
                                String[] split = optString6.split("/", 2);
                                if (split.length == 2) {
                                    intent.setComponent(new ComponentName(split[0], split[1]));
                                }
                            }
                            try {
                                jSONObject.put(optString, packageManager.resolveActivity(intent, 65536) != null);
                            } catch (JSONException e) {
                                zzb.zzb("Error constructing openable urls response.", e);
                            }
                        } catch (JSONException e2) {
                            zzb.zzb("Error parsing the intent data.", e2);
                        }
                    }
                    zzjp.zzb("openableIntents", jSONObject);
                } catch (JSONException e3) {
                    zzjp.zzb("openableIntents", new JSONObject());
                }
            } catch (JSONException e4) {
                zzjp.zzb("openableIntents", new JSONObject());
            }
        }
    };
    public static final zzdf zzza = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaK("URL missing from click GMSG.");
                return;
            }
            Uri zza;
            Uri parse = Uri.parse(str);
            try {
                zzan zzhW = zzjp.zzhW();
                if (zzhW != null && zzhW.zzb(parse)) {
                    zza = zzhW.zza(parse, zzjp.getContext());
                    new zziy(zzjp.getContext(), zzjp.zzhX().afmaVersion, zza.toString()).zzgd();
                }
            } catch (zzao e) {
                zzb.zzaK("Unable to append parameter to URL: " + str);
            }
            zza = parse;
            new zziy(zzjp.getContext(), zzjp.zzhX().afmaVersion, zza.toString()).zzgd();
        }
    };
    public static final zzdf zzzb = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            zzd zzhS = zzjp.zzhS();
            if (zzhS != null) {
                zzhS.close();
                return;
            }
            zzhS = zzjp.zzhT();
            if (zzhS != null) {
                zzhS.close();
            } else {
                zzb.zzaK("A GMSG tried to close something that wasn't an overlay.");
            }
        }
    };
    public static final zzdf zzzc = new zzdf() {
        private void zzc(zzjp zzjp) {
            zzb.zzaJ("Received support message, responding.");
            boolean z = false;
            com.google.android.gms.ads.internal.zzd zzhR = zzjp.zzhR();
            if (zzhR != null) {
                zzm zzm = zzhR.zzpy;
                if (zzm != null) {
                    z = zzm.zzfM();
                }
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("event", "checkSupport");
                jSONObject.put("supports", z);
                zzjp.zzb("appStreaming", jSONObject);
            } catch (Throwable th) {
            }
        }

        public void zza(zzjp zzjp, Map<String, String> map) {
            if ("checkSupport".equals(map.get("action"))) {
                zzc(zzjp);
                return;
            }
            zzd zzhS = zzjp.zzhS();
            if (zzhS != null) {
                zzhS.zzg(zzjp, map);
            }
        }
    };
    public static final zzdf zzzd = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            zzjp.zzE("1".equals(map.get("custom_close")));
        }
    };
    public static final zzdf zzze = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaK("URL missing from httpTrack GMSG.");
            } else {
                new zziy(zzjp.getContext(), zzjp.zzhX().afmaVersion, str).zzgd();
            }
        }
    };
    public static final zzdf zzzf = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            zzb.zzaJ("Received log message: " + ((String) map.get("string")));
        }
    };
    public static final zzdf zzzg = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            String str = (String) map.get("ty");
            String str2 = (String) map.get("td");
            try {
                int parseInt = Integer.parseInt((String) map.get("tx"));
                int parseInt2 = Integer.parseInt(str);
                int parseInt3 = Integer.parseInt(str2);
                zzan zzhW = zzjp.zzhW();
                if (zzhW != null) {
                    zzhW.zzab().zza(parseInt, parseInt2, parseInt3);
                }
            } catch (NumberFormatException e) {
                zzb.zzaK("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final zzdf zzzh = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            if (((Boolean) zzbt.zzwT.get()).booleanValue()) {
                zzjp.zzF(!Boolean.parseBoolean((String) map.get("disabled")));
            }
        }
    };
    public static final zzdf zzzi = new zzdo();
    public static final zzdf zzzj = new zzds();
    public static final zzdf zzzk = new zzdd();
}
