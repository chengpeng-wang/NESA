package com.baidu.mapapi.search;

import com.baidu.mapapi.search.a.a;

final class b extends Thread {
    final /* synthetic */ String a;
    final /* synthetic */ a b;
    final /* synthetic */ int c;
    final /* synthetic */ int d;

    b(String str, a aVar, int i, int i2) {
        this.a = str;
        this.b = aVar;
        this.c = i;
        this.d = i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:58:0x00cd A:{SYNTHETIC, Splitter:B:58:0x00cd} */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00d2 A:{SYNTHETIC, Splitter:B:61:0x00d2} */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void run() {
        /*
        r9 = this;
        r2 = 0;
        r3 = com.baidu.mapapi.search.a.b;
        monitor-enter(r3);
        r0 = com.baidu.mapapi.search.a.b;	 Catch:{ all -> 0x008a }
        r1 = r9.a;	 Catch:{ all -> 0x008a }
        r0 = r0.get(r1);	 Catch:{ all -> 0x008a }
        r0 = (java.lang.ref.SoftReference) r0;	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0025;
    L_0x0010:
        r0 = r0.get();	 Catch:{ all -> 0x008a }
        r0 = (com.baidu.mapapi.search.j) r0;	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0025;
    L_0x0018:
        r1 = r9.b;	 Catch:{ all -> 0x008a }
        r2 = r9.c;	 Catch:{ all -> 0x008a }
        r4 = r9.d;	 Catch:{ all -> 0x008a }
        r5 = r9.a;	 Catch:{ all -> 0x008a }
        r1.onOk(r2, r4, r5, r0);	 Catch:{ all -> 0x008a }
        monitor-exit(r3);	 Catch:{ all -> 0x008a }
    L_0x0024:
        return;
    L_0x0025:
        r4 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x008a }
        r4.<init>();	 Catch:{ all -> 0x008a }
        r0 = r9.a;	 Catch:{ Exception -> 0x00dd, all -> 0x00c9 }
        r1 = com.baidu.mapapi.search.a.a(r0);	 Catch:{ Exception -> 0x00dd, all -> 0x00c9 }
        if (r1 == 0) goto L_0x008d;
    L_0x0032:
        r0 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r1.setConnectTimeout(r0);	 Catch:{ Exception -> 0x009f }
        r1.connect();	 Catch:{ Exception -> 0x009f }
        r0 = r1.getResponseCode();	 Catch:{ Exception -> 0x009f }
        r5 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r5) goto L_0x007e;
    L_0x0042:
        r2 = r1.getInputStream();	 Catch:{ Exception -> 0x009f }
        r0 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
        r5 = new byte[r0];	 Catch:{ Exception -> 0x009f }
        r0 = r2.read(r5);	 Catch:{ Exception -> 0x009f }
    L_0x004e:
        r6 = -1;
        if (r0 == r6) goto L_0x005a;
    L_0x0051:
        r6 = 0;
        r4.write(r5, r6, r0);	 Catch:{ Exception -> 0x009f }
        r0 = r2.read(r5);	 Catch:{ Exception -> 0x009f }
        goto L_0x004e;
    L_0x005a:
        r0 = r9.b;	 Catch:{ Exception -> 0x009f }
        if (r0 == 0) goto L_0x007e;
    L_0x005e:
        r0 = new com.baidu.mapapi.search.j;	 Catch:{ Exception -> 0x009f }
        r4 = r4.toByteArray();	 Catch:{ Exception -> 0x009f }
        r0.m1069init(r4);	 Catch:{ Exception -> 0x009f }
        r4 = new java.lang.ref.SoftReference;	 Catch:{ Exception -> 0x009f }
        r4.<init>(r0);	 Catch:{ Exception -> 0x009f }
        r5 = com.baidu.mapapi.search.a.b;	 Catch:{ Exception -> 0x009f }
        r6 = r9.a;	 Catch:{ Exception -> 0x009f }
        r5.put(r6, r4);	 Catch:{ Exception -> 0x009f }
        r4 = r9.b;	 Catch:{ Exception -> 0x009f }
        r5 = r9.c;	 Catch:{ Exception -> 0x009f }
        r6 = r9.d;	 Catch:{ Exception -> 0x009f }
        r7 = r9.a;	 Catch:{ Exception -> 0x009f }
        r4.onOk(r5, r6, r7, r0);	 Catch:{ Exception -> 0x009f }
    L_0x007e:
        if (r2 == 0) goto L_0x0083;
    L_0x0080:
        r2.close();	 Catch:{ IOException -> 0x00bf }
    L_0x0083:
        if (r1 == 0) goto L_0x0088;
    L_0x0085:
        r1.disconnect();	 Catch:{ all -> 0x008a }
    L_0x0088:
        monitor-exit(r3);	 Catch:{ all -> 0x008a }
        goto L_0x0024;
    L_0x008a:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x008a }
        throw r0;
    L_0x008d:
        r0 = r9.b;	 Catch:{ Exception -> 0x009f }
        if (r0 == 0) goto L_0x007e;
    L_0x0091:
        r0 = r9.b;	 Catch:{ Exception -> 0x009f }
        r4 = r9.c;	 Catch:{ Exception -> 0x009f }
        r5 = r9.d;	 Catch:{ Exception -> 0x009f }
        r6 = r9.a;	 Catch:{ Exception -> 0x009f }
        r7 = "网络连接失败";
        r0.onError(r4, r5, r6, r7);	 Catch:{ Exception -> 0x009f }
        goto L_0x007e;
    L_0x009f:
        r0 = move-exception;
    L_0x00a0:
        r4 = r9.b;	 Catch:{ all -> 0x00db }
        if (r4 == 0) goto L_0x00b1;
    L_0x00a4:
        r4 = r9.b;	 Catch:{ all -> 0x00db }
        r5 = r9.c;	 Catch:{ all -> 0x00db }
        r6 = r9.d;	 Catch:{ all -> 0x00db }
        r7 = r9.a;	 Catch:{ all -> 0x00db }
        r8 = "网络连接失败";
        r4.onError(r5, r6, r7, r8);	 Catch:{ all -> 0x00db }
    L_0x00b1:
        r0.printStackTrace();	 Catch:{ all -> 0x00db }
        if (r2 == 0) goto L_0x00b9;
    L_0x00b6:
        r2.close();	 Catch:{ IOException -> 0x00c4 }
    L_0x00b9:
        if (r1 == 0) goto L_0x0088;
    L_0x00bb:
        r1.disconnect();	 Catch:{ all -> 0x008a }
        goto L_0x0088;
    L_0x00bf:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x008a }
        goto L_0x0083;
    L_0x00c4:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x008a }
        goto L_0x00b9;
    L_0x00c9:
        r0 = move-exception;
        r1 = r2;
    L_0x00cb:
        if (r2 == 0) goto L_0x00d0;
    L_0x00cd:
        r2.close();	 Catch:{ IOException -> 0x00d6 }
    L_0x00d0:
        if (r1 == 0) goto L_0x00d5;
    L_0x00d2:
        r1.disconnect();	 Catch:{ all -> 0x008a }
    L_0x00d5:
        throw r0;	 Catch:{ all -> 0x008a }
    L_0x00d6:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x008a }
        goto L_0x00d0;
    L_0x00db:
        r0 = move-exception;
        goto L_0x00cb;
    L_0x00dd:
        r0 = move-exception;
        r1 = r2;
        goto L_0x00a0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.search.b.run():void");
    }
}
