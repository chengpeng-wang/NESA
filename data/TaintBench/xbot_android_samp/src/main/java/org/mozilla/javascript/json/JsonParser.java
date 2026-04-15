package org.mozilla.javascript.json;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.objectweb.asm.signature.SignatureVisitor;

public class JsonParser {
    static final /* synthetic */ boolean $assertionsDisabled = (!JsonParser.class.desiredAssertionStatus());
    private Context cx;
    private int length;
    private int pos;
    private Scriptable scope;
    private String src;

    public static class ParseException extends Exception {
        static final long serialVersionUID = 4804542791749920772L;

        ParseException(String message) {
            super(message);
        }

        ParseException(Exception cause) {
            super(cause);
        }
    }

    public JsonParser(Context cx, Scriptable scope) {
        this.cx = cx;
        this.scope = scope;
    }

    public synchronized Object parseValue(String json) throws ParseException {
        Object value;
        if (json == null) {
            throw new ParseException("Input string may not be null");
        }
        this.pos = 0;
        this.length = json.length();
        this.src = json;
        value = readValue();
        consumeWhitespace();
        if (this.pos < this.length) {
            throw new ParseException("Expected end of stream at char " + this.pos);
        }
        return value;
    }

    private Object readValue() throws ParseException {
        consumeWhitespace();
        if (this.pos < this.length) {
            String str = this.src;
            int i = this.pos;
            this.pos = i + 1;
            char c = str.charAt(i);
            switch (c) {
                case '\"':
                    return readString();
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return readNumber(c);
                case '[':
                    return readArray();
                case 'f':
                    return readFalse();
                case 'n':
                    return readNull();
                case 't':
                    return readTrue();
                case '{':
                    return readObject();
                default:
                    throw new ParseException("Unexpected token: " + c);
            }
        }
        throw new ParseException("Empty JSON string");
    }

