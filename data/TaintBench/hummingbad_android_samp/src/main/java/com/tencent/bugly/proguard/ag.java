package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Base64;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/* compiled from: BUGLY */
public class ag {
    public static String a(String str) throws IllegalArgumentException {
        return new String(Base64.decode(str, 0));
    }

    public static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        try {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.getBuffer().toString();
        } catch (Throwable th2) {
            if (!z.a(th2)) {
                th2.printStackTrace();
            }
            return "fail";
        }
    }

    public static String a() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
    }

    public static String a(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
    }

    public static byte[] a(byte[] bArr, int i, String str) {
        if (bArr == null || i == -1) {
            return bArr;
        }
        z.c("rqdp{  enD:} %d %d", Integer.valueOf(bArr.length), Integer.valueOf(i));
        try {
            as a = ar.a(i);
            if (a == null) {
                return null;
            }
            a.a(str);
            return a.b(bArr);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] b(byte[] bArr, int i, String str) {
        if (bArr == null || i == -1) {
            return bArr;
        }
        try {
            as a = ar.a(i);
            if (a == null) {
                return null;
            }
            a.a(str);
            return a.a(bArr);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            z.d("encrytype %d %s", Integer.valueOf(i), str);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x0090 A:{SYNTHETIC, EDGE_INSN: B:48:0x0090->B:49:? ?: BREAK  , Splitter:B:48:0x0090} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0078 A:{Catch:{ Throwable -> 0x0056 }} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x009f A:{SYNTHETIC, Splitter:B:51:0x009f} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a4 A:{SYNTHETIC, Splitter:B:54:0x00a4} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004b A:{LOOP_START, LOOP:0: B:17:0x004b->B:20:0x0055, Catch:{ Throwable -> 0x0056 }} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0078 A:{Catch:{ Throwable -> 0x0056 }} */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0090 A:{SYNTHETIC, EDGE_INSN: B:48:0x0090->B:49:? ?: BREAK  , EDGE_INSN: B:48:0x0090->B:49:? ?: BREAK  , Splitter:B:48:0x0090} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x009f A:{SYNTHETIC, Splitter:B:51:0x009f} */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a4 A:{SYNTHETIC, Splitter:B:54:0x00a4} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005d A:{Catch:{ all -> 0x007d }} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0062 A:{SYNTHETIC, Splitter:B:27:0x0062} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0067 A:{SYNTHETIC, Splitter:B:30:0x0067} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0080 A:{SYNTHETIC, Splitter:B:41:0x0080} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0085 A:{SYNTHETIC, Splitter:B:44:0x0085} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005d A:{Catch:{ all -> 0x007d }} */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0062 A:{SYNTHETIC, Splitter:B:27:0x0062} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0067 A:{SYNTHETIC, Splitter:B:30:0x0067} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0080 A:{SYNTHETIC, Splitter:B:41:0x0080} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0085 A:{SYNTHETIC, Splitter:B:44:0x0085} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0080 A:{SYNTHETIC, Splitter:B:41:0x0080} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0085 A:{SYNTHETIC, Splitter:B:44:0x0085} */
    public static byte[] a(java.io.File r9, java.lang.String r10) {
        /*
        r0 = 0;
        r8 = 0;
        r1 = "rqdp{  ZF start}";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
        r1 = "buglyCacheLog.txt";
        if (r9 == 0) goto L_0x00e9;
    L_0x000d:
        r2 = r9.exists();	 Catch:{ Throwable -> 0x00db, all -> 0x00cd }
        if (r2 == 0) goto L_0x00e9;
    L_0x0013:
        r2 = r9.canRead();	 Catch:{ Throwable -> 0x00db, all -> 0x00cd }
        if (r2 == 0) goto L_0x00e9;
    L_0x0019:
        r2 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x00db, all -> 0x00cd }
        r2.<init>(r9);	 Catch:{ Throwable -> 0x00db, all -> 0x00cd }
        r1 = r9.getName();	 Catch:{ Throwable -> 0x00e0, all -> 0x00d2 }
        r3 = r2;
    L_0x0023:
        r2 = "UTF-8";
        r2 = r10.getBytes(r2);	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r4 = new java.io.ByteArrayInputStream;	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r4.<init>(r2);	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r5 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r5.<init>();	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r2 = new java.util.zip.ZipOutputStream;	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r2.<init>(r5);	 Catch:{ Throwable -> 0x00e5, all -> 0x00d7 }
        r6 = 8;
        r2.setMethod(r6);	 Catch:{ Throwable -> 0x0056 }
        r6 = new java.util.zip.ZipEntry;	 Catch:{ Throwable -> 0x0056 }
        r6.<init>(r1);	 Catch:{ Throwable -> 0x0056 }
        r2.putNextEntry(r6);	 Catch:{ Throwable -> 0x0056 }
        r1 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = new byte[r1];	 Catch:{ Throwable -> 0x0056 }
        if (r3 == 0) goto L_0x0072;
    L_0x004b:
        r6 = r3.read(r1);	 Catch:{ Throwable -> 0x0056 }
        if (r6 <= 0) goto L_0x0072;
    L_0x0051:
        r7 = 0;
        r2.write(r1, r7, r6);	 Catch:{ Throwable -> 0x0056 }
        goto L_0x004b;
    L_0x0056:
        r1 = move-exception;
    L_0x0057:
        r4 = com.tencent.bugly.proguard.z.a(r1);	 Catch:{ all -> 0x007d }
        if (r4 != 0) goto L_0x0060;
    L_0x005d:
        r1.printStackTrace();	 Catch:{ all -> 0x007d }
    L_0x0060:
        if (r3 == 0) goto L_0x0065;
    L_0x0062:
        r3.close();	 Catch:{ IOException -> 0x00b9 }
    L_0x0065:
        if (r2 == 0) goto L_0x006a;
    L_0x0067:
        r2.close();	 Catch:{ IOException -> 0x00be }
    L_0x006a:
        r1 = "rqdp{  ZF end}";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
    L_0x0071:
        return r0;
    L_0x0072:
        r6 = r4.read(r1);	 Catch:{ Throwable -> 0x0056 }
        if (r6 <= 0) goto L_0x0090;
    L_0x0078:
        r7 = 0;
        r2.write(r1, r7, r6);	 Catch:{ Throwable -> 0x0056 }
        goto L_0x0072;
    L_0x007d:
        r0 = move-exception;
    L_0x007e:
        if (r3 == 0) goto L_0x0083;
    L_0x0080:
        r3.close();	 Catch:{ IOException -> 0x00c3 }
    L_0x0083:
        if (r2 == 0) goto L_0x0088;
    L_0x0085:
        r2.close();	 Catch:{ IOException -> 0x00c8 }
    L_0x0088:
        r1 = "rqdp{  ZF end}";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
        throw r0;
    L_0x0090:
        r2.closeEntry();	 Catch:{ Throwable -> 0x0056 }
        r2.flush();	 Catch:{ Throwable -> 0x0056 }
        r2.finish();	 Catch:{ Throwable -> 0x0056 }
        r0 = r5.toByteArray();	 Catch:{ Throwable -> 0x0056 }
        if (r3 == 0) goto L_0x00a2;
    L_0x009f:
        r3.close();	 Catch:{ IOException -> 0x00af }
    L_0x00a2:
        if (r2 == 0) goto L_0x00a7;
    L_0x00a4:
        r2.close();	 Catch:{ IOException -> 0x00b4 }
    L_0x00a7:
        r1 = "rqdp{  ZF end}";
        r2 = new java.lang.Object[r8];
        com.tencent.bugly.proguard.z.c(r1, r2);
        goto L_0x0071;
    L_0x00af:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00a2;
    L_0x00b4:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x00a7;
    L_0x00b9:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0065;
    L_0x00be:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x006a;
    L_0x00c3:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0083;
    L_0x00c8:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0088;
    L_0x00cd:
        r1 = move-exception;
        r2 = r0;
        r3 = r0;
        r0 = r1;
        goto L_0x007e;
    L_0x00d2:
        r1 = move-exception;
        r3 = r2;
        r2 = r0;
        r0 = r1;
        goto L_0x007e;
    L_0x00d7:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
        goto L_0x007e;
    L_0x00db:
        r1 = move-exception;
        r2 = r0;
        r3 = r0;
        goto L_0x0057;
    L_0x00e0:
        r1 = move-exception;
        r3 = r2;
        r2 = r0;
        goto L_0x0057;
    L_0x00e5:
        r1 = move-exception;
        r2 = r0;
        goto L_0x0057;
    L_0x00e9:
        r3 = r0;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.ag.a(java.io.File, java.lang.String):byte[]");
    }

    public static byte[] a(byte[] bArr, int i) {
        if (bArr == null || i == -1) {
            return bArr;
        }
        z.c("rqdp{  zp:} %s rqdp{  len:} %s", Integer.valueOf(i), Integer.valueOf(bArr.length));
        try {
            am a = al.a(i);
            if (a == null) {
                return null;
            }
            return a.a(bArr);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] b(byte[] bArr, int i) {
        if (bArr == null || i == -1) {
            return bArr;
        }
        z.c("rqdp{  unzp:} %s rqdp{  len:} %s", Integer.valueOf(i), Integer.valueOf(bArr.length));
        try {
            am a = al.a(i);
            if (a == null) {
                return null;
            }
            return a.b(bArr);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] a(byte[] bArr, int i, int i2, String str) {
        byte[] bArr2 = null;
        if (bArr == null) {
            return bArr2;
        }
        try {
            return a(a(bArr, i), i2, str);
        } catch (Throwable th) {
            if (z.a(th)) {
                return bArr2;
            }
            th.printStackTrace();
            return bArr2;
        }
    }

    public static byte[] b(byte[] bArr, int i, int i2, String str) {
        try {
            return b(b(bArr, i2, str), i);
        } catch (Exception e) {
            if (!z.a(e)) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static long b() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return simpleDateFormat.parse(simpleDateFormat.format(new Date())).getTime();
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return -1;
        }
    }

    private static String c(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String str = "";
        for (byte b : bArr) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(toHexString);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static String a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "NULL";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(bArr);
            return c(instance.digest());
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3 A:{Catch:{ all -> 0x010e }} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a8 A:{SYNTHETIC, Splitter:B:49:0x00a8} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ad A:{SYNTHETIC, Splitter:B:52:0x00ad} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00ef A:{SYNTHETIC, Splitter:B:77:0x00ef} */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00f4 A:{SYNTHETIC, Splitter:B:80:0x00f4} */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a3 A:{Catch:{ all -> 0x010e }} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a8 A:{SYNTHETIC, Splitter:B:49:0x00a8} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ad A:{SYNTHETIC, Splitter:B:52:0x00ad} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00ef A:{SYNTHETIC, Splitter:B:77:0x00ef} */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00f4 A:{SYNTHETIC, Splitter:B:80:0x00f4} */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00ef A:{SYNTHETIC, Splitter:B:77:0x00ef} */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00f4 A:{SYNTHETIC, Splitter:B:80:0x00f4} */
    public static boolean a(java.io.File r6, java.io.File r7, int r8) {
        /*
        r3 = 0;
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r1 = 0;
        r0 = "rqdp{  ZF start}";
        r2 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.c(r0, r2);
        if (r6 == 0) goto L_0x0015;
    L_0x000d:
        if (r7 == 0) goto L_0x0015;
    L_0x000f:
        r0 = r6.equals(r7);
        if (r0 == 0) goto L_0x001e;
    L_0x0015:
        r0 = "rqdp{  err ZF 1R!}";
        r2 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.d(r0, r2);
        r0 = r1;
    L_0x001d:
        return r0;
    L_0x001e:
        r0 = r6.exists();
        if (r0 == 0) goto L_0x002a;
    L_0x0024:
        r0 = r6.canRead();
        if (r0 != 0) goto L_0x0033;
    L_0x002a:
        r0 = "rqdp{  !sFile.exists() || !sFile.canRead(),pls check ,return!}";
        r2 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.d(r0, r2);
        r0 = r1;
        goto L_0x001d;
    L_0x0033:
        r0 = r7.getParentFile();	 Catch:{ Throwable -> 0x0061 }
        if (r0 == 0) goto L_0x004a;
    L_0x0039:
        r0 = r7.getParentFile();	 Catch:{ Throwable -> 0x0061 }
        r0 = r0.exists();	 Catch:{ Throwable -> 0x0061 }
        if (r0 != 0) goto L_0x004a;
    L_0x0043:
        r0 = r7.getParentFile();	 Catch:{ Throwable -> 0x0061 }
        r0.mkdirs();	 Catch:{ Throwable -> 0x0061 }
    L_0x004a:
        r0 = r7.exists();	 Catch:{ Throwable -> 0x0061 }
        if (r0 != 0) goto L_0x0053;
    L_0x0050:
        r7.createNewFile();	 Catch:{ Throwable -> 0x0061 }
    L_0x0053:
        r0 = r7.exists();
        if (r0 == 0) goto L_0x005f;
    L_0x0059:
        r0 = r7.canRead();
        if (r0 != 0) goto L_0x006c;
    L_0x005f:
        r0 = r1;
        goto L_0x001d;
    L_0x0061:
        r0 = move-exception;
        r2 = com.tencent.bugly.proguard.z.a(r0);
        if (r2 != 0) goto L_0x0053;
    L_0x0068:
        r0.printStackTrace();
        goto L_0x0053;
    L_0x006c:
        r4 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0111, all -> 0x00ea }
        r4.<init>(r6);	 Catch:{ Throwable -> 0x0111, all -> 0x00ea }
        r2 = new java.util.zip.ZipOutputStream;	 Catch:{ Throwable -> 0x0114, all -> 0x0109 }
        r0 = new java.io.FileOutputStream;	 Catch:{ Throwable -> 0x0114, all -> 0x0109 }
        r0.<init>(r7);	 Catch:{ Throwable -> 0x0114, all -> 0x0109 }
        r2.<init>(r0);	 Catch:{ Throwable -> 0x0114, all -> 0x0109 }
        r0 = 8;
        r2.setMethod(r0);	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r0 = new java.util.zip.ZipEntry;	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r3 = r6.getName();	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r0.<init>(r3);	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r2.putNextEntry(r0);	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        if (r8 <= r5) goto L_0x00ba;
    L_0x008e:
        r0 = new byte[r8];	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
    L_0x0090:
        r3 = r4.read(r0);	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        if (r3 <= 0) goto L_0x00bc;
    L_0x0096:
        r5 = 0;
        r2.write(r0, r5, r3);	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        goto L_0x0090;
    L_0x009b:
        r0 = move-exception;
        r3 = r4;
    L_0x009d:
        r4 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x010e }
        if (r4 != 0) goto L_0x00a6;
    L_0x00a3:
        r0.printStackTrace();	 Catch:{ all -> 0x010e }
    L_0x00a6:
        if (r3 == 0) goto L_0x00ab;
    L_0x00a8:
        r3.close();	 Catch:{ IOException -> 0x00e0 }
    L_0x00ab:
        if (r2 == 0) goto L_0x00b0;
    L_0x00ad:
        r2.close();	 Catch:{ IOException -> 0x00e5 }
    L_0x00b0:
        r0 = "rqdp{  ZF end}";
        r2 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.c(r0, r2);
        r0 = r1;
        goto L_0x001d;
    L_0x00ba:
        r8 = r5;
        goto L_0x008e;
    L_0x00bc:
        r2.flush();	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r2.closeEntry();	 Catch:{ Throwable -> 0x009b, all -> 0x010c }
        r0 = 1;
        if (r4 == 0) goto L_0x00c8;
    L_0x00c5:
        r4.close();	 Catch:{ IOException -> 0x00d6 }
    L_0x00c8:
        if (r2 == 0) goto L_0x00cd;
    L_0x00ca:
        r2.close();	 Catch:{ IOException -> 0x00db }
    L_0x00cd:
        r2 = "rqdp{  ZF end}";
        r1 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.c(r2, r1);
        goto L_0x001d;
    L_0x00d6:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x00c8;
    L_0x00db:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x00cd;
    L_0x00e0:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x00ab;
    L_0x00e5:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x00b0;
    L_0x00ea:
        r0 = move-exception;
        r2 = r3;
        r4 = r3;
    L_0x00ed:
        if (r4 == 0) goto L_0x00f2;
    L_0x00ef:
        r4.close();	 Catch:{ IOException -> 0x00ff }
    L_0x00f2:
        if (r2 == 0) goto L_0x00f7;
    L_0x00f4:
        r2.close();	 Catch:{ IOException -> 0x0104 }
    L_0x00f7:
        r2 = "rqdp{  ZF end}";
        r1 = new java.lang.Object[r1];
        com.tencent.bugly.proguard.z.c(r2, r1);
        throw r0;
    L_0x00ff:
        r3 = move-exception;
        r3.printStackTrace();
        goto L_0x00f2;
    L_0x0104:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x00f7;
    L_0x0109:
        r0 = move-exception;
        r2 = r3;
        goto L_0x00ed;
    L_0x010c:
        r0 = move-exception;
        goto L_0x00ed;
    L_0x010e:
        r0 = move-exception;
        r4 = r3;
        goto L_0x00ed;
    L_0x0111:
        r0 = move-exception;
        r2 = r3;
        goto L_0x009d;
    L_0x0114:
        r0 = move-exception;
        r2 = r3;
        r3 = r4;
        goto L_0x009d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.ag.a(java.io.File, java.io.File, int):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048 A:{Catch:{ all -> 0x00a9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004d A:{SYNTHETIC, Splitter:B:17:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0052 A:{SYNTHETIC, Splitter:B:20:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0094 A:{SYNTHETIC, Splitter:B:48:0x0094} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0099 A:{SYNTHETIC, Splitter:B:51:0x0099} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048 A:{Catch:{ all -> 0x00a9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004d A:{SYNTHETIC, Splitter:B:17:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0052 A:{SYNTHETIC, Splitter:B:20:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0094 A:{SYNTHETIC, Splitter:B:48:0x0094} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0099 A:{SYNTHETIC, Splitter:B:51:0x0099} */
    public static java.util.ArrayList<java.lang.String> a(android.content.Context r6, java.lang.String[] r7) {
        /*
        r1 = 0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r2 = com.tencent.bugly.crashreport.common.info.a.a(r6);
        r2 = r2.P();
        if (r2 == 0) goto L_0x0020;
    L_0x0010:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = new java.lang.String;
        r2 = "unknown(low memory)";
        r1.<init>(r2);
        r0.add(r1);
    L_0x001f:
        return r0;
    L_0x0020:
        r2 = java.lang.Runtime.getRuntime();	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r4 = r2.exec(r7);	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r3 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r2 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r5 = r4.getInputStream();	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r2.<init>(r5);	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
        r3.<init>(r2);	 Catch:{ Throwable -> 0x00ac, all -> 0x0090 }
    L_0x0036:
        r2 = r3.readLine();	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        if (r2 == 0) goto L_0x0057;
    L_0x003c:
        r0.add(r2);	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        goto L_0x0036;
    L_0x0040:
        r0 = move-exception;
        r2 = r1;
    L_0x0042:
        r4 = com.tencent.bugly.proguard.z.a(r0);	 Catch:{ all -> 0x00a9 }
        if (r4 != 0) goto L_0x004b;
    L_0x0048:
        r0.printStackTrace();	 Catch:{ all -> 0x00a9 }
    L_0x004b:
        if (r3 == 0) goto L_0x0050;
    L_0x004d:
        r3.close();	 Catch:{ IOException -> 0x0086 }
    L_0x0050:
        if (r2 == 0) goto L_0x0055;
    L_0x0052:
        r2.close();	 Catch:{ IOException -> 0x008b }
    L_0x0055:
        r0 = r1;
        goto L_0x001f;
    L_0x0057:
        r2 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        r5 = new java.io.InputStreamReader;	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        r4 = r4.getErrorStream();	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        r5.<init>(r4);	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
        r2.<init>(r5);	 Catch:{ Throwable -> 0x0040, all -> 0x00a7 }
    L_0x0065:
        r4 = r2.readLine();	 Catch:{ Throwable -> 0x006f }
        if (r4 == 0) goto L_0x0071;
    L_0x006b:
        r0.add(r4);	 Catch:{ Throwable -> 0x006f }
        goto L_0x0065;
    L_0x006f:
        r0 = move-exception;
        goto L_0x0042;
    L_0x0071:
        if (r3 == 0) goto L_0x0076;
    L_0x0073:
        r3.close();	 Catch:{ IOException -> 0x0081 }
    L_0x0076:
        if (r2 == 0) goto L_0x001f;
    L_0x0078:
        r2.close();	 Catch:{ IOException -> 0x007c }
        goto L_0x001f;
    L_0x007c:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x001f;
    L_0x0081:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0076;
    L_0x0086:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0050;
    L_0x008b:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0055;
    L_0x0090:
        r0 = move-exception;
        r3 = r1;
    L_0x0092:
        if (r3 == 0) goto L_0x0097;
    L_0x0094:
        r3.close();	 Catch:{ IOException -> 0x009d }
    L_0x0097:
        if (r1 == 0) goto L_0x009c;
    L_0x0099:
        r1.close();	 Catch:{ IOException -> 0x00a2 }
    L_0x009c:
        throw r0;
    L_0x009d:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0097;
    L_0x00a2:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x009c;
    L_0x00a7:
        r0 = move-exception;
        goto L_0x0092;
    L_0x00a9:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0092;
    L_0x00ac:
        r0 = move-exception;
        r2 = r1;
        r3 = r1;
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.ag.a(android.content.Context, java.lang.String[]):java.util.ArrayList");
    }

    public static String a(Context context, String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        ArrayList a = a(context, new String[]{"/system/bin/sh", "-c", "getprop " + str});
        if (a == null || a.size() <= 0) {
            return "fail";
        }
        return (String) a.get(0);
    }

    public static void a(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void c() {
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    public static boolean b(String str) {
        if (str == null || str.trim().length() <= 0) {
            return true;
        }
        return false;
    }

    public static void c(String str) {
        if (str != null) {
            File file = new File(str);
            if (file.isFile() && file.exists() && file.canWrite()) {
                file.delete();
            }
        }
    }

    public static byte[] b(long j) {
        try {
            return ("" + j).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long b(byte[] bArr) {
        long j = -1;
        if (bArr == null && bArr.length == 0) {
            return j;
        }
        try {
            return Long.parseLong(new String(bArr, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return j;
        }
    }

    public static Context a(Context context) {
        if (context == null) {
            return context;
        }
        Context applicationContext = context.getApplicationContext();
        return applicationContext != null ? applicationContext : context;
    }

    public static String b(Throwable th) {
        if (th == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }
}
