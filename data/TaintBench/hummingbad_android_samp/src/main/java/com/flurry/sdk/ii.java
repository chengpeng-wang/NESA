package com.flurry.sdk;

import java.io.File;

public class ii {
    private static final String a = ii.class.getSimpleName();
    private final File b;
    private final lc c;

    public ii(File file, String str, int i, lh lhVar) {
        this.b = file;
        this.c = new ky(new le(str, i, lhVar));
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
    public final java.lang.Object a() {
        /*
        r8 = this;
        r7 = 3;
        r0 = 0;
        r1 = r8.b;
        if (r1 != 0) goto L_0x0007;
    L_0x0006:
        return r0;
    L_0x0007:
        r1 = r8.b;
        r1 = r1.exists();
        if (r1 != 0) goto L_0x002b;
    L_0x000f:
        r1 = 5;
        r2 = a;
        r3 = new java.lang.StringBuilder;
        r4 = "No data to read for file:";
        r3.<init>(r4);
        r4 = r8.b;
        r4 = r4.getName();
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.flurry.sdk.iw.a(r1, r2, r3);
        goto L_0x0006;
    L_0x002b:
        r1 = 0;
        r2 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x005e, all -> 0x0080 }
        r3 = r8.b;	 Catch:{ Exception -> 0x005e, all -> 0x0080 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0080 }
        r3 = r8.c;	 Catch:{ Exception -> 0x0089 }
        r0 = r3.a(r2);	 Catch:{ Exception -> 0x0089 }
        com.flurry.sdk.lt.a(r2);
    L_0x003c:
        if (r1 == 0) goto L_0x0006;
    L_0x003e:
        r1 = a;
        r2 = new java.lang.StringBuilder;
        r3 = "Deleting data file:";
        r2.<init>(r3);
        r3 = r8.b;
        r3 = r3.getName();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.flurry.sdk.iw.a(r7, r1, r2);
        r1 = r8.b;
        r1.delete();
        goto L_0x0006;
    L_0x005e:
        r1 = move-exception;
        r2 = r0;
    L_0x0060:
        r3 = 3;
        r4 = a;	 Catch:{ all -> 0x0087 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0087 }
        r6 = "Error reading data file:";
        r5.<init>(r6);	 Catch:{ all -> 0x0087 }
        r6 = r8.b;	 Catch:{ all -> 0x0087 }
        r6 = r6.getName();	 Catch:{ all -> 0x0087 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0087 }
        r5 = r5.toString();	 Catch:{ all -> 0x0087 }
        com.flurry.sdk.iw.a(r3, r4, r5, r1);	 Catch:{ all -> 0x0087 }
        r1 = 1;
        com.flurry.sdk.lt.a(r2);
        goto L_0x003c;
    L_0x0080:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0083:
        com.flurry.sdk.lt.a(r2);
        throw r0;
    L_0x0087:
        r0 = move-exception;
        goto L_0x0083;
    L_0x0089:
        r1 = move-exception;
        goto L_0x0060;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.ii.a():java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A:{SYNTHETIC, RETURN} */
    public final void a(java.lang.Object r8) {
        /*
        r7 = this;
        r6 = 3;
        r0 = 0;
        r1 = 0;
        r2 = r7.b;	 Catch:{ Exception -> 0x0013 }
        r2 = com.flurry.sdk.ls.a(r2);	 Catch:{ Exception -> 0x0013 }
        if (r2 != 0) goto L_0x0055;
    L_0x000b:
        r0 = new java.io.IOException;	 Catch:{ Exception -> 0x0013 }
        r2 = "Cannot create parent directory!";
        r0.<init>(r2);	 Catch:{ Exception -> 0x0013 }
        throw r0;	 Catch:{ Exception -> 0x0013 }
    L_0x0013:
        r0 = move-exception;
    L_0x0014:
        r2 = 3;
        r3 = a;	 Catch:{ all -> 0x0065 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0065 }
        r5 = "Error writing data file:";
        r4.<init>(r5);	 Catch:{ all -> 0x0065 }
        r5 = r7.b;	 Catch:{ all -> 0x0065 }
        r5 = r5.getName();	 Catch:{ all -> 0x0065 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0065 }
        r4 = r4.toString();	 Catch:{ all -> 0x0065 }
        com.flurry.sdk.iw.a(r2, r3, r4, r0);	 Catch:{ all -> 0x0065 }
        r0 = 1;
        com.flurry.sdk.lt.a(r1);
    L_0x0033:
        if (r0 == 0) goto L_0x0054;
    L_0x0035:
        r0 = a;
        r1 = new java.lang.StringBuilder;
        r2 = "Deleting data file:";
        r1.<init>(r2);
        r2 = r7.b;
        r2 = r2.getName();
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.flurry.sdk.iw.a(r6, r0, r1);
        r0 = r7.b;
        r0.delete();
    L_0x0054:
        return;
    L_0x0055:
        r2 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0013 }
        r3 = r7.b;	 Catch:{ Exception -> 0x0013 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0013 }
        r1 = r7.c;	 Catch:{ Exception -> 0x006d, all -> 0x006a }
        r1.a(r2, r8);	 Catch:{ Exception -> 0x006d, all -> 0x006a }
        com.flurry.sdk.lt.a(r2);
        goto L_0x0033;
    L_0x0065:
        r0 = move-exception;
    L_0x0066:
        com.flurry.sdk.lt.a(r1);
        throw r0;
    L_0x006a:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0066;
    L_0x006d:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.ii.a(java.lang.Object):void");
    }

    public final boolean b() {
        return this.b != null && this.b.delete();
    }
}
