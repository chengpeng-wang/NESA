package flexjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.apache.log4j.net.SyslogAppender;

public class JSONTokener {
    private int index;
    private char lastChar;
    private Reader reader;
    private boolean useLastChar;

    public JSONTokener(Reader reader) {
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader);
        }
        this.reader = reader;
        this.useLastChar = false;
        this.index = 0;
    }

    public JSONTokener(String s) {
        this(new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.useLastChar || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.useLastChar = true;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 55;
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 87;
    }

    public boolean more() throws JSONException {
        if (next() == 0) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        if (this.useLastChar) {
            this.useLastChar = false;
            if (this.lastChar != 0) {
                this.index++;
            }
            return this.lastChar;
        }
        try {
            int c = this.reader.read();
            if (c <= 0) {
                this.lastChar = 0;
                return 0;
            }
            this.index++;
            this.lastChar = (char) c;
            return this.lastChar;
        } catch (IOException exc) {
            throw new JSONException(exc);
        }
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n == c) {
            return n;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] buffer = new char[n];
        int pos = 0;
        if (this.useLastChar) {
            this.useLastChar = false;
            buffer[0] = this.lastChar;
            pos = 1;
        }
        while (pos < n) {
            try {
                int len = this.reader.read(buffer, pos, n - pos);
                if (len == -1) {
                    break;
                }
                pos += len;
            } catch (IOException exc) {
                throw new JSONException(exc);
            }
        }
        this.index += pos;
        if (pos < n) {
            throw syntaxError("Substring bounds error");
        }
        this.lastChar = buffer[n - 1];
        return new String(buffer);
    }

    public char nextClean() throws JSONException {
        char c;
        do {
            c = next();
            if (c == 0) {
                break;
            }
        } while (c <= ' ');
        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            switch (c) {
                case 0:
                case 10:
                case 13:
                    throw syntaxError("Unterminated string");
                case '\\':
                    c = next();
                    switch (c) {
                        case 'b':
                            sb.append(8);
                            break;
                        case HttpStatus.SC_PROCESSING /*102*/:
                            sb.append(12);
                            break;
                        case 'n':
                            sb.append(10);
                            break;
                        case 'r':
                            sb.append(13);
                            break;
                        case 't':
                            sb.append(9);
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        case 'x':
                            sb.append((char) Integer.parseInt(next(2), 16));
                            break;
                        default:
                            sb.append(c);
                            break;
                    }
                default:
                    if (c != quote) {
                        sb.append(c);
                        break;
                    }
                    return sb.toString();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    public java.lang.String nextTo(char r4) throws flexjson.JSONException {
        /*
        r3 = this;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
    L_0x0005:
        r0 = r3.next();
        if (r0 == r4) goto L_0x0015;
    L_0x000b:
        if (r0 == 0) goto L_0x0015;
    L_0x000d:
        r2 = 10;
        if (r0 == r2) goto L_0x0015;
    L_0x0011:
        r2 = 13;
        if (r0 != r2) goto L_0x0023;
    L_0x0015:
        if (r0 == 0) goto L_0x001a;
    L_0x0017:
        r3.back();
    L_0x001a:
        r2 = r1.toString();
        r2 = r2.trim();
        return r2;
    L_0x0023:
        r1.append(r0);
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: flexjson.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    public java.lang.String nextTo(java.lang.String r4) throws flexjson.JSONException {
        /*
        r3 = this;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
    L_0x0005:
        r0 = r3.next();
        r2 = r4.indexOf(r0);
        if (r2 >= 0) goto L_0x0019;
    L_0x000f:
        if (r0 == 0) goto L_0x0019;
    L_0x0011:
        r2 = 10;
        if (r0 == r2) goto L_0x0019;
    L_0x0015:
        r2 = 13;
        if (r0 != r2) goto L_0x0027;
    L_0x0019:
        if (r0 == 0) goto L_0x001e;
    L_0x001b:
        r3.back();
    L_0x001e:
        r2 = r1.toString();
        r2 = r2.trim();
        return r2;
    L_0x0027:
        r1.append(r0);
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: flexjson.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case '\"':
            case '\'':
                return nextString(c);
            case SyslogAppender.LOG_SYSLOG /*40*/:
            case '[':
                back();
                return parseArray();
            case '{':
                back();
                return parseObject();
            default:
                StringBuilder sb = new StringBuilder();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = next();
                }
                back();
                String s = sb.toString().trim();
                if (!s.equals("")) {
                    return stringToValue(s);
                }
                throw syntaxError("Missing value");
        }
    }

    public char skipTo(char to) throws JSONException {
        try {
            char c;
            int startIndex = this.index;
            this.reader.mark(Integer.MAX_VALUE);
            do {
                c = next();
                if (c == 0) {
                    this.reader.reset();
                    this.index = startIndex;
                    break;
                }
            } while (c != to);
            back();
            return c;
        } catch (IOException exc) {
            throw new JSONException(exc);
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(message + toString());
    }

    public String toString() {
        return " at character " + this.index;
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> jsonObject = new HashMap();
        if (nextClean() != '{') {
            throw syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (nextClean()) {
                case 0:
                    throw syntaxError("A JSONObject text must end with '}'");
                case '}':
                    break;
                default:
                    back();
                    String key = nextValue().toString();
                    char c = nextClean();
                    if (c == '=') {
                        if (next() != '>') {
                            back();
                        }
                    } else if (c != ':') {
                        throw syntaxError("Expected a ':' after a key");
                    }
                    putOnce(jsonObject, key, nextValue());
                    switch (nextClean()) {
                        case ',':
                        case ';':
                            if (nextClean() == '}') {
                                break;
                            }
                            back();
                        case '}':
                            break;
                        default:
                            throw syntaxError("Expected a ',' or '}'");
                    }
            }
        }
        return jsonObject;
    }

    private void putOnce(Map<String, Object> jsonObject, String key, Object value) {
        if (key == null) {
            return;
        }
        if (jsonObject.containsKey(key)) {
            throw new JSONException("Duplicate key \"" + key + "\"");
        }
        jsonObject.put(key, value);
    }

    public java.util.List<java.lang.Object> parseArray() {
        /*
        r6 = this;
        r5 = 93;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r0 = r6.nextClean();
        r3 = 91;
        if (r0 != r3) goto L_0x0018;
    L_0x000f:
        r2 = 93;
    L_0x0011:
        r3 = r6.nextClean();
        if (r3 != r5) goto L_0x0026;
    L_0x0017:
        return r1;
    L_0x0018:
        r3 = 40;
        if (r0 != r3) goto L_0x001f;
    L_0x001c:
        r2 = 41;
        goto L_0x0011;
    L_0x001f:
        r3 = "A JSONArray text must start with '['";
        r3 = r6.syntaxError(r3);
        throw r3;
    L_0x0026:
        r6.back();
    L_0x0029:
        r3 = r6.nextClean();
        r4 = 44;
        if (r3 != r4) goto L_0x0046;
    L_0x0031:
        r6.back();
        r3 = 0;
        r1.add(r3);
    L_0x0038:
        r0 = r6.nextClean();
        switch(r0) {
            case 41: goto L_0x005b;
            case 44: goto L_0x0051;
            case 59: goto L_0x0051;
            case 93: goto L_0x005b;
            default: goto L_0x003f;
        };
    L_0x003f:
        r3 = "Expected a ',' or ']'";
        r3 = r6.syntaxError(r3);
        throw r3;
    L_0x0046:
        r6.back();
        r3 = r6.nextValue();
        r1.add(r3);
        goto L_0x0038;
    L_0x0051:
        r3 = r6.nextClean();
        if (r3 == r5) goto L_0x0017;
    L_0x0057:
        r6.back();
        goto L_0x0029;
    L_0x005b:
        if (r2 == r0) goto L_0x0017;
    L_0x005d:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Expected a '";
        r3 = r3.append(r4);
        r3 = r3.append(r2);
        r4 = "'";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r3 = r6.syntaxError(r3);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: flexjson.JSONTokener.parseArray():java.util.List");
    }

    private Object stringToValue(String s) {
        if (s.equals("")) {
            return s;
        }
        if (s.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (s.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (s.equalsIgnoreCase("null")) {
            return null;
        }
        char b = s.charAt(0);
        if ((b < '0' || b > '9') && b != '.' && b != '-' && b != '+') {
            return s;
        }
        if (b == '0') {
            if (s.length() <= 2 || !(s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
                try {
                    return Integer.valueOf(Integer.parseInt(s, 8));
                } catch (Exception e) {
                }
            } else {
                try {
                    return Integer.valueOf(Integer.parseInt(s.substring(2), 16));
                } catch (Exception e2) {
                }
            }
        }
        try {
            return new Integer(s);
        } catch (Exception e3) {
            try {
                return new Long(s);
            } catch (Exception e4) {
                try {
                    return new Double(s);
                } catch (Exception e5) {
                    return s;
                }
            }
        }
    }
}
