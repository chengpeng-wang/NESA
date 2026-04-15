package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class et implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV2 deserialize");
        if (inputStream == null) {
            return null;
        }
        eu euVar = new eu(this, inputStream);
        eq eqVar = new eq();
        eqVar.c = euVar.readLong();
        eqVar.d = euVar.readBoolean();
        eqVar.e = euVar.readInt();
        eqVar.f = euVar.readUTF();
        eqVar.g = euVar.readUTF();
        eqVar.a = euVar.readUTF();
        eqVar.b = euVar.readUTF();
        eqVar.i = euVar.readBoolean();
        return eqVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV2 deserialize");
        throw new UnsupportedOperationException("Serialization not supported");
    }
}
