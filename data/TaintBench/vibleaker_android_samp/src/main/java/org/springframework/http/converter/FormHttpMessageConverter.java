package org.springframework.http.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

public class FormHttpMessageConverter implements HttpMessageConverter<MultiValueMap<String, ?>> {
    private static final byte[] BOUNDARY_CHARS = new byte[]{(byte) 45, (byte) 95, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90};
    private Charset charset = Charset.forName("UTF-8");
    private List<HttpMessageConverter<?>> partConverters = new ArrayList();
    private final Random rnd = new Random();
    private List<MediaType> supportedMediaTypes = new ArrayList();

    private class MultipartHttpOutputMessage implements HttpOutputMessage {
        private final HttpHeaders headers = new HttpHeaders();
        private boolean headersWritten = false;
        private final OutputStream os;

        public MultipartHttpOutputMessage(OutputStream os) {
            this.os = os;
        }

        public HttpHeaders getHeaders() {
            return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
        }

        public OutputStream getBody() throws IOException {
            writeHeaders();
            return this.os;
        }

        private void writeHeaders() throws IOException {
            if (!this.headersWritten) {
                for (Entry<String, List<String>> entry : this.headers.entrySet()) {
                    byte[] headerName = getAsciiBytes((String) entry.getKey());
                    for (String headerValueString : (List) entry.getValue()) {
                        byte[] headerValue = getAsciiBytes(headerValueString);
                        this.os.write(headerName);
                        this.os.write(58);
                        this.os.write(32);
                        this.os.write(headerValue);
                        FormHttpMessageConverter.this.writeNewLine(this.os);
                    }
                }
                FormHttpMessageConverter.this.writeNewLine(this.os);
                this.headersWritten = true;
            }
        }

