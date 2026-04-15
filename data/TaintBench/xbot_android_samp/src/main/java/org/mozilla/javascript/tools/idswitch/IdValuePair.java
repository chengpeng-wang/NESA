package org.mozilla.javascript.tools.idswitch;

public class IdValuePair {
    public final String id;
    public final int idLength;
    private int lineNumber;
    public final String value;

    public IdValuePair(String id, String value) {
        this.idLength = id.length();
        this.id = id;
        this.value = value;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int value) {
        this.lineNumber = value;
    }
}
