package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;

public final class ev implements lc {
    public final /* synthetic */ Object a(InputStream inputStream) {
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV3 deserialize");
        if (inputStream == null) {
            return null;
        }
        ex exVar = new ex(this, inputStream);
        eq eqVar = new eq();
        eqVar.c = exVar.readLong();
        eqVar.d = exVar.readBoolean();
        eqVar.e = exVar.readInt();
        eqVar.f = exVar.readUTF();
        eqVar.g = exVar.readUTF();
        eqVar.a = exVar.readUTF();
        eqVar.b = exVar.readUTF();
        eqVar.i = exVar.readBoolean();
        eqVar.j = exVar.readInt();
        return eqVar;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        eq eqVar = (eq) obj;
        iw.a(5, eq.h, "AdsAsyncReportInfoSerializerV3 serialize");
        if (outputStream != null && eqVar != null) {
            ew ewVar = new ew(this, outputStream);
            ewVar.writeLong(eqVar.c);
            ewVar.writeBoolean(eqVar.d);
            ewVar.writeInt(eqVar.e);
            ewVar.writeUTF(eqVar.f);
            ewVar.writeUTF(eqVar.g);
            ewVar.writeUTF(eqVar.a);
            ewVar.writeUTF(eqVar.b);
            ewVar.writeBoolean(eqVar.i);
            ewVar.writeInt(eqVar.j);
            ewVar.flush();
        }
    }
}
