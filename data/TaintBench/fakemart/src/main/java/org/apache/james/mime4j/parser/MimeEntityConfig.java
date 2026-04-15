package org.apache.james.mime4j.parser;

public final class MimeEntityConfig implements Cloneable {
    private boolean countLineNumbers = false;
    private long maxContentLen = -1;
    private int maxHeaderCount = 1000;
    private int maxLineLen = 1000;
    private boolean maximalBodyDescriptor = false;
    private boolean strictParsing = false;

    public boolean isMaximalBodyDescriptor() {
        return this.maximalBodyDescriptor;
    }

    public void setMaximalBodyDescriptor(boolean maximalBodyDescriptor) {
        this.maximalBodyDescriptor = maximalBodyDescriptor;
    }

    public void setStrictParsing(boolean strictParsing) {
        this.strictParsing = strictParsing;
    }

    public boolean isStrictParsing() {
        return this.strictParsing;
    }

    public void setMaxLineLen(int maxLineLen) {
        this.maxLineLen = maxLineLen;
    }

    public int getMaxLineLen() {
        return this.maxLineLen;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }

    public void setMaxContentLen(long maxContentLen) {
        this.maxContentLen = maxContentLen;
    }

    public long getMaxContentLen() {
        return this.maxContentLen;
    }

    public void setCountLineNumbers(boolean countLineNumbers) {
        this.countLineNumbers = countLineNumbers;
    }

    public boolean isCountLineNumbers() {
        return this.countLineNumbers;
    }

    public MimeEntityConfig clone() {
        try {
            return (MimeEntityConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public String toString() {
        return "[max body descriptor: " + this.maximalBodyDescriptor + ", strict parsing: " + this.strictParsing + ", max line length: " + this.maxLineLen + ", max header count: " + this.maxHeaderCount + ", max content length: " + this.maxContentLen + ", count line numbers: " + this.countLineNumbers + "]";
    }
}
