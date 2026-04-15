package com.moceanmobile.mast;

import com.mopub.mobileads.VastIconXmlManager;
import java.util.ArrayList;
import java.util.Map;

public class AdDescriptor {
    private final Map<String, String> adInfo;
    private String errorMessage;
    private ArrayList<String> mClickTrackers;
    private ArrayList<String> mImpressionTrackers;
    private MediationData mMediationData;

    /* JADX WARNING: Removed duplicated region for block: B:82:0x01e9 A:{Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }} */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0306 A:{ExcHandler: IOException (r3_18 'e' java.io.IOException), Splitter:B:4:0x0015} */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x0372 A:{Catch:{ JSONException -> 0x03c8, all -> 0x03fd }} */
    /* JADX WARNING: Removed duplicated region for block: B:262:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03ce A:{SYNTHETIC, Splitter:B:183:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x0403 A:{ExcHandler: Exception (r3_16 'e' java.lang.Exception), Splitter:B:4:0x0015} */
    /* JADX WARNING: Removed duplicated region for block: B:259:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0311 A:{SYNTHETIC, Splitter:B:138:0x0311} */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x0372 A:{Catch:{ JSONException -> 0x03c8, all -> 0x03fd }} */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03ce A:{SYNTHETIC, Splitter:B:183:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:262:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:264:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x03e4 A:{SYNTHETIC, Splitter:B:192:0x03e4} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0311 A:{SYNTHETIC, Splitter:B:138:0x0311} */
    /* JADX WARNING: Removed duplicated region for block: B:259:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x0372 A:{Catch:{ JSONException -> 0x03c8, all -> 0x03fd }} */
    /* JADX WARNING: Removed duplicated region for block: B:262:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03ce A:{SYNTHETIC, Splitter:B:183:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x03e4 A:{SYNTHETIC, Splitter:B:192:0x03e4} */
    /* JADX WARNING: Removed duplicated region for block: B:264:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:259:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0311 A:{SYNTHETIC, Splitter:B:138:0x0311} */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x0372 A:{Catch:{ JSONException -> 0x03c8, all -> 0x03fd }} */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03ce A:{SYNTHETIC, Splitter:B:183:0x03ce} */
    /* JADX WARNING: Removed duplicated region for block: B:262:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:264:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x03e4 A:{SYNTHETIC, Splitter:B:192:0x03e4} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03f4 A:{SYNTHETIC, Splitter:B:199:0x03f4} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x03f4 A:{SYNTHETIC, Splitter:B:199:0x03f4} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:133:0x0306, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:134:0x0307, code skipped:
            r4 = r3;
            r5 = r0;
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:139:?, code skipped:
            r5.close();
     */
    /* JADX WARNING: Missing block: B:140:0x0316, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:141:0x0317, code skipped:
            r4.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:149:0x035d, code skipped:
            r4 = r24;
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:153:0x0372, code skipped:
            r4 = new com.moceanmobile.mast.AdDescriptor();
            r4.errorMessage = r5;
     */
    /* JADX WARNING: Missing block: B:154:0x0379, code skipped:
            if (r22 != null) goto L_0x037b;
     */
    /* JADX WARNING: Missing block: B:156:?, code skipped:
            r22.close();
     */
    /* JADX WARNING: Missing block: B:177:0x03c3, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:178:0x03c4, code skipped:
            r3.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:184:?, code skipped:
            r22.close();
     */
    /* JADX WARNING: Missing block: B:185:0x03d3, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:186:0x03d4, code skipped:
            r4.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:193:?, code skipped:
            r22.close();
     */
    /* JADX WARNING: Missing block: B:194:0x03e9, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:195:0x03ea, code skipped:
            r4.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:207:0x0403, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:208:0x0404, code skipped:
            r4 = r3;
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:259:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:260:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:261:?, code skipped:
            return r4;
     */
    /* JADX WARNING: Missing block: B:262:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:263:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:264:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:265:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:267:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:268:?, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:269:?, code skipped:
            return r3;
     */
    public static com.moceanmobile.mast.AdDescriptor parseNativeResponse(java.io.InputStream r26) {
        /*
        r23 = 0;
        r4 = 0;
        r3 = 0;
        r22 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0427, JSONException -> 0x040e, Exception -> 0x03d9, all -> 0x03ef }
        r5 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0427, JSONException -> 0x040e, Exception -> 0x03d9, all -> 0x03ef }
        r0 = r26;
        r5.<init>(r0);	 Catch:{ IOException -> 0x0427, JSONException -> 0x040e, Exception -> 0x03d9, all -> 0x03ef }
        r0 = r22;
        r0.<init>(r5);	 Catch:{ IOException -> 0x0427, JSONException -> 0x040e, Exception -> 0x03d9, all -> 0x03ef }
        r4 = 0;
        r18 = r4;
    L_0x0015:
        r5 = r22.readLine();	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        if (r5 != 0) goto L_0x0073;
    L_0x001b:
        if (r18 == 0) goto L_0x043a;
    L_0x001d:
        r11 = new java.util.ArrayList;	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r11.<init>();	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r12 = 0;
        r7 = 0;
        r13 = 0;
        r21 = 0;
        r6 = 0;
        r4 = 0;
        r5 = 0;
        r14 = 0;
        r15 = 0;
        r16 = 0;
        r17 = 0;
        r9 = 0;
        r8 = 0;
        r10 = 0;
        r24 = r18.toString();	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r3 = new org.json.JSONObject;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r24;
        r3.<init>(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r18 = "ads";
        r0 = r18;
        r3 = r3.getJSONArray(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 == 0) goto L_0x008a;
    L_0x0048:
        r18 = 0;
        r0 = r18;
        r18 = r3.optJSONObject(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r18 == 0) goto L_0x008a;
    L_0x0052:
        r19 = "error";
        r19 = r18.has(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 == 0) goto L_0x008a;
    L_0x005a:
        r19 = "error";
        r18 = r18.optString(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = android.text.TextUtils.isEmpty(r18);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 != 0) goto L_0x008a;
    L_0x0066:
        r3 = new com.moceanmobile.mast.AdDescriptor;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r3.m1758init();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r18;
        r3.errorMessage = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r22.close();	 Catch:{ IOException -> 0x0085 }
    L_0x0072:
        return r3;
    L_0x0073:
        if (r18 != 0) goto L_0x0449;
    L_0x0075:
        r4 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r4.<init>();	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
    L_0x007a:
        r4.append(r5);	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r5 = "\n";
        r4.append(r5);	 Catch:{ IOException -> 0x0306, JSONException -> 0x0416, Exception -> 0x0403 }
        r18 = r4;
        goto L_0x0015;
    L_0x0085:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0072;
    L_0x008a:
        if (r3 == 0) goto L_0x0444;
    L_0x008c:
        r18 = 0;
        r0 = r18;
        r18 = r3.optJSONObject(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r18 == 0) goto L_0x0444;
    L_0x0096:
        r3 = "type";
        r0 = r18;
        r4 = r0.optString(r3);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r3 = "subtype";
        r0 = r18;
        r3 = r0.optString(r3);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "creativeid";
        r19 = -1;
        r0 = r18;
        r1 = r19;
        r6 = r0.optInt(r6, r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = -1;
        r0 = r19;
        if (r6 == r0) goto L_0x00bc;
    L_0x00b8:
        r13 = java.lang.String.valueOf(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x00bc:
        r6 = "feedid";
        r19 = -1;
        r0 = r18;
        r1 = r19;
        r6 = r0.optInt(r6, r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = -1;
        r0 = r19;
        if (r6 == r0) goto L_0x00d2;
    L_0x00ce:
        r21 = java.lang.String.valueOf(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x00d2:
        r6 = "mediation";
        r0 = r18;
        r19 = r0.optJSONObject(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 == 0) goto L_0x0148;
    L_0x00dc:
        r6 = "name";
        r0 = r19;
        r14 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "source";
        r0 = r19;
        r17 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "id";
        r10 = -1;
        r0 = r19;
        r6 = r0.optInt(r6, r10);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r10 = -1;
        if (r6 == r10) goto L_0x00fc;
    L_0x00f8:
        r15 = java.lang.String.valueOf(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x00fc:
        r6 = "data";
        r0 = r19;
        r6 = r0.optJSONObject(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r6 == 0) goto L_0x010c;
    L_0x0106:
        r10 = "adid";
        r16 = r6.optString(r10);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x010c:
        r6 = "imptrackers";
        r0 = r19;
        r10 = r0.optJSONArray(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "imptrackers";
        r0 = r19;
        r0.remove(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = 0;
    L_0x011c:
        if (r10 == 0) goto L_0x0126;
    L_0x011e:
        r20 = r10.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r20;
        if (r6 < r0) goto L_0x021a;
    L_0x0126:
        r6 = "jstracker";
        r0 = r19;
        r10 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "clicktrackers";
        r0 = r19;
        r20 = r0.optJSONArray(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "clicktrackers";
        r0 = r19;
        r0.remove(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = 0;
    L_0x013e:
        if (r20 == 0) goto L_0x0148;
    L_0x0140:
        r19 = r20.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        if (r6 < r0) goto L_0x022e;
    L_0x0148:
        r6 = "native";
        r6 = android.text.TextUtils.equals(r6, r4);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r6 != 0) goto L_0x0160;
    L_0x0150:
        r6 = "thirdparty";
        r6 = android.text.TextUtils.equals(r6, r4);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r6 == 0) goto L_0x0441;
    L_0x0158:
        r6 = "native";
        r6 = android.text.TextUtils.equals(r6, r3);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r6 == 0) goto L_0x0441;
    L_0x0160:
        r6 = "native";
        r0 = r18;
        r18 = r0.getJSONObject(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r18 == 0) goto L_0x0441;
    L_0x016a:
        r5 = "ver";
        r0 = r18;
        r5 = r0.optInt(r5);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "imptrackers";
        r0 = r18;
        r10 = r0.optJSONArray(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "imptrackers";
        r0 = r18;
        r0.remove(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = 0;
    L_0x0182:
        if (r10 == 0) goto L_0x018c;
    L_0x0184:
        r19 = r10.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        if (r6 < r0) goto L_0x0244;
    L_0x018c:
        r6 = "jstracker";
        r0 = r18;
        r10 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r6 = "link";
        r0 = r18;
        r19 = r0.optJSONObject(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 == 0) goto L_0x043e;
    L_0x019e:
        r6 = "url";
        r0 = r19;
        r6 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r7 = "fallback";
        r0 = r19;
        r7 = r0.optString(r7);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r12 = "clicktrackers";
        r0 = r19;
        r20 = r0.optJSONArray(r12);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r12 = "clicktrackers";
        r0 = r19;
        r0.remove(r12);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r12 = 0;
    L_0x01be:
        if (r20 == 0) goto L_0x01c8;
    L_0x01c0:
        r19 = r20.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        if (r12 < r0) goto L_0x0258;
    L_0x01c8:
        r12 = "assets";
        r0 = r18;
        r18 = r0.optJSONArray(r12);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r18 == 0) goto L_0x01e1;
    L_0x01d2:
        r12 = r18.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 <= 0) goto L_0x01e1;
    L_0x01d8:
        r12 = 0;
    L_0x01d9:
        r19 = r18.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        if (r12 < r0) goto L_0x026e;
    L_0x01e1:
        r12 = "native";
        r12 = android.text.TextUtils.equals(r12, r4);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 != 0) goto L_0x01f9;
    L_0x01e9:
        r12 = "thirdparty";
        r12 = android.text.TextUtils.equals(r12, r4);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 == 0) goto L_0x0381;
    L_0x01f1:
        r12 = "native";
        r12 = android.text.TextUtils.equals(r12, r3);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 == 0) goto L_0x0381;
    L_0x01f9:
        r12 = android.text.TextUtils.isEmpty(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 != 0) goto L_0x0381;
    L_0x01ff:
        r12 = r11.size();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r12 <= 0) goto L_0x0381;
    L_0x0205:
        r3 = new com.moceanmobile.mast.NativeAdDescriptor;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r3.m3774init(r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r24;
        r3.setNativeAdJSON(r0);	 Catch:{ IOException -> 0x042e, JSONException -> 0x041c, Exception -> 0x0408 }
    L_0x020f:
        r22.close();	 Catch:{ IOException -> 0x0214 }
        goto L_0x0072;
    L_0x0214:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0072;
    L_0x021a:
        r20 = r10.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r8 != 0) goto L_0x0226;
    L_0x0220:
        r8 = r10.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r8 = new java.lang.String[r8];	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x0226:
        if (r20 == 0) goto L_0x022a;
    L_0x0228:
        r8[r6] = r20;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x022a:
        r6 = r6 + 1;
        goto L_0x011c;
    L_0x022e:
        r0 = r20;
        r19 = r0.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r9 != 0) goto L_0x023c;
    L_0x0236:
        r9 = r20.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r9 = new java.lang.String[r9];	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x023c:
        if (r19 == 0) goto L_0x0240;
    L_0x023e:
        r9[r6] = r19;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x0240:
        r6 = r6 + 1;
        goto L_0x013e;
    L_0x0244:
        r19 = r10.optString(r6);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r8 != 0) goto L_0x0250;
    L_0x024a:
        r8 = r10.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r8 = new java.lang.String[r8];	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x0250:
        if (r19 == 0) goto L_0x0254;
    L_0x0252:
        r8[r6] = r19;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x0254:
        r6 = r6 + 1;
        goto L_0x0182;
    L_0x0258:
        r0 = r20;
        r19 = r0.optString(r12);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r9 != 0) goto L_0x0266;
    L_0x0260:
        r9 = r20.length();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r9 = new java.lang.String[r9];	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x0266:
        if (r19 == 0) goto L_0x026a;
    L_0x0268:
        r9[r12] = r19;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x026a:
        r12 = r12 + 1;
        goto L_0x01be;
    L_0x026e:
        r0 = r18;
        r19 = r0.optJSONObject(r12);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r20 = "id";
        r25 = -1;
        r0 = r19;
        r1 = r20;
        r2 = r25;
        r20 = r0.optInt(r1, r2);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25 = "img";
        r0 = r19;
        r1 = r25;
        r25 = r0.isNull(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r25 != 0) goto L_0x02c7;
    L_0x028e:
        r25 = "img";
        r0 = r19;
        r1 = r25;
        r19 = r0.optJSONObject(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25 = new com.moceanmobile.mast.bean.ImageAssetResponse;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25.m3779init();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r20;
        r1 = r25;
        r1.assetId = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = com.moceanmobile.mast.MASTNativeAd.Image.getImage(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r25;
        r1 = r19;
        r0.setImage(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = r25.getImage();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        r0 = r0.url;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = r0;
        r19 = android.text.TextUtils.isEmpty(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 != 0) goto L_0x02c3;
    L_0x02be:
        r0 = r25;
        r11.add(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
    L_0x02c3:
        r12 = r12 + 1;
        goto L_0x01d9;
    L_0x02c7:
        r25 = "title";
        r0 = r19;
        r1 = r25;
        r25 = r0.isNull(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r25 != 0) goto L_0x031c;
    L_0x02d3:
        r25 = "title";
        r0 = r19;
        r1 = r25;
        r19 = r0.optJSONObject(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25 = new com.moceanmobile.mast.bean.TitleAssetResponse;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25.m3781init();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r20;
        r1 = r25;
        r1.assetId = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r20 = "text";
        r19 = r19.optString(r20);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        r1 = r25;
        r1.titleText = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r25;
        r0 = r0.titleText;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = r0;
        r19 = android.text.TextUtils.isEmpty(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 != 0) goto L_0x02c3;
    L_0x0300:
        r0 = r25;
        r11.add(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        goto L_0x02c3;
    L_0x0306:
        r3 = move-exception;
        r4 = r3;
        r5 = r22;
        r3 = r23;
    L_0x030c:
        r4.printStackTrace();	 Catch:{ all -> 0x03ff }
        if (r5 == 0) goto L_0x0072;
    L_0x0311:
        r5.close();	 Catch:{ IOException -> 0x0316 }
        goto L_0x0072;
    L_0x0316:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0072;
    L_0x031c:
        r25 = "data";
        r0 = r19;
        r1 = r25;
        r25 = r0.isNull(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r25 != 0) goto L_0x02c3;
    L_0x0328:
        r25 = "data";
        r0 = r19;
        r1 = r25;
        r19 = r0.optJSONObject(r1);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25 = new com.moceanmobile.mast.bean.DataAssetResponse;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r25.m3777init();	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r20;
        r1 = r25;
        r1.assetId = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r20 = "value";
        r19 = r19.optString(r20);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r19;
        r1 = r25;
        r1.value = r0;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r25;
        r0 = r0.value;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r19 = r0;
        r19 = android.text.TextUtils.isEmpty(r19);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r19 != 0) goto L_0x02c3;
    L_0x0355:
        r0 = r25;
        r11.add(r0);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        goto L_0x02c3;
    L_0x035c:
        r3 = move-exception;
        r4 = r24;
        r3 = r23;
    L_0x0361:
        r5 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x03c8 }
        r5.<init>(r4);	 Catch:{ JSONException -> 0x03c8 }
        r4 = "error";
        r5 = r5.optString(r4);	 Catch:{ JSONException -> 0x03c8 }
        r4 = android.text.TextUtils.isEmpty(r5);	 Catch:{ JSONException -> 0x03c8 }
        if (r4 != 0) goto L_0x03cc;
    L_0x0372:
        r4 = new com.moceanmobile.mast.AdDescriptor;	 Catch:{ JSONException -> 0x03c8 }
        r4.m1758init();	 Catch:{ JSONException -> 0x03c8 }
        r4.errorMessage = r5;	 Catch:{ JSONException -> 0x03c8 }
        if (r22 == 0) goto L_0x037e;
    L_0x037b:
        r22.close();	 Catch:{ IOException -> 0x03c3 }
    L_0x037e:
        r3 = r4;
        goto L_0x0072;
    L_0x0381:
        r5 = "thirdparty";
        r5 = android.text.TextUtils.equals(r5, r4);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r5 == 0) goto L_0x043a;
    L_0x0389:
        r5 = "mediation";
        r3 = android.text.TextUtils.equals(r5, r3);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 == 0) goto L_0x043a;
    L_0x0391:
        r3 = android.text.TextUtils.isEmpty(r14);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 != 0) goto L_0x043a;
    L_0x0397:
        r3 = android.text.TextUtils.isEmpty(r15);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 == 0) goto L_0x03a3;
    L_0x039d:
        r3 = android.text.TextUtils.isEmpty(r13);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 != 0) goto L_0x043a;
    L_0x03a3:
        r3 = android.text.TextUtils.isEmpty(r16);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 != 0) goto L_0x043a;
    L_0x03a9:
        r3 = android.text.TextUtils.isEmpty(r17);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        if (r3 != 0) goto L_0x043a;
    L_0x03af:
        r11 = new com.moceanmobile.mast.NativeAdDescriptor;	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r12 = r4;
        r18 = r8;
        r19 = r9;
        r20 = r10;
        r11.m3775init(r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);	 Catch:{ IOException -> 0x0306, JSONException -> 0x035c, Exception -> 0x0403 }
        r0 = r24;
        r11.setNativeAdJSON(r0);	 Catch:{ IOException -> 0x0433, JSONException -> 0x0421, Exception -> 0x040a }
        r3 = r11;
        goto L_0x020f;
    L_0x03c3:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x037e;
    L_0x03c8:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ all -> 0x03fd }
    L_0x03cc:
        if (r22 == 0) goto L_0x0072;
    L_0x03ce:
        r22.close();	 Catch:{ IOException -> 0x03d3 }
        goto L_0x0072;
    L_0x03d3:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0072;
    L_0x03d9:
        r3 = move-exception;
        r22 = r4;
        r4 = r3;
        r3 = r23;
    L_0x03df:
        r4.printStackTrace();	 Catch:{ all -> 0x03fd }
        if (r22 == 0) goto L_0x0072;
    L_0x03e4:
        r22.close();	 Catch:{ IOException -> 0x03e9 }
        goto L_0x0072;
    L_0x03e9:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x0072;
    L_0x03ef:
        r3 = move-exception;
        r22 = r4;
    L_0x03f2:
        if (r22 == 0) goto L_0x03f7;
    L_0x03f4:
        r22.close();	 Catch:{ IOException -> 0x03f8 }
    L_0x03f7:
        throw r3;
    L_0x03f8:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x03f7;
    L_0x03fd:
        r3 = move-exception;
        goto L_0x03f2;
    L_0x03ff:
        r3 = move-exception;
        r22 = r5;
        goto L_0x03f2;
    L_0x0403:
        r3 = move-exception;
        r4 = r3;
        r3 = r23;
        goto L_0x03df;
    L_0x0408:
        r4 = move-exception;
        goto L_0x03df;
    L_0x040a:
        r3 = move-exception;
        r4 = r3;
        r3 = r11;
        goto L_0x03df;
    L_0x040e:
        r5 = move-exception;
        r22 = r4;
        r4 = r3;
        r3 = r23;
        goto L_0x0361;
    L_0x0416:
        r4 = move-exception;
        r4 = r3;
        r3 = r23;
        goto L_0x0361;
    L_0x041c:
        r4 = move-exception;
        r4 = r24;
        goto L_0x0361;
    L_0x0421:
        r3 = move-exception;
        r4 = r24;
        r3 = r11;
        goto L_0x0361;
    L_0x0427:
        r3 = move-exception;
        r5 = r4;
        r4 = r3;
        r3 = r23;
        goto L_0x030c;
    L_0x042e:
        r4 = move-exception;
        r5 = r22;
        goto L_0x030c;
    L_0x0433:
        r3 = move-exception;
        r4 = r3;
        r5 = r22;
        r3 = r11;
        goto L_0x030c;
    L_0x043a:
        r3 = r23;
        goto L_0x020f;
    L_0x043e:
        r6 = r12;
        goto L_0x01c8;
    L_0x0441:
        r6 = r12;
        goto L_0x01e1;
    L_0x0444:
        r3 = r4;
        r4 = r6;
        r6 = r12;
        goto L_0x01e1;
    L_0x0449:
        r4 = r18;
        goto L_0x007a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.moceanmobile.mast.AdDescriptor.parseNativeResponse(java.io.InputStream):com.moceanmobile.mast.AdDescriptor");
    }

    AdDescriptor() {
        this.mImpressionTrackers = new ArrayList();
        this.mClickTrackers = new ArrayList();
        this.mMediationData = null;
        this.errorMessage = null;
        this.adInfo = null;
    }

    public AdDescriptor(Map<String, String> adInfo) {
        this.mImpressionTrackers = new ArrayList();
        this.mClickTrackers = new ArrayList();
        this.mMediationData = null;
        this.errorMessage = null;
        this.adInfo = adInfo;
    }

    public String getType() {
        return (String) this.adInfo.get("type");
    }

    public String getWidth() {
        return (String) this.adInfo.get(VastIconXmlManager.WIDTH);
    }

    public String getHeight() {
        return (String) this.adInfo.get(VastIconXmlManager.HEIGHT);
    }

    public String getSubType() {
        return (String) this.adInfo.get(MASTNativeAdConstants.RESPONSE_SUBTYPE);
    }

    public String getURL() {
        return (String) this.adInfo.get(MASTNativeAdConstants.RESPONSE_URL);
    }

    public String getTrack() {
        return (String) this.adInfo.get("track");
    }

    public String getImage() {
        return (String) this.adInfo.get("img");
    }

    public String getImageType() {
        return (String) this.adInfo.get("imgType");
    }

    public String getText() {
        return (String) this.adInfo.get(MASTNativeAdConstants.RESPONSE_TEXT);
    }

    public String getContent() {
        return (String) this.adInfo.get("content");
    }

    public String getAdCreativeId() {
        return (String) this.adInfo.get(MASTNativeAdConstants.RESPONSE_CREATIVEID);
    }

    public ArrayList<String> getImpressionTrackers() {
        return this.mImpressionTrackers;
    }

    public void setImpressionTrackers(ArrayList<String> mImpressionTrackers) {
        this.mImpressionTrackers.clear();
        this.mImpressionTrackers = mImpressionTrackers;
    }

    public ArrayList<String> getClickTrackers() {
        return this.mClickTrackers;
    }

    public void setClickTrackers(ArrayList<String> clickTrackers) {
        if (this.mClickTrackers != null) {
            this.mClickTrackers.clear();
        }
        this.mClickTrackers = clickTrackers;
    }

    public MediationData getMediationData() {
        return this.mMediationData;
    }

    public void setMediationData(MediationData mediationData) {
        this.mMediationData = mediationData;
    }

    /* access modifiers changed from: 0000 */
    public String getErrroMessage() {
        return this.errorMessage;
    }
}
