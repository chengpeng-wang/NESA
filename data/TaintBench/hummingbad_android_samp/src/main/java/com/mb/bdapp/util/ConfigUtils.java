package com.mb.bdapp.util;

import android.content.Context;

public class ConfigUtils {
    private static final String DEBUG_CONFIG_FILE = "debug.config";
    private static final String DEBUG_KEY = "debug";
    private static final String TAG = ConfigUtils.class.getSimpleName();

    public static void initConfig(Context context) {
        if (!SharedPreferencesUtils.getBoolean(context, "inited_configs", false)) {
            initDebugConfig(context);
        }
        setDebug(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0063 A:{SYNTHETIC, Splitter:B:26:0x0063} */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0051 A:{SYNTHETIC, Splitter:B:20:0x0051} */
    private static void initDebugConfig(android.content.Context r9) {
        /*
        r3 = 0;
        r6 = "debug.config";
        r2 = com.mb.bdapp.util.FileUtils.getFile(r6);	 Catch:{ Exception -> 0x0045 }
        r6 = "inited_configs";
        r7 = 1;
        com.mb.bdapp.util.SharedPreferencesUtils.putBoolean(r9, r6, r7);	 Catch:{ Exception -> 0x0045 }
        r6 = r2.exists();	 Catch:{ Exception -> 0x0045 }
        if (r6 == 0) goto L_0x0037;
    L_0x0013:
        r4 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0045 }
        r4.<init>(r2);	 Catch:{ Exception -> 0x0045 }
        r5 = new java.util.Properties;	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r5.<init>();	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r5.load(r4);	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r6 = "debug";
        r7 = "false";
        r0 = r5.getProperty(r6, r7);	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r6 = "true";
        r6 = r6.equals(r0);	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        if (r6 == 0) goto L_0x003d;
    L_0x0030:
        r6 = "debug";
        r7 = 1;
        com.mb.bdapp.util.SharedPreferencesUtils.putBoolean(r9, r6, r7);	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r3 = r4;
    L_0x0037:
        if (r3 == 0) goto L_0x003c;
    L_0x0039:
        r3.close();	 Catch:{ IOException -> 0x0072 }
    L_0x003c:
        return;
    L_0x003d:
        r6 = "debug";
        r7 = 0;
        com.mb.bdapp.util.SharedPreferencesUtils.putBoolean(r9, r6, r7);	 Catch:{ Exception -> 0x0080, all -> 0x007d }
        r3 = r4;
        goto L_0x0037;
    L_0x0045:
        r1 = move-exception;
    L_0x0046:
        r6 = TAG;	 Catch:{ all -> 0x0060 }
        r7 = r1.getLocalizedMessage();	 Catch:{ all -> 0x0060 }
        com.mb.bdapp.util.LogUtil.e(r6, r7);	 Catch:{ all -> 0x0060 }
        if (r3 == 0) goto L_0x003c;
    L_0x0051:
        r3.close();	 Catch:{ IOException -> 0x0055 }
        goto L_0x003c;
    L_0x0055:
        r1 = move-exception;
        r6 = TAG;
        r7 = r1.getLocalizedMessage();
        com.mb.bdapp.util.LogUtil.e(r6, r7);
        goto L_0x003c;
    L_0x0060:
        r6 = move-exception;
    L_0x0061:
        if (r3 == 0) goto L_0x0066;
    L_0x0063:
        r3.close();	 Catch:{ IOException -> 0x0067 }
    L_0x0066:
        throw r6;
    L_0x0067:
        r1 = move-exception;
        r7 = TAG;
        r8 = r1.getLocalizedMessage();
        com.mb.bdapp.util.LogUtil.e(r7, r8);
        goto L_0x0066;
    L_0x0072:
        r1 = move-exception;
        r6 = TAG;
        r7 = r1.getLocalizedMessage();
        com.mb.bdapp.util.LogUtil.e(r6, r7);
        goto L_0x003c;
    L_0x007d:
        r6 = move-exception;
        r3 = r4;
        goto L_0x0061;
    L_0x0080:
        r1 = move-exception;
        r3 = r4;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mb.bdapp.util.ConfigUtils.initDebugConfig(android.content.Context):void");
    }

    private static void setDebug(Context context) {
        if (SharedPreferencesUtils.getBoolean(context, DEBUG_KEY, false)) {
            LogUtil.enableLog();
        } else {
            LogUtil.disableLog();
        }
    }
}
