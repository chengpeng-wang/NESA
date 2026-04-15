package com.mopub.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
    public static void copyContent(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            throw new IOException("Unable to copy from or to a null stream.");
        }
        byte[] bArr = new byte[16384];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static void copyContent(InputStream inputStream, OutputStream outputStream, long maxBytes) throws IOException {
        if (inputStream == null || outputStream == null) {
            throw new IOException("Unable to copy from or to a null stream.");
        }
        byte[] bArr = new byte[16384];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                j += (long) read;
                if (j >= maxBytes) {
                    throw new IOException("Error copying content: attempted to copy " + j + " bytes, with " + maxBytes + " maximum.");
                }
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static void readStream(InputStream inputStream, byte[] buffer) throws IOException {
        int i = 0;
        int length = buffer.length;
        do {
            int read = inputStream.read(buffer, i, length);
            if (read != -1) {
                i += read;
                length -= read;
            } else {
                return;
            }
        } while (length > 0);
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }
}
