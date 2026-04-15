package com.tencent.bugly.crashreport.crash;

import com.moceanmobile.mast.MASTNativeAdConstants;
import com.tencent.bugly.proguard.z;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: BUGLY */
public class d {
    public static String a(Throwable th, int i) {
        if (th.getMessage() == null) {
            return "";
        }
        if (i < 0 || th.getMessage().length() <= i) {
            return th.getMessage();
        }
        return th.getMessage().substring(0, i) + "\n[Message over limit size:" + i + ", has been cutted!]";
    }

    public static String b(Throwable th, int i) {
        if (th == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (th.getStackTrace() != null) {
                StackTraceElement[] stackTrace = th.getStackTrace();
                int length = stackTrace.length;
                int i2 = 0;
                while (i2 < length) {
                    StackTraceElement stackTraceElement = stackTrace[i2];
                    if (i <= 0 || stringBuilder.length() < i) {
                        stringBuilder.append(stackTraceElement.toString()).append(MASTNativeAdConstants.NEWLINE);
                        i2++;
                    } else {
                        stringBuilder.append("\n[Stack over limit size :" + i + " , has been cutted !]");
                        return stringBuilder.toString();
                    }
                }
            }
        } catch (Throwable th2) {
            z.e("gen stack error %s", th2.toString());
        }
        return stringBuilder.toString();
    }

