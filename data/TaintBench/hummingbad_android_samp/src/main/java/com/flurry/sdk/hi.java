package com.flurry.sdk;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class hi {
    private static final String c = hi.class.getSimpleName();
    private static hi d;
    public final Map a = new HashMap();
    public hx b;
    private final Set e;
    private hm f = hm.NONE;
    private String g;
    private byte[] h;

    private hi() {
        HashSet hashSet = new HashSet();
        hashSet.add("null");
        hashSet.add("9774d56d682e549c");
        hashSet.add("dead00beef");
        this.e = Collections.unmodifiableSet(hashSet);
        hz.a.b(new hj(this));
    }

    public static synchronized hi a() {
        hi hiVar;
        synchronized (hi.class) {
            if (d == null) {
                d = new hi();
            }
            hiVar = d;
        }
        return hiVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c5 A:{Catch:{ Exception -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b8 A:{Catch:{ Exception -> 0x0054 }} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00d5 A:{Catch:{ Exception -> 0x0054 }} */
    static /* synthetic */ void a(com.flurry.sdk.hi r8) {
        /*
        r6 = 37;
    L_0x0002:
        r0 = com.flurry.sdk.hm.FINISHED;
        r1 = r8.f;
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0198;
    L_0x000c:
        r0 = com.flurry.sdk.hl.a;
        r1 = r8.f;
        r1 = r1.ordinal();
        r0 = r0[r1];
        switch(r0) {
            case 1: goto L_0x0077;
            case 2: goto L_0x007c;
            case 3: goto L_0x0081;
            case 4: goto L_0x0086;
            case 5: goto L_0x008b;
            default: goto L_0x0019;
        };
    L_0x0019:
        r0 = com.flurry.sdk.hl.a;	 Catch:{ Exception -> 0x0054 }
        r1 = r8.f;	 Catch:{ Exception -> 0x0054 }
        r1 = r1.ordinal();	 Catch:{ Exception -> 0x0054 }
        r0 = r0[r1];	 Catch:{ Exception -> 0x0054 }
        switch(r0) {
            case 2: goto L_0x0027;
            case 3: goto L_0x0090;
            case 4: goto L_0x0134;
            case 5: goto L_0x0193;
            default: goto L_0x0026;
        };	 Catch:{ Exception -> 0x0054 }
    L_0x0026:
        goto L_0x0002;
    L_0x0027:
        com.flurry.sdk.lt.b();	 Catch:{ Exception -> 0x0054 }
        r0 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0054 }
        r0 = com.flurry.sdk.lu.a(r0);	 Catch:{ Exception -> 0x0054 }
        if (r0 == 0) goto L_0x0002;
    L_0x0034:
        r0 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0054 }
        r0 = com.flurry.sdk.lu.b(r0);	 Catch:{ Exception -> 0x0054 }
        r8.b = r0;	 Catch:{ Exception -> 0x0054 }
        r0 = r8.b();	 Catch:{ Exception -> 0x0054 }
        if (r0 == 0) goto L_0x0002;
    L_0x0044:
        r8.e();	 Catch:{ Exception -> 0x0054 }
        r0 = new com.flurry.sdk.ho;	 Catch:{ Exception -> 0x0054 }
        r0.m3436init();	 Catch:{ Exception -> 0x0054 }
        r1 = com.flurry.sdk.il.a();	 Catch:{ Exception -> 0x0054 }
        r1.a(r0);	 Catch:{ Exception -> 0x0054 }
        goto L_0x0002;
    L_0x0054:
        r0 = move-exception;
        r1 = 4;
        r2 = c;
        r3 = new java.lang.StringBuilder;
        r4 = "Exception during id fetch:";
        r3.<init>(r4);
        r4 = r8.f;
        r3 = r3.append(r4);
        r4 = ", ";
        r3 = r3.append(r4);
        r0 = r3.append(r0);
        r0 = r0.toString();
        com.flurry.sdk.iw.a(r1, r2, r0);
        goto L_0x0002;
    L_0x0077:
        r0 = com.flurry.sdk.hm.ADVERTISING;
        r8.f = r0;
        goto L_0x0019;
    L_0x007c:
        r0 = com.flurry.sdk.hm.DEVICE;
        r8.f = r0;
        goto L_0x0019;
    L_0x0081:
        r0 = com.flurry.sdk.hm.HASHED_IMEI;
        r8.f = r0;
        goto L_0x0019;
    L_0x0086:
        r0 = com.flurry.sdk.hm.REPORTED_IDS;
        r8.f = r0;
        goto L_0x0019;
    L_0x008b:
        r0 = com.flurry.sdk.hm.FINISHED;
        r8.f = r0;
        goto L_0x0019;
    L_0x0090:
        com.flurry.sdk.lt.b();	 Catch:{ Exception -> 0x0054 }
        r0 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.getContentResolver();	 Catch:{ Exception -> 0x0054 }
        r1 = "android_id";
        r1 = android.provider.Settings.Secure.getString(r0, r1);	 Catch:{ Exception -> 0x0054 }
        r0 = android.text.TextUtils.isEmpty(r1);	 Catch:{ Exception -> 0x0054 }
        if (r0 != 0) goto L_0x00c3;
    L_0x00a7:
        r0 = java.util.Locale.US;	 Catch:{ Exception -> 0x0054 }
        r0 = r1.toLowerCase(r0);	 Catch:{ Exception -> 0x0054 }
        r2 = r8.e;	 Catch:{ Exception -> 0x0054 }
        r0 = r2.contains(r0);	 Catch:{ Exception -> 0x0054 }
        if (r0 != 0) goto L_0x00c3;
    L_0x00b5:
        r0 = 1;
    L_0x00b6:
        if (r0 != 0) goto L_0x00c5;
    L_0x00b8:
        r0 = 0;
    L_0x00b9:
        r1 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x0054 }
        if (r1 != 0) goto L_0x00d5;
    L_0x00bf:
        r8.g = r0;	 Catch:{ Exception -> 0x0054 }
        goto L_0x0002;
    L_0x00c3:
        r0 = 0;
        goto L_0x00b6;
    L_0x00c5:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0054 }
        r2 = "AND";
        r0.<init>(r2);	 Catch:{ Exception -> 0x0054 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0054 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0054 }
        goto L_0x00b9;
    L_0x00d5:
        r0 = c();	 Catch:{ Exception -> 0x0054 }
        r1 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x0054 }
        if (r1 == 0) goto L_0x00bf;
    L_0x00df:
        r0 = r8.d();	 Catch:{ Exception -> 0x0054 }
        r1 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x0054 }
        if (r1 == 0) goto L_0x011a;
    L_0x00e9:
        r0 = java.lang.Math.random();	 Catch:{ Exception -> 0x0054 }
        r0 = java.lang.Double.doubleToLongBits(r0);	 Catch:{ Exception -> 0x0054 }
        r2 = java.lang.System.nanoTime();	 Catch:{ Exception -> 0x0054 }
        r4 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r4 = r4.b;	 Catch:{ Exception -> 0x0054 }
        r4 = com.flurry.sdk.lq.a(r4);	 Catch:{ Exception -> 0x0054 }
        r4 = com.flurry.sdk.lt.f(r4);	 Catch:{ Exception -> 0x0054 }
        r4 = r4 * r6;
        r2 = r2 + r4;
        r2 = r2 * r6;
        r0 = r0 + r2;
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0054 }
        r3 = "ID";
        r2.<init>(r3);	 Catch:{ Exception -> 0x0054 }
        r3 = 16;
        r0 = java.lang.Long.toString(r0, r3);	 Catch:{ Exception -> 0x0054 }
        r0 = r2.append(r0);	 Catch:{ Exception -> 0x0054 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0054 }
    L_0x011a:
        r1 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x0054 }
        if (r1 != 0) goto L_0x00bf;
    L_0x0120:
        r1 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r1 = r1.b;	 Catch:{ Exception -> 0x0054 }
        r2 = ".flurryb.";
        r1 = r1.getFileStreamPath(r2);	 Catch:{ Exception -> 0x0054 }
        r2 = com.flurry.sdk.ls.a(r1);	 Catch:{ Exception -> 0x0054 }
        if (r2 == 0) goto L_0x00bf;
    L_0x0130:
        a(r0, r1);	 Catch:{ Exception -> 0x0054 }
        goto L_0x00bf;
    L_0x0134:
        r0 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0054 }
        r1 = "android.permission.READ_PHONE_STATE";
        r0 = r0.checkCallingOrSelfPermission(r1);	 Catch:{ Exception -> 0x0054 }
        if (r0 != 0) goto L_0x0002;
    L_0x0140:
        r0 = com.flurry.sdk.hz.a;	 Catch:{ Exception -> 0x0054 }
        r0 = r0.b;	 Catch:{ Exception -> 0x0054 }
        r1 = "phone";
        r0 = r0.getSystemService(r1);	 Catch:{ Exception -> 0x0054 }
        r0 = (android.telephony.TelephonyManager) r0;	 Catch:{ Exception -> 0x0054 }
        if (r0 == 0) goto L_0x0002;
    L_0x014e:
        r0 = r0.getDeviceId();	 Catch:{ Exception -> 0x0054 }
        if (r0 == 0) goto L_0x0002;
    L_0x0154:
        r1 = r0.trim();	 Catch:{ Exception -> 0x0054 }
        r1 = r1.length();	 Catch:{ Exception -> 0x0054 }
        if (r1 <= 0) goto L_0x0002;
    L_0x015e:
        r0 = com.flurry.sdk.lt.d(r0);	 Catch:{ Exception -> 0x016d }
        if (r0 == 0) goto L_0x0178;
    L_0x0164:
        r1 = r0.length;	 Catch:{ Exception -> 0x016d }
        r2 = 20;
        if (r1 != r2) goto L_0x0178;
    L_0x0169:
        r8.h = r0;	 Catch:{ Exception -> 0x016d }
        goto L_0x0002;
    L_0x016d:
        r0 = move-exception;
        r0 = 6;
        r1 = c;	 Catch:{ Exception -> 0x0054 }
        r2 = "Exception in generateHashedImei()";
        com.flurry.sdk.iw.a(r0, r1, r2);	 Catch:{ Exception -> 0x0054 }
        goto L_0x0002;
    L_0x0178:
        r1 = 6;
        r2 = c;	 Catch:{ Exception -> 0x016d }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x016d }
        r4 = "sha1 is not 20 bytes long: ";
        r3.<init>(r4);	 Catch:{ Exception -> 0x016d }
        r0 = java.util.Arrays.toString(r0);	 Catch:{ Exception -> 0x016d }
        r0 = r3.append(r0);	 Catch:{ Exception -> 0x016d }
        r0 = r0.toString();	 Catch:{ Exception -> 0x016d }
        com.flurry.sdk.iw.a(r1, r2, r0);	 Catch:{ Exception -> 0x016d }
        goto L_0x0002;
    L_0x0193:
        r8.e();	 Catch:{ Exception -> 0x0054 }
        goto L_0x0002;
    L_0x0198:
        r0 = new com.flurry.sdk.hn;
        r0.m3435init();
        r1 = com.flurry.sdk.il.a();
        r1.a(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flurry.sdk.hi.a(com.flurry.sdk.hi):void");
    }

    private static void a(String str, File file) {
        Throwable th;
        Closeable dataOutputStream;
        try {
            dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            try {
                dataOutputStream.writeInt(1);
                dataOutputStream.writeUTF(str);
                lt.a(dataOutputStream);
            } catch (Throwable th2) {
                th = th2;
                try {
                    iw.a(6, c, "Error when saving deviceId", th);
                    lt.a(dataOutputStream);
                } catch (Throwable th3) {
                    th = th3;
                    lt.a(dataOutputStream);
                    throw th;
                }
            }
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream = null;
            lt.a(dataOutputStream);
            throw th;
        }
    }

    private static String c() {
        Throwable th;
        Throwable th2;
        String str = null;
        File fileStreamPath = hz.a.b.getFileStreamPath(".flurryb.");
        if (fileStreamPath != null && fileStreamPath.exists()) {
            Closeable dataInputStream;
            try {
                dataInputStream = new DataInputStream(new FileInputStream(fileStreamPath));
                try {
                    if (1 == dataInputStream.readInt()) {
                        str = dataInputStream.readUTF();
                    }
                    lt.a(dataInputStream);
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Throwable th4) {
                dataInputStream = str;
                th2 = th4;
                lt.a(dataInputStream);
                throw th2;
            }
        }
        return str;
    }

    private String d() {
        Throwable th;
        Throwable th2;
        String str = null;
        File filesDir = hz.a.b.getFilesDir();
        if (filesDir != null) {
            String[] list = filesDir.list(new hk(this));
            if (!(list == null || list.length == 0)) {
                filesDir = hz.a.b.getFileStreamPath(list[0]);
                if (filesDir != null && filesDir.exists()) {
                    Closeable dataInputStream;
                    try {
                        dataInputStream = new DataInputStream(new FileInputStream(filesDir));
                        try {
                            if (46586 == dataInputStream.readUnsignedShort()) {
                                if (2 == dataInputStream.readUnsignedShort()) {
                                    dataInputStream.readUTF();
                                    str = dataInputStream.readUTF();
                                }
                            }
                            lt.a(dataInputStream);
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        dataInputStream = null;
                        th2 = th4;
                        lt.a(dataInputStream);
                        throw th2;
                    }
                }
            }
        }
        return str;
    }

    private void e() {
        String str = this.b == null ? null : this.b.a;
        if (str != null) {
            iw.a(3, c, "Fetched advertising id");
            this.a.put(hv.AndroidAdvertisingId, lt.c(str));
        }
        str = this.g;
        if (str != null) {
            iw.a(3, c, "Fetched device id");
            this.a.put(hv.DeviceId, lt.c(str));
        }
        byte[] bArr = this.h;
        if (bArr != null) {
            iw.a(3, c, "Fetched hashed IMEI");
            this.a.put(hv.Sha1Imei, bArr);
        }
    }

    public final boolean b() {
        return hm.FINISHED.equals(this.f);
    }
}
