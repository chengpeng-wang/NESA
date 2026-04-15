package com.flurry.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class le implements lc {
    private final String a;
    private final int b;
    private final lh c;

    public le(String str, int i, lh lhVar) {
        this.a = str;
        this.b = i;
        this.c = lhVar;
    }

    public final Object a(InputStream inputStream) {
        if (inputStream == null || this.c == null) {
            return null;
        }
        lg lgVar = new lg(this, inputStream);
        String readUTF = lgVar.readUTF();
        if (this.a.equals(readUTF)) {
            return this.c.a(lgVar.readInt()).a(lgVar);
        }
        throw new IOException("Signature: " + readUTF + " is invalid");
    }

    public final void a(OutputStream outputStream, Object obj) {
        if (outputStream != null && this.c != null) {
            lf lfVar = new lf(this, outputStream);
            lfVar.writeUTF(this.a);
            lfVar.writeInt(this.b);
            this.c.a(this.b).a(lfVar, obj);
            lfVar.flush();
        }
    }
}
