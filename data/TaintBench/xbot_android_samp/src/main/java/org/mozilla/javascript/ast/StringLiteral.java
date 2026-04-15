package org.mozilla.javascript.ast;

import org.mozilla.javascript.ScriptRuntime;

public class StringLiteral extends AstNode {
    private char quoteChar;
    private String value;

    public StringLiteral() {
        this.type = 41;
    }

    public StringLiteral(int pos) {
        super(pos);
        this.type = 41;
    }

    public StringLiteral(int pos, int len) {
        super(pos, len);
        this.type = 41;
    }

    public String getValue() {
        return this.value;
    }

    public String getValue(boolean includeQuotes) {
        if (includeQuotes) {
            return this.quoteChar + this.value + this.quoteChar;
        }
        return this.value;
    }

    public void setValue(String value) {
        assertNotNull(value);
        this.value = value;
    }

    public char getQuoteCharacter() {
        return this.quoteChar;
    }

    public void setQuoteCharacter(char c) {
        this.quoteChar = c;
    }

    public String toSource(int depth) {
        return new StringBuilder(makeIndent(depth)).append(this.quoteChar).append(ScriptRuntime.escapeString(this.value, this.quoteChar)).append(this.quoteChar).toString();
    }

    public void visit(NodeVisitor v) {
        v.visit(this);
    }
}
