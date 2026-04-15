package org.mozilla.javascript.ast;

import org.mozilla.javascript.Node;

public class Jump extends AstNode {
    private Jump jumpNode;
    public Node target;
    private Node target2;

    public Jump() {
        this.type = -1;
    }

    public Jump(int nodeType) {
        this.type = nodeType;
    }

    public Jump(int type, int lineno) {
        this(type);
        setLineno(lineno);
    }

    public Jump(int type, Node child) {
        this(type);
        addChildToBack(child);
    }

    public Jump(int type, Node child, int lineno) {
        this(type, child);
        setLineno(lineno);
    }

    public Jump getJumpStatement() {
        if (!(this.type == 120 || this.type == 121)) {
            AstNode.codeBug();
        }
        return this.jumpNode;
    }

    public void setJumpStatement(Jump jumpStatement) {
        if (!(this.type == 120 || this.type == 121)) {
            AstNode.codeBug();
        }
        if (jumpStatement == null) {
            AstNode.codeBug();
        }
        if (this.jumpNode != null) {
            AstNode.codeBug();
        }
        this.jumpNode = jumpStatement;
    }

    public Node getDefault() {
        if (this.type != 114) {
            AstNode.codeBug();
        }
        return this.target2;
    }

    public void setDefault(Node defaultTarget) {
        if (this.type != 114) {
            AstNode.codeBug();
        }
        if (defaultTarget.getType() != 131) {
            AstNode.codeBug();
        }
        if (this.target2 != null) {
            AstNode.codeBug();
        }
        this.target2 = defaultTarget;
    }

    public Node getFinally() {
        if (this.type != 81) {
            AstNode.codeBug();
        }
        return this.target2;
    }

    public void setFinally(Node finallyTarget) {
        if (this.type != 81) {
            AstNode.codeBug();
        }
        if (finallyTarget.getType() != 131) {
            AstNode.codeBug();
        }
        if (this.target2 != null) {
            AstNode.codeBug();
        }
        this.target2 = finallyTarget;
    }

    public Jump getLoop() {
        if (this.type != 130) {
            AstNode.codeBug();
        }
        return this.jumpNode;
    }

    public void setLoop(Jump loop) {
        if (this.type != 130) {
            AstNode.codeBug();
        }
        if (loop == null) {
            AstNode.codeBug();
        }
        if (this.jumpNode != null) {
            AstNode.codeBug();
        }
        this.jumpNode = loop;
    }

    public Node getContinue() {
        if (this.type != 132) {
            AstNode.codeBug();
        }
        return this.target2;
    }

    public void setContinue(Node continueTarget) {
        if (this.type != 132) {
            AstNode.codeBug();
        }
        if (continueTarget.getType() != 131) {
            AstNode.codeBug();
        }
        if (this.target2 != null) {
            AstNode.codeBug();
        }
        this.target2 = continueTarget;
    }

    public void visit(NodeVisitor visitor) {
        throw new UnsupportedOperationException(toString());
    }

    public String toSource(int depth) {
        throw new UnsupportedOperationException(toString());
    }
}
