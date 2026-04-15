package org.mozilla.javascript.ast;

public class XmlMemberGet extends InfixExpression {
    public XmlMemberGet() {
        this.type = 143;
    }

    public XmlMemberGet(int pos) {
        super(pos);
        this.type = 143;
    }

    public XmlMemberGet(int pos, int len) {
        super(pos, len);
        this.type = 143;
    }

    public XmlMemberGet(int pos, int len, AstNode target, XmlRef ref) {
        super(pos, len, target, (AstNode) ref);
        this.type = 143;
    }

    public XmlMemberGet(AstNode target, XmlRef ref) {
        super(target, (AstNode) ref);
        this.type = 143;
    }

    public XmlMemberGet(AstNode target, XmlRef ref, int opPos) {
        super(143, target, (AstNode) ref, opPos);
        this.type = 143;
    }

    public AstNode getTarget() {
        return getLeft();
    }

    public void setTarget(AstNode target) {
        setLeft(target);
    }

    public XmlRef getMemberRef() {
        return (XmlRef) getRight();
    }

    public void setProperty(XmlRef ref) {
        setRight(ref);
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append(getLeft().toSource(0));
        sb.append(AstNode.operatorToString(getType()));
        sb.append(getRight().toSource(0));
        return sb.toString();
    }
}
