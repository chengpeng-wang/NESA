package org.mozilla.javascript;

import org.objectweb.asm.signature.SignatureVisitor;

public class Decompiler {
    public static final int CASE_GAP_PROP = 3;
    private static final int FUNCTION_END = 164;
    public static final int INDENT_GAP_PROP = 2;
    public static final int INITIAL_INDENT_PROP = 1;
    public static final int ONLY_BODY_FLAG = 1;
    public static final int TO_SOURCE_FLAG = 2;
    private static final boolean printSource = false;
    private char[] sourceBuffer = new char[128];
    private int sourceTop;

    /* access modifiers changed from: 0000 */
    public String getEncodedSource() {
        return sourceToString(0);
    }

    /* access modifiers changed from: 0000 */
    public int getCurrentOffset() {
        return this.sourceTop;
    }

    /* access modifiers changed from: 0000 */
    public int markFunctionStart(int functionType) {
        int savedOffset = getCurrentOffset();
        addToken(109);
        append((char) functionType);
        return savedOffset;
    }

    /* access modifiers changed from: 0000 */
    public int markFunctionEnd(int functionStart) {
        int offset = getCurrentOffset();
        append(164);
        return offset;
    }

    /* access modifiers changed from: 0000 */
    public void addToken(int token) {
        if (token < 0 || token > 163) {
            throw new IllegalArgumentException();
        }
        append((char) token);
    }

    /* access modifiers changed from: 0000 */
    public void addEOL(int token) {
        if (token < 0 || token > 163) {
            throw new IllegalArgumentException();
        }
        append((char) token);
        append(1);
    }

    /* access modifiers changed from: 0000 */
    public void addName(String str) {
        addToken(39);
        appendString(str);
    }

    /* access modifiers changed from: 0000 */
    public void addString(String str) {
        addToken(41);
        appendString(str);
    }

    /* access modifiers changed from: 0000 */
    public void addRegexp(String regexp, String flags) {
        addToken(48);
        appendString('/' + regexp + '/' + flags);
    }

    /* access modifiers changed from: 0000 */
    public void addNumber(double n) {
        addToken(40);
        long lbits = (long) n;
        if (((double) lbits) != n) {
            lbits = Double.doubleToLongBits(n);
            append('D');
            append((char) ((int) (lbits >> 48)));
            append((char) ((int) (lbits >> 32)));
            append((char) ((int) (lbits >> 16)));
            append((char) ((int) lbits));
            return;
        }
        if (lbits < 0) {
            Kit.codeBug();
        }
        if (lbits <= 65535) {
            append('S');
            append((char) ((int) lbits));
            return;
        }
        append('J');
        append((char) ((int) (lbits >> 48)));
        append((char) ((int) (lbits >> 32)));
        append((char) ((int) (lbits >> 16)));
        append((char) ((int) lbits));
    }

    private void appendString(String str) {
        int L = str.length();
        int lengthEncodingSize = 1;
        if (L >= 32768) {
            lengthEncodingSize = 2;
        }
        int nextTop = (this.sourceTop + lengthEncodingSize) + L;
        if (nextTop > this.sourceBuffer.length) {
            increaseSourceCapacity(nextTop);
        }
        if (L >= 32768) {
            this.sourceBuffer[this.sourceTop] = (char) ((L >>> 16) | 32768);
            this.sourceTop++;
        }
        this.sourceBuffer[this.sourceTop] = (char) L;
        this.sourceTop++;
        str.getChars(0, L, this.sourceBuffer, this.sourceTop);
        this.sourceTop = nextTop;
    }

    private void append(char c) {
        if (this.sourceTop == this.sourceBuffer.length) {
            increaseSourceCapacity(this.sourceTop + 1);
        }
        this.sourceBuffer[this.sourceTop] = c;
        this.sourceTop++;
    }

    private void increaseSourceCapacity(int minimalCapacity) {
        if (minimalCapacity <= this.sourceBuffer.length) {
            Kit.codeBug();
        }
        int newCapacity = this.sourceBuffer.length * 2;
        if (newCapacity < minimalCapacity) {
            newCapacity = minimalCapacity;
        }
        char[] tmp = new char[newCapacity];
        System.arraycopy(this.sourceBuffer, 0, tmp, 0, this.sourceTop);
        this.sourceBuffer = tmp;
    }

    private String sourceToString(int offset) {
        if (offset < 0 || this.sourceTop < offset) {
            Kit.codeBug();
        }
        return new String(this.sourceBuffer, offset, this.sourceTop - offset);
    }

