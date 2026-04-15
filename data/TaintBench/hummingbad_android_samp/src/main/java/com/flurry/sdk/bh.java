package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class bh implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        bj bjVar = new bj(this, inputStream);
        bg bgVar = new bg();
        bgVar.a = bjVar.readUTF();
        bgVar.b = cr.values()[bjVar.readInt()];
        bgVar.c = bjVar.readLong();
        bgVar.d = bjVar.readLong();
        bgVar.e = bw.values()[bjVar.readInt()];
        bgVar.f = bjVar.readLong();
        bgVar.g = bjVar.readLong();
        return bgVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        bg bgVar = (bg) obj;
        if (outputStream != null && bgVar != null) {
            bi biVar = new bi(this, outputStream);
            biVar.writeUTF(bgVar.a);
            biVar.writeInt(bgVar.b.ordinal());
            biVar.writeLong(bgVar.c);
            biVar.writeLong(bgVar.d);
            biVar.writeInt(bgVar.e.ordinal());
            biVar.writeLong(bgVar.f);
            biVar.writeLong(bgVar.g);
            biVar.flush();
        }
    }
}