    public static Throwable a(Throwable th) {
        if (th == null) {
            return null;
        }
        while (th.getCause() != null) {
            th = th.getCause();
        }
        return th;
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ed A:{SYNTHETIC, Splitter:B:55:0x00ed} */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ed A:{SYNTHETIC, Splitter:B:55:0x00ed} */
    public static java.lang.String a(android.content.Context r6, int r7) {
        /*
        r0 = 0;
        r3 = 0;
        r1 = com.tencent.bugly.crashreport.common.info.AppInfo.c(r6);
        if (r1 != 0) goto L_0x0010;
    L_0x0008:
        r1 = "no read_log permission!";
        r2 = new java.lang.Object[r3];
        com.tencent.bugly.proguard.z.d(r1, r2);
    L_0x000f:
        return r0;
    L_0x0010:
        r1 = 4;
        r1 = new java.lang.String[r1];
        r2 = "logcat";
        r1[r3] = r2;
        r2 = 1;
        r3 = "-d";
        r1[r2] = r3;
        r2 = 2;
        r3 = "-v";
        r1[r2] = r3;
        r2 = 3;
        r3 = "threadtime";
        r1[r2] = r3;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r2 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x0117, all -> 0x00e8 }
        r2 = r2.exec(r1);	 Catch:{ Throwable -> 0x0117, all -> 0x00e8 }
        r0 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r1 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r4 = r2.getInputStream();	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r1.<init>(r4);	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
    L_0x0041:
        r1 = r0.readLine();	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        if (r1 == 0) goto L_0x00b1;
    L_0x0047:
        r1 = r3.append(r1);	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r4 = "\n";
        r1.append(r4);	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        if (r7 <= 0) goto L_0x0041;
    L_0x0052:
        r1 = r3.length();	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        if (r1 <= r7) goto L_0x0041;
    L_0x0058:
        r1 = 0;
        r4 = r3.length();	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        r4 = r4 - r7;
        r3.delete(r1, r4);	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        goto L_0x0041;
    L_0x0062:
        r0 = move-exception;
        r1 = r2;
    L_0x0064:
        r2 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x0114 }
        if (r2 != 0) goto L_0x006d;
    L_0x006a:
        r0.printStackTrace();	 Catch:{ all -> 0x0114 }
    L_0x006d:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0114 }
        r2.<init>();	 Catch:{ all -> 0x0114 }
        r4 = "\n[error:";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0114 }
        r0 = r0.toString();	 Catch:{ all -> 0x0114 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0114 }
        r2 = "]";
        r0 = r0.append(r2);	 Catch:{ all -> 0x0114 }
        r0 = r0.toString();	 Catch:{ all -> 0x0114 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x0114 }
        r0 = r0.toString();	 Catch:{ all -> 0x0114 }
        if (r1 == 0) goto L_0x000f;
    L_0x0094:
        r2 = r1.getOutputStream();	 Catch:{ IOException -> 0x00de }
        r2.close();	 Catch:{ IOException -> 0x00de }
    L_0x009b:
        r2 = r1.getInputStream();	 Catch:{ IOException -> 0x00e3 }
        r2.close();	 Catch:{ IOException -> 0x00e3 }
    L_0x00a2:
        r1 = r1.getErrorStream();	 Catch:{ IOException -> 0x00ab }
        r1.close();	 Catch:{ IOException -> 0x00ab }
        goto L_0x000f;
    L_0x00ab:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x00b1:
        r0 = r3.toString();	 Catch:{ Throwable -> 0x0062, all -> 0x0112 }
        if (r2 == 0) goto L_0x000f;
    L_0x00b7:
        r1 = r2.getOutputStream();	 Catch:{ IOException -> 0x00d4 }
        r1.close();	 Catch:{ IOException -> 0x00d4 }
    L_0x00be:
        r1 = r2.getInputStream();	 Catch:{ IOException -> 0x00d9 }
        r1.close();	 Catch:{ IOException -> 0x00d9 }
    L_0x00c5:
        r1 = r2.getErrorStream();	 Catch:{ IOException -> 0x00ce }
        r1.close();	 Catch:{ IOException -> 0x00ce }
        goto L_0x000f;
    L_0x00ce:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x00d4:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00be;
    L_0x00d9:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00c5;
    L_0x00de:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x009b;
    L_0x00e3:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x00a2;
    L_0x00e8:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x00eb:
        if (r2 == 0) goto L_0x0102;
    L_0x00ed:
        r1 = r2.getOutputStream();	 Catch:{ IOException -> 0x0103 }
        r1.close();	 Catch:{ IOException -> 0x0103 }
    L_0x00f4:
        r1 = r2.getInputStream();	 Catch:{ IOException -> 0x0108 }
        r1.close();	 Catch:{ IOException -> 0x0108 }
    L_0x00fb:
        r1 = r2.getErrorStream();	 Catch:{ IOException -> 0x010d }
        r1.close();	 Catch:{ IOException -> 0x010d }
    L_0x0102:
        throw r0;
    L_0x0103:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00f4;
    L_0x0108:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00fb;
    L_0x010d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0102;
    L_0x0112:
        r0 = move-exception;
        goto L_0x00eb;
    L_0x0114:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00eb;
    L_0x0117:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
        goto L_0x0064;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.d.a(android.content.Context, int):java.lang.String");
    }

    public static Map<String, String> a(int i, boolean z) {
        HashMap hashMap = new HashMap(12);
        Map allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces == null) {
            return null;
        }
        long id = Thread.currentThread().getId();
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : allStackTraces.entrySet()) {
            if (!z || id != ((Thread) entry.getKey()).getId()) {
                stringBuilder.setLength(0);
                if (!(entry.getValue() == null || ((StackTraceElement[]) entry.getValue()).length == 0)) {
                    for (StackTraceElement stackTraceElement : (StackTraceElement[]) entry.getValue()) {
                        if (i > 0 && stringBuilder.length() >= i) {
                            stringBuilder.append("\n[Stack over limit size :" + i + " , has been cutted !]");
                            break;
                        }
                        stringBuilder.append(stackTraceElement.toString()).append(MASTNativeAdConstants.NEWLINE);
                    }
                    hashMap.put(((Thread) entry.getKey()).getName() + "(" + ((Thread) entry.getKey()).getId() + ")", stringBuilder.toString());
                }
            }
        }
        return hashMap;
    }
}
