package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public final class da implements lc {
    private final cw a;

    public da(cw cwVar) {
        this.a = cwVar;
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        String str = null;
        if (inputStream == null || this.a == null) {
            return null;
        }
        dc dcVar = new dc(this, inputStream);
        cz czVar = new cz();
        czVar.a = dcVar.readInt();
        czVar.b = dcVar.readLong();
        String readUTF = dcVar.readUTF();
        if (!readUTF.equals("")) {
            str = readUTF;
        }
        czVar.c = str;
        czVar.d = new ArrayList();
        short readShort = dcVar.readShort();
        for (short s = (short) 0; s < readShort; s = (short) (s + 1)) {
            czVar.d.add(this.a.a(dcVar));
        }
        return czVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        cz czVar = (cz) obj;
        if (outputStream != null && czVar != null && this.a != null) {
            OutputStream dbVar = new db(this, outputStream);
            dbVar.writeInt(czVar.a);
            dbVar.writeLong(czVar.b);
            dbVar.writeUTF(czVar.c == null ? "" : czVar.c);
            dbVar.writeShort(czVar.d.size());
            for (cv a : czVar.d) {
                this.a.a(dbVar, a);
            }
            dbVar.flush();
        }
    }
}