        /* access modifiers changed from: protected */
        public byte[] getAsciiBytes(String name) {
            try {
                return name.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    public FormHttpMessageConverter() {
        this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        this.partConverters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        this.partConverters.add(stringHttpMessageConverter);
        this.partConverters.add(new ResourceHttpMessageConverter());
    }

    public final void setPartConverters(List<HttpMessageConverter<?>> partConverters) {
        Assert.notEmpty((Collection) partConverters, "'partConverters' must not be empty");
        this.partConverters = partConverters;
    }

    public final void addPartConverter(HttpMessageConverter<?> partConverter) {
        Assert.notNull(partConverter, "'partConverter' must not be NULL");
        this.partConverters.add(partConverter);
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (!MultiValueMap.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) && supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!MultiValueMap.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return Collections.unmodifiableList(this.supportedMediaTypes);
    }

    public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> cls, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = contentType.getCharSet() != null ? contentType.getCharSet() : this.charset;
        String[] pairs = StringUtils.tokenizeToStringArray(StreamUtils.copyToString(inputMessage.getBody(), charset), "&");
        MultiValueMap<String, String> result = new LinkedMultiValueMap(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf(61);
            if (idx == -1) {
                result.add(URLDecoder.decode(pair, charset.name()), null);
            } else {
                result.add(URLDecoder.decode(pair.substring(0, idx), charset.name()), URLDecoder.decode(pair.substring(idx + 1), charset.name()));
            }
        }
        return result;
    }

    public void write(MultiValueMap<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (isMultipart(map, contentType)) {
            writeMultipart(map, outputMessage);
        } else {
            writeForm(map, contentType, outputMessage);
        }
    }

    private boolean isMultipart(MultiValueMap<String, ?> map, MediaType contentType) {
        if (contentType != null) {
            return MediaType.MULTIPART_FORM_DATA.includes(contentType);
        }
        for (String name : map.keySet()) {
            for (Object value : (List) map.get(name)) {
                if (value != null && !(value instanceof String)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeForm(MultiValueMap<String, String> form, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
        Charset charset;
        if (contentType != null) {
            outputMessage.getHeaders().setContentType(contentType);
            charset = contentType.getCharSet() != null ? contentType.getCharSet() : this.charset;
        } else {
            outputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            charset = this.charset;
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> nameIterator = form.keySet().iterator();
        while (nameIterator.hasNext()) {
            String name = (String) nameIterator.next();
            Iterator<String> valueIterator = ((List) form.get(name)).iterator();
            while (valueIterator.hasNext()) {
                String value = (String) valueIterator.next();
                builder.append(URLEncoder.encode(name, charset.name()));
                if (value != null) {
                    builder.append('=');
                    builder.append(URLEncoder.encode(value, charset.name()));
                    if (valueIterator.hasNext()) {
                        builder.append('&');
                    }
                }
            }
            if (nameIterator.hasNext()) {
                builder.append('&');
            }
        }
        byte[] bytes = builder.toString().getBytes(charset.name());
        outputMessage.getHeaders().setContentLength((long) bytes.length);
        StreamUtils.copy(bytes, outputMessage.getBody());
    }

    private void writeMultipart(MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage) throws IOException {
        byte[] boundary = generateMultipartBoundary();
        outputMessage.getHeaders().setContentType(new MediaType(MediaType.MULTIPART_FORM_DATA, Collections.singletonMap("boundary", new String(boundary, "US-ASCII"))));
        writeParts(outputMessage.getBody(), parts, boundary);
        writeEnd(boundary, outputMessage.getBody());
    }

    private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary) throws IOException {
        for (Entry<String, List<Object>> entry : parts.entrySet()) {
            String name = (String) entry.getKey();
            for (Object part : (List) entry.getValue()) {
                if (part != null) {
                    writeBoundary(boundary, os);
                    writePart(name, getEntity(part), os);
                    writeNewLine(os);
                }
            }
        }
    }

    private void writeBoundary(byte[] boundary, OutputStream os) throws IOException {
        os.write(45);
        os.write(45);
        os.write(boundary);
        writeNewLine(os);
    }

    private HttpEntity getEntity(Object part) {
        if (part instanceof HttpEntity) {
            return (HttpEntity) part;
        }
        return new HttpEntity(part);
    }

    private void writePart(String name, HttpEntity partEntity, OutputStream os) throws IOException {
        Object partBody = partEntity.getBody();
        Class<?> partType = partBody.getClass();
        HttpHeaders partHeaders = partEntity.getHeaders();
        MediaType partContentType = partHeaders.getContentType();
        for (HttpMessageConverter messageConverter : this.partConverters) {
            if (messageConverter.canWrite(partType, partContentType)) {
                HttpOutputMessage multipartOutputMessage = new MultipartHttpOutputMessage(os);
                multipartOutputMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
                if (!partHeaders.isEmpty()) {
                    multipartOutputMessage.getHeaders().putAll(partHeaders);
                }
                messageConverter.write(partBody, partContentType, multipartOutputMessage);
                return;
            }
        }
        throw new HttpMessageNotWritableException("Could not write request: no suitable HttpMessageConverter found for request type [" + partType.getName() + "]");
    }

    private void writeEnd(byte[] boundary, OutputStream os) throws IOException {
        os.write(45);
        os.write(45);
        os.write(boundary);
        os.write(45);
        os.write(45);
        writeNewLine(os);
    }

    /* access modifiers changed from: private */
    public void writeNewLine(OutputStream os) throws IOException {
        os.write(13);
        os.write(10);
    }

    /* access modifiers changed from: protected */
    public byte[] generateMultipartBoundary() {
        byte[] boundary = new byte[(this.rnd.nextInt(11) + 30)];
        for (int i = 0; i < boundary.length; i++) {
            boundary[i] = BOUNDARY_CHARS[this.rnd.nextInt(BOUNDARY_CHARS.length)];
        }
        return boundary;
    }

    /* access modifiers changed from: protected */
    public String getFilename(Object part) {
        if (part instanceof Resource) {
            return ((Resource) part).getFilename();
        }
        return null;
    }
}
