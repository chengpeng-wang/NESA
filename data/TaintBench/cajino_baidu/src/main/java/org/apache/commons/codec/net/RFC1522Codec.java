package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.log4j.spi.LocationInfo;

abstract class RFC1522Codec {
    public abstract byte[] doDecoding(byte[] bArr) throws DecoderException;

    public abstract byte[] doEncoding(byte[] bArr) throws EncoderException;

    public abstract String getEncoding();

    RFC1522Codec() {
    }

    /* access modifiers changed from: protected */
    public String encodeText(String text, String charset) throws EncoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("=?");
        buffer.append(charset);
        buffer.append('?');
        buffer.append(getEncoding());
        buffer.append('?');
        buffer.append(new String(doEncoding(text.getBytes(charset)), "US-ASCII"));
        buffer.append("?=");
        return buffer.toString();
    }

    /* access modifiers changed from: protected */
    public String decodeText(String text) throws DecoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        if (text.startsWith("=?") && text.endsWith("?=")) {
            int termnator = text.length() - 2;
            int to = text.indexOf(LocationInfo.NA, 2);
            if (to == -1 || to == termnator) {
                throw new DecoderException("RFC 1522 violation: charset token not found");
            }
            String charset = text.substring(2, to);
            if (charset.equals("")) {
                throw new DecoderException("RFC 1522 violation: charset not specified");
            }
            int from = to + 1;
            to = text.indexOf(LocationInfo.NA, from);
            if (to == -1 || to == termnator) {
                throw new DecoderException("RFC 1522 violation: encoding token not found");
            }
            String encoding = text.substring(from, to);
            if (getEncoding().equalsIgnoreCase(encoding)) {
                from = to + 1;
                return new String(doDecoding(text.substring(from, text.indexOf(LocationInfo.NA, from)).getBytes("US-ASCII")), charset);
            }
            throw new DecoderException(new StringBuffer().append("This codec cannot decode ").append(encoding).append(" encoded content").toString());
        }
        throw new DecoderException("RFC 1522 violation: malformed encoded content");
    }
}
