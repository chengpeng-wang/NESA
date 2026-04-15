package com.flurry.sdk;

final class fa implements je {
    final /* synthetic */ eq a;
    final /* synthetic */ ey b;

    fa(ey eyVar, eq eqVar) {
        this.b = eyVar;
        this.a = eqVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:47:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x013c  */
    public final /* synthetic */ void a(com.flurry.sdk.jc r7, java.lang.Object r8) {
        /*
        r6 = this;
        r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        r1 = 0;
        r5 = 3;
        r0 = com.flurry.sdk.ey.b;
        r2 = new java.lang.StringBuilder;
        r3 = "AsyncReportInfo request: HTTP status code is:";
        r2.<init>(r3);
        r3 = r7.i;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r5, r0, r2);
        r2 = r7.i;
        r0 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 < r0) goto L_0x005f;
    L_0x0022:
        if (r2 >= r4) goto L_0x005f;
    L_0x0024:
        r0 = com.flurry.sdk.ey.b;
        r1 = new java.lang.StringBuilder;
        r3 = "Send report successful to url: ";
        r1.<init>(r3);
        r3 = r7.e;
        r1 = r1.append(r3);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r5, r0, r1);
        r0 = r6.b;
        r1 = r6.a;
        r0.c(r1);
        r0 = com.flurry.sdk.iw.a();
        if (r0 > r5) goto L_0x0059;
    L_0x0049:
        r0 = com.flurry.sdk.iw.b();
        if (r0 == 0) goto L_0x0059;
    L_0x004f:
        r0 = com.flurry.sdk.hz.a;
        r1 = new com.flurry.sdk.fb;
        r1.m1085init(r6);
        r0.a(r1);
    L_0x0059:
        r0 = r6.a;
        com.flurry.sdk.ey.a(r0, r2);
    L_0x005e:
        return;
    L_0x005f:
        if (r2 < r4) goto L_0x00e6;
    L_0x0061:
        r0 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r2 >= r0) goto L_0x00e6;
    L_0x0065:
        r0 = 0;
        r3 = "Location";
        r3 = r7.a(r3);
        if (r3 == 0) goto L_0x0082;
    L_0x006e:
        r4 = r3.size();
        if (r4 <= 0) goto L_0x0082;
    L_0x0074:
        r0 = r3.get(r1);
        r0 = (java.lang.String) r0;
        r1 = r6.a;
        r1 = r1.g;
        r0 = com.flurry.sdk.mb.a(r0, r1);
    L_0x0082:
        r1 = android.text.TextUtils.isEmpty(r0);
        if (r1 == 0) goto L_0x00c3;
    L_0x0088:
        r0 = com.flurry.sdk.ey.b;
        r1 = new java.lang.StringBuilder;
        r3 = "Send report successful to url: ";
        r1.<init>(r3);
        r3 = r7.e;
        r1 = r1.append(r3);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r5, r0, r1);
        r0 = r6.b;
        r1 = r6.a;
        r0.c(r1);
        r0 = com.flurry.sdk.iw.a();
        if (r0 > r5) goto L_0x00bd;
    L_0x00ad:
        r0 = com.flurry.sdk.iw.b();
        if (r0 == 0) goto L_0x00bd;
    L_0x00b3:
        r0 = com.flurry.sdk.hz.a;
        r1 = new com.flurry.sdk.fc;
        r1.m1086init(r6);
        r0.a(r1);
    L_0x00bd:
        r0 = r6.a;
        com.flurry.sdk.ey.a(r0, r2);
        goto L_0x005e;
    L_0x00c3:
        r1 = com.flurry.sdk.ey.b;
        r2 = new java.lang.StringBuilder;
        r3 = "Send report redirecting to url: ";
        r2.<init>(r3);
        r2 = r2.append(r0);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r5, r1, r2);
        r1 = r6.a;
        r1.g = r0;
        r0 = r6.b;
        r1 = r6.a;
        r0.a(r1);
        goto L_0x005e;
    L_0x00e6:
        r0 = com.flurry.sdk.ey.b;
        r3 = new java.lang.StringBuilder;
        r4 = "Send report failed to url: ";
        r3.<init>(r4);
        r4 = r7.e;
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r5, r0, r3);
        r0 = r6.a;
        r0 = r0.g;
        r3 = android.text.TextUtils.isEmpty(r0);
        if (r3 != 0) goto L_0x0143;
    L_0x0108:
        r0 = com.flurry.sdk.mb.b(r0);
        if (r0 == 0) goto L_0x0143;
    L_0x010e:
        r3 = r0.getScheme();
        if (r3 == 0) goto L_0x012c;
    L_0x0114:
        r3 = "http";
        r4 = r0.getScheme();
        r3 = r3.equalsIgnoreCase(r4);
        if (r3 != 0) goto L_0x012c;
    L_0x0120:
        r3 = "https";
        r0 = r0.getScheme();
        r0 = r3.equalsIgnoreCase(r0);
        if (r0 == 0) goto L_0x0143;
    L_0x012c:
        r0 = 1;
    L_0x012d:
        if (r0 == 0) goto L_0x0145;
    L_0x012f:
        r0 = r6.b;
        r1 = r6.a;
        r0.d(r1);
    L_0x0136:
        r0 = r6.a;
        r0 = r0.e;
        if (r0 != 0) goto L_0x005e;
    L_0x013c:
        r0 = r6.a;
        com.flurry.sdk.ey.a(r0, r2);
        goto L_0x005e;
    L_0x0143:
        r0 = r1;
        goto L_0x012d;
    L_0x0145:
        r0 = com.flurry.sdk.ey.b;
        r1 = new java.lang.StringBuilder;
        r3 = "Oops! url: ";
        r1.<init>(r3);
        r3 = r7.e;
        r1 = r1.append(r3);
        r3 = " is invalid, aborting transmission";
        r1 = r1.append(r3);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r5, r0, r1);
        r0 = r6.b;
        r1 = r6.a;
        r0.c(r1);
        goto L_0x0136;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.fa.a(com.flurry.sdk.jc, java.lang.Object):void");
    }
}
