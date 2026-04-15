package com.flurry.sdk;

import android.net.Uri;
import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import java.net.URLConnection;
import java.util.List;

public class ba {
    private static final String b = ba.class.getSimpleName();
    public at a;
    private long c;
    private long d;
    private bb e = bb.NONE;

    public static File a(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        File file = new File(hc.a(i), hc.b(str));
        return file.exists() ? file : null;
    }

    public static void a(af afVar) {
        if (afVar != null) {
            try {
                File a = hc.a(afVar.d());
                iw.a(3, b, "Precaching: Removing local assets for adObject:" + afVar.d());
                ls.b(a);
            } catch (Exception e) {
                iw.a(6, b, "Precaching: Error removing local assets for adObject:" + afVar.d() + " " + e.getMessage(), e);
            }
        }
    }

    private synchronized void a(bd bdVar) {
        ij bcVar = new bc();
        bcVar.a = bdVar;
        il.a().a(bcVar);
    }

    private boolean a(af afVar, String str) {
        boolean z = false;
        if (!d() || TextUtils.isEmpty(str)) {
            return false;
        }
        iw.a(3, b, "Precaching: Saving local asset for adObject:" + afVar.d());
        if (!bw.COMPLETE.equals(this.a.b(str))) {
            return false;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                String path = Uri.parse(str).getPath();
                if (!TextUtils.isEmpty(path)) {
                    path = URLConnection.guessContentTypeFromName(path);
                    if (path != null && path.startsWith("video")) {
                        z = true;
                    }
                }
            } catch (Exception e) {
                iw.a(3, b, "Error while getting mime type");
            }
        }
        return ((afVar instanceof ag) && z) ? true : b(afVar, str);
    }

    public static boolean a(ct ctVar, String str) {
        if (ctVar == null || TextUtils.isEmpty(str)) {
            return false;
        }
        List list = ctVar.a.d;
        for (int i = 0; i < list.size(); i++) {
            for (String equals : ctVar.b(i)) {
                if (equals.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean a(String str, long j) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        cr crVar = cr.UNKNOWN;
        String path = Uri.parse(str).getPath();
        if (TextUtils.isEmpty(path)) {
            iw.a(5, b, "Precaching: could not identify urlPath for asset: " + str);
        } else {
            String guessContentTypeFromName = URLConnection.guessContentTypeFromName(path);
            iw.a(3, b, "Precaching: assetLink: " + str + " urlPath: " + path + " mimeType: " + guessContentTypeFromName);
            if (guessContentTypeFromName != null) {
                if (guessContentTypeFromName.startsWith("video")) {
                    iw.a(3, b, "Precaching: asset is a video: " + str);
                    crVar = cr.VIDEO;
                } else if (guessContentTypeFromName.startsWith("image")) {
                    iw.a(3, b, "Precaching: asset is an image: " + str);
                    crVar = cr.IMAGE;
                } else if (guessContentTypeFromName.startsWith(MASTNativeAdConstants.RESPONSE_TEXT)) {
                    iw.a(3, b, "Precaching: asset is text: " + str);
                    crVar = cr.TEXT;
                } else {
                    iw.a(5, b, "Precaching: could not identify media type for asset: " + str);
                }
            }
        }
        return this.a.a(str, crVar, j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0047  */
    private boolean b(com.flurry.sdk.af r11, java.lang.String r12) {
        /*
        r10 = this;
        r3 = 1;
        r2 = 0;
        r6 = 3;
        r0 = 0;
        if (r11 == 0) goto L_0x000c;
    L_0x0006:
        r1 = android.text.TextUtils.isEmpty(r12);
        if (r1 == 0) goto L_0x000e;
    L_0x000c:
        r0 = r2;
    L_0x000d:
        return r0;
    L_0x000e:
        r1 = b;
        r4 = new java.lang.StringBuilder;
        r5 = "fAdIdQueue: Creating temp file for ";
        r4.<init>(r5);
        r5 = r11.d();
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.flurry.sdk.iw.a(r6, r1, r4);
        r4 = new java.io.File;
        r1 = r11.d();
        r1 = com.flurry.sdk.hc.a(r1);
        r5 = com.flurry.sdk.hc.b(r12);
        r4.<init>(r1, r5);
        r1 = r4.exists();	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        if (r1 == 0) goto L_0x004b;
    L_0x003d:
        r1 = r0;
    L_0x003e:
        com.flurry.sdk.lt.a(r0);
        com.flurry.sdk.lt.a(r1);
        r0 = r3;
    L_0x0045:
        if (r0 != 0) goto L_0x000d;
    L_0x0047:
        r4.delete();
        goto L_0x000d;
    L_0x004b:
        r1 = com.flurry.sdk.ls.a(r4);	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        if (r1 != 0) goto L_0x009a;
    L_0x0051:
        r1 = new java.io.IOException;	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r5 = "Precaching: Error creating directory to save tmp file:";
        r3.<init>(r5);	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r5 = r4.getAbsolutePath();	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r3 = r3.append(r5);	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r3 = r3.toString();	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r1.<init>(r3);	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        throw r1;	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
    L_0x006a:
        r1 = move-exception;
        r3 = r0;
        r9 = r0;
        r0 = r1;
        r1 = r9;
    L_0x006f:
        r5 = 6;
        r6 = b;	 Catch:{ all -> 0x0113 }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0113 }
        r8 = "Precaching: Error saving temp file for url:";
        r7.<init>(r8);	 Catch:{ all -> 0x0113 }
        r7 = r7.append(r12);	 Catch:{ all -> 0x0113 }
        r8 = " ";
        r7 = r7.append(r8);	 Catch:{ all -> 0x0113 }
        r8 = r0.getMessage();	 Catch:{ all -> 0x0113 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x0113 }
        r7 = r7.toString();	 Catch:{ all -> 0x0113 }
        com.flurry.sdk.iw.a(r5, r6, r7, r0);	 Catch:{ all -> 0x0113 }
        com.flurry.sdk.lt.a(r1);
        com.flurry.sdk.lt.a(r3);
        r0 = r2;
        goto L_0x0045;
    L_0x009a:
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x006a, all -> 0x0108 }
        r5 = r10.a;	 Catch:{ IOException -> 0x0115, all -> 0x010e }
        r6 = r5.a();	 Catch:{ IOException -> 0x0115, all -> 0x010e }
        if (r6 == 0) goto L_0x00d9;
    L_0x00a7:
        if (r0 == 0) goto L_0x00e6;
    L_0x00a9:
        r5 = r0.a;	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        com.flurry.sdk.lt.a(r5, r1);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r5 = 3;
        r6 = b;	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r8 = "Precaching: Temp asset ";
        r7.<init>(r8);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = r7.append(r12);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r8 = " saved to :";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r8 = r4.getAbsolutePath();	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = r7.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        com.flurry.sdk.iw.a(r5, r6, r7);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        goto L_0x003e;
    L_0x00d3:
        r3 = move-exception;
        r9 = r3;
        r3 = r1;
        r1 = r0;
        r0 = r9;
        goto L_0x006f;
    L_0x00d9:
        r6 = android.text.TextUtils.isEmpty(r12);	 Catch:{ IOException -> 0x0115, all -> 0x010e }
        if (r6 != 0) goto L_0x00a7;
    L_0x00df:
        r5 = r5.a;	 Catch:{ IOException -> 0x0115, all -> 0x010e }
        r0 = r5.a(r12);	 Catch:{ IOException -> 0x0115, all -> 0x010e }
        goto L_0x00a7;
    L_0x00e6:
        r5 = 3;
        r6 = b;	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r8 = "Precaching: Temp asset not saved.  Could not open cache reader: ";
        r7.<init>(r8);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = r7.append(r12);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        r7 = r7.toString();	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        com.flurry.sdk.iw.a(r5, r6, r7);	 Catch:{ IOException -> 0x00d3, all -> 0x00fd }
        goto L_0x003e;
    L_0x00fd:
        r2 = move-exception;
        r3 = r1;
        r1 = r0;
        r0 = r2;
    L_0x0101:
        com.flurry.sdk.lt.a(r1);
        com.flurry.sdk.lt.a(r3);
        throw r0;
    L_0x0108:
        r1 = move-exception;
        r3 = r0;
        r9 = r0;
        r0 = r1;
        r1 = r9;
        goto L_0x0101;
    L_0x010e:
        r2 = move-exception;
        r3 = r1;
        r1 = r0;
        r0 = r2;
        goto L_0x0101;
    L_0x0113:
        r0 = move-exception;
        goto L_0x0101;
    L_0x0115:
        r3 = move-exception;
        r9 = r3;
        r3 = r1;
        r1 = r0;
        r0 = r9;
        goto L_0x006f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.ba.b(com.flurry.sdk.af, java.lang.String):boolean");
    }

    private boolean d() {
        return bb.ACTIVE.equals(this.e) || bb.PAUSED.equals(this.e);
    }

    public final bk a(ct ctVar) {
        if (!d()) {
            return bk.ERROR;
        }
        if (ctVar == null) {
            return bk.ERROR;
        }
        if (ctVar.a == null) {
            return bk.COMPLETE;
        }
        bk bkVar = bk.NOT_STARTED;
        List list = ctVar.a.d;
        Object obj = null;
        bk bkVar2 = bkVar;
        int i = 0;
        bk bkVar3 = bkVar2;
        while (i < list.size()) {
            Object obj2 = obj;
            bk bkVar4 = bkVar3;
            for (String b : ctVar.b(i)) {
                bw b2 = this.a.b(b);
                if (bkVar4 == null) {
                    bkVar4 = bk.NOT_STARTED;
                } else if (b2 != null) {
                    if (bw.ERROR.equals(b2)) {
                        bkVar4 = bk.ERROR;
                    } else if (bw.EVICTED.equals(b2)) {
                        if (!bkVar4.equals(bk.ERROR)) {
                            bkVar4 = bk.EVICTED;
                        }
                    } else if (bw.NONE.equals(b2) || bw.CANCELLED.equals(b2)) {
                        if (!(bkVar4.equals(bk.ERROR) || bkVar4.equals(bk.EVICTED))) {
                            bkVar4 = bk.INCOMPLETE;
                        }
                    } else if (bw.QUEUED.equals(b2) || bw.IN_PROGRESS.equals(b2)) {
                        if (bk.NOT_STARTED.equals(bkVar4) || bk.COMPLETE.equals(bkVar4)) {
                            bkVar4 = bk.IN_PROGRESS;
                        }
                    } else if (bw.COMPLETE.equals(b2) && bk.NOT_STARTED.equals(bkVar4)) {
                        bkVar4 = bk.COMPLETE;
                    }
                }
                obj2 = 1;
            }
            i++;
            bkVar3 = bkVar4;
            obj = obj2;
        }
        return obj == null ? bk.COMPLETE : bkVar3;
    }

    public final synchronized void a(long j, long j2) {
        if (!a()) {
            iw.a(3, b, "Precaching: Initializing AssetCacheManager.");
            this.c = j;
            this.d = j2;
            try {
                File file = new File(ls.a().getPath() + File.separator + ".fcaches" + File.separator + "fileStreamCacheDownloaderTmp");
                iw.a(3, b, "Precaching: Cleaning temp asset directory: " + file);
                ls.b(file);
            } catch (Exception e) {
                iw.a(6, b, "Precaching: Error cleaning temp asset directory: " + e.getMessage(), e);
            }
            this.e = bb.INIT;
            a(bd.INIT);
        }
        return;
    }

    public final void a(List list) {
        if (d() && list != null) {
            ct ctVar;
            for (int size = list.size() - 1; size >= 0; size--) {
                ctVar = (ct) list.get(size);
                if (d() && ctVar != null) {
                    List list2 = ctVar.a.d;
                    for (int i = 0; i < list2.size(); i++) {
                        for (String str : ctVar.b(i)) {
                            if (!TextUtils.isEmpty(str)) {
                                this.a.b(str);
                            }
                        }
                    }
                }
            }
            int i2 = 0;
            for (ct ctVar2 : list) {
                int i3 = (b(ctVar2) > 0 ? 1 : 0) + i2;
                if (i3 < 2) {
                    i2 = i3;
                } else {
                    return;
                }
            }
        }
    }

    public final boolean a() {
        return !bb.NONE.equals(this.e);
    }

    public final boolean a(af afVar, ct ctVar) {
        if (!d() || afVar == null || ctVar == null) {
            return false;
        }
        iw.a(3, b, "Precaching: Saving local assets for adObject:" + afVar.d());
        List list = ctVar.a.d;
        for (int i = 0; i < list.size(); i++) {
            for (String a : ctVar.b(i)) {
                if (!a(afVar, a)) {
                    return false;
                }
            }
        }
        return true;
    }

    public final int b(ct ctVar) {
        if (!d() || ctVar == null) {
            return 0;
        }
        List list = ctVar.a.d;
        int i = 0;
        int i2 = 0;
        while (i < list.size()) {
            dq dqVar = (dq) list.get(i);
            int i3 = i2;
            for (String a : ctVar.b(i)) {
                if (a(a, dqVar.h)) {
                    i3++;
                }
            }
            i++;
            i2 = i3;
        }
        return i2;
    }

    public final synchronized void b() {
        if (a()) {
            if (!d()) {
                iw.a(3, b, "Precaching: Starting AssetCacheManager.");
                this.a = new at(this.c);
                this.a.b();
                this.e = bb.ACTIVE;
                a(bd.START);
            }
        }
    }

    public final synchronized void c() {
        if (d()) {
            if (bb.PAUSED.equals(this.e)) {
                iw.a(3, b, "Precaching: Resuming AssetCacheManager.");
                this.a.c();
                this.e = bb.ACTIVE;
                a(bd.RESUME);
            }
        }
    }

    public final void c(ct ctVar) {
        if (d() && ctVar != null) {
            List list = ctVar.a.d;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < list.size()) {
                    dq dqVar = (dq) list.get(i2);
                    for (String str : ctVar.b(i2)) {
                        if (a(str, dqVar.h) && d()) {
                            this.a.a(str);
                        }
                    }
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        }
    }
}