    public static String decompile(String source, int flags, UintMap properties) {
        int length = source.length();
        if (length == 0) {
            return "";
        }
        int indent = properties.getInt(1, 0);
        if (indent < 0) {
            throw new IllegalArgumentException();
        }
        int indentGap = properties.getInt(2, 4);
        if (indentGap < 0) {
            throw new IllegalArgumentException();
        }
        int caseGap = properties.getInt(3, 2);
        if (caseGap < 0) {
            throw new IllegalArgumentException();
        }
        boolean toSource;
        int topFunctionType;
        StringBuilder result = new StringBuilder();
        boolean justFunctionBody = (flags & 1) != 0;
        if ((flags & 2) != 0) {
            toSource = true;
        } else {
            toSource = false;
        }
        int braceNesting = 0;
        boolean afterFirstEOL = false;
        int i = 0;
        if (source.charAt(0) == 136) {
            i = 0 + 1;
            topFunctionType = -1;
        } else {
            topFunctionType = source.charAt(1);
        }
        if (!toSource) {
            result.append(10);
            for (int j = 0; j < indent; j++) {
                result.append(' ');
            }
        } else if (topFunctionType == 2) {
            result.append('(');
        }
        while (i < length) {
            switch (source.charAt(i)) {
                case 1:
                    if (!toSource) {
                        boolean newLine = true;
                        if (!afterFirstEOL) {
                            afterFirstEOL = true;
                            if (justFunctionBody) {
                                result.setLength(0);
                                indent -= indentGap;
                                newLine = false;
                            }
                        }
                        if (newLine) {
                            result.append(10);
                        }
                        if (i + 1 < length) {
                            int less = 0;
                            int nextToken = source.charAt(i + 1);
                            if (nextToken == 115 || nextToken == 116) {
                                less = indentGap - caseGap;
                            } else if (nextToken == 86) {
                                less = indentGap;
                            } else if (nextToken == 39) {
                                if (source.charAt(getSourceStringEnd(source, i + 2)) == 'g') {
                                    less = indentGap;
                                }
                            }
                            while (less < indent) {
                                result.append(' ');
                                less++;
                            }
                            break;
                        }
                    }
                    break;
                case 4:
                    result.append("return");
                    if (82 != getNext(source, length, i)) {
                        result.append(' ');
                        break;
                    }
                    break;
                case 9:
                    result.append(" | ");
                    break;
                case 10:
                    result.append(" ^ ");
                    break;
                case 11:
                    result.append(" & ");
                    break;
                case 12:
                    result.append(" == ");
                    break;
                case 13:
                    result.append(" != ");
                    break;
                case 14:
                    result.append(" < ");
                    break;
                case 15:
                    result.append(" <= ");
                    break;
                case 16:
                    result.append(" > ");
                    break;
                case 17:
                    result.append(" >= ");
                    break;
                case 18:
                    result.append(" << ");
                    break;
                case 19:
                    result.append(" >> ");
                    break;
                case 20:
                    result.append(" >>> ");
                    break;
                case 21:
                    result.append(" + ");
                    break;
                case 22:
                    result.append(" - ");
                    break;
                case 23:
                    result.append(" * ");
                    break;
                case 24:
                    result.append(" / ");
                    break;
                case 25:
                    result.append(" % ");
                    break;
                case 26:
                    result.append('!');
                    break;
                case 27:
                    result.append('~');
                    break;
                case 28:
                    result.append(SignatureVisitor.EXTENDS);
                    break;
                case 29:
                    result.append(SignatureVisitor.SUPER);
                    break;
                case 30:
                    result.append("new ");
                    break;
                case 31:
                    result.append("delete ");
                    break;
                case ' ':
                    result.append("typeof ");
                    break;
                case '\'':
                case '0':
                    i = printSourceString(source, i + 1, false, result);
                    continue;
                case '(':
                    i = printSourceNumber(source, i + 1, result);
                    continue;
                case ')':
                    i = printSourceString(source, i + 1, true, result);
                    continue;
                case '*':
                    result.append("null");
                    break;
                case '+':
                    result.append("this");
                    break;
                case ',':
                    result.append("false");
                    break;
                case '-':
                    result.append("true");
                    break;
                case '.':
                    result.append(" === ");
                    break;
                case '/':
                    result.append(" !== ");
                    break;
                case '2':
                    result.append("throw ");
                    break;
                case '4':
                    result.append(" in ");
                    break;
                case '5':
                    result.append(" instanceof ");
                    break;
                case 'B':
                    result.append(": ");
                    break;
                case 'H':
                    result.append("yield ");
                    break;
                case 'Q':
                    result.append("try ");
                    break;
                case 'R':
                    result.append(';');
                    if (1 != getNext(source, length, i)) {
                        result.append(' ');
                        break;
                    }
                    break;
                case 'S':
                    result.append('[');
                    break;
                case 'T':
                    result.append(']');
                    break;
                case 'U':
                    braceNesting++;
                    if (1 == getNext(source, length, i)) {
                        indent += indentGap;
                    }
                    result.append('{');
                    break;
                case 'V':
                    braceNesting--;
                    if (!(justFunctionBody && braceNesting == 0)) {
                        result.append('}');
                        switch (getNext(source, length, i)) {
                            case 1:
                            case 164:
                                indent -= indentGap;
                                break;
                            case 113:
                            case 117:
                                indent -= indentGap;
                                result.append(' ');
                                break;
                        }
                    }
                    break;
                case 'W':
                    result.append('(');
                    break;
                case 'X':
                    result.append(')');
                    if (85 == getNext(source, length, i)) {
                        result.append(' ');
                        break;
                    }
                    break;
                case 'Y':
                    result.append(", ");
                    break;
                case 'Z':
                    result.append(" = ");
                    break;
                case '[':
                    result.append(" |= ");
                    break;
                case '\\':
                    result.append(" ^= ");
                    break;
                case ']':
                    result.append(" &= ");
                    break;
                case '^':
                    result.append(" <<= ");
                    break;
                case '_':
                    result.append(" >>= ");
                    break;
                case '`':
                    result.append(" >>>= ");
                    break;
                case 'a':
                    result.append(" += ");
                    break;
                case 'b':
                    result.append(" -= ");
                    break;
                case 'c':
                    result.append(" *= ");
                    break;
                case 'd':
                    result.append(" /= ");
                    break;
                case 'e':
                    result.append(" %= ");
                    break;
                case 'f':
                    result.append(" ? ");
                    break;
                case 'g':
                    if (1 != getNext(source, length, i)) {
                        result.append(" : ");
                        break;
                    }
                    result.append(':');
                    break;
                case 'h':
                    result.append(" || ");
                    break;
                case 'i':
                    result.append(" && ");
                    break;
                case 'j':
                    result.append("++");
                    break;
                case 'k':
                    result.append("--");
                    break;
                case 'l':
                    result.append('.');
                    break;
                case 'm':
                    i++;
                    result.append("function ");
                    break;
                case 'p':
                    result.append("if ");
                    break;
                case 'q':
                    result.append("else ");
                    break;
                case 'r':
                    result.append("switch ");
                    break;
                case 's':
                    result.append("case ");
                    break;
                case 't':
                    result.append("default");
                    break;
                case 'u':
                    result.append("while ");
                    break;
                case 'v':
                    result.append("do ");
                    break;
                case 'w':
                    result.append("for ");
                    break;
                case 'x':
                    result.append("break");
                    if (39 == getNext(source, length, i)) {
                        result.append(' ');
                        break;
                    }
                    break;
                case 'y':
                    result.append("continue");
                    if (39 == getNext(source, length, i)) {
                        result.append(' ');
                        break;
                    }
                    break;
                case 'z':
                    result.append("var ");
                    break;
                case '{':
                    result.append("with ");
                    break;
                case '|':
                    result.append("catch ");
                    break;
                case '}':
                    result.append("finally ");
                    break;
                case '~':
                    result.append("void ");
                    break;
                case 143:
                    result.append("..");
                    break;
                case 144:
                    result.append("::");
                    break;
                case 146:
                    result.append(".(");
                    break;
                case 147:
                    result.append('@');
                    break;
                case 151:
                case 152:
                    result.append(source.charAt(i) == 151 ? "get " : "set ");
                    i = printSourceString(source, (i + 1) + 1, false, result) + 1;
                    break;
                case 153:
                    result.append("let ");
                    break;
                case 154:
                    result.append("const ");
                    break;
                case 160:
                    result.append("debugger;\n");
                    break;
                case 164:
                    break;
                default:
                    throw new RuntimeException("Token: " + Token.name(source.charAt(i)));
            }
            i++;
        }
        if (toSource) {
            if (topFunctionType == 2) {
                result.append(')');
            }
        } else if (!justFunctionBody) {
            result.append(10);
        }
        return result.toString();
    }

