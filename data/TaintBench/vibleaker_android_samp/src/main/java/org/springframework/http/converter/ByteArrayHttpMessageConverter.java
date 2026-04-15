package org.springframework.http.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

public class ByteArrayHttpMessageConverter extends AbstractHttpMessageConverter<byte[]> {
    public ByteArrayHttpMessageConverter() {
        super(new MediaType("application", "octet-stream"), MediaType.ALL);
    }

    public boolean supports(Class<?> clazz) {
        return byte[].class.equals(clazz);
    }

    public byte[] readInternal(Class<? extends byte[]> cls, HttpInputMessage inputMessage) throws IOException {
        long contentLength = inputMessage.getHeaders().getContentLength();
        OutputStream bos = new ByteArrayOutputStream(contentLength >= 0 ? (int) contentLength : 4096);
        StreamUtils.copy(inputMessage.getBody(), bos);
        return bos.toByteArray();
    }

    /* access modifiers changed from: protected */
    public Long getContentLength(byte[] bytes, MediaType contentType) {
        return Long.valueOf((long) bytes.length);
    }

    /* access modifiers changed from: protected */
    public void writeInternal(byte[] bytes, HttpOutputMessage outputMessage) throws IOException {
        StreamUtils.copy(bytes, outputMessage.getBody());
    }
}
