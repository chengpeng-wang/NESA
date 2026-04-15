package org.apache.http.entity.mime.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.james.mime4j.field.ContentTypeField;

@NotThreadSafe
public class StringBody extends AbstractContentBody {
    private final Charset charset;
    private final byte[] content;

    public StringBody(String text, String mimeType, Charset charset) throws UnsupportedEncodingException {
        super(mimeType);
        if (text == null) {
            throw new IllegalArgumentException("Text may not be null");
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        this.content = text.getBytes(charset.name());
        this.charset = charset;
    }

    public StringBody(String text, Charset charset) throws UnsupportedEncodingException {
        this(text, ContentTypeField.TYPE_TEXT_PLAIN, charset);
    }

    public StringBody(String text) throws UnsupportedEncodingException {
        this(text, ContentTypeField.TYPE_TEXT_PLAIN, null);
    }

    public Reader getReader() {
        return new InputStreamReader(new ByteArrayInputStream(this.content), this.charset);
    }

    @Deprecated
    public void writeTo(OutputStream out, int mode) throws IOException {
        writeTo(out);
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream in = new ByteArrayInputStream(this.content);
        byte[] tmp = new byte[4096];
        while (true) {
            int l = in.read(tmp);
            if (l != -1) {
                out.write(tmp, 0, l);
            } else {
                out.flush();
                return;
            }
        }
    }

    public String getTransferEncoding() {
        return "8bit";
    }

    public String getCharset() {
        return this.charset.name();
    }

    public Map<String, String> getContentTypeParameters() {
        Map<String, String> map = new HashMap();
        map.put(ContentTypeField.PARAM_CHARSET, this.charset.name());
        return map;
    }

    public long getContentLength() {
        return (long) this.content.length;
    }

    public String getFilename() {
        return null;
    }
}
