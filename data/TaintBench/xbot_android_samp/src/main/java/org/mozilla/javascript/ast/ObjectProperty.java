package org.mozilla.javascript.ast;

public class ObjectProperty extends InfixExpression {
    public void setNodeType(int nodeType) {
        if (nodeType == 103 || nodeType == 151 || nodeType == 152) {
            setType(nodeType);
            return;
        }
        throw new IllegalArgumentException("invalid node type: " + nodeType);
    }

    public ObjectProperty() {
        this.type = 103;
    }

    public ObjectProperty(int pos) {
        super(pos);
        this.type = 103;
    }

    public ObjectProperty(int pos, int len) {
        super(pos, len);
        this.type = 103;
    }

    public void setIsGetter() {
        this.type = 151;
    }

    public boolean isGetter() {
        return this.type == 151;
    }

    public void setIsSetter() {
        this.type = 152;
    }

    public boolean isSetter() {
        return this.type == 152;
    }

    public String toSource(int depth) {
        int i;
        int i2 = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(makeIndent(depth + 1));
        if (isGetter()) {
            sb.append("get ");
        } else if (isSetter()) {
            sb.append("set ");
        }
        AstNode astNode = this.left;
        if (getType() == 103) {
            i = 0;
        } else {
            i = depth;
        }
        sb.append(astNode.toSource(i));
        if (this.type == 103) {
            sb.append(": ");
        }
        AstNode astNode2 = this.right;
        if (getType() != 103) {
            i2 = depth + 1;
        }
        sb.append(astNode2.toSource(i2));
        return sb.toString();
    }
}