    /* JADX WARNING: Missing block: B:20:0x0057, code skipped:
            consumeWhitespace();
     */
    private java.lang.Object readObject() throws org.mozilla.javascript.json.JsonParser.ParseException {
        /*
        r10 = this;
        r10.consumeWhitespace();
        r7 = r10.cx;
        r8 = r10.scope;
        r5 = r7.newObject(r8);
        r7 = r10.pos;
        r8 = r10.length;
        if (r7 >= r8) goto L_0x0024;
    L_0x0011:
        r7 = r10.src;
        r8 = r10.pos;
        r7 = r7.charAt(r8);
        r8 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r7 != r8) goto L_0x0024;
    L_0x001d:
        r7 = r10.pos;
        r7 = r7 + 1;
        r10.pos = r7;
    L_0x0023:
        return r5;
    L_0x0024:
        r4 = 0;
    L_0x0025:
        r7 = r10.pos;
        r8 = r10.length;
        if (r7 >= r8) goto L_0x0086;
    L_0x002b:
        r7 = r10.src;
        r8 = r10.pos;
        r9 = r8 + 1;
        r10.pos = r9;
        r0 = r7.charAt(r8);
        switch(r0) {
            case 34: goto L_0x005b;
            case 44: goto L_0x004c;
            case 125: goto L_0x0042;
            default: goto L_0x003a;
        };
    L_0x003a:
        r7 = new org.mozilla.javascript.json.JsonParser$ParseException;
        r8 = "Unexpected token in object literal";
        r7.m507init(r8);
        throw r7;
    L_0x0042:
        if (r4 != 0) goto L_0x0023;
    L_0x0044:
        r7 = new org.mozilla.javascript.json.JsonParser$ParseException;
        r8 = "Unexpected comma in object literal";
        r7.m507init(r8);
        throw r7;
    L_0x004c:
        if (r4 != 0) goto L_0x0056;
    L_0x004e:
        r7 = new org.mozilla.javascript.json.JsonParser$ParseException;
        r8 = "Unexpected comma in object literal";
        r7.m507init(r8);
        throw r7;
    L_0x0056:
        r4 = 0;
    L_0x0057:
        r10.consumeWhitespace();
        goto L_0x0025;
    L_0x005b:
        if (r4 == 0) goto L_0x0065;
    L_0x005d:
        r7 = new org.mozilla.javascript.json.JsonParser$ParseException;
        r8 = "Missing comma in object literal";
        r7.m507init(r8);
        throw r7;
    L_0x0065:
        r1 = r10.readString();
        r7 = 58;
        r10.consume(r7);
        r6 = r10.readValue();
        r2 = org.mozilla.javascript.ScriptRuntime.indexFromString(r1);
        r7 = 0;
        r7 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r7 >= 0) goto L_0x0081;
    L_0x007c:
        r5.put(r1, r5, r6);
    L_0x007f:
        r4 = 1;
        goto L_0x0057;
    L_0x0081:
        r7 = (int) r2;
        r5.put(r7, r5, r6);
        goto L_0x007f;
    L_0x0086:
        r7 = new org.mozilla.javascript.json.JsonParser$ParseException;
        r8 = "Unterminated object literal";
        r7.m507init(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.json.JsonParser.readObject():java.lang.Object");
    }

    private Object readArray() throws ParseException {
        consumeWhitespace();
        if (this.pos >= this.length || this.src.charAt(this.pos) != ']') {
            List<Object> list = new ArrayList();
            boolean needsComma = false;
            while (this.pos < this.length) {
                switch (this.src.charAt(this.pos)) {
                    case ',':
                        if (needsComma) {
                            needsComma = false;
                            this.pos++;
                            break;
                        }
                        throw new ParseException("Unexpected comma in array literal");
                    case ']':
                        if (needsComma) {
                            this.pos++;
                            return this.cx.newArray(this.scope, list.toArray());
                        }
                        throw new ParseException("Unexpected comma in array literal");
                    default:
                        if (!needsComma) {
                            list.add(readValue());
                            needsComma = true;
                            break;
                        }
                        throw new ParseException("Missing comma in array literal");
                }
                consumeWhitespace();
            }
            throw new ParseException("Unterminated array literal");
        }
        this.pos++;
        return this.cx.newArray(this.scope, 0);
    }

    private String readString() throws ParseException {
        String str;
        int i;
        char c;
        int stringStart = this.pos;
        while (this.pos < this.length) {
            str = this.src;
            i = this.pos;
            this.pos = i + 1;
            c = str.charAt(i);
            if (c <= 31) {
                throw new ParseException("String contains control character");
            } else if (c == '\\') {
                break;
            } else if (c == '\"') {
                return this.src.substring(stringStart, this.pos - 1);
            }
        }
        StringBuilder b = new StringBuilder();
        while (this.pos < this.length) {
            if ($assertionsDisabled || this.src.charAt(this.pos - 1) == '\\') {
                b.append(this.src, stringStart, this.pos - 1);
                if (this.pos >= this.length) {
                    throw new ParseException("Unterminated string");
                }
                str = this.src;
                i = this.pos;
                this.pos = i + 1;
                c = str.charAt(i);
                switch (c) {
                    case '\"':
                        b.append('\"');
                        break;
                    case '/':
                        b.append('/');
                        break;
                    case '\\':
                        b.append('\\');
                        break;
                    case 'b':
                        b.append(8);
                        break;
                    case 'f':
                        b.append(12);
                        break;
                    case 'n':
                        b.append(10);
                        break;
                    case 'r':
                        b.append(13);
                        break;
                    case 't':
                        b.append(9);
                        break;
                    case 'u':
                        if (this.length - this.pos >= 5) {
                            int code = (((fromHex(this.src.charAt(this.pos + 0)) << 12) | (fromHex(this.src.charAt(this.pos + 1)) << 8)) | (fromHex(this.src.charAt(this.pos + 2)) << 4)) | fromHex(this.src.charAt(this.pos + 3));
                            if (code >= 0) {
                                this.pos += 4;
                                b.append((char) code);
                                break;
                            }
                            throw new ParseException("Invalid character code: " + this.src.substring(this.pos, this.pos + 4));
                        }
                        throw new ParseException("Invalid character code: \\u" + this.src.substring(this.pos));
                    default:
                        throw new ParseException("Unexpected character in string: '\\" + c + "'");
                }
                stringStart = this.pos;
                while (this.pos < this.length) {
                    str = this.src;
                    i = this.pos;
                    this.pos = i + 1;
                    c = str.charAt(i);
                    if (c <= 31) {
                        throw new ParseException("String contains control character");
                    } else if (c == '\\') {
                        continue;
                    } else if (c == '\"') {
                        b.append(this.src, stringStart, this.pos - 1);
                        return b.toString();
                    }
                }
            }
            throw new AssertionError();
        }
        throw new ParseException("Unterminated string literal");
    }

    private int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c < 'A' || c > 'F') {
            return (c < 'a' || c > 'f') ? -1 : (c - 97) + 10;
        } else {
            return (c - 65) + 10;
        }
    }

    private Number readNumber(char c) throws ParseException {
        if ($assertionsDisabled || c == SignatureVisitor.SUPER || (c >= '0' && c <= '9')) {
            int numberStart = this.pos - 1;
            if (c == SignatureVisitor.SUPER) {
                c = nextOrNumberError(numberStart);
                if (c < '0' || c > '9') {
                    throw numberError(numberStart, this.pos);
                }
            }
            if (c != '0') {
                readDigits();
            }
            if (this.pos < this.length && this.src.charAt(this.pos) == '.') {
                this.pos++;
                c = nextOrNumberError(numberStart);
                if (c < '0' || c > '9') {
                    throw numberError(numberStart, this.pos);
                }
                readDigits();
            }
            if (this.pos < this.length) {
                c = this.src.charAt(this.pos);
                if (c == 'e' || c == 'E') {
                    this.pos++;
                    c = nextOrNumberError(numberStart);
                    if (c == SignatureVisitor.SUPER || c == SignatureVisitor.EXTENDS) {
                        c = nextOrNumberError(numberStart);
                    }
                    if (c < '0' || c > '9') {
                        throw numberError(numberStart, this.pos);
                    }
                    readDigits();
                }
            }
            double dval = Double.parseDouble(this.src.substring(numberStart, this.pos));
            int ival = (int) dval;
            if (((double) ival) == dval) {
                return Integer.valueOf(ival);
            }
            return Double.valueOf(dval);
        }
        throw new AssertionError();
    }

    private ParseException numberError(int start, int end) {
        return new ParseException("Unsupported number format: " + this.src.substring(start, end));
    }

    private char nextOrNumberError(int numberStart) throws ParseException {
        if (this.pos >= this.length) {
            throw numberError(numberStart, this.length);
        }
        String str = this.src;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    private void readDigits() {
        while (this.pos < this.length) {
            char c = this.src.charAt(this.pos);
            if (c >= '0' && c <= '9') {
                this.pos++;
            } else {
                return;
            }
        }
    }

    private Boolean readTrue() throws ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'r' && this.src.charAt(this.pos + 1) == 'u' && this.src.charAt(this.pos + 2) == 'e') {
            this.pos += 3;
            return Boolean.TRUE;
        }
        throw new ParseException("Unexpected token: t");
    }

    private Boolean readFalse() throws ParseException {
        if (this.length - this.pos >= 4 && this.src.charAt(this.pos) == 'a' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 's' && this.src.charAt(this.pos + 3) == 'e') {
            this.pos += 4;
            return Boolean.FALSE;
        }
        throw new ParseException("Unexpected token: f");
    }

    private Object readNull() throws ParseException {
        if (this.length - this.pos >= 3 && this.src.charAt(this.pos) == 'u' && this.src.charAt(this.pos + 1) == 'l' && this.src.charAt(this.pos + 2) == 'l') {
            this.pos += 3;
            return null;
        }
        throw new ParseException("Unexpected token: n");
    }

    private void consumeWhitespace() {
        while (this.pos < this.length) {
            switch (this.src.charAt(this.pos)) {
                case 9:
                case 10:
                case 13:
                case ' ':
                    this.pos++;
                default:
                    return;
            }
        }
    }

    private void consume(char token) throws ParseException {
        consumeWhitespace();
        if (this.pos >= this.length) {
            throw new ParseException("Expected " + token + " but reached end of stream");
        }
        String str = this.src;
        int i = this.pos;
        this.pos = i + 1;
        char c = str.charAt(i);
        if (c != token) {
            throw new ParseException("Expected " + token + " found " + c);
        }
    }
}
