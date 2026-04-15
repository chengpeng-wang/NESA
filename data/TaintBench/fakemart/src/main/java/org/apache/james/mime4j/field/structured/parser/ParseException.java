package org.apache.james.mime4j.field.structured.parser;

public class ParseException extends org.apache.james.mime4j.field.ParseException {
    private static final long serialVersionUID = 1;
    public Token currentToken;
    protected String eol = System.getProperty("line.separator", "\n");
    public int[][] expectedTokenSequences;
    protected boolean specialConstructor = false;
    public String[] tokenImage;

    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super("");
        this.currentToken = currentTokenVal;
        this.expectedTokenSequences = expectedTokenSequencesVal;
        this.tokenImage = tokenImageVal;
    }

    public ParseException() {
        super("Cannot parse field");
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message) {
        super(message);
    }

    public String getMessage() {
        if (!this.specialConstructor) {
            return super.getMessage();
        }
        int i;
        StringBuffer expected = new StringBuffer();
        int maxSize = 0;
        for (i = 0; i < this.expectedTokenSequences.length; i++) {
            if (maxSize < this.expectedTokenSequences[i].length) {
                maxSize = this.expectedTokenSequences[i].length;
            }
            for (int i2 : this.expectedTokenSequences[i]) {
                expected.append(this.tokenImage[i2]).append(" ");
            }
            if (this.expectedTokenSequences[i][this.expectedTokenSequences[i].length - 1] != 0) {
                expected.append("...");
            }
            expected.append(this.eol).append("    ");
        }
        String retval = "Encountered \"";
        Token tok = this.currentToken.next;
        for (i = 0; i < maxSize; i++) {
            if (i != 0) {
                retval = retval + " ";
            }
            if (tok.kind == 0) {
                retval = retval + this.tokenImage[0];
                break;
            }
            retval = retval + add_escapes(tok.image);
            tok = tok.next;
        }
        retval = (retval + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn) + "." + this.eol;
        if (this.expectedTokenSequences.length == 1) {
            retval = retval + "Was expecting:" + this.eol + "    ";
        } else {
            retval = retval + "Was expecting one of:" + this.eol + "    ";
        }
        return retval + expected.toString();
    }

    /* access modifiers changed from: protected */
    public String add_escapes(String str) {
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 0:
                    break;
                case 8:
                    retval.append("\\b");
                    break;
                case 9:
                    retval.append("\\t");
                    break;
                case 10:
                    retval.append("\\n");
                    break;
                case 12:
                    retval.append("\\f");
                    break;
                case 13:
                    retval.append("\\r");
                    break;
                case '\"':
                    retval.append("\\\"");
                    break;
                case '\'':
                    retval.append("\\'");
                    break;
                case '\\':
                    retval.append("\\\\");
                    break;
                default:
                    char ch = str.charAt(i);
                    if (ch >= ' ' && ch <= '~') {
                        retval.append(ch);
                        break;
                    }
                    String s = "0000" + Integer.toString(ch, 16);
                    retval.append("\\u" + s.substring(s.length() - 4, s.length()));
                    break;
                    break;
            }
        }
        return retval.toString();
    }
}
