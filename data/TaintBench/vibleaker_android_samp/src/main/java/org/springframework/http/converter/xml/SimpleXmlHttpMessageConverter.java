package org.springframework.http.converter.xml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

public class SimpleXmlHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Serializer serializer;

    public SimpleXmlHttpMessageConverter() {
        this(new Persister());
    }

    public SimpleXmlHttpMessageConverter(Serializer serializer) {
        super(MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.APPLICATION_WILDCARD_XML);
        setSerializer(serializer);
    }

    public void setSerializer(Serializer serializer) {
        Assert.notNull(serializer, "'serializer' must not be null");
        this.serializer = serializer;
    }

    public boolean canRead(Class<?> cls, MediaType mediaType) {
        return canRead(mediaType);
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.isAnnotationPresent(Root.class) && canWrite(mediaType);
    }

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> cls) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        try {
            Object result = this.serializer.read(clazz, new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders())));
            if (clazz.isInstance(result)) {
                return result;
            }
            throw new TypeMismatchException(result, (Class) clazz);
        } catch (Exception ex) {
            throw new HttpMessageNotReadableException("Could not read [" + clazz + "]", ex);
        }
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        Writer out = new OutputStreamWriter(outputMessage.getBody(), getCharset(outputMessage.getHeaders()));
        try {
            this.serializer.write(o, out);
            out.close();
        } catch (Exception ex) {
            throw new HttpMessageNotWritableException("Could not write [" + o + "]", ex);
        }
    }

    private Charset getCharset(HttpHeaders headers) {
        if (headers == null || headers.getContentType() == null || headers.getContentType().getCharSet() == null) {
            return DEFAULT_CHARSET;
        }
        return headers.getContentType().getCharSet();
    }
}
