package com.flurry.sdk;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class gc {
    /* access modifiers changed from: private|static|final */
    public static final String a = gc.class.getSimpleName();
    /* access modifiers changed from: private|final */
    public final ja b = new ja("ad response", new gn());
    private final ja c = new ja("ad request", new gl());
    /* access modifiers changed from: private|final */
    public final String d;
    private final List e;
    private gj f;
    /* access modifiers changed from: private */
    public af g;
    /* access modifiers changed from: private */
    public ct h;
    private am i;
    /* access modifiers changed from: private */
    public List j;
    private final ik k = new gd(this);

    public gc(String str) {
        this.d = str;
        this.e = Arrays.asList(new Integer[]{Integer.valueOf(dd.a), Integer.valueOf(dd.b), Integer.valueOf(dd.c), Integer.valueOf(dd.d), Integer.valueOf(dd.e), Integer.valueOf(dd.f)});
        this.f = gj.NONE;
        b();
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(com.flurry.sdk.af r21, com.flurry.sdk.ct r22) {
        /*
        r20 = this;
        monitor-enter(r20);
        r3 = com.flurry.sdk.gj.BUILD_REQUEST;	 Catch:{ all -> 0x02d1 }
        r0 = r20;
        r4 = r0.f;	 Catch:{ all -> 0x02d1 }
        r3 = r3.equals(r4);	 Catch:{ all -> 0x02d1 }
        if (r3 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r20);
        return;
    L_0x000f:
        r3 = com.flurry.sdk.gj.REQUEST;	 Catch:{ all -> 0x02d1 }
        r0 = r20;
        r0.a(r3);	 Catch:{ all -> 0x02d1 }
        r6 = r21.f();	 Catch:{ all -> 0x02d1 }
        r7 = com.flurry.sdk.dx.a;	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.lr.d();	 Catch:{ all -> 0x02d1 }
        r4 = com.flurry.sdk.lr.b();	 Catch:{ all -> 0x02d1 }
        r5 = com.flurry.sdk.lr.c();	 Catch:{ all -> 0x02d1 }
        switch(r3) {
            case 2: goto L_0x02d4;
            default: goto L_0x002b;
        };	 Catch:{ all -> 0x02d1 }
    L_0x002b:
        r3 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x02d1 }
        r4 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x02d1 }
        r3 = android.util.Pair.create(r3, r4);	 Catch:{ all -> 0x02d1 }
        r4 = r3;
    L_0x0038:
        r3 = r4.first;	 Catch:{ all -> 0x02d1 }
        r3 = (java.lang.Integer) r3;	 Catch:{ all -> 0x02d1 }
        r8 = r3.intValue();	 Catch:{ all -> 0x02d1 }
        r3 = r4.second;	 Catch:{ all -> 0x02d1 }
        r3 = (java.lang.Integer) r3;	 Catch:{ all -> 0x02d1 }
        r9 = r3.intValue();	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.lr.b();	 Catch:{ all -> 0x02d1 }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x02d1 }
        r4 = com.flurry.sdk.lr.c();	 Catch:{ all -> 0x02d1 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x02d1 }
        r5 = android.util.Pair.create(r3, r4);	 Catch:{ all -> 0x02d1 }
        r3 = r5.first;	 Catch:{ all -> 0x02d1 }
        r3 = (java.lang.Integer) r3;	 Catch:{ all -> 0x02d1 }
        r4 = r3.intValue();	 Catch:{ all -> 0x02d1 }
        r3 = r5.second;	 Catch:{ all -> 0x02d1 }
        r3 = (java.lang.Integer) r3;	 Catch:{ all -> 0x02d1 }
        r3 = r3.intValue();	 Catch:{ all -> 0x02d1 }
        if (r6 == 0) goto L_0x031c;
    L_0x006e:
        r5 = r6.getHeight();	 Catch:{ all -> 0x02d1 }
        if (r5 <= 0) goto L_0x031c;
    L_0x0074:
        r3 = r6.getHeight();	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.lr.a(r3);	 Catch:{ all -> 0x02d1 }
        r5 = r3;
    L_0x007d:
        if (r6 == 0) goto L_0x0319;
    L_0x007f:
        r3 = r6.getWidth();	 Catch:{ all -> 0x02d1 }
        if (r3 <= 0) goto L_0x0319;
    L_0x0085:
        r3 = r6.getWidth();	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.lr.a(r3);	 Catch:{ all -> 0x02d1 }
    L_0x008d:
        r6 = new com.flurry.sdk.dw;	 Catch:{ all -> 0x02d1 }
        r6.m1053init();	 Catch:{ all -> 0x02d1 }
        r6.d = r9;	 Catch:{ all -> 0x02d1 }
        r6.c = r8;	 Catch:{ all -> 0x02d1 }
        r6.b = r5;	 Catch:{ all -> 0x02d1 }
        r6.a = r3;	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.lr.a();	 Catch:{ all -> 0x02d1 }
        r3 = r3.density;	 Catch:{ all -> 0x02d1 }
        r6.e = r3;	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.ha.b();	 Catch:{ all -> 0x02d1 }
        r6.f = r3;	 Catch:{ all -> 0x02d1 }
        r8 = com.flurry.sdk.ha.c();	 Catch:{ all -> 0x02d1 }
        r9 = java.util.Collections.emptyMap();	 Catch:{ all -> 0x02d1 }
        r10 = new com.flurry.sdk.ep;	 Catch:{ all -> 0x02d1 }
        r10.m1078init();	 Catch:{ all -> 0x02d1 }
        r3 = java.util.Collections.emptyList();	 Catch:{ all -> 0x02d1 }
        r10.c = r3;	 Catch:{ all -> 0x02d1 }
        r3 = -1;
        r10.a = r3;	 Catch:{ all -> 0x02d1 }
        r3 = -1;
        r10.b = r3;	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.li.a();	 Catch:{ all -> 0x02d1 }
        r4 = "Age";
        r3 = r3.a(r4);	 Catch:{ all -> 0x02d1 }
        r3 = (java.lang.Long) r3;	 Catch:{ all -> 0x02d1 }
        r4 = com.flurry.sdk.li.a();	 Catch:{ all -> 0x02d1 }
        r5 = "Gender";
        r4 = r4.a(r5);	 Catch:{ all -> 0x02d1 }
        r4 = (java.lang.Byte) r4;	 Catch:{ all -> 0x02d1 }
        if (r4 == 0) goto L_0x00e8;
    L_0x00db:
        r5 = r4.byteValue();	 Catch:{ all -> 0x02d1 }
        r11 = -1;
        if (r5 == r11) goto L_0x00e8;
    L_0x00e2:
        r4 = r4.intValue();	 Catch:{ all -> 0x02d1 }
        r10.b = r4;	 Catch:{ all -> 0x02d1 }
    L_0x00e8:
        if (r3 == 0) goto L_0x0107;
    L_0x00ea:
        r4 = java.util.Calendar.getInstance();	 Catch:{ all -> 0x02d1 }
        r11 = r3.longValue();	 Catch:{ all -> 0x02d1 }
        r4.setTimeInMillis(r11);	 Catch:{ all -> 0x02d1 }
        r3 = 1;
        r3 = r4.get(r3);	 Catch:{ all -> 0x02d1 }
        r4 = java.util.Calendar.getInstance();	 Catch:{ all -> 0x02d1 }
        r5 = 1;
        r4 = r4.get(r5);	 Catch:{ all -> 0x02d1 }
        r3 = r4 - r3;
        r10.a = r3;	 Catch:{ all -> 0x02d1 }
    L_0x0107:
        r11 = com.flurry.sdk.ha.e();	 Catch:{ all -> 0x02d1 }
        r12 = com.flurry.sdk.ha.f();	 Catch:{ all -> 0x02d1 }
        r13 = java.util.Collections.emptyList();	 Catch:{ all -> 0x02d1 }
        r14 = new java.util.ArrayList;	 Catch:{ all -> 0x02d1 }
        r14.<init>();	 Catch:{ all -> 0x02d1 }
        r4 = 0;
        r3 = java.util.Collections.emptyMap();	 Catch:{ all -> 0x02d1 }
        if (r22 == 0) goto L_0x0315;
    L_0x011f:
        r0 = r22;
        r3 = r0.a;	 Catch:{ all -> 0x02d1 }
        r4 = r3.t;	 Catch:{ all -> 0x02d1 }
        r3 = r3.u;	 Catch:{ all -> 0x02d1 }
        r5 = r4;
        r4 = r3;
    L_0x0129:
        r15 = new com.flurry.sdk.ef;	 Catch:{ all -> 0x02d1 }
        r15.m1066init();	 Catch:{ all -> 0x02d1 }
        r3 = 1;
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x02d1 }
        r3 = java.util.Collections.singletonList(r3);	 Catch:{ all -> 0x02d1 }
        r15.a = r3;	 Catch:{ all -> 0x02d1 }
        r3 = java.util.Collections.emptyList();	 Catch:{ all -> 0x02d1 }
        r15.b = r3;	 Catch:{ all -> 0x02d1 }
        r16 = "";
        r17 = new com.flurry.sdk.ds;	 Catch:{ Exception -> 0x02ec }
        r17.m1049init();	 Catch:{ Exception -> 0x02ec }
        r18 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x02ec }
        r0 = r18;
        r2 = r17;
        r2.a = r0;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x02ec }
        r3 = r3.d;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.b = r3;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.ia.a();	 Catch:{ Exception -> 0x02ec }
        r3 = java.lang.Integer.toString(r3);	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.c = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.d = r7;	 Catch:{ Exception -> 0x02ec }
        r0 = r20;
        r3 = r0.d;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.e = r3;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hg.a();	 Catch:{ Exception -> 0x02ec }
        r0 = r3.a;	 Catch:{ Exception -> 0x02ec }
        r18 = r0;
        r0 = r18;
        r2 = r17;
        r2.f = r0;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.g = r11;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.h = r8;	 Catch:{ Exception -> 0x02ec }
        r3 = 0;
        r0 = r17;
        r0.i = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r20;
        r3 = r0.e;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.j = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.k = r6;	 Catch:{ Exception -> 0x02ec }
        com.flurry.sdk.hp.a();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hp.e();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.l = r3;	 Catch:{ Exception -> 0x02ec }
        com.flurry.sdk.hp.a();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hp.f();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.m = r3;	 Catch:{ Exception -> 0x02ec }
        com.flurry.sdk.hw.a();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hw.b();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.n = r3;	 Catch:{ Exception -> 0x02ec }
        com.flurry.sdk.hw.a();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hw.c();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.o = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.p = r9;	 Catch:{ Exception -> 0x02ec }
        r3 = 0;
        r0 = r17;
        r0.q = r3;	 Catch:{ Exception -> 0x02ec }
        com.flurry.sdk.hg.a();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hg.b();	 Catch:{ Exception -> 0x02ec }
        r3 = r3.e;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.r = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.s = r12;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.t = r13;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.hi.a();	 Catch:{ Exception -> 0x02ec }
        r6 = r3.b;	 Catch:{ Exception -> 0x02ec }
        if (r6 == 0) goto L_0x01ef;
    L_0x01e9:
        r3 = r3.b;	 Catch:{ Exception -> 0x02ec }
        r3 = r3.b;	 Catch:{ Exception -> 0x02ec }
        if (r3 != 0) goto L_0x02e3;
    L_0x01ef:
        r3 = 1;
    L_0x01f0:
        r0 = r17;
        r0.u = r3;	 Catch:{ Exception -> 0x02ec }
        r3 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x02ec }
        r3 = r3.getLanguage();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.v = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.w = r14;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.n.a();	 Catch:{ Exception -> 0x02ec }
        r6 = r3.i;	 Catch:{ Exception -> 0x02ec }
        if (r6 == 0) goto L_0x02e6;
    L_0x020c:
        r3 = r3.i;	 Catch:{ Exception -> 0x02ec }
        r3 = r3.c;	 Catch:{ Exception -> 0x02ec }
    L_0x0210:
        r0 = r17;
        r0.x = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.y = r10;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.n.a();	 Catch:{ Exception -> 0x02ec }
        r3 = r3.h;	 Catch:{ Exception -> 0x02ec }
        if (r3 != 0) goto L_0x02e9;
    L_0x0220:
        r3 = 1;
    L_0x0221:
        r0 = r17;
        r0.z = r3;	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.ha.d();	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.A = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.B = r5;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.C = r4;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.D = r15;	 Catch:{ Exception -> 0x02ec }
        r0 = r16;
        r1 = r17;
        r1.E = r0;	 Catch:{ Exception -> 0x02ec }
        r3 = r21.e();	 Catch:{ Exception -> 0x02ec }
        r3 = com.flurry.sdk.lq.a(r3);	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r0.F = r3;	 Catch:{ Exception -> 0x02ec }
        r0 = r20;
        r3 = r0.c;	 Catch:{ Exception -> 0x02ec }
        r0 = r17;
        r4 = r3.a(r0);	 Catch:{ Exception -> 0x02ec }
        r5 = new com.flurry.sdk.jc;	 Catch:{ all -> 0x02d1 }
        r5.m4896init();	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.u.a();	 Catch:{ all -> 0x02d1 }
        r6 = r3.a;	 Catch:{ all -> 0x02d1 }
        if (r6 == 0) goto L_0x0307;
    L_0x0262:
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02d1 }
        r6.<init>();	 Catch:{ all -> 0x02d1 }
        r3 = r3.a;	 Catch:{ all -> 0x02d1 }
        r3 = r6.append(r3);	 Catch:{ all -> 0x02d1 }
        r6 = "/v14/getAds.do";
        r3 = r3.append(r6);	 Catch:{ all -> 0x02d1 }
        r3 = r3.toString();	 Catch:{ all -> 0x02d1 }
    L_0x0277:
        r5.e = r3;	 Catch:{ all -> 0x02d1 }
        r3 = 20000; // 0x4e20 float:2.8026E-41 double:9.8813E-320;
        r5.j = r3;	 Catch:{ all -> 0x02d1 }
        r3 = com.flurry.sdk.jj.kPost;	 Catch:{ all -> 0x02d1 }
        r5.f = r3;	 Catch:{ all -> 0x02d1 }
        r3 = "Content-Type";
        r6 = "application/x-flurry";
        r5.a(r3, r6);	 Catch:{ all -> 0x02d1 }
        r3 = "Accept";
        r6 = "application/x-flurry";
        r5.a(r3, r6);	 Catch:{ all -> 0x02d1 }
        r3 = "FM-Checksum";
        r6 = com.flurry.sdk.ja.a(r4);	 Catch:{ all -> 0x02d1 }
        r6 = java.lang.Integer.toString(r6);	 Catch:{ all -> 0x02d1 }
        r5.a(r3, r6);	 Catch:{ all -> 0x02d1 }
        r3 = new com.flurry.sdk.kw;	 Catch:{ all -> 0x02d1 }
        r3.m3478init();	 Catch:{ all -> 0x02d1 }
        r5.c = r3;	 Catch:{ all -> 0x02d1 }
        r3 = new com.flurry.sdk.kw;	 Catch:{ all -> 0x02d1 }
        r3.m3478init();	 Catch:{ all -> 0x02d1 }
        r5.d = r3;	 Catch:{ all -> 0x02d1 }
        r5.b = r4;	 Catch:{ all -> 0x02d1 }
        r3 = new com.flurry.sdk.gg;	 Catch:{ all -> 0x02d1 }
        r0 = r20;
        r1 = r21;
        r3.m3412init(r0, r1);	 Catch:{ all -> 0x02d1 }
        r5.a = r3;	 Catch:{ all -> 0x02d1 }
        r0 = r21;
        r3 = r0 instanceof com.flurry.sdk.ag;	 Catch:{ all -> 0x02d1 }
        if (r3 == 0) goto L_0x02c6;
    L_0x02bd:
        r3 = com.flurry.sdk.f.a();	 Catch:{ all -> 0x02d1 }
        r4 = "nativeAdRequest";
        r3.a(r4);	 Catch:{ all -> 0x02d1 }
    L_0x02c6:
        r3 = com.flurry.sdk.hy.a();	 Catch:{ all -> 0x02d1 }
        r0 = r20;
        r3.a(r0, r5);	 Catch:{ all -> 0x02d1 }
        goto L_0x000d;
    L_0x02d1:
        r3 = move-exception;
        monitor-exit(r20);
        throw r3;
    L_0x02d4:
        r3 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x02d1 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x02d1 }
        r3 = android.util.Pair.create(r3, r4);	 Catch:{ all -> 0x02d1 }
        r4 = r3;
        goto L_0x0038;
    L_0x02e3:
        r3 = 0;
        goto L_0x01f0;
    L_0x02e6:
        r3 = 0;
        goto L_0x0210;
    L_0x02e9:
        r3 = 0;
        goto L_0x0221;
    L_0x02ec:
        r3 = move-exception;
        r4 = 5;
        r5 = a;	 Catch:{ all -> 0x02d1 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02d1 }
        r7 = "Ad request failed with exception: ";
        r6.<init>(r7);	 Catch:{ all -> 0x02d1 }
        r3 = r6.append(r3);	 Catch:{ all -> 0x02d1 }
        r3 = r3.toString();	 Catch:{ all -> 0x02d1 }
        com.flurry.sdk.iw.a(r4, r5, r3);	 Catch:{ all -> 0x02d1 }
        r20.b();	 Catch:{ all -> 0x02d1 }
        goto L_0x000d;
    L_0x0307:
        r3 = com.flurry.sdk.u.b();	 Catch:{ all -> 0x02d1 }
        if (r3 == 0) goto L_0x0311;
    L_0x030d:
        r3 = "https://ads.flurry.com/v14/getAds.do";
        goto L_0x0277;
    L_0x0311:
        r3 = "http://ads.flurry.com/v14/getAds.do";
        goto L_0x0277;
    L_0x0315:
        r5 = r4;
        r4 = r3;
        goto L_0x0129;
    L_0x0319:
        r3 = r4;
        goto L_0x008d;
    L_0x031c:
        r5 = r3;
        goto L_0x007d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.gc.a(com.flurry.sdk.af, com.flurry.sdk.ct):void");
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(gj gjVar) {
        Object gjVar2;
        if (gjVar2 == null) {
            gjVar2 = gj.NONE;
        }
        iw.a(3, a, "Setting state from " + this.f + " to " + gjVar2);
        if (gj.NONE.equals(this.f) && !gj.NONE.equals(gjVar2)) {
            iw.a(3, a, "Adding request listeners for adspace: " + this.d);
            il.a().a("com.flurry.android.sdk.IdProviderFinishedEvent", this.k);
        } else if (gj.NONE.equals(gjVar2) && !gj.NONE.equals(this.f)) {
            iw.a(3, a, "Removing request listeners for adspace: " + this.d);
            il.a().a(this.k);
        }
        this.f = gjVar2;
    }

    private synchronized void b() {
        hy.a().a((Object) this);
        a(gj.NONE);
        this.i = null;
        this.g = null;
        this.h = null;
        this.j = null;
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void c() {
        if (gj.WAIT_FOR_REPORTED_IDS.equals(this.f)) {
            iw.a(3, a, "Reported ids retrieved; request may continue");
            a(gj.BUILD_REQUEST);
            hz.a.b(new gf(this));
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void d() {
        if (gj.PREPROCESS.equals(this.f)) {
            for (ct ctVar : this.j) {
                List list = ctVar.a.d;
                int i = 0;
                while (i < list.size()) {
                    List emptyList;
                    if (ctVar == null) {
                        emptyList = Collections.emptyList();
                    } else {
                        dq a = ctVar.a(i);
                        if (a == null) {
                            emptyList = Collections.emptyList();
                        } else {
                            if (bl.STREAM_ONLY.equals(bl.values()[a.g])) {
                                emptyList = Collections.emptyList();
                            } else {
                                emptyList = new ArrayList();
                                String str;
                                if (a.i != null && a.i.size() > 0) {
                                    for (String str2 : a.i) {
                                        if (!TextUtils.isEmpty(str2)) {
                                            emptyList.add(str2);
                                        }
                                    }
                                } else if (a.a == dd.h) {
                                    for (eh ehVar : ctVar.b()) {
                                        if (ei.IMAGE.equals(ehVar.b) || ei.VIDEO.equals(ehVar.b)) {
                                            str2 = ehVar.c;
                                            if (!(TextUtils.isEmpty(str2) || be.a(a.j, str2))) {
                                                emptyList.add(str2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (i >= 0 && i < ctVar.b.size()) {
                        ((cu) ctVar.b.get(i)).a = emptyList;
                    }
                    i++;
                }
            }
            iw.a(3, a, "Handling ad response for adSpace: " + this.d + ", size: " + this.j.size());
            if (this.j.size() > 0) {
                if (this.i != null) {
                    this.i.a(this.j);
                }
                hz.a.b(new gi(this));
            }
            e();
            b();
        }
    }

    private void e() {
        ij gkVar = new gk();
        gkVar.a = this;
        gkVar.b = this.j;
        il.a().a(gkVar);
    }

    public final synchronized void a(af afVar, am amVar) {
        iw.a(3, a, "requestAd: adSpace = " + this.d);
        if (!gj.NONE.equals(this.f)) {
            iw.a(3, a, "requestAds: request pending " + this.f);
        } else if (ht.a().a) {
            this.g = afVar;
            this.h = null;
            this.i = amVar;
            n.a().f.b();
            if (hi.a().b()) {
                a(gj.BUILD_REQUEST);
                hz.a.b(new ge(this));
            } else {
                iw.a(3, a, "No reported ids yet; waiting");
                a(gj.WAIT_FOR_REPORTED_IDS);
            }
        } else {
            iw.a(5, a, "There is no network connectivity (requestAds will fail)");
            e();
        }
    }
}
