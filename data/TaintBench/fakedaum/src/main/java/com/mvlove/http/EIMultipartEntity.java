package com.mvlove.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class EIMultipartEntity extends MultipartEntity {
    private FileTransferListener transferListener;

    public static class CountingOutputStream extends FilterOutputStream {
        private final FileTransferListener listener;
        private long transferred = 0;

        public CountingOutputStream(OutputStream out, FileTransferListener listener) {
            super(out);
            this.listener = listener;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            this.out.write(b, off, len);
            this.transferred += (long) len;
            if (this.listener != null) {
                this.listener.transferred(this.transferred);
            }
        }

        public void write(int b) throws IOException {
            this.out.write(b);
            this.transferred++;
            if (this.listener != null) {
                this.listener.transferred(this.transferred);
            }
        }
    }

    public EIMultipartEntity(FileTransferListener fileTransferListener) {
        this.transferListener = fileTransferListener;
    }

    public EIMultipartEntity(HttpMultipartMode mode, String boundary, Charset charset, FileTransferListener fileTransferListener) {
        super(mode, boundary, charset);
        this.transferListener = fileTransferListener;
    }

    public EIMultipartEntity(HttpMultipartMode mode, FileTransferListener fileTransferListener) {
        super(mode);
        this.transferListener = fileTransferListener;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.transferListener));
    }
}
