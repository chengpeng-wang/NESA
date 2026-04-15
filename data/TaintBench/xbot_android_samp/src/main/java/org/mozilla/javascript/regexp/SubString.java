package org.mozilla.javascript.regexp;

public class SubString {
    public static final SubString emptySubString = new SubString();
    int index;
    int length;
    String str;

    public SubString(String str) {
        this.str = str;
        this.index = 0;
        this.length = str.length();
    }

    public SubString(String source, int start, int len) {
        this.str = source;
        this.index = start;
        this.length = len;
    }

    public String toString() {
        if (this.str == null) {
            return "";
        }
        return this.str.substring(this.index, this.index + this.length);
    }
}
