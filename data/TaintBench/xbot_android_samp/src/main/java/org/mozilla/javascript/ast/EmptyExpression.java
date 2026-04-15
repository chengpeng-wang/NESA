package org.mozilla.javascript.ast;

public class EmptyExpression extends AstNode {
    public EmptyExpression() {
        this.type = 128;
    }

    public EmptyExpression(int pos) {
        super(pos);
        this.type = 128;
    }

    public EmptyExpression(int pos, int len) {
        super(pos, len);
        this.type = 128;
    }

    public String toSource(int depth) {
        return makeIndent(depth);
    }

    public void visit(NodeVisitor v) {
        v.visit(this);
    }
}
