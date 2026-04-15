package org.springframework.util;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public abstract class Base64Utils {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Base64Delegate delegate = new AndroidBase64Delegate();

    private interface Base64Delegate {
        byte[] decode(byte[] bArr);

        byte[] encode(byte[] bArr);
    }

    private static class AndroidBase64Delegate implements Base64Delegate {
        private AndroidBase64Delegate() {
        }

        public byte[] encode(byte[] src) {
            return (src == null || src.length == 0) ? src : Base64.encode(src, 2);
        }

        public byte[] decode(byte[] src) {
            return (src == null || src.length == 0) ? src : Base64.decode(src, 2);
        }
    }

    public static byte[] encode(byte[] src) {
        return delegate.encode(src);
    }

    public static String encodeToString(byte[] src) {
        if (src == null) {
            return null;
        }
        if (src.length == 0) {
            return "";
        }
        try {
            return new String(delegate.encode(src), DEFAULT_CHARSET.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] decode(byte[] src) {
        return delegate.decode(src);
    }

    @Deprecated
    public static byte[] decode(String src) {
        return decodeFromString(src);
    }

    public static byte[] decodeFromString(String src) {
        if (src == null) {
            return null;
        }
        if (src.length() == 0) {
            return new byte[0];
        }
        try {
            return delegate.decode(src.getBytes(DEFAULT_CHARSET.displayName()));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
