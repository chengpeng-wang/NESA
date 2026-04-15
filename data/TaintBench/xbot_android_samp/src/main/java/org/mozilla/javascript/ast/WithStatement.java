package org.mozilla.javascript.ast;

public class WithStatement extends AstNode {
    private AstNode expression;
    private int lp;
    private int rp;
    private AstNode statement;

    public WithStatement() {
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }

    public WithStatement(int pos) {
        super(pos);
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }

    public WithStatement(int pos, int len) {
        super(pos, len);
        this.lp = -1;
        this.rp = -1;
        this.type = 123;
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        assertNotNull(expression);
        this.expression = expression;
        expression.setParent(this);
    }

    public AstNode getStatement() {
        return this.statement;
    }

    public void setStatement(AstNode statement) {
        assertNotNull(statement);
        this.statement = statement;
        statement.setParent(this);
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
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append("with (");
        sb.append(this.expression.toSource(0));
        sb.append(") ");
        if (this.statement.getType() == 129) {
            sb.append(this.statement.toSource(depth).trim());
            sb.append("\n");
        } else {
            sb.append("\n").append(this.statement.toSource(depth + 1));
        }
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.expression.visit(v);
            this.statement.visit(v);
        }
    }
}
