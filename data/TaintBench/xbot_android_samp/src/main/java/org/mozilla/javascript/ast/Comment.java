package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token.CommentType;

public class Comment extends AstNode {
    private CommentType commentType;
    private String value;

    public Comment(int pos, int len, CommentType type, String value) {
        super(pos, len);
        this.type = 161;
        this.commentType = type;
        this.value = value;
    }

    public CommentType getCommentType() {
        return this.commentType;
    }

    public void setCommentType(CommentType type) {
        this.commentType = type;
    }

    public String getValue() {
        return this.value;
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder(getLength() + 10);
        sb.append(makeIndent(depth));
        sb.append(this.value);
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        v.visit(this);
    }
}
