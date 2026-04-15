package com.yxx.jiejie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.apache.http.protocol.HTTP;

public class FileUtil {
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0038 A:{SYNTHETIC, Splitter:B:21:0x0038} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003d A:{Catch:{ IOException -> 0x0041 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0049 A:{SYNTHETIC, Splitter:B:29:0x0049} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x004e A:{Catch:{ IOException -> 0x0052 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0049 A:{SYNTHETIC, Splitter:B:29:0x0049} */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x004e A:{Catch:{ IOException -> 0x0052 }} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0038 A:{SYNTHETIC, Splitter:B:21:0x0038} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003d A:{Catch:{ IOException -> 0x0041 }} */
    public static java.lang.String getFromAsset(android.content.Context r9, java.lang.String r10) {
        /*
        r3 = 0;
        r5 = 0;
        r0 = 0;
        r7 = "";
        r8 = r9.getResources();	 Catch:{ IOException -> 0x0032 }
        r8 = r8.getAssets();	 Catch:{ IOException -> 0x0032 }
        r3 = r8.open(r10);	 Catch:{ IOException -> 0x0032 }
        r6 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x0032 }
        r6.<init>(r3);	 Catch:{ IOException -> 0x0032 }
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0065, all -> 0x005e }
        r1.<init>(r6);	 Catch:{ IOException -> 0x0065, all -> 0x005e }
        r4 = "";
    L_0x001d:
        r4 = r1.readLine();	 Catch:{ IOException -> 0x0068, all -> 0x0061 }
        if (r4 != 0) goto L_0x0030;
    L_0x0023:
        if (r1 == 0) goto L_0x0028;
    L_0x0025:
        r1.close();	 Catch:{ IOException -> 0x0057 }
    L_0x0028:
        if (r6 == 0) goto L_0x005b;
    L_0x002a:
        r6.close();	 Catch:{ IOException -> 0x0057 }
        r0 = r1;
        r5 = r6;
    L_0x002f:
        return r7;
    L_0x0030:
        r7 = r4;
        goto L_0x001d;
    L_0x0032:
        r2 = move-exception;
    L_0x0033:
        r2.printStackTrace();	 Catch:{ all -> 0x0046 }
        if (r0 == 0) goto L_0x003b;
    L_0x0038:
        r0.close();	 Catch:{ IOException -> 0x0041 }
    L_0x003b:
        if (r5 == 0) goto L_0x002f;
    L_0x003d:
        r5.close();	 Catch:{ IOException -> 0x0041 }
        goto L_0x002f;
    L_0x0041:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x002f;
    L_0x0046:
        r8 = move-exception;
    L_0x0047:
        if (r0 == 0) goto L_0x004c;
    L_0x0049:
        r0.close();	 Catch:{ IOException -> 0x0052 }
    L_0x004c:
        if (r5 == 0) goto L_0x0051;
    L_0x004e:
        r5.close();	 Catch:{ IOException -> 0x0052 }
    L_0x0051:
        throw r8;
    L_0x0052:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0051;
    L_0x0057:
        r2 = move-exception;
        r2.printStackTrace();
    L_0x005b:
        r0 = r1;
        r5 = r6;
        goto L_0x002f;
    L_0x005e:
        r8 = move-exception;
        r5 = r6;
        goto L_0x0047;
    L_0x0061:
        r8 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0047;
    L_0x0065:
        r2 = move-exception;
        r5 = r6;
        goto L_0x0033;
    L_0x0068:
        r2 = move-exception;
        r0 = r1;
        r5 = r6;
        goto L_0x0033;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yxx.jiejie.FileUtil.getFromAsset(android.content.Context, java.lang.String):java.lang.String");
    }

    public static void writeToFile(File fileName, ProcBufferedReader pbr) throws Exception {
        Exception e;
        Throwable th;
        PrintWriter pw = null;
        try {
            PrintWriter pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), HTTP.UTF_8)));
            try {
                pbr.writeToFile(pw2);
                if (pw2 != null) {
                    pw2.close();
                }
            } catch (Exception e2) {
                e = e2;
                pw = pw2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                pw = pw2;
                if (pw != null) {
                    pw.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            throw e;
        }
    }
}
