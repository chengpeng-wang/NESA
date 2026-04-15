package com.flurry.sdk;

final class gg implements je {
    final /* synthetic */ af a;
    final /* synthetic */ gc b;

    gg(gc gcVar, af afVar) {
        this.b = gcVar;
        this.a = afVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00cf  */
    public final /* synthetic */ void a(com.flurry.sdk.jc r9, java.lang.Object r10) {
        /*
        r8 = this;
        r7 = 3;
        r10 = (byte[]) r10;
        r0 = com.flurry.sdk.gc.a;
        r1 = new java.lang.StringBuilder;
        r2 = "AdRequest: HTTP status code is:";
        r1.<init>(r2);
        r2 = r9.i;
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r7, r0, r1);
        r0 = r8.b;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r0.j = r1;
        r1 = java.util.Collections.emptyList();
        r0 = r9.b();
        if (r0 == 0) goto L_0x010f;
    L_0x002f:
        if (r10 == 0) goto L_0x010f;
    L_0x0031:
        r2 = 0;
        r0 = r8.b;	 Catch:{ Exception -> 0x008b }
        r0 = r0.b;	 Catch:{ Exception -> 0x008b }
        r0 = r0.b(r10);	 Catch:{ Exception -> 0x008b }
        r0 = (com.flurry.sdk.dt) r0;	 Catch:{ Exception -> 0x008b }
        r2 = r0;
    L_0x003f:
        if (r2 == 0) goto L_0x010f;
    L_0x0041:
        r0 = r2.d;
        if (r0 == 0) goto L_0x0060;
    L_0x0045:
        r0 = r0.a;
        if (r0 == 0) goto L_0x0060;
    L_0x0049:
        r3 = com.flurry.sdk.gc.a;
        r4 = "Ad server responded with configuration.";
        com.flurry.sdk.iw.a(r7, r3, r4);
        r3 = new com.flurry.sdk.fk;
        r3.m3393init();
        r3.a = r0;
        r0 = com.flurry.sdk.il.a();
        r0.a(r3);
    L_0x0060:
        r0 = r2.c;
        r0 = r0.size();
        if (r0 <= 0) goto L_0x00a4;
    L_0x0068:
        r0 = com.flurry.sdk.gc.a;
        r3 = "Ad server responded with the following error(s):";
        com.flurry.sdk.iw.b(r0, r3);
        r0 = r2.c;
        r3 = r0.iterator();
    L_0x0077:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x00a4;
    L_0x007d:
        r0 = r3.next();
        r0 = (java.lang.String) r0;
        r4 = com.flurry.sdk.gc.a;
        com.flurry.sdk.iw.b(r4, r0);
        goto L_0x0077;
    L_0x008b:
        r0 = move-exception;
        r3 = 5;
        r4 = com.flurry.sdk.gc.a;
        r5 = new java.lang.StringBuilder;
        r6 = "Failed to decode ad response: ";
        r5.<init>(r6);
        r0 = r5.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r3, r4, r0);
        goto L_0x003f;
    L_0x00a4:
        r0 = r2.a;
        if (r0 == 0) goto L_0x010d;
    L_0x00a8:
        r0 = r2.a;
    L_0x00aa:
        r1 = r8.b;
        r1 = r1.d;
        r1 = android.text.TextUtils.isEmpty(r1);
        if (r1 != 0) goto L_0x00c5;
    L_0x00b6:
        r1 = r0.size();
        if (r1 != 0) goto L_0x00c5;
    L_0x00bc:
        r1 = com.flurry.sdk.gc.a;
        r2 = "Ad server responded but sent no ad units.";
        com.flurry.sdk.iw.b(r1, r2);
    L_0x00c5:
        r1 = r0.iterator();
    L_0x00c9:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x00fb;
    L_0x00cf:
        r0 = r1.next();
        r0 = (com.flurry.sdk.dv) r0;
        r2 = r0.d;
        r2 = r2.size();
        if (r2 == 0) goto L_0x00c9;
    L_0x00dd:
        r2 = r8.a;
        r2 = r2 instanceof com.flurry.sdk.ag;
        if (r2 == 0) goto L_0x00ec;
    L_0x00e3:
        r2 = com.flurry.sdk.f.a();
        r3 = "nativeAdReturned";
        r2.a(r3);
    L_0x00ec:
        r2 = new com.flurry.sdk.ct;
        r2.m1016init(r0);
        r0 = r8.b;
        r0 = r0.j;
        r0.add(r2);
        goto L_0x00c9;
    L_0x00fb:
        r0 = r8.b;
        r1 = com.flurry.sdk.gj.PREPROCESS;
        r0.a(r1);
        r0 = com.flurry.sdk.hz.a;
        r1 = new com.flurry.sdk.gh;
        r1.m3413init(r8);
        r0.b(r1);
        return;
    L_0x010d:
        r0 = r1;
        goto L_0x00aa;
    L_0x010f:
        r0 = r1;
        goto L_0x00c5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.gg.a(com.flurry.sdk.jc, java.lang.Object):void");
    }
}
