package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class dk implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        dm dmVar = new dm(this, inputStream);
        dj djVar = new dj();
        djVar.a = (ed) Enum.valueOf(ed.class, dmVar.readUTF());
        djVar.b = dmVar.readUTF();
        djVar.c = dmVar.readLong();
        djVar.d = dmVar.readLong();
        djVar.e = dmVar.readLong();
        djVar.f = dmVar.readInt();
        djVar.g = dmVar.readInt();
        djVar.h = dmVar.readInt();
        djVar.i = dmVar.readInt();
        djVar.j = dmVar.readLong();
        return djVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        dj djVar = (dj) obj;
        if (outputStream != null && djVar != null) {
            dl dlVar = new dl(this, outputStream);
            dlVar.writeUTF(djVar.a.name());
            dlVar.writeUTF(djVar.b);
            dlVar.writeLong(djVar.c);
            dlVar.writeLong(djVar.d);
            dlVar.writeLong(djVar.e);
            dlVar.writeInt(djVar.f);
            dlVar.writeInt(djVar.g);
            dlVar.writeInt(djVar.h);
            dlVar.writeInt(djVar.i);
            dlVar.writeLong(djVar.j);
            dlVar.flush();
        }
    }
}
