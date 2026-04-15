package com.flurry.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public final class kz implements lc {
    private lc a;

    public kz(lc lcVar) {
        this.a = lcVar;
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        int readInt = new lb(this, inputStream).readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i = 0; i < readInt; i++) {
            Object a = this.a.a(inputStream);
            if (a == null) {
                throw new IOException("Missing record.");
            }
            arrayList.add(a);
        }
        return arrayList;
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        int i = 0;
        List list = (List) obj;
        if (outputStream != null) {
            la laVar = new la(this, outputStream);
            int size = list != null ? list.size() : 0;
            laVar.writeInt(size);
            while (i < size) {
                this.a.a(outputStream, list.get(i));
                i++;
            }
            laVar.flush();
        }
    }
}
