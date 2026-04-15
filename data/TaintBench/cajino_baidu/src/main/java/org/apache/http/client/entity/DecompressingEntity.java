package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

abstract class DecompressingEntity extends HttpEntityWrapper {
    private static final int BUFFER_SIZE = 2048;

    public DecompressingEntity(HttpEntity wrapped) {
        super(wrapped);
    }

    public void writeTo(OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream instream = getContent();
        byte[] buffer = new byte[2048];
        while (true) {
            int l = instream.read(buffer);
            if (l != -1) {
                outstream.write(buffer, 0, l);
            } else {
                return;
            }
        }
    }
}
