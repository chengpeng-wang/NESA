package com.cmcm.adsdk.unifiedreport;

/* compiled from: UnifiedNetUtil */
public final class a {
    /* JADX WARNING: Removed duplicated region for block: B:72:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065 A:{SYNTHETIC, Splitter:B:21:0x0065} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006a A:{SYNTHETIC, Splitter:B:24:0x006a} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:72:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0097 A:{SYNTHETIC, Splitter:B:47:0x0097} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x009c A:{SYNTHETIC, Splitter:B:50:0x009c} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0065 A:{SYNTHETIC, Splitter:B:21:0x0065} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006a A:{SYNTHETIC, Splitter:B:24:0x006a} */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:72:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0097 A:{SYNTHETIC, Splitter:B:47:0x0097} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x009c A:{SYNTHETIC, Splitter:B:50:0x009c} */
    public static java.lang.String a(java.lang.String r8, int r9, int r10) {
        /*
        r1 = 0;
        if (r8 == 0) goto L_0x0007;
    L_0x0003:
        if (r9 <= 0) goto L_0x0007;
    L_0x0005:
        if (r10 > 0) goto L_0x0009;
    L_0x0007:
        r0 = r1;
    L_0x0008:
        return r0;
    L_0x0009:
        r0 = new java.net.URL;	 Catch:{ Exception -> 0x00b3, all -> 0x0093 }
        r0.<init>(r8);	 Catch:{ Exception -> 0x00b3, all -> 0x0093 }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x00b3, all -> 0x0093 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x00b3, all -> 0x0093 }
        r0.setConnectTimeout(r9);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = "GET";
        r0.setRequestMethod(r2);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = "Charset";
        r3 = "utf-8";
        r0.setRequestProperty(r2, r3);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = 0;
        r0.setUseCaches(r2);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = 1;
        r0.setDoInput(r2);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = 0;
        r0.setDoOutput(r2);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r0.connect();	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = r0.getResponseCode();	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 < r3) goto L_0x00be;
    L_0x003a:
        r3 = 207; // 0xcf float:2.9E-43 double:1.023E-321;
        if (r2 > r3) goto L_0x00be;
    L_0x003e:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r3.<init>();	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r4 = new char[r2];	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r5 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r6 = r0.getInputStream();	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r5.<init>(r6);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
        r2.<init>(r5);	 Catch:{ Exception -> 0x00b7, all -> 0x00a8 }
    L_0x0055:
        r5 = r2.read(r4);	 Catch:{ Exception -> 0x0062, all -> 0x00ae }
        if (r5 < 0) goto L_0x0077;
    L_0x005b:
        if (r5 <= 0) goto L_0x0055;
    L_0x005d:
        r6 = 0;
        r3.append(r4, r6, r5);	 Catch:{ Exception -> 0x0062, all -> 0x00ae }
        goto L_0x0055;
    L_0x0062:
        r3 = move-exception;
    L_0x0063:
        if (r0 == 0) goto L_0x0068;
    L_0x0065:
        r0.disconnect();	 Catch:{ Exception -> 0x00a2 }
    L_0x0068:
        if (r2 == 0) goto L_0x00ba;
    L_0x006a:
        r2.close();	 Catch:{ Exception -> 0x0090 }
        r0 = r1;
    L_0x006e:
        if (r0 != 0) goto L_0x0008;
    L_0x0070:
        r0 = r10 + -1;
        r0 = a(r8, r9, r0);
        goto L_0x0008;
    L_0x0077:
        r4 = r3.length();	 Catch:{ Exception -> 0x0062, all -> 0x00ae }
        if (r4 <= 0) goto L_0x0081;
    L_0x007d:
        r1 = r3.toString();	 Catch:{ Exception -> 0x0062, all -> 0x00ae }
    L_0x0081:
        if (r0 == 0) goto L_0x0086;
    L_0x0083:
        r0.disconnect();	 Catch:{ Exception -> 0x00a0 }
    L_0x0086:
        if (r2 == 0) goto L_0x00bc;
    L_0x0088:
        r2.close();	 Catch:{ Exception -> 0x008d }
        r0 = r1;
        goto L_0x006e;
    L_0x008d:
        r0 = move-exception;
        r0 = r1;
        goto L_0x006e;
    L_0x0090:
        r0 = move-exception;
        r0 = r1;
        goto L_0x006e;
    L_0x0093:
        r0 = move-exception;
        r2 = r1;
    L_0x0095:
        if (r1 == 0) goto L_0x009a;
    L_0x0097:
        r1.disconnect();	 Catch:{ Exception -> 0x00a4 }
    L_0x009a:
        if (r2 == 0) goto L_0x009f;
    L_0x009c:
        r2.close();	 Catch:{ Exception -> 0x00a6 }
    L_0x009f:
        throw r0;
    L_0x00a0:
        r0 = move-exception;
        goto L_0x0086;
    L_0x00a2:
        r0 = move-exception;
        goto L_0x0068;
    L_0x00a4:
        r1 = move-exception;
        goto L_0x009a;
    L_0x00a6:
        r1 = move-exception;
        goto L_0x009f;
    L_0x00a8:
        r2 = move-exception;
        r7 = r2;
        r2 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0095;
    L_0x00ae:
        r1 = move-exception;
        r7 = r1;
        r1 = r0;
        r0 = r7;
        goto L_0x0095;
    L_0x00b3:
        r0 = move-exception;
        r0 = r1;
        r2 = r1;
        goto L_0x0063;
    L_0x00b7:
        r2 = move-exception;
        r2 = r1;
        goto L_0x0063;
    L_0x00ba:
        r0 = r1;
        goto L_0x006e;
    L_0x00bc:
        r0 = r1;
        goto L_0x006e;
    L_0x00be:
        r2 = r1;
        goto L_0x0081;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.unifiedreport.a.a(java.lang.String, int, int):java.lang.String");
    }
}