    private static int getNext(String source, int length, int i) {
        return i + 1 < length ? source.charAt(i + 1) : 0;
    }

    private static int getSourceStringEnd(String source, int offset) {
        return printSourceString(source, offset, false, null);
    }

    private static int printSourceString(String source, int offset, boolean asQuotedString, StringBuilder sb) {
        int length = source.charAt(offset);
        offset++;
        if ((32768 & length) != 0) {
            length = ((length & 32767) << 16) | source.charAt(offset);
            offset++;
        }
        if (sb != null) {
            String str = source.substring(offset, offset + length);
            if (asQuotedString) {
                sb.append('\"');
                sb.append(ScriptRuntime.escapeString(str));
                sb.append('\"');
            } else {
                sb.append(str);
            }
        }
        return offset + length;
    }

    private static int printSourceNumber(String source, int offset, StringBuilder sb) {
        double number = 0.0d;
        char type = source.charAt(offset);
        offset++;
        if (type == 'S') {
            if (sb != null) {
                number = (double) source.charAt(offset);
            }
            offset++;
        } else if (type == 'J' || type == 'D') {
            if (sb != null) {
                long lbits = (((((long) source.charAt(offset)) << 48) | (((long) source.charAt(offset + 1)) << 32)) | (((long) source.charAt(offset + 2)) << 16)) | ((long) source.charAt(offset + 3));
                if (type == 'J') {
                    number = (double) lbits;
                } else {
                    number = Double.longBitsToDouble(lbits);
                }
            }
            offset += 4;
        } else {
            throw new RuntimeException();
        }
        if (sb != null) {
            sb.append(ScriptRuntime.numberToString(number, 10));
        }
        return offset;
    }
}
