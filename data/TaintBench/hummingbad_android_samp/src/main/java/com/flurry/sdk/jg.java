package com.flurry.sdk;

import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.picksinit.ErrorInfo;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

public class jg extends ma {
    private static final String a = jg.class.getSimpleName();
    private final ig b = new ig();
    /* access modifiers changed from: private */
    public HttpURLConnection c;
    private boolean d;
    public String e;
    public jj f;
    public boolean g = true;
    public jl h;
    public int i = -1;
    private boolean k;
    private Exception l;
    private final ig m = new ig();
    private final Object n = new Object();
    private final jf o = new jf();

    private void e() {
        Closeable bufferedOutputStream;
        Throwable th;
        Closeable closeable = null;
        if (!this.k) {
            this.e = lt.a(this.e);
            this.c = (HttpURLConnection) new URL(this.e).openConnection();
            this.c.setConnectTimeout(10000);
            this.c.setReadTimeout(15000);
            this.c.setRequestMethod(this.f.toString());
            this.c.setInstanceFollowRedirects(this.g);
            this.c.setDoOutput(jj.kPost.equals(this.f));
            this.c.setDoInput(true);
            for (Entry entry : this.b.a()) {
                this.c.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            if (!(jj.kGet.equals(this.f) || jj.kPost.equals(this.f))) {
                this.c.setRequestProperty("Accept-Encoding", "");
            }
            if (this.k) {
                g();
                return;
            }
            Closeable outputStream;
            if (jj.kPost.equals(this.f)) {
                try {
                    outputStream = this.c.getOutputStream();
                    try {
                        bufferedOutputStream = new BufferedOutputStream(outputStream);
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedOutputStream = null;
                        closeable = outputStream;
                        lt.a(bufferedOutputStream);
                        lt.a(closeable);
                        throw th;
                    }
                    try {
                        if (!(this.h == null || a())) {
                            this.h.a((OutputStream) bufferedOutputStream);
                        }
                        lt.a(bufferedOutputStream);
                        lt.a(outputStream);
                    } catch (Throwable th3) {
                        th = th3;
                        closeable = outputStream;
                        lt.a(bufferedOutputStream);
                        lt.a(closeable);
                        throw th;
                    }
                } catch (Throwable th4) {
                    g();
                }
            }
            this.i = this.c.getResponseCode();
            this.o.a();
            for (Entry entry2 : this.c.getHeaderFields().entrySet()) {
                for (Object a : (List) entry2.getValue()) {
                    this.m.a(entry2.getKey(), a);
                }
            }
            if (!jj.kGet.equals(this.f) && !jj.kPost.equals(this.f)) {
                g();
            } else if (this.k) {
                g();
            } else {
                try {
                    bufferedOutputStream = this.c.getInputStream();
                    try {
                        outputStream = new BufferedInputStream(bufferedOutputStream);
                    } catch (Throwable th5) {
                        th = th5;
                        Closeable closeable2 = bufferedOutputStream;
                        bufferedOutputStream = null;
                        closeable = closeable2;
                        lt.a(bufferedOutputStream);
                        lt.a(closeable);
                        throw th;
                    }
                    try {
                        if (!(this.h == null || a())) {
                            this.h.a(this, outputStream);
                        }
                        lt.a(outputStream);
                        lt.a(bufferedOutputStream);
                        g();
                    } catch (Throwable th6) {
                        th = th6;
                        closeable = bufferedOutputStream;
                        bufferedOutputStream = outputStream;
                        lt.a(bufferedOutputStream);
                        lt.a(closeable);
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    bufferedOutputStream = null;
                }
            }
        }
    }

    private void f() {
        if (this.h != null && !a()) {
            this.h.a(this);
        }
    }

    private void g() {
        if (!this.d) {
            this.d = true;
            if (this.c != null) {
                this.c.disconnect();
            }
        }
    }

    public final List a(String str) {
        return this.m.a(str);
    }

    public final void a(String str, String str2) {
        this.b.a((Object) str, (Object) str2);
    }

    /* access modifiers changed from: final */
    public final boolean a() {
        boolean z;
        synchronized (this.n) {
            z = this.k;
        }
        return z;
    }

    public final boolean b() {
        return !(this.l != null) && c();
    }

    /* access modifiers changed from: final */
    public final boolean c() {
        return this.i >= CtaButton.WIDTH_DIPS && this.i < ErrorInfo.ERROR_CODE_NO_FILL;
    }

    public final void d() {
        iw.a(3, a, "Cancelling http request: " + this.e);
        synchronized (this.n) {
            this.k = true;
        }
        if (!this.d) {
            this.d = true;
            if (this.c != null) {
                new jh(this).start();
            }
        }
    }

    public void safeRun() {
        try {
            if (this.e != null) {
                if (ht.a().a) {
                    if (this.f == null || jj.kUnknown.equals(this.f)) {
                        this.f = jj.kGet;
                    }
                    e();
                    iw.a(4, a, "HTTP status: " + this.i + " for url: " + this.e);
                    this.o.a();
                    f();
                    return;
                }
                iw.a(3, a, "Network not available, aborting http request: " + this.e);
                this.o.a();
                f();
            }
        } catch (Exception e) {
            iw.a(4, a, "HTTP status: " + this.i + " for url: " + this.e);
            iw.a(3, a, "Exception during http request: " + this.e, e);
            this.l = e;
        } finally {
            this.o.a();
            f();
        }
    }
}
