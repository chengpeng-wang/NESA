package org.mozilla.javascript.ast;

public class XmlPropRef extends XmlRef {
    private Name propName;

    public XmlPropRef() {
        this.type = 79;
    }

    public XmlPropRef(int pos) {
        super(pos);
        this.type = 79;
    }

    public XmlPropRef(int pos, int len) {
        super(pos, len);
        this.type = 79;
    }

    public Name getPropName() {
        return this.propName;
    }

    public void setPropName(Name propName) {
        assertNotNull(propName);
        this.propName = propName;
        propName.setParent(this);
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        if (isAttributeAccess()) {
            sb.append("@");
        }
        if (this.namespace != null) {
            sb.append(this.namespace.toSource(0));
            sb.append("::");
        }
        sb.append(this.propName.toSource(0));
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            if (this.namespace != null) {
                this.namespace.visit(v);
            }
            this.propName.visit(v);
        }
    }
}
