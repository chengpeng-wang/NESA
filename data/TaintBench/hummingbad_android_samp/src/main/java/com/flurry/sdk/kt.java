package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class kt implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        kv kvVar = new kv(this, inputStream);
        ks ksVar = new ks();
        ksVar.a = kvVar.readUTF();
        return ksVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        ks ksVar = (ks) obj;
        if (outputStream != null && ksVar != null) {
            ku kuVar = new ku(this, outputStream);
            kuVar.writeUTF(ksVar.a);
            kuVar.flush();
        }
    }
}
