package org.apache.http.entity.mime;

import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import org.apache.james.mime4j.util.CharsetUtil;

@Immutable
public final class MIME {
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_TRANSFER_ENC = "Content-Transfer-Encoding";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final Charset DEFAULT_CHARSET = CharsetUtil.getCharset("US-ASCII");
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BINARY = "binary";
}
