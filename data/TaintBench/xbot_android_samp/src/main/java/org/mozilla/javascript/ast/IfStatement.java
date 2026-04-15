package org.mozilla.javascript.ast;

public class IfStatement extends AstNode {
    private AstNode condition;
    private AstNode elsePart;
    private int elsePosition;
    private int lp;
    private int rp;
    private AstNode thenPart;

    public IfStatement() {
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }

    public IfStatement(int pos) {
        super(pos);
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }

    public IfStatement(int pos, int len) {
        super(pos, len);
        this.elsePosition = -1;
        this.lp = -1;
        this.rp = -1;
        this.type = 112;
    }

    public AstNode getCondition() {
        return this.condition;
    }

    public void setCondition(AstNode condition) {
        assertNotNull(condition);
        this.condition = condition;
        condition.setParent(this);
    }

    public AstNode getThenPart() {
        return this.thenPart;
    }

    public void setThenPart(AstNode thenPart) {
        assertNotNull(thenPart);
        this.thenPart = thenPart;
        thenPart.setParent(this);
    }

    public AstNode getElsePart() {
        return this.elsePart;
    }

    public void setElsePart(AstNode elsePart) {
        this.elsePart = elsePart;
        if (elsePart != null) {
            elsePart.setParent(this);
        }
    }

    public int getElsePosition() {
        return this.elsePosition;
    }

    public void setElsePosition(int elsePosition) {
        this.elsePosition = elsePosition;
    }

    public int getLp() {
        return this.lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getRp() {
        return this.rp;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public void setParens(int lp, int rp) {
        this.lp = lp;
        this.rp = rp;
    }

    public String toSource(int depth) {
        String pad = makeIndent(depth);
        StringBuilder sb = new StringBuilder(32);
        sb.append(pad);
        sb.append("if (");
        sb.append(this.condition.toSource(0));
        sb.append(") ");
        if (this.thenPart.getType() != 129) {
            sb.append("\n").append(makeIndent(depth + 1));
        }
        sb.append(this.thenPart.toSource(depth).trim());
        if (this.elsePart != null) {
            if (this.thenPart.getType() != 129) {
                sb.append("\n").append(pad).append("else ");
            } else {
                sb.append(" else ");
            }
            if (!(this.elsePart.getType() == 129 || this.elsePart.getType() == 112)) {
                sb.append("\n").append(makeIndent(depth + 1));
            }
            sb.append(this.elsePart.toSource(depth).trim());
        }
        sb.append("\n");
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.condition.visit(v);
            this.thenPart.visit(v);
            if (this.elsePart != null) {
                this.elsePart.visit(v);
            }
        }
    }
}
