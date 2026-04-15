package com.mopub.common;

import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Streams;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class DownloadResponse {
    private byte[] mBytes = new byte[0];
    private final long mContentLength;
    private final Header[] mHeaders;
    private final int mStatusCode;

    public DownloadResponse(HttpResponse httpResponse) throws Exception {
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Closeable closeable = null;
        try {
            Closeable bufferedInputStream;
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                bufferedInputStream = new BufferedInputStream(entity.getContent());
                try {
                    Streams.copyContent(bufferedInputStream, byteArrayOutputStream);
                    this.mBytes = byteArrayOutputStream.toByteArray();
                } catch (Throwable th2) {
                    Throwable th3 = th2;
                    closeable = bufferedInputStream;
                    th = th3;
                    Streams.closeStream(closeable);
                    Streams.closeStream(byteArrayOutputStream);
                    throw th;
                }
            }
            bufferedInputStream = null;
            Streams.closeStream(bufferedInputStream);
            Streams.closeStream(byteArrayOutputStream);
            this.mStatusCode = httpResponse.getStatusLine().getStatusCode();
            this.mContentLength = (long) this.mBytes.length;
            this.mHeaders = httpResponse.getAllHeaders();
        } catch (Throwable th4) {
            th = th4;
            Streams.closeStream(closeable);
            Streams.closeStream(byteArrayOutputStream);
            throw th;
        }
    }

    public byte[] getByteArray() {
        return this.mBytes;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    public long getContentLength() {
        return this.mContentLength;
    }

    public String getFirstHeader(ResponseHeader responseHeader) {
        for (Header header : this.mHeaders) {
            if (header.getName().equalsIgnoreCase(responseHeader.getKey())) {
                return header.getValue();
            }
        }
        return null;
    }
}
