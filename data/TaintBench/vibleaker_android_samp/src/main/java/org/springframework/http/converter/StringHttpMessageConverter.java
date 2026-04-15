package org.springframework.http.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
    private final List<Charset> availableCharsets;
    private final Charset defaultCharset;
    private boolean writeAcceptCharset;

    public StringHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    public StringHttpMessageConverter(Charset defaultCharset) {
        super(new MediaType("text", "plain", defaultCharset), MediaType.ALL);
        this.writeAcceptCharset = true;
        this.defaultCharset = defaultCharset;
        this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    /* access modifiers changed from: protected */
    public String readInternal(Class<? extends String> cls, HttpInputMessage inputMessage) throws IOException {
        return StreamUtils.copyToString(inputMessage.getBody(), getContentTypeCharset(inputMessage.getHeaders().getContentType()));
    }

    /* access modifiers changed from: protected */
    public Long getContentLength(String s, MediaType contentType) {
        try {
            return Long.valueOf((long) s.getBytes(getContentTypeCharset(contentType).name()).length);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* access modifiers changed from: protected */
    public void writeInternal(String s, HttpOutputMessage outputMessage) throws IOException {
        if (this.writeAcceptCharset) {
            outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
        }
        StreamUtils.copy(s, getContentTypeCharset(outputMessage.getHeaders().getContentType()), outputMessage.getBody());
    }

    /* access modifiers changed from: protected */
    public List<Charset> getAcceptedCharsets() {
        return this.availableCharsets;
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        if (contentType == null || contentType.getCharSet() == null) {
            return this.defaultCharset;
        }
        return contentType.getCharSet();
    }
}
