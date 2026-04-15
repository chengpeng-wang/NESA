package org.springframework.http.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

public class ObjectToStringHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private ConversionService conversionService;
    private StringHttpMessageConverter stringHttpMessageConverter;

    public ObjectToStringHttpMessageConverter(ConversionService conversionService) {
        this(conversionService, StringHttpMessageConverter.DEFAULT_CHARSET);
    }

    public ObjectToStringHttpMessageConverter(ConversionService conversionService, Charset defaultCharset) {
        super(new MediaType("text", "plain", defaultCharset));
        Assert.notNull(conversionService, "conversionService is required");
        this.conversionService = conversionService;
        this.stringHttpMessageConverter = new StringHttpMessageConverter(defaultCharset);
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.stringHttpMessageConverter.setWriteAcceptCharset(writeAcceptCharset);
    }

    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return this.conversionService.canConvert(String.class, (Class) clazz) && canRead(mediaType);
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.conversionService.canConvert((Class) clazz, String.class) && canWrite(mediaType);
    }

    /* access modifiers changed from: protected */
    public boolean supports(Class<?> cls) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException {
        return this.conversionService.convert(this.stringHttpMessageConverter.readInternal(String.class, inputMessage), clazz);
    }

    /* access modifiers changed from: protected */
    public void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException {
        this.stringHttpMessageConverter.writeInternal((String) this.conversionService.convert(obj, String.class), outputMessage);
    }

    /* access modifiers changed from: protected */
    public Long getContentLength(Object obj, MediaType contentType) {
        return this.stringHttpMessageConverter.getContentLength((String) this.conversionService.convert(obj, String.class), contentType);
    }
}
