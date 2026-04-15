package com.flurry.sdk;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ky extends kx {
    public ky(lc lcVar) {
        super(lcVar);
    }

    public final Object a(InputStream inputStream) {
        Throwable th;
        Object obj = null;
        if (inputStream != null) {
            Closeable gZIPInputStream;
            try {
                gZIPInputStream = new GZIPInputStream(inputStream);
                try {
                    obj = super.a(gZIPInputStream);
                    lt.a(gZIPInputStream);
                } catch (Throwable th2) {
                    th = th2;
                    lt.a(gZIPInputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                gZIPInputStream = null;
                th = th4;
                lt.a(gZIPInputStream);
                throw th;
            }
        }
        return obj;
    }

    public final void a(OutputStream outputStream, Object obj) {
        Throwable th;
        if (outputStream != null) {
            Closeable gZIPOutputStream;
            try {
                gZIPOutputStream = new GZIPOutputStream(outputStream);
                try {
                    super.a(gZIPOutputStream, obj);
                    lt.a(gZIPOutputStream);
                } catch (Throwable th2) {
                    th = th2;
                    lt.a(gZIPOutputStream);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                gZIPOutputStream = null;
                lt.a(gZIPOutputStream);
                throw th;
            }
        }
    }
}
