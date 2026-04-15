package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.ObjArray;
import org.mozilla.javascript.ast.ScriptNode;

class Optimizer {
    static final int AnyType = 3;
    static final int NoType = 0;
    static final int NumberType = 1;
    private boolean inDirectCallFunction;
    private boolean parameterUsedInNumberContext;
    OptFunctionNode theFunction;

    Optimizer() {
    }

    /* access modifiers changed from: 0000 */
    public void optimize(ScriptNode scriptOrFn) {
        int functionCount = scriptOrFn.getFunctionCount();
        for (int i = 0; i != functionCount; i++) {
            optimizeFunction(OptFunctionNode.get(scriptOrFn, i));
        }
    }

    private void optimizeFunction(OptFunctionNode theFunction) {
        int i = 0;
        if (!theFunction.fnode.requiresActivation()) {
            this.inDirectCallFunction = theFunction.isTargetOfDirectCall();
            this.theFunction = theFunction;
            ObjArray statementsArray = new ObjArray();
            buildStatementList_r(theFunction.fnode, statementsArray);
            Node[] theStatementNodes = new Node[statementsArray.size()];
            statementsArray.toArray(theStatementNodes);
            Block.runFlowAnalyzes(theFunction, theStatementNodes);
            if (!theFunction.fnode.requiresActivation()) {
                this.parameterUsedInNumberContext = false;
                int length = theStatementNodes.length;
                while (i < length) {
                    rewriteForNumberVariables(theStatementNodes[i], 1);
                    i++;
                }
                theFunction.setParameterNumberContext(this.parameterUsedInNumberContext);
            }
        }
    }

    private void markDCPNumberContext(Node n) {
        if (this.inDirectCallFunction && n.getType() == 55) {
            if (this.theFunction.isParameter(this.theFunction.getVarIndex(n))) {
                this.parameterUsedInNumberContext = true;
            }
        }
    }

    private boolean convertParameter(Node n) {
        if (this.inDirectCallFunction && n.getType() == 55) {
            if (this.theFunction.isParameter(this.theFunction.getVarIndex(n))) {
                n.removeProp(8);
                return true;
            }
        }
        return false;
    }

