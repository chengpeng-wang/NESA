package com.duapps.ad.stats;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Base64;
import com.duapps.ad.base.d;
import com.duapps.ad.base.e;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.duapps.ad.base.t;
import com.duapps.ad.base.w;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.Defaults;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.mopub.volley.BuildConfig;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

public final class ToolStatsCore implements Callback {
    private static ToolStatsCore b;
    /* access modifiers changed from: private|static */
    public static String c = "http://api.mobula.sdk.duapps.com/adunion/slot/getSrcPrio?";
    private static String d = "http://rts.mobula.sdk.duapps.com/orts/rp?";
    private static String e = "http://rts.mobula.sdk.duapps.com/orts/rpb?";
    private int a = 0;
    /* access modifiers changed from: private */
    public Context f;
    private Handler g;

    public static synchronized void a(Context context) {
        synchronized (ToolStatsCore.class) {
            if (b == null) {
                ReentrantLock reentrantLock = new ReentrantLock();
                try {
                    reentrantLock.lock();
                    if (b == null) {
                        b = new ToolStatsCore(context.getApplicationContext());
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
    }

    public static ToolStatsCore getInstance(Context context) {
        a(context);
        return b;
    }

    private ToolStatsCore(Context context) {
        long j = 0;
        this.f = context;
        HandlerThread handlerThread = new HandlerThread("stts", 10);
        handlerThread.start();
        this.g = new Handler(handlerThread.getLooper(), this);
        this.g.sendEmptyMessage(4);
        long d = i.d(this.f);
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - d;
        if (j2 < 0) {
            i.c(this.f);
            return;
        }
        if (j2 <= 21600000) {
            j = (currentTimeMillis + 21600000) - d;
        }
        this.g.sendEmptyMessageDelayed(5, j);
    }

    public final void reportEvent(String str, String str2, int i) {
        Context context = this.f;
        String a = t.a();
        if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(str2)) {
            if (f.a()) {
                f.c("ToolStatsCore", "rept = " + str2);
            }
            ContentValues contentValues = new ContentValues(3);
            contentValues.put("ts", Long.valueOf(System.currentTimeMillis()));
            contentValues.put("content", Base64.encode(str2.getBytes(), 3));
            contentValues.put("ls", a);
            contentValues.put("stype", str);
            try {
                this.f.getContentResolver().insert(DuAdCacheProvider.a(this.f, 4), contentValues);
            } catch (Exception e) {
                f.a("ToolStatsCore", "mDatabase reportEvent() exception: ", e);
            } catch (Throwable th) {
                f.a("ToolStatsCore", "mDatabase reportEvent() exception: ", th);
            }
            if (i == 0) {
                this.g.sendEmptyMessageDelayed(3, 5000);
            }
        } else if (f.a()) {
            f.c("ToolStatsCore", "Discard ls=" + a + ";rp=" + str2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x012c  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01db  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x012c  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01db  */
    public final boolean handleMessage(android.os.Message r17) {
        /*
        r16 = this;
        r0 = r17;
        r1 = r0.what;
        switch(r1) {
            case 3: goto L_0x0009;
            case 4: goto L_0x01e2;
            case 5: goto L_0x0200;
            default: goto L_0x0007;
        };
    L_0x0007:
        r1 = 0;
    L_0x0008:
        return r1;
    L_0x0009:
        r0 = r16;
        r1 = r0.g;
        r2 = 3;
        r1.removeMessages(r2);
        r1 = java.lang.System.currentTimeMillis();
        r11 = 0;
        r10 = 0;
        r9 = 0;
        r8 = 0;
        r12 = new org.json.JSONStringer;
        r12.<init>();
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r3 = 604800000; // 0x240c8400 float:3.046947E-17 double:2.988109026E-315;
        r14 = r1 - r3;
        r4 = "ts > ?";
        r1 = 1;
        r5 = new java.lang.String[r1];
        r1 = 0;
        r2 = java.lang.String.valueOf(r14);
        r5[r1] = r2;
        r1 = 5;
        r3 = new java.lang.String[r1];
        r1 = 0;
        r2 = "ts";
        r3[r1] = r2;
        r1 = 1;
        r2 = "content";
        r3[r1] = r2;
        r1 = 2;
        r2 = "ls";
        r3[r1] = r2;
        r1 = 3;
        r2 = "stype";
        r3[r1] = r2;
        r1 = 4;
        r2 = "_id";
        r3[r1] = r2;
        r7 = 0;
        r0 = r16;
        r1 = r0.f;	 Catch:{ Exception -> 0x01a7, all -> 0x01c3 }
        r1 = r1.getContentResolver();	 Catch:{ Exception -> 0x01a7, all -> 0x01c3 }
        r0 = r16;
        r2 = r0.f;	 Catch:{ Exception -> 0x01a7, all -> 0x01c3 }
        r6 = 4;
        r2 = com.duapps.ad.stats.DuAdCacheProvider.a(r2, r6);	 Catch:{ Exception -> 0x01a7, all -> 0x01c3 }
        r6 = "ts DESC LIMIT 100 OFFSET 0";
        r2 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x01a7, all -> 0x01c3 }
        if (r2 != 0) goto L_0x0091;
    L_0x006a:
        if (r2 == 0) goto L_0x0075;
    L_0x006c:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x0075;
    L_0x0072:
        r2.close();
    L_0x0075:
        r0 = r16;
        r1 = r0.a;
        r1 = r1 + 1;
        r0 = r16;
        r0.a = r1;
        r1 = 3;
        r0 = r16;
        r2 = r0.a;
        if (r1 <= r2) goto L_0x01db;
    L_0x0086:
        r0 = r16;
        r1 = r0.g;
        r2 = 3;
        r1.sendEmptyMessage(r2);
    L_0x008e:
        r1 = 1;
        goto L_0x0008;
    L_0x0091:
        r1 = r2.getCount();	 Catch:{ Exception -> 0x0230 }
        if (r1 > 0) goto L_0x00a6;
    L_0x0097:
        r2.close();	 Catch:{ Exception -> 0x0230 }
        if (r2 == 0) goto L_0x0075;
    L_0x009c:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x0075;
    L_0x00a2:
        r2.close();
        goto L_0x0075;
    L_0x00a6:
        r12.array();	 Catch:{ Exception -> 0x0230 }
        r3 = r8;
        r4 = r9;
        r5 = r10;
    L_0x00ac:
        r1 = r2.moveToNext();	 Catch:{ Exception -> 0x0236 }
        if (r1 == 0) goto L_0x00f5;
    L_0x00b2:
        r1 = 0;
        r7 = r2.getLong(r1);	 Catch:{ Exception -> 0x0236 }
        r1 = 1;
        r9 = r2.getBlob(r1);	 Catch:{ Exception -> 0x0236 }
        r1 = 2;
        r1 = r2.getString(r1);	 Catch:{ Exception -> 0x0236 }
        r6 = 3;
        r6 = r2.getString(r6);	 Catch:{ Exception -> 0x0236 }
        if (r4 != 0) goto L_0x00c9;
    L_0x00c8:
        r4 = r1;
    L_0x00c9:
        if (r3 != 0) goto L_0x00cc;
    L_0x00cb:
        r3 = r6;
    L_0x00cc:
        r1 = r4.equals(r1);	 Catch:{ Exception -> 0x0236 }
        if (r1 == 0) goto L_0x00ac;
    L_0x00d2:
        r1 = r3.equals(r6);	 Catch:{ Exception -> 0x0236 }
        if (r1 == 0) goto L_0x00ac;
    L_0x00d8:
        r1 = new java.lang.String;	 Catch:{ Exception -> 0x0236 }
        r6 = 3;
        r6 = android.util.Base64.decode(r9, r6);	 Catch:{ Exception -> 0x0236 }
        r1.<init>(r6);	 Catch:{ Exception -> 0x0236 }
        r6 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0236 }
        r6.<init>(r1);	 Catch:{ Exception -> 0x0236 }
        r12.value(r6);	 Catch:{ Exception -> 0x0236 }
        r1 = r13.append(r7);	 Catch:{ Exception -> 0x0236 }
        r6 = ",";
        r1.append(r6);	 Catch:{ Exception -> 0x0236 }
        r5 = 1;
        goto L_0x00ac;
    L_0x00f5:
        r12.endArray();	 Catch:{ Exception -> 0x0236 }
        if (r2 == 0) goto L_0x023d;
    L_0x00fa:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x023d;
    L_0x0100:
        r2.close();
        r2 = r4;
        r1 = r11;
    L_0x0105:
        if (r5 == 0) goto L_0x0075;
    L_0x0107:
        if (r1 != 0) goto L_0x0075;
    L_0x0109:
        if (r2 == 0) goto L_0x0075;
    L_0x010b:
        if (r3 == 0) goto L_0x0075;
    L_0x010d:
        r1 = r13.length();
        r1 = r1 + -1;
        r13.deleteCharAt(r1);
        r4 = r12.toString();
        r0 = r16;
        r1 = r0.f;
        r5 = "seq";
        r5 = com.duapps.ad.base.i.a(r1, r5);
        r1 = r16;
        r1 = r1.a(r2, r3, r4, r5);
        if (r1 == 0) goto L_0x0075;
    L_0x012c:
        r1 = "ts <= ? ";
        r2 = 1;
        r2 = new java.lang.String[r2];
        r3 = 0;
        r4 = java.lang.String.valueOf(r14);
        r2[r3] = r4;
        r0 = r16;
        r3 = r0.f;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r3 = r3.getContentResolver();	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r0 = r16;
        r4 = r0.f;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r7 = 4;
        r4 = com.duapps.ad.stats.DuAdCacheProvider.a(r4, r7);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r3.delete(r4, r1, r2);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = "ts IN (";
        r1.<init>(r2);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = r13.toString();	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = ")";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r0 = r16;
        r2 = r0.f;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = r2.getContentResolver();	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r0 = r16;
        r3 = r0.f;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r4 = 4;
        r3 = com.duapps.ad.stats.DuAdCacheProvider.a(r3, r4);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r4 = 0;
        r1 = r2.delete(r3, r1, r4);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = "ToolStatsCore";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r4 = "del srecord success :";
        r3.<init>(r4);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r1 = r3.append(r1);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        com.duapps.ad.base.f.c(r2, r1);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r0 = r16;
        r1 = r0.f;	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        r2 = "seq";
        r3 = 1;
        r3 = r3 + r5;
        com.duapps.ad.base.i.a(r1, r2, r3);	 Catch:{ Exception -> 0x019d, Throwable -> 0x01d1 }
        goto L_0x0075;
    L_0x019d:
        r1 = move-exception;
        r2 = "ToolStatsCore";
        r3 = "mDatabase reportEvent() del exception: ";
        com.duapps.ad.base.f.a(r2, r3, r1);
        goto L_0x0075;
    L_0x01a7:
        r1 = move-exception;
        r2 = r7;
        r3 = r8;
        r4 = r9;
        r5 = r10;
    L_0x01ac:
        r6 = 1;
        r7 = "ToolStatsCore";
        r8 = "mDatabase reportEvent() exception: ";
        com.duapps.ad.base.f.a(r7, r8, r1);	 Catch:{ all -> 0x022e }
        if (r2 == 0) goto L_0x0239;
    L_0x01b6:
        r1 = r2.isClosed();
        if (r1 != 0) goto L_0x0239;
    L_0x01bc:
        r2.close();
        r2 = r4;
        r1 = r6;
        goto L_0x0105;
    L_0x01c3:
        r1 = move-exception;
        r2 = r7;
    L_0x01c5:
        if (r2 == 0) goto L_0x01d0;
    L_0x01c7:
        r3 = r2.isClosed();
        if (r3 != 0) goto L_0x01d0;
    L_0x01cd:
        r2.close();
    L_0x01d0:
        throw r1;
    L_0x01d1:
        r1 = move-exception;
        r2 = "ToolStatsCore";
        r3 = "mDatabase reportEvent() del exception: ";
        com.duapps.ad.base.f.a(r2, r3, r1);
        goto L_0x0075;
    L_0x01db:
        r1 = 0;
        r0 = r16;
        r0.a = r1;
        goto L_0x008e;
    L_0x01e2:
        r0 = r16;
        r1 = r0.g;
        r2 = 4;
        r1.removeMessages(r2);
        r0 = r16;
        r1 = r0.g;
        r2 = 3;
        r1.sendEmptyMessage(r2);
        r0 = r16;
        r1 = r0.g;
        r2 = 4;
        r3 = 3600000; // 0x36ee80 float:5.044674E-39 double:1.7786363E-317;
        r1.sendEmptyMessageDelayed(r2, r3);
        r1 = 1;
        goto L_0x0008;
    L_0x0200:
        r0 = r16;
        r1 = r0.g;
        r2 = 5;
        r1.removeMessages(r2);
        r0 = r16;
        r1 = r0.f;
        r1 = com.duapps.ad.base.d.b(r1);
        if (r1 == 0) goto L_0x0220;
    L_0x0212:
        r1 = com.duapps.ad.base.y.a();
        r2 = new com.duapps.ad.stats.k;
        r0 = r16;
        r2.m741init(r0);
        r1.a(r2);
    L_0x0220:
        r0 = r16;
        r1 = r0.g;
        r2 = 5;
        r3 = 21600000; // 0x1499700 float:3.7026207E-38 double:1.0671818E-316;
        r1.sendEmptyMessageDelayed(r2, r3);
        r1 = 1;
        goto L_0x0008;
    L_0x022e:
        r1 = move-exception;
        goto L_0x01c5;
    L_0x0230:
        r1 = move-exception;
        r3 = r8;
        r4 = r9;
        r5 = r10;
        goto L_0x01ac;
    L_0x0236:
        r1 = move-exception;
        goto L_0x01ac;
    L_0x0239:
        r2 = r4;
        r1 = r6;
        goto L_0x0105;
    L_0x023d:
        r2 = r4;
        r1 = r11;
        goto L_0x0105;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.duapps.ad.stats.ToolStatsCore.handleMessage(android.os.Message):boolean");
    }

    public final boolean a(String str, String str2, String str3) {
        HttpResponse httpResponse = null;
        long a = i.a(this.f, "seq");
        List a2 = e.a(this.f, str);
        String valueOf = String.valueOf(System.currentTimeMillis());
        a2.add(new BasicNameValuePair("mdu", "adsdk"));
        a2.add(new BasicNameValuePair("rv", BuildConfig.VERSION_NAME));
        a2.add(new BasicNameValuePair("ts", valueOf));
        a2.add(new BasicNameValuePair("seq", Long.toString(a)));
        a2.add(new BasicNameValuePair("stype", str2));
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str3.getBytes());
            instance.update(valueOf.getBytes());
            instance.update(str.getBytes());
            a2.add(new BasicNameValuePair("s", d.a(instance.digest())));
            try {
                httpResponse = w.a(new URI(e + URLEncodedUtils.format(a2, Defaults.ENCODING_UTF_8)), str3, null);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                f.c("ToolStatsCore", "status code = " + statusCode);
                if (statusCode == CtaButton.WIDTH_DIPS) {
                    i.a(this.f, "seq", a + 1);
                    if (f.a()) {
                        a(httpResponse);
                    }
                    f.c("reportException", "report success!!!");
                    return true;
                }
                w.a(httpResponse);
                return false;
            } catch (Exception e) {
                f.a("ToolStatsCore", "post failed.", e);
                return false;
            } finally {
                w.a(httpResponse);
            }
        } catch (NoSuchAlgorithmException e2) {
            return false;
        }
    }

    private boolean a(String str, String str2, String str3, long j) {
        List a = e.a(this.f, str);
        String valueOf = String.valueOf(System.currentTimeMillis());
        a.add(new BasicNameValuePair("mdu", "adsdk"));
        a.add(new BasicNameValuePair("rv", BuildConfig.VERSION_NAME));
        a.add(new BasicNameValuePair("ts", valueOf));
        a.add(new BasicNameValuePair("seq", Long.toString(j)));
        a.add(new BasicNameValuePair("stype", str2));
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str3.getBytes());
            instance.update(valueOf.getBytes());
            instance.update(str.getBytes());
            a.add(new BasicNameValuePair("s", d.a(instance.digest())));
            valueOf = URLEncodedUtils.format(a, Defaults.ENCODING_UTF_8);
            try {
                URI uri;
                if (str2.equalsIgnoreCase("behavior")) {
                    uri = new URI(e + valueOf);
                } else {
                    uri = new URI(d + valueOf);
                }
                HttpResponse a2 = w.a(uri, str3, null);
                int statusCode = a2.getStatusLine().getStatusCode();
                f.c("ToolStatsCore", "status code = " + statusCode);
                if (statusCode == CtaButton.WIDTH_DIPS) {
                    if (f.a()) {
                        a(a2);
                    }
                    w.a(a2);
                    return true;
                }
                w.a(a2);
                return false;
            } catch (Exception e) {
                f.a("ToolStatsCore", "post failed.", e);
                w.a(null);
                return false;
            } catch (Throwable th) {
                w.a(null);
                throw th;
            }
        } catch (NoSuchAlgorithmException e2) {
            return false;
        }
    }

    private static void a(HttpResponse httpResponse) {
        Closeable gZIPInputStream;
        HttpEntity entity = httpResponse.getEntity();
        InputStream content = entity.getContent();
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding == null || contentEncoding.getValue().indexOf("gzip") == -1) {
            Object gZIPInputStream2 = content;
        } else {
            gZIPInputStream2 = new GZIPInputStream(content);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(gZIPInputStream2, Defaults.ENCODING_UTF_8);
        CharArrayBuffer charArrayBuffer = new CharArrayBuffer(1024);
        char[] cArr = new char[1024];
        while (true) {
            int read = inputStreamReader.read(cArr);
            if (read == -1) {
                break;
            }
            charArrayBuffer.append(cArr, 0, read);
        }
        d.a(gZIPInputStream2);
        String charArrayBuffer2 = charArrayBuffer.toString();
        f.c("ToolStatsCore", "result = " + charArrayBuffer2);
        try {
            JSONObject jSONObject = new JSONObject(charArrayBuffer2).getJSONObject("responseHeader");
            if (jSONObject.getInt(DuAd.DB_STATUS) != CtaButton.WIDTH_DIPS) {
                throw new IOException(jSONObject.getString("msg"));
            }
        } catch (JSONException e) {
        }
    }
}
