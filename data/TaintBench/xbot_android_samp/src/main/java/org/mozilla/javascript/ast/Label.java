package org.mozilla.javascript.ast;

public class Label extends Jump {
    private String name;

    public Label() {
        this.type = 130;
    }

    public Label(int pos) {
        this(pos, -1);
    }

    public Label(int pos, int len) {
        this.type = 130;
        this.position = pos;
        this.length = len;
    }

    public Label(int pos, int len, String name) {
        this(pos, len);
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        Object trim = name == null ? null : name.trim();
        if (trim == null || "".equals(trim)) {
            throw new IllegalArgumentException("invalid label name");
        }
        this.name = trim;
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append(this.name);
        sb.append(":\n");
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        v.visit(this);
    }
}
