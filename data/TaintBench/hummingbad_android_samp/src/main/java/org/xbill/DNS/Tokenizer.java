package org.xbill.DNS;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base64;

public class Tokenizer {
    public static final int COMMENT = 5;
    public static final int EOF = 0;
    public static final int EOL = 1;
    public static final int IDENTIFIER = 3;
    public static final int QUOTED_STRING = 4;
    public static final int WHITESPACE = 2;
    private static String delim = " \t\n;()\"";
    private static String quotes = "\"";
    private Token current;
    private String delimiters;
    private String filename;
    private PushbackInputStream is;
    private int line;
    private int multiline;
    private boolean quoting;
    private StringBuffer sb;
    private boolean ungottenToken;
    private boolean wantClose;

    /* renamed from: org.xbill.DNS.Tokenizer$1 */
    static class AnonymousClass1 {
    }

    public static class Token {
        public int type;
        public String value;

        Token(AnonymousClass1 x0) {
            this();
        }

        static Token access$100(Token x0, int x1, StringBuffer x2) {
            return x0.set(x1, x2);
        }

        private Token() {
            this.type = -1;
            this.value = null;
        }

        private Token set(int type, StringBuffer value) {
            if (type < 0) {
                throw new IllegalArgumentException();
            }
            this.type = type;
            this.value = value == null ? null : value.toString();
            return this;
        }

        public String toString() {
            switch (this.type) {
                case 0:
                    return "<eof>";
                case 1:
                    return "<eol>";
                case 2:
                    return "<whitespace>";
                case 3:
                    return new StringBuffer().append("<identifier: ").append(this.value).append(">").toString();
                case 4:
                    return new StringBuffer().append("<quoted_string: ").append(this.value).append(">").toString();
                case 5:
                    return new StringBuffer().append("<comment: ").append(this.value).append(">").toString();
                default:
                    return "<unknown>";
            }
        }

        public boolean isString() {
            return this.type == 3 || this.type == 4;
        }

        public boolean isEOL() {
            return this.type == 1 || this.type == 0;
        }
    }

    static class TokenizerException extends TextParseException {
        String message;

        public TokenizerException(String filename, int line, String message) {
            super(new StringBuffer().append(filename).append(":").append(line).append(": ").append(message).toString());
            this.message = message;
        }

        public String getBaseMessage() {
            return this.message;
        }
    }

    public Tokenizer(InputStream is) {
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        this.is = new PushbackInputStream(is, 2);
        this.ungottenToken = false;
        this.multiline = 0;
        this.quoting = false;
        this.delimiters = delim;
        this.current = new Token(null);
        this.sb = new StringBuffer();
        this.filename = "<none>";
        this.line = 1;
    }

    public Tokenizer(String s) {
        this(new ByteArrayInputStream(s.getBytes()));
    }

    public Tokenizer(File f) throws FileNotFoundException {
        this(new FileInputStream(f));
        this.wantClose = true;
        this.filename = f.getName();
    }

    private int getChar() throws IOException {
        int c = this.is.read();
        if (c == 13) {
            int next = this.is.read();
            if (next != 10) {
                this.is.unread(next);
            }
            c = 10;
        }
        if (c == 10) {
            this.line++;
        }
        return c;
    }

    private void ungetChar(int c) throws IOException {
        if (c != -1) {
            this.is.unread(c);
            if (c == 10) {
                this.line--;
            }
        }
    }

    private int skipWhitespace() throws IOException {
        int c;
        int skipped = 0;
        while (true) {
            c = getChar();
            if (c == 32 || c == 9 || (c == 10 && this.multiline > 0)) {
                skipped++;
            }
        }
        ungetChar(c);
        return skipped;
    }

