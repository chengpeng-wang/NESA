package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

public class InfixExpression extends AstNode {
    protected AstNode left;
    protected int operatorPosition = -1;
    protected AstNode right;

    public InfixExpression(int pos) {
        super(pos);
    }

    public InfixExpression(int pos, int len) {
        super(pos, len);
    }

    public InfixExpression(int pos, int len, AstNode left, AstNode right) {
        super(pos, len);
        setLeft(left);
        setRight(right);
    }

    public InfixExpression(AstNode left, AstNode right) {
        setLeftAndRight(left, right);
    }

    public InfixExpression(int operator, AstNode left, AstNode right, int operatorPos) {
        setType(operator);
        setOperatorPosition(operatorPos - left.getPosition());
        setLeftAndRight(left, right);
    }

    public void setLeftAndRight(AstNode left, AstNode right) {
        assertNotNull(left);
        assertNotNull(right);
        setBounds(left.getPosition(), right.getPosition() + right.getLength());
        setLeft(left);
        setRight(right);
    }

    public int getOperator() {
        return getType();
    }

    public void setOperator(int operator) {
        if (Token.isValidToken(operator)) {
            setType(operator);
            return;
        }
        throw new IllegalArgumentException("Invalid token: " + operator);
    }

    public AstNode getLeft() {
        return this.left;
    }

    public void setLeft(AstNode left) {
        assertNotNull(left);
        this.left = left;
        setLineno(left.getLineno());
        left.setParent(this);
    }

    public AstNode getRight() {
        return this.right;
    }

    public void setRight(AstNode right) {
        assertNotNull(right);
        this.right = right;
        right.setParent(this);
    }

    public int getOperatorPosition() {
        return this.operatorPosition;
    }

    public void setOperatorPosition(int operatorPosition) {
        this.operatorPosition = operatorPosition;
    }

    public boolean hasSideEffects() {
        boolean z = false;
        switch (getType()) {
            case 89:
                if (this.right == null || !this.right.hasSideEffects()) {
                    return false;
                }
                return true;
            case 104:
            case 105:
                if ((this.left != null && this.left.hasSideEffects()) || (this.right != null && this.right.hasSideEffects())) {
                    z = true;
                }
                return z;
            default:
                return super.hasSideEffects();
        }
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append(this.left.toSource());
        sb.append(" ");
        sb.append(AstNode.operatorToString(getType()));
        sb.append(" ");
        sb.append(this.right.toSource());
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.left.visit(v);
            this.right.visit(v);
        }
    }
}
