package com.facebook.ads.internal.thirdparty.http;

import android.content.Context;
import android.os.Build.VERSION;
import com.facebook.ads.internal.dto.f;
import com.facebook.ads.internal.e;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.h;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class a {
    private static int[] g = new int[20];
    private static final String h = a.class.getSimpleName();
    protected final p a;
    protected final d b;
    protected String c;
    protected q d;
    protected int e;
    protected int f;
    private int i;
    private Map<String, String> j;
    private boolean k;

    static {
        c();
        if (VERSION.SDK_INT > 8) {
            a();
        }
    }

    public a() {
        this("");
    }

    public a(Context context) {
        this(context, null);
    }

    public a(Context context, e eVar) {
        this();
        a("user-agent", h.a(context, eVar) + " [FBAN/AudienceNetworkForAndroid;" + "FBSN/Android" + ";FBSV/" + f.a + ";FBAB/" + f.d + ";FBAV/" + f.f + ";FBBV/" + f.g + ";FBLC/" + Locale.getDefault().toString() + "]");
        if (g.a()) {
            g.b();
        }
    }

    public a(d dVar, String str) {
        this(dVar, str, new f() {
        });
    }

    public a(d dVar, String str, p pVar) {
        this.c = "";
        this.d = new g();
        this.e = 2000;
        this.f = 8000;
        this.i = 3;
        this.j = new TreeMap();
        this.c = str;
        this.a = pVar;
        this.b = dVar;
    }

    public a(String str) {
        this(new e(), str);
    }

    public static void a() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
    }

    private static void c() {
        if (VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private void c(HttpURLConnection httpURLConnection) {
        for (String str : this.j.keySet()) {
            httpURLConnection.setRequestProperty(str, (String) this.j.get(str));
        }
    }

    /* access modifiers changed from: protected */
    public int a(int i) {
        return g[i + 2] * 1000;
    }

    /* access modifiers changed from: protected */
    public int a(HttpURLConnection httpURLConnection, byte[] bArr) {
        OutputStream outputStream = null;
        try {
            outputStream = this.a.a(httpURLConnection);
            if (outputStream != null) {
                this.a.a(outputStream, bArr);
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }
            return responseCode;
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    public a a(String str, String str2) {
        this.j.put(str, str2);
        return this;
    }

    public n a(l lVar) {
        int i = 0;
        long currentTimeMillis = System.currentTimeMillis();
        while (i < this.i) {
            try {
                c(a(i));
                if (this.d.a()) {
                    this.d.a((i + 1) + "of" + this.i + ", trying " + lVar.a());
                }
                currentTimeMillis = System.currentTimeMillis();
                n a = a(lVar.a(), lVar.b(), lVar.c(), lVar.d());
                if (a != null) {
                    return a;
                }
                i++;
            } catch (m e) {
                if (a((Throwable) e, currentTimeMillis) && i < this.i - 1) {
                    continue;
                } else if (!this.a.a(e) || i >= this.i - 1) {
                    throw e;
                } else {
                    try {
                        Thread.sleep((long) this.e);
                    } catch (InterruptedException e2) {
                        throw e;
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:58:0x00b1=Splitter:B:58:0x00b1, B:73:0x00d8=Splitter:B:73:0x00d8} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x008d  */
    public com.facebook.ads.internal.thirdparty.http.n a(java.lang.String r7, com.facebook.ads.internal.thirdparty.http.j r8, java.lang.String r9, byte[] r10) {
        /*
        r6 = this;
        r1 = 0;
        r4 = 0;
        r0 = 0;
        r6.k = r0;	 Catch:{ Exception -> 0x00e3, all -> 0x00de }
        r2 = r6.a(r7);	 Catch:{ Exception -> 0x00e3, all -> 0x00de }
        r6.a(r2, r8, r9);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r6.c(r2);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r0 = r6.d;	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r0 = r0.a();	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        if (r0 == 0) goto L_0x001c;
    L_0x0017:
        r0 = r6.d;	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r0.a(r2, r10);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
    L_0x001c:
        r2.connect();	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r0 = 1;
        r6.k = r0;	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r0 = r2.getDoOutput();	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        if (r0 == 0) goto L_0x002d;
    L_0x0028:
        if (r10 == 0) goto L_0x002d;
    L_0x002a:
        r6.a(r2, r10);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
    L_0x002d:
        r0 = r2.getDoInput();	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        if (r0 == 0) goto L_0x004a;
    L_0x0033:
        r0 = r6.a(r2);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
    L_0x0037:
        r1 = r6.d;
        r1 = r1.a();
        if (r1 == 0) goto L_0x0044;
    L_0x003f:
        r1 = r6.d;
        r1.a(r0);
    L_0x0044:
        if (r2 == 0) goto L_0x0049;
    L_0x0046:
        r2.disconnect();
    L_0x0049:
        return r0;
    L_0x004a:
        r0 = new com.facebook.ads.internal.thirdparty.http.n;	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        r3 = 0;
        r0.m871init(r2, r3);	 Catch:{ Exception -> 0x0051, all -> 0x00e1 }
        goto L_0x0037;
    L_0x0051:
        r0 = move-exception;
        r3 = r2;
        r2 = r0;
    L_0x0054:
        r0 = r6.b(r3);	 Catch:{ Exception -> 0x0091 }
        if (r0 == 0) goto L_0x0073;
    L_0x005a:
        r1 = r0.a();	 Catch:{ all -> 0x0079 }
        if (r1 <= 0) goto L_0x0073;
    L_0x0060:
        r1 = r6.d;
        r1 = r1.a();
        if (r1 == 0) goto L_0x006d;
    L_0x0068:
        r1 = r6.d;
        r1.a(r0);
    L_0x006d:
        if (r3 == 0) goto L_0x0049;
    L_0x006f:
        r3.disconnect();
        goto L_0x0049;
    L_0x0073:
        r1 = new com.facebook.ads.internal.thirdparty.http.m;	 Catch:{ all -> 0x0079 }
        r1.m870init(r2, r0);	 Catch:{ all -> 0x0079 }
        throw r1;	 Catch:{ all -> 0x0079 }
    L_0x0079:
        r1 = move-exception;
        r2 = r3;
        r5 = r0;
        r0 = r1;
        r1 = r5;
    L_0x007e:
        r3 = r6.d;
        r3 = r3.a();
        if (r3 == 0) goto L_0x008b;
    L_0x0086:
        r3 = r6.d;
        r3.a(r1);
    L_0x008b:
        if (r2 == 0) goto L_0x0090;
    L_0x008d:
        r2.disconnect();
    L_0x0090:
        throw r0;
    L_0x0091:
        r0 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x00ba }
        if (r1 == 0) goto L_0x00b1;
    L_0x0097:
        r0 = r4.a();	 Catch:{ all -> 0x00b7 }
        if (r0 <= 0) goto L_0x00b1;
    L_0x009d:
        r0 = r6.d;
        r0 = r0.a();
        if (r0 == 0) goto L_0x00aa;
    L_0x00a5:
        r0 = r6.d;
        r0.a(r1);
    L_0x00aa:
        if (r3 == 0) goto L_0x00af;
    L_0x00ac:
        r3.disconnect();
    L_0x00af:
        r0 = r1;
        goto L_0x0049;
    L_0x00b1:
        r0 = new com.facebook.ads.internal.thirdparty.http.m;	 Catch:{ all -> 0x00b7 }
        r0.m870init(r2, r4);	 Catch:{ all -> 0x00b7 }
        throw r0;	 Catch:{ all -> 0x00b7 }
    L_0x00b7:
        r0 = move-exception;
        r2 = r3;
        goto L_0x007e;
    L_0x00ba:
        r0 = move-exception;
        if (r1 == 0) goto L_0x00d8;
    L_0x00bd:
        r0 = r4.a();	 Catch:{ all -> 0x00b7 }
        if (r0 <= 0) goto L_0x00d8;
    L_0x00c3:
        r0 = r6.d;
        r0 = r0.a();
        if (r0 == 0) goto L_0x00d0;
    L_0x00cb:
        r0 = r6.d;
        r0.a(r1);
    L_0x00d0:
        if (r3 == 0) goto L_0x00d5;
    L_0x00d2:
        r3.disconnect();
    L_0x00d5:
        r0 = r1;
        goto L_0x0049;
    L_0x00d8:
        r0 = new com.facebook.ads.internal.thirdparty.http.m;	 Catch:{ all -> 0x00b7 }
        r0.m870init(r2, r4);	 Catch:{ all -> 0x00b7 }
        throw r0;	 Catch:{ all -> 0x00b7 }
    L_0x00de:
        r0 = move-exception;
        r2 = r1;
        goto L_0x007e;
    L_0x00e1:
        r0 = move-exception;
        goto L_0x007e;
    L_0x00e3:
        r0 = move-exception;
        r2 = r0;
        r3 = r1;
        goto L_0x0054;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.ads.internal.thirdparty.http.a.a(java.lang.String, com.facebook.ads.internal.thirdparty.http.j, java.lang.String, byte[]):com.facebook.ads.internal.thirdparty.http.n");
    }

    public n a(String str, o oVar) {
        return b(new i(str, oVar));
    }

    /* access modifiers changed from: protected */
    public n a(HttpURLConnection httpURLConnection) {
        Throwable th;
        byte[] bArr = null;
        InputStream b;
        try {
            b = this.a.b(httpURLConnection);
            if (b != null) {
                try {
                    bArr = this.a.a(b);
                } catch (Throwable th2) {
                    th = th2;
                    if (b != null) {
                        try {
                            b.close();
                        } catch (Exception e) {
                        }
                    }
                    throw th;
                }
            }
            n nVar = new n(httpURLConnection, bArr);
            if (b != null) {
                try {
                    b.close();
                } catch (Exception e2) {
                }
            }
            return nVar;
        } catch (Throwable th3) {
            th = th3;
            b = null;
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection a(String str) {
        String str2 = this.c + str;
        try {
            URL url = new URL(str2);
            return this.a.a(str2);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(str2 + " is not a valid URL", e);
        }
    }

    /* access modifiers changed from: protected */
    public void a(l lVar, b bVar) {
        this.b.a(this, bVar).a(lVar);
    }

    public void a(String str, o oVar, b bVar) {
        a(new k(str, oVar), bVar);
    }

    /* access modifiers changed from: protected */
    public void a(HttpURLConnection httpURLConnection, j jVar, String str) {
        httpURLConnection.setConnectTimeout(this.e);
        httpURLConnection.setReadTimeout(this.f);
        this.a.a(httpURLConnection, jVar, str);
    }

    /* access modifiers changed from: protected */
    public boolean a(Throwable th, long j) {
        long currentTimeMillis = (System.currentTimeMillis() - j) + 10;
        if (this.d.a()) {
            this.d.a("ELAPSED TIME = " + currentTimeMillis + ", CT = " + this.e + ", RT = " + this.f);
        }
        return this.k ? currentTimeMillis >= ((long) this.f) : currentTimeMillis >= ((long) this.e);
    }

    public n b(l lVar) {
        n nVar = null;
        try {
            return a(lVar.a(), lVar.b(), lVar.c(), lVar.d());
        } catch (m e) {
            this.a.a(e);
            return nVar;
        } catch (Exception e2) {
            this.a.a(new m(e2, nVar));
            return nVar;
        }
    }

    public n b(String str, o oVar) {
        return b(new k(str, oVar));
    }

    /* access modifiers changed from: protected */
    public n b(HttpURLConnection httpURLConnection) {
        Throwable th;
        byte[] bArr = null;
        InputStream errorStream;
        try {
            errorStream = httpURLConnection.getErrorStream();
            if (errorStream != null) {
                try {
                    bArr = this.a.a(errorStream);
                } catch (Throwable th2) {
                    th = th2;
                    if (errorStream != null) {
                        try {
                            errorStream.close();
                        } catch (Exception e) {
                        }
                    }
                    throw th;
                }
            }
            n nVar = new n(httpURLConnection, bArr);
            if (errorStream != null) {
                try {
                    errorStream.close();
                } catch (Exception e2) {
                }
            }
            return nVar;
        } catch (Throwable th3) {
            th = th3;
            errorStream = null;
        }
    }

    public o b() {
        return new o();
    }

    public void b(int i) {
        if (i <= 0 || i > 18) {
            throw new IllegalArgumentException("Maximum retries must be between 1 and 18");
        }
        this.i = i;
    }

    public void c(int i) {
        this.e = i;
    }
}