    private void checkUnbalancedParens() throws TextParseException {
        if (this.multiline > 0) {
            throw exception("unbalanced parentheses");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:62:0x00f7  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x00e2 A:{SYNTHETIC} */
    public org.xbill.DNS.Tokenizer.Token get(boolean r11, boolean r12) throws java.io.IOException {
        /*
        r10 = this;
        r9 = 4;
        r8 = 1;
        r7 = 0;
        r6 = -1;
        r5 = 0;
        r3 = r10.ungottenToken;
        if (r3 == 0) goto L_0x0032;
    L_0x0009:
        r10.ungottenToken = r5;
        r3 = r10.current;
        r3 = r3.type;
        r4 = 2;
        if (r3 != r4) goto L_0x0017;
    L_0x0012:
        if (r11 == 0) goto L_0x0032;
    L_0x0014:
        r3 = r10.current;
    L_0x0016:
        return r3;
    L_0x0017:
        r3 = r10.current;
        r3 = r3.type;
        r4 = 5;
        if (r3 != r4) goto L_0x0023;
    L_0x001e:
        if (r12 == 0) goto L_0x0032;
    L_0x0020:
        r3 = r10.current;
        goto L_0x0016;
    L_0x0023:
        r3 = r10.current;
        r3 = r3.type;
        if (r3 != r8) goto L_0x002f;
    L_0x0029:
        r3 = r10.line;
        r3 = r3 + 1;
        r10.line = r3;
    L_0x002f:
        r3 = r10.current;
        goto L_0x0016;
    L_0x0032:
        r1 = r10.skipWhitespace();
        if (r1 <= 0) goto L_0x0042;
    L_0x0038:
        if (r11 == 0) goto L_0x0042;
    L_0x003a:
        r3 = r10.current;
        r4 = 2;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r4, r7);
        goto L_0x0016;
    L_0x0042:
        r2 = 3;
        r3 = r10.sb;
        r3.setLength(r5);
    L_0x0048:
        r0 = r10.getChar();
        if (r0 == r6) goto L_0x0056;
    L_0x004e:
        r3 = r10.delimiters;
        r3 = r3.indexOf(r0);
        if (r3 == r6) goto L_0x013a;
    L_0x0056:
        if (r0 != r6) goto L_0x007b;
    L_0x0058:
        r3 = r10.quoting;
        if (r3 == 0) goto L_0x0063;
    L_0x005c:
        r3 = "EOF in quoted string";
        r3 = r10.exception(r3);
        throw r3;
    L_0x0063:
        r3 = r10.sb;
        r3 = r3.length();
        if (r3 != 0) goto L_0x0072;
    L_0x006b:
        r3 = r10.current;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r5, r7);
        goto L_0x0016;
    L_0x0072:
        r3 = r10.current;
        r4 = r10.sb;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r2, r4);
        goto L_0x0016;
    L_0x007b:
        r3 = r10.sb;
        r3 = r3.length();
        if (r3 != 0) goto L_0x0122;
    L_0x0083:
        if (r2 == r9) goto L_0x0122;
    L_0x0085:
        r3 = 40;
        if (r0 != r3) goto L_0x0093;
    L_0x0089:
        r3 = r10.multiline;
        r3 = r3 + 1;
        r10.multiline = r3;
        r10.skipWhitespace();
        goto L_0x0048;
    L_0x0093:
        r3 = 41;
        if (r0 != r3) goto L_0x00ac;
    L_0x0097:
        r3 = r10.multiline;
        if (r3 > 0) goto L_0x00a2;
    L_0x009b:
        r3 = "invalid close parenthesis";
        r3 = r10.exception(r3);
        throw r3;
    L_0x00a2:
        r3 = r10.multiline;
        r3 = r3 + -1;
        r10.multiline = r3;
        r10.skipWhitespace();
        goto L_0x0048;
    L_0x00ac:
        r3 = 34;
        if (r0 != r3) goto L_0x00c6;
    L_0x00b0:
        r3 = r10.quoting;
        if (r3 != 0) goto L_0x00bc;
    L_0x00b4:
        r10.quoting = r8;
        r3 = quotes;
        r10.delimiters = r3;
        r2 = 4;
        goto L_0x0048;
    L_0x00bc:
        r10.quoting = r5;
        r3 = delim;
        r10.delimiters = r3;
        r10.skipWhitespace();
        goto L_0x0048;
    L_0x00c6:
        r3 = 10;
        if (r0 != r3) goto L_0x00d2;
    L_0x00ca:
        r3 = r10.current;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r8, r7);
        goto L_0x0016;
    L_0x00d2:
        r3 = 59;
        if (r0 != r3) goto L_0x011c;
    L_0x00d6:
        r0 = r10.getChar();
        r3 = 10;
        if (r0 == r3) goto L_0x00e0;
    L_0x00de:
        if (r0 != r6) goto L_0x00f0;
    L_0x00e0:
        if (r12 == 0) goto L_0x00f7;
    L_0x00e2:
        r10.ungetChar(r0);
        r3 = r10.current;
        r4 = 5;
        r5 = r10.sb;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r4, r5);
        goto L_0x0016;
    L_0x00f0:
        r3 = r10.sb;
        r4 = (char) r0;
        r3.append(r4);
        goto L_0x00d6;
    L_0x00f7:
        if (r0 != r6) goto L_0x0106;
    L_0x00f9:
        if (r2 == r9) goto L_0x0106;
    L_0x00fb:
        r10.checkUnbalancedParens();
        r3 = r10.current;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r5, r7);
        goto L_0x0016;
    L_0x0106:
        r3 = r10.multiline;
        if (r3 <= 0) goto L_0x0114;
    L_0x010a:
        r10.skipWhitespace();
        r3 = r10.sb;
        r3.setLength(r5);
        goto L_0x0048;
    L_0x0114:
        r3 = r10.current;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r8, r7);
        goto L_0x0016;
    L_0x011c:
        r3 = new java.lang.IllegalStateException;
        r3.<init>();
        throw r3;
    L_0x0122:
        r10.ungetChar(r0);
        r3 = r10.sb;
        r3 = r3.length();
        if (r3 != 0) goto L_0x0169;
    L_0x012d:
        if (r2 == r9) goto L_0x0169;
    L_0x012f:
        r10.checkUnbalancedParens();
        r3 = r10.current;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r5, r7);
        goto L_0x0016;
    L_0x013a:
        r3 = 92;
        if (r0 != r3) goto L_0x015a;
    L_0x013e:
        r0 = r10.getChar();
        if (r0 != r6) goto L_0x014b;
    L_0x0144:
        r3 = "unterminated escape sequence";
        r3 = r10.exception(r3);
        throw r3;
    L_0x014b:
        r3 = r10.sb;
        r4 = 92;
        r3.append(r4);
    L_0x0152:
        r3 = r10.sb;
        r4 = (char) r0;
        r3.append(r4);
        goto L_0x0048;
    L_0x015a:
        r3 = r10.quoting;
        if (r3 == 0) goto L_0x0152;
    L_0x015e:
        r3 = 10;
        if (r0 != r3) goto L_0x0152;
    L_0x0162:
        r3 = "newline in quoted string";
        r3 = r10.exception(r3);
        throw r3;
    L_0x0169:
        r3 = r10.current;
        r4 = r10.sb;
        r3 = org.xbill.DNS.Tokenizer.Token.access$100(r3, r2, r4);
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Tokenizer.get(boolean, boolean):org.xbill.DNS.Tokenizer$Token");
    }

    public Token get() throws IOException {
        return get(false, false);
    }

    public void unget() {
        if (this.ungottenToken) {
            throw new IllegalStateException("Cannot unget multiple tokens");
        }
        if (this.current.type == 1) {
            this.line--;
        }
        this.ungottenToken = true;
    }

    public String getString() throws IOException {
        Token next = get();
        if (next.isString()) {
            return next.value;
        }
        throw exception("expected a string");
    }

    private String _getIdentifier(String expected) throws IOException {
        Token next = get();
        if (next.type == 3) {
            return next.value;
        }
        throw exception(new StringBuffer().append("expected ").append(expected).toString());
    }

    public String getIdentifier() throws IOException {
        return _getIdentifier("an identifier");
    }

    public long getLong() throws IOException {
        String next = _getIdentifier("an integer");
        if (Character.isDigit(next.charAt(0))) {
            try {
                return Long.parseLong(next);
            } catch (NumberFormatException e) {
                throw exception("expected an integer");
            }
        }
        throw exception("expected an integer");
    }

    public long getUInt32() throws IOException {
        long l = getLong();
        if (l >= 0 && l <= 4294967295L) {
            return l;
        }
        throw exception("expected an 32 bit unsigned integer");
    }

    public int getUInt16() throws IOException {
        long l = getLong();
        if (l >= 0 && l <= 65535) {
            return (int) l;
        }
        throw exception("expected an 16 bit unsigned integer");
    }

    public int getUInt8() throws IOException {
        long l = getLong();
        if (l >= 0 && l <= 255) {
            return (int) l;
        }
        throw exception("expected an 8 bit unsigned integer");
    }

    public long getTTL() throws IOException {
        try {
            return TTL.parseTTL(_getIdentifier("a TTL value"));
        } catch (NumberFormatException e) {
            throw exception("expected a TTL value");
        }
    }

    public long getTTLLike() throws IOException {
        try {
            return TTL.parse(_getIdentifier("a TTL-like value"), false);
        } catch (NumberFormatException e) {
            throw exception("expected a TTL-like value");
        }
    }

    public Name getName(Name origin) throws IOException {
        try {
            Name name = Name.fromString(_getIdentifier("a name"), origin);
            if (name.isAbsolute()) {
                return name;
            }
            throw new RelativeNameException(name);
        } catch (TextParseException e) {
            throw exception(e.getMessage());
        }
    }

    public byte[] getAddressBytes(int family) throws IOException {
        String next = _getIdentifier("an address");
        byte[] bytes = Address.toByteArray(next, family);
        if (bytes != null) {
            return bytes;
        }
        throw exception(new StringBuffer().append("Invalid address: ").append(next).toString());
    }

    public InetAddress getAddress(int family) throws IOException {
        try {
            return Address.getByAddress(_getIdentifier("an address"), family);
        } catch (UnknownHostException e) {
            throw exception(e.getMessage());
        }
    }

    public void getEOL() throws IOException {
        Token next = get();
        if (next.type != 1 && next.type != 0) {
            throw exception("expected EOL or EOF");
        }
    }

    private String remainingStrings() throws IOException {
        StringBuffer buffer = null;
        while (true) {
            Token t = get();
            if (!t.isString()) {
                break;
            }
            if (buffer == null) {
                buffer = new StringBuffer();
            }
            buffer.append(t.value);
        }
        unget();
        if (buffer == null) {
            return null;
        }
        return buffer.toString();
    }

    public byte[] getBase64(boolean required) throws IOException {
        String s = remainingStrings();
        if (s != null) {
            byte[] array = base64.fromString(s);
            if (array != null) {
                return array;
            }
            throw exception("invalid base64 encoding");
        } else if (!required) {
            return null;
        } else {
            throw exception("expected base64 encoded string");
        }
    }

    public byte[] getBase64() throws IOException {
        return getBase64(false);
    }

    public byte[] getHex(boolean required) throws IOException {
        String s = remainingStrings();
        if (s != null) {
            byte[] array = base16.fromString(s);
            if (array != null) {
                return array;
            }
            throw exception("invalid hex encoding");
        } else if (!required) {
            return null;
        } else {
            throw exception("expected hex encoded string");
        }
    }

    public byte[] getHex() throws IOException {
        return getHex(false);
    }

    public byte[] getHexString() throws IOException {
        byte[] array = base16.fromString(_getIdentifier("a hex string"));
        if (array != null) {
            return array;
        }
        throw exception("invalid hex encoding");
    }

    public byte[] getBase32String(base32 b32) throws IOException {
        byte[] array = b32.fromString(_getIdentifier("a base32 string"));
        if (array != null) {
            return array;
        }
        throw exception("invalid base32 encoding");
    }

    public TextParseException exception(String s) {
        return new TokenizerException(this.filename, this.line, s);
    }

    public void close() {
        if (this.wantClose) {
            try {
                this.is.close();
            } catch (IOException e) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close();
    }
}
