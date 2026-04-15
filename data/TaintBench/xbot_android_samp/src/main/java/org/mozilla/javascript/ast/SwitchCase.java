package org.mozilla.javascript.ast;

import java.util.ArrayList;
import java.util.List;

public class SwitchCase extends AstNode {
    private AstNode expression;
    private List<AstNode> statements;

    public SwitchCase() {
        this.type = 115;
    }

    public SwitchCase(int pos) {
        super(pos);
        this.type = 115;
    }

    public SwitchCase(int pos, int len) {
        super(pos, len);
        this.type = 115;
    }

    public AstNode getExpression() {
        return this.expression;
    }

    public void setExpression(AstNode expression) {
        this.expression = expression;
        if (expression != null) {
            expression.setParent(this);
        }
    }

    public boolean isDefault() {
        return this.expression == null;
    }

    public List<AstNode> getStatements() {
        return this.statements;
    }

    public void setStatements(List<AstNode> statements) {
        if (this.statements != null) {
            this.statements.clear();
        }
        for (AstNode s : statements) {
            addStatement(s);
        }
    }

    public void addStatement(AstNode statement) {
        assertNotNull(statement);
        if (this.statements == null) {
            this.statements = new ArrayList();
        }
        setLength((statement.getPosition() + statement.getLength()) - getPosition());
        this.statements.add(statement);
        statement.setParent(this);
    }

    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        if (this.expression == null) {
            sb.append("default:\n");
        } else {
            sb.append("case ");
            sb.append(this.expression.toSource(0));
            sb.append(":\n");
        }
        if (this.statements != null) {
            for (AstNode s : this.statements) {
                sb.append(s.toSource(depth + 1));
            }
        }
        return sb.toString();
    }

    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            if (this.expression != null) {
                this.expression.visit(v);
            }
            if (this.statements != null) {
                for (AstNode s : this.statements) {
                    s.visit(v);
                }
            }
        }
    }
}
