package org.mozilla.javascript.ast;

public class ForInLoop extends Loop {
    protected int eachPosition;
    protected int inPosition;
    protected boolean isForEach;
    protected AstNode iteratedObject;
    protected AstNode iterator;

    public ForInLoop() {
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }

    public ForInLoop(int pos) {
        super(pos);
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }

    public ForInLoop(int pos, int len) {
        super(pos, len);
        this.inPosition = -1;
        this.eachPosition = -1;
        this.type = 119;
    }

    public AstNode getIterator() {
        return this.iterator;
    }

    public void setIterator(AstNode iterator) {
        assertNotNull(iterator);
        this.iterator = iterator;
        iterator.setParent(this);
    }

    public AstNode getIteratedObject() {
        return this.iteratedObject;
    }

    public void setIteratedObject(AstNode object) {
        assertNotNull(object);
        this.iteratedObject = object;
        object.setParent(this);
    }

    public boolean isForEach() {
        return this.isForEach;
    }

    public void setIsForEach(boolean isForEach) {
        this.isForEach = isForEach;
    }

    public int getInPosition() {
        return this.inPosition;
    }

    public void setInPosition(int inPosition) {
        this.inPosition = inPosition;
    }

    public int getEachPosition() {
        return this.eachPosition;
    }

    public void setEachPosition(int eachPosition) {
        this.eachPosition = eachPosition;
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append("for ");
        if (isForEach()) {
            sb.append("each ");
        }
        sb.append("(");
        sb.append(this.iterator.toSource(0));
        sb.append(" in ");
        sb.append(this.iteratedObject.toSource(0));
        sb.append(") ");
        if (this.body.getType() == 129) {
            sb.append(this.body.toSource(depth).trim()).append("\n");
        } else {
            sb.append("\n").append(this.body.toSource(depth + 1));
        }
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            this.iterator.visit(v);
            this.iteratedObject.visit(v);
            this.body.visit(v);
        }
    }
}
