package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import u.aly.bf;

final class bu extends ByteArrayOutputStream {
    final /* synthetic */ bt a;

    bu(bt btVar, int i) {
        this.a = btVar;
        super(i);
    }

    public final String toString() {
        int i = (this.count <= 0 || this.buf[this.count - 1] != bf.k) ? this.count : this.count - 1;
        try {
            return new String(this.buf, 0, i, this.a.a.name());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
