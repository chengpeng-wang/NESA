package org.mozilla.javascript.ast;

public abstract class XmlFragment extends AstNode {
    public XmlFragment() {
        this.type = 145;
    }

    public XmlFragment(int pos) {
        super(pos);
        this.type = 145;
    }

    public XmlFragment(int pos, int len) {
        super(pos, len);
        this.type = 145;
    }
}
