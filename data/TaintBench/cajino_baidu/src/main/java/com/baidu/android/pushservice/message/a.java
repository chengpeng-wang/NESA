package com.baidu.android.pushservice.message;

import android.content.Context;
import com.baidu.android.pushservice.e;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class a {
    protected InputStream a;
    protected OutputStream b;
    protected Context c;
    protected e d;

    public a(Context context, e eVar) {
        this.c = context;
        this.d = eVar;
    }

    public a(Context context, e eVar, InputStream inputStream, OutputStream outputStream) {
        this.a = inputStream;
        this.b = outputStream;
        this.c = context;
        this.d = eVar;
    }

    public abstract b a(byte[] bArr, int i);

    public abstract void a();

    public abstract void a(int i);

    public void a(b bVar) {
        this.d.a(bVar);
    }

    public abstract b b();

    public abstract void b(b bVar);

    public abstract void c();

    public abstract void d();
}
