package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.z;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public class b {
    protected static Map<String, Integer> a(String str) {
        if (str == null) {
            return null;
        }
        try {
            HashMap hashMap = new HashMap();
            for (String split : str.split(",")) {
                String[] split2 = split.split(":");
                if (split2.length != 2) {
                    z.e("error format at %s", split);
                    return null;
                }
                hashMap.put(split2[0], Integer.valueOf(Integer.parseInt(split2[1])));
            }
            return hashMap;
        } catch (Exception e) {
            z.e("error format intStateStr %s", str);
            e.printStackTrace();
            return null;
        }
    }

    protected static String b(String str) {
        if (str == null) {
            return "";
        }
        String[] split = str.split(MASTNativeAdConstants.NEWLINE);
        if (split == null && split.length == 0) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : split) {
            if (!str2.contains("java.lang.Thread.getStackTrace(")) {
                stringBuilder.append(str2).append(MASTNativeAdConstants.NEWLINE);
            }
        }
        return stringBuilder.toString();
    }

    protected static CrashDetailBean a(Context context, Map<String, String> map, NativeExceptionHandler nativeExceptionHandler) {
        if (map == null) {
            return null;
        }
        if (a.a(context) == null) {
            z.e("abnormal com info not created", new Object[0]);
            return null;
        }
        String str = (String) map.get("intStateStr");
        if (str == null || str.trim().length() <= 0) {
            z.e("no intStateStr", new Object[0]);
            return null;
        }
        Map a = a(str);
        if (a == null) {
            z.e("parse intSateMap fail", Integer.valueOf(map.size()));
            return null;
        }
        try {
            int intValue = ((Integer) a.get("ep")).intValue();
            int intValue2 = ((Integer) a.get("et")).intValue();
            ((Integer) a.get("sino")).intValue();
            int intValue3 = ((Integer) a.get("sico")).intValue();
            ((Integer) a.get("spd")).intValue();
            ((Integer) a.get("sud")).intValue();
            long intValue4 = (long) ((Integer) a.get("ets")).intValue();
            long intValue5 = (long) ((Integer) a.get("etms")).intValue();
            String str2 = (String) map.get("soVersion");
            if (str2 == null) {
                z.e("error format at version", new Object[0]);
                return null;
            }
            String str3;
            String str4;
            String str5;
            String str6;
            String str7;
            str = (String) map.get("errorAddr");
            String str8 = str == null ? "unknown2" : str;
            str = (String) map.get("codeMsg");
            if (str == null) {
                str3 = "unknown2";
            } else {
                str3 = str;
            }
            str = (String) map.get("tombPath");
            if (str == null) {
                str4 = "unknown2";
            } else {
                str4 = str;
            }
            str = (String) map.get("signalName");
            if (str == null) {
                str5 = "unknown2";
            } else {
                str5 = str;
            }
            str = (String) map.get("errnoMsg");
            str = (String) map.get("stack");
            if (str == null) {
                str6 = "unknown2";
            } else {
                str6 = str;
            }
            str = (String) map.get("jstack");
            if (str != null) {
                str = str6 + "java:\n" + str;
            } else {
                str = str6;
            }
            intValue4 = (intValue4 * 1000) + (intValue5 / 1000);
            String b = b(str);
            str = Const.CONNECTION_TYPE_UNKNOWN;
            String str9 = "UNKNOWN(" + intValue + ")";
            if (intValue3 > 0) {
                str5 = str5 + "(" + str3 + ")";
                str3 = "KERNEL";
            }
            str = (String) map.get("nativeLog");
            byte[] bArr = null;
            if (!(str == null || str.isEmpty())) {
                bArr = ag.a(null, str);
            }
            str = (String) map.get("processName");
            if (str == null) {
                str6 = "unknown(" + intValue + ")";
            } else {
                str6 = str;
            }
            str = (String) map.get("threadName");
            if (str == null) {
                str7 = "unknown(" + intValue2 + ")";
            } else {
                str7 = str;
            }
            Map map2 = null;
            str = (String) map.get("key-value");
            if (str != null) {
                map2 = new HashMap();
                for (String split : str.split(MASTNativeAdConstants.NEWLINE)) {
                    String[] split2 = split.split(MASTNativeAdConstants.EQUAL);
                    if (split2.length == 2) {
                        map2.put(split2[0], split2[1]);
                    }
                }
            }
            CrashDetailBean packageCrashDatas = nativeExceptionHandler.packageCrashDatas(str6, str7, intValue4, str5, str8, b, str3, str9, str4, str2, bArr, map2, false);
            if (packageCrashDatas == null) {
                return packageCrashDatas;
            }
            packageCrashDatas.y = null;
            packageCrashDatas.k = true;
            return packageCrashDatas;
        } catch (Throwable th) {
            z.e("error format", new Object[0]);
            th.printStackTrace();
            return null;
        }
    }

    protected static String a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                return null;
            }
            if (read == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append((char) read);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x009f A:{SYNTHETIC, Splitter:B:56:0x009f} */
    public static com.tencent.bugly.crashreport.crash.CrashDetailBean a(android.content.Context r6, java.lang.String r7, com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler r8) {
        /*
        r2 = 0;
        r0 = 0;
        if (r6 == 0) goto L_0x0008;
    L_0x0004:
        if (r7 == 0) goto L_0x0008;
    L_0x0006:
        if (r8 != 0) goto L_0x0010;
    L_0x0008:
        r1 = "get eup record file args error";
        r2 = new java.lang.Object[r2];
        com.tencent.bugly.proguard.z.e(r1, r2);
    L_0x000f:
        return r0;
    L_0x0010:
        r1 = new java.io.File;
        r2 = "rqd_record.eup";
        r1.<init>(r7, r2);
        r2 = r1.exists();
        if (r2 == 0) goto L_0x000f;
    L_0x001d:
        r2 = r1.canRead();
        if (r2 == 0) goto L_0x000f;
    L_0x0023:
        r2 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0088, all -> 0x009a }
        r2.<init>(r1);	 Catch:{ IOException -> 0x0088, all -> 0x009a }
        r1 = a(r2);	 Catch:{ IOException -> 0x00aa }
        if (r1 == 0) goto L_0x0036;
    L_0x002e:
        r3 = "NATIVE_RQD_REPORT";
        r3 = r1.equals(r3);	 Catch:{ IOException -> 0x00aa }
        if (r3 != 0) goto L_0x004c;
    L_0x0036:
        r3 = "record read fail! %s";
        r4 = 1;
        r4 = new java.lang.Object[r4];	 Catch:{ IOException -> 0x00aa }
        r5 = 0;
        r4[r5] = r1;	 Catch:{ IOException -> 0x00aa }
        com.tencent.bugly.proguard.z.e(r3, r4);	 Catch:{ IOException -> 0x00aa }
        if (r2 == 0) goto L_0x000f;
    L_0x0043:
        r2.close();	 Catch:{ IOException -> 0x0047 }
        goto L_0x000f;
    L_0x0047:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x004c:
        r4 = new java.util.HashMap;	 Catch:{ IOException -> 0x00aa }
        r4.<init>();	 Catch:{ IOException -> 0x00aa }
        r1 = r0;
    L_0x0052:
        r3 = a(r2);	 Catch:{ IOException -> 0x00aa }
        if (r3 == 0) goto L_0x0061;
    L_0x0058:
        if (r1 != 0) goto L_0x005c;
    L_0x005a:
        r1 = r3;
        goto L_0x0052;
    L_0x005c:
        r4.put(r1, r3);	 Catch:{ IOException -> 0x00aa }
        r1 = r0;
        goto L_0x0052;
    L_0x0061:
        if (r1 == 0) goto L_0x0079;
    L_0x0063:
        r3 = "record not pair! drop! %s";
        r4 = 1;
        r4 = new java.lang.Object[r4];	 Catch:{ IOException -> 0x00aa }
        r5 = 0;
        r4[r5] = r1;	 Catch:{ IOException -> 0x00aa }
        com.tencent.bugly.proguard.z.e(r3, r4);	 Catch:{ IOException -> 0x00aa }
        if (r2 == 0) goto L_0x000f;
    L_0x0070:
        r2.close();	 Catch:{ IOException -> 0x0074 }
        goto L_0x000f;
    L_0x0074:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x0079:
        r0 = a(r6, r4, r8);	 Catch:{ IOException -> 0x00aa }
        if (r2 == 0) goto L_0x000f;
    L_0x007f:
        r2.close();	 Catch:{ IOException -> 0x0083 }
        goto L_0x000f;
    L_0x0083:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x0088:
        r1 = move-exception;
        r2 = r0;
    L_0x008a:
        r1.printStackTrace();	 Catch:{ all -> 0x00a8 }
        if (r2 == 0) goto L_0x000f;
    L_0x008f:
        r2.close();	 Catch:{ IOException -> 0x0094 }
        goto L_0x000f;
    L_0x0094:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x009a:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x009d:
        if (r2 == 0) goto L_0x00a2;
    L_0x009f:
        r2.close();	 Catch:{ IOException -> 0x00a3 }
    L_0x00a2:
        throw r0;
    L_0x00a3:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00a2;
    L_0x00a8:
        r0 = move-exception;
        goto L_0x009d;
    L_0x00aa:
        r1 = move-exception;
        goto L_0x008a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.b.a(android.content.Context, java.lang.String, com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler):com.tencent.bugly.crashreport.crash.CrashDetailBean");
    }

    public static void c(String str) {
        File file = new File(str, "rqd_record.eup");
        if (file.exists() && file.canWrite()) {
            file.delete();
            z.c("delete record file %s", file.getAbsoluteFile());
        }
    }
}
