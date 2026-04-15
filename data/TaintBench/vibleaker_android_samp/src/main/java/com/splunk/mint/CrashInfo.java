package com.splunk.mint;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CrashInfo extends BaseExecutor implements InterfaceExecutor {
    private static final String crashCounterFile = "crashCounter";
    private static final String lastCrashIDFile = "lastCrashID";

    CrashInfo() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x0098 A:{Splitter:B:14:0x004e, ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x008f A:{SYNTHETIC, Splitter:B:39:0x008f} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0098 A:{Splitter:B:14:0x004e, ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:26:?, code skipped:
            r2 = java.lang.Integer.valueOf(0);
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:42:0x0093, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:43:0x0094, code skipped:
            r3.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:44:0x0098, code skipped:
            r5 = th;
     */
    /* JADX WARNING: Missing block: B:45:0x0099, code skipped:
            r0 = r1;
     */
    protected static int getTotalCrashesNum() {
        /*
        r5 = 0;
        r2 = java.lang.Integer.valueOf(r5);
        r6 = com.splunk.mint.Properties.FILES_PATH;
        if (r6 != 0) goto L_0x000f;
    L_0x0009:
        r6 = "Please use getTotalCrashesNum after initializing the plugin! Returning 0.";
        com.splunk.mint.Logger.logWarning(r6);
    L_0x000e:
        return r5;
    L_0x000f:
        r4 = new java.io.File;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = com.splunk.mint.Properties.FILES_PATH;
        r6 = r6.append(r7);
        r7 = "/";
        r6 = r6.append(r7);
        r7 = "crashCounter";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r4.<init>(r6);
        if (r4 == 0) goto L_0x0043;
    L_0x0031:
        r6 = r4.exists();
        if (r6 != 0) goto L_0x0043;
    L_0x0037:
        r4.createNewFile();	 Catch:{ IOException -> 0x003f }
        r5 = r2.intValue();	 Catch:{ IOException -> 0x003f }
        goto L_0x000e;
    L_0x003f:
        r3 = move-exception;
        r3.printStackTrace();
    L_0x0043:
        r0 = 0;
        r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0074 }
        r6 = new java.io.FileReader;	 Catch:{ Exception -> 0x0074 }
        r6.<init>(r4);	 Catch:{ Exception -> 0x0074 }
        r1.<init>(r6);	 Catch:{ Exception -> 0x0074 }
        r6 = r1.readLine();	 Catch:{ Exception -> 0x006d, all -> 0x0098 }
        r6 = r6.trim();	 Catch:{ Exception -> 0x006d, all -> 0x0098 }
        r6 = java.lang.Integer.parseInt(r6);	 Catch:{ Exception -> 0x006d, all -> 0x0098 }
        r2 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x006d, all -> 0x0098 }
    L_0x005e:
        r5 = r2.intValue();	 Catch:{ Exception -> 0x009b, all -> 0x0098 }
        if (r1 == 0) goto L_0x000e;
    L_0x0064:
        r1.close();	 Catch:{ IOException -> 0x0068 }
        goto L_0x000e;
    L_0x0068:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x000e;
    L_0x006d:
        r3 = move-exception;
        r6 = 0;
        r2 = java.lang.Integer.valueOf(r6);	 Catch:{ Exception -> 0x009b, all -> 0x0098 }
        goto L_0x005e;
    L_0x0074:
        r3 = move-exception;
    L_0x0075:
        r6 = "There was a problem getting the crash counter";
        com.splunk.mint.Logger.logWarning(r6);	 Catch:{ all -> 0x008c }
        r6 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x008c }
        if (r6 == 0) goto L_0x0081;
    L_0x007e:
        r3.printStackTrace();	 Catch:{ all -> 0x008c }
    L_0x0081:
        if (r0 == 0) goto L_0x000e;
    L_0x0083:
        r0.close();	 Catch:{ IOException -> 0x0087 }
        goto L_0x000e;
    L_0x0087:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x000e;
    L_0x008c:
        r5 = move-exception;
    L_0x008d:
        if (r0 == 0) goto L_0x0092;
    L_0x008f:
        r0.close();	 Catch:{ IOException -> 0x0093 }
    L_0x0092:
        throw r5;
    L_0x0093:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0092;
    L_0x0098:
        r5 = move-exception;
        r0 = r1;
        goto L_0x008d;
    L_0x009b:
        r3 = move-exception;
        r0 = r1;
        goto L_0x0075;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.CrashInfo.getTotalCrashesNum():int");
    }

    /* access modifiers changed from: protected */
    public void clearCrashCounter() {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                File file = new File(Properties.FILES_PATH + "/" + CrashInfo.crashCounterFile);
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
        });
        ExecutorService executor = getExecutor();
        if (t != null && executor != null) {
            executor.submit(t);
        }
    }

    /* access modifiers changed from: protected */
    public void saveCrashCounter() {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:52:0x00ba A:{SYNTHETIC, Splitter:B:52:0x00ba} */
            /* JADX WARNING: Removed duplicated region for block: B:55:0x00bf A:{SYNTHETIC, Splitter:B:55:0x00bf} */
            /* JADX WARNING: Removed duplicated region for block: B:39:0x009f A:{Catch:{ all -> 0x00b7 }} */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x00a4 A:{SYNTHETIC, Splitter:B:41:0x00a4} */
            /* JADX WARNING: Removed duplicated region for block: B:73:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:44:0x00a9 A:{SYNTHETIC, Splitter:B:44:0x00a9} */
            /* JADX WARNING: Removed duplicated region for block: B:52:0x00ba A:{SYNTHETIC, Splitter:B:52:0x00ba} */
            /* JADX WARNING: Removed duplicated region for block: B:55:0x00bf A:{SYNTHETIC, Splitter:B:55:0x00bf} */
            public void run() {
                /*
                r9 = this;
                r6 = new java.io.File;
                r7 = new java.lang.StringBuilder;
                r7.<init>();
                r8 = com.splunk.mint.Properties.FILES_PATH;
                r7 = r7.append(r8);
                r8 = "/";
                r7 = r7.append(r8);
                r8 = "crashCounter";
                r7 = r7.append(r8);
                r7 = r7.toString();
                r6.<init>(r7);
                if (r6 == 0) goto L_0x002b;
            L_0x0022:
                r7 = r6.exists();
                if (r7 != 0) goto L_0x002b;
            L_0x0028:
                r6.createNewFile();	 Catch:{ IOException -> 0x007d }
            L_0x002b:
                r0 = 0;
                r2 = 0;
                r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0095 }
                r7 = new java.io.FileReader;	 Catch:{ IOException -> 0x0095 }
                r7.<init>(r6);	 Catch:{ IOException -> 0x0095 }
                r1.<init>(r7);	 Catch:{ IOException -> 0x0095 }
                r7 = 0;
                r4 = java.lang.Integer.valueOf(r7);	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r7 = r1.readLine();	 Catch:{ Exception -> 0x0082 }
                r7 = r7.trim();	 Catch:{ Exception -> 0x0082 }
                r7 = java.lang.Integer.parseInt(r7);	 Catch:{ Exception -> 0x0082 }
                r4 = java.lang.Integer.valueOf(r7);	 Catch:{ Exception -> 0x0082 }
            L_0x004c:
                r7 = r4.intValue();	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r7 = r7 + 1;
                r4 = java.lang.Integer.valueOf(r7);	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r3 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r7 = new java.io.FileWriter;	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r7.<init>(r6);	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r3.<init>(r7);	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                r7 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x00d7, all -> 0x00d0 }
                r3.write(r7);	 Catch:{ IOException -> 0x00d7, all -> 0x00d0 }
                r3.newLine();	 Catch:{ IOException -> 0x00d7, all -> 0x00d0 }
                r3.flush();	 Catch:{ IOException -> 0x00d7, all -> 0x00d0 }
                r3.close();	 Catch:{ IOException -> 0x00d7, all -> 0x00d0 }
                if (r1 == 0) goto L_0x0075;
            L_0x0072:
                r1.close();	 Catch:{ IOException -> 0x0089 }
            L_0x0075:
                if (r3 == 0) goto L_0x00db;
            L_0x0077:
                r3.close();	 Catch:{ IOException -> 0x008e }
                r2 = r3;
                r0 = r1;
            L_0x007c:
                return;
            L_0x007d:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x002b;
            L_0x0082:
                r5 = move-exception;
                r7 = 0;
                r4 = java.lang.Integer.valueOf(r7);	 Catch:{ IOException -> 0x00d4, all -> 0x00cd }
                goto L_0x004c;
            L_0x0089:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x0075;
            L_0x008e:
                r5 = move-exception;
                r5.printStackTrace();
                r2 = r3;
                r0 = r1;
                goto L_0x007c;
            L_0x0095:
                r5 = move-exception;
            L_0x0096:
                r7 = "There was a problem saving the crash counter";
                com.splunk.mint.Logger.logWarning(r7);	 Catch:{ all -> 0x00b7 }
                r7 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x00b7 }
                if (r7 == 0) goto L_0x00a2;
            L_0x009f:
                r5.printStackTrace();	 Catch:{ all -> 0x00b7 }
            L_0x00a2:
                if (r0 == 0) goto L_0x00a7;
            L_0x00a4:
                r0.close();	 Catch:{ IOException -> 0x00b2 }
            L_0x00a7:
                if (r2 == 0) goto L_0x007c;
            L_0x00a9:
                r2.close();	 Catch:{ IOException -> 0x00ad }
                goto L_0x007c;
            L_0x00ad:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x007c;
            L_0x00b2:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x00a7;
            L_0x00b7:
                r7 = move-exception;
            L_0x00b8:
                if (r0 == 0) goto L_0x00bd;
            L_0x00ba:
                r0.close();	 Catch:{ IOException -> 0x00c3 }
            L_0x00bd:
                if (r2 == 0) goto L_0x00c2;
            L_0x00bf:
                r2.close();	 Catch:{ IOException -> 0x00c8 }
            L_0x00c2:
                throw r7;
            L_0x00c3:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x00bd;
            L_0x00c8:
                r5 = move-exception;
                r5.printStackTrace();
                goto L_0x00c2;
            L_0x00cd:
                r7 = move-exception;
                r0 = r1;
                goto L_0x00b8;
            L_0x00d0:
                r7 = move-exception;
                r2 = r3;
                r0 = r1;
                goto L_0x00b8;
            L_0x00d4:
                r5 = move-exception;
                r0 = r1;
                goto L_0x0096;
            L_0x00d7:
                r5 = move-exception;
                r2 = r3;
                r0 = r1;
                goto L_0x0096;
            L_0x00db:
                r2 = r3;
                r0 = r1;
                goto L_0x007c;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.CrashInfo$AnonymousClass2.run():void");
            }
        });
        ExecutorService executor = getExecutor();
        if (t != null && executor != null) {
            executor.submit(t);
        }
    }

    /* access modifiers changed from: protected */
    public void saveLastCrashID(final String lastID) {
        if (lastID != null) {
            Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:23:0x0060 A:{Catch:{ all -> 0x006e }} */
                /* JADX WARNING: Removed duplicated region for block: B:43:? A:{SYNTHETIC, RETURN} */
                /* JADX WARNING: Removed duplicated region for block: B:25:0x0065 A:{SYNTHETIC, Splitter:B:25:0x0065} */
                /* JADX WARNING: Removed duplicated region for block: B:31:0x0071 A:{SYNTHETIC, Splitter:B:31:0x0071} */
                public void run() {
                    /*
                    r6 = this;
                    r3 = new java.io.File;
                    r4 = new java.lang.StringBuilder;
                    r4.<init>();
                    r5 = com.splunk.mint.Properties.FILES_PATH;
                    r4 = r4.append(r5);
                    r5 = "/";
                    r4 = r4.append(r5);
                    r5 = "lastCrashID";
                    r4 = r4.append(r5);
                    r4 = r4.toString();
                    r3.<init>(r4);
                    if (r3 == 0) goto L_0x002b;
                L_0x0022:
                    r4 = r3.exists();
                    if (r4 != 0) goto L_0x002b;
                L_0x0028:
                    r3.createNewFile();	 Catch:{ IOException -> 0x004b }
                L_0x002b:
                    r0 = 0;
                    r1 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x0056 }
                    r4 = new java.io.FileWriter;	 Catch:{ IOException -> 0x0056 }
                    r4.<init>(r3);	 Catch:{ IOException -> 0x0056 }
                    r1.<init>(r4);	 Catch:{ IOException -> 0x0056 }
                    r4 = r5;	 Catch:{ IOException -> 0x007d, all -> 0x007a }
                    r1.write(r4);	 Catch:{ IOException -> 0x007d, all -> 0x007a }
                    r1.newLine();	 Catch:{ IOException -> 0x007d, all -> 0x007a }
                    r1.flush();	 Catch:{ IOException -> 0x007d, all -> 0x007a }
                    r1.close();	 Catch:{ IOException -> 0x007d, all -> 0x007a }
                    if (r1 == 0) goto L_0x0080;
                L_0x0046:
                    r1.close();	 Catch:{ IOException -> 0x0050 }
                    r0 = r1;
                L_0x004a:
                    return;
                L_0x004b:
                    r2 = move-exception;
                    r2.printStackTrace();
                    goto L_0x002b;
                L_0x0050:
                    r2 = move-exception;
                    r2.printStackTrace();
                    r0 = r1;
                    goto L_0x004a;
                L_0x0056:
                    r2 = move-exception;
                L_0x0057:
                    r4 = "There was a problem saving the last crash id";
                    com.splunk.mint.Logger.logWarning(r4);	 Catch:{ all -> 0x006e }
                    r4 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x006e }
                    if (r4 == 0) goto L_0x0063;
                L_0x0060:
                    r2.printStackTrace();	 Catch:{ all -> 0x006e }
                L_0x0063:
                    if (r0 == 0) goto L_0x004a;
                L_0x0065:
                    r0.close();	 Catch:{ IOException -> 0x0069 }
                    goto L_0x004a;
                L_0x0069:
                    r2 = move-exception;
                    r2.printStackTrace();
                    goto L_0x004a;
                L_0x006e:
                    r4 = move-exception;
                L_0x006f:
                    if (r0 == 0) goto L_0x0074;
                L_0x0071:
                    r0.close();	 Catch:{ IOException -> 0x0075 }
                L_0x0074:
                    throw r4;
                L_0x0075:
                    r2 = move-exception;
                    r2.printStackTrace();
                    goto L_0x0074;
                L_0x007a:
                    r4 = move-exception;
                    r0 = r1;
                    goto L_0x006f;
                L_0x007d:
                    r2 = move-exception;
                    r0 = r1;
                    goto L_0x0057;
                L_0x0080:
                    r0 = r1;
                    goto L_0x004a;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.CrashInfo$AnonymousClass3.run():void");
                }
            });
            ExecutorService executor = getExecutor();
            if (t != null && executor != null) {
                executor.submit(t);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x0073 A:{SYNTHETIC, Splitter:B:36:0x0073} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x007c A:{Splitter:B:12:0x003e, ExcHandler: all (th java.lang.Throwable)} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0061 A:{Catch:{ all -> 0x0070 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0066 A:{SYNTHETIC, Splitter:B:29:0x0066} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:19:0x0051, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:21:?, code skipped:
            r3.printStackTrace();
     */
    /* JADX WARNING: Missing block: B:22:0x0055, code skipped:
            r2 = null;
     */
    /* JADX WARNING: Missing block: B:41:0x007c, code skipped:
            r5 = th;
     */
    /* JADX WARNING: Missing block: B:42:0x007d, code skipped:
            r0 = r1;
     */
    protected static java.lang.String getLastCrashID() {
        /*
        r5 = 0;
        r2 = 0;
        r4 = new java.io.File;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = com.splunk.mint.Properties.FILES_PATH;
        r6 = r6.append(r7);
        r7 = "/";
        r6 = r6.append(r7);
        r7 = "lastCrashID";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r4.<init>(r6);
        if (r4 == 0) goto L_0x0033;
    L_0x0024:
        r6 = r4.exists();
        if (r6 != 0) goto L_0x0033;
    L_0x002a:
        r4.createNewFile();	 Catch:{ IOException -> 0x002f }
        r2 = r5;
    L_0x002e:
        return r2;
    L_0x002f:
        r3 = move-exception;
        r3.printStackTrace();
    L_0x0033:
        r0 = 0;
        r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0057 }
        r6 = new java.io.FileReader;	 Catch:{ Exception -> 0x0057 }
        r6.<init>(r4);	 Catch:{ Exception -> 0x0057 }
        r1.<init>(r6);	 Catch:{ Exception -> 0x0057 }
        r6 = r1.readLine();	 Catch:{ Exception -> 0x0051, all -> 0x007c }
        r2 = r6.trim();	 Catch:{ Exception -> 0x0051, all -> 0x007c }
    L_0x0046:
        if (r1 == 0) goto L_0x002e;
    L_0x0048:
        r1.close();	 Catch:{ IOException -> 0x004c }
        goto L_0x002e;
    L_0x004c:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x002e;
    L_0x0051:
        r3 = move-exception;
        r3.printStackTrace();	 Catch:{ Exception -> 0x007f, all -> 0x007c }
        r2 = 0;
        goto L_0x0046;
    L_0x0057:
        r3 = move-exception;
    L_0x0058:
        r6 = "There was a problem getting the last crash id";
        com.splunk.mint.Logger.logWarning(r6);	 Catch:{ all -> 0x0070 }
        r6 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x0070 }
        if (r6 == 0) goto L_0x0064;
    L_0x0061:
        r3.printStackTrace();	 Catch:{ all -> 0x0070 }
    L_0x0064:
        if (r0 == 0) goto L_0x0069;
    L_0x0066:
        r0.close();	 Catch:{ IOException -> 0x006b }
    L_0x0069:
        r2 = r5;
        goto L_0x002e;
    L_0x006b:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0069;
    L_0x0070:
        r5 = move-exception;
    L_0x0071:
        if (r0 == 0) goto L_0x0076;
    L_0x0073:
        r0.close();	 Catch:{ IOException -> 0x0077 }
    L_0x0076:
        throw r5;
    L_0x0077:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x0076;
    L_0x007c:
        r5 = move-exception;
        r0 = r1;
        goto L_0x0071;
    L_0x007f:
        r3 = move-exception;
        r0 = r1;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.CrashInfo.getLastCrashID():java.lang.String");
    }

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(1);
        }
        return executor;
    }
}
