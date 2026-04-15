package org.mozilla.javascript.ast;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.java_websocket.framing.CloseFrame;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.AstNode.PositionComparator;

public class AstRoot extends ScriptNode {
    private SortedSet<Comment> comments;
    private boolean inStrictMode;

    public AstRoot() {
        this.type = 136;
    }

    public AstRoot(int pos) {
        super(pos);
        this.type = 136;
    }

    public SortedSet<Comment> getComments() {
        return this.comments;
    }

    public void setComments(SortedSet<Comment> comments) {
        if (comments == null) {
            this.comments = null;
            return;
        }
        if (this.comments != null) {
            this.comments.clear();
        }
        for (Comment c : comments) {
            addComment(c);
        }
    }

    public void addComment(Comment comment) {
        assertNotNull(comment);
        if (this.comments == null) {
            this.comments = new TreeSet(new PositionComparator());
        }
        this.comments.add(comment);
        comment.setParent(this);
    }

    public void setInStrictMode(boolean inStrictMode) {
        this.inStrictMode = inStrictMode;
    }

    public boolean isInStrictMode() {
        return this.inStrictMode;
    }

    public void visitComments(NodeVisitor visitor) {
        if (this.comments != null) {
            for (Comment c : this.comments) {
                visitor.visit(c);
            }
        }
    }

    public void visitAll(NodeVisitor visitor) {
        visit(visitor);
        visitComments(visitor);
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            sb.append(((AstNode) ((Node) it.next())).toSource(depth));
        }
        return sb.toString();
    }

    public String debugPrint() {
        DebugPrintVisitor dpv = new DebugPrintVisitor(new StringBuilder(CloseFrame.NORMAL));
        visitAll(dpv);
        return dpv.toString();
    }

    public void checkParentLinks() {
        visit(new NodeVisitor() {
            public boolean visit(AstNode node) {
                if (node.getType() == 136 || node.getParent() != null) {
                    return true;
                }
                throw new IllegalStateException("No parent for node: " + node + "\n" + node.toSource(0));
            }
        });
    }
}