    private int rewriteForNumberVariables(Node n, int desired) {
        Node lChild;
        Node rChild;
        int lType;
        int rType;
        Node arrayBase;
        Node arrayIndex;
        Node child;
        int varIndex;
        switch (n.getType()) {
            case 9:
            case 10:
            case 11:
            case 18:
            case 19:
            case 22:
            case 23:
            case 24:
            case 25:
                lChild = n.getFirstChild();
                rChild = lChild.getNext();
                lType = rewriteForNumberVariables(lChild, 1);
                rType = rewriteForNumberVariables(rChild, 1);
                markDCPNumberContext(lChild);
                markDCPNumberContext(rChild);
                if (lType == 1) {
                    if (rType == 1) {
                        n.putIntProp(8, 0);
                        return 1;
                    }
                    if (!convertParameter(rChild)) {
                        n.removeChild(rChild);
                        n.addChildToBack(new Node(150, rChild));
                        n.putIntProp(8, 0);
                    }
                    return 1;
                } else if (rType == 1) {
                    if (!convertParameter(lChild)) {
                        n.removeChild(lChild);
                        n.addChildToFront(new Node(150, lChild));
                        n.putIntProp(8, 0);
                    }
                    return 1;
                } else {
                    if (!convertParameter(lChild)) {
                        n.removeChild(lChild);
                        n.addChildToFront(new Node(150, lChild));
                    }
                    if (!convertParameter(rChild)) {
                        n.removeChild(rChild);
                        n.addChildToBack(new Node(150, rChild));
                    }
                    n.putIntProp(8, 0);
                    return 1;
                }
            case 14:
            case 15:
            case 16:
            case 17:
                lChild = n.getFirstChild();
                rChild = lChild.getNext();
                lType = rewriteForNumberVariables(lChild, 1);
                rType = rewriteForNumberVariables(rChild, 1);
                markDCPNumberContext(lChild);
                markDCPNumberContext(rChild);
                if (convertParameter(lChild)) {
                    if (convertParameter(rChild)) {
                        return 0;
                    }
                    if (rType == 1) {
                        n.putIntProp(8, 2);
                    }
                } else if (convertParameter(rChild)) {
                    if (lType == 1) {
                        n.putIntProp(8, 1);
                    }
                } else if (lType == 1) {
                    if (rType == 1) {
                        n.putIntProp(8, 0);
                    } else {
                        n.putIntProp(8, 1);
                    }
                } else if (rType == 1) {
                    n.putIntProp(8, 2);
                }
                return 0;
            case 21:
                lChild = n.getFirstChild();
                rChild = lChild.getNext();
                lType = rewriteForNumberVariables(lChild, 1);
                rType = rewriteForNumberVariables(rChild, 1);
                if (convertParameter(lChild)) {
                    if (convertParameter(rChild)) {
                        return 0;
                    }
                    if (rType == 1) {
                        n.putIntProp(8, 2);
                    }
                } else if (convertParameter(rChild)) {
                    if (lType == 1) {
                        n.putIntProp(8, 1);
                    }
                } else if (lType == 1) {
                    if (rType == 1) {
                        n.putIntProp(8, 0);
                        return 1;
                    }
                    n.putIntProp(8, 1);
                } else if (rType == 1) {
                    n.putIntProp(8, 2);
                }
                return 0;
            case 36:
                arrayBase = n.getFirstChild();
                arrayIndex = arrayBase.getNext();
                if (rewriteForNumberVariables(arrayBase, 1) == 1 && !convertParameter(arrayBase)) {
                    n.removeChild(arrayBase);
                    n.addChildToFront(new Node(149, arrayBase));
                }
                if (rewriteForNumberVariables(arrayIndex, 1) == 1 && !convertParameter(arrayIndex)) {
                    n.putIntProp(8, 2);
                }
                return 0;
            case 37:
            case 140:
                arrayBase = n.getFirstChild();
                arrayIndex = arrayBase.getNext();
                Node rValue = arrayIndex.getNext();
                if (rewriteForNumberVariables(arrayBase, 1) == 1 && !convertParameter(arrayBase)) {
                    n.removeChild(arrayBase);
                    n.addChildToFront(new Node(149, arrayBase));
                }
                if (rewriteForNumberVariables(arrayIndex, 1) == 1 && !convertParameter(arrayIndex)) {
                    n.putIntProp(8, 1);
                }
                if (rewriteForNumberVariables(rValue, 1) == 1 && !convertParameter(rValue)) {
                    n.removeChild(rValue);
                    n.addChildToBack(new Node(149, rValue));
                }
                return 0;
            case 38:
                child = n.getFirstChild();
                rewriteAsObjectChildren(child, child.getFirstChild());
                child = child.getNext();
                if (((OptFunctionNode) n.getProp(9)) != null) {
                    while (child != null) {
                        if (rewriteForNumberVariables(child, 1) == 1) {
                            markDCPNumberContext(child);
                        }
                        child = child.getNext();
                    }
                } else {
                    rewriteAsObjectChildren(n, child);
                }
                return 0;
            case 40:
                n.putIntProp(8, 0);
                return 1;
            case 55:
                varIndex = this.theFunction.getVarIndex(n);
                if (this.inDirectCallFunction && this.theFunction.isParameter(varIndex) && desired == 1) {
                    n.putIntProp(8, 0);
                    return 1;
                } else if (!this.theFunction.isNumberVar(varIndex)) {
                    return 0;
                } else {
                    n.putIntProp(8, 0);
                    return 1;
                }
            case 56:
            case 156:
                rChild = n.getFirstChild().getNext();
                rType = rewriteForNumberVariables(rChild, 1);
                varIndex = this.theFunction.getVarIndex(n);
                if (this.inDirectCallFunction && this.theFunction.isParameter(varIndex)) {
                    if (rType != 1) {
                        return rType;
                    }
                    if (convertParameter(rChild)) {
                        markDCPNumberContext(rChild);
                        return 0;
                    }
                    n.putIntProp(8, 0);
                    return 1;
                } else if (this.theFunction.isNumberVar(varIndex)) {
                    if (rType != 1) {
                        n.removeChild(rChild);
                        n.addChildToBack(new Node(150, rChild));
                    }
                    n.putIntProp(8, 0);
                    markDCPNumberContext(rChild);
                    return 1;
                } else {
                    if (rType == 1 && !convertParameter(rChild)) {
                        n.removeChild(rChild);
                        n.addChildToBack(new Node(149, rChild));
                    }
                    return 0;
                }
            case 106:
            case 107:
                child = n.getFirstChild();
                int type = rewriteForNumberVariables(child, 1);
                if (child.getType() == 55) {
                    if (type != 1 || convertParameter(child)) {
                        return 0;
                    }
                    n.putIntProp(8, 0);
                    markDCPNumberContext(child);
                    return 1;
                } else if (child.getType() == 36 || child.getType() == 33) {
                    return type;
                } else {
                    return 0;
                }
            case 133:
                if (rewriteForNumberVariables(n.getFirstChild(), 1) == 1) {
                    n.putIntProp(8, 0);
                }
                return 0;
            default:
                rewriteAsObjectChildren(n, n.getFirstChild());
                return 0;
        }
    }

    private void rewriteAsObjectChildren(Node n, Node child) {
        while (child != null) {
            Node nextChild = child.getNext();
            if (rewriteForNumberVariables(child, 0) == 1 && !convertParameter(child)) {
                n.removeChild(child);
                Node nuChild = new Node(149, child);
                if (nextChild == null) {
                    n.addChildToBack(nuChild);
                } else {
                    n.addChildBefore(nuChild, nextChild);
                }
            }
            child = nextChild;
        }
    }

    private static void buildStatementList_r(Node node, ObjArray statements) {
        int type = node.getType();
        if (type == 129 || type == 141 || type == 132 || type == 109) {
            for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
                buildStatementList_r(child, statements);
            }
            return;
        }
        statements.add(node);
    }
}
