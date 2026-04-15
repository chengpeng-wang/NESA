package javax.mail.internet;

import com.googleprojects.mm.MMMailContentUtil;

public class HeaderTokenizer {
    private static final Token EOFToken = new Token(-4, null);
    public static final String MIME = "()<>@,;:\\\"\t []/?=";
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";
    private int currentPos;
    private String delimiters;
    private int maxPos;
    private int nextPos;
    private int peekPos;
    private boolean skipComments;
    private String string;

    public static class Token {
        public static final int ATOM = -1;
        public static final int COMMENT = -3;
        public static final int EOF = -4;
        public static final int QUOTEDSTRING = -2;
        private int type;
        private String value;

        public Token(int type, String value) {
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return this.type;
        }

        public String getValue() {
            return this.value;
        }
    }

    public HeaderTokenizer(String header, String delimiters, boolean skipComments) {
        if (header == null) {
            header = MMMailContentUtil.MM_MESSAGE_SUBJECT;
        }
        this.string = header;
        this.skipComments = skipComments;
        this.delimiters = delimiters;
        this.peekPos = 0;
        this.nextPos = 0;
        this.currentPos = 0;
        this.maxPos = this.string.length();
    }

    public HeaderTokenizer(String header, String delimiters) {
        this(header, delimiters, true);
    }

    public HeaderTokenizer(String header) {
        this(header, RFC822);
    }

    public Token next() throws ParseException {
        this.currentPos = this.nextPos;
        Token tk = getNext();
        int i = this.currentPos;
        this.peekPos = i;
        this.nextPos = i;
        return tk;
    }

    public Token peek() throws ParseException {
        this.currentPos = this.peekPos;
        Token tk = getNext();
        this.peekPos = this.currentPos;
        return tk;
    }

    public String getRemainder() {
        return this.string.substring(this.nextPos);
    }

    private Token getNext() throws ParseException {
        if (this.currentPos >= this.maxPos) {
            return EOFToken;
        }
        if (skipWhiteSpace() == -4) {
            return EOFToken;
        }
        int start;
        String s;
        boolean filter = false;
        char c = this.string.charAt(this.currentPos);
        while (c == '(') {
            start = this.currentPos + 1;
            this.currentPos = start;
            int nesting = 1;
            while (nesting > 0 && this.currentPos < this.maxPos) {
                c = this.string.charAt(this.currentPos);
                if (c == '\\') {
                    this.currentPos++;
                    filter = true;
                } else if (c == 13) {
                    filter = true;
                } else if (c == '(') {
                    nesting++;
                } else if (c == ')') {
                    nesting--;
                }
                this.currentPos++;
            }
            if (nesting != 0) {
                throw new ParseException("Unbalanced comments");
            } else if (!this.skipComments) {
                if (filter) {
                    s = filterToken(this.string, start, this.currentPos - 1);
                } else {
                    s = this.string.substring(start, this.currentPos - 1);
                }
                return new Token(-3, s);
            } else if (skipWhiteSpace() == -4) {
                return EOFToken;
            } else {
                c = this.string.charAt(this.currentPos);
            }
        }
        if (c == '\"') {
            start = this.currentPos + 1;
            this.currentPos = start;
            while (this.currentPos < this.maxPos) {
                c = this.string.charAt(this.currentPos);
                if (c == '\\') {
                    this.currentPos++;
                    filter = true;
                } else if (c == 13) {
                    filter = true;
                } else if (c == '\"') {
                    this.currentPos++;
                    if (filter) {
                        s = filterToken(this.string, start, this.currentPos - 1);
                    } else {
                        s = this.string.substring(start, this.currentPos - 1);
                    }
                    return new Token(-2, s);
                }
                this.currentPos++;
            }
            throw new ParseException("Unbalanced quoted string");
        } else if (c < ' ' || c >= 127 || this.delimiters.indexOf(c) >= 0) {
            this.currentPos++;
            return new Token(c, new String(new char[]{c}));
        } else {
            start = this.currentPos;
            while (this.currentPos < this.maxPos) {
                c = this.string.charAt(this.currentPos);
                if (c < ' ' || c >= 127 || c == '(' || c == ' ' || c == '\"' || this.delimiters.indexOf(c) >= 0) {
                    break;
                }
                this.currentPos++;
            }
            return new Token(-1, this.string.substring(start, this.currentPos));
        }
    }

    private int skipWhiteSpace() {
        while (this.currentPos < this.maxPos) {
            char c = this.string.charAt(this.currentPos);
            if (c != ' ' && c != 9 && c != 13 && c != 10) {
                return this.currentPos;
            }
            this.currentPos++;
        }
        return -4;
    }

    private static String filterToken(String s, int start, int end) {
        StringBuffer sb = new StringBuffer();
        boolean gotEscape = false;
        boolean gotCR = false;
        for (int i = start; i < end; i++) {
            char c = s.charAt(i);
            if (c == 10 && gotCR) {
                gotCR = false;
            } else {
                gotCR = false;
                if (gotEscape) {
                    sb.append(c);
                    gotEscape = false;
                } else if (c == '\\') {
                    gotEscape = true;
                } else if (c == 13) {
                    gotCR = true;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
