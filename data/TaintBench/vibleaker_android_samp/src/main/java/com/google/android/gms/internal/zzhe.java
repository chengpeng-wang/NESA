package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.SearchAdRequestParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzhn.zza;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzhe {
    private static final SimpleDateFormat zzJg = new SimpleDateFormat("yyyyMMdd", Locale.US);

    private static String zzL(int i) {
        return String.format(Locale.US, "#%06x", new Object[]{Integer.valueOf(ViewCompat.MEASURED_SIZE_MASK & i)});
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0132 A:{Catch:{ JSONException -> 0x01e3 }} */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(android.content.Context r39, com.google.android.gms.ads.internal.request.AdRequestInfoParcel r40, java.lang.String r41) {
        /*
        r28 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x01e3 }
        r0 = r28;
        r1 = r41;
        r0.<init>(r1);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "ad_base_url";
        r5 = 0;
        r0 = r28;
        r6 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "ad_url";
        r5 = 0;
        r0 = r28;
        r7 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "ad_size";
        r5 = 0;
        r0 = r28;
        r19 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r40 == 0) goto L_0x0093;
    L_0x0026:
        r0 = r40;
        r4 = r0.zzHz;	 Catch:{ JSONException -> 0x01e3 }
        if (r4 == 0) goto L_0x0093;
    L_0x002c:
        r27 = 1;
    L_0x002e:
        if (r27 == 0) goto L_0x0096;
    L_0x0030:
        r4 = "ad_json";
        r5 = 0;
        r0 = r28;
        r5 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
    L_0x0039:
        r20 = -1;
        r4 = "debug_dialog";
        r8 = 0;
        r0 = r28;
        r22 = r0.optString(r4, r8);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "interstitial_timeout";
        r0 = r28;
        r4 = r0.has(r4);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 == 0) goto L_0x00a0;
    L_0x004e:
        r4 = "interstitial_timeout";
        r0 = r28;
        r8 = r0.getDouble(r4);	 Catch:{ JSONException -> 0x01e3 }
        r10 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r8 = r8 * r10;
        r0 = (long) r8;	 Catch:{ JSONException -> 0x01e3 }
        r16 = r0;
    L_0x005f:
        r4 = "orientation";
        r8 = 0;
        r0 = r28;
        r4 = r0.optString(r4, r8);	 Catch:{ JSONException -> 0x01e3 }
        r18 = -1;
        r8 = "portrait";
        r8 = r8.equals(r4);	 Catch:{ JSONException -> 0x01e3 }
        if (r8 == 0) goto L_0x00a3;
    L_0x0072:
        r4 = com.google.android.gms.ads.internal.zzr.zzbE();	 Catch:{ JSONException -> 0x01e3 }
        r18 = r4.zzhw();	 Catch:{ JSONException -> 0x01e3 }
    L_0x007a:
        r4 = 0;
        r8 = android.text.TextUtils.isEmpty(r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r8 != 0) goto L_0x00b4;
    L_0x0081:
        r7 = android.text.TextUtils.isEmpty(r6);	 Catch:{ JSONException -> 0x01e3 }
        if (r7 == 0) goto L_0x024d;
    L_0x0087:
        r4 = "Could not parse the mediation config: Missing required ad_base_url field";
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r4);	 Catch:{ JSONException -> 0x01e3 }
        r4 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ JSONException -> 0x01e3 }
        r5 = 0;
        r4.m2387init(r5);	 Catch:{ JSONException -> 0x01e3 }
    L_0x0092:
        return r4;
    L_0x0093:
        r27 = 0;
        goto L_0x002e;
    L_0x0096:
        r4 = "ad_html";
        r5 = 0;
        r0 = r28;
        r5 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x0039;
    L_0x00a0:
        r16 = -1;
        goto L_0x005f;
    L_0x00a3:
        r8 = "landscape";
        r4 = r8.equals(r4);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 == 0) goto L_0x007a;
    L_0x00ab:
        r4 = com.google.android.gms.ads.internal.zzr.zzbE();	 Catch:{ JSONException -> 0x01e3 }
        r18 = r4.zzhv();	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x007a;
    L_0x00b4:
        r4 = android.text.TextUtils.isEmpty(r7);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 != 0) goto L_0x0206;
    L_0x00ba:
        r0 = r40;
        r4 = r0.zzrl;	 Catch:{ JSONException -> 0x01e3 }
        r6 = r4.afmaVersion;	 Catch:{ JSONException -> 0x01e3 }
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = 0;
        r12 = 0;
        r4 = r40;
        r5 = r39;
        r4 = com.google.android.gms.internal.zzhd.zza(r4, r5, r6, r7, r8, r9, r10, r11, r12);	 Catch:{ JSONException -> 0x01e3 }
        r6 = r4.zzEF;	 Catch:{ JSONException -> 0x01e3 }
        r7 = r4.body;	 Catch:{ JSONException -> 0x01e3 }
        r0 = r4.zzHX;	 Catch:{ JSONException -> 0x01e3 }
        r20 = r0;
    L_0x00d5:
        r5 = "click_urls";
        r0 = r28;
        r5 = r0.optJSONArray(r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 != 0) goto L_0x023d;
    L_0x00df:
        r8 = 0;
    L_0x00e0:
        if (r5 == 0) goto L_0x00e6;
    L_0x00e2:
        r8 = zza(r5, r8);	 Catch:{ JSONException -> 0x01e3 }
    L_0x00e6:
        r5 = "impression_urls";
        r0 = r28;
        r5 = r0.optJSONArray(r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 != 0) goto L_0x0241;
    L_0x00f0:
        r9 = 0;
    L_0x00f1:
        if (r5 == 0) goto L_0x00f7;
    L_0x00f3:
        r9 = zza(r5, r9);	 Catch:{ JSONException -> 0x01e3 }
    L_0x00f7:
        r5 = "manual_impression_urls";
        r0 = r28;
        r5 = r0.optJSONArray(r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r4 != 0) goto L_0x0245;
    L_0x0101:
        r15 = 0;
    L_0x0102:
        if (r5 == 0) goto L_0x0108;
    L_0x0104:
        r15 = zza(r5, r15);	 Catch:{ JSONException -> 0x01e3 }
    L_0x0108:
        if (r4 == 0) goto L_0x0249;
    L_0x010a:
        r5 = r4.orientation;	 Catch:{ JSONException -> 0x01e3 }
        r10 = -1;
        if (r5 == r10) goto L_0x0113;
    L_0x010f:
        r0 = r4.orientation;	 Catch:{ JSONException -> 0x01e3 }
        r18 = r0;
    L_0x0113:
        r10 = r4.zzHS;	 Catch:{ JSONException -> 0x01e3 }
        r12 = 0;
        r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r5 <= 0) goto L_0x0249;
    L_0x011b:
        r10 = r4.zzHS;	 Catch:{ JSONException -> 0x01e3 }
    L_0x011d:
        r4 = "active_view";
        r0 = r28;
        r25 = r0.optString(r4);	 Catch:{ JSONException -> 0x01e3 }
        r24 = 0;
        r4 = "ad_is_javascript";
        r5 = 0;
        r0 = r28;
        r23 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r23 == 0) goto L_0x013b;
    L_0x0132:
        r4 = "ad_passback_url";
        r5 = 0;
        r0 = r28;
        r24 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
    L_0x013b:
        r4 = "mediation";
        r5 = 0;
        r0 = r28;
        r12 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "custom_render_allowed";
        r5 = 0;
        r0 = r28;
        r26 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "content_url_opted_out";
        r5 = 1;
        r0 = r28;
        r29 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "prefetch";
        r5 = 0;
        r0 = r28;
        r30 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "oauth2_token_status";
        r5 = 0;
        r0 = r28;
        r31 = r0.optInt(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "refresh_interval_milliseconds";
        r16 = -1;
        r0 = r28;
        r1 = r16;
        r16 = r0.optLong(r4, r1);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "mediation_config_cache_time_milliseconds";
        r32 = -1;
        r0 = r28;
        r1 = r32;
        r13 = r0.optLong(r4, r1);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "gws_query_id";
        r5 = "";
        r0 = r28;
        r32 = r0.optString(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "height";
        r5 = "fluid";
        r33 = "";
        r0 = r28;
        r1 = r33;
        r5 = r0.optString(r5, r1);	 Catch:{ JSONException -> 0x01e3 }
        r33 = r4.equals(r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "native_express";
        r5 = 0;
        r0 = r28;
        r34 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "video_start_urls";
        r0 = r28;
        r4 = r0.optJSONArray(r4);	 Catch:{ JSONException -> 0x01e3 }
        r5 = 0;
        r36 = zza(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "video_complete_urls";
        r0 = r28;
        r4 = r0.optJSONArray(r4);	 Catch:{ JSONException -> 0x01e3 }
        r5 = 0;
        r37 = zza(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "rewards";
        r0 = r28;
        r4 = r0.optJSONArray(r4);	 Catch:{ JSONException -> 0x01e3 }
        r35 = com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel.zza(r4);	 Catch:{ JSONException -> 0x01e3 }
        r4 = "use_displayed_impression";
        r5 = 0;
        r0 = r28;
        r38 = r0.optBoolean(r4, r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ JSONException -> 0x01e3 }
        r0 = r40;
        r0 = r0.zzHB;	 Catch:{ JSONException -> 0x01e3 }
        r28 = r0;
        r5 = r40;
        r4.m2391init(r5, r6, r7, r8, r9, r10, r12, r13, r15, r16, r18, r19, r20, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37, r38);	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x0092;
    L_0x01e3:
        r4 = move-exception;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse the mediation config: ";
        r5 = r5.append(r6);
        r4 = r4.getMessage();
        r4 = r5.append(r4);
        r4 = r4.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r4);
        r4 = new com.google.android.gms.ads.internal.request.AdResponseParcel;
        r5 = 0;
        r4.m2387init(r5);
        goto L_0x0092;
    L_0x0206:
        r4 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x01e3 }
        r4.<init>();	 Catch:{ JSONException -> 0x01e3 }
        r5 = "Could not parse the mediation config: Missing required ";
        r5 = r4.append(r5);	 Catch:{ JSONException -> 0x01e3 }
        if (r27 == 0) goto L_0x023a;
    L_0x0213:
        r4 = "ad_json";
    L_0x0215:
        r4 = r5.append(r4);	 Catch:{ JSONException -> 0x01e3 }
        r5 = " or ";
        r4 = r4.append(r5);	 Catch:{ JSONException -> 0x01e3 }
        r5 = "ad_url";
        r4 = r4.append(r5);	 Catch:{ JSONException -> 0x01e3 }
        r5 = " field.";
        r4 = r4.append(r5);	 Catch:{ JSONException -> 0x01e3 }
        r4 = r4.toString();	 Catch:{ JSONException -> 0x01e3 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK(r4);	 Catch:{ JSONException -> 0x01e3 }
        r4 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ JSONException -> 0x01e3 }
        r5 = 0;
        r4.m2387init(r5);	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x0092;
    L_0x023a:
        r4 = "ad_html";
        goto L_0x0215;
    L_0x023d:
        r8 = r4.zzBQ;	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x00e0;
    L_0x0241:
        r9 = r4.zzBR;	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x00f1;
    L_0x0245:
        r15 = r4.zzHV;	 Catch:{ JSONException -> 0x01e3 }
        goto L_0x0102;
    L_0x0249:
        r10 = r16;
        goto L_0x011d;
    L_0x024d:
        r7 = r5;
        goto L_0x00d5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhe.zza(android.content.Context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel, java.lang.String):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    @Nullable
    private static List<String> zza(@Nullable JSONArray jSONArray, @Nullable List<String> list) throws JSONException {
        if (jSONArray == null) {
            return null;
        }
        if (list == null) {
            list = new LinkedList();
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            list.add(jSONArray.getString(i));
        }
        return list;
    }

    public static JSONObject zza(Context context, AdRequestInfoParcel adRequestInfoParcel, zzhj zzhj, zza zza, Location location, zzbm zzbm, String str, String str2, List<String> list, Bundle bundle) {
        try {
            HashMap hashMap = new HashMap();
            if (list.size() > 0) {
                hashMap.put("eid", TextUtils.join(",", list));
            }
            if (adRequestInfoParcel.zzHs != null) {
                hashMap.put("ad_pos", adRequestInfoParcel.zzHs);
            }
            zza(hashMap, adRequestInfoParcel.zzHt);
            hashMap.put("format", adRequestInfoParcel.zzrp.zzuh);
            if (adRequestInfoParcel.zzrp.width == -1) {
                hashMap.put("smart_w", "full");
            }
            if (adRequestInfoParcel.zzrp.height == -2) {
                hashMap.put("smart_h", "auto");
            }
            if (adRequestInfoParcel.zzrp.zzul) {
                hashMap.put("fluid", "height");
            }
            if (adRequestInfoParcel.zzrp.zzuj != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzrp.zzuj) {
                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("|");
                    }
                    stringBuilder.append(adSizeParcel.width == -1 ? (int) (((float) adSizeParcel.widthPixels) / zzhj.zzHF) : adSizeParcel.width);
                    stringBuilder.append("x");
                    stringBuilder.append(adSizeParcel.height == -2 ? (int) (((float) adSizeParcel.heightPixels) / zzhj.zzHF) : adSizeParcel.height);
                }
                hashMap.put("sz", stringBuilder);
            }
            if (adRequestInfoParcel.zzHz != 0) {
                hashMap.put("native_version", Integer.valueOf(adRequestInfoParcel.zzHz));
                if (!adRequestInfoParcel.zzrp.zzum) {
                    hashMap.put("native_templates", adRequestInfoParcel.zzrH);
                    hashMap.put("native_image_orientation", zzc(adRequestInfoParcel.zzrD));
                    if (!adRequestInfoParcel.zzHK.isEmpty()) {
                        hashMap.put("native_custom_templates", adRequestInfoParcel.zzHK);
                    }
                }
            }
            hashMap.put("slotname", adRequestInfoParcel.zzrj);
            hashMap.put("pn", adRequestInfoParcel.applicationInfo.packageName);
            if (adRequestInfoParcel.zzHu != null) {
                hashMap.put("vc", Integer.valueOf(adRequestInfoParcel.zzHu.versionCode));
            }
            hashMap.put("ms", str2);
            hashMap.put("seq_num", adRequestInfoParcel.zzHw);
            hashMap.put("session_id", adRequestInfoParcel.zzHx);
            hashMap.put("js", adRequestInfoParcel.zzrl.afmaVersion);
            zza(hashMap, zzhj, zza);
            hashMap.put("platform", Build.MANUFACTURER);
            hashMap.put("submodel", Build.MODEL);
            if (adRequestInfoParcel.zzHt.versionCode >= 2 && adRequestInfoParcel.zzHt.zztK != null) {
                zza(hashMap, adRequestInfoParcel.zzHt.zztK);
            }
            if (adRequestInfoParcel.versionCode >= 2) {
                hashMap.put("quality_signals", adRequestInfoParcel.zzHy);
            }
            if (adRequestInfoParcel.versionCode >= 4 && adRequestInfoParcel.zzHB) {
                hashMap.put("forceHttps", Boolean.valueOf(adRequestInfoParcel.zzHB));
            }
            if (bundle != null) {
                hashMap.put("content_info", bundle);
            }
            if (adRequestInfoParcel.versionCode >= 5) {
                hashMap.put("u_sd", Float.valueOf(adRequestInfoParcel.zzHF));
                hashMap.put("sh", Integer.valueOf(adRequestInfoParcel.zzHE));
                hashMap.put("sw", Integer.valueOf(adRequestInfoParcel.zzHD));
            } else {
                hashMap.put("u_sd", Float.valueOf(zzhj.zzHF));
                hashMap.put("sh", Integer.valueOf(zzhj.zzHE));
                hashMap.put("sw", Integer.valueOf(zzhj.zzHD));
            }
            if (adRequestInfoParcel.versionCode >= 6) {
                if (!TextUtils.isEmpty(adRequestInfoParcel.zzHG)) {
                    try {
                        hashMap.put("view_hierarchy", new JSONObject(adRequestInfoParcel.zzHG));
                    } catch (JSONException e) {
                        zzb.zzd("Problem serializing view hierarchy to JSON", e);
                    }
                }
                hashMap.put("correlation_id", Long.valueOf(adRequestInfoParcel.zzHH));
            }
            if (adRequestInfoParcel.versionCode >= 7) {
                hashMap.put("request_id", adRequestInfoParcel.zzHI);
            }
            if (adRequestInfoParcel.versionCode >= 11 && adRequestInfoParcel.zzHM != null) {
                hashMap.put("capability", adRequestInfoParcel.zzHM.toBundle());
            }
            zza(hashMap, str);
            if (adRequestInfoParcel.versionCode >= 12 && !TextUtils.isEmpty(adRequestInfoParcel.zzHN)) {
                hashMap.put("anchor", adRequestInfoParcel.zzHN);
            }
            if (adRequestInfoParcel.versionCode >= 13) {
                hashMap.put("avol", Float.valueOf(adRequestInfoParcel.zzHO));
            }
            if (adRequestInfoParcel.versionCode >= 14 && adRequestInfoParcel.zzHP > 0) {
                hashMap.put("target_api", Integer.valueOf(adRequestInfoParcel.zzHP));
            }
            if (adRequestInfoParcel.versionCode >= 15) {
                hashMap.put("scroll_index", Integer.valueOf(adRequestInfoParcel.zzHQ == -1 ? -1 : adRequestInfoParcel.zzHQ));
            }
            if (zzb.zzQ(2)) {
                zzin.v("Ad Request JSON: " + zzr.zzbC().zzG(hashMap).toString(2));
            }
            return zzr.zzbC().zzG(hashMap);
        } catch (JSONException e2) {
            zzb.zzaK("Problem serializing ad request to JSON: " + e2.getMessage());
            return null;
        }
    }

    private static void zza(HashMap<String, Object> hashMap, Location location) {
        HashMap hashMap2 = new HashMap();
        Float valueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
        Long valueOf2 = Long.valueOf(location.getTime() * 1000);
        Long valueOf3 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
        Long valueOf4 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
        hashMap2.put("radius", valueOf);
        hashMap2.put("lat", valueOf3);
        hashMap2.put("long", valueOf4);
        hashMap2.put("time", valueOf2);
        hashMap.put("uule", hashMap2);
    }

    private static void zza(HashMap<String, Object> hashMap, AdRequestParcel adRequestParcel) {
        String zzhm = zzil.zzhm();
        if (zzhm != null) {
            hashMap.put("abf", zzhm);
        }
        if (adRequestParcel.zztC != -1) {
            hashMap.put("cust_age", zzJg.format(new Date(adRequestParcel.zztC)));
        }
        if (adRequestParcel.extras != null) {
            hashMap.put("extras", adRequestParcel.extras);
        }
        if (adRequestParcel.zztD != -1) {
            hashMap.put("cust_gender", Integer.valueOf(adRequestParcel.zztD));
        }
        if (adRequestParcel.zztE != null) {
            hashMap.put("kw", adRequestParcel.zztE);
        }
        if (adRequestParcel.zztG != -1) {
            hashMap.put("tag_for_child_directed_treatment", Integer.valueOf(adRequestParcel.zztG));
        }
        if (adRequestParcel.zztF) {
            hashMap.put("adtest", "on");
        }
        if (adRequestParcel.versionCode >= 2) {
            if (adRequestParcel.zztH) {
                hashMap.put("d_imp_hdr", Integer.valueOf(1));
            }
            if (!TextUtils.isEmpty(adRequestParcel.zztI)) {
                hashMap.put("ppid", adRequestParcel.zztI);
            }
            if (adRequestParcel.zztJ != null) {
                zza((HashMap) hashMap, adRequestParcel.zztJ);
            }
        }
        if (adRequestParcel.versionCode >= 3 && adRequestParcel.zztL != null) {
            hashMap.put("url", adRequestParcel.zztL);
        }
        if (adRequestParcel.versionCode >= 5) {
            if (adRequestParcel.zztN != null) {
                hashMap.put("custom_targeting", adRequestParcel.zztN);
            }
            if (adRequestParcel.zztO != null) {
                hashMap.put("category_exclusions", adRequestParcel.zztO);
            }
            if (adRequestParcel.zztP != null) {
                hashMap.put("request_agent", adRequestParcel.zztP);
            }
        }
        if (adRequestParcel.versionCode >= 6 && adRequestParcel.zztQ != null) {
            hashMap.put("request_pkg", adRequestParcel.zztQ);
        }
        if (adRequestParcel.versionCode >= 7) {
            hashMap.put("is_designed_for_families", Boolean.valueOf(adRequestParcel.zztR));
        }
    }

    private static void zza(HashMap<String, Object> hashMap, SearchAdRequestParcel searchAdRequestParcel) {
        Object obj;
        Object obj2 = null;
        if (Color.alpha(searchAdRequestParcel.zzvd) != 0) {
            hashMap.put("acolor", zzL(searchAdRequestParcel.zzvd));
        }
        if (Color.alpha(searchAdRequestParcel.backgroundColor) != 0) {
            hashMap.put("bgcolor", zzL(searchAdRequestParcel.backgroundColor));
        }
        if (!(Color.alpha(searchAdRequestParcel.zzve) == 0 || Color.alpha(searchAdRequestParcel.zzvf) == 0)) {
            hashMap.put("gradientto", zzL(searchAdRequestParcel.zzve));
            hashMap.put("gradientfrom", zzL(searchAdRequestParcel.zzvf));
        }
        if (Color.alpha(searchAdRequestParcel.zzvg) != 0) {
            hashMap.put("bcolor", zzL(searchAdRequestParcel.zzvg));
        }
        hashMap.put("bthick", Integer.toString(searchAdRequestParcel.zzvh));
        switch (searchAdRequestParcel.zzvi) {
            case 0:
                obj = "none";
                break;
            case 1:
                obj = "dashed";
                break;
            case 2:
                obj = "dotted";
                break;
            case 3:
                obj = "solid";
                break;
            default:
                obj = null;
                break;
        }
        if (obj != null) {
            hashMap.put("btype", obj);
        }
        switch (searchAdRequestParcel.zzvj) {
            case 0:
                obj2 = "light";
                break;
            case 1:
                obj2 = "medium";
                break;
            case 2:
                obj2 = "dark";
                break;
        }
        if (obj2 != null) {
            hashMap.put("callbuttoncolor", obj2);
        }
        if (searchAdRequestParcel.zzvk != null) {
            hashMap.put("channel", searchAdRequestParcel.zzvk);
        }
        if (Color.alpha(searchAdRequestParcel.zzvl) != 0) {
            hashMap.put("dcolor", zzL(searchAdRequestParcel.zzvl));
        }
        if (searchAdRequestParcel.zzvm != null) {
            hashMap.put("font", searchAdRequestParcel.zzvm);
        }
        if (Color.alpha(searchAdRequestParcel.zzvn) != 0) {
            hashMap.put("hcolor", zzL(searchAdRequestParcel.zzvn));
        }
        hashMap.put("headersize", Integer.toString(searchAdRequestParcel.zzvo));
        if (searchAdRequestParcel.zzvp != null) {
            hashMap.put("q", searchAdRequestParcel.zzvp);
        }
    }

    private static void zza(HashMap<String, Object> hashMap, zzhj zzhj, zza zza) {
        hashMap.put("am", Integer.valueOf(zzhj.zzJQ));
        hashMap.put("cog", zzy(zzhj.zzJR));
        hashMap.put("coh", zzy(zzhj.zzJS));
        if (!TextUtils.isEmpty(zzhj.zzJT)) {
            hashMap.put("carrier", zzhj.zzJT);
        }
        hashMap.put("gl", zzhj.zzJU);
        if (zzhj.zzJV) {
            hashMap.put("simulator", Integer.valueOf(1));
        }
        if (zzhj.zzJW) {
            hashMap.put("is_sidewinder", Integer.valueOf(1));
        }
        hashMap.put("ma", zzy(zzhj.zzJX));
        hashMap.put("sp", zzy(zzhj.zzJY));
        hashMap.put("hl", zzhj.zzJZ);
        if (!TextUtils.isEmpty(zzhj.zzKa)) {
            hashMap.put("mv", zzhj.zzKa);
        }
        hashMap.put("muv", Integer.valueOf(zzhj.zzKb));
        if (zzhj.zzKc != -2) {
            hashMap.put("cnt", Integer.valueOf(zzhj.zzKc));
        }
        hashMap.put("gnt", Integer.valueOf(zzhj.zzKd));
        hashMap.put("pt", Integer.valueOf(zzhj.zzKe));
        hashMap.put("rm", Integer.valueOf(zzhj.zzKf));
        hashMap.put("riv", Integer.valueOf(zzhj.zzKg));
        Bundle bundle = new Bundle();
        bundle.putString("build", zzhj.zzKl);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("is_charging", zzhj.zzKi);
        bundle2.putDouble("battery_level", zzhj.zzKh);
        bundle.putBundle("battery", bundle2);
        bundle2 = new Bundle();
        bundle2.putInt("active_network_state", zzhj.zzKk);
        bundle2.putBoolean("active_network_metered", zzhj.zzKj);
        if (zza != null) {
            Bundle bundle3 = new Bundle();
            bundle3.putInt("predicted_latency_micros", zza.zzKq);
            bundle3.putLong("predicted_down_throughput_bps", zza.zzKr);
            bundle3.putLong("predicted_up_throughput_bps", zza.zzKs);
            bundle2.putBundle("predictions", bundle3);
        }
        bundle.putBundle("network", bundle2);
        hashMap.put("device", bundle);
    }

    private static void zza(HashMap<String, Object> hashMap, String str) {
        if (str != null) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("token", str);
            hashMap.put("pan", hashMap2);
        }
    }

    private static String zzc(NativeAdOptionsParcel nativeAdOptionsParcel) {
        switch (nativeAdOptionsParcel != null ? nativeAdOptionsParcel.zzyB : 0) {
            case 1:
                return "portrait";
            case 2:
                return "landscape";
            default:
                return "any";
        }
    }

    public static JSONObject zzc(AdResponseParcel adResponseParcel) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (adResponseParcel.zzEF != null) {
            jSONObject.put("ad_base_url", adResponseParcel.zzEF);
        }
        if (adResponseParcel.zzHW != null) {
            jSONObject.put("ad_size", adResponseParcel.zzHW);
        }
        jSONObject.put("native", adResponseParcel.zzuk);
        if (adResponseParcel.zzuk) {
            jSONObject.put("ad_json", adResponseParcel.body);
        } else {
            jSONObject.put("ad_html", adResponseParcel.body);
        }
        if (adResponseParcel.zzHY != null) {
            jSONObject.put("debug_dialog", adResponseParcel.zzHY);
        }
        if (adResponseParcel.zzHS != -1) {
            jSONObject.put("interstitial_timeout", ((double) adResponseParcel.zzHS) / 1000.0d);
        }
        if (adResponseParcel.orientation == zzr.zzbE().zzhw()) {
            jSONObject.put("orientation", "portrait");
        } else if (adResponseParcel.orientation == zzr.zzbE().zzhv()) {
            jSONObject.put("orientation", "landscape");
        }
        if (adResponseParcel.zzBQ != null) {
            jSONObject.put("click_urls", zzi(adResponseParcel.zzBQ));
        }
        if (adResponseParcel.zzBR != null) {
            jSONObject.put("impression_urls", zzi(adResponseParcel.zzBR));
        }
        if (adResponseParcel.zzHV != null) {
            jSONObject.put("manual_impression_urls", zzi(adResponseParcel.zzHV));
        }
        if (adResponseParcel.zzIb != null) {
            jSONObject.put("active_view", adResponseParcel.zzIb);
        }
        jSONObject.put("ad_is_javascript", adResponseParcel.zzHZ);
        if (adResponseParcel.zzIa != null) {
            jSONObject.put("ad_passback_url", adResponseParcel.zzIa);
        }
        jSONObject.put("mediation", adResponseParcel.zzHT);
        jSONObject.put("custom_render_allowed", adResponseParcel.zzIc);
        jSONObject.put("content_url_opted_out", adResponseParcel.zzId);
        jSONObject.put("prefetch", adResponseParcel.zzIe);
        jSONObject.put("oauth2_token_status", adResponseParcel.zzIf);
        if (adResponseParcel.zzBU != -1) {
            jSONObject.put("refresh_interval_milliseconds", adResponseParcel.zzBU);
        }
        if (adResponseParcel.zzHU != -1) {
            jSONObject.put("mediation_config_cache_time_milliseconds", adResponseParcel.zzHU);
        }
        if (!TextUtils.isEmpty(adResponseParcel.zzIi)) {
            jSONObject.put("gws_query_id", adResponseParcel.zzIi);
        }
        jSONObject.put("fluid", adResponseParcel.zzul ? "height" : "");
        jSONObject.put("native_express", adResponseParcel.zzum);
        if (adResponseParcel.zzIk != null) {
            jSONObject.put("video_start_urls", zzi(adResponseParcel.zzIk));
        }
        if (adResponseParcel.zzIl != null) {
            jSONObject.put("video_complete_urls", zzi(adResponseParcel.zzIl));
        }
        if (adResponseParcel.zzIj != null) {
            jSONObject.put("rewards", adResponseParcel.zzIj.zzgR());
        }
        jSONObject.put("use_displayed_impression", adResponseParcel.zzIm);
        return jSONObject;
    }

    @Nullable
    static JSONArray zzi(List<String> list) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (String put : list) {
            jSONArray.put(put);
        }
        return jSONArray;
    }

    private static Integer zzy(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}
