package org.mozilla.javascript.ast;

import java.util.Iterator;
import org.mozilla.javascript.Node;

public class Block extends AstNode {
    public Block() {
        this.type = 129;
    }

    public Block(int pos) {
        super(pos);
        this.type = 129;
    }

    public Block(int pos, int len) {
        super(pos, len);
        this.type = 129;
    }

    public void addStatement(AstNode statement) {
        addChild(statement);
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append("{\n");
        Iterator it = iterator();
        while (it.hasNext()) {
            sb.append(((AstNode) ((Node) it.next())).toSource(depth + 1));
        }
        sb.append(makeIndent(depth));
        sb.append("}\n");
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            Iterator it = iterator();
            while (it.hasNext()) {
                ((AstNode) ((Node) it.next())).visit(v);
            }
        }
    }
}
