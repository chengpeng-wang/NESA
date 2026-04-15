package com.google.android.gms.internal;

import com.duapps.ad.AdError;
import com.google.android.gms.internal.zzb.zza;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.impl.cookie.DateUtils;

public class zzt implements zzf {
    protected static final boolean DEBUG = zzs.DEBUG;
    private static int zzao = AdError.TIME_OUT_CODE;
    private static int zzap = 4096;
    protected final zzy zzaq;
    protected final zzu zzar;

    public zzt(zzy zzy) {
        this(zzy, new zzu(zzap));
    }

    public zzt(zzy zzy, zzu zzu) {
        this.zzaq = zzy;
        this.zzar = zzu;
    }

    protected static Map<String, String> zza(Header[] headerArr) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private void zza(long j, zzk<?> zzk, byte[] bArr, StatusLine statusLine) {
        if (DEBUG || j > ((long) zzao)) {
            String str = "HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]";
            Object[] objArr = new Object[5];
            objArr[0] = zzk;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(zzk.zzu().zze());
            zzs.zzb(str, objArr);
        }
    }

    private static void zza(String str, zzk<?> zzk, zzr zzr) throws zzr {
        zzo zzu = zzk.zzu();
        int zzt = zzk.zzt();
        try {
            zzu.zza(zzr);
            zzk.zzc(String.format("%s-retry [timeout=%s]", new Object[]{str, Integer.valueOf(zzt)}));
        } catch (zzr e) {
            zzk.zzc(String.format("%s-timeout-giveup [timeout=%s]", new Object[]{str, Integer.valueOf(zzt)}));
            throw e;
        }
    }

    private void zza(Map<String, String> map, zza zza) {
        if (zza != null) {
            if (zza.zzb != null) {
                map.put("If-None-Match", zza.zzb);
            }
            if (zza.zzd > 0) {
                map.put("If-Modified-Since", DateUtils.formatDate(new Date(zza.zzd)));
            }
        }
    }

