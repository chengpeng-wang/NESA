package javax.activation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class MimeTypeParameterList {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
    private Hashtable parameters = new Hashtable();

    public MimeTypeParameterList(String parameterList) throws MimeTypeParseException {
        parse(parameterList);
    }

    /* access modifiers changed from: protected */
    public void parse(String parameterList) throws MimeTypeParseException {
        if (parameterList != null) {
            int length = parameterList.length();
            if (length > 0) {
                int i = skipWhiteSpace(parameterList, 0);
                while (i < length && parameterList.charAt(i) == ';') {
                    i = skipWhiteSpace(parameterList, i + 1);
                    if (i < length) {
                        int lastIndex = i;
                        while (i < length && isTokenChar(parameterList.charAt(i))) {
                            i++;
                        }
                        String name = parameterList.substring(lastIndex, i).toLowerCase(Locale.ENGLISH);
                        i = skipWhiteSpace(parameterList, i);
                        if (i >= length || parameterList.charAt(i) != '=') {
                            throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
                        }
                        i = skipWhiteSpace(parameterList, i + 1);
                        if (i >= length) {
                            throw new MimeTypeParseException("Couldn't find a value for parameter named " + name);
                        }
                        String value;
                        char c = parameterList.charAt(i);
                        if (c == '\"') {
                            i++;
                            if (i >= length) {
                                throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                            }
                            lastIndex = i;
                            while (i < length) {
                                c = parameterList.charAt(i);
                                if (c == '\"') {
                                    break;
                                }
                                if (c == '\\') {
                                    i++;
                                }
                                i++;
                            }
                            if (c != '\"') {
                                throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                            }
                            value = unquote(parameterList.substring(lastIndex, i));
                            i++;
                        } else if (isTokenChar(c)) {
                            lastIndex = i;
                            while (i < length && isTokenChar(parameterList.charAt(i))) {
                                i++;
                            }
                            value = parameterList.substring(lastIndex, i);
                        } else {
                            throw new MimeTypeParseException("Unexpected character encountered at index " + i);
                        }
                        this.parameters.put(name, value);
                        i = skipWhiteSpace(parameterList, i);
                    } else {
                        return;
                    }
                }
                if (i < length) {
                    throw new MimeTypeParseException("More characters encountered in input than expected.");
                }
            }
        }
    }

    public int size() {
        return this.parameters.size();
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public String get(String name) {
        return (String) this.parameters.get(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public void set(String name, String value) {
        this.parameters.put(name.trim().toLowerCase(Locale.ENGLISH), value);
    }

    public void remove(String name) {
        this.parameters.remove(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        return this.parameters.keys();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(this.parameters.size() * 16);
        Enumeration keys = this.parameters.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            buffer.append("; ");
            buffer.append(key);
            buffer.append('=');
            buffer.append(quote((String) this.parameters.get(key)));
        }
        return buffer.toString();
    }

    private static boolean isTokenChar(char c) {
        return c > ' ' && c < 127 && TSPECIALS.indexOf(c) < 0;
    }

    private static int skipWhiteSpace(String rawdata, int i) {
        int length = rawdata.length();
        while (i < length && Character.isWhitespace(rawdata.charAt(i))) {
            i++;
        }
        return i;
    }

    private static String quote(String value) {
        int i;
        boolean needsQuotes = false;
        int length = value.length();
        for (i = 0; i < length && !needsQuotes; i++) {
            needsQuotes = !isTokenChar(value.charAt(i));
        }
        if (!needsQuotes) {
            return value;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity((int) (((double) length) * 1.5d));
        buffer.append('\"');
        for (i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '\"') {
                buffer.append('\\');
            }
            buffer.append(c);
        }
        buffer.append('\"');
        return buffer.toString();
    }

    private static String unquote(String value) {
        int valueLength = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(valueLength);
        boolean escaped = false;
        for (int i = 0; i < valueLength; i++) {
            char currentChar = value.charAt(i);
            if (!escaped && currentChar != '\\') {
                buffer.append(currentChar);
            } else if (escaped) {
                buffer.append(currentChar);
                escaped = false;
            } else {
                escaped = true;
            }
        }
        return buffer.toString();
    }
}
