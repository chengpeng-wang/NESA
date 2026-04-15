package com.flurry.sdk;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.List;

final class cf extends jk {
    final /* synthetic */ String a;
    final /* synthetic */ String b;
    final /* synthetic */ bx c;

    cf(bx bxVar, String str, String str2) {
        this.c = bxVar;
        this.a = str;
        this.b = str2;
    }

    public final void a(jg jgVar) {
        if (!this.c.m) {
            int i = jgVar.i;
            iw.a(3, bx.g, "Downloader: Download status code is:" + i + " for url: " + this.c.b + " chunk: " + this.c.l);
            String str = null;
            List a = jgVar.a("Content-Range");
            if (!(a == null || a.isEmpty())) {
                str = (String) a.get(0);
                iw.a(3, bx.g, "Downloader: Content range is:" + str + " for url: " + this.c.b + " chunk: " + this.c.l);
            }
            if (jgVar.b() && i == 206 && str != null && str.startsWith(this.b.replaceAll(MASTNativeAdConstants.EQUAL, " "))) {
                this.c.l = this.c.l + 1;
                hz.a.b(new cg(this));
                return;
            }
            hz.a.b(new ch(this));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0055 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0055 A:{RETURN} */
    public final void a(com.flurry.sdk.jg r7, java.io.InputStream r8) {
        /*
        r6 = this;
        r2 = 0;
        r0 = r6.c;
        r0 = r0.m;
        if (r0 == 0) goto L_0x0011;
    L_0x0009:
        r0 = new java.io.IOException;
        r1 = "Downloader: request cancelled";
        r0.<init>(r1);
        throw r0;
    L_0x0011:
        r0 = r6.c;
        r0 = r0.d;
        r1 = r6.a;
        r3 = r0.b(r1);
        if (r3 == 0) goto L_0x0035;
    L_0x001f:
        r1 = new com.flurry.sdk.cs;	 Catch:{ IOException -> 0x0043, all -> 0x004d }
        r0 = r6.c;	 Catch:{ IOException -> 0x0043, all -> 0x004d }
        r4 = r0.h;	 Catch:{ IOException -> 0x0043, all -> 0x004d }
        r1.m1015init(r8, r4);	 Catch:{ IOException -> 0x0043, all -> 0x004d }
        r0 = r3.a;	 Catch:{ IOException -> 0x0059, all -> 0x0056 }
        com.flurry.sdk.lt.a(r1, r0);	 Catch:{ IOException -> 0x0059, all -> 0x0056 }
        com.flurry.sdk.lt.a(r1);
        com.flurry.sdk.lt.a(r3);
    L_0x0035:
        if (r2 == 0) goto L_0x0055;
    L_0x0037:
        r0 = r6.c;
        r0 = r0.d;
        r1 = r6.a;
        r0.c(r1);
        throw r2;
    L_0x0043:
        r0 = move-exception;
        r1 = r2;
    L_0x0045:
        com.flurry.sdk.lt.a(r1);
        com.flurry.sdk.lt.a(r3);
        r2 = r0;
        goto L_0x0035;
    L_0x004d:
        r0 = move-exception;
    L_0x004e:
        com.flurry.sdk.lt.a(r2);
        com.flurry.sdk.lt.a(r3);
        throw r0;
    L_0x0055:
        return;
    L_0x0056:
        r0 = move-exception;
        r2 = r1;
        goto L_0x004e;
    L_0x0059:
        r0 = move-exception;
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.cf.a(com.flurry.sdk.jg, java.io.InputStream):void");
    }
}
