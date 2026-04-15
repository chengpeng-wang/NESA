package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzgk
public final class zzdf {
    public static final zzdg zzwI = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
        }
    };
    public static final zzdg zzwJ = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            String str = (String) map.get("urls");
            if (TextUtils.isEmpty(str)) {
                zzb.zzaE("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] split = str.split(",");
            HashMap hashMap = new HashMap();
            PackageManager packageManager = zzip.getContext().getPackageManager();
            for (String str2 : split) {
                String[] split2 = str2.split(";", 2);
                hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(split2[0].trim())), 65536) != null));
            }
            zzip.zzc("openableURLs", hashMap);
        }
    };
    public static final zzdg zzwK = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            PackageManager packageManager = zzip.getContext().getPackageManager();
            try {
                try {
                    JSONArray jSONArray = new JSONObject((String) map.get("data")).getJSONArray("intents");
                    JSONObject jSONObject = new JSONObject();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            String optString = jSONObject2.optString(MASTNativeAdConstants.ID_STRING);
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
                    zzip.zzb("openableIntents", jSONObject);
                } catch (JSONException e3) {
                    zzip.zzb("openableIntents", new JSONObject());
                }
            } catch (JSONException e4) {
                zzip.zzb("openableIntents", new JSONObject());
            }
        }
    };
    public static final zzdg zzwL = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaE("URL missing from click GMSG.");
                return;
            }
            String zzd = zzp.zzbx().zzd(zzip.getContext(), str, zzip.zzha());
            Uri parse = Uri.parse(zzd);
            try {
                zzan zzgU = zzip.zzgU();
                if (zzgU != null && zzgU.zzb(parse)) {
                    parse = zzgU.zza(parse, zzip.getContext());
                }
            } catch (zzao e) {
                zzb.zzaE("Unable to append parameter to URL: " + zzd);
            }
            new zzia(zzip.getContext(), zzip.zzgV().zzIz, parse.toString()).zzgn();
        }
    };
    public static final zzdg zzwM = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            zzd zzgQ = zzip.zzgQ();
            if (zzgQ != null) {
                zzgQ.close();
                return;
            }
            zzgQ = zzip.zzgR();
            if (zzgQ != null) {
                zzgQ.close();
            } else {
                zzb.zzaE("A GMSG tried to close something that wasn't an overlay.");
            }
        }
    };
    public static final zzdg zzwN = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            zzip.zzD("1".equals(map.get("custom_close")));
        }
    };
    public static final zzdg zzwO = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                zzb.zzaE("URL missing from httpTrack GMSG.");
            } else {
                new zzia(zzip.getContext(), zzip.zzgV().zzIz, str).zzgn();
            }
        }
    };
    public static final zzdg zzwP = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            zzb.zzaD("Received log message: " + ((String) map.get("string")));
        }
    };
    public static final zzdg zzwQ = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            String str = (String) map.get("ty");
            String str2 = (String) map.get("td");
            try {
                int parseInt = Integer.parseInt((String) map.get("tx"));
                int parseInt2 = Integer.parseInt(str);
                int parseInt3 = Integer.parseInt(str2);
                zzan zzgU = zzip.zzgU();
                if (zzgU != null) {
                    zzgU.zzab().zza(parseInt, parseInt2, parseInt3);
                }
            } catch (NumberFormatException e) {
                zzb.zzaE("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final zzdg zzwR = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            if (((Boolean) zzby.zzvd.get()).booleanValue()) {
                zzip.zzE(!Boolean.parseBoolean((String) map.get("disabled")));
            }
        }
    };
    public static final zzdg zzwS = new zzdo();
    public static final zzdg zzwT = new zzds();
}
