package org.mozilla.javascript.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.mozilla.classfile.ClassFileWriter;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Jump;
import org.mozilla.javascript.ast.ScriptNode;

/* compiled from: Codegen */
class BodyCodegen {
    static final /* synthetic */ boolean $assertionsDisabled = (!BodyCodegen.class.desiredAssertionStatus());
    private static final int ECMAERROR_EXCEPTION = 2;
    private static final int EVALUATOR_EXCEPTION = 1;
    private static final int EXCEPTION_MAX = 5;
    private static final int FINALLY_EXCEPTION = 4;
    static final int GENERATOR_START = 0;
    static final int GENERATOR_TERMINATE = -1;
    static final int GENERATOR_YIELD_START = 1;
    private static final int JAVASCRIPT_EXCEPTION = 0;
    private static final int MAX_LOCALS = 1024;
    private static final int THROWABLE_EXCEPTION = 3;
    private short argsLocal;
    ClassFileWriter cfw;
    Codegen codegen;
    CompilerEnvirons compilerEnv;
    private short contextLocal;
    private int enterAreaStartLabel;
    private int epilogueLabel;
    private ExceptionManager exceptionManager = new ExceptionManager();
    private Map<Node, FinallyReturnPoint> finallys;
    private short firstFreeLocal;
    private OptFunctionNode fnCurrent;
    private short funObjLocal;
    private short generatorStateLocal;
    private int generatorSwitch;
    private boolean hasVarsInRegs;
    private boolean inDirectCallFunction;
    private boolean inLocalBlock;
    private boolean isGenerator;
    private boolean itsForcedObjectParameters;
    private int itsLineNumber;
    private short itsOneArgArray;
    private short itsZeroArgArray;
    private List<Node> literals;
    private int[] locals;
    private short localsMax;
    private int maxLocals = 0;
    private int maxStack = 0;
    private short operationLocal;
    private short popvLocal;
    private int savedCodeOffset;
    ScriptNode scriptOrFn;
    public int scriptOrFnIndex;
    private short thisObjLocal;
    private short[] varRegisters;
    private short variableObjectLocal;

    /* compiled from: Codegen */
    private class ExceptionManager {
        private LinkedList<ExceptionInfo> exceptionInfo = new LinkedList();

        /* compiled from: Codegen */
        private class ExceptionInfo {
            Node currentFinally = null;
            int[] exceptionStarts = new int[5];
            Node finallyBlock;
            int[] handlerLabels = new int[5];
            Jump node;

            ExceptionInfo(Jump node, Node finallyBlock) {
                this.node = node;
                this.finallyBlock = finallyBlock;
            }
        }

        ExceptionManager() {
        }

        /* access modifiers changed from: 0000 */
        public void pushExceptionInfo(Jump node) {
            this.exceptionInfo.add(new ExceptionInfo(node, BodyCodegen.this.getFinallyAtTarget(node.getFinally())));
        }

        /* access modifiers changed from: 0000 */
        public void addHandler(int exceptionType, int handlerLabel, int startLabel) {
            ExceptionInfo top = getTop();
            top.handlerLabels[exceptionType] = handlerLabel;
            top.exceptionStarts[exceptionType] = startLabel;
        }

        /* access modifiers changed from: 0000 */
        public void setHandlers(int[] handlerLabels, int startLabel) {
            ExceptionInfo top = getTop();
            for (int i = 0; i < handlerLabels.length; i++) {
                if (handlerLabels[i] != 0) {
                    addHandler(i, handlerLabels[i], startLabel);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public int removeHandler(int exceptionType, int endLabel) {
            ExceptionInfo top = getTop();
            if (top.handlerLabels[exceptionType] == 0) {
                return 0;
            }
            int handlerLabel = top.handlerLabels[exceptionType];
            endCatch(top, exceptionType, endLabel);
            top.handlerLabels[exceptionType] = 0;
            return handlerLabel;
        }

        /* access modifiers changed from: 0000 */
        public void popExceptionInfo() {
            this.exceptionInfo.removeLast();
        }

        /* access modifiers changed from: 0000 */
        public void markInlineFinallyStart(Node finallyBlock, int finallyStart) {
            ListIterator<ExceptionInfo> iter = this.exceptionInfo.listIterator(this.exceptionInfo.size());
            while (iter.hasPrevious()) {
                ExceptionInfo ei = (ExceptionInfo) iter.previous();
                for (int i = 0; i < 5; i++) {
                    if (ei.handlerLabels[i] != 0 && ei.currentFinally == null) {
                        endCatch(ei, i, finallyStart);
                        ei.exceptionStarts[i] = 0;
                        ei.currentFinally = finallyBlock;
                    }
                }
                if (ei.finallyBlock == finallyBlock) {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void markInlineFinallyEnd(Node finallyBlock, int finallyEnd) {
            ListIterator<ExceptionInfo> iter = this.exceptionInfo.listIterator(this.exceptionInfo.size());
            while (iter.hasPrevious()) {
                ExceptionInfo ei = (ExceptionInfo) iter.previous();
                for (int i = 0; i < 5; i++) {
                    if (ei.handlerLabels[i] != 0 && ei.currentFinally == finallyBlock) {
                        ei.exceptionStarts[i] = finallyEnd;
                        ei.currentFinally = null;
                    }
                }
                if (ei.finallyBlock == finallyBlock) {
                    return;
                }
            }
        }

        private void endCatch(ExceptionInfo ei, int exceptionType, int catchEnd) {
            if (ei.exceptionStarts[exceptionType] == 0) {
                throw new IllegalStateException("bad exception start");
            }
            if (BodyCodegen.this.cfw.getLabelPC(ei.exceptionStarts[exceptionType]) != BodyCodegen.this.cfw.getLabelPC(catchEnd)) {
                BodyCodegen.this.cfw.addExceptionHandler(ei.exceptionStarts[exceptionType], catchEnd, ei.handlerLabels[exceptionType], BodyCodegen.this.exceptionTypeToName(exceptionType));
            }
        }

        private ExceptionInfo getTop() {
            return (ExceptionInfo) this.exceptionInfo.getLast();
        }
    }

    /* compiled from: Codegen */
    static class FinallyReturnPoint {
        public List<Integer> jsrPoints = new ArrayList();
        public int tableLabel = 0;

        FinallyReturnPoint() {
        }
    }

    BodyCodegen() {
    }

    /* access modifiers changed from: 0000 */
    public void generateBodyCode() {
        Node treeTop;
        this.isGenerator = Codegen.isGenerator(this.scriptOrFn);
        initBodyGeneration();
        if (this.isGenerator) {
            this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn) + "_gen", "(" + this.codegen.mainClassSignature + "Lorg/mozilla/javascript/Context;" + "Lorg/mozilla/javascript/Scriptable;" + "Ljava/lang/Object;" + "Ljava/lang/Object;I)Ljava/lang/Object;", (short) 10);
        } else {
            this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short) 10);
        }
        generatePrologue();
        if (this.fnCurrent != null) {
            treeTop = this.scriptOrFn.getLastChild();
        } else {
            treeTop = this.scriptOrFn;
        }
        generateStatement(treeTop);
        generateEpilogue();
        this.cfw.stopMethod((short) (this.localsMax + 1));
        if (this.isGenerator) {
            generateGenerator();
        }
        if (this.literals != null) {
            for (int i = 0; i < this.literals.size(); i++) {
                Node node = (Node) this.literals.get(i);
                int type = node.getType();
                switch (type) {
                    case 65:
                        generateArrayLiteralFactory(node, i + 1);
                        break;
                    case 66:
                        generateObjectLiteralFactory(node, i + 1);
                        break;
                    default:
                        Kit.codeBug(Token.typeToName(type));
                        break;
                }
            }
        }
    }

    private void generateGenerator() {
        this.cfw.startMethod(this.codegen.getBodyMethodName(this.scriptOrFn), this.codegen.getBodyMethodSignature(this.scriptOrFn), (short) 10);
        initBodyGeneration();
        short s = this.firstFreeLocal;
        this.firstFreeLocal = (short) (s + 1);
        this.argsLocal = s;
        this.localsMax = this.firstFreeLocal;
        if (this.fnCurrent != null) {
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.variableObjectLocal);
        }
        this.cfw.addALoad(this.funObjLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.argsLocal);
        addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
        this.cfw.add(187, this.codegen.mainClassName);
        this.cfw.add(89);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(this.scriptOrFnIndex);
        this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
        generateNestedFunctionInits();
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.thisObjLocal);
        this.cfw.addLoadConstant(this.maxLocals);
        this.cfw.addLoadConstant(this.maxStack);
        addOptRuntimeInvoke("createNativeGenerator", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;II)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.add(176);
        this.cfw.stopMethod((short) (this.localsMax + 1));
    }

