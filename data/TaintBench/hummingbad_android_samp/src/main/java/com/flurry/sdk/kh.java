package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class kh implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        kj kjVar = new kj(this, inputStream);
        kg kgVar = new kg();
        short readShort = kjVar.readShort();
        if (readShort == (short) 0) {
            return null;
        }
        kgVar.b = new byte[readShort];
        kjVar.readFully(kgVar.b);
        kjVar.readUnsignedShort();
        return kgVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        kg kgVar = (kg) obj;
        if (outputStream != null && kgVar != null) {
            ki kiVar = new ki(this, outputStream);
            kiVar.writeShort(kgVar.b.length);
            kiVar.write(kgVar.b);
            kiVar.writeShort(0);
            kiVar.flush();
        }
    }
}
