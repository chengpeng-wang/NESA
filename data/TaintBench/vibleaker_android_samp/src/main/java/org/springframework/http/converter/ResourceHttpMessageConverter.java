package org.springframework.http.converter;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

public class ResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource> {
    public ResourceHttpMessageConverter() {
        super(MediaType.ALL);
    }

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> clazz) {
        return Resource.class.isAssignableFrom(clazz);
    }

    /* access modifiers changed from: protected */
    public Resource readInternal(Class<? extends Resource> cls, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return new ByteArrayResource(StreamUtils.copyToByteArray(inputMessage.getBody()));
    }

    /* access modifiers changed from: protected */
    public MediaType getDefaultContentType(Resource resource) {
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    /* access modifiers changed from: protected */
    public Long getContentLength(Resource resource, MediaType contentType) throws IOException {
        return InputStreamResource.class.equals(resource.getClass()) ? null : Long.valueOf(resource.contentLength());
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        InputStream in = resource.getInputStream();
        try {
            StreamUtils.copy(in, outputMessage.getBody());
            outputMessage.getBody().flush();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
}