    private void generateNestedFunctionInits() {
        int functionCount = this.scriptOrFn.getFunctionCount();
        for (int i = 0; i != functionCount; i++) {
            OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFn, i);
            if (ofn.fnode.getFunctionType() == 1) {
                visitFunction(ofn, 1);
            }
        }
    }

    private void initBodyGeneration() {
        this.varRegisters = null;
        if (this.scriptOrFn.getType() == 109) {
            this.fnCurrent = OptFunctionNode.get(this.scriptOrFn);
            this.hasVarsInRegs = !this.fnCurrent.fnode.requiresActivation();
            if (this.hasVarsInRegs) {
                int n = this.fnCurrent.fnode.getParamAndVarCount();
                if (n != 0) {
                    this.varRegisters = new short[n];
                }
            }
            this.inDirectCallFunction = this.fnCurrent.isTargetOfDirectCall();
            if (this.inDirectCallFunction && !this.hasVarsInRegs) {
                Codegen.badTree();
            }
        } else {
            this.fnCurrent = null;
            this.hasVarsInRegs = false;
            this.inDirectCallFunction = false;
        }
        this.locals = new int[1024];
        this.funObjLocal = (short) 0;
        this.contextLocal = (short) 1;
        this.variableObjectLocal = (short) 2;
        this.thisObjLocal = (short) 3;
        this.localsMax = (short) 4;
        this.firstFreeLocal = (short) 4;
        this.popvLocal = (short) -1;
        this.argsLocal = (short) -1;
        this.itsZeroArgArray = (short) -1;
        this.itsOneArgArray = (short) -1;
        this.epilogueLabel = -1;
        this.enterAreaStartLabel = -1;
        this.generatorStateLocal = (short) -1;
    }

    private void generatePrologue() {
        int i;
        short reg;
        if (this.inDirectCallFunction) {
            int directParameterCount = this.scriptOrFn.getParamCount();
            if (this.firstFreeLocal != (short) 4) {
                Kit.codeBug();
            }
            for (i = 0; i != directParameterCount; i++) {
                this.varRegisters[i] = this.firstFreeLocal;
                this.firstFreeLocal = (short) (this.firstFreeLocal + 3);
            }
            if (!this.fnCurrent.getParameterNumberContext()) {
                this.itsForcedObjectParameters = true;
                for (i = 0; i != directParameterCount; i++) {
                    reg = this.varRegisters[i];
                    this.cfw.addALoad(reg);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    int isObjectLabel = this.cfw.acquireLabel();
                    this.cfw.add(166, isObjectLabel);
                    this.cfw.addDLoad(reg + 1);
                    addDoubleWrap();
                    this.cfw.addAStore(reg);
                    this.cfw.markLabel(isObjectLabel);
                }
            }
        }
        if (this.fnCurrent != null) {
            this.cfw.addALoad(this.funObjLocal);
            this.cfw.addInvoke(185, "org/mozilla/javascript/Scriptable", "getParentScope", "()Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.variableObjectLocal);
        }
        short s = this.firstFreeLocal;
        this.firstFreeLocal = (short) (s + 1);
        this.argsLocal = s;
        this.localsMax = this.firstFreeLocal;
        if (this.isGenerator) {
            s = this.firstFreeLocal;
            this.firstFreeLocal = (short) (s + 1);
            this.operationLocal = s;
            this.localsMax = this.firstFreeLocal;
            this.cfw.addALoad(this.thisObjLocal);
            s = this.firstFreeLocal;
            this.firstFreeLocal = (short) (s + 1);
            this.generatorStateLocal = s;
            this.localsMax = this.firstFreeLocal;
            this.cfw.add(192, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState");
            this.cfw.add(89);
            this.cfw.addAStore(this.generatorStateLocal);
            this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "thisObj", "Lorg/mozilla/javascript/Scriptable;");
            this.cfw.addAStore(this.thisObjLocal);
            if (this.epilogueLabel == -1) {
                this.epilogueLabel = this.cfw.acquireLabel();
            }
            List<Node> targets = ((FunctionNode) this.scriptOrFn).getResumptionPoints();
            if (targets != null) {
                generateGetGeneratorResumptionPoint();
                this.generatorSwitch = this.cfw.addTableSwitch(0, targets.size() + 0);
                generateCheckForThrowOrClose(-1, false, 0);
            }
        }
        if (this.fnCurrent == null && this.scriptOrFn.getRegexpCount() != 0) {
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addInvoke(184, this.codegen.mainClassName, "_reInit", "(Lorg/mozilla/javascript/Context;)V");
        }
        if (this.compilerEnv.isGenerateObserverCount()) {
            saveCurrentCodeOffset();
        }
        if (this.hasVarsInRegs) {
            int parmCount = this.scriptOrFn.getParamCount();
            if (parmCount > 0 && !this.inDirectCallFunction) {
                this.cfw.addALoad(this.argsLocal);
                this.cfw.add(190);
                this.cfw.addPush(parmCount);
                int label = this.cfw.acquireLabel();
                this.cfw.add(162, label);
                this.cfw.addALoad(this.argsLocal);
                this.cfw.addPush(parmCount);
                addScriptRuntimeInvoke("padArguments", "([Ljava/lang/Object;I)[Ljava/lang/Object;");
                this.cfw.addAStore(this.argsLocal);
                this.cfw.markLabel(label);
            }
            int paramCount = this.fnCurrent.fnode.getParamCount();
            int varCount = this.fnCurrent.fnode.getParamAndVarCount();
            boolean[] constDeclarations = this.fnCurrent.fnode.getParamAndVarConst();
            short firstUndefVar = (short) -1;
            for (i = 0; i != varCount; i++) {
                reg = (short) -1;
                if (i < paramCount) {
                    if (!this.inDirectCallFunction) {
                        reg = getNewWordLocal();
                        this.cfw.addALoad(this.argsLocal);
                        this.cfw.addPush(i);
                        this.cfw.add(50);
                        this.cfw.addAStore(reg);
                    }
                } else if (this.fnCurrent.isNumberVar(i)) {
                    reg = getNewWordPairLocal(constDeclarations[i]);
                    this.cfw.addPush(0.0d);
                    this.cfw.addDStore(reg);
                } else {
                    reg = getNewWordLocal(constDeclarations[i]);
                    if (firstUndefVar == (short) -1) {
                        Codegen.pushUndefined(this.cfw);
                        firstUndefVar = reg;
                    } else {
                        this.cfw.addALoad(firstUndefVar);
                    }
                    this.cfw.addAStore(reg);
                }
                if (reg >= (short) 0) {
                    if (constDeclarations[i]) {
                        this.cfw.addPush(0);
                        this.cfw.addIStore((this.fnCurrent.isNumberVar(i) ? 2 : 1) + reg);
                    }
                    this.varRegisters[i] = reg;
                }
                if (this.compilerEnv.isGenerateDebugInfo()) {
                    String name = this.fnCurrent.fnode.getParamOrVarName(i);
                    String type = this.fnCurrent.isNumberVar(i) ? "D" : "Ljava/lang/Object;";
                    int startPC = this.cfw.getCurrentCodeOffset();
                    if (reg < (short) 0) {
                        reg = this.varRegisters[i];
                    }
                    this.cfw.addVariableDescriptor(name, type, startPC, reg);
                }
            }
        } else if (!this.isGenerator) {
            String debugVariableName;
            if (this.fnCurrent != null) {
                debugVariableName = "activation";
                this.cfw.addALoad(this.funObjLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addALoad(this.argsLocal);
                addScriptRuntimeInvoke("createFunctionActivation", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
                this.cfw.addAStore(this.variableObjectLocal);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("enterActivationFunction", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)V");
            } else {
                debugVariableName = "global";
                this.cfw.addALoad(this.funObjLocal);
                this.cfw.addALoad(this.thisObjLocal);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(0);
                addScriptRuntimeInvoke("initScript", "(Lorg/mozilla/javascript/NativeFunction;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Z)V");
            }
            this.enterAreaStartLabel = this.cfw.acquireLabel();
            this.epilogueLabel = this.cfw.acquireLabel();
            this.cfw.markLabel(this.enterAreaStartLabel);
            generateNestedFunctionInits();
            if (this.compilerEnv.isGenerateDebugInfo()) {
                this.cfw.addVariableDescriptor(debugVariableName, "Lorg/mozilla/javascript/Scriptable;", this.cfw.getCurrentCodeOffset(), this.variableObjectLocal);
            }
            if (this.fnCurrent == null) {
                this.popvLocal = getNewWordLocal();
                Codegen.pushUndefined(this.cfw);
                this.cfw.addAStore(this.popvLocal);
                int linenum = this.scriptOrFn.getEndLineno();
                if (linenum != -1) {
                    this.cfw.addLineNumberEntry((short) linenum);
                    return;
                }
                return;
            }
            if (this.fnCurrent.itsContainsCalls0) {
                this.itsZeroArgArray = getNewWordLocal();
                this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
                this.cfw.addAStore(this.itsZeroArgArray);
            }
            if (this.fnCurrent.itsContainsCalls1) {
                this.itsOneArgArray = getNewWordLocal();
                this.cfw.addPush(1);
                this.cfw.add(189, "java/lang/Object");
                this.cfw.addAStore(this.itsOneArgArray);
            }
        }
    }

    private void generateGetGeneratorResumptionPoint() {
        this.cfw.addALoad(this.generatorStateLocal);
        this.cfw.add(180, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
    }

    private void generateSetGeneratorResumptionPoint(int nextState) {
        this.cfw.addALoad(this.generatorStateLocal);
        this.cfw.addLoadConstant(nextState);
        this.cfw.add(181, "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
    }

    private void generateGetGeneratorStackState() {
        this.cfw.addALoad(this.generatorStateLocal);
        addOptRuntimeInvoke("getGeneratorStackState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
    }

    private void generateEpilogue() {
        if (this.compilerEnv.isGenerateObserverCount()) {
            addInstructionCount();
        }
        if (this.isGenerator) {
            int i;
            Map<Node, int[]> liveLocals = ((FunctionNode) this.scriptOrFn).getLiveLocals();
            if (liveLocals != null) {
                List<Node> nodes = ((FunctionNode) this.scriptOrFn).getResumptionPoints();
                for (i = 0; i < nodes.size(); i++) {
                    Node node = (Node) nodes.get(i);
                    int[] live = (int[]) liveLocals.get(node);
                    if (live != null) {
                        this.cfw.markTableSwitchCase(this.generatorSwitch, getNextGeneratorState(node));
                        generateGetGeneratorLocalsState();
                        for (int j = 0; j < live.length; j++) {
                            this.cfw.add(89);
                            this.cfw.addLoadConstant(j);
                            this.cfw.add(50);
                            this.cfw.addAStore(live[j]);
                        }
                        this.cfw.add(87);
                        this.cfw.add(167, getTargetLabel(node));
                    }
                }
            }
            if (this.finallys != null) {
                for (Node n : this.finallys.keySet()) {
                    if (n.getType() == 125) {
                        FinallyReturnPoint ret = (FinallyReturnPoint) this.finallys.get(n);
                        this.cfw.markLabel(ret.tableLabel, (short) 1);
                        int startSwitch = this.cfw.addTableSwitch(0, ret.jsrPoints.size() - 1);
                        int c = 0;
                        this.cfw.markTableSwitchDefault(startSwitch);
                        for (i = 0; i < ret.jsrPoints.size(); i++) {
                            this.cfw.markTableSwitchCase(startSwitch, c);
                            this.cfw.add(167, ((Integer) ret.jsrPoints.get(i)).intValue());
                            c++;
                        }
                    }
                }
            }
        }
        if (this.epilogueLabel != -1) {
            this.cfw.markLabel(this.epilogueLabel);
        }
        if (this.hasVarsInRegs) {
            this.cfw.add(176);
        } else if (this.isGenerator) {
            if (((FunctionNode) this.scriptOrFn).getResumptionPoints() != null) {
                this.cfw.markTableSwitchDefault(this.generatorSwitch);
            }
            generateSetGeneratorResumptionPoint(-1);
            this.cfw.addALoad(this.variableObjectLocal);
            addOptRuntimeInvoke("throwStopIteration", "(Ljava/lang/Object;)V");
            Codegen.pushUndefined(this.cfw);
            this.cfw.add(176);
        } else if (this.fnCurrent == null) {
            this.cfw.addALoad(this.popvLocal);
            this.cfw.add(176);
        } else {
            generateActivationExit();
            this.cfw.add(176);
            int finallyHandler = this.cfw.acquireLabel();
            this.cfw.markHandler(finallyHandler);
            short exceptionObject = getNewWordLocal();
            this.cfw.addAStore(exceptionObject);
            generateActivationExit();
            this.cfw.addALoad(exceptionObject);
            releaseWordLocal(exceptionObject);
            this.cfw.add(191);
            this.cfw.addExceptionHandler(this.enterAreaStartLabel, this.epilogueLabel, finallyHandler, null);
        }
    }

    private void generateGetGeneratorLocalsState() {
        this.cfw.addALoad(this.generatorStateLocal);
        addOptRuntimeInvoke("getGeneratorLocalsState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
    }

    private void generateActivationExit() {
        if (this.fnCurrent == null || this.hasVarsInRegs) {
            throw Kit.codeBug();
        }
        this.cfw.addALoad(this.contextLocal);
        addScriptRuntimeInvoke("exitActivationFunction", "(Lorg/mozilla/javascript/Context;)V");
    }

    private void generateStatement(Node node) {
        updateLineNumber(node);
        int type = node.getType();
        Node child = node.getFirstChild();
        int local;
        switch (type) {
            case 2:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("enterWith", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                this.cfw.addAStore(this.variableObjectLocal);
                incReferenceWordLocal(this.variableObjectLocal);
                return;
            case 3:
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("leaveWith", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                this.cfw.addAStore(this.variableObjectLocal);
                decReferenceWordLocal(this.variableObjectLocal);
                return;
            case 4:
            case 64:
                if (!this.isGenerator) {
                    if (child != null) {
                        generateExpression(child, node);
                    } else if (type == 4) {
                        Codegen.pushUndefined(this.cfw);
                    } else if (this.popvLocal < (short) 0) {
                        throw Codegen.badTree();
                    } else {
                        this.cfw.addALoad(this.popvLocal);
                    }
                }
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                if (this.epilogueLabel == -1) {
                    if (this.hasVarsInRegs) {
                        this.epilogueLabel = this.cfw.acquireLabel();
                    } else {
                        throw Codegen.badTree();
                    }
                }
                this.cfw.add(167, this.epilogueLabel);
                return;
            case 5:
            case 6:
            case 7:
            case 135:
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                visitGoto((Jump) node, type, child);
                return;
            case 50:
                generateExpression(child, node);
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                generateThrowJavaScriptException();
                return;
            case 51:
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                this.cfw.addALoad(getLocalBlockRegister(node));
                this.cfw.add(191);
                return;
            case 57:
                this.cfw.setStackTop((short) 0);
                local = getLocalBlockRegister(node);
                int scopeIndex = node.getExistingIntProp(14);
                String name = child.getString();
                generateExpression(child.getNext(), node);
                if (scopeIndex == 0) {
                    this.cfw.add(1);
                } else {
                    this.cfw.addALoad(local);
                }
                this.cfw.addPush(name);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("newCatchScope", "(Ljava/lang/Throwable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
                this.cfw.addAStore(local);
                return;
            case 58:
            case 59:
            case 60:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                int enumType = type == 58 ? 0 : type == 59 ? 1 : 2;
                this.cfw.addPush(enumType);
                addScriptRuntimeInvoke("enumInit", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                this.cfw.addAStore(getLocalBlockRegister(node));
                return;
            case 81:
                visitTryCatchFinally((Jump) node, child);
                return;
            case 109:
                OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFn, node.getExistingIntProp(1));
                int t = ofn.fnode.getFunctionType();
                if (t == 3) {
                    visitFunction(ofn, t);
                    return;
                } else if (t != 1) {
                    throw Codegen.badTree();
                } else {
                    return;
                }
            case 114:
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                visitSwitch((Jump) node, child);
                return;
            case 123:
            case 128:
            case 129:
            case 130:
            case 132:
            case 136:
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount(1);
                }
                while (child != null) {
                    generateStatement(child);
                    child = child.getNext();
                }
                return;
            case 125:
                if (this.isGenerator) {
                    if (this.compilerEnv.isGenerateObserverCount()) {
                        saveCurrentCodeOffset();
                    }
                    this.cfw.setStackTop((short) 1);
                    int finallyRegister = getNewWordLocal();
                    int finallyStart = this.cfw.acquireLabel();
                    int finallyEnd = this.cfw.acquireLabel();
                    this.cfw.markLabel(finallyStart);
                    generateIntegerWrap();
                    this.cfw.addAStore(finallyRegister);
                    while (child != null) {
                        generateStatement(child);
                        child = child.getNext();
                    }
                    this.cfw.addALoad(finallyRegister);
                    this.cfw.add(192, "java/lang/Integer");
                    generateIntegerUnwrap();
                    FinallyReturnPoint ret = (FinallyReturnPoint) this.finallys.get(node);
                    ret.tableLabel = this.cfw.acquireLabel();
                    this.cfw.add(167, ret.tableLabel);
                    releaseWordLocal((short) finallyRegister);
                    this.cfw.markLabel(finallyEnd);
                    return;
                }
                return;
            case 131:
                if (this.compilerEnv.isGenerateObserverCount()) {
                    addInstructionCount();
                }
                this.cfw.markLabel(getTargetLabel(node));
                if (this.compilerEnv.isGenerateObserverCount()) {
                    saveCurrentCodeOffset();
                    return;
                }
                return;
            case 133:
                if (child.getType() == 56) {
                    visitSetVar(child, child.getFirstChild(), false);
                    return;
                } else if (child.getType() == 156) {
                    visitSetConstVar(child, child.getFirstChild(), false);
                    return;
                } else if (child.getType() == 72) {
                    generateYieldPoint(child, false);
                    return;
                } else {
                    generateExpression(child, node);
                    if (node.getIntProp(8, -1) != -1) {
                        this.cfw.add(88);
                        return;
                    } else {
                        this.cfw.add(87);
                        return;
                    }
                }
            case 134:
                generateExpression(child, node);
                if (this.popvLocal < (short) 0) {
                    this.popvLocal = getNewWordLocal();
                }
                this.cfw.addAStore(this.popvLocal);
                return;
            case 141:
                boolean prevLocal = this.inLocalBlock;
                this.inLocalBlock = true;
                local = getNewWordLocal();
                if (this.isGenerator) {
                    this.cfw.add(1);
                    this.cfw.addAStore(local);
                }
                node.putIntProp(2, local);
                while (child != null) {
                    generateStatement(child);
                    child = child.getNext();
                }
                releaseWordLocal((short) local);
                node.removeProp(2);
                this.inLocalBlock = prevLocal;
                return;
            case 160:
                return;
            default:
                throw Codegen.badTree();
        }
    }

    private void generateIntegerWrap() {
        this.cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
    }

    private void generateIntegerUnwrap() {
        this.cfw.addInvoke(182, "java/lang/Integer", "intValue", "()I");
    }

    private void generateThrowJavaScriptException() {
        this.cfw.add(187, "org/mozilla/javascript/JavaScriptException");
        this.cfw.add(90);
        this.cfw.add(95);
        this.cfw.addPush(this.scriptOrFn.getSourceName());
        this.cfw.addPush(this.itsLineNumber);
        this.cfw.addInvoke(183, "org/mozilla/javascript/JavaScriptException", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V");
        this.cfw.add(191);
    }

    private int getNextGeneratorState(Node node) {
        return ((FunctionNode) this.scriptOrFn).getResumptionPoints().indexOf(node) + 1;
    }

    private void generateExpression(Node node, Node parent) {
        int type = node.getType();
        Node child = node.getFirstChild();
        int trueGOTO;
        int falseGOTO;
        int falseTarget;
        switch (type) {
            case 8:
                visitSetName(node, child);
                return;
            case 9:
            case 10:
            case 11:
            case 18:
            case 19:
            case 20:
                visitBitOp(node, type, child);
                return;
            case 12:
            case 13:
            case 46:
            case 47:
                trueGOTO = this.cfw.acquireLabel();
                falseGOTO = this.cfw.acquireLabel();
                visitIfJumpEqOp(node, child, trueGOTO, falseGOTO);
                addJumpedBooleanWrap(trueGOTO, falseGOTO);
                return;
            case 14:
            case 15:
            case 16:
            case 17:
            case 52:
            case 53:
                trueGOTO = this.cfw.acquireLabel();
                falseGOTO = this.cfw.acquireLabel();
                visitIfJumpRelOp(node, child, trueGOTO, falseGOTO);
                addJumpedBooleanWrap(trueGOTO, falseGOTO);
                return;
            case 21:
                generateExpression(child, node);
                generateExpression(child.getNext(), node);
                switch (node.getIntProp(8, -1)) {
                    case 0:
                        this.cfw.add(99);
                        return;
                    case 1:
                        addOptRuntimeInvoke("add", "(DLjava/lang/Object;)Ljava/lang/Object;");
                        return;
                    case 2:
                        addOptRuntimeInvoke("add", "(Ljava/lang/Object;D)Ljava/lang/Object;");
                        return;
                    default:
                        if (child.getType() == 41) {
                            addScriptRuntimeInvoke("add", "(Ljava/lang/CharSequence;Ljava/lang/Object;)Ljava/lang/CharSequence;");
                            return;
                        } else if (child.getNext().getType() == 41) {
                            addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
                            return;
                        } else {
                            this.cfw.addALoad(this.contextLocal);
                            addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                            return;
                        }
                }
            case 22:
                visitArithmetic(node, 103, child, parent);
                return;
            case 23:
                visitArithmetic(node, 107, child, parent);
                return;
            case 24:
            case 25:
                visitArithmetic(node, type == 24 ? 111 : 115, child, parent);
                return;
            case 26:
                int trueTarget = this.cfw.acquireLabel();
                falseTarget = this.cfw.acquireLabel();
                int beyond = this.cfw.acquireLabel();
                generateIfJump(child, node, trueTarget, falseTarget);
                this.cfw.markLabel(trueTarget);
                this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                this.cfw.add(167, beyond);
                this.cfw.markLabel(falseTarget);
                this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                this.cfw.markLabel(beyond);
                this.cfw.adjustStackTop(-1);
                return;
            case 27:
                generateExpression(child, node);
                addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
                this.cfw.addPush(-1);
                this.cfw.add(130);
                this.cfw.add(135);
                addDoubleWrap();
                return;
            case 28:
            case 29:
                generateExpression(child, node);
                addObjectToDouble();
                if (type == 29) {
                    this.cfw.add(119);
                }
                addDoubleWrap();
                return;
            case 30:
            case 38:
                int specialType = node.getIntProp(10, 0);
                if (specialType == 0) {
                    OptFunctionNode target = (OptFunctionNode) node.getProp(9);
                    if (target != null) {
                        visitOptimizedCall(node, target, type, child);
                        return;
                    } else if (type == 38) {
                        visitStandardCall(node, child);
                        return;
                    } else {
                        visitStandardNew(node, child);
                        return;
                    }
                }
                visitSpecialCall(node, type, specialType, child);
                return;
            case 31:
                boolean isName = child.getType() == 49;
                generateExpression(child, node);
                generateExpression(child.getNext(), node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addPush(isName);
                addScriptRuntimeInvoke("delete", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Z)Ljava/lang/Object;");
                return;
            case 32:
                generateExpression(child, node);
                addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                return;
            case 33:
            case 34:
                visitGetProp(node, child);
                return;
            case 35:
            case 139:
                visitSetProp(type, node, child);
                return;
            case 36:
                generateExpression(child, node);
                generateExpression(child.getNext(), node);
                this.cfw.addALoad(this.contextLocal);
                if (node.getIntProp(8, -1) != -1) {
                    addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                    return;
                }
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
                return;
            case 37:
            case 140:
                visitSetElem(type, node, child);
                return;
            case 39:
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(node.getString());
                addScriptRuntimeInvoke("name", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
                return;
            case 40:
                double num = node.getDouble();
                if (node.getIntProp(8, -1) != -1) {
                    this.cfw.addPush(num);
                    return;
                } else {
                    this.codegen.pushNumberAsObject(this.cfw, num);
                    return;
                }
            case 41:
                this.cfw.addPush(node.getString());
                return;
            case 42:
                this.cfw.add(1);
                return;
            case 43:
                this.cfw.addALoad(this.thisObjLocal);
                return;
            case 44:
                this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
                return;
            case 45:
                this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
                return;
            case 48:
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.add(178, this.codegen.mainClassName, this.codegen.getCompiledRegexpName(this.scriptOrFn, node.getExistingIntProp(4)), "Ljava/lang/Object;");
                this.cfw.addInvoke(184, "org/mozilla/javascript/ScriptRuntime", "wrapRegExp", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
                return;
            case 49:
                break;
            case 54:
                this.cfw.addALoad(getLocalBlockRegister(node));
                return;
            case 55:
                visitGetVar(node);
                return;
            case 56:
                visitSetVar(node, child, true);
                return;
            case 61:
            case 62:
                this.cfw.addALoad(getLocalBlockRegister(node));
                if (type == 61) {
                    addScriptRuntimeInvoke("enumNext", "(Ljava/lang/Object;)Ljava/lang/Boolean;");
                    return;
                }
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("enumId", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                return;
            case 63:
                this.cfw.add(42);
                return;
            case 65:
                visitArrayLiteral(node, child, false);
                return;
            case 66:
                visitObjectLiteral(node, child, false);
                return;
            case 67:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                return;
            case 68:
            case 142:
                generateExpression(child, node);
                child = child.getNext();
                if (type == 142) {
                    this.cfw.add(89);
                    this.cfw.addALoad(this.contextLocal);
                    addScriptRuntimeInvoke("refGet", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                }
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("refSet", "(Lorg/mozilla/javascript/Ref;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
                return;
            case 69:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("refDel", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                return;
            case 70:
                generateFunctionAndThisObj(child, node);
                generateCallArgArray(node, child.getNext(), false);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("callRef", "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Ref;");
                return;
            case 71:
                String special = (String) node.getProp(17);
                generateExpression(child, node);
                this.cfw.addPush(special);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("specialRef", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Ref;");
                return;
            case 72:
                generateYieldPoint(node, true);
                return;
            case 73:
                visitStrictSetName(node, child);
                return;
            case 74:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("setDefaultNamespace", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
                return;
            case 75:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("escapeAttributeValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
                return;
            case 76:
                generateExpression(child, node);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("escapeTextValue", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Ljava/lang/String;");
                return;
            case 77:
            case 78:
            case 79:
            case 80:
                String methodName;
                String signature;
                int memberTypeFlags = node.getIntProp(16, 0);
                do {
                    generateExpression(child, node);
                    child = child.getNext();
                } while (child != null);
                this.cfw.addALoad(this.contextLocal);
                switch (type) {
                    case 77:
                        methodName = "memberRef";
                        signature = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
                        break;
                    case 78:
                        methodName = "memberRef";
                        signature = "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;I)Lorg/mozilla/javascript/Ref;";
                        break;
                    case 79:
                        methodName = "nameRef";
                        signature = "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
                        this.cfw.addALoad(this.variableObjectLocal);
                        break;
                    case 80:
                        methodName = "nameRef";
                        signature = "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Lorg/mozilla/javascript/Ref;";
                        this.cfw.addALoad(this.variableObjectLocal);
                        break;
                    default:
                        throw Kit.codeBug();
                }
                this.cfw.addPush(memberTypeFlags);
                addScriptRuntimeInvoke(methodName, signature);
                return;
            case 89:
                for (Node next = child.getNext(); next != null; next = next.getNext()) {
                    generateExpression(child, node);
                    this.cfw.add(87);
                    child = next;
                }
                generateExpression(child, node);
                return;
            case 102:
                Node ifThen = child.getNext();
                Node ifElse = ifThen.getNext();
                generateExpression(child, node);
                addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                int elseTarget = this.cfw.acquireLabel();
                this.cfw.add(153, elseTarget);
                short stack = this.cfw.getStackTop();
                generateExpression(ifThen, node);
                int afterHook = this.cfw.acquireLabel();
                this.cfw.add(167, afterHook);
                this.cfw.markLabel(elseTarget, stack);
                generateExpression(ifElse, node);
                this.cfw.markLabel(afterHook);
                return;
            case 104:
            case 105:
                generateExpression(child, node);
                this.cfw.add(89);
                addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                falseTarget = this.cfw.acquireLabel();
                if (type == 105) {
                    this.cfw.add(153, falseTarget);
                } else {
                    this.cfw.add(154, falseTarget);
                }
                this.cfw.add(87);
                generateExpression(child.getNext(), node);
                this.cfw.markLabel(falseTarget);
                return;
            case 106:
            case 107:
                visitIncDec(node);
                return;
            case 109:
                if (this.fnCurrent != null || parent.getType() != 136) {
                    OptFunctionNode ofn = OptFunctionNode.get(this.scriptOrFn, node.getExistingIntProp(1));
                    int t = ofn.fnode.getFunctionType();
                    if (t != 2) {
                        throw Codegen.badTree();
                    }
                    visitFunction(ofn, t);
                    return;
                }
                return;
            case 126:
                generateExpression(child, node);
                this.cfw.add(87);
                Codegen.pushUndefined(this.cfw);
                return;
            case 137:
                visitTypeofname(node);
                return;
            case 138:
                return;
            case 146:
                visitDotQuery(node, child);
                return;
            case 149:
                int prop = -1;
                if (child.getType() == 40) {
                    prop = child.getIntProp(8, -1);
                }
                if (prop != -1) {
                    child.removeProp(8);
                    generateExpression(child, node);
                    child.putIntProp(8, prop);
                    return;
                }
                generateExpression(child, node);
                addDoubleWrap();
                return;
            case 150:
                generateExpression(child, node);
                addObjectToDouble();
                return;
            case 155:
                visitSetConst(node, child);
                return;
            case 156:
                visitSetConstVar(node, child, true);
                return;
            case 157:
                Node initStmt = child;
                Node expr = child.getNext();
                generateStatement(initStmt);
                generateExpression(expr, node);
                return;
            case 159:
                Node enterWith = child;
                Node with = enterWith.getNext();
                Node leaveWith = with.getNext();
                generateStatement(enterWith);
                generateExpression(with.getFirstChild(), with);
                generateStatement(leaveWith);
                return;
            default:
                throw new RuntimeException("Unexpected node type " + type);
        }
        while (child != null) {
            generateExpression(child, node);
            child = child.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(node.getString());
        addScriptRuntimeInvoke("bind", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Scriptable;");
    }

    private void generateYieldPoint(Node node, boolean exprContext) {
        int i;
        int i2;
        int top = this.cfw.getStackTop();
        if (this.maxStack > top) {
            i = this.maxStack;
        } else {
            i = top;
        }
        this.maxStack = i;
        if (this.cfw.getStackTop() != (short) 0) {
            generateGetGeneratorStackState();
            for (i2 = 0; i2 < top; i2++) {
                this.cfw.add(90);
                this.cfw.add(95);
                this.cfw.addLoadConstant(i2);
                this.cfw.add(95);
                this.cfw.add(83);
            }
            this.cfw.add(87);
        }
        Node child = node.getFirstChild();
        if (child != null) {
            generateExpression(child, node);
        } else {
            Codegen.pushUndefined(this.cfw);
        }
        int nextState = getNextGeneratorState(node);
        generateSetGeneratorResumptionPoint(nextState);
        boolean hasLocals = generateSaveLocals(node);
        this.cfw.add(176);
        generateCheckForThrowOrClose(getTargetLabel(node), hasLocals, nextState);
        if (top != 0) {
            generateGetGeneratorStackState();
            for (i2 = 0; i2 < top; i2++) {
                this.cfw.add(89);
                this.cfw.addLoadConstant((top - i2) - 1);
                this.cfw.add(50);
                this.cfw.add(95);
            }
            this.cfw.add(87);
        }
        if (exprContext) {
            this.cfw.addALoad(this.argsLocal);
        }
    }

    private void generateCheckForThrowOrClose(int label, boolean hasLocals, int nextState) {
        int throwLabel = this.cfw.acquireLabel();
        int closeLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(throwLabel);
        this.cfw.addALoad(this.argsLocal);
        generateThrowJavaScriptException();
        this.cfw.markLabel(closeLabel);
        this.cfw.addALoad(this.argsLocal);
        this.cfw.add(192, "java/lang/Throwable");
        this.cfw.add(191);
        if (label != -1) {
            this.cfw.markLabel(label);
        }
        if (!hasLocals) {
            this.cfw.markTableSwitchCase(this.generatorSwitch, nextState);
        }
        this.cfw.addILoad(this.operationLocal);
        this.cfw.addLoadConstant(2);
        this.cfw.add(159, closeLabel);
        this.cfw.addILoad(this.operationLocal);
        this.cfw.addLoadConstant(1);
        this.cfw.add(159, throwLabel);
    }

    private void generateIfJump(Node node, Node parent, int trueLabel, int falseLabel) {
        int type = node.getType();
        Node child = node.getFirstChild();
        switch (type) {
            case 12:
            case 13:
            case 46:
            case 47:
                visitIfJumpEqOp(node, child, trueLabel, falseLabel);
                return;
            case 14:
            case 15:
            case 16:
            case 17:
            case 52:
            case 53:
                visitIfJumpRelOp(node, child, trueLabel, falseLabel);
                return;
            case 26:
                generateIfJump(child, node, falseLabel, trueLabel);
                return;
            case 104:
            case 105:
                int interLabel = this.cfw.acquireLabel();
                if (type == 105) {
                    generateIfJump(child, node, interLabel, falseLabel);
                } else {
                    generateIfJump(child, node, trueLabel, interLabel);
                }
                this.cfw.markLabel(interLabel);
                generateIfJump(child.getNext(), node, trueLabel, falseLabel);
                return;
            default:
                generateExpression(node, parent);
                addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
                this.cfw.add(154, trueLabel);
                this.cfw.add(167, falseLabel);
                return;
        }
    }

    private void visitFunction(OptFunctionNode ofn, int functionType) {
        int fnIndex = this.codegen.getIndex(ofn.fnode);
        this.cfw.add(187, this.codegen.mainClassName);
        this.cfw.add(89);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(fnIndex);
        this.cfw.addInvoke(183, this.codegen.mainClassName, "<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;I)V");
        if (functionType != 2) {
            this.cfw.addPush(functionType);
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.contextLocal);
            addOptRuntimeInvoke("initFunction", "(Lorg/mozilla/javascript/NativeFunction;ILorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;)V");
        }
    }

    private int getTargetLabel(Node target) {
        int labelId = target.labelId();
        if (labelId != -1) {
            return labelId;
        }
        labelId = this.cfw.acquireLabel();
        target.labelId(labelId);
        return labelId;
    }

    private void visitGoto(Jump node, int type, Node child) {
        Node target = node.target;
        if (type == 6 || type == 7) {
            if (child == null) {
                throw Codegen.badTree();
            }
            int targetLabel = getTargetLabel(target);
            int fallThruLabel = this.cfw.acquireLabel();
            if (type == 6) {
                generateIfJump(child, node, targetLabel, fallThruLabel);
            } else {
                generateIfJump(child, node, fallThruLabel, targetLabel);
            }
            this.cfw.markLabel(fallThruLabel);
        } else if (type != 135) {
            addGoto(target, 167);
        } else if (this.isGenerator) {
            addGotoWithReturn(target);
        } else {
            inlineFinally(target);
        }
    }

    private void addGotoWithReturn(Node target) {
        FinallyReturnPoint ret = (FinallyReturnPoint) this.finallys.get(target);
        this.cfw.addLoadConstant(ret.jsrPoints.size());
        addGoto(target, 167);
        int retLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(retLabel);
        ret.jsrPoints.add(Integer.valueOf(retLabel));
    }

    private void generateArrayLiteralFactory(Node node, int count) {
        String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + count;
        initBodyGeneration();
        short s = this.firstFreeLocal;
        this.firstFreeLocal = (short) (s + 1);
        this.argsLocal = s;
        this.localsMax = this.firstFreeLocal;
        this.cfw.startMethod(methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short) 2);
        visitArrayLiteral(node, node.getFirstChild(), true);
        this.cfw.add(176);
        this.cfw.stopMethod((short) (this.localsMax + 1));
    }

    private void generateObjectLiteralFactory(Node node, int count) {
        String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + count;
        initBodyGeneration();
        short s = this.firstFreeLocal;
        this.firstFreeLocal = (short) (s + 1);
        this.argsLocal = s;
        this.localsMax = this.firstFreeLocal;
        this.cfw.startMethod(methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;", (short) 2);
        visitObjectLiteral(node, node.getFirstChild(), true);
        this.cfw.add(176);
        this.cfw.stopMethod((short) (this.localsMax + 1));
    }

    private void visitArrayLiteral(Node node, Node child, boolean topLevel) {
        int count = 0;
        for (Node cursor = child; cursor != null; cursor = cursor.getNext()) {
            count++;
        }
        if (topLevel || ((count <= 10 && this.cfw.getCurrentCodeOffset() <= 30000) || this.hasVarsInRegs || this.isGenerator || this.inLocalBlock)) {
            int i;
            if (this.isGenerator) {
                for (i = 0; i != count; i++) {
                    generateExpression(child, node);
                    child = child.getNext();
                }
                addNewObjectArray(count);
                for (i = 0; i != count; i++) {
                    this.cfw.add(90);
                    this.cfw.add(95);
                    this.cfw.addPush((count - i) - 1);
                    this.cfw.add(95);
                    this.cfw.add(83);
                }
            } else {
                addNewObjectArray(count);
                for (i = 0; i != count; i++) {
                    this.cfw.add(89);
                    this.cfw.addPush(i);
                    generateExpression(child, node);
                    this.cfw.add(83);
                    child = child.getNext();
                }
            }
            int[] skipIndexes = (int[]) node.getProp(11);
            if (skipIndexes == null) {
                this.cfw.add(1);
                this.cfw.add(3);
            } else {
                this.cfw.addPush(OptRuntime.encodeIntArray(skipIndexes));
                this.cfw.addPush(skipIndexes.length);
            }
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            addOptRuntimeInvoke("newArrayLiteral", "([Ljava/lang/Object;Ljava/lang/String;ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
            return;
        }
        if (this.literals == null) {
            this.literals = new LinkedList();
        }
        this.literals.add(node);
        String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + this.literals.size();
        this.cfw.addALoad(this.funObjLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.thisObjLocal);
        this.cfw.addALoad(this.argsLocal);
        this.cfw.addInvoke(182, this.codegen.mainClassName, methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
    }

    private void addLoadPropertyIds(Object[] properties, int count) {
        addNewObjectArray(count);
        for (int i = 0; i != count; i++) {
            this.cfw.add(89);
            this.cfw.addPush(i);
            Object id = properties[i];
            if (id instanceof String) {
                this.cfw.addPush((String) id);
            } else {
                this.cfw.addPush(((Integer) id).intValue());
                addScriptRuntimeInvoke("wrapInt", "(I)Ljava/lang/Integer;");
            }
            this.cfw.add(83);
        }
    }

    private void addLoadPropertyValues(Node node, Node child, int count) {
        int i;
        int childType;
        if (this.isGenerator) {
            for (i = 0; i != count; i++) {
                childType = child.getType();
                if (childType == 151 || childType == 152) {
                    generateExpression(child.getFirstChild(), node);
                } else {
                    generateExpression(child, node);
                }
                child = child.getNext();
            }
            addNewObjectArray(count);
            for (i = 0; i != count; i++) {
                this.cfw.add(90);
                this.cfw.add(95);
                this.cfw.addPush((count - i) - 1);
                this.cfw.add(95);
                this.cfw.add(83);
            }
            return;
        }
        addNewObjectArray(count);
        Node child2 = child;
        for (i = 0; i != count; i++) {
            this.cfw.add(89);
            this.cfw.addPush(i);
            childType = child2.getType();
            if (childType == 151 || childType == 152) {
                generateExpression(child2.getFirstChild(), node);
            } else {
                generateExpression(child2, node);
            }
            this.cfw.add(83);
            child2 = child2.getNext();
        }
    }

    private void visitObjectLiteral(Node node, Node child, boolean topLevel) {
        Object[] properties = (Object[]) node.getProp(12);
        int count = properties.length;
        if (topLevel || ((count <= 10 && this.cfw.getCurrentCodeOffset() <= 30000) || this.hasVarsInRegs || this.isGenerator || this.inLocalBlock)) {
            int childType;
            if (this.isGenerator) {
                addLoadPropertyValues(node, child, count);
                addLoadPropertyIds(properties, count);
                this.cfw.add(95);
            } else {
                addLoadPropertyIds(properties, count);
                addLoadPropertyValues(node, child, count);
            }
            boolean hasGetterSetters = false;
            Node child2 = child;
            int i = 0;
            while (i != count) {
                childType = child2.getType();
                if (childType == 151 || childType == 152) {
                    hasGetterSetters = true;
                    break;
                } else {
                    child2 = child2.getNext();
                    i++;
                }
            }
            if (hasGetterSetters) {
                this.cfw.addPush(count);
                this.cfw.add(188, 10);
                child2 = child;
                for (i = 0; i != count; i++) {
                    this.cfw.add(89);
                    this.cfw.addPush(i);
                    childType = child2.getType();
                    if (childType == 151) {
                        this.cfw.add(2);
                    } else if (childType == 152) {
                        this.cfw.add(4);
                    } else {
                        this.cfw.add(3);
                    }
                    this.cfw.add(79);
                    child2 = child2.getNext();
                }
            } else {
                this.cfw.add(1);
            }
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            addScriptRuntimeInvoke("newObjectLiteral", "([Ljava/lang/Object;[Ljava/lang/Object;[ILorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
            return;
        }
        if (this.literals == null) {
            this.literals = new LinkedList();
        }
        this.literals.add(node);
        String methodName = this.codegen.getBodyMethodName(this.scriptOrFn) + "_literal" + this.literals.size();
        this.cfw.addALoad(this.funObjLocal);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addALoad(this.thisObjLocal);
        this.cfw.addALoad(this.argsLocal);
        this.cfw.addInvoke(182, this.codegen.mainClassName, methodName, "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
    }

    private void visitSpecialCall(Node node, int type, int specialType, Node child) {
        String methodName;
        String callSignature;
        this.cfw.addALoad(this.contextLocal);
        if (type == 30) {
            generateExpression(child, node);
        } else {
            generateFunctionAndThisObj(child, node);
        }
        generateCallArgArray(node, child.getNext(), false);
        if (type == 30) {
            methodName = "newObjectSpecial";
            callSignature = "(Lorg/mozilla/javascript/Context;Ljava/lang/Object;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;";
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addPush(specialType);
        } else {
            methodName = "callSpecial";
            callSignature = "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;ILjava/lang/String;I)Ljava/lang/Object;";
            this.cfw.addALoad(this.variableObjectLocal);
            this.cfw.addALoad(this.thisObjLocal);
            this.cfw.addPush(specialType);
            String sourceName = this.scriptOrFn.getSourceName();
            ClassFileWriter classFileWriter = this.cfw;
            if (sourceName == null) {
                sourceName = "";
            }
            classFileWriter.addPush(sourceName);
            this.cfw.addPush(this.itsLineNumber);
        }
        addOptRuntimeInvoke(methodName, callSignature);
    }

    private void visitStandardCall(Node node, Node child) {
        if (node.getType() != 38) {
            throw Codegen.badTree();
        }
        String methodName;
        String signature;
        Node firstArgChild = child.getNext();
        int childType = child.getType();
        if (firstArgChild == null) {
            if (childType == 39) {
                this.cfw.addPush(child.getString());
                methodName = "callName0";
                signature = "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            } else if (childType == 33) {
                Node propTarget = child.getFirstChild();
                generateExpression(propTarget, node);
                this.cfw.addPush(propTarget.getNext().getString());
                methodName = "callProp0";
                signature = "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            } else if (childType == 34) {
                throw Kit.codeBug();
            } else {
                generateFunctionAndThisObj(child, node);
                methodName = "call0";
                signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
        } else if (childType == 39) {
            String name = child.getString();
            generateCallArgArray(node, firstArgChild, false);
            this.cfw.addPush(name);
            methodName = "callName";
            signature = "([Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
        } else {
            int argCount = 0;
            for (Node arg = firstArgChild; arg != null; arg = arg.getNext()) {
                argCount++;
            }
            generateFunctionAndThisObj(child, node);
            if (argCount == 1) {
                generateExpression(firstArgChild, node);
                methodName = "call1";
                signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            } else if (argCount == 2) {
                generateExpression(firstArgChild, node);
                generateExpression(firstArgChild.getNext(), node);
                methodName = "call2";
                signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            } else {
                generateCallArgArray(node, firstArgChild, false);
                methodName = "callN";
                signature = "(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;";
            }
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        addOptRuntimeInvoke(methodName, signature);
    }

    private void visitStandardNew(Node node, Node child) {
        if (node.getType() != 30) {
            throw Codegen.badTree();
        }
        Node firstArgChild = child.getNext();
        generateExpression(child, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        generateCallArgArray(node, firstArgChild, false);
        addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
    }

    private void visitOptimizedCall(Node node, OptFunctionNode target, int type, Node child) {
        String directCtorName;
        Node firstArgChild = child.getNext();
        String className = this.codegen.mainClassName;
        short thisObjLocal = (short) 0;
        if (type == 30) {
            generateExpression(child, node);
        } else {
            generateFunctionAndThisObj(child, node);
            thisObjLocal = getNewWordLocal();
            this.cfw.addAStore(thisObjLocal);
        }
        int beyond = this.cfw.acquireLabel();
        int regularCall = this.cfw.acquireLabel();
        this.cfw.add(89);
        this.cfw.add(193, className);
        this.cfw.add(153, regularCall);
        this.cfw.add(192, className);
        this.cfw.add(89);
        this.cfw.add(180, className, "_id", "I");
        this.cfw.addPush(this.codegen.getIndex(target.fnode));
        this.cfw.add(160, regularCall);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (type == 30) {
            this.cfw.add(1);
        } else {
            this.cfw.addALoad(thisObjLocal);
        }
        for (Node argChild = firstArgChild; argChild != null; argChild = argChild.getNext()) {
            int dcp_register = nodeIsDirectCallParameter(argChild);
            if (dcp_register >= 0) {
                this.cfw.addALoad(dcp_register);
                this.cfw.addDLoad(dcp_register + 1);
            } else if (argChild.getIntProp(8, -1) == 0) {
                this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                generateExpression(argChild, node);
            } else {
                generateExpression(argChild, node);
                this.cfw.addPush(0.0d);
            }
        }
        this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
        ClassFileWriter classFileWriter = this.cfw;
        String str = this.codegen.mainClassName;
        if (type == 30) {
            directCtorName = this.codegen.getDirectCtorName(target.fnode);
        } else {
            directCtorName = this.codegen.getBodyMethodName(target.fnode);
        }
        classFileWriter.addInvoke(184, str, directCtorName, this.codegen.getBodyMethodSignature(target.fnode));
        this.cfw.add(167, beyond);
        this.cfw.markLabel(regularCall);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (type != 30) {
            this.cfw.addALoad(thisObjLocal);
            releaseWordLocal(thisObjLocal);
        }
        generateCallArgArray(node, firstArgChild, true);
        if (type == 30) {
            addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        } else {
            this.cfw.addInvoke(185, "org/mozilla/javascript/Callable", "call", "(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        }
        this.cfw.markLabel(beyond);
    }

    private void generateCallArgArray(Node node, Node argChild, boolean directCall) {
        int argCount = 0;
        for (Node child = argChild; child != null; child = child.getNext()) {
            argCount++;
        }
        if (argCount != 1 || this.itsOneArgArray < (short) 0) {
            addNewObjectArray(argCount);
        } else {
            this.cfw.addALoad(this.itsOneArgArray);
        }
        for (int i = 0; i != argCount; i++) {
            if (!this.isGenerator) {
                this.cfw.add(89);
                this.cfw.addPush(i);
            }
            if (directCall) {
                int dcp_register = nodeIsDirectCallParameter(argChild);
                if (dcp_register >= 0) {
                    dcpLoadAsObject(dcp_register);
                } else {
                    generateExpression(argChild, node);
                    if (argChild.getIntProp(8, -1) == 0) {
                        addDoubleWrap();
                    }
                }
            } else {
                generateExpression(argChild, node);
            }
            if (this.isGenerator) {
                short tempLocal = getNewWordLocal();
                this.cfw.addAStore(tempLocal);
                this.cfw.add(192, "[Ljava/lang/Object;");
                this.cfw.add(89);
                this.cfw.addPush(i);
                this.cfw.addALoad(tempLocal);
                releaseWordLocal(tempLocal);
            }
            this.cfw.add(83);
            argChild = argChild.getNext();
        }
    }

    private void generateFunctionAndThisObj(Node node, Node parent) {
        int type = node.getType();
        switch (node.getType()) {
            case 33:
            case 36:
                Node target = node.getFirstChild();
                generateExpression(target, node);
                Node id = target.getNext();
                if (type != 33) {
                    generateExpression(id, node);
                    if (node.getIntProp(8, -1) != -1) {
                        addDoubleWrap();
                    }
                    this.cfw.addALoad(this.contextLocal);
                    this.cfw.addALoad(this.variableObjectLocal);
                    addScriptRuntimeInvoke("getElemFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
                    break;
                }
                this.cfw.addPush(id.getString());
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getPropFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
                break;
            case 34:
                throw Kit.codeBug();
            case 39:
                this.cfw.addPush(node.getString());
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getNameFunctionAndThis", "(Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;");
                break;
            default:
                generateExpression(node, parent);
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("getValueFunctionAndThis", "(Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Callable;");
                break;
        }
        this.cfw.addALoad(this.contextLocal);
        addScriptRuntimeInvoke("lastStoredScriptable", "(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;");
    }

    private void updateLineNumber(Node node) {
        this.itsLineNumber = node.getLineno();
        if (this.itsLineNumber != -1) {
            this.cfw.addLineNumberEntry((short) this.itsLineNumber);
        }
    }

    private void visitTryCatchFinally(Jump node, Node child) {
        Context cx;
        int catchLabel;
        short savedVariableObject = getNewWordLocal();
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addAStore(savedVariableObject);
        int startLabel = this.cfw.acquireLabel();
        this.cfw.markLabel(startLabel, (short) 0);
        Node catchTarget = node.target;
        Node finallyTarget = node.getFinally();
        int[] handlerLabels = new int[5];
        this.exceptionManager.pushExceptionInfo(node);
        if (catchTarget != null) {
            handlerLabels[0] = this.cfw.acquireLabel();
            handlerLabels[1] = this.cfw.acquireLabel();
            handlerLabels[2] = this.cfw.acquireLabel();
            cx = Context.getCurrentContext();
            if (cx != null && cx.hasFeature(13)) {
                handlerLabels[3] = this.cfw.acquireLabel();
            }
        }
        if (finallyTarget != null) {
            handlerLabels[4] = this.cfw.acquireLabel();
        }
        this.exceptionManager.setHandlers(handlerLabels, startLabel);
        if (this.isGenerator && finallyTarget != null) {
            FinallyReturnPoint ret = new FinallyReturnPoint();
            if (this.finallys == null) {
                this.finallys = new HashMap();
            }
            this.finallys.put(finallyTarget, ret);
            this.finallys.put(finallyTarget.getNext(), ret);
        }
        while (child != null) {
            if (child == catchTarget) {
                catchLabel = getTargetLabel(catchTarget);
                this.exceptionManager.removeHandler(0, catchLabel);
                this.exceptionManager.removeHandler(1, catchLabel);
                this.exceptionManager.removeHandler(2, catchLabel);
                this.exceptionManager.removeHandler(3, catchLabel);
            }
            generateStatement(child);
            child = child.getNext();
        }
        int realEnd = this.cfw.acquireLabel();
        this.cfw.add(167, realEnd);
        int exceptionLocal = getLocalBlockRegister(node);
        if (catchTarget != null) {
            catchLabel = catchTarget.labelId();
            generateCatchBlock(0, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[0]);
            generateCatchBlock(1, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[1]);
            generateCatchBlock(2, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[2]);
            cx = Context.getCurrentContext();
            if (cx != null && cx.hasFeature(13)) {
                generateCatchBlock(3, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[3]);
            }
        }
        if (finallyTarget != null) {
            int finallyHandler = this.cfw.acquireLabel();
            int finallyEnd = this.cfw.acquireLabel();
            this.cfw.markHandler(finallyHandler);
            if (!this.isGenerator) {
                this.cfw.markLabel(handlerLabels[4]);
            }
            this.cfw.addAStore(exceptionLocal);
            this.cfw.addALoad(savedVariableObject);
            this.cfw.addAStore(this.variableObjectLocal);
            int finallyLabel = finallyTarget.labelId();
            if (this.isGenerator) {
                addGotoWithReturn(finallyTarget);
            } else {
                inlineFinally(finallyTarget, handlerLabels[4], finallyEnd);
            }
            this.cfw.addALoad(exceptionLocal);
            if (this.isGenerator) {
                this.cfw.add(192, "java/lang/Throwable");
            }
            this.cfw.add(191);
            this.cfw.markLabel(finallyEnd);
            if (this.isGenerator) {
                this.cfw.addExceptionHandler(startLabel, finallyLabel, finallyHandler, null);
            }
        }
        releaseWordLocal(savedVariableObject);
        this.cfw.markLabel(realEnd);
        if (!this.isGenerator) {
            this.exceptionManager.popExceptionInfo();
        }
    }

    private void generateCatchBlock(int exceptionType, short savedVariableObject, int catchLabel, int exceptionLocal, int handler) {
        if (handler == 0) {
            handler = this.cfw.acquireLabel();
        }
        this.cfw.markHandler(handler);
        this.cfw.addAStore(exceptionLocal);
        this.cfw.addALoad(savedVariableObject);
        this.cfw.addAStore(this.variableObjectLocal);
        String exceptionName = exceptionTypeToName(exceptionType);
        this.cfw.add(167, catchLabel);
    }

    /* access modifiers changed from: private */
    public String exceptionTypeToName(int exceptionType) {
        if (exceptionType == 0) {
            return "org/mozilla/javascript/JavaScriptException";
        }
        if (exceptionType == 1) {
            return "org/mozilla/javascript/EvaluatorException";
        }
        if (exceptionType == 2) {
            return "org/mozilla/javascript/EcmaError";
        }
        if (exceptionType == 3) {
            return "java/lang/Throwable";
        }
        if (exceptionType == 4) {
            return null;
        }
        throw Kit.codeBug();
    }

    private void inlineFinally(Node finallyTarget, int finallyStart, int finallyEnd) {
        Node fBlock = getFinallyAtTarget(finallyTarget);
        fBlock.resetTargets();
        this.exceptionManager.markInlineFinallyStart(fBlock, finallyStart);
        for (Node child = fBlock.getFirstChild(); child != null; child = child.getNext()) {
            generateStatement(child);
        }
        this.exceptionManager.markInlineFinallyEnd(fBlock, finallyEnd);
    }

    private void inlineFinally(Node finallyTarget) {
        int finallyStart = this.cfw.acquireLabel();
        int finallyEnd = this.cfw.acquireLabel();
        this.cfw.markLabel(finallyStart);
        inlineFinally(finallyTarget, finallyStart, finallyEnd);
        this.cfw.markLabel(finallyEnd);
    }

    /* access modifiers changed from: private */
    public Node getFinallyAtTarget(Node node) {
        if (node == null) {
            return null;
        }
        if (node.getType() == 125) {
            return node;
        }
        if (node != null && node.getType() == 131) {
            Node fBlock = node.getNext();
            if (fBlock != null && fBlock.getType() == 125) {
                return fBlock;
            }
        }
        throw Kit.codeBug("bad finally target");
    }

    private boolean generateSaveLocals(Node node) {
        short i;
        int count = 0;
        for (i = (short) 0; i < this.firstFreeLocal; i++) {
            if (this.locals[i] != 0) {
                count++;
            }
        }
        if (count == 0) {
            ((FunctionNode) this.scriptOrFn).addLiveLocals(node, null);
            return false;
        }
        int i2;
        if (this.maxLocals > count) {
            i2 = this.maxLocals;
        } else {
            i2 = count;
        }
        this.maxLocals = i2;
        int[] ls = new int[count];
        int s = 0;
        for (i = (short) 0; i < this.firstFreeLocal; i++) {
            if (this.locals[i] != 0) {
                ls[s] = i;
                s++;
            }
        }
        ((FunctionNode) this.scriptOrFn).addLiveLocals(node, ls);
        generateGetGeneratorLocalsState();
        for (int i3 = 0; i3 < count; i3++) {
            this.cfw.add(89);
            this.cfw.addLoadConstant(i3);
            this.cfw.addALoad(ls[i3]);
            this.cfw.add(83);
        }
        this.cfw.add(87);
        return true;
    }

    private void visitSwitch(Jump switchNode, Node child) {
        generateExpression(child, switchNode);
        short selector = getNewWordLocal();
        this.cfw.addAStore(selector);
        for (Jump caseNode = (Jump) child.getNext(); caseNode != null; caseNode = (Jump) caseNode.getNext()) {
            if (caseNode.getType() != 115) {
                throw Codegen.badTree();
            }
            generateExpression(caseNode.getFirstChild(), caseNode);
            this.cfw.addALoad(selector);
            addScriptRuntimeInvoke("shallowEq", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
            addGoto(caseNode.target, 154);
        }
        releaseWordLocal(selector);
    }

    private void visitTypeofname(Node node) {
        if (this.hasVarsInRegs) {
            int varIndex = this.fnCurrent.fnode.getIndexForNameNode(node);
            if (varIndex >= 0) {
                if (this.fnCurrent.isNumberVar(varIndex)) {
                    this.cfw.addPush("number");
                    return;
                } else if (varIsDirectCallParameter(varIndex)) {
                    int dcp_register = this.varRegisters[varIndex];
                    this.cfw.addALoad(dcp_register);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    int isNumberLabel = this.cfw.acquireLabel();
                    this.cfw.add(165, isNumberLabel);
                    short stack = this.cfw.getStackTop();
                    this.cfw.addALoad(dcp_register);
                    addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                    int beyond = this.cfw.acquireLabel();
                    this.cfw.add(167, beyond);
                    this.cfw.markLabel(isNumberLabel, stack);
                    this.cfw.addPush("number");
                    this.cfw.markLabel(beyond);
                    return;
                } else {
                    this.cfw.addALoad(this.varRegisters[varIndex]);
                    addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
                    return;
                }
            }
        }
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(node.getString());
        addScriptRuntimeInvoke("typeofName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/String;");
    }

    private void saveCurrentCodeOffset() {
        this.savedCodeOffset = this.cfw.getCurrentCodeOffset();
    }

    private void addInstructionCount() {
        addInstructionCount(Math.max(this.cfw.getCurrentCodeOffset() - this.savedCodeOffset, 1));
    }

    private void addInstructionCount(int count) {
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(count);
        addScriptRuntimeInvoke("addInstructionCount", "(Lorg/mozilla/javascript/Context;I)V");
    }

    private void visitIncDec(Node node) {
        int incrDecrMask = node.getExistingIntProp(13);
        Node child = node.getFirstChild();
        switch (child.getType()) {
            case 33:
                Node getPropChild = child.getFirstChild();
                generateExpression(getPropChild, node);
                generateExpression(getPropChild.getNext(), node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(incrDecrMask);
                addScriptRuntimeInvoke("propIncrDecr", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                return;
            case 34:
                throw Kit.codeBug();
            case 36:
                Node elemChild = child.getFirstChild();
                generateExpression(elemChild, node);
                generateExpression(elemChild.getNext(), node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(incrDecrMask);
                if (elemChild.getNext().getIntProp(8, -1) != -1) {
                    addOptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                    return;
                } else {
                    addScriptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                    return;
                }
            case 39:
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(child.getString());
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addPush(incrDecrMask);
                addScriptRuntimeInvoke("nameIncrDecr", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;I)Ljava/lang/Object;");
                return;
            case 55:
                if (!this.hasVarsInRegs) {
                    Kit.codeBug();
                }
                boolean post = (incrDecrMask & 2) != 0;
                int varIndex = this.fnCurrent.getVarIndex(child);
                short reg = this.varRegisters[varIndex];
                if (this.fnCurrent.fnode.getParamAndVarConst()[varIndex]) {
                    if (node.getIntProp(8, -1) != -1) {
                        this.cfw.addDLoad(reg + (varIsDirectCallParameter(varIndex) ? 1 : 0));
                        if (!post) {
                            this.cfw.addPush(1.0d);
                            if ((incrDecrMask & 1) == 0) {
                                this.cfw.add(99);
                                return;
                            } else {
                                this.cfw.add(103);
                                return;
                            }
                        }
                        return;
                    }
                    if (varIsDirectCallParameter(varIndex)) {
                        dcpLoadAsObject(reg);
                    } else {
                        this.cfw.addALoad(reg);
                    }
                    if (post) {
                        this.cfw.add(89);
                        addObjectToDouble();
                        this.cfw.add(88);
                        return;
                    }
                    addObjectToDouble();
                    this.cfw.addPush(1.0d);
                    if ((incrDecrMask & 1) == 0) {
                        this.cfw.add(99);
                    } else {
                        this.cfw.add(103);
                    }
                    addDoubleWrap();
                    return;
                } else if (node.getIntProp(8, -1) != -1) {
                    int offset = varIsDirectCallParameter(varIndex) ? 1 : 0;
                    this.cfw.addDLoad(reg + offset);
                    if (post) {
                        this.cfw.add(92);
                    }
                    this.cfw.addPush(1.0d);
                    if ((incrDecrMask & 1) == 0) {
                        this.cfw.add(99);
                    } else {
                        this.cfw.add(103);
                    }
                    if (!post) {
                        this.cfw.add(92);
                    }
                    this.cfw.addDStore(reg + offset);
                    return;
                } else {
                    if (varIsDirectCallParameter(varIndex)) {
                        dcpLoadAsObject(reg);
                    } else {
                        this.cfw.addALoad(reg);
                    }
                    if (post) {
                        this.cfw.add(89);
                    }
                    addObjectToDouble();
                    this.cfw.addPush(1.0d);
                    if ((incrDecrMask & 1) == 0) {
                        this.cfw.add(99);
                    } else {
                        this.cfw.add(103);
                    }
                    addDoubleWrap();
                    if (!post) {
                        this.cfw.add(89);
                    }
                    this.cfw.addAStore(reg);
                    return;
                }
            case 67:
                generateExpression(child.getFirstChild(), node);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                this.cfw.addPush(incrDecrMask);
                addScriptRuntimeInvoke("refIncrDecr", "(Lorg/mozilla/javascript/Ref;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;I)Ljava/lang/Object;");
                return;
            default:
                Codegen.badTree();
                return;
        }
    }

    private static boolean isArithmeticNode(Node node) {
        int type = node.getType();
        return type == 22 || type == 25 || type == 24 || type == 23;
    }

    private void visitArithmetic(Node node, int opCode, Node child, Node parent) {
        if (node.getIntProp(8, -1) != -1) {
            generateExpression(child, node);
            generateExpression(child.getNext(), node);
            this.cfw.add(opCode);
            return;
        }
        boolean childOfArithmetic = isArithmeticNode(parent);
        generateExpression(child, node);
        if (!isArithmeticNode(child)) {
            addObjectToDouble();
        }
        generateExpression(child.getNext(), node);
        if (!isArithmeticNode(child.getNext())) {
            addObjectToDouble();
        }
        this.cfw.add(opCode);
        if (!childOfArithmetic) {
            addDoubleWrap();
        }
    }

    private void visitBitOp(Node node, int type, Node child) {
        int childNumberFlag = node.getIntProp(8, -1);
        generateExpression(child, node);
        if (type == 20) {
            addScriptRuntimeInvoke("toUint32", "(Ljava/lang/Object;)J");
            generateExpression(child.getNext(), node);
            addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
            this.cfw.addPush(31);
            this.cfw.add(126);
            this.cfw.add(125);
            this.cfw.add(138);
            addDoubleWrap();
            return;
        }
        if (childNumberFlag == -1) {
            addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
            generateExpression(child.getNext(), node);
            addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
        } else {
            addScriptRuntimeInvoke("toInt32", "(D)I");
            generateExpression(child.getNext(), node);
            addScriptRuntimeInvoke("toInt32", "(D)I");
        }
        switch (type) {
            case 9:
                this.cfw.add(128);
                break;
            case 10:
                this.cfw.add(130);
                break;
            case 11:
                this.cfw.add(126);
                break;
            case 18:
                this.cfw.add(120);
                break;
            case 19:
                this.cfw.add(122);
                break;
            default:
                throw Codegen.badTree();
        }
        this.cfw.add(135);
        if (childNumberFlag == -1) {
            addDoubleWrap();
        }
    }

    private int nodeIsDirectCallParameter(Node node) {
        if (node.getType() == 55 && this.inDirectCallFunction && !this.itsForcedObjectParameters) {
            int varIndex = this.fnCurrent.getVarIndex(node);
            if (this.fnCurrent.isParameter(varIndex)) {
                return this.varRegisters[varIndex];
            }
        }
        return -1;
    }

    private boolean varIsDirectCallParameter(int varIndex) {
        return this.fnCurrent.isParameter(varIndex) && this.inDirectCallFunction && !this.itsForcedObjectParameters;
    }

    private void genSimpleCompare(int type, int trueGOTO, int falseGOTO) {
        if (trueGOTO == -1) {
            throw Codegen.badTree();
        }
        switch (type) {
            case 14:
                this.cfw.add(152);
                this.cfw.add(155, trueGOTO);
                break;
            case 15:
                this.cfw.add(152);
                this.cfw.add(158, trueGOTO);
                break;
            case 16:
                this.cfw.add(151);
                this.cfw.add(157, trueGOTO);
                break;
            case 17:
                this.cfw.add(151);
                this.cfw.add(156, trueGOTO);
                break;
            default:
                throw Codegen.badTree();
        }
        if (falseGOTO != -1) {
            this.cfw.add(167, falseGOTO);
        }
    }

    private void visitIfJumpRelOp(Node node, Node child, int trueGOTO, int falseGOTO) {
        if (trueGOTO == -1 || falseGOTO == -1) {
            throw Codegen.badTree();
        }
        int type = node.getType();
        Node rChild = child.getNext();
        if (type == 53 || type == 52) {
            generateExpression(child, node);
            generateExpression(rChild, node);
            this.cfw.addALoad(this.contextLocal);
            addScriptRuntimeInvoke(type == 53 ? "instanceOf" : "in", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;)Z");
            this.cfw.add(154, trueGOTO);
            this.cfw.add(167, falseGOTO);
            return;
        }
        int childNumberFlag = node.getIntProp(8, -1);
        int left_dcp_register = nodeIsDirectCallParameter(child);
        int right_dcp_register = nodeIsDirectCallParameter(rChild);
        if (childNumberFlag != -1) {
            if (childNumberFlag != 2) {
                generateExpression(child, node);
            } else if (left_dcp_register != -1) {
                dcpLoadAsNumber(left_dcp_register);
            } else {
                generateExpression(child, node);
                addObjectToDouble();
            }
            if (childNumberFlag != 1) {
                generateExpression(rChild, node);
            } else if (right_dcp_register != -1) {
                dcpLoadAsNumber(right_dcp_register);
            } else {
                generateExpression(rChild, node);
                addObjectToDouble();
            }
            genSimpleCompare(type, trueGOTO, falseGOTO);
            return;
        }
        if (left_dcp_register == -1 || right_dcp_register == -1) {
            generateExpression(child, node);
            generateExpression(rChild, node);
        } else {
            short stack = this.cfw.getStackTop();
            int leftIsNotNumber = this.cfw.acquireLabel();
            this.cfw.addALoad(left_dcp_register);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            this.cfw.add(166, leftIsNotNumber);
            this.cfw.addDLoad(left_dcp_register + 1);
            dcpLoadAsNumber(right_dcp_register);
            genSimpleCompare(type, trueGOTO, falseGOTO);
            if (stack != this.cfw.getStackTop()) {
                throw Codegen.badTree();
            }
            this.cfw.markLabel(leftIsNotNumber);
            int rightIsNotNumber = this.cfw.acquireLabel();
            this.cfw.addALoad(right_dcp_register);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            this.cfw.add(166, rightIsNotNumber);
            this.cfw.addALoad(left_dcp_register);
            addObjectToDouble();
            this.cfw.addDLoad(right_dcp_register + 1);
            genSimpleCompare(type, trueGOTO, falseGOTO);
            if (stack != this.cfw.getStackTop()) {
                throw Codegen.badTree();
            }
            this.cfw.markLabel(rightIsNotNumber);
            this.cfw.addALoad(left_dcp_register);
            this.cfw.addALoad(right_dcp_register);
        }
        if (type == 17 || type == 16) {
            this.cfw.add(95);
        }
        String routine = (type == 14 || type == 16) ? "cmp_LT" : "cmp_LE";
        addScriptRuntimeInvoke(routine, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
        this.cfw.add(154, trueGOTO);
        this.cfw.add(167, falseGOTO);
    }

    private void visitIfJumpEqOp(Node node, Node child, int trueGOTO, int falseGOTO) {
        if (trueGOTO == -1 || falseGOTO == -1) {
            throw Codegen.badTree();
        }
        short stackInitial = this.cfw.getStackTop();
        int type = node.getType();
        Node rChild = child.getNext();
        if (child.getType() == 42 || rChild.getType() == 42) {
            if (child.getType() == 42) {
                child = rChild;
            }
            generateExpression(child, node);
            if (type == 46 || type == 47) {
                this.cfw.add(type == 46 ? 198 : 199, trueGOTO);
            } else {
                if (type != 12) {
                    if (type != 13) {
                        throw Codegen.badTree();
                    }
                    int tmp = trueGOTO;
                    trueGOTO = falseGOTO;
                    falseGOTO = tmp;
                }
                this.cfw.add(89);
                int undefCheckLabel = this.cfw.acquireLabel();
                this.cfw.add(199, undefCheckLabel);
                short stack = this.cfw.getStackTop();
                this.cfw.add(87);
                this.cfw.add(167, trueGOTO);
                this.cfw.markLabel(undefCheckLabel, stack);
                Codegen.pushUndefined(this.cfw);
                this.cfw.add(165, trueGOTO);
            }
            this.cfw.add(167, falseGOTO);
        } else {
            String name;
            int testCode;
            int child_dcp_register = nodeIsDirectCallParameter(child);
            if (child_dcp_register != -1 && rChild.getType() == 149) {
                Node convertChild = rChild.getFirstChild();
                if (convertChild.getType() == 40) {
                    this.cfw.addALoad(child_dcp_register);
                    this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
                    int notNumbersLabel = this.cfw.acquireLabel();
                    this.cfw.add(166, notNumbersLabel);
                    this.cfw.addDLoad(child_dcp_register + 1);
                    this.cfw.addPush(convertChild.getDouble());
                    this.cfw.add(151);
                    if (type == 12) {
                        this.cfw.add(153, trueGOTO);
                    } else {
                        this.cfw.add(154, trueGOTO);
                    }
                    this.cfw.add(167, falseGOTO);
                    this.cfw.markLabel(notNumbersLabel);
                }
            }
            generateExpression(child, node);
            generateExpression(rChild, node);
            switch (type) {
                case 12:
                    name = "eq";
                    testCode = 154;
                    break;
                case 13:
                    name = "eq";
                    testCode = 153;
                    break;
                case 46:
                    name = "shallowEq";
                    testCode = 154;
                    break;
                case 47:
                    name = "shallowEq";
                    testCode = 153;
                    break;
                default:
                    throw Codegen.badTree();
            }
            addScriptRuntimeInvoke(name, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
            this.cfw.add(testCode, trueGOTO);
            this.cfw.add(167, falseGOTO);
        }
        if (stackInitial != this.cfw.getStackTop()) {
            throw Codegen.badTree();
        }
    }

    private void visitSetName(Node node, Node child) {
        String name = node.getFirstChild().getString();
        while (child != null) {
            generateExpression(child, node);
            child = child.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(name);
        addScriptRuntimeInvoke("setName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
    }

    private void visitStrictSetName(Node node, Node child) {
        String name = node.getFirstChild().getString();
        while (child != null) {
            generateExpression(child, node);
            child = child.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        this.cfw.addPush(name);
        addScriptRuntimeInvoke("strictSetName", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
    }

    private void visitSetConst(Node node, Node child) {
        String name = node.getFirstChild().getString();
        while (child != null) {
            generateExpression(child, node);
            child = child.getNext();
        }
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addPush(name);
        addScriptRuntimeInvoke("setConst", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Ljava/lang/String;)Ljava/lang/Object;");
    }

    private void visitGetVar(Node node) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        int varIndex = this.fnCurrent.getVarIndex(node);
        short reg = this.varRegisters[varIndex];
        if (varIsDirectCallParameter(varIndex)) {
            if (node.getIntProp(8, -1) != -1) {
                dcpLoadAsNumber(reg);
            } else {
                dcpLoadAsObject(reg);
            }
        } else if (this.fnCurrent.isNumberVar(varIndex)) {
            this.cfw.addDLoad(reg);
        } else {
            this.cfw.addALoad(reg);
        }
    }

    private void visitSetVar(Node node, Node child, boolean needValue) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        int varIndex = this.fnCurrent.getVarIndex(node);
        generateExpression(child.getNext(), node);
        boolean isNumber = node.getIntProp(8, -1) != -1;
        short reg = this.varRegisters[varIndex];
        if (this.fnCurrent.fnode.getParamAndVarConst()[varIndex]) {
            if (!needValue) {
                if (isNumber) {
                    this.cfw.add(88);
                } else {
                    this.cfw.add(87);
                }
            }
        } else if (!varIsDirectCallParameter(varIndex)) {
            boolean isNumberVar = this.fnCurrent.isNumberVar(varIndex);
            if (!isNumber) {
                if (isNumberVar) {
                    Kit.codeBug();
                }
                this.cfw.addAStore(reg);
                if (needValue) {
                    this.cfw.addALoad(reg);
                }
            } else if (isNumberVar) {
                this.cfw.addDStore(reg);
                if (needValue) {
                    this.cfw.addDLoad(reg);
                }
            } else {
                if (needValue) {
                    this.cfw.add(92);
                }
                addDoubleWrap();
                this.cfw.addAStore(reg);
            }
        } else if (isNumber) {
            if (needValue) {
                this.cfw.add(92);
            }
            this.cfw.addALoad(reg);
            this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
            int isNumberLabel = this.cfw.acquireLabel();
            int beyond = this.cfw.acquireLabel();
            this.cfw.add(165, isNumberLabel);
            short stack = this.cfw.getStackTop();
            addDoubleWrap();
            this.cfw.addAStore(reg);
            this.cfw.add(167, beyond);
            this.cfw.markLabel(isNumberLabel, stack);
            this.cfw.addDStore(reg + 1);
            this.cfw.markLabel(beyond);
        } else {
            if (needValue) {
                this.cfw.add(89);
            }
            this.cfw.addAStore(reg);
        }
    }

    private void visitSetConstVar(Node node, Node child, boolean needValue) {
        if (!this.hasVarsInRegs) {
            Kit.codeBug();
        }
        int varIndex = this.fnCurrent.getVarIndex(node);
        generateExpression(child.getNext(), node);
        boolean isNumber = node.getIntProp(8, -1) != -1;
        short reg = this.varRegisters[varIndex];
        int beyond = this.cfw.acquireLabel();
        int noAssign = this.cfw.acquireLabel();
        short stack;
        if (isNumber) {
            this.cfw.addILoad(reg + 2);
            this.cfw.add(154, noAssign);
            stack = this.cfw.getStackTop();
            this.cfw.addPush(1);
            this.cfw.addIStore(reg + 2);
            this.cfw.addDStore(reg);
            if (needValue) {
                this.cfw.addDLoad(reg);
                this.cfw.markLabel(noAssign, stack);
            } else {
                this.cfw.add(167, beyond);
                this.cfw.markLabel(noAssign, stack);
                this.cfw.add(88);
            }
        } else {
            this.cfw.addILoad(reg + 1);
            this.cfw.add(154, noAssign);
            stack = this.cfw.getStackTop();
            this.cfw.addPush(1);
            this.cfw.addIStore(reg + 1);
            this.cfw.addAStore(reg);
            if (needValue) {
                this.cfw.addALoad(reg);
                this.cfw.markLabel(noAssign, stack);
            } else {
                this.cfw.add(167, beyond);
                this.cfw.markLabel(noAssign, stack);
                this.cfw.add(87);
            }
        }
        this.cfw.markLabel(beyond);
    }

    private void visitGetProp(Node node, Node child) {
        generateExpression(child, node);
        Node nameChild = child.getNext();
        generateExpression(nameChild, node);
        if (node.getType() == 34) {
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            addScriptRuntimeInvoke("getObjectPropNoWarn", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        } else if (child.getType() == 43 && nameChild.getType() == 41) {
            this.cfw.addALoad(this.contextLocal);
            addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
        } else {
            this.cfw.addALoad(this.contextLocal);
            this.cfw.addALoad(this.variableObjectLocal);
            addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        }
    }

    private void visitSetProp(int type, Node node, Node child) {
        Node objectChild = child;
        generateExpression(child, node);
        child = child.getNext();
        if (type == 139) {
            this.cfw.add(89);
        }
        Node nameChild = child;
        generateExpression(child, node);
        child = child.getNext();
        if (type == 139) {
            this.cfw.add(90);
            if (objectChild.getType() == 43 && nameChild.getType() == 41) {
                this.cfw.addALoad(this.contextLocal);
                addScriptRuntimeInvoke("getObjectProp", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Lorg/mozilla/javascript/Context;)Ljava/lang/Object;");
            } else {
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            }
        }
        generateExpression(child, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("setObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
    }

    private void visitSetElem(int type, Node node, Node child) {
        generateExpression(child, node);
        child = child.getNext();
        if (type == 140) {
            this.cfw.add(89);
        }
        generateExpression(child, node);
        child = child.getNext();
        boolean indexIsNumber = node.getIntProp(8, -1) != -1;
        if (type == 140) {
            if (indexIsNumber) {
                this.cfw.add(93);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            } else {
                this.cfw.add(90);
                this.cfw.addALoad(this.contextLocal);
                this.cfw.addALoad(this.variableObjectLocal);
                addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
            }
        }
        generateExpression(child, node);
        this.cfw.addALoad(this.contextLocal);
        this.cfw.addALoad(this.variableObjectLocal);
        if (indexIsNumber) {
            addScriptRuntimeInvoke("setObjectIndex", "(Ljava/lang/Object;DLjava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        } else {
            addScriptRuntimeInvoke("setObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        }
    }

    private void visitDotQuery(Node node, Node child) {
        updateLineNumber(node);
        generateExpression(child, node);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("enterDotQuery", "(Ljava/lang/Object;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
        this.cfw.add(1);
        int queryLoopStart = this.cfw.acquireLabel();
        this.cfw.markLabel(queryLoopStart);
        this.cfw.add(87);
        generateExpression(child.getNext(), node);
        addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("updateDotQuery", "(ZLorg/mozilla/javascript/Scriptable;)Ljava/lang/Object;");
        this.cfw.add(89);
        this.cfw.add(198, queryLoopStart);
        this.cfw.addALoad(this.variableObjectLocal);
        addScriptRuntimeInvoke("leaveDotQuery", "(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;");
        this.cfw.addAStore(this.variableObjectLocal);
    }

    private int getLocalBlockRegister(Node node) {
        return ((Node) node.getProp(3)).getExistingIntProp(2);
    }

    private void dcpLoadAsNumber(int dcp_register) {
        this.cfw.addALoad(dcp_register);
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        int isNumberLabel = this.cfw.acquireLabel();
        this.cfw.add(165, isNumberLabel);
        short stack = this.cfw.getStackTop();
        this.cfw.addALoad(dcp_register);
        addObjectToDouble();
        int beyond = this.cfw.acquireLabel();
        this.cfw.add(167, beyond);
        this.cfw.markLabel(isNumberLabel, stack);
        this.cfw.addDLoad(dcp_register + 1);
        this.cfw.markLabel(beyond);
    }

    private void dcpLoadAsObject(int dcp_register) {
        this.cfw.addALoad(dcp_register);
        this.cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        int isNumberLabel = this.cfw.acquireLabel();
        this.cfw.add(165, isNumberLabel);
        short stack = this.cfw.getStackTop();
        this.cfw.addALoad(dcp_register);
        int beyond = this.cfw.acquireLabel();
        this.cfw.add(167, beyond);
        this.cfw.markLabel(isNumberLabel, stack);
        this.cfw.addDLoad(dcp_register + 1);
        addDoubleWrap();
        this.cfw.markLabel(beyond);
    }

    private void addGoto(Node target, int jumpcode) {
        this.cfw.add(jumpcode, getTargetLabel(target));
    }

    private void addObjectToDouble() {
        addScriptRuntimeInvoke("toNumber", "(Ljava/lang/Object;)D");
    }

    private void addNewObjectArray(int size) {
        if (size != 0) {
            this.cfw.addPush(size);
            this.cfw.add(189, "java/lang/Object");
        } else if (this.itsZeroArgArray >= (short) 0) {
            this.cfw.addALoad(this.itsZeroArgArray);
        } else {
            this.cfw.add(178, "org/mozilla/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
        }
    }

    private void addScriptRuntimeInvoke(String methodName, String methodSignature) {
        this.cfw.addInvoke(184, "org.mozilla.javascript.ScriptRuntime", methodName, methodSignature);
    }

    private void addOptRuntimeInvoke(String methodName, String methodSignature) {
        this.cfw.addInvoke(184, "org/mozilla/javascript/optimizer/OptRuntime", methodName, methodSignature);
    }

    private void addJumpedBooleanWrap(int trueLabel, int falseLabel) {
        this.cfw.markLabel(falseLabel);
        int skip = this.cfw.acquireLabel();
        this.cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
        this.cfw.add(167, skip);
        this.cfw.markLabel(trueLabel);
        this.cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
        this.cfw.markLabel(skip);
        this.cfw.adjustStackTop(-1);
    }

    private void addDoubleWrap() {
        addOptRuntimeInvoke("wrapDouble", "(D)Ljava/lang/Double;");
    }

    private short getNewWordPairLocal(boolean isConst) {
        return getNewWordIntern(isConst ? 3 : 2);
    }

    private short getNewWordLocal(boolean isConst) {
        return getNewWordIntern(isConst ? 2 : 1);
    }

    private short getNewWordLocal() {
        return getNewWordIntern(1);
    }

    private short getNewWordIntern(int count) {
        if ($assertionsDisabled || (count >= 1 && count <= 3)) {
            int[] locals = this.locals;
            short result = (short) -1;
            if (count > 1) {
                short i = this.firstFreeLocal;
                loop0:
                while (i + count <= 1024) {
                    int j = 0;
                    while (j < count) {
                        if (locals[i + j] != 0) {
                            i += j + 1;
                        } else {
                            j++;
                        }
                    }
                    result = i;
                }
            } else {
                result = this.firstFreeLocal;
            }
            if (result != (short) -1) {
                locals[result] = 1;
                if (count > 1) {
                    locals[result + 1] = 1;
                }
                if (count > 2) {
                    locals[result + 2] = 1;
                }
                if (result != this.firstFreeLocal) {
                    return (short) result;
                }
                for (int i2 = result + count; i2 < 1024; i2++) {
                    if (locals[i2] == 0) {
                        this.firstFreeLocal = (short) i2;
                        if (this.localsMax < this.firstFreeLocal) {
                            this.localsMax = this.firstFreeLocal;
                        }
                        return (short) result;
                    }
                }
            }
            throw Context.reportRuntimeError("Program too complex (out of locals)");
        }
        throw new AssertionError();
    }

    private void incReferenceWordLocal(short local) {
        int[] iArr = this.locals;
        iArr[local] = iArr[local] + 1;
    }

    private void decReferenceWordLocal(short local) {
        int[] iArr = this.locals;
        iArr[local] = iArr[local] - 1;
    }

    private void releaseWordLocal(short local) {
        if (local < this.firstFreeLocal) {
            this.firstFreeLocal = local;
        }
        this.locals[local] = 0;
    }
}
