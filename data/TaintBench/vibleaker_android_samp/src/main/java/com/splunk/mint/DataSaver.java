package com.splunk.mint;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataSaver extends BaseExecutor implements InterfaceExecutor {
    private static final String LAST_SAVED_NAME = "/Mint-lastsavedfile";
    private static final int MAX_FILE_SIZE = 140000;

    DataSaver() {
    }

    public synchronized void save(final String jsonData) {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:38:0x00c6 A:{Catch:{ all -> 0x00f0 }} */
            /* JADX WARNING: Removed duplicated region for block: B:40:0x00cd A:{SYNTHETIC, Splitter:B:40:0x00cd} */
            /* JADX WARNING: Removed duplicated region for block: B:44:0x00db  */
            /* JADX WARNING: Removed duplicated region for block: B:68:? A:{SYNTHETIC, RETURN} */
            /* JADX WARNING: Removed duplicated region for block: B:46:0x00e2  */
            /* JADX WARNING: Removed duplicated region for block: B:51:0x00f3 A:{SYNTHETIC, Splitter:B:51:0x00f3} */
            /* JADX WARNING: Removed duplicated region for block: B:55:0x0101  */
            /* JADX WARNING: Removed duplicated region for block: B:57:0x0108  */
            public void run() {
                /*
                r13 = this;
                r12 = 1;
                r7 = com.splunk.mint.Properties.USER_OPTEDOUT;
                if (r7 != 0) goto L_0x0088;
            L_0x0005:
                r4 = 0;
                r7 = com.splunk.mint.DataSaver.this;
                r6 = r7.getLastSavedName();
                if (r6 == 0) goto L_0x0014;
            L_0x000e:
                r7 = r6.length();
                if (r7 != 0) goto L_0x0089;
            L_0x0014:
                r4 = new java.io.File;
                r7 = com.splunk.mint.SplunkFileFilter.createNewFile();
                r4.<init>(r7);
            L_0x001d:
                r5 = 0;
                r8 = r4.length();
                r10 = 140000; // 0x222e0 float:1.96182E-40 double:6.9169E-319;
                r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
                if (r7 < 0) goto L_0x0033;
            L_0x0029:
                r4 = new java.io.File;
                r7 = com.splunk.mint.SplunkFileFilter.createNewFile();
                r4.<init>(r7);
                r5 = 1;
            L_0x0033:
                r2 = new com.splunk.mint.DataSaverResponse;
                r7 = r4;
                r8 = r4.getAbsolutePath();
                r2.m3608init(r7, r8);
                if (r4 == 0) goto L_0x0049;
            L_0x0040:
                r7 = r4.exists();
                if (r7 != 0) goto L_0x0049;
            L_0x0046:
                r4.createNewFile();	 Catch:{ IOException -> 0x00a9 }
            L_0x0049:
                r0 = 0;
                r1 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x00b3 }
                r7 = new java.io.FileWriter;	 Catch:{ IOException -> 0x00b3 }
                r8 = 1;
                r7.<init>(r4, r8);	 Catch:{ IOException -> 0x00b3 }
                r1.<init>(r7);	 Catch:{ IOException -> 0x00b3 }
                r7 = r4;	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r1.append(r7);	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r1.flush();	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r1.close();	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r7 = com.splunk.mint.DataSaver.this;	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r8 = r4.getName();	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                r7.saveLastSavedName(r8);	 Catch:{ IOException -> 0x0119, all -> 0x0116 }
                if (r1 == 0) goto L_0x006e;
            L_0x006b:
                r1.close();	 Catch:{ IOException -> 0x00ae }
            L_0x006e:
                r7 = java.lang.Boolean.valueOf(r12);
                r2.setSavedSuccessfully(r7);
                r7 = com.splunk.mint.Mint.mintCallback;
                if (r7 == 0) goto L_0x007e;
            L_0x0079:
                r7 = com.splunk.mint.Mint.mintCallback;
                r7.dataSaverResponse(r2);
            L_0x007e:
                if (r5 == 0) goto L_0x0088;
            L_0x0080:
                r7 = new com.splunk.mint.DataFlusher;
                r7.m3605init();
                r7.send();
            L_0x0088:
                return;
            L_0x0089:
                r4 = new java.io.File;
                r7 = new java.lang.StringBuilder;
                r7.<init>();
                r8 = com.splunk.mint.Properties.FILES_PATH;
                r7 = r7.append(r8);
                r8 = "/";
                r7 = r7.append(r8);
                r7 = r7.append(r6);
                r7 = r7.toString();
                r4.<init>(r7);
                goto L_0x001d;
            L_0x00a9:
                r3 = move-exception;
                r3.printStackTrace();
                goto L_0x0049;
            L_0x00ae:
                r3 = move-exception;
                r3.printStackTrace();
                goto L_0x006e;
            L_0x00b3:
                r3 = move-exception;
            L_0x00b4:
                r3.printStackTrace();	 Catch:{ all -> 0x00f0 }
                r2.setException(r3);	 Catch:{ all -> 0x00f0 }
                r7 = 0;
                r7 = java.lang.Boolean.valueOf(r7);	 Catch:{ all -> 0x00f0 }
                r2.setSavedSuccessfully(r7);	 Catch:{ all -> 0x00f0 }
                r7 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x00f0 }
                if (r7 == 0) goto L_0x00cb;
            L_0x00c6:
                r7 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x00f0 }
                r7.dataSaverResponse(r2);	 Catch:{ all -> 0x00f0 }
            L_0x00cb:
                if (r0 == 0) goto L_0x00d0;
            L_0x00cd:
                r0.close();	 Catch:{ IOException -> 0x00eb }
            L_0x00d0:
                r7 = java.lang.Boolean.valueOf(r12);
                r2.setSavedSuccessfully(r7);
                r7 = com.splunk.mint.Mint.mintCallback;
                if (r7 == 0) goto L_0x00e0;
            L_0x00db:
                r7 = com.splunk.mint.Mint.mintCallback;
                r7.dataSaverResponse(r2);
            L_0x00e0:
                if (r5 == 0) goto L_0x0088;
            L_0x00e2:
                r7 = new com.splunk.mint.DataFlusher;
                r7.m3605init();
                r7.send();
                goto L_0x0088;
            L_0x00eb:
                r3 = move-exception;
                r3.printStackTrace();
                goto L_0x00d0;
            L_0x00f0:
                r7 = move-exception;
            L_0x00f1:
                if (r0 == 0) goto L_0x00f6;
            L_0x00f3:
                r0.close();	 Catch:{ IOException -> 0x0111 }
            L_0x00f6:
                r8 = java.lang.Boolean.valueOf(r12);
                r2.setSavedSuccessfully(r8);
                r8 = com.splunk.mint.Mint.mintCallback;
                if (r8 == 0) goto L_0x0106;
            L_0x0101:
                r8 = com.splunk.mint.Mint.mintCallback;
                r8.dataSaverResponse(r2);
            L_0x0106:
                if (r5 == 0) goto L_0x0110;
            L_0x0108:
                r8 = new com.splunk.mint.DataFlusher;
                r8.m3605init();
                r8.send();
            L_0x0110:
                throw r7;
            L_0x0111:
                r3 = move-exception;
                r3.printStackTrace();
                goto L_0x00f6;
            L_0x0116:
                r7 = move-exception;
                r0 = r1;
                goto L_0x00f1;
            L_0x0119:
                r3 = move-exception;
                r0 = r1;
                goto L_0x00b4;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.DataSaver$AnonymousClass1.run():void");
            }
        });
        if (getExecutor() != null) {
            getExecutor().execute(t);
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized String getLastSavedName() {
        String trim;
        try {
            trim = Utils.readFile(new File(Properties.FILES_PATH + LAST_SAVED_NAME).getAbsolutePath()).trim();
        } catch (Exception e) {
            trim = null;
        }
        return trim;
    }

    /* access modifiers changed from: private|declared_synchronized */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0061 A:{Catch:{ all -> 0x006f }} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0066 A:{SYNTHETIC, Splitter:B:35:0x0066} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0072 A:{SYNTHETIC, Splitter:B:42:0x0072} */
    public synchronized void saveLastSavedName(java.lang.String r7) {
        /*
        r6 = this;
        monitor-enter(r6);
        r3 = new java.io.File;	 Catch:{ all -> 0x004e }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004e }
        r4.<init>();	 Catch:{ all -> 0x004e }
        r5 = com.splunk.mint.Properties.FILES_PATH;	 Catch:{ all -> 0x004e }
        r4 = r4.append(r5);	 Catch:{ all -> 0x004e }
        r5 = "/Mint-lastsavedfile";
        r4 = r4.append(r5);	 Catch:{ all -> 0x004e }
        r4 = r4.toString();	 Catch:{ all -> 0x004e }
        r3.<init>(r4);	 Catch:{ all -> 0x004e }
        if (r3 == 0) goto L_0x0029;
    L_0x001d:
        r4 = r3.exists();	 Catch:{ all -> 0x004e }
        if (r4 != 0) goto L_0x0029;
    L_0x0023:
        r3.delete();	 Catch:{ IOException -> 0x0049 }
        r3.createNewFile();	 Catch:{ IOException -> 0x0049 }
    L_0x0029:
        r0 = 0;
        r1 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x0057 }
        r4 = new java.io.FileWriter;	 Catch:{ IOException -> 0x0057 }
        r4.<init>(r3);	 Catch:{ IOException -> 0x0057 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x0057 }
        r4 = r7.trim();	 Catch:{ IOException -> 0x007e, all -> 0x007b }
        r1.write(r4);	 Catch:{ IOException -> 0x007e, all -> 0x007b }
        r1.flush();	 Catch:{ IOException -> 0x007e, all -> 0x007b }
        r1.close();	 Catch:{ IOException -> 0x007e, all -> 0x007b }
        if (r1 == 0) goto L_0x0081;
    L_0x0043:
        r1.close();	 Catch:{ IOException -> 0x0051 }
        r0 = r1;
    L_0x0047:
        monitor-exit(r6);
        return;
    L_0x0049:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x004e }
        goto L_0x0029;
    L_0x004e:
        r4 = move-exception;
        monitor-exit(r6);
        throw r4;
    L_0x0051:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x004e }
        r0 = r1;
        goto L_0x0047;
    L_0x0057:
        r2 = move-exception;
    L_0x0058:
        r4 = "There was a problem saving the last saved file name";
        com.splunk.mint.Logger.logWarning(r4);	 Catch:{ all -> 0x006f }
        r4 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x006f }
        if (r4 == 0) goto L_0x0064;
    L_0x0061:
        r2.printStackTrace();	 Catch:{ all -> 0x006f }
    L_0x0064:
        if (r0 == 0) goto L_0x0047;
    L_0x0066:
        r0.close();	 Catch:{ IOException -> 0x006a }
        goto L_0x0047;
    L_0x006a:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x004e }
        goto L_0x0047;
    L_0x006f:
        r4 = move-exception;
    L_0x0070:
        if (r0 == 0) goto L_0x0075;
    L_0x0072:
        r0.close();	 Catch:{ IOException -> 0x0076 }
    L_0x0075:
        throw r4;	 Catch:{ all -> 0x004e }
    L_0x0076:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x004e }
        goto L_0x0075;
    L_0x007b:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0070;
    L_0x007e:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0058;
    L_0x0081:
        r0 = r1;
        goto L_0x0047;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.DataSaver.saveLastSavedName(java.lang.String):void");
    }

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(1);
        }
        return executor;
    }
}
