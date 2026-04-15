package com.flurry.sdk;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public final class cw implements lc {
    public final void a(OutputStream outputStream, cv cvVar) {
        if (outputStream != null && cvVar != null) {
            cx cxVar = new cx(this, outputStream);
            cxVar.writeUTF(cvVar.a);
            cxVar.writeBoolean(cvVar.b);
            cxVar.writeLong(cvVar.c);
            cxVar.writeShort(cvVar.d.size());
            for (Entry entry : cvVar.d.entrySet()) {
                cxVar.writeUTF((String) entry.getKey());
                cxVar.writeUTF((String) entry.getValue());
            }
            cxVar.flush();
        }
    }

    /* renamed from: b */
    public final cv a(InputStream inputStream) {
        short s = (short) 0;
        if (inputStream == null) {
            return null;
        }
        cy cyVar = new cy(this, inputStream);
        cv cvVar = new cv();
        cvVar.a = cyVar.readUTF();
        cvVar.b = cyVar.readBoolean();
        cvVar.c = cyVar.readLong();
        cvVar.d = new HashMap();
        short readShort = cyVar.readShort();
        while (s < readShort) {
            cvVar.d.put(cyVar.readUTF(), cyVar.readUTF());
            s = (short) (s + 1);
        }
        return cvVar;
    }
}
