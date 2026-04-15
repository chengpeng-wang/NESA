package org.apache.http.entity.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.field.ContentTypeField;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.MessageWriter;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;

@NotThreadSafe
public class HttpMultipart extends Multipart {
    private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, CharsetUtil.CRLF);
    private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
    private HttpMultipartMode mode = HttpMultipartMode.STRICT;

    private static ByteArrayBuffer encode(Charset charset, String string) {
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
    }

    private static void writeBytes(ByteSequence b, OutputStream out) throws IOException {
        if (b instanceof ByteArrayBuffer) {
            writeBytes((ByteArrayBuffer) b, out);
        } else {
            out.write(b.toByteArray());
        }
    }

    public HttpMultipart(String subType) {
        super(subType);
    }

    public HttpMultipartMode getMode() {
        return this.mode;
    }

    public void setMode(HttpMultipartMode mode) {
        this.mode = mode;
    }

    /* access modifiers changed from: protected */
    public Charset getCharset() {
        ContentTypeField cField = (ContentTypeField) getParent().getHeader().getField("Content-Type");
        switch (this.mode) {
            case STRICT:
                return MIME.DEFAULT_CHARSET;
            case BROWSER_COMPATIBLE:
                if (cField.getCharset() != null) {
                    return CharsetUtil.getCharset(cField.getCharset());
                }
                return CharsetUtil.getCharset("ISO-8859-1");
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public String getBoundary() {
        return ((ContentTypeField) getParent().getHeader().getField("Content-Type")).getBoundary();
    }

    private void doWriteTo(HttpMultipartMode mode, OutputStream out, boolean writeContent) throws IOException {
        List<BodyPart> bodyParts = getBodyParts();
        Charset charset = getCharset();
        ByteArrayBuffer boundary = encode(charset, getBoundary());
        int i;
        BodyPart part;
        switch (mode) {
            case STRICT:
                String preamble = getPreamble();
                if (!(preamble == null || preamble.length() == 0)) {
                    writeBytes(encode(charset, preamble), out);
                    writeBytes(CR_LF, out);
                }
                for (i = 0; i < bodyParts.size(); i++) {
                    writeBytes(TWO_DASHES, out);
                    writeBytes(boundary, out);
                    writeBytes(CR_LF, out);
                    part = (BodyPart) bodyParts.get(i);
                    for (Field field : part.getHeader().getFields()) {
                        writeBytes(field.getRaw(), out);
                        writeBytes(CR_LF, out);
                    }
                    writeBytes(CR_LF, out);
                    if (writeContent) {
                        MessageWriter.DEFAULT.writeBody(part.getBody(), out);
                    }
                    writeBytes(CR_LF, out);
                }
                writeBytes(TWO_DASHES, out);
                writeBytes(boundary, out);
                writeBytes(TWO_DASHES, out);
                writeBytes(CR_LF, out);
                String epilogue = getEpilogue();
                if (epilogue != null && epilogue.length() != 0) {
                    writeBytes(encode(charset, epilogue), out);
                    writeBytes(CR_LF, out);
                    return;
                }
                return;
            case BROWSER_COMPATIBLE:
                for (i = 0; i < bodyParts.size(); i++) {
                    writeBytes(TWO_DASHES, out);
                    writeBytes(boundary, out);
                    writeBytes(CR_LF, out);
                    part = (BodyPart) bodyParts.get(i);
                    Field cd = part.getHeader().getField("Content-Disposition");
                    StringBuilder s = new StringBuilder();
                    s.append(cd.getName());
                    s.append(": ");
                    s.append(cd.getBody());
                    writeBytes(encode(charset, s.toString()), out);
                    writeBytes(CR_LF, out);
                    writeBytes(CR_LF, out);
                    if (writeContent) {
                        MessageWriter.DEFAULT.writeBody(part.getBody(), out);
                    }
                    writeBytes(CR_LF, out);
                }
                writeBytes(TWO_DASHES, out);
                writeBytes(boundary, out);
                writeBytes(TWO_DASHES, out);
                writeBytes(CR_LF, out);
                return;
            default:
                return;
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        doWriteTo(this.mode, out, true);
    }

    public long getTotalLength() {
        long j = -1;
        List<?> bodyParts = getBodyParts();
        long contentLen = 0;
        for (int i = 0; i < bodyParts.size(); i++) {
            Body body = ((BodyPart) bodyParts.get(i)).getBody();
            if (!(body instanceof ContentBody)) {
                return j;
            }
            long len = ((ContentBody) body).getContentLength();
            if (len < 0) {
                return j;
            }
            contentLen += len;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doWriteTo(this.mode, out, false);
            return ((long) out.toByteArray().length) + contentLen;
        } catch (IOException e) {
            return j;
        }
    }
}
