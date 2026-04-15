package flexjson;

import java.util.Arrays;

public class PathExpression {
    String[] expression;
    boolean included = true;
    boolean wildcard = false;

    public PathExpression(String expr, boolean anInclude) {
        boolean z = true;
        this.expression = expr.split("\\.");
        if (expr.indexOf(42) < 0) {
            z = false;
        }
        this.wildcard = z;
        this.included = anInclude;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < this.expression.length; i++) {
            builder.append(this.expression[i]);
            if (i < this.expression.length - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public boolean matches(Path path) {
        int exprCurrentIndex = 0;
        int pathCurrentIndex = 0;
        while (pathCurrentIndex < path.length()) {
            String current = (String) path.getPath().get(pathCurrentIndex);
            if (exprCurrentIndex < this.expression.length && this.expression[exprCurrentIndex].equals("*")) {
                exprCurrentIndex++;
            } else if (exprCurrentIndex < this.expression.length && this.expression[exprCurrentIndex].equals(current)) {
                pathCurrentIndex++;
                exprCurrentIndex++;
            } else if (exprCurrentIndex - 1 < 0 || !this.expression[exprCurrentIndex - 1].equals("*")) {
                return false;
            } else {
                pathCurrentIndex++;
            }
        }
        if (exprCurrentIndex <= 0 || !this.expression[exprCurrentIndex - 1].equals("*")) {
            if (pathCurrentIndex < path.length() || path.length() <= 0) {
                return false;
            }
            return true;
        } else if (pathCurrentIndex < path.length() || exprCurrentIndex < this.expression.length) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isWildcard() {
        return this.wildcard;
    }

    public boolean isIncluded() {
        return this.included;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (Arrays.equals(this.expression, ((PathExpression) o).expression)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.expression != null ? Arrays.hashCode(this.expression) : 0;
    }
}
