package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class er implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV1 deserialize");
        if (inputStream == null) {
            return null;
        }
        es esVar = new es(this, inputStream);
        eq eqVar = new eq();
        eqVar.a = esVar.readUTF();
        eqVar.b = esVar.readUTF();
        eqVar.a(esVar.readUTF());
        eqVar.c = esVar.readLong();
        eqVar.i = esVar.readBoolean();
        eqVar.d = esVar.readBoolean();
        eqVar.e = esVar.readInt();
        return eqVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV1 serialize");
        throw new UnsupportedOperationException("Serialization not supported");
    }
}