    private byte[] zza(HttpEntity httpEntity) throws IOException, zzp {
        zzaa zzaa = new zzaa(this.zzar, (int) httpEntity.getContentLength());
        byte[] bArr = null;
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new zzp();
            }
            bArr = this.zzar.zzb(1024);
            while (true) {
                int read = content.read(bArr);
                if (read == -1) {
                    break;
                }
                zzaa.write(bArr, 0, read);
            }
            byte[] toByteArray = zzaa.toByteArray();
            return toByteArray;
        } finally {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                zzs.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzar.zza(bArr);
            zzaa.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x008f A:{ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00af A:{ExcHandler: ConnectTimeoutException (e org.apache.http.conn.ConnectTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00be A:{ExcHandler: MalformedURLException (r1_6 'e' java.net.MalformedURLException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0122 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x008f A:{ExcHandler: SocketTimeoutException (e java.net.SocketTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00af A:{ExcHandler: ConnectTimeoutException (e org.apache.http.conn.ConnectTimeoutException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00be A:{ExcHandler: MalformedURLException (r1_6 'e' java.net.MalformedURLException), Splitter:B:2:0x000a} */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0122 A:{SYNTHETIC} */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:22:0x0090, code skipped:
            zza("socket", r17, new com.google.android.gms.internal.zzq());
     */
    /* JADX WARNING: Missing block: B:29:0x00b0, code skipped:
            zza("connection", r17, new com.google.android.gms.internal.zzq());
     */
    /* JADX WARNING: Missing block: B:30:0x00be, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:32:0x00db, code skipped:
            throw new java.lang.RuntimeException("Bad URL " + r17.getUrl(), r1);
     */
    /* JADX WARNING: Missing block: B:33:0x00dc, code skipped:
            r1 = e;
     */
    /* JADX WARNING: Missing block: B:34:0x00dd, code skipped:
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:37:0x00e1, code skipped:
            r2 = r2.getStatusLine().getStatusCode();
            com.google.android.gms.internal.zzs.zzc("Unexpected response code %d for %s", java.lang.Integer.valueOf(r2), r17.getUrl());
     */
    /* JADX WARNING: Missing block: B:38:0x00ff, code skipped:
            if (r3 != null) goto L_0x0101;
     */
    /* JADX WARNING: Missing block: B:39:0x0101, code skipped:
            r1 = new com.google.android.gms.internal.zzi(r2, r3, r4, false, android.os.SystemClock.elapsedRealtime() - r14);
     */
    /* JADX WARNING: Missing block: B:40:0x010e, code skipped:
            if (r2 == com.picksinit.ErrorInfo.ERROR_CODE_AD_DATA_IS_NULL) goto L_0x0114;
     */
    /* JADX WARNING: Missing block: B:43:0x0114, code skipped:
            zza("auth", r17, new com.google.android.gms.internal.zza(r1));
     */
    /* JADX WARNING: Missing block: B:45:0x0127, code skipped:
            throw new com.google.android.gms.internal.zzj(r1);
     */
    /* JADX WARNING: Missing block: B:47:0x012d, code skipped:
            throw new com.google.android.gms.internal.zzp(r1);
     */
    /* JADX WARNING: Missing block: B:49:0x0133, code skipped:
            throw new com.google.android.gms.internal.zzh(null);
     */
    /* JADX WARNING: Missing block: B:50:0x0134, code skipped:
            r1 = e;
     */
    /* JADX WARNING: Missing block: B:51:0x0135, code skipped:
            r3 = null;
            r2 = r13;
     */
    public com.google.android.gms.internal.zzi zza(com.google.android.gms.internal.zzk<?> r17) throws com.google.android.gms.internal.zzr {
        /*
        r16 = this;
        r14 = android.os.SystemClock.elapsedRealtime();
    L_0x0004:
        r2 = 0;
        r12 = 0;
        r4 = java.util.Collections.emptyMap();
        r1 = new java.util.HashMap;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r1.<init>();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r3 = r17.zzi();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r0 = r16;
        r0.zza(r1, r3);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r0 = r16;
        r3 = r0.zzaq;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r0 = r17;
        r13 = r3.zza(r0, r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x00dc }
        r10 = r13.getStatusLine();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r2 = r10.getStatusCode();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r1 = r13.getAllHeaders();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r4 = zza(r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r1 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        if (r2 != r1) goto L_0x0064;
    L_0x0036:
        r1 = r17.zzi();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        if (r1 != 0) goto L_0x004b;
    L_0x003c:
        r1 = new com.google.android.gms.internal.zzi;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r2 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r3 = 0;
        r5 = 1;
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r6 = r6 - r14;
        r1.m1621init(r2, r3, r4, r5, r6);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
    L_0x004a:
        return r1;
    L_0x004b:
        r2 = r1.zzg;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r2.putAll(r4);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r5 = new com.google.android.gms.internal.zzi;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r6 = 304; // 0x130 float:4.26E-43 double:1.5E-321;
        r7 = r1.data;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r8 = r1.zzg;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r9 = 1;
        r1 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r10 = r1 - r14;
        r5.m1621init(r6, r7, r8, r9, r10);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r1 = r5;
        goto L_0x004a;
    L_0x0064:
        r1 = r13.getEntity();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        if (r1 == 0) goto L_0x009e;
    L_0x006a:
        r1 = r13.getEntity();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        r0 = r16;
        r9 = r0.zza(r1);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
    L_0x0074:
        r5 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        r6 = r5 - r14;
        r5 = r16;
        r8 = r17;
        r5.zza(r6, r8, r9, r10);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r2 < r1) goto L_0x0089;
    L_0x0085:
        r1 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r2 <= r1) goto L_0x00a2;
    L_0x0089:
        r1 = new java.io.IOException;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        r1.<init>();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        throw r1;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
    L_0x008f:
        r1 = move-exception;
        r1 = "socket";
        r2 = new com.google.android.gms.internal.zzq;
        r2.m3750init();
        r0 = r17;
        zza(r1, r0, r2);
        goto L_0x0004;
    L_0x009e:
        r1 = 0;
        r9 = new byte[r1];	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0134 }
        goto L_0x0074;
    L_0x00a2:
        r1 = new com.google.android.gms.internal.zzi;	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        r5 = 0;
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        r6 = r6 - r14;
        r3 = r9;
        r1.m1621init(r2, r3, r4, r5, r6);	 Catch:{ SocketTimeoutException -> 0x008f, ConnectTimeoutException -> 0x00af, MalformedURLException -> 0x00be, IOException -> 0x0138 }
        goto L_0x004a;
    L_0x00af:
        r1 = move-exception;
        r1 = "connection";
        r2 = new com.google.android.gms.internal.zzq;
        r2.m3750init();
        r0 = r17;
        zza(r1, r0, r2);
        goto L_0x0004;
    L_0x00be:
        r1 = move-exception;
        r2 = new java.lang.RuntimeException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Bad URL ";
        r3 = r3.append(r4);
        r4 = r17.getUrl();
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3, r1);
        throw r2;
    L_0x00dc:
        r1 = move-exception;
        r3 = r12;
    L_0x00de:
        r5 = 0;
        if (r2 == 0) goto L_0x0122;
    L_0x00e1:
        r1 = r2.getStatusLine();
        r2 = r1.getStatusCode();
        r1 = "Unexpected response code %d for %s";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r7 = 0;
        r8 = java.lang.Integer.valueOf(r2);
        r6[r7] = r8;
        r7 = 1;
        r8 = r17.getUrl();
        r6[r7] = r8;
        com.google.android.gms.internal.zzs.zzc(r1, r6);
        if (r3 == 0) goto L_0x012e;
    L_0x0101:
        r1 = new com.google.android.gms.internal.zzi;
        r5 = 0;
        r6 = android.os.SystemClock.elapsedRealtime();
        r6 = r6 - r14;
        r1.m1621init(r2, r3, r4, r5, r6);
        r3 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        if (r2 == r3) goto L_0x0114;
    L_0x0110:
        r3 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        if (r2 != r3) goto L_0x0128;
    L_0x0114:
        r2 = "auth";
        r3 = new com.google.android.gms.internal.zza;
        r3.m3580init(r1);
        r0 = r17;
        zza(r2, r0, r3);
        goto L_0x0004;
    L_0x0122:
        r2 = new com.google.android.gms.internal.zzj;
        r2.m4665init(r1);
        throw r2;
    L_0x0128:
        r2 = new com.google.android.gms.internal.zzp;
        r2.m3749init(r1);
        throw r2;
    L_0x012e:
        r1 = new com.google.android.gms.internal.zzh;
        r1.m3722init(r5);
        throw r1;
    L_0x0134:
        r1 = move-exception;
        r3 = r12;
        r2 = r13;
        goto L_0x00de;
    L_0x0138:
        r1 = move-exception;
        r3 = r9;
        r2 = r13;
        goto L_0x00de;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzt.zza(com.google.android.gms.internal.zzk):com.google.android.gms.internal.zzi");
    }
}
