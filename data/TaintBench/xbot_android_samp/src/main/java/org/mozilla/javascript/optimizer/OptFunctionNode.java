package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.ScriptNode;

public final class OptFunctionNode {
    private int directTargetIndex = -1;
    public final FunctionNode fnode;
    boolean itsContainsCalls0;
    boolean itsContainsCalls1;
    private boolean itsParameterNumberContext;
    private boolean[] numberVarFlags;

    OptFunctionNode(FunctionNode fnode) {
        this.fnode = fnode;
        fnode.setCompilerData(this);
    }

    public static OptFunctionNode get(ScriptNode scriptOrFn, int i) {
        return (OptFunctionNode) scriptOrFn.getFunctionNode(i).getCompilerData();
    }

    public static OptFunctionNode get(ScriptNode scriptOrFn) {
        return (OptFunctionNode) scriptOrFn.getCompilerData();
    }

    public boolean isTargetOfDirectCall() {
        return this.directTargetIndex >= 0;
    }

    public int getDirectTargetIndex() {
        return this.directTargetIndex;
    }

    /* access modifiers changed from: 0000 */
    public void setDirectTargetIndex(int directTargetIndex) {
        if (directTargetIndex < 0 || this.directTargetIndex >= 0) {
            Kit.codeBug();
        }
        this.directTargetIndex = directTargetIndex;
    }

    /* access modifiers changed from: 0000 */
    public void setParameterNumberContext(boolean b) {
        this.itsParameterNumberContext = b;
    }

    public boolean getParameterNumberContext() {
        return this.itsParameterNumberContext;
    }

    public int getVarCount() {
        return this.fnode.getParamAndVarCount();
    }

    public boolean isParameter(int varIndex) {
        return varIndex < this.fnode.getParamCount();
    }

    public boolean isNumberVar(int varIndex) {
        varIndex -= this.fnode.getParamCount();
        if (varIndex < 0 || this.numberVarFlags == null) {
            return false;
        }
        return this.numberVarFlags[varIndex];
    }

    /* access modifiers changed from: 0000 */
    public void setIsNumberVar(int varIndex) {
        varIndex -= this.fnode.getParamCount();
        if (varIndex < 0) {
            Kit.codeBug();
        }
        if (this.numberVarFlags == null) {
            this.numberVarFlags = new boolean[(this.fnode.getParamAndVarCount() - this.fnode.getParamCount())];
        }
        this.numberVarFlags[varIndex] = true;
    }

    public int getVarIndex(Node n) {
        int index = n.getIntProp(7, -1);
        if (index == -1) {
            Node node;
            int type = n.getType();
            if (type == 55) {
                node = n;
            } else if (type == 56 || type == 156) {
                node = n.getFirstChild();
            } else {
                throw Kit.codeBug();
            }
            index = this.fnode.getIndexForNameNode(node);
            if (index < 0) {
                throw Kit.codeBug();
            }
            n.putIntProp(7, index);
        }
        return index;
    }
}
