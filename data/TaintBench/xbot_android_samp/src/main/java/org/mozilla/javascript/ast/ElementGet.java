package org.mozilla.javascript.ast;

public class ElementGet extends AstNode {
    private AstNode element;
    private int lb;
    private int rb;
    private AstNode target;

    public ElementGet() {
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }

    public ElementGet(int pos) {
        super(pos);
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }

    public ElementGet(int pos, int len) {
        super(pos, len);
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
    }

    public ElementGet(AstNode target, AstNode element) {
        this.lb = -1;
        this.rb = -1;
        this.type = 36;
        setTarget(target);
        setElement(element);
    }

    public AstNode getTarget() {
        return this.target;
    }

    public void setTarget(AstNode target) {
        assertNotNull(target);
        this.target = target;
        target.setParent(this);
    }

    public AstNode getElement() {
        return this.element;
    }

    public void setElement(AstNode element) {
        assertNotNull(element);
        this.element = element;
        element.setParent(this);
    }

    public int getLb() {
        return this.lb;
    }

    public void setLb(int lb) {
        this.lb = lb;
    }

    public int getRb() {
        return this.rb;
    }

    public void setRb(int rb) {
        this.rb = rb;
    }

    public void setParens(int lb, int rb) {
        this.lb = lb;
        this.rb = rb;
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append(this.target.toSource(0));
        sb.append("[");
        sb.append(this.element.toSource(0));
        sb.append("]");
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.target.visit(v);
            this.element.visit(v);
        }
    }
}
