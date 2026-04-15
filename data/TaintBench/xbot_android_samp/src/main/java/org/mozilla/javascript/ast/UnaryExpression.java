package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

public class UnaryExpression extends AstNode {
    private boolean isPostfix;
    private AstNode operand;

    public UnaryExpression(int pos) {
        super(pos);
    }

    public UnaryExpression(int pos, int len) {
        super(pos, len);
    }

    public UnaryExpression(int operator, int operatorPosition, AstNode operand) {
        this(operator, operatorPosition, operand, false);
    }

    public UnaryExpression(int operator, int operatorPosition, AstNode operand, boolean postFix) {
        int beg;
        int end;
        assertNotNull(operand);
        if (postFix) {
            beg = operand.getPosition();
        } else {
            beg = operatorPosition;
        }
        if (postFix) {
            end = operatorPosition + 2;
        } else {
            end = operand.getPosition() + operand.getLength();
        }
        setBounds(beg, end);
        setOperator(operator);
        setOperand(operand);
        this.isPostfix = postFix;
    }

    public int getOperator() {
        return this.type;
    }

    public void setOperator(int operator) {
        if (Token.isValidToken(operator)) {
            setType(operator);
            return;
        }
        throw new IllegalArgumentException("Invalid token: " + operator);
    }

    public AstNode getOperand() {
        return this.operand;
    }

    public void setOperand(AstNode operand) {
        assertNotNull(operand);
        this.operand = operand;
        operand.setParent(this);
    }

    public boolean isPostfix() {
        return this.isPostfix;
    }

    public boolean isPrefix() {
        return !this.isPostfix;
    }

    public void setIsPostfix(boolean isPostfix) {
        this.isPostfix = isPostfix;
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        int type = getType();
        if (!this.isPostfix) {
            sb.append(AstNode.operatorToString(type));
            if (type == 32 || type == 31 || type == 126) {
                sb.append(" ");
            }
        }
        sb.append(this.operand.toSource());
        if (this.isPostfix) {
            sb.append(AstNode.operatorToString(type));
        }
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.operand.visit(v);
        }
    }
}
