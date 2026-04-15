package org.json.simple.parser;

public class ParseException extends Exception {
    public static final int ERROR_UNEXPECTED_CHAR = 0;
    public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
    public static final int ERROR_UNEXPECTED_TOKEN = 1;
    private static final long serialVersionUID = -7880698968187728548L;
    private int errorType;
    private int position;
    private Object unexpectedObject;

    public ParseException(int i) {
        this(-1, i, null);
    }

    public ParseException(int i, int i2, Object obj) {
        this.position = i;
        this.errorType = i2;
        this.unexpectedObject = obj;
    }

    public ParseException(int i, Object obj) {
        this(-1, i, obj);
    }

    public int getErrorType() {
        return this.errorType;
    }

    public int getPosition() {
        return this.position;
    }

    public Object getUnexpectedObject() {
        return this.unexpectedObject;
    }

    public void setErrorType(int i) {
        this.errorType = i;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public void setUnexpectedObject(Object obj) {
        this.unexpectedObject = obj;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.errorType) {
            case 0:
                stringBuffer.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
                break;
            case 1:
                stringBuffer.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
                break;
            case 2:
                stringBuffer.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
                break;
            default:
                stringBuffer.append("Unkown error at position ").append(this.position).append(".");
                break;
        }
        return stringBuffer.toString();
    }
}
