package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.b;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: BUGLY */
public class t {
    protected final int a = 10000;
    protected final int b = 30000;
    protected final int c = 2000;
    protected final int d = 2;
    protected final int e = 3;
    protected final long f = 10000;
    protected Context g;

    public t(Context context) {
        this.g = ag.a(context);
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x012b  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x012b  */
    public byte[] a(java.lang.String r16, byte[] r17, com.tencent.bugly.proguard.u r18, java.util.Map<java.lang.String, java.lang.String> r19) {
        /*
        r15 = this;
        if (r16 != 0) goto L_0x000c;
    L_0x0002:
        r2 = "rqdp{  no destUrl!}";
        r3 = 0;
        r3 = new java.lang.Object[r3];
        com.tencent.bugly.proguard.z.e(r2, r3);
        r2 = 0;
    L_0x000b:
        return r2;
    L_0x000c:
        r6 = 0;
        r5 = 0;
        if (r17 != 0) goto L_0x0057;
    L_0x0010:
        r2 = 0;
    L_0x0012:
        r4 = "req %s sz:%d";
        r7 = 2;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r7[r8] = r16;
        r8 = 1;
        r9 = java.lang.Long.valueOf(r2);
        r7[r8] = r9;
        com.tencent.bugly.proguard.z.c(r4, r7);
        r4 = "req %s sz:%d";
        r7 = 2;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r7[r8] = r16;
        r8 = 1;
        r9 = java.lang.Long.valueOf(r2);
        r7[r8] = r9;
        com.tencent.bugly.proguard.z.a(r4, r7);
        r4 = 0;
        r8 = r6;
        r7 = r16;
    L_0x003a:
        r6 = r8 + 1;
        r9 = 3;
        if (r8 >= r9) goto L_0x0141;
    L_0x003f:
        r8 = 2;
        if (r5 >= r8) goto L_0x0141;
    L_0x0042:
        if (r4 == 0) goto L_0x005c;
    L_0x0044:
        r4 = 0;
    L_0x0045:
        r8 = r15.g;
        r8 = com.tencent.bugly.crashreport.common.info.b.e(r8);
        if (r8 != 0) goto L_0x007e;
    L_0x004d:
        r8 = "req but network not avail,so drop!";
        r9 = 0;
        r9 = new java.lang.Object[r9];
        com.tencent.bugly.proguard.z.d(r8, r9);
        r8 = r6;
        goto L_0x003a;
    L_0x0057:
        r0 = r17;
        r2 = r0.length;
        r2 = (long) r2;
        goto L_0x0012;
    L_0x005c:
        r8 = 1;
        if (r6 <= r8) goto L_0x0045;
    L_0x005f:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "try time ";
        r8 = r8.append(r9);
        r8 = r8.append(r6);
        r8 = r8.toString();
        r9 = 0;
        r9 = new java.lang.Object[r9];
        com.tencent.bugly.proguard.z.c(r8, r9);
        r8 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r15.a(r8);
        goto L_0x0045;
    L_0x007e:
        if (r18 == 0) goto L_0x0085;
    L_0x0080:
        r0 = r18;
        r0.a(r7, r2, r8);
    L_0x0085:
        r0 = r17;
        r1 = r19;
        r10 = r15.a(r7, r0, r8, r1);
        if (r10 == 0) goto L_0x012f;
    L_0x008f:
        r11 = r10.getResponseCode();	 Catch:{ IOException -> 0x0124 }
        r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r11 != r8) goto L_0x00ac;
    L_0x0097:
        r8 = r15.a(r10);	 Catch:{ IOException -> 0x0124 }
        if (r18 == 0) goto L_0x00a6;
    L_0x009d:
        if (r8 != 0) goto L_0x00a9;
    L_0x009f:
        r9 = 0;
    L_0x00a1:
        r0 = r18;
        r0.a(r9);	 Catch:{ IOException -> 0x0124 }
    L_0x00a6:
        r2 = r8;
        goto L_0x000b;
    L_0x00a9:
        r9 = r8.length;	 Catch:{ IOException -> 0x0124 }
        r9 = (long) r9;	 Catch:{ IOException -> 0x0124 }
        goto L_0x00a1;
    L_0x00ac:
        r8 = r15.a(r11);	 Catch:{ IOException -> 0x0124 }
        if (r8 == 0) goto L_0x00f4;
    L_0x00b2:
        r8 = 1;
        r4 = "Location";
        r9 = r10.getHeaderField(r4);	 Catch:{ IOException -> 0x0144 }
        if (r9 != 0) goto L_0x00dd;
    L_0x00bb:
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0144 }
        r4.<init>();	 Catch:{ IOException -> 0x0144 }
        r9 = "rqdp{  redirect code:}";
        r4 = r4.append(r9);	 Catch:{ IOException -> 0x0144 }
        r4 = r4.append(r11);	 Catch:{ IOException -> 0x0144 }
        r9 = "rqdp{   Location is null! return}";
        r4 = r4.append(r9);	 Catch:{ IOException -> 0x0144 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0144 }
        r9 = 0;
        r9 = new java.lang.Object[r9];	 Catch:{ IOException -> 0x0144 }
        com.tencent.bugly.proguard.z.e(r4, r9);	 Catch:{ IOException -> 0x0144 }
        r2 = 0;
        goto L_0x000b;
    L_0x00dd:
        r5 = r5 + 1;
        r6 = 0;
        r4 = "redirect code: %d ,to:%s";
        r7 = 2;
        r7 = new java.lang.Object[r7];	 Catch:{ IOException -> 0x0149 }
        r12 = 0;
        r13 = java.lang.Integer.valueOf(r11);	 Catch:{ IOException -> 0x0149 }
        r7[r12] = r13;	 Catch:{ IOException -> 0x0149 }
        r12 = 1;
        r7[r12] = r9;	 Catch:{ IOException -> 0x0149 }
        com.tencent.bugly.proguard.z.c(r4, r7);	 Catch:{ IOException -> 0x0149 }
        r4 = r8;
        r7 = r9;
    L_0x00f4:
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0124 }
        r8.<init>();	 Catch:{ IOException -> 0x0124 }
        r9 = "response code ";
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x0124 }
        r8 = r8.append(r11);	 Catch:{ IOException -> 0x0124 }
        r8 = r8.toString();	 Catch:{ IOException -> 0x0124 }
        r9 = 0;
        r9 = new java.lang.Object[r9];	 Catch:{ IOException -> 0x0124 }
        com.tencent.bugly.proguard.z.d(r8, r9);	 Catch:{ IOException -> 0x0124 }
        r8 = r10.getContentLength();	 Catch:{ IOException -> 0x0124 }
        r8 = (long) r8;	 Catch:{ IOException -> 0x0124 }
        r10 = 0;
        r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r10 >= 0) goto L_0x011a;
    L_0x0118:
        r8 = 0;
    L_0x011a:
        if (r18 == 0) goto L_0x0121;
    L_0x011c:
        r0 = r18;
        r0.a(r8);	 Catch:{ IOException -> 0x0124 }
    L_0x0121:
        r8 = r6;
        goto L_0x003a;
    L_0x0124:
        r8 = move-exception;
    L_0x0125:
        r9 = com.tencent.bugly.proguard.z.a(r8);
        if (r9 != 0) goto L_0x0121;
    L_0x012b:
        r8.printStackTrace();
        goto L_0x0121;
    L_0x012f:
        r8 = "Failed to execute post.";
        r9 = 0;
        r9 = new java.lang.Object[r9];
        com.tencent.bugly.proguard.z.c(r8, r9);
        if (r18 == 0) goto L_0x0121;
    L_0x0139:
        r8 = 0;
        r0 = r18;
        r0.a(r8);
        goto L_0x0121;
    L_0x0141:
        r2 = 0;
        goto L_0x000b;
    L_0x0144:
        r4 = move-exception;
        r14 = r4;
        r4 = r8;
        r8 = r14;
        goto L_0x0125;
    L_0x0149:
        r4 = move-exception;
        r7 = r9;
        r14 = r8;
        r8 = r4;
        r4 = r14;
        goto L_0x0125;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.t.a(java.lang.String, byte[], com.tencent.bugly.proguard.u, java.util.Map):byte[]");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0049 A:{SYNTHETIC, Splitter:B:31:0x0049} */
    public byte[] a(java.net.HttpURLConnection r6) {
        /*
        r5 = this;
        r0 = 0;
        if (r6 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r2 = new java.io.BufferedInputStream;	 Catch:{ Throwable -> 0x0054, all -> 0x0044 }
        r1 = r6.getInputStream();	 Catch:{ Throwable -> 0x0054, all -> 0x0044 }
        r2.<init>(r1);	 Catch:{ Throwable -> 0x0054, all -> 0x0044 }
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x001d }
        r1.<init>();	 Catch:{ Throwable -> 0x001d }
    L_0x0012:
        r3 = r2.read();	 Catch:{ Throwable -> 0x001d }
        r4 = -1;
        if (r3 == r4) goto L_0x0032;
    L_0x0019:
        r1.write(r3);	 Catch:{ Throwable -> 0x001d }
        goto L_0x0012;
    L_0x001d:
        r1 = move-exception;
    L_0x001e:
        r3 = com.tencent.bugly.proguard.z.a(r1);	 Catch:{ all -> 0x0052 }
        if (r3 != 0) goto L_0x0027;
    L_0x0024:
        r1.printStackTrace();	 Catch:{ all -> 0x0052 }
    L_0x0027:
        if (r2 == 0) goto L_0x0003;
    L_0x0029:
        r2.close();	 Catch:{ Throwable -> 0x002d }
        goto L_0x0003;
    L_0x002d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0003;
    L_0x0032:
        r1.flush();	 Catch:{ Throwable -> 0x001d }
        r0 = r1.toByteArray();	 Catch:{ Throwable -> 0x001d }
        if (r2 == 0) goto L_0x0003;
    L_0x003b:
        r2.close();	 Catch:{ Throwable -> 0x003f }
        goto L_0x0003;
    L_0x003f:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x0003;
    L_0x0044:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0047:
        if (r2 == 0) goto L_0x004c;
    L_0x0049:
        r2.close();	 Catch:{ Throwable -> 0x004d }
    L_0x004c:
        throw r0;
    L_0x004d:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x004c;
    L_0x0052:
        r0 = move-exception;
        goto L_0x0047;
    L_0x0054:
        r1 = move-exception;
        r2 = r0;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.t.a(java.net.HttpURLConnection):byte[]");
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection a(String str, byte[] bArr, String str2, Map<String, String> map) {
        if (str == null) {
            z.e("destUrl is null.", new Object[0]);
            return null;
        }
        HttpURLConnection a = a(str2, str);
        if (a == null) {
            z.e("Failed to get HttpURLConnection object.", new Object[0]);
            return null;
        }
        try {
            a.setRequestProperty("wup_version", "3.0");
            if (map != null && map.size() > 0) {
                for (Entry entry : map.entrySet()) {
                    a.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
            }
            a.setRequestProperty("A37", URLEncoder.encode(str2, "utf-8"));
            a.setRequestProperty("A38", URLEncoder.encode(b.e(this.g), "utf-8"));
            a.connect();
            OutputStream outputStream = a.getOutputStream();
            if (bArr == null) {
                outputStream.write(0);
            } else {
                outputStream.write(bArr);
            }
            return a;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            z.e("Failed to upload crash, please check your network.", new Object[0]);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection a(String str, String str2) {
        try {
            HttpURLConnection httpURLConnection;
            URL url = new URL(str2);
            if (str == null || !str.toLowerCase(Locale.US).contains("wap")) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")))));
            }
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            return httpURLConnection;
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean a(int i) {
        return i == 301 || i == 302 || i == 303 || i == 307;
    }

    /* access modifiers changed from: protected */
    public void a(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
