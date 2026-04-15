package com.splunk.mint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

class NetSender extends BaseExecutor implements InterfaceExecutor {
    NetSender() {
    }

    public synchronized void send(final String data, final boolean saveOnFail) {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                NetSender.this.sendBlocking(data, saveOnFail);
            }
        });
        if (getExecutor() != null) {
            getExecutor().execute(t);
        }
    }

    public synchronized NetSenderResponse sendBlocking(String data, boolean saveOnFail) {
        return sendBlocking(null, data, saveOnFail);
    }

    /* JADX WARNING: Removed duplicated region for block: B:54:0x016a A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x016f A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0183 A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018c A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x019f A:{SYNTHETIC, Splitter:B:63:0x019f} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01a4 A:{SYNTHETIC, Splitter:B:66:0x01a4} */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01ad A:{SYNTHETIC, Splitter:B:71:0x01ad} */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01b2 A:{SYNTHETIC, Splitter:B:74:0x01b2} */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01ad A:{SYNTHETIC, Splitter:B:71:0x01ad} */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01b2 A:{SYNTHETIC, Splitter:B:74:0x01b2} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x016a A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x016f A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0183 A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x018c A:{Catch:{ all -> 0x01aa }} */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x019f A:{SYNTHETIC, Splitter:B:63:0x019f} */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01a4 A:{SYNTHETIC, Splitter:B:66:0x01a4} */
    public synchronized com.splunk.mint.NetSenderResponse sendBlocking(java.lang.String r24, java.lang.String r25, boolean r26) {
        /*
        r23 = this;
        monitor-enter(r23);
        r21 = com.splunk.mint.Properties.USER_OPTEDOUT;	 Catch:{ all -> 0x01b6 }
        if (r21 != 0) goto L_0x01b9;
    L_0x0005:
        r13 = new com.splunk.mint.NetSenderResponse;	 Catch:{ all -> 0x01b6 }
        r0 = r24;
        r1 = r25;
        r13.m3646init(r0, r1);	 Catch:{ all -> 0x01b6 }
        if (r25 != 0) goto L_0x0031;
    L_0x0010:
        r21 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x01b6 }
        r22 = "null data!";
        r21.<init>(r22);	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r13.setException(r0);	 Catch:{ all -> 0x01b6 }
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01b6 }
        if (r21 == 0) goto L_0x0027;
    L_0x0020:
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r0.netSenderResponse(r13);	 Catch:{ all -> 0x01b6 }
    L_0x0027:
        r21 = r13.toString();	 Catch:{ all -> 0x01b6 }
        com.splunk.mint.Logger.logInfo(r21);	 Catch:{ all -> 0x01b6 }
        r14 = r13;
    L_0x002f:
        monitor-exit(r23);
        return r14;
    L_0x0031:
        if (r24 != 0) goto L_0x0053;
    L_0x0033:
        r17 = 0;
        r16 = 0;
        r0 = r23;
        r1 = r25;
        r15 = r0.findAllActions(r1);	 Catch:{ all -> 0x01b6 }
        if (r15 <= 0) goto L_0x004b;
    L_0x0041:
        r0 = r23;
        r1 = r25;
        r17 = r0.findAllErrors(r1);	 Catch:{ all -> 0x01b6 }
        r16 = r15 - r17;
    L_0x004b:
        r0 = r17;
        r1 = r16;
        r24 = com.splunk.mint.MintUrls.getURL(r0, r1);	 Catch:{ all -> 0x01b6 }
    L_0x0053:
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01b6 }
        r21.<init>();	 Catch:{ all -> 0x01b6 }
        r22 = "NetSender: Sending data to url: ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r1 = r24;
        r21 = r0.append(r1);	 Catch:{ all -> 0x01b6 }
        r21 = r21.toString();	 Catch:{ all -> 0x01b6 }
        com.splunk.mint.Logger.logInfo(r21);	 Catch:{ all -> 0x01b6 }
        r9 = new org.apache.http.impl.client.DefaultHttpClient;	 Catch:{ all -> 0x01b6 }
        r9.<init>();	 Catch:{ all -> 0x01b6 }
        r18 = r9.getParams();	 Catch:{ all -> 0x01b6 }
        r21 = 0;
        r0 = r18;
        r1 = r21;
        org.apache.http.params.HttpProtocolParams.setUseExpectContinue(r0, r1);	 Catch:{ all -> 0x01b6 }
        r21 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r0 = r18;
        r1 = r21;
        org.apache.http.params.HttpConnectionParams.setConnectionTimeout(r0, r1);	 Catch:{ all -> 0x01b6 }
        r21 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r0 = r18;
        r1 = r21;
        org.apache.http.params.HttpConnectionParams.setSoTimeout(r0, r1);	 Catch:{ all -> 0x01b6 }
        r10 = new org.apache.http.client.methods.HttpPost;	 Catch:{ all -> 0x01b6 }
        r0 = r24;
        r10.<init>(r0);	 Catch:{ all -> 0x01b6 }
        r21 = "Content-Type";
        r22 = "application/x-gzip";
        r0 = r21;
        r1 = r22;
        r10.setHeader(r0, r1);	 Catch:{ all -> 0x01b6 }
        r19 = 0;
        r2 = 0;
        r7 = 0;
        r3 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x01f3 }
        r3.<init>();	 Catch:{ Exception -> 0x01f3 }
        r8 = new com.splunk.mint.NetSender$2;	 Catch:{ Exception -> 0x01f6, all -> 0x01ec }
        r0 = r23;
        r8.m3644init(r3);	 Catch:{ Exception -> 0x01f6, all -> 0x01ec }
        r21 = r25.getBytes();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r8.write(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r8.close();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r4 = new org.apache.http.entity.ByteArrayEntity;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21 = r3.toByteArray();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r4.<init>(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r10.setEntity(r4);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r19 = r9.execute(r10);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r6 = r19.getEntity();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21 = r19.getStatusLine();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r20 = r21.getStatusCode();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r20;
        r13.setResponseCode(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        if (r6 != 0) goto L_0x0121;
    L_0x00e4:
        r21 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        r0 = r20;
        r1 = r21;
        if (r0 < r1) goto L_0x0121;
    L_0x00ec:
        r21 = new java.lang.Exception;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r22 = r19.getStatusLine();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r22 = r22.getReasonPhrase();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21.<init>(r22);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r13.setException(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        if (r21 == 0) goto L_0x0109;
    L_0x0102:
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r0.netSenderResponse(r13);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
    L_0x0109:
        if (r3 == 0) goto L_0x010e;
    L_0x010b:
        r3.close();	 Catch:{ IOException -> 0x01de }
    L_0x010e:
        if (r8 == 0) goto L_0x0113;
    L_0x0110:
        r8.close();	 Catch:{ IOException -> 0x01e1 }
    L_0x0113:
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01b6 }
        if (r21 == 0) goto L_0x011e;
    L_0x0117:
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r0.netSenderResponse(r13);	 Catch:{ all -> 0x01b6 }
    L_0x011e:
        r14 = r13;
        goto L_0x002f;
    L_0x0121:
        if (r6 == 0) goto L_0x013d;
    L_0x0123:
        r11 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r22 = r6.getContent();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r21.<init>(r22);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r11.<init>(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r12 = r11.readLine();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r11.close();	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r13.setServerResponse(r12);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
    L_0x013d:
        r21 = 1;
        r21 = java.lang.Boolean.valueOf(r21);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        r0 = r21;
        r13.setSentSuccessfully(r0);	 Catch:{ Exception -> 0x0149, all -> 0x01ef }
        goto L_0x0109;
    L_0x0149:
        r5 = move-exception;
        r7 = r8;
        r2 = r3;
    L_0x014c:
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01aa }
        r21.<init>();	 Catch:{ all -> 0x01aa }
        r22 = "NetSender: Transmitting Exception ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x01aa }
        r22 = r5.getMessage();	 Catch:{ all -> 0x01aa }
        r21 = r21.append(r22);	 Catch:{ all -> 0x01aa }
        r21 = r21.toString();	 Catch:{ all -> 0x01aa }
        com.splunk.mint.Logger.logError(r21);	 Catch:{ all -> 0x01aa }
        r21 = com.splunk.mint.Mint.DEBUG;	 Catch:{ all -> 0x01aa }
        if (r21 == 0) goto L_0x016d;
    L_0x016a:
        r5.printStackTrace();	 Catch:{ all -> 0x01aa }
    L_0x016d:
        if (r19 == 0) goto L_0x017c;
    L_0x016f:
        r21 = r19.getStatusLine();	 Catch:{ all -> 0x01aa }
        r21 = r21.getStatusCode();	 Catch:{ all -> 0x01aa }
        r0 = r21;
        r13.setResponseCode(r0);	 Catch:{ all -> 0x01aa }
    L_0x017c:
        r13.setException(r5);	 Catch:{ all -> 0x01aa }
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01aa }
        if (r21 == 0) goto L_0x018a;
    L_0x0183:
        r21 = com.splunk.mint.Mint.mintCallback;	 Catch:{ all -> 0x01aa }
        r0 = r21;
        r0.netSenderResponse(r13);	 Catch:{ all -> 0x01aa }
    L_0x018a:
        if (r26 == 0) goto L_0x019d;
    L_0x018c:
        r21 = "NetSender: Couldn't send data, saving...";
        com.splunk.mint.Logger.logWarning(r21);	 Catch:{ all -> 0x01aa }
        r21 = new com.splunk.mint.DataSaver;	 Catch:{ all -> 0x01aa }
        r21.m3607init();	 Catch:{ all -> 0x01aa }
        r0 = r21;
        r1 = r25;
        r0.save(r1);	 Catch:{ all -> 0x01aa }
    L_0x019d:
        if (r2 == 0) goto L_0x01a2;
    L_0x019f:
        r2.close();	 Catch:{ IOException -> 0x01e4 }
    L_0x01a2:
        if (r7 == 0) goto L_0x01a7;
    L_0x01a4:
        r7.close();	 Catch:{ IOException -> 0x01e6 }
    L_0x01a7:
        r14 = r13;
        goto L_0x002f;
    L_0x01aa:
        r21 = move-exception;
    L_0x01ab:
        if (r2 == 0) goto L_0x01b0;
    L_0x01ad:
        r2.close();	 Catch:{ IOException -> 0x01e8 }
    L_0x01b0:
        if (r7 == 0) goto L_0x01b5;
    L_0x01b2:
        r7.close();	 Catch:{ IOException -> 0x01ea }
    L_0x01b5:
        throw r21;	 Catch:{ all -> 0x01b6 }
    L_0x01b6:
        r21 = move-exception;
        monitor-exit(r23);
        throw r21;
    L_0x01b9:
        r13 = new com.splunk.mint.NetSenderResponse;	 Catch:{ all -> 0x01b6 }
        r21 = 0;
        r0 = r24;
        r1 = r21;
        r13.m3646init(r0, r1);	 Catch:{ all -> 0x01b6 }
        r21 = 0;
        r21 = java.lang.Boolean.valueOf(r21);	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r13.setSentSuccessfully(r0);	 Catch:{ all -> 0x01b6 }
        r21 = new java.lang.Exception;	 Catch:{ all -> 0x01b6 }
        r22 = "User has opt out from logging data!";
        r21.<init>(r22);	 Catch:{ all -> 0x01b6 }
        r0 = r21;
        r13.setException(r0);	 Catch:{ all -> 0x01b6 }
        r14 = r13;
        goto L_0x002f;
    L_0x01de:
        r21 = move-exception;
        goto L_0x010e;
    L_0x01e1:
        r21 = move-exception;
        goto L_0x0113;
    L_0x01e4:
        r21 = move-exception;
        goto L_0x01a2;
    L_0x01e6:
        r21 = move-exception;
        goto L_0x01a7;
    L_0x01e8:
        r22 = move-exception;
        goto L_0x01b0;
    L_0x01ea:
        r22 = move-exception;
        goto L_0x01b5;
    L_0x01ec:
        r21 = move-exception;
        r2 = r3;
        goto L_0x01ab;
    L_0x01ef:
        r21 = move-exception;
        r7 = r8;
        r2 = r3;
        goto L_0x01ab;
    L_0x01f3:
        r5 = move-exception;
        goto L_0x014c;
    L_0x01f6:
        r5 = move-exception;
        r2 = r3;
        goto L_0x014c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.splunk.mint.NetSender.sendBlocking(java.lang.String, java.lang.String, boolean):com.splunk.mint.NetSenderResponse");
    }

    private int findAllActions(String data) {
        int count = 0;
        while (Pattern.compile("\\{\\^[0-9]+?\\^[a-z]+?\\^[0-9]+?\\}").matcher(data).find()) {
            count++;
        }
        return count;
    }

    private int findAllErrors(String data) {
        int count = 0;
        while (Pattern.compile("\\^" + EnumActionType.error.toString() + "\\^").matcher(data).find()) {
            count++;
        }
        return count;
    }

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(2);
        }
        return executor;
    }
}
