package com.baidu.android.common.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.text.TextUtils;
import com.baidu.android.common.security.AESUtil;
import com.baidu.android.common.security.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public final class DeviceId {
    private static final String AES_KEY = "30212102dicudiab";
    private static final boolean DEBUG = false;
    private static final String EXT_FILE = "baidu/.cuid";
    private static final String KEY_DEVICE_ID = "com.baidu.deviceid";
    private static final String TAG = "DeviceId";

    static final class IMEIInfo {
        private static final String KEY_IMEI = "bd_setting_i";
        public final boolean CAN_READ_AND_WRITE_SYSTEM_SETTINGS;
        public final String IMEI;

        private IMEIInfo(String str, boolean z) {
            this.IMEI = str;
            this.CAN_READ_AND_WRITE_SYSTEM_SETTINGS = z;
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0020  */
        /* JADX WARNING: Removed duplicated region for block: B:11:? A:{SYNTHETIC, RETURN, ORIG_RETURN} */
        private static java.lang.String getIMEI(android.content.Context r4, java.lang.String r5) {
            /*
            r1 = 0;
            r0 = "phone";
            r0 = r4.getSystemService(r0);	 Catch:{ Exception -> 0x0016 }
            r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x0016 }
            if (r0 == 0) goto L_0x001e;
        L_0x000b:
            r0 = r0.getDeviceId();	 Catch:{ Exception -> 0x0016 }
        L_0x000f:
            r1 = android.text.TextUtils.isEmpty(r0);
            if (r1 == 0) goto L_0x0020;
        L_0x0015:
            return r5;
        L_0x0016:
            r0 = move-exception;
            r2 = "DeviceId";
            r3 = "Read IMEI failed";
            com.baidu.android.common.logging.Log.e(r2, r3, r0);
        L_0x001e:
            r0 = r1;
            goto L_0x000f;
        L_0x0020:
            r5 = r0;
            goto L_0x0015;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.common.util.DeviceId$IMEIInfo.getIMEI(android.content.Context, java.lang.String):java.lang.String");
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0045  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0049  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x003d  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0045  */
        static com.baidu.android.common.util.DeviceId.IMEIInfo getIMEIInfo(android.content.Context r7) {
            /*
            r3 = 1;
            r2 = 0;
            r1 = "";
            r0 = r7.getContentResolver();	 Catch:{ Exception -> 0x002c }
            r4 = "bd_setting_i";
            r1 = android.provider.Settings.System.getString(r0, r4);	 Catch:{ Exception -> 0x002c }
            r0 = android.text.TextUtils.isEmpty(r1);	 Catch:{ Exception -> 0x002c }
            if (r0 == 0) goto L_0x004b;
        L_0x0014:
            r0 = "";
            r0 = getIMEI(r7, r0);	 Catch:{ Exception -> 0x002c }
        L_0x001a:
            r1 = r7.getContentResolver();	 Catch:{ Exception -> 0x0047 }
            r4 = "bd_setting_i";
            android.provider.Settings.System.putString(r1, r4, r0);	 Catch:{ Exception -> 0x0047 }
            r1 = r2;
        L_0x0024:
            r4 = new com.baidu.android.common.util.DeviceId$IMEIInfo;
            if (r1 != 0) goto L_0x0045;
        L_0x0028:
            r4.m616init(r0, r3);
            return r4;
        L_0x002c:
            r0 = move-exception;
            r6 = r0;
            r0 = r1;
            r1 = r6;
        L_0x0030:
            r4 = "DeviceId";
            r5 = "Settings.System.getString or putString failed";
            com.baidu.android.common.logging.Log.e(r4, r5, r1);
            r1 = android.text.TextUtils.isEmpty(r0);
            if (r1 == 0) goto L_0x0049;
        L_0x003d:
            r0 = "";
            r0 = getIMEI(r7, r0);
            r1 = r3;
            goto L_0x0024;
        L_0x0045:
            r3 = r2;
            goto L_0x0028;
        L_0x0047:
            r1 = move-exception;
            goto L_0x0030;
        L_0x0049:
            r1 = r3;
            goto L_0x0024;
        L_0x004b:
            r0 = r1;
            goto L_0x001a;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.android.common.util.DeviceId$IMEIInfo.getIMEIInfo(android.content.Context):com.baidu.android.common.util.DeviceId$IMEIInfo");
        }
    }

    private DeviceId() {
    }

    private static void checkPermission(Context context, String str) {
        if ((context.checkCallingOrSelfPermission(str) == 0 ? 1 : null) == null) {
            throw new SecurityException("Permission Denial: requires permission " + str);
        }
    }

    public static String getAndroidId(Context context) {
        String str = "";
        str = Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public static String getDeviceID(Context context) {
        checkPermission(context, "android.permission.WRITE_SETTINGS");
        checkPermission(context, "android.permission.READ_PHONE_STATE");
        checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        IMEIInfo iMEIInfo = IMEIInfo.getIMEIInfo(context);
        String str = iMEIInfo.IMEI;
        boolean z = !iMEIInfo.CAN_READ_AND_WRITE_SYSTEM_SETTINGS ? true : DEBUG;
        String androidId = getAndroidId(context);
        String str2 = "";
        if (z) {
            return Util.toMd5(("com.baidu" + androidId).getBytes(), true);
        }
        String str3 = null;
        CharSequence string = System.getString(context.getContentResolver(), KEY_DEVICE_ID);
        if (TextUtils.isEmpty(string)) {
            str3 = Util.toMd5(("com.baidu" + str + androidId).getBytes(), true);
            string = System.getString(context.getContentResolver(), str3);
            if (!TextUtils.isEmpty(string)) {
                System.putString(context.getContentResolver(), KEY_DEVICE_ID, string);
                setExternalDeviceId(str, string);
            }
        }
        if (TextUtils.isEmpty(string)) {
            string = getExternalDeviceId(str);
            if (!TextUtils.isEmpty(string)) {
                System.putString(context.getContentResolver(), str3, string);
                System.putString(context.getContentResolver(), KEY_DEVICE_ID, string);
            }
        }
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        str2 = Util.toMd5((str + androidId + UUID.randomUUID().toString()).getBytes(), true);
        System.putString(context.getContentResolver(), str3, str2);
        System.putString(context.getContentResolver(), KEY_DEVICE_ID, str2);
        setExternalDeviceId(str, str2);
        return str2;
    }

    private static String getExternalDeviceId(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String str2 = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Environment.getExternalStorageDirectory(), EXT_FILE)));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine);
                stringBuilder.append("\r\n");
            }
            bufferedReader.close();
            String[] split = new String(AESUtil.decrypt(AES_KEY, AES_KEY, Base64.decode(stringBuilder.toString().getBytes()))).split("=");
            return (split != null && split.length == 2 && str.equals(split[0])) ? split[1] : str2;
        } catch (FileNotFoundException e) {
            return str2;
        } catch (IOException | Exception e2) {
            return str2;
        }
    }

    public static String getIMEI(Context context) {
        return IMEIInfo.getIMEIInfo(context).IMEI;
    }

    private static void setExternalDeviceId(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("=");
            stringBuilder.append(str2);
            File file = new File(Environment.getExternalStorageDirectory(), EXT_FILE);
            try {
                new File(file.getParent()).mkdirs();
                FileWriter fileWriter = new FileWriter(file, DEBUG);
                fileWriter.write(Base64.encode(AESUtil.encrypt(AES_KEY, AES_KEY, stringBuilder.toString().getBytes()), "utf-8"));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException | Exception e) {
            }
        }
    }
}
