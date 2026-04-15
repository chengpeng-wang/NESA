package com.flurry.sdk;

import android.os.FileObserver;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class ck {
    /* access modifiers changed from: private|static|final */
    public static final String a = ck.class.getSimpleName();
    /* access modifiers changed from: private|final */
    public final String b = "fileStreamCacheDownloader";
    private final long c;
    private final boolean d;
    private FileObserver e;
    /* access modifiers changed from: private */
    public bm f;

    ck(long j) {
        this.c = j;
        this.d = false;
    }

    public final co a(String str) {
        if (this.f == null || str == null) {
            return null;
        }
        co coVar;
        try {
            bs a = this.f.a(hc.b(str));
            coVar = a != null ? new co(this, a, this.d, (byte) 0) : null;
        } catch (IOException e) {
            iw.a(3, a, "Exception during getReader for cache: " + this.b + " key: " + str, e);
            lt.a(null);
            coVar = null;
        }
        return coVar;
    }

    public final void a() {
        try {
            File file = new File(hc.a(this.b), "canary");
            if (ls.a(file) && (file.exists() || file.createNewFile())) {
                this.e = new cl(this, file.getAbsolutePath());
                this.e.startWatching();
                this.f = bm.a(hc.a(this.b), this.c);
                return;
            }
            throw new IOException("Could not create canary file.");
        } catch (IOException e) {
            iw.a(3, a, "Could not open cache: " + this.b);
        }
    }

    public final cp b(String str) {
        if (this.f == null || str == null) {
            return null;
        }
        cp cpVar;
        try {
            bp b = this.f.b(hc.b(str));
            cpVar = b != null ? new cp(this, b, this.d, (byte) 0) : null;
        } catch (IOException e) {
            iw.a(3, a, "Exception during getWriter for cache: " + this.b + " key: " + str, e);
            lt.a(null);
            cpVar = null;
        }
        return cpVar;
    }

    public final void b() {
        if (this.e != null) {
            this.e.stopWatching();
            this.e = null;
        }
        lt.a(this.f);
    }

    public final boolean c(String str) {
        boolean z = false;
        if (this.f == null || str == null) {
            return z;
        }
        try {
            return this.f.c(hc.b(str));
        } catch (IOException e) {
            iw.a(3, a, "Exception during remove for cache: " + this.b + " key: " + str, e);
            return z;
        }
    }

    public final boolean d(String str) {
        boolean z = false;
        if (!(this.f == null || str == null)) {
            try {
                Closeable a = this.f.a(hc.b(str));
                if (a != null) {
                    z = true;
                }
                lt.a(a);
            } catch (IOException e) {
                iw.a(3, a, "Exception during exists for cache: " + this.b, e);
                lt.a(null);
            } catch (Throwable th) {
                lt.a(null);
                throw th;
            }
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        super.finalize();
        b();
    }
}
