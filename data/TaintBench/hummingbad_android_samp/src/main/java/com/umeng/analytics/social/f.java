package com.umeng.analytics.social;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.umeng.analytics.onlineconfig.a;
import com.umeng.analytics.social.UMPlatformData.GENDER;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* compiled from: UMUtils */
public abstract class f {
    private static Map<String, String> a;

    protected static String[] a(Context context, String str, UMPlatformData... uMPlatformDataArr) throws a {
        if (uMPlatformDataArr == null || uMPlatformDataArr.length == 0) {
            throw new a("platform data is null");
        }
        String a = a(context);
        if (TextUtils.isEmpty(a)) {
            throw new a("can`t get appkey.");
        }
        List arrayList = new ArrayList();
        String str2 = "http://log.umsns.com/share/api/" + a + "/";
        if (a == null || a.isEmpty()) {
            a = c(context);
        }
        if (!(a == null || a.isEmpty())) {
            for (Entry entry : a.entrySet()) {
                arrayList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
        }
        arrayList.add(new BasicNameValuePair("date", String.valueOf(System.currentTimeMillis())));
        arrayList.add(new BasicNameValuePair(a.c, e.e));
        if (!TextUtils.isEmpty(str)) {
            arrayList.add(new BasicNameValuePair("topic", str));
        }
        arrayList.addAll(a(uMPlatformDataArr));
        a = b(uMPlatformDataArr);
        if (a == null) {
            a = "null";
        }
        b.c(com.umeng.analytics.a.e, "URL:" + new StringBuilder(String.valueOf(str2)).append(MASTNativeAdConstants.QUESTIONMARK).append(a(arrayList)).toString());
        b.c(com.umeng.analytics.a.e, "BODY:" + a);
        return new String[]{r1, a};
    }

    private static String a(List<NameValuePair> list) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new UrlEncodedFormEntity(list, Defaults.ENCODING_UTF_8).writeTo(byteArrayOutputStream);
            return byteArrayOutputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<NameValuePair> a(UMPlatformData... uMPlatformDataArr) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        for (UMPlatformData uMPlatformData : uMPlatformDataArr) {
            stringBuilder.append(uMPlatformData.getMeida().toString());
            stringBuilder.append(',');
            stringBuilder2.append(uMPlatformData.getUsid());
            stringBuilder2.append(',');
            stringBuilder3.append(uMPlatformData.getWeiboId());
            stringBuilder3.append(',');
        }
        if (uMPlatformDataArr.length > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
            stringBuilder3.deleteCharAt(stringBuilder3.length() - 1);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("platform", stringBuilder.toString()));
        arrayList.add(new BasicNameValuePair("usid", stringBuilder2.toString()));
        if (stringBuilder3.length() > 0) {
            arrayList.add(new BasicNameValuePair("weiboid", stringBuilder3.toString()));
        }
        return arrayList;
    }

    private static String b(UMPlatformData... uMPlatformDataArr) {
        JSONObject jSONObject = new JSONObject();
        for (UMPlatformData uMPlatformData : uMPlatformDataArr) {
            GENDER gender = uMPlatformData.getGender();
            String name = uMPlatformData.getName();
            if (gender == null) {
                try {
                    if (TextUtils.isEmpty(name)) {
                    }
                } catch (Exception e) {
                    throw new a("build body exception", e);
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("gender", gender == null ? "" : String.valueOf(gender.value));
            jSONObject2.put("name", name == null ? "" : String.valueOf(name));
            jSONObject.put(uMPlatformData.getMeida().toString(), jSONObject2);
        }
        if (jSONObject.length() == 0) {
            return null;
        }
        return jSONObject.toString();
    }

    private static Map<String, String> c(Context context) throws a {
        HashMap hashMap = new HashMap();
        Map b = b(context);
        if (b == null || b.isEmpty()) {
            throw new a("can`t get device id.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (Entry entry : b.entrySet()) {
            if (!TextUtils.isEmpty((CharSequence) entry.getValue())) {
                stringBuilder2.append((String) entry.getKey()).append(",");
                stringBuilder.append((String) entry.getValue()).append(",");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            hashMap.put("deviceid", stringBuilder.toString());
        }
        if (stringBuilder2.length() > 0) {
            stringBuilder2.deleteCharAt(stringBuilder2.length() - 1);
            hashMap.put("idtype", stringBuilder2.toString());
        }
        return hashMap;
    }

    protected static String a(Context context) {
        String str = e.d;
        if (TextUtils.isEmpty(str)) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if (applicationInfo != null) {
                    str = applicationInfo.metaData.getString("UMENG_APPKEY");
                    if (str != null) {
                        return str.trim();
                    }
                    b.b(com.umeng.analytics.a.e, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.");
                }
            } catch (Exception e) {
                b.b(com.umeng.analytics.a.e, "Could not read UMENG_APPKEY meta-data from AndroidManifest.xml.", e);
            }
            return null;
        }
        b.b(com.umeng.analytics.a.e, "use usefully appkey from constant field.");
        return str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004d  */
    public static java.util.Map<java.lang.String, java.lang.String> b(android.content.Context r5) {
        /*
        r2 = new java.util.HashMap;
        r2.<init>();
        r0 = "phone";
        r0 = r5.getSystemService(r0);
        r0 = (android.telephony.TelephonyManager) r0;
        if (r0 != 0) goto L_0x0016;
    L_0x000f:
        r1 = "MobclickAgent";
        r3 = "No IMEI.";
        com.umeng.analytics.social.b.e(r1, r3);
    L_0x0016:
        r1 = 0;
        r3 = "android.permission.READ_PHONE_STATE";
        r3 = a(r5, r3);	 Catch:{ Exception -> 0x0053 }
        if (r3 == 0) goto L_0x005b;
    L_0x001f:
        r0 = r0.getDeviceId();	 Catch:{ Exception -> 0x0053 }
    L_0x0023:
        r1 = d(r5);
        r3 = r5.getContentResolver();
        r4 = "android_id";
        r3 = android.provider.Settings.Secure.getString(r3, r4);
        r4 = android.text.TextUtils.isEmpty(r1);
        if (r4 != 0) goto L_0x003c;
    L_0x0037:
        r4 = "mac";
        r2.put(r4, r1);
    L_0x003c:
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 != 0) goto L_0x0047;
    L_0x0042:
        r1 = "imei";
        r2.put(r1, r0);
    L_0x0047:
        r0 = android.text.TextUtils.isEmpty(r3);
        if (r0 != 0) goto L_0x0052;
    L_0x004d:
        r0 = "android_id";
        r2.put(r0, r3);
    L_0x0052:
        return r2;
    L_0x0053:
        r0 = move-exception;
        r3 = "MobclickAgent";
        r4 = "No IMEI.";
        com.umeng.analytics.social.b.e(r3, r4, r0);
    L_0x005b:
        r0 = r1;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.social.f.b(android.content.Context):java.util.Map");
    }

    private static boolean a(Context context, String str) {
        if (context.getPackageManager().checkPermission(str, context.getPackageName()) != 0) {
            return false;
        }
        return true;
    }

    private static String d(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (a(context, "android.permission.ACCESS_WIFI_STATE")) {
                return wifiManager.getConnectionInfo().getMacAddress();
            }
            b.e(com.umeng.analytics.a.e, "Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
            return "";
        } catch (Exception e) {
            b.e(com.umeng.analytics.a.e, "Could not get mac address." + e.toString());
        }
    }
}
