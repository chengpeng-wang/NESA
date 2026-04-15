package com.androidquery.util;

import java.io.ByteArrayOutputStream;

public class PredefinedBAOS extends ByteArrayOutputStream {
    public PredefinedBAOS(int size) {
        super(size);
    }

    public byte[] toByteArray() {
        if (this.count == this.buf.length) {
            return this.buf;
        }
        return super.toByteArray();
    }
}
