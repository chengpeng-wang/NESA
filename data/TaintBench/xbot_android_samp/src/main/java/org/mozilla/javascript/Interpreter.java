package org.mozilla.javascript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.framing.CloseFrame;
import org.mozilla.classfile.ByteCode;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.debug.DebugFrame;

public final class Interpreter extends Icode implements Evaluator {
    static final int EXCEPTION_HANDLER_SLOT = 2;
    static final int EXCEPTION_LOCAL_SLOT = 4;
    static final int EXCEPTION_SCOPE_SLOT = 5;
    static final int EXCEPTION_SLOT_SIZE = 6;
    static final int EXCEPTION_TRY_END_SLOT = 1;
    static final int EXCEPTION_TRY_START_SLOT = 0;
    static final int EXCEPTION_TYPE_SLOT = 3;
    InterpreterData itsData;

    private static class CallFrame implements Cloneable, Serializable {
        static final long serialVersionUID = -2843792508994958978L;
        DebugFrame debuggerFrame;
        int emptyStackTop;
        InterpretedFunction fnOrScript;
        int frameIndex;
        boolean frozen;
        InterpreterData idata;
        boolean isContinuationsTopFrame;
        int localShift;
        CallFrame parentFrame;
        int pc;
        int pcPrevBranch;
        int pcSourceLineStart;
        Object result;
        double resultDbl;
        double[] sDbl;
        int savedCallOp;
        int savedStackTop;
        Scriptable scope;
        Object[] stack;
        int[] stackAttributes;
        Scriptable thisObj;
        Object throwable;
        boolean useActivation;
        CallFrame varSource;

        private CallFrame() {
        }

        /* access modifiers changed from: 0000 */
        public CallFrame cloneFrozen() {
            if (!this.frozen) {
                Kit.codeBug();
            }
            try {
                CallFrame copy = (CallFrame) clone();
                copy.stack = (Object[]) this.stack.clone();
                copy.stackAttributes = (int[]) this.stackAttributes.clone();
                copy.sDbl = (double[]) this.sDbl.clone();
                copy.frozen = false;
                return copy;
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException();
            }
        }
    }

    private static final class ContinuationJump implements Serializable {
        static final long serialVersionUID = 7687739156004308247L;
        CallFrame branchFrame;
        CallFrame capturedFrame;
        Object result;
        double resultDbl;

        ContinuationJump(NativeContinuation c, CallFrame current) {
            this.capturedFrame = (CallFrame) c.getImplementation();
            if (this.capturedFrame == null || current == null) {
                this.branchFrame = null;
                return;
            }
            CallFrame chain1 = this.capturedFrame;
            CallFrame chain2 = current;
            int diff = chain1.frameIndex - chain2.frameIndex;
            if (diff != 0) {
                if (diff < 0) {
                    chain1 = current;
                    chain2 = this.capturedFrame;
                    diff = -diff;
                }
                do {
                    chain1 = chain1.parentFrame;
                    diff--;
                } while (diff != 0);
                if (chain1.frameIndex != chain2.frameIndex) {
                    Kit.codeBug();
                }
            }
            while (chain1 != chain2 && chain1 != null) {
                chain1 = chain1.parentFrame;
                chain2 = chain2.parentFrame;
            }
            this.branchFrame = chain1;
            if (this.branchFrame != null && !this.branchFrame.frozen) {
                Kit.codeBug();
            }
        }
    }

    static class GeneratorState {
        int operation;
        RuntimeException returnedException;
        Object value;

        GeneratorState(int operation, Object value) {
            this.operation = operation;
            this.value = value;
        }
    }

    private static CallFrame captureFrameForGenerator(CallFrame frame) {
        frame.frozen = true;
        CallFrame result = frame.cloneFrozen();
        frame.frozen = false;
        result.parentFrame = null;
        result.frameIndex = 0;
        return result;
    }

    public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction) {
        this.itsData = new CodeGenerator().compile(compilerEnv, tree, encodedSource, returnFunction);
        return this.itsData;
    }

    public Script createScriptObject(Object bytecode, Object staticSecurityDomain) {
        if (bytecode != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createScript(this.itsData, staticSecurityDomain);
    }

    public void setEvalScriptFlag(Script script) {
        ((InterpretedFunction) script).idata.evalScriptFlag = true;
    }

    public Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain) {
        if (bytecode != this.itsData) {
            Kit.codeBug();
        }
        return InterpretedFunction.createFunction(cx, scope, this.itsData, staticSecurityDomain);
    }

    private static int getShort(byte[] iCode, int pc) {
        return (iCode[pc] << 8) | (iCode[pc + 1] & ByteCode.IMPDEP2);
    }

    private static int getIndex(byte[] iCode, int pc) {
        return ((iCode[pc] & ByteCode.IMPDEP2) << 8) | (iCode[pc + 1] & ByteCode.IMPDEP2);
    }

    private static int getInt(byte[] iCode, int pc) {
        return (((iCode[pc] << 24) | ((iCode[pc + 1] & ByteCode.IMPDEP2) << 16)) | ((iCode[pc + 2] & ByteCode.IMPDEP2) << 8)) | (iCode[pc + 3] & ByteCode.IMPDEP2);
    }

    private static int getExceptionHandler(CallFrame frame, boolean onlyFinally) {
        int[] exceptionTable = frame.idata.itsExceptionTable;
        if (exceptionTable == null) {
            return -1;
        }
        int pc = frame.pc - 1;
        int best = -1;
        int bestStart = 0;
        int bestEnd = 0;
        int i = 0;
        while (i != exceptionTable.length) {
            int start = exceptionTable[i + 0];
            int end = exceptionTable[i + 1];
            if (start <= pc && pc < end && (!onlyFinally || exceptionTable[i + 3] == 1)) {
                if (best >= 0) {
                    if (bestEnd >= end) {
                        if (bestStart > start) {
                            Kit.codeBug();
                        }
                        if (bestEnd == end) {
                            Kit.codeBug();
                        }
                    }
                }
                best = i;
                bestStart = start;
                bestEnd = end;
            }
            i += 6;
        }
        return best;
    }

    static void dumpICode(InterpreterData idata) {
    }

    private static int bytecodeSpan(int bytecode) {
        switch (bytecode) {
            case -63:
            case -62:
            case -54:
            case -46:
            case -39:
            case -27:
            case -26:
            case -23:
            case -6:
            case 5:
            case 6:
            case 7:
            case 50:
            case 72:
                return 3;
            case -61:
            case -49:
            case -48:
                return 2;
            case -47:
                return 5;
            case -45:
                return 2;
            case -40:
                return 5;
            case -38:
                return 2;
            case -28:
                return 5;
            case -21:
                return 5;
            case -11:
            case -10:
            case -9:
            case -8:
            case -7:
                return 2;
            case 57:
                return 2;
            default:
                if (Icode.validBytecode(bytecode)) {
                    return 1;
                }
                throw Kit.codeBug();
        }
    }

    static int[] getLineNumbers(InterpreterData data) {
        UintMap presentLines = new UintMap();
        byte[] iCode = data.itsICode;
        int iCodeLength = iCode.length;
        int pc = 0;
        while (pc != iCodeLength) {
            int bytecode = iCode[pc];
            int span = bytecodeSpan(bytecode);
            if (bytecode == -26) {
                if (span != 3) {
                    Kit.codeBug();
                }
                presentLines.put(getIndex(iCode, pc + 1), 0);
            }
            pc += span;
        }
        return presentLines.getKeys();
    }

    public void captureStackInfo(RhinoException ex) {
        Context cx = Context.getCurrentContext();
        if (cx == null || cx.lastInterpreterFrame == null) {
            ex.interpreterStackInfo = null;
            ex.interpreterLineData = null;
            return;
        }
        CallFrame[] array;
        int i;
        if (cx.previousInterpreterInvocations == null || cx.previousInterpreterInvocations.size() == 0) {
            array = new CallFrame[1];
        } else {
            int previousCount = cx.previousInterpreterInvocations.size();
            if (cx.previousInterpreterInvocations.peek() == cx.lastInterpreterFrame) {
                previousCount--;
            }
            array = new CallFrame[(previousCount + 1)];
            cx.previousInterpreterInvocations.toArray(array);
        }
        array[array.length - 1] = (CallFrame) cx.lastInterpreterFrame;
        int interpreterFrameCount = 0;
        for (i = 0; i != array.length; i++) {
            interpreterFrameCount += array[i].frameIndex + 1;
        }
        int[] linePC = new int[interpreterFrameCount];
        int linePCIndex = interpreterFrameCount;
        i = array.length;
        while (i != 0) {
            i--;
            for (CallFrame frame = array[i]; frame != null; frame = frame.parentFrame) {
                linePCIndex--;
                linePC[linePCIndex] = frame.pcSourceLineStart;
            }
        }
        if (linePCIndex != 0) {
            Kit.codeBug();
        }
        ex.interpreterStackInfo = array;
        ex.interpreterLineData = linePC;
    }

    public String getSourcePositionFromStack(Context cx, int[] linep) {
        CallFrame frame = cx.lastInterpreterFrame;
        InterpreterData idata = frame.idata;
        if (frame.pcSourceLineStart >= 0) {
            linep[0] = getIndex(idata.itsICode, frame.pcSourceLineStart);
        } else {
            linep[0] = 0;
        }
        return idata.itsSourceFile;
    }

    public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
        String tag = "org.mozilla.javascript.Interpreter.interpretLoop";
        StringBuilder sb = new StringBuilder(nativeStackTrace.length() + CloseFrame.NORMAL);
        String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
        CallFrame[] array = (CallFrame[]) ex.interpreterStackInfo;
        int[] linePC = ex.interpreterLineData;
        int arrayIndex = array.length;
        int linePCIndex = linePC.length;
        int offset = 0;
        while (arrayIndex != 0) {
            arrayIndex--;
            int pos = nativeStackTrace.indexOf(tag, offset);
            if (pos < 0) {
                break;
            }
            pos += tag.length();
            while (pos != nativeStackTrace.length()) {
                char c = nativeStackTrace.charAt(pos);
                if (c == 10 || c == 13) {
                    break;
                }
                pos++;
            }
            sb.append(nativeStackTrace.substring(offset, pos));
            offset = pos;
            for (CallFrame frame = array[arrayIndex]; frame != null; frame = frame.parentFrame) {
                if (linePCIndex == 0) {
                    Kit.codeBug();
                }
                linePCIndex--;
                InterpreterData idata = frame.idata;
                sb.append(lineSeparator);
                sb.append("\tat script");
                if (!(idata.itsName == null || idata.itsName.length() == 0)) {
                    sb.append('.');
                    sb.append(idata.itsName);
                }
                sb.append('(');
                sb.append(idata.itsSourceFile);
                int pc = linePC[linePCIndex];
                if (pc >= 0) {
                    sb.append(':');
                    sb.append(getIndex(idata.itsICode, pc));
                }
                sb.append(')');
            }
        }
        sb.append(nativeStackTrace.substring(offset));
        return sb.toString();
    }

    public List<String> getScriptStack(RhinoException ex) {
        ScriptStackElement[][] stack = getScriptStackElements(ex);
        List<String> list = new ArrayList(stack.length);
        String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
        for (ScriptStackElement[] group : stack) {
            StringBuilder sb = new StringBuilder();
            for (ScriptStackElement elem : group) {
                elem.renderJavaStyle(sb);
                sb.append(lineSeparator);
            }
            list.add(sb.toString());
        }
        return list;
    }

    public ScriptStackElement[][] getScriptStackElements(RhinoException ex) {
        if (ex.interpreterStackInfo == null) {
            return (ScriptStackElement[][]) null;
        }
        List<ScriptStackElement[]> list = new ArrayList();
        CallFrame[] array = (CallFrame[]) ex.interpreterStackInfo;
        int[] linePC = ex.interpreterLineData;
        int arrayIndex = array.length;
        int linePCIndex = linePC.length;
        while (arrayIndex != 0) {
            arrayIndex--;
            CallFrame frame = array[arrayIndex];
            List<ScriptStackElement> group = new ArrayList();
            while (frame != null) {
                if (linePCIndex == 0) {
                    Kit.codeBug();
                }
                linePCIndex--;
                InterpreterData idata = frame.idata;
                String fileName = idata.itsSourceFile;
                String functionName = null;
                int lineNumber = -1;
                int pc = linePC[linePCIndex];
                if (pc >= 0) {
                    lineNumber = getIndex(idata.itsICode, pc);
                }
                if (!(idata.itsName == null || idata.itsName.length() == 0)) {
                    functionName = idata.itsName;
                }
                frame = frame.parentFrame;
                group.add(new ScriptStackElement(fileName, functionName, lineNumber));
            }
            list.add(group.toArray(new ScriptStackElement[group.size()]));
        }
        return (ScriptStackElement[][]) list.toArray(new ScriptStackElement[list.size()][]);
    }

    static String getEncodedSource(InterpreterData idata) {
        if (idata.encodedSource == null) {
            return null;
        }
        return idata.encodedSource.substring(idata.encodedSourceStart, idata.encodedSourceEnd);
    }

    private static void initFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
        InterpretedFunction fn = InterpretedFunction.createFunction(cx, scope, parent, index);
        ScriptRuntime.initFunction(cx, scope, fn, fn.idata.itsFunctionType, parent.idata.evalScriptFlag);
    }

    static Object interpret(InterpretedFunction ifun, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!ScriptRuntime.hasTopCall(cx)) {
            Kit.codeBug();
        }
        if (cx.interpreterSecurityDomain != ifun.securityDomain) {
            Object savedDomain = cx.interpreterSecurityDomain;
            cx.interpreterSecurityDomain = ifun.securityDomain;
            try {
                Object callWithDomain = ifun.securityController.callWithDomain(ifun.securityDomain, cx, ifun, scope, thisObj, args);
                return callWithDomain;
            } finally {
                cx.interpreterSecurityDomain = savedDomain;
            }
        } else {
            CallFrame frame = new CallFrame();
            initFrame(cx, scope, thisObj, args, null, 0, args.length, ifun, null, frame);
            frame.isContinuationsTopFrame = cx.isContinuationsTopCall;
            cx.isContinuationsTopCall = false;
            return interpretLoop(cx, frame, null);
        }
    }

    public static Object resumeGenerator(Context cx, Scriptable scope, int operation, Object savedState, Object value) {
        CallFrame frame = (CallFrame) savedState;
        GeneratorState generatorState = new GeneratorState(operation, value);
        if (operation == 2) {
            try {
                return interpretLoop(cx, frame, generatorState);
            } catch (RuntimeException e) {
                if (e == value) {
                    return Undefined.instance;
                }
                throw e;
            }
        }
        Object result = interpretLoop(cx, frame, generatorState);
        if (generatorState.returnedException == null) {
            return result;
        }
        throw generatorState.returnedException;
    }

    public static Object restartContinuation(NativeContinuation c, Context cx, Scriptable scope, Object[] args) {
        if (!ScriptRuntime.hasTopCall(cx)) {
            return ScriptRuntime.doTopCall(c, cx, scope, null, args);
        }
        Object arg;
        if (args.length == 0) {
            arg = Undefined.instance;
        } else {
            arg = args[0];
        }
        if (((CallFrame) c.getImplementation()) == null) {
            return arg;
        }
        ContinuationJump cjump = new ContinuationJump(c, null);
        cjump.result = arg;
        return interpretLoop(cx, null, cjump);
    }

    /* JADX WARNING: Removed duplicated region for block: B:116:0x02e3 A:{Catch:{ Throwable -> 0x00e8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:416:0x0e71 A:{Catch:{ Throwable -> 0x00e8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x02f5 A:{Catch:{ Throwable -> 0x00e8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:530:0x00a9 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0302 A:{Catch:{ Throwable -> 0x00e8 }} */
    /* JADX WARNING: Removed duplicated region for block: B:428:0x0eac  */
    /* JADX WARNING: Removed duplicated region for block: B:494:0x00eb A:{SYNTHETIC} */
    /* JADX WARNING: Missing block: B:44:0x0144, code skipped:
            exitFrame(r112, r113, null);
            r79 = r113.result;
            r80 = r113.resultDbl;
     */
    /* JADX WARNING: Missing block: B:45:0x015c, code skipped:
            if (r113.parentFrame == null) goto L_0x0e85;
     */
    /* JADX WARNING: Missing block: B:46:0x015e, code skipped:
            r113 = r113.parentFrame;
     */
    /* JADX WARNING: Missing block: B:47:0x0168, code skipped:
            if (r113.frozen == false) goto L_0x016e;
     */
    /* JADX WARNING: Missing block: B:48:0x016a, code skipped:
            r113 = r113.cloneFrozen();
     */
    /* JADX WARNING: Missing block: B:49:0x016e, code skipped:
            setCallResult(r113, r79, r80);
            r79 = null;
     */
    /* JADX WARNING: Missing block: B:51:0x017f, code skipped:
            if (r113.frozen != false) goto L_0x018c;
     */
    /* JADX WARNING: Missing block: B:53:0x018c, code skipped:
            r90 = thawGenerator(r113, r10, r73, r7);
     */
    /* JADX WARNING: Missing block: B:55:0x0198, code skipped:
            if (r90 == org.mozilla.javascript.Scriptable.NOT_FOUND) goto L_0x00a9;
     */
    /* JADX WARNING: Missing block: B:56:0x019a, code skipped:
            r114 = r90;
     */
    /* JADX WARNING: Missing block: B:300:0x09d9, code skipped:
            r10 = doSetConstVar(r113, r8, r9, r10, r28, r29, r30, r20);
     */
    /* JADX WARNING: Missing block: B:302:0x09f5, code skipped:
            r10 = doSetVar(r113, r8, r9, r10, r28, r29, r30, r20);
     */
    /* JADX WARNING: Missing block: B:304:0x0a11, code skipped:
            r10 = doGetVar(r113, r8, r9, r10, r28, r29, r20);
     */
    /* JADX WARNING: Missing block: B:417:0x0e85, code skipped:
            r5 = r111;
     */
    /* JADX WARNING: Missing block: B:489:0x0fb0, code skipped:
            r10 = r103;
     */
    /* JADX WARNING: Missing block: B:648:?, code skipped:
            return freezeGenerator(r112, r113, r10, r73);
     */
    private static java.lang.Object interpretLoop(org.mozilla.javascript.Context r112, org.mozilla.javascript.Interpreter.CallFrame r113, java.lang.Object r114) {
        /*
        r54 = org.mozilla.javascript.UniqueTag.DOUBLE_MARK;
        r106 = org.mozilla.javascript.Undefined.instance;
        r0 = r112;
        r5 = r0.instructionThreshold;
        if (r5 == 0) goto L_0x00f8;
    L_0x000a:
        r78 = 1;
    L_0x000c:
        r59 = 100;
        r55 = 100;
        r104 = 0;
        r20 = -1;
        r0 = r112;
        r5 = r0.lastInterpreterFrame;
        if (r5 == 0) goto L_0x0034;
    L_0x001a:
        r0 = r112;
        r5 = r0.previousInterpreterInvocations;
        if (r5 != 0) goto L_0x0029;
    L_0x0020:
        r5 = new org.mozilla.javascript.ObjArray;
        r5.m427init();
        r0 = r112;
        r0.previousInterpreterInvocations = r5;
    L_0x0029:
        r0 = r112;
        r5 = r0.previousInterpreterInvocations;
        r0 = r112;
        r6 = r0.lastInterpreterFrame;
        r5.push(r6);
    L_0x0034:
        r73 = 0;
        if (r114 == 0) goto L_0x004e;
    L_0x0038:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.Interpreter.GeneratorState;
        if (r5 == 0) goto L_0x00fc;
    L_0x003e:
        r73 = r114;
        r73 = (org.mozilla.javascript.Interpreter.GeneratorState) r73;
        r5 = org.mozilla.javascript.ScriptRuntime.emptyArgs;
        r6 = 1;
        r0 = r112;
        r1 = r113;
        enterFrame(r0, r1, r5, r6);
        r114 = 0;
    L_0x004e:
        r79 = 0;
        r80 = 0;
        r111 = r114;
    L_0x0054:
        if (r111 == 0) goto L_0x0107;
    L_0x0056:
        r0 = r112;
        r1 = r111;
        r2 = r113;
        r3 = r20;
        r4 = r78;
        r113 = processThrowable(r0, r1, r2, r3, r4);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0 = r0.throwable;	 Catch:{ Throwable -> 0x00e8 }
        r114 = r0;
        r5 = 0;
        r0 = r113;
        r0.throwable = r5;	 Catch:{ Throwable -> 0x0fab }
        r111 = r114;
    L_0x0071:
        r0 = r113;
        r8 = r0.stack;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r9 = r0.sDbl;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.varSource;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5.stack;	 Catch:{ Throwable -> 0x00e8 }
        r28 = r0;
        r0 = r113;
        r5 = r0.varSource;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5.sDbl;	 Catch:{ Throwable -> 0x00e8 }
        r29 = r0;
        r0 = r113;
        r5 = r0.varSource;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5.stackAttributes;	 Catch:{ Throwable -> 0x00e8 }
        r30 = r0;
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r13 = r5.itsICode;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5.itsStringTable;	 Catch:{ Throwable -> 0x00e8 }
        r105 = r0;
        r0 = r113;
        r10 = r0.savedStackTop;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r1 = r112;
        r1.lastInterpreterFrame = r0;	 Catch:{ Throwable -> 0x00e8 }
    L_0x00a9:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r5 + 1;
        r0 = r113;
        r0.pc = r6;	 Catch:{ Throwable -> 0x00e8 }
        r7 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
        switch(r7) {
            case -64: goto L_0x0d87;
            case -63: goto L_0x01f9;
            case -62: goto L_0x0114;
            case -61: goto L_0x09cd;
            case -60: goto L_0x00b8;
            case -59: goto L_0x04a8;
            case -58: goto L_0x0c73;
            case -57: goto L_0x0c50;
            case -56: goto L_0x0616;
            case -55: goto L_0x06dd;
            case -54: goto L_0x0d15;
            case -53: goto L_0x0cf5;
            case -52: goto L_0x0a47;
            case -51: goto L_0x0a3d;
            case -50: goto L_0x0a7c;
            case -49: goto L_0x09e9;
            case -48: goto L_0x0a05;
            case -47: goto L_0x0e5b;
            case -46: goto L_0x0e45;
            case -45: goto L_0x0e2f;
            case -44: goto L_0x0e2a;
            case -43: goto L_0x0e25;
            case -42: goto L_0x0e20;
            case -41: goto L_0x0e1b;
            case -40: goto L_0x0e07;
            case -39: goto L_0x0df3;
            case -38: goto L_0x0ddd;
            case -37: goto L_0x0dd9;
            case -36: goto L_0x0dd5;
            case -35: goto L_0x0dd1;
            case -34: goto L_0x0dcd;
            case -33: goto L_0x0dc9;
            case -32: goto L_0x0dc5;
            case -31: goto L_0x0c96;
            case -30: goto L_0x0c2c;
            case -29: goto L_0x0c16;
            case -28: goto L_0x096e;
            case -27: goto L_0x0953;
            case -26: goto L_0x0d98;
            case -25: goto L_0x033f;
            case -24: goto L_0x031a;
            case -23: goto L_0x030c;
            case -22: goto L_0x03e2;
            case -21: goto L_0x06bf;
            case -20: goto L_0x0beb;
            case -19: goto L_0x0bd5;
            case -18: goto L_0x069d;
            case -17: goto L_0x0663;
            case -16: goto L_0x063b;
            case -15: goto L_0x0621;
            case -14: goto L_0x093d;
            case -13: goto L_0x0bc7;
            case -12: goto L_0x0bb7;
            case -11: goto L_0x05e0;
            case -10: goto L_0x058c;
            case -9: goto L_0x0548;
            case -8: goto L_0x09ab;
            case -7: goto L_0x0a25;
            case -6: goto L_0x02c4;
            case -5: goto L_0x0375;
            case -4: goto L_0x036e;
            case -3: goto L_0x03b8;
            case -2: goto L_0x0398;
            case -1: goto L_0x0388;
            case 0: goto L_0x04cc;
            case 1: goto L_0x00b8;
            case 2: goto L_0x0a82;
            case 3: goto L_0x0aa4;
            case 4: goto L_0x03d2;
            case 5: goto L_0x02e1;
            case 6: goto L_0x02ac;
            case 7: goto L_0x0294;
            case 8: goto L_0x046b;
            case 9: goto L_0x03f9;
            case 10: goto L_0x03f9;
            case 11: goto L_0x03f9;
            case 12: goto L_0x0266;
            case 13: goto L_0x0266;
            case 14: goto L_0x0256;
            case 15: goto L_0x0256;
            case 16: goto L_0x0256;
            case 17: goto L_0x0256;
            case 18: goto L_0x03f9;
            case 19: goto L_0x03f9;
            case 20: goto L_0x0401;
            case 21: goto L_0x0435;
            case 22: goto L_0x043e;
            case 23: goto L_0x043e;
            case 24: goto L_0x043e;
            case 25: goto L_0x043e;
            case 26: goto L_0x0446;
            case 27: goto L_0x03ea;
            case 28: goto L_0x0420;
            case 29: goto L_0x0420;
            case 30: goto L_0x086a;
            case 31: goto L_0x04cc;
            case 32: goto L_0x0927;
            case 33: goto L_0x04f6;
            case 34: goto L_0x04d6;
            case 35: goto L_0x0516;
            case 36: goto L_0x0578;
            case 37: goto L_0x0582;
            case 38: goto L_0x06dd;
            case 39: goto L_0x0999;
            case 40: goto L_0x0989;
            case 41: goto L_0x094d;
            case 42: goto L_0x0a51;
            case 43: goto L_0x0a58;
            case 44: goto L_0x0a6c;
            case 45: goto L_0x0a74;
            case 46: goto L_0x027d;
            case 47: goto L_0x027d;
            case 48: goto L_0x0bfc;
            case 49: goto L_0x0459;
            case 50: goto L_0x0221;
            case 51: goto L_0x024c;
            case 52: goto L_0x025e;
            case 53: goto L_0x025e;
            case 54: goto L_0x0604;
            case 55: goto L_0x0a11;
            case 56: goto L_0x09f5;
            case 57: goto L_0x0ab2;
            case 58: goto L_0x0afa;
            case 59: goto L_0x0afa;
            case 60: goto L_0x0afa;
            case 61: goto L_0x0b32;
            case 62: goto L_0x0b32;
            case 63: goto L_0x0a62;
            case 64: goto L_0x0144;
            case 65: goto L_0x0c96;
            case 66: goto L_0x0c96;
            case 67: goto L_0x059a;
            case 68: goto L_0x05aa;
            case 69: goto L_0x05d0;
            case 70: goto L_0x06dd;
            case 71: goto L_0x0b51;
            case 72: goto L_0x017b;
            case 73: goto L_0x046b;
            case 74: goto L_0x0d45;
            case 75: goto L_0x0d5f;
            case 76: goto L_0x0d73;
            case 77: goto L_0x0b71;
            case 78: goto L_0x0b7b;
            case 79: goto L_0x0b85;
            case 80: goto L_0x0ba5;
            case 81: goto L_0x00b8;
            case 82: goto L_0x00b8;
            case 83: goto L_0x00b8;
            case 84: goto L_0x00b8;
            case 85: goto L_0x00b8;
            case 86: goto L_0x00b8;
            case 87: goto L_0x00b8;
            case 88: goto L_0x00b8;
            case 89: goto L_0x00b8;
            case 90: goto L_0x00b8;
            case 91: goto L_0x00b8;
            case 92: goto L_0x00b8;
            case 93: goto L_0x00b8;
            case 94: goto L_0x00b8;
            case 95: goto L_0x00b8;
            case 96: goto L_0x00b8;
            case 97: goto L_0x00b8;
            case 98: goto L_0x00b8;
            case 99: goto L_0x00b8;
            case 100: goto L_0x00b8;
            case 101: goto L_0x00b8;
            case 102: goto L_0x00b8;
            case 103: goto L_0x00b8;
            case 104: goto L_0x00b8;
            case 105: goto L_0x00b8;
            case 106: goto L_0x00b8;
            case 107: goto L_0x00b8;
            case 108: goto L_0x00b8;
            case 109: goto L_0x00b8;
            case 110: goto L_0x00b8;
            case 111: goto L_0x00b8;
            case 112: goto L_0x00b8;
            case 113: goto L_0x00b8;
            case 114: goto L_0x00b8;
            case 115: goto L_0x00b8;
            case 116: goto L_0x00b8;
            case 117: goto L_0x00b8;
            case 118: goto L_0x00b8;
            case 119: goto L_0x00b8;
            case 120: goto L_0x00b8;
            case 121: goto L_0x00b8;
            case 122: goto L_0x00b8;
            case 123: goto L_0x00b8;
            case 124: goto L_0x00b8;
            case 125: goto L_0x00b8;
            case 126: goto L_0x00b8;
            case 127: goto L_0x00b8;
            case 128: goto L_0x00b8;
            case 129: goto L_0x00b8;
            case 130: goto L_0x00b8;
            case 131: goto L_0x00b8;
            case 132: goto L_0x00b8;
            case 133: goto L_0x00b8;
            case 134: goto L_0x00b8;
            case 135: goto L_0x00b8;
            case 136: goto L_0x00b8;
            case 137: goto L_0x00b8;
            case 138: goto L_0x00b8;
            case 139: goto L_0x00b8;
            case 140: goto L_0x00b8;
            case 141: goto L_0x00b8;
            case 142: goto L_0x00b8;
            case 143: goto L_0x00b8;
            case 144: goto L_0x00b8;
            case 145: goto L_0x00b8;
            case 146: goto L_0x00b8;
            case 147: goto L_0x00b8;
            case 148: goto L_0x00b8;
            case 149: goto L_0x00b8;
            case 150: goto L_0x00b8;
            case 151: goto L_0x00b8;
            case 152: goto L_0x00b8;
            case 153: goto L_0x00b8;
            case 154: goto L_0x00b8;
            case 155: goto L_0x00b8;
            case 156: goto L_0x09d9;
            default: goto L_0x00b8;
        };	 Catch:{ Throwable -> 0x00e8 }
    L_0x00b8:
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        dumpICode(r5);	 Catch:{ Throwable -> 0x00e8 }
        r5 = new java.lang.RuntimeException;	 Catch:{ Throwable -> 0x00e8 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00e8 }
        r6.<init>();	 Catch:{ Throwable -> 0x00e8 }
        r11 = "Unknown icode : ";
        r6 = r6.append(r11);	 Catch:{ Throwable -> 0x00e8 }
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x00e8 }
        r11 = " @ pc : ";
        r6 = r6.append(r11);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r11 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r11 = r11 + -1;
        r6 = r6.append(r11);	 Catch:{ Throwable -> 0x00e8 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x00e8 }
        r5.<init>(r6);	 Catch:{ Throwable -> 0x00e8 }
        throw r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x00e8:
        r68 = move-exception;
    L_0x00e9:
        if (r111 == 0) goto L_0x0eac;
    L_0x00eb:
        r5 = java.lang.System.err;
        r0 = r68;
        r0.printStackTrace(r5);
        r5 = new java.lang.IllegalStateException;
        r5.<init>();
        throw r5;
    L_0x00f8:
        r78 = 0;
        goto L_0x000c;
    L_0x00fc:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.Interpreter.ContinuationJump;
        if (r5 != 0) goto L_0x004e;
    L_0x0102:
        org.mozilla.javascript.Kit.codeBug();
        goto L_0x004e;
    L_0x0107:
        if (r73 != 0) goto L_0x0071;
    L_0x0109:
        r0 = r113;
        r5 = r0.frozen;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0071;
    L_0x010f:
        org.mozilla.javascript.Kit.codeBug();	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0071;
    L_0x0114:
        r0 = r113;
        r5 = r0.frozen;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x017b;
    L_0x011a:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + -1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        r72 = captureFrameForGenerator(r113);	 Catch:{ Throwable -> 0x00e8 }
        r5 = 1;
        r0 = r72;
        r0.frozen = r5;	 Catch:{ Throwable -> 0x00e8 }
        r71 = new org.mozilla.javascript.NativeGenerator;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r72;
        r6 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r71;
        r1 = r72;
        r0.m1233init(r5, r6, r1);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r71;
        r1 = r113;
        r1.result = r0;	 Catch:{ Throwable -> 0x00e8 }
    L_0x0144:
        r5 = 0;
        r0 = r112;
        r1 = r113;
        exitFrame(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0 = r0.result;	 Catch:{ Throwable -> 0x00e8 }
        r79 = r0;
        r0 = r113;
        r0 = r0.resultDbl;	 Catch:{ Throwable -> 0x00e8 }
        r80 = r0;
        r0 = r113;
        r5 = r0.parentFrame;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0e85;
    L_0x015e:
        r0 = r113;
        r0 = r0.parentFrame;	 Catch:{ Throwable -> 0x00e8 }
        r113 = r0;
        r0 = r113;
        r5 = r0.frozen;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x016e;
    L_0x016a:
        r113 = r113.cloneFrozen();	 Catch:{ Throwable -> 0x00e8 }
    L_0x016e:
        r0 = r113;
        r1 = r79;
        r2 = r80;
        setCallResult(r0, r1, r2);	 Catch:{ Throwable -> 0x00e8 }
        r79 = 0;
        goto L_0x0054;
    L_0x017b:
        r0 = r113;
        r5 = r0.frozen;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x018c;
    L_0x0181:
        r0 = r112;
        r1 = r113;
        r2 = r73;
        r79 = freezeGenerator(r0, r1, r10, r2);	 Catch:{ Throwable -> 0x00e8 }
    L_0x018b:
        return r79;
    L_0x018c:
        r0 = r113;
        r1 = r73;
        r90 = thawGenerator(r0, r10, r1, r7);	 Catch:{ Throwable -> 0x00e8 }
        r5 = org.mozilla.javascript.Scriptable.NOT_FOUND;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        if (r0 == r5) goto L_0x00a9;
    L_0x019a:
        r114 = r90;
    L_0x019c:
        if (r114 != 0) goto L_0x01a1;
    L_0x019e:
        org.mozilla.javascript.Kit.codeBug();
    L_0x01a1:
        r56 = 2;
        r57 = 1;
        r58 = 0;
        r63 = 0;
        if (r73 == 0) goto L_0x0eb0;
    L_0x01ab:
        r0 = r73;
        r5 = r0.operation;
        r6 = 2;
        if (r5 != r6) goto L_0x0eb0;
    L_0x01b2:
        r0 = r73;
        r5 = r0.value;
        r0 = r114;
        if (r0 != r5) goto L_0x0eb0;
    L_0x01ba:
        r69 = 1;
    L_0x01bc:
        if (r78 == 0) goto L_0x0fb4;
    L_0x01be:
        r5 = 100;
        r0 = r112;
        r1 = r113;
        addInstructionCount(r0, r1, r5);	 Catch:{ RuntimeException -> 0x0f25, Error -> 0x0f2e }
        r5 = r114;
    L_0x01c9:
        r0 = r113;
        r6 = r0.debuggerFrame;
        if (r6 == 0) goto L_0x01e2;
    L_0x01cf:
        r6 = r5 instanceof java.lang.RuntimeException;
        if (r6 == 0) goto L_0x01e2;
    L_0x01d3:
        r99 = r5;
        r99 = (java.lang.RuntimeException) r99;
        r0 = r113;
        r6 = r0.debuggerFrame;	 Catch:{ Throwable -> 0x0f39 }
        r0 = r112;
        r1 = r99;
        r6.onExceptionThrown(r0, r1);	 Catch:{ Throwable -> 0x0f39 }
    L_0x01e2:
        if (r69 == 0) goto L_0x0f48;
    L_0x01e4:
        r6 = 2;
        r0 = r69;
        if (r0 == r6) goto L_0x0f44;
    L_0x01e9:
        r92 = 1;
    L_0x01eb:
        r0 = r113;
        r1 = r92;
        r20 = getExceptionHandler(r0, r1);
        if (r20 < 0) goto L_0x0f48;
    L_0x01f5:
        r111 = r5;
        goto L_0x0054;
    L_0x01f9:
        r5 = 1;
        r0 = r113;
        r0.frozen = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r102 = getIndex(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r5 = new org.mozilla.javascript.JavaScriptException;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r6 = org.mozilla.javascript.NativeIterator.getStopIterationObject(r6);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r11 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r11 = r11.itsSourceFile;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r102;
        r5.m917init(r6, r11, r0);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r73;
        r0.returnedException = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0144;
    L_0x0221:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 != r1) goto L_0x022f;
    L_0x0229:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r109 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x022f:
        r10 = r10 + -1;
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r102 = getIndex(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r114 = new org.mozilla.javascript.JavaScriptException;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.itsSourceFile;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r114;
        r1 = r109;
        r2 = r102;
        r0.m917init(r1, r5, r2);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x019c;
    L_0x024c:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r114 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x019c;
    L_0x0256:
        r0 = r113;
        r10 = doCompare(r0, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x025e:
        r0 = r112;
        r10 = doInOrInstanceof(r0, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0266:
        r10 = r10 + -1;
        r108 = doEquals(r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        r5 = 13;
        if (r7 != r5) goto L_0x027b;
    L_0x0270:
        r5 = 1;
    L_0x0271:
        r108 = r108 ^ r5;
        r5 = org.mozilla.javascript.ScriptRuntime.wrapBoolean(r108);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x027b:
        r5 = 0;
        goto L_0x0271;
    L_0x027d:
        r10 = r10 + -1;
        r108 = doShallowEquals(r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        r5 = 47;
        if (r7 != r5) goto L_0x0292;
    L_0x0287:
        r5 = 1;
    L_0x0288:
        r108 = r108 ^ r5;
        r5 = org.mozilla.javascript.ScriptRuntime.wrapBoolean(r108);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0292:
        r5 = 0;
        goto L_0x0288;
    L_0x0294:
        r103 = r10 + -1;
        r0 = r113;
        r5 = stack_boolean(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0fb0;
    L_0x029e:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r103;
        goto L_0x00a9;
    L_0x02ac:
        r103 = r10 + -1;
        r0 = r113;
        r5 = stack_boolean(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x0fb0;
    L_0x02b6:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r103;
        goto L_0x00a9;
    L_0x02c4:
        r103 = r10 + -1;
        r0 = r113;
        r5 = stack_boolean(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x02dc;
    L_0x02ce:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r103;
        goto L_0x00a9;
    L_0x02dc:
        r10 = r103 + -1;
        r5 = 0;
        r8[r103] = r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x02e1:
        if (r78 == 0) goto L_0x02eb;
    L_0x02e3:
        r5 = 2;
        r0 = r112;
        r1 = r113;
        addInstructionCount(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x02eb:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r91 = getShort(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        if (r91 == 0) goto L_0x0e71;
    L_0x02f5:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r91 + -1;
        r5 = r5 + r6;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x0300:
        if (r78 == 0) goto L_0x00a9;
    L_0x0302:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.pcPrevBranch = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x030c:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x02e1;
    L_0x031a:
        r0 = r113;
        r5 = r0.emptyStackTop;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        if (r10 != r5) goto L_0x0334;
    L_0x0322:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r8[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r9[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        goto L_0x00a9;
    L_0x0334:
        r0 = r113;
        r5 = r0.emptyStackTop;	 Catch:{ Throwable -> 0x00e8 }
        if (r10 == r5) goto L_0x00a9;
    L_0x033a:
        org.mozilla.javascript.Kit.codeBug();	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x033f:
        if (r78 == 0) goto L_0x0349;
    L_0x0341:
        r5 = 0;
        r0 = r112;
        r1 = r113;
        addInstructionCount(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0349:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r109 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 == r1) goto L_0x035b;
    L_0x0357:
        r114 = r109;
        goto L_0x019c;
    L_0x035b:
        r5 = r9[r20];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        if (r78 == 0) goto L_0x00a9;
    L_0x0364:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.pcPrevBranch = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x036e:
        r5 = 0;
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        goto L_0x00a9;
    L_0x0375:
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.result = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.resultDbl = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 0;
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        goto L_0x00a9;
    L_0x0388:
        r5 = r10 + 1;
        r6 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r8[r5] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 1;
        r11 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r9[r5] = r11;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        goto L_0x00a9;
    L_0x0398:
        r5 = r10 + 1;
        r6 = r10 + -1;
        r6 = r8[r6];	 Catch:{ Throwable -> 0x00e8 }
        r8[r5] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 1;
        r6 = r10 + -1;
        r11 = r9[r6];	 Catch:{ Throwable -> 0x00e8 }
        r9[r5] = r11;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 2;
        r6 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r8[r5] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 2;
        r11 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r9[r5] = r11;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 2;
        goto L_0x00a9;
    L_0x03b8:
        r89 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r5 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r8[r5] = r89;	 Catch:{ Throwable -> 0x00e8 }
        r64 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r5 = r9[r5];	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r9[r5] = r64;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x03d2:
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.result = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.resultDbl = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        goto L_0x0144;
    L_0x03e2:
        r0 = r106;
        r1 = r113;
        r1.result = r0;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0144;
    L_0x03ea:
        r0 = r113;
        r96 = stack_int32(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r96 ^ -1;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x03f9:
        r0 = r113;
        r10 = doBitOp(r0, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0401:
        r5 = r10 + -1;
        r0 = r113;
        r82 = stack_double(r0, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = stack_int32(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        r96 = r5 & 31;
        r10 = r10 + -1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r5 = org.mozilla.javascript.ScriptRuntime.toUint32(r82);	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 >>> r96;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0420:
        r0 = r113;
        r94 = stack_double(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 29;
        if (r7 != r5) goto L_0x0431;
    L_0x042c:
        r0 = r94;
        r0 = -r0;
        r94 = r0;
    L_0x0431:
        r9[r10] = r94;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0435:
        r10 = r10 + -1;
        r0 = r112;
        doAdd(r8, r9, r10, r0);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x043e:
        r0 = r113;
        r10 = doArithmetic(r0, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0446:
        r0 = r113;
        r5 = stack_boolean(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x0457;
    L_0x044e:
        r5 = 1;
    L_0x044f:
        r5 = org.mozilla.javascript.ScriptRuntime.wrapBoolean(r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0457:
        r5 = 0;
        goto L_0x044f;
    L_0x0459:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.bind(r0, r5, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x046b:
        r100 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r100;
        r1 = r54;
        if (r0 != r1) goto L_0x0479;
    L_0x0473:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r100 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0479:
        r10 = r10 + -1;
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = (org.mozilla.javascript.Scriptable) r85;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 8;
        if (r7 != r5) goto L_0x0497;
    L_0x0483:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r100;
        r2 = r112;
        r3 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.setName(r0, r1, r2, r5, r3);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0493:
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0497:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r100;
        r2 = r112;
        r3 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.strictSetName(r0, r1, r2, r5, r3);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0493;
    L_0x04a8:
        r100 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r100;
        r1 = r54;
        if (r0 != r1) goto L_0x04b6;
    L_0x04b0:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r100 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x04b6:
        r10 = r10 + -1;
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = (org.mozilla.javascript.Scriptable) r85;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r100;
        r2 = r112;
        r3 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.setConst(r0, r1, r2, r3);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x04cc:
        r5 = r112;
        r6 = r113;
        r10 = doDelName(r5, r6, r7, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x04d6:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x04e4;
    L_0x04de:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x04e4:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r104;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.getObjectPropNoWarn(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x04f6:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0504;
    L_0x04fe:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0504:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r104;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.getObjectProp(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0516:
        r100 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r100;
        r1 = r54;
        if (r0 != r1) goto L_0x0524;
    L_0x051e:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r100 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0524:
        r10 = r10 + -1;
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0534;
    L_0x052e:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0534:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r104;
        r2 = r100;
        r3 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.setObjectProp(r0, r1, r2, r3, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0548:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0556;
    L_0x0550:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0556:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r13[r6];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r104;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.propIncrDecr(r0, r1, r2, r5, r6);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0578:
        r0 = r112;
        r1 = r113;
        r10 = doGetElem(r0, r1, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0582:
        r0 = r112;
        r1 = r113;
        r10 = doSetElem(r0, r1, r8, r9, r10);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x058c:
        r11 = r112;
        r12 = r113;
        r14 = r8;
        r15 = r9;
        r16 = r10;
        r10 = doElemIncDec(r11, r12, r13, r14, r15, r16);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x059a:
        r98 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r98 = (org.mozilla.javascript.Ref) r98;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r98;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.refGet(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x05aa:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 != r1) goto L_0x05b8;
    L_0x05b2:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r109 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x05b8:
        r10 = r10 + -1;
        r98 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r98 = (org.mozilla.javascript.Ref) r98;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r98;
        r1 = r109;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.refSet(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x05d0:
        r98 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r98 = (org.mozilla.javascript.Ref) r98;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r98;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.refDel(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x05e0:
        r98 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r98 = (org.mozilla.javascript.Ref) r98;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r13[r6];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r98;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.refIncrDecr(r0, r1, r5, r6);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0604:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r5 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r9[r20];	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0616:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r5 = 0;
        r8[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0621:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r104;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.getNameFunctionAndThis(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r5 = org.mozilla.javascript.ScriptRuntime.lastStoredScriptable(r112);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x063b:
        r90 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r54;
        if (r0 != r1) goto L_0x0649;
    L_0x0643:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r90 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0649:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r104;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.getPropFunctionAndThis(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r5 = org.mozilla.javascript.ScriptRuntime.lastStoredScriptable(r112);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0663:
        r5 = r10 + -1;
        r90 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r54;
        if (r0 != r1) goto L_0x0675;
    L_0x066d:
        r5 = r10 + -1;
        r5 = r9[r5];	 Catch:{ Throwable -> 0x00e8 }
        r90 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0675:
        r76 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r76;
        r1 = r54;
        if (r0 != r1) goto L_0x0683;
    L_0x067d:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r76 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0683:
        r5 = r10 + -1;
        r0 = r113;
        r6 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r76;
        r2 = r112;
        r6 = org.mozilla.javascript.ScriptRuntime.getElemFunctionAndThis(r0, r1, r2, r6);	 Catch:{ Throwable -> 0x00e8 }
        r8[r5] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = org.mozilla.javascript.ScriptRuntime.lastStoredScriptable(r112);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x069d:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 != r1) goto L_0x06ab;
    L_0x06a5:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r109 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x06ab:
        r0 = r109;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.getValueFunctionAndThis(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r5 = org.mozilla.javascript.ScriptRuntime.lastStoredScriptable(r112);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x06bf:
        if (r78 == 0) goto L_0x06cb;
    L_0x06c1:
        r0 = r112;
        r5 = r0.instructionCount;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 100;
        r0 = r112;
        r0.instructionCount = r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x06cb:
        r14 = r112;
        r15 = r113;
        r16 = r8;
        r17 = r9;
        r18 = r10;
        r19 = r13;
        r10 = doCallSpecial(r14, r15, r16, r17, r18, r19, r20);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x06dd:
        if (r78 == 0) goto L_0x06e9;
    L_0x06df:
        r0 = r112;
        r5 = r0.instructionCount;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 100;
        r0 = r112;
        r0.instructionCount = r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x06e9:
        r5 = r20 + 1;
        r10 = r10 - r5;
        r70 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r70 = (org.mozilla.javascript.Callable) r70;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 1;
        r16 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r16 = (org.mozilla.javascript.Scriptable) r16;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 70;
        if (r7 != r5) goto L_0x0712;
    L_0x06fa:
        r5 = r10 + 2;
        r0 = r20;
        r93 = getArgsArray(r8, r9, r5, r0);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r70;
        r1 = r16;
        r2 = r93;
        r3 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.callRef(r0, r1, r2, r3);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0712:
        r0 = r113;
        r15 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.useActivation;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0724;
    L_0x071c:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r15 = org.mozilla.javascript.ScriptableObject.getTopLevelScope(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0724:
        r0 = r70;
        r5 = r0 instanceof org.mozilla.javascript.InterpretedFunction;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0773;
    L_0x072a:
        r0 = r70;
        r0 = (org.mozilla.javascript.InterpretedFunction) r0;	 Catch:{ Throwable -> 0x00e8 }
        r21 = r0;
        r0 = r113;
        r5 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r21;
        r6 = r0.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != r6) goto L_0x0773;
    L_0x073c:
        r22 = r113;
        r23 = new org.mozilla.javascript.Interpreter$CallFrame;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 0;
        r0 = r23;
        r0.m381init();	 Catch:{ Throwable -> 0x00e8 }
        r5 = -55;
        if (r7 != r5) goto L_0x0758;
    L_0x074a:
        r0 = r113;
        r0 = r0.parentFrame;	 Catch:{ Throwable -> 0x00e8 }
        r22 = r0;
        r5 = 0;
        r0 = r112;
        r1 = r113;
        exitFrame(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0758:
        r19 = r10 + 2;
        r14 = r112;
        r17 = r8;
        r18 = r9;
        initFrame(r14, r15, r16, r17, r18, r19, r20, r21, r22, r23);	 Catch:{ Throwable -> 0x00e8 }
        r5 = -55;
        if (r7 == r5) goto L_0x076f;
    L_0x0767:
        r0 = r113;
        r0.savedStackTop = r10;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.savedCallOp = r7;	 Catch:{ Throwable -> 0x00e8 }
    L_0x076f:
        r113 = r23;
        goto L_0x0054;
    L_0x0773:
        r0 = r70;
        r5 = r0 instanceof org.mozilla.javascript.NativeContinuation;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x07a3;
    L_0x0779:
        r63 = new org.mozilla.javascript.Interpreter$ContinuationJump;	 Catch:{ Throwable -> 0x00e8 }
        r70 = (org.mozilla.javascript.NativeContinuation) r70;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r63;
        r1 = r70;
        r2 = r113;
        r0.m382init(r1, r2);	 Catch:{ Throwable -> 0x00e8 }
        if (r20 != 0) goto L_0x0792;
    L_0x0788:
        r0 = r106;
        r1 = r63;
        r1.result = r0;	 Catch:{ Throwable -> 0x00e8 }
    L_0x078e:
        r114 = r63;
        goto L_0x019c;
    L_0x0792:
        r5 = r10 + 2;
        r5 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r63;
        r0.result = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 2;
        r5 = r9[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r63;
        r0.resultDbl = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x078e;
    L_0x07a3:
        r0 = r70;
        r5 = r0 instanceof org.mozilla.javascript.IdFunctionObject;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0802;
    L_0x07a9:
        r0 = r70;
        r0 = (org.mozilla.javascript.IdFunctionObject) r0;	 Catch:{ Throwable -> 0x00e8 }
        r21 = r0;
        r5 = org.mozilla.javascript.NativeContinuation.isContinuationConstructor(r21);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x07c8;
    L_0x07b5:
        r0 = r113;
        r5 = r0.stack;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.parentFrame;	 Catch:{ Throwable -> 0x00e8 }
        r11 = 0;
        r0 = r112;
        r6 = captureContinuation(r0, r6, r11);	 Catch:{ Throwable -> 0x00e8 }
        r5[r10] = r6;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x07c8:
        r5 = org.mozilla.javascript.BaseFunction.isApplyOrCall(r21);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0802;
    L_0x07ce:
        r61 = org.mozilla.javascript.ScriptRuntime.getCallable(r16);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r61;
        r5 = r0 instanceof org.mozilla.javascript.InterpretedFunction;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0802;
    L_0x07d8:
        r0 = r61;
        r0 = (org.mozilla.javascript.InterpretedFunction) r0;	 Catch:{ Throwable -> 0x00e8 }
        r33 = r0;
        r0 = r113;
        r5 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r33;
        r6 = r0.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != r6) goto L_0x0802;
    L_0x07ea:
        r24 = r112;
        r25 = r113;
        r26 = r20;
        r27 = r8;
        r28 = r9;
        r29 = r10;
        r30 = r7;
        r31 = r15;
        r32 = r21;
        r113 = initFrameForApplyOrCall(r24, r25, r26, r27, r28, r29, r30, r31, r32, r33);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0054;
    L_0x0802:
        r0 = r70;
        r5 = r0 instanceof org.mozilla.javascript.ScriptRuntime.NoSuchMethodShim;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0846;
    L_0x0808:
        r0 = r70;
        r0 = (org.mozilla.javascript.ScriptRuntime.NoSuchMethodShim) r0;	 Catch:{ Throwable -> 0x00e8 }
        r43 = r0;
        r0 = r43;
        r0 = r0.noSuchMethodMethod;	 Catch:{ Throwable -> 0x00e8 }
        r88 = r0;
        r0 = r88;
        r5 = r0 instanceof org.mozilla.javascript.InterpretedFunction;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0846;
    L_0x081a:
        r0 = r88;
        r0 = (org.mozilla.javascript.InterpretedFunction) r0;	 Catch:{ Throwable -> 0x00e8 }
        r21 = r0;
        r0 = r113;
        r5 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r21;
        r6 = r0.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != r6) goto L_0x0846;
    L_0x082c:
        r34 = r112;
        r35 = r113;
        r36 = r20;
        r37 = r8;
        r38 = r9;
        r39 = r10;
        r40 = r7;
        r41 = r16;
        r42 = r15;
        r44 = r21;
        r113 = initFrameForNoSuchMethod(r34, r35, r36, r37, r38, r39, r40, r41, r42, r43, r44);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0054;
    L_0x0846:
        r0 = r113;
        r1 = r112;
        r1.lastInterpreterFrame = r0;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.savedCallOp = r7;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.savedStackTop = r10;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + 2;
        r0 = r20;
        r5 = getArgsArray(r8, r9, r5, r0);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r70;
        r1 = r112;
        r2 = r16;
        r5 = r0.call(r1, r15, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x086a:
        if (r78 == 0) goto L_0x0876;
    L_0x086c:
        r0 = r112;
        r5 = r0.instructionCount;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 100;
        r0 = r112;
        r0.instructionCount = r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x0876:
        r10 = r10 - r20;
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r5 = r0 instanceof org.mozilla.javascript.InterpretedFunction;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x08cb;
    L_0x0880:
        r0 = r85;
        r0 = (org.mozilla.javascript.InterpretedFunction) r0;	 Catch:{ Throwable -> 0x00e8 }
        r51 = r0;
        r0 = r113;
        r5 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r51;
        r6 = r0.securityDomain;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != r6) goto L_0x08cb;
    L_0x0892:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r51;
        r1 = r112;
        r46 = r0.createObject(r1, r5);	 Catch:{ Throwable -> 0x00e8 }
        r23 = new org.mozilla.javascript.Interpreter$CallFrame;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 0;
        r0 = r23;
        r0.m381init();	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r45 = r0;
        r49 = r10 + 1;
        r44 = r112;
        r47 = r8;
        r48 = r9;
        r50 = r20;
        r52 = r113;
        r53 = r23;
        initFrame(r44, r45, r46, r47, r48, r49, r50, r51, r52, r53);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r46;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.savedStackTop = r10;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.savedCallOp = r7;	 Catch:{ Throwable -> 0x00e8 }
        r113 = r23;
        goto L_0x0054;
    L_0x08cb:
        r0 = r85;
        r5 = r0 instanceof org.mozilla.javascript.Function;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 != 0) goto L_0x08e2;
    L_0x08d1:
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x08dd;
    L_0x08d7:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x08dd:
        r5 = org.mozilla.javascript.ScriptRuntime.notFunctionError(r85);	 Catch:{ Throwable -> 0x00e8 }
        throw r5;	 Catch:{ Throwable -> 0x00e8 }
    L_0x08e2:
        r0 = r85;
        r0 = (org.mozilla.javascript.Function) r0;	 Catch:{ Throwable -> 0x00e8 }
        r70 = r0;
        r0 = r70;
        r5 = r0 instanceof org.mozilla.javascript.IdFunctionObject;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x090d;
    L_0x08ee:
        r0 = r70;
        r0 = (org.mozilla.javascript.IdFunctionObject) r0;	 Catch:{ Throwable -> 0x00e8 }
        r21 = r0;
        r5 = org.mozilla.javascript.NativeContinuation.isContinuationConstructor(r21);	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x090d;
    L_0x08fa:
        r0 = r113;
        r5 = r0.stack;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.parentFrame;	 Catch:{ Throwable -> 0x00e8 }
        r11 = 0;
        r0 = r112;
        r6 = captureContinuation(r0, r6, r11);	 Catch:{ Throwable -> 0x00e8 }
        r5[r10] = r6;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x090d:
        r5 = r10 + 1;
        r0 = r20;
        r93 = getArgsArray(r8, r9, r5, r0);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r70;
        r1 = r112;
        r2 = r93;
        r5 = r0.construct(r1, r5, r2);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0927:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0935;
    L_0x092f:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0935:
        r5 = org.mozilla.javascript.ScriptRuntime.typeof(r85);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x093d:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.typeofName(r5, r0);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x094d:
        r10 = r10 + 1;
        r8[r10] = r104;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0953:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = getShort(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x096e:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = getInt(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 4;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0989:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.itsDoubleTable;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5[r20];	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0999:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r104;
        r5 = org.mozilla.javascript.ScriptRuntime.name(r0, r5, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x09ab:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r13[r6];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r104;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.nameIncrDecr(r5, r0, r1, r6);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x09cd:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r5 + 1;
        r0 = r113;
        r0.pc = r6;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
    L_0x09d9:
        r24 = r113;
        r25 = r8;
        r26 = r9;
        r27 = r10;
        r31 = r20;
        r10 = doSetConstVar(r24, r25, r26, r27, r28, r29, r30, r31);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x09e9:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r5 + 1;
        r0 = r113;
        r0.pc = r6;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
    L_0x09f5:
        r24 = r113;
        r25 = r8;
        r26 = r9;
        r27 = r10;
        r31 = r20;
        r10 = doSetVar(r24, r25, r26, r27, r28, r29, r30, r31);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a05:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r6 = r5 + 1;
        r0 = r113;
        r0.pc = r6;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
    L_0x0a11:
        r34 = r113;
        r35 = r8;
        r36 = r9;
        r37 = r10;
        r38 = r28;
        r39 = r29;
        r40 = r20;
        r10 = doGetVar(r34, r35, r36, r37, r38, r39, r40);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a25:
        r34 = r112;
        r35 = r113;
        r36 = r8;
        r37 = r9;
        r38 = r10;
        r39 = r28;
        r40 = r29;
        r41 = r30;
        r42 = r20;
        r10 = doVarIncDec(r34, r35, r36, r37, r38, r39, r40, r41, r42);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a3d:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 0;
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a47:
        r10 = r10 + 1;
        r8[r10] = r54;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a51:
        r10 = r10 + 1;
        r5 = 0;
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a58:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.thisObj;	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a62:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a6c:
        r10 = r10 + 1;
        r5 = java.lang.Boolean.FALSE;	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a74:
        r10 = r10 + 1;
        r5 = java.lang.Boolean.TRUE;	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a7c:
        r10 = r10 + 1;
        r8[r10] = r106;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0a82:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0a90;
    L_0x0a8a:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0a90:
        r10 = r10 + -1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.enterWith(r0, r1, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.scope = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0aa4:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r5 = org.mozilla.javascript.ScriptRuntime.leaveWith(r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.scope = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0ab2:
        r10 = r10 + -1;
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.itsICode;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5[r6];	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0af2;
    L_0x0ac8:
        r60 = 1;
    L_0x0aca:
        r5 = r10 + 1;
        r62 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r62 = (java.lang.Throwable) r62;	 Catch:{ Throwable -> 0x00e8 }
        if (r60 != 0) goto L_0x0af5;
    L_0x0ad2:
        r84 = 0;
    L_0x0ad4:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r62;
        r1 = r84;
        r2 = r104;
        r3 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.newCatchScope(r0, r1, r2, r3, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0af2:
        r60 = 0;
        goto L_0x0aca;
    L_0x0af5:
        r84 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        r84 = (org.mozilla.javascript.Scriptable) r84;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0ad4;
    L_0x0afa:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0b08;
    L_0x0b02:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0b08:
        r10 = r10 + -1;
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r5 = 58;
        if (r7 != r5) goto L_0x0b28;
    L_0x0b14:
        r67 = 0;
    L_0x0b16:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r112;
        r2 = r67;
        r5 = org.mozilla.javascript.ScriptRuntime.enumInit(r0, r1, r5, r2);	 Catch:{ Throwable -> 0x00e8 }
        r8[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0b28:
        r5 = 59;
        if (r7 != r5) goto L_0x0b2f;
    L_0x0b2c:
        r67 = 1;
        goto L_0x0b16;
    L_0x0b2f:
        r67 = 2;
        goto L_0x0b16;
    L_0x0b32:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r107 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r5 = 61;
        if (r7 != r5) goto L_0x0b48;
    L_0x0b40:
        r5 = org.mozilla.javascript.ScriptRuntime.enumNext(r107);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0b44:
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0b48:
        r0 = r107;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.enumId(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0b44;
    L_0x0b51:
        r90 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r54;
        if (r0 != r1) goto L_0x0b5f;
    L_0x0b59:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r90 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0b5f:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r90;
        r1 = r104;
        r2 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.specialRef(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0b71:
        r0 = r112;
        r1 = r20;
        r10 = doRefMember(r0, r8, r9, r10, r1);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0b7b:
        r0 = r112;
        r1 = r20;
        r10 = doRefNsMember(r0, r8, r9, r10, r1);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0b85:
        r87 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r87;
        r1 = r54;
        if (r0 != r1) goto L_0x0b93;
    L_0x0b8d:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r87 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0b93:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r87;
        r1 = r112;
        r2 = r20;
        r5 = org.mozilla.javascript.ScriptRuntime.nameRef(r0, r1, r5, r2);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0ba5:
        r34 = r112;
        r35 = r113;
        r36 = r8;
        r37 = r9;
        r38 = r10;
        r39 = r20;
        r10 = doRefNsName(r34, r35, r36, r37, r38, r39);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0bb7:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r5 = r8[r20];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (org.mozilla.javascript.Scriptable) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.scope = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0bc7:
        r0 = r113;
        r5 = r0.localShift;	 Catch:{ Throwable -> 0x00e8 }
        r20 = r20 + r5;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r8[r20] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0bd5:
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r20;
        r5 = org.mozilla.javascript.InterpretedFunction.createFunction(r0, r5, r6, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0beb:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.fnOrScript;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r20;
        initFunction(r0, r5, r6, r1);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0bfc:
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.itsRegExpLiterals;	 Catch:{ Throwable -> 0x00e8 }
        r97 = r5[r20];	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r97;
        r5 = org.mozilla.javascript.ScriptRuntime.wrapRegExp(r0, r5, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0c16:
        r10 = r10 + 1;
        r0 = r20;
        r5 = new int[r0];	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + 1;
        r0 = r20;
        r5 = new java.lang.Object[r0];	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = 0;
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0c2c:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 != r1) goto L_0x0c3a;
    L_0x0c34:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r109 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0c3a:
        r10 = r10 + -1;
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = (int) r5;	 Catch:{ Throwable -> 0x00e8 }
        r75 = r0;
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5[r75] = r109;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r75 + 1;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0c50:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = (int) r5;	 Catch:{ Throwable -> 0x00e8 }
        r75 = r0;
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5[r75] = r109;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r5 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r6 = -1;
        r5[r75] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r75 + 1;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0c73:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r10 = r10 + -1;
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = (int) r5;	 Catch:{ Throwable -> 0x00e8 }
        r75 = r0;
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5[r75] = r109;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r10 + -1;
        r5 = r8[r5];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r6 = 1;
        r5[r75] = r6;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r75 + 1;
        r5 = (double) r5;	 Catch:{ Throwable -> 0x00e8 }
        r9[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0c96:
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5;
        r0 = (java.lang.Object[]) r0;	 Catch:{ Throwable -> 0x00e8 }
        r66 = r0;
        r10 = r10 + -1;
        r5 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5;
        r0 = (int[]) r0;	 Catch:{ Throwable -> 0x00e8 }
        r74 = r0;
        r5 = 66;
        if (r7 != r5) goto L_0x0cd1;
    L_0x0cae:
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.literalIds;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5[r20];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (java.lang.Object[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5;
        r0 = (java.lang.Object[]) r0;	 Catch:{ Throwable -> 0x00e8 }
        r77 = r0;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r77;
        r1 = r66;
        r2 = r74;
        r3 = r112;
        r107 = org.mozilla.javascript.ScriptRuntime.newObjectLiteral(r0, r1, r2, r3, r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0ccd:
        r8[r10] = r107;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0cd1:
        r101 = 0;
        r5 = -31;
        if (r7 != r5) goto L_0x0ce6;
    L_0x0cd7:
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.literalIds;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5[r20];	 Catch:{ Throwable -> 0x00e8 }
        r5 = (int[]) r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5;
        r0 = (int[]) r0;	 Catch:{ Throwable -> 0x00e8 }
        r101 = r0;
    L_0x0ce6:
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r66;
        r1 = r101;
        r2 = r112;
        r107 = org.mozilla.javascript.ScriptRuntime.newArrayLiteral(r0, r1, r2, r5);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0ccd;
    L_0x0cf5:
        r85 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r1 = r54;
        if (r0 != r1) goto L_0x0d03;
    L_0x0cfd:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r85 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0d03:
        r10 = r10 + -1;
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r85;
        r5 = org.mozilla.javascript.ScriptRuntime.enterDotQuery(r0, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.scope = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d15:
        r0 = r113;
        r108 = stack_boolean(r0, r10);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r108;
        r110 = org.mozilla.javascript.ScriptRuntime.updateDotQuery(r0, r5);	 Catch:{ Throwable -> 0x00e8 }
        if (r110 == 0) goto L_0x0d41;
    L_0x0d27:
        r8[r10] = r110;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.scope;	 Catch:{ Throwable -> 0x00e8 }
        r5 = org.mozilla.javascript.ScriptRuntime.leaveDotQuery(r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.scope = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d41:
        r10 = r10 + -1;
        goto L_0x02e1;
    L_0x0d45:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 != r1) goto L_0x0d53;
    L_0x0d4d:
        r5 = r9[r10];	 Catch:{ Throwable -> 0x00e8 }
        r109 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r5);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0d53:
        r0 = r109;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.setDefaultNamespace(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d5f:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 == r1) goto L_0x00a9;
    L_0x0d67:
        r0 = r109;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.escapeAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d73:
        r109 = r8[r10];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r109;
        r1 = r54;
        if (r0 == r1) goto L_0x00a9;
    L_0x0d7b:
        r0 = r109;
        r1 = r112;
        r5 = org.mozilla.javascript.ScriptRuntime.escapeTextValue(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
        r8[r10] = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d87:
        r0 = r113;
        r5 = r0.debuggerFrame;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x00a9;
    L_0x0d8d:
        r0 = r113;
        r5 = r0.debuggerFrame;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r5.onDebuggerStatement(r0);	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0d98:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.pcSourceLineStart = r5;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.debuggerFrame;	 Catch:{ Throwable -> 0x00e8 }
        if (r5 == 0) goto L_0x0db9;
    L_0x0da6:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r86 = getIndex(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.debuggerFrame;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r112;
        r1 = r86;
        r5.onLineChange(r0, r1);	 Catch:{ Throwable -> 0x00e8 }
    L_0x0db9:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0dc5:
        r20 = 0;
        goto L_0x00a9;
    L_0x0dc9:
        r20 = 1;
        goto L_0x00a9;
    L_0x0dcd:
        r20 = 2;
        goto L_0x00a9;
    L_0x0dd1:
        r20 = 3;
        goto L_0x00a9;
    L_0x0dd5:
        r20 = 4;
        goto L_0x00a9;
    L_0x0dd9:
        r20 = 5;
        goto L_0x00a9;
    L_0x0ddd:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r5 & 255;
        r20 = r0;
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0df3:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r20 = getIndex(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e07:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r20 = getInt(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 4;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e1b:
        r5 = 0;
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e20:
        r5 = 1;
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e25:
        r5 = 2;
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e2a:
        r5 = 3;
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e2f:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r13[r5];	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 & 255;
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 1;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e45:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = getIndex(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 2;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e5b:
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = getInt(r13, r5);	 Catch:{ Throwable -> 0x00e8 }
        r104 = r105[r5];	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r5 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5 + 4;
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x00a9;
    L_0x0e71:
        r0 = r113;
        r5 = r0.idata;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.longJumps;	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r6 = r0.pc;	 Catch:{ Throwable -> 0x00e8 }
        r5 = r5.getExistingInt(r6);	 Catch:{ Throwable -> 0x00e8 }
        r0 = r113;
        r0.pc = r5;	 Catch:{ Throwable -> 0x00e8 }
        goto L_0x0300;
    L_0x0e85:
        r5 = r111;
    L_0x0e87:
        r0 = r112;
        r6 = r0.previousInterpreterInvocations;
        if (r6 == 0) goto L_0x0f90;
    L_0x0e8d:
        r0 = r112;
        r6 = r0.previousInterpreterInvocations;
        r6 = r6.size();
        if (r6 == 0) goto L_0x0f90;
    L_0x0e97:
        r0 = r112;
        r6 = r0.previousInterpreterInvocations;
        r6 = r6.pop();
        r0 = r112;
        r0.lastInterpreterFrame = r6;
    L_0x0ea3:
        if (r5 == 0) goto L_0x0f9f;
    L_0x0ea5:
        r6 = r5 instanceof java.lang.RuntimeException;
        if (r6 == 0) goto L_0x0f9c;
    L_0x0ea9:
        r5 = (java.lang.RuntimeException) r5;
        throw r5;
    L_0x0eac:
        r114 = r68;
        goto L_0x019c;
    L_0x0eb0:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.JavaScriptException;
        if (r5 == 0) goto L_0x0eba;
    L_0x0eb6:
        r69 = 2;
        goto L_0x01bc;
    L_0x0eba:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.EcmaError;
        if (r5 == 0) goto L_0x0ec4;
    L_0x0ec0:
        r69 = 2;
        goto L_0x01bc;
    L_0x0ec4:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.EvaluatorException;
        if (r5 == 0) goto L_0x0ece;
    L_0x0eca:
        r69 = 2;
        goto L_0x01bc;
    L_0x0ece:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.ContinuationPending;
        if (r5 == 0) goto L_0x0ed8;
    L_0x0ed4:
        r69 = 0;
        goto L_0x01bc;
    L_0x0ed8:
        r0 = r114;
        r5 = r0 instanceof java.lang.RuntimeException;
        if (r5 == 0) goto L_0x0eef;
    L_0x0ede:
        r5 = 13;
        r0 = r112;
        r5 = r0.hasFeature(r5);
        if (r5 == 0) goto L_0x0eec;
    L_0x0ee8:
        r69 = 2;
    L_0x0eea:
        goto L_0x01bc;
    L_0x0eec:
        r69 = 1;
        goto L_0x0eea;
    L_0x0eef:
        r0 = r114;
        r5 = r0 instanceof java.lang.Error;
        if (r5 == 0) goto L_0x0f06;
    L_0x0ef5:
        r5 = 13;
        r0 = r112;
        r5 = r0.hasFeature(r5);
        if (r5 == 0) goto L_0x0f03;
    L_0x0eff:
        r69 = 2;
    L_0x0f01:
        goto L_0x01bc;
    L_0x0f03:
        r69 = 0;
        goto L_0x0f01;
    L_0x0f06:
        r0 = r114;
        r5 = r0 instanceof org.mozilla.javascript.Interpreter.ContinuationJump;
        if (r5 == 0) goto L_0x0f14;
    L_0x0f0c:
        r69 = 1;
        r63 = r114;
        r63 = (org.mozilla.javascript.Interpreter.ContinuationJump) r63;
        goto L_0x01bc;
    L_0x0f14:
        r5 = 13;
        r0 = r112;
        r5 = r0.hasFeature(r5);
        if (r5 == 0) goto L_0x0f22;
    L_0x0f1e:
        r69 = 2;
    L_0x0f20:
        goto L_0x01bc;
    L_0x0f22:
        r69 = 1;
        goto L_0x0f20;
    L_0x0f25:
        r68 = move-exception;
        r114 = r68;
        r69 = 1;
        r5 = r114;
        goto L_0x01c9;
    L_0x0f2e:
        r68 = move-exception;
        r114 = r68;
        r63 = 0;
        r69 = 0;
        r5 = r114;
        goto L_0x01c9;
    L_0x0f39:
        r68 = move-exception;
        r114 = r68;
        r63 = 0;
        r69 = 0;
        r5 = r114;
        goto L_0x01e2;
    L_0x0f44:
        r92 = 0;
        goto L_0x01eb;
    L_0x0f48:
        r0 = r112;
        r1 = r113;
        exitFrame(r0, r1, r5);
        r0 = r113;
        r0 = r0.parentFrame;
        r113 = r0;
        if (r113 != 0) goto L_0x0f6e;
    L_0x0f57:
        if (r63 == 0) goto L_0x0e87;
    L_0x0f59:
        r0 = r63;
        r6 = r0.branchFrame;
        if (r6 == 0) goto L_0x0f62;
    L_0x0f5f:
        org.mozilla.javascript.Kit.codeBug();
    L_0x0f62:
        r0 = r63;
        r6 = r0.capturedFrame;
        if (r6 == 0) goto L_0x0f7e;
    L_0x0f68:
        r20 = -1;
        r111 = r5;
        goto L_0x0054;
    L_0x0f6e:
        if (r63 == 0) goto L_0x01e2;
    L_0x0f70:
        r0 = r63;
        r6 = r0.branchFrame;
        r0 = r113;
        if (r6 != r0) goto L_0x01e2;
    L_0x0f78:
        r20 = -1;
        r111 = r5;
        goto L_0x0054;
    L_0x0f7e:
        r0 = r63;
        r0 = r0.result;
        r79 = r0;
        r0 = r63;
        r0 = r0.resultDbl;
        r80 = r0;
        r114 = 0;
        r5 = r114;
        goto L_0x0e87;
    L_0x0f90:
        r6 = 0;
        r0 = r112;
        r0.lastInterpreterFrame = r6;
        r6 = 0;
        r0 = r112;
        r0.previousInterpreterInvocations = r6;
        goto L_0x0ea3;
    L_0x0f9c:
        r5 = (java.lang.Error) r5;
        throw r5;
    L_0x0f9f:
        r0 = r79;
        r1 = r54;
        if (r0 != r1) goto L_0x018b;
    L_0x0fa5:
        r79 = org.mozilla.javascript.ScriptRuntime.wrapNumber(r80);
        goto L_0x018b;
    L_0x0fab:
        r68 = move-exception;
        r111 = r114;
        goto L_0x00e9;
    L_0x0fb0:
        r10 = r103;
        goto L_0x02e1;
    L_0x0fb4:
        r5 = r114;
        goto L_0x01c9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.Interpreter.interpretLoop(org.mozilla.javascript.Context, org.mozilla.javascript.Interpreter$CallFrame, java.lang.Object):java.lang.Object");
    }

    private static int doInOrInstanceof(Context cx, int op, Object[] stack, double[] sDbl, int stackTop) {
        boolean valBln;
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        if (op == 52) {
            valBln = ScriptRuntime.in(lhs, rhs, cx);
        } else {
            valBln = ScriptRuntime.instanceOf(lhs, rhs, cx);
        }
        stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
        return stackTop;
    }

    private static int doCompare(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        double rDbl;
        double stack_double;
        boolean valBln = true;
        stackTop--;
        Object rhs = stack[stackTop + 1];
        UniqueTag lhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rDbl = sDbl[stackTop + 1];
            stack_double = stack_double(frame, stackTop);
        } else if (lhs == UniqueTag.DOUBLE_MARK) {
            rDbl = ScriptRuntime.toNumber(rhs);
            stack_double = sDbl[stackTop];
        } else {
            switch (op) {
                case 14:
                    valBln = ScriptRuntime.cmp_LT(lhs, rhs);
                    break;
                case 15:
                    valBln = ScriptRuntime.cmp_LE(lhs, rhs);
                    break;
                case 16:
                    valBln = ScriptRuntime.cmp_LT(rhs, lhs);
                    break;
                case 17:
                    valBln = ScriptRuntime.cmp_LE(rhs, lhs);
                    break;
                default:
                    throw Kit.codeBug();
            }
            stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
            return stackTop;
        }
        switch (op) {
            case 14:
                if (stack_double >= rDbl) {
                    valBln = false;
                    break;
                }
                break;
            case 15:
                if (stack_double > rDbl) {
                    valBln = false;
                    break;
                }
                break;
            case 16:
                if (stack_double <= rDbl) {
                    valBln = false;
                    break;
                }
                break;
            case 17:
                if (stack_double < rDbl) {
                    valBln = false;
                    break;
                }
                break;
            default:
                throw Kit.codeBug();
        }
        stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
        return stackTop;
    }

    private static int doBitOp(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        int lIntValue = stack_int32(frame, stackTop - 1);
        int rIntValue = stack_int32(frame, stackTop);
        stackTop--;
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        switch (op) {
            case 9:
                lIntValue |= rIntValue;
                break;
            case 10:
                lIntValue ^= rIntValue;
                break;
            case 11:
                lIntValue &= rIntValue;
                break;
            case 18:
                lIntValue <<= rIntValue;
                break;
            case 19:
                lIntValue >>= rIntValue;
                break;
        }
        sDbl[stackTop] = (double) lIntValue;
        return stackTop;
    }

    private static int doDelName(Context cx, CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.delete(lhs, rhs, cx, frame.scope, op == 0);
        return stackTop;
    }

    private static int doGetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
        Object value;
        stackTop--;
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        UniqueTag id = stack[stackTop + 1];
        if (id != UniqueTag.DOUBLE_MARK) {
            value = ScriptRuntime.getObjectElem(lhs, id, cx, frame.scope);
        } else {
            value = ScriptRuntime.getObjectIndex(lhs, sDbl[stackTop + 1], cx, frame.scope);
        }
        stack[stackTop] = value;
        return stackTop;
    }

    private static int doSetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop) {
        Object value;
        stackTop -= 2;
        Object rhs = stack[stackTop + 2];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]);
        }
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        UniqueTag id = stack[stackTop + 1];
        if (id != UniqueTag.DOUBLE_MARK) {
            value = ScriptRuntime.setObjectElem(lhs, id, rhs, cx, frame.scope);
        } else {
            value = ScriptRuntime.setObjectIndex(lhs, sDbl[stackTop + 1], rhs, cx, frame.scope);
        }
        stack[stackTop] = value;
        return stackTop;
    }

    private static int doElemIncDec(Context cx, CallFrame frame, byte[] iCode, Object[] stack, double[] sDbl, int stackTop) {
        Object rhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object lhs = stack[stackTop];
        if (lhs == UniqueTag.DOUBLE_MARK) {
            lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.elemIncrDecr(lhs, rhs, cx, frame.scope, iCode[frame.pc]);
        frame.pc++;
        return stackTop;
    }

    private static int doCallSpecial(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, byte[] iCode, int indexReg) {
        int callType = iCode[frame.pc] & ByteCode.IMPDEP2;
        boolean isNew = iCode[frame.pc + 1] != (byte) 0;
        int sourceLine = getIndex(iCode, frame.pc + 2);
        if (isNew) {
            stackTop -= indexReg;
            Object function = stack[stackTop];
            if (function == UniqueTag.DOUBLE_MARK) {
                function = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            stack[stackTop] = ScriptRuntime.newSpecial(cx, function, getArgsArray(stack, sDbl, stackTop + 1, indexReg), frame.scope, callType);
        } else {
            stackTop -= indexReg + 1;
            stack[stackTop] = ScriptRuntime.callSpecial(cx, stack[stackTop], stack[stackTop + 1], getArgsArray(stack, sDbl, stackTop + 2, indexReg), frame.scope, frame.thisObj, callType, frame.idata.itsSourceFile, sourceLine);
        }
        frame.pc += 4;
        return stackTop;
    }

    private static int doSetConstVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
        if (frame.useActivation) {
            Object val = stack[stackTop];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            String stringReg = frame.idata.argNames[indexReg];
            if (frame.scope instanceof ConstProperties) {
                frame.scope.putConst(stringReg, frame.scope, val);
            } else {
                throw Kit.codeBug();
            }
        } else if ((varAttributes[indexReg] & 1) == 0) {
            throw Context.reportRuntimeError1("msg.var.redecl", frame.idata.argNames[indexReg]);
        } else if ((varAttributes[indexReg] & 8) != 0) {
            vars[indexReg] = stack[stackTop];
            varAttributes[indexReg] = varAttributes[indexReg] & -9;
            varDbls[indexReg] = sDbl[stackTop];
        }
        return stackTop;
    }

    private static int doSetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
        if (frame.useActivation) {
            Object val = stack[stackTop];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            frame.scope.put(frame.idata.argNames[indexReg], frame.scope, val);
        } else if ((varAttributes[indexReg] & 1) == 0) {
            vars[indexReg] = stack[stackTop];
            varDbls[indexReg] = sDbl[stackTop];
        }
        return stackTop;
    }

    private static int doGetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int indexReg) {
        stackTop++;
        if (frame.useActivation) {
            stack[stackTop] = frame.scope.get(frame.idata.argNames[indexReg], frame.scope);
        } else {
            stack[stackTop] = vars[indexReg];
            sDbl[stackTop] = varDbls[indexReg];
        }
        return stackTop;
    }

    private static int doVarIncDec(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg) {
        stackTop++;
        int incrDecrMask = frame.idata.itsICode[frame.pc];
        if (frame.useActivation) {
            stack[stackTop] = ScriptRuntime.nameIncrDecr(frame.scope, frame.idata.argNames[indexReg], cx, incrDecrMask);
        } else {
            double d;
            UniqueTag varValue = vars[indexReg];
            if (varValue == UniqueTag.DOUBLE_MARK) {
                d = varDbls[indexReg];
            } else {
                d = ScriptRuntime.toNumber((Object) varValue);
            }
            double d2 = (incrDecrMask & 1) == 0 ? d + 1.0d : d - 1.0d;
            boolean post = (incrDecrMask & 2) != 0;
            if ((varAttributes[indexReg] & 1) == 0) {
                if (varValue != UniqueTag.DOUBLE_MARK) {
                    vars[indexReg] = UniqueTag.DOUBLE_MARK;
                }
                varDbls[indexReg] = d2;
                stack[stackTop] = UniqueTag.DOUBLE_MARK;
                if (!post) {
                    d = d2;
                }
                sDbl[stackTop] = d;
            } else if (!post || varValue == UniqueTag.DOUBLE_MARK) {
                stack[stackTop] = UniqueTag.DOUBLE_MARK;
                if (!post) {
                    d = d2;
                }
                sDbl[stackTop] = d;
            } else {
                stack[stackTop] = varValue;
            }
        }
        frame.pc++;
        return stackTop;
    }

    private static int doRefMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags) {
        Object elem = stack[stackTop];
        if (elem == UniqueTag.DOUBLE_MARK) {
            elem = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object obj = stack[stackTop];
        if (obj == UniqueTag.DOUBLE_MARK) {
            obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.memberRef(obj, elem, cx, flags);
        return stackTop;
    }

    private static int doRefNsMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags) {
        Object elem = stack[stackTop];
        if (elem == UniqueTag.DOUBLE_MARK) {
            elem = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object ns = stack[stackTop];
        if (ns == UniqueTag.DOUBLE_MARK) {
            ns = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object obj = stack[stackTop];
        if (obj == UniqueTag.DOUBLE_MARK) {
            obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.memberRef(obj, ns, elem, cx, flags);
        return stackTop;
    }

    private static int doRefNsName(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, int flags) {
        Object name = stack[stackTop];
        if (name == UniqueTag.DOUBLE_MARK) {
            name = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stackTop--;
        Object ns = stack[stackTop];
        if (ns == UniqueTag.DOUBLE_MARK) {
            ns = ScriptRuntime.wrapNumber(sDbl[stackTop]);
        }
        stack[stackTop] = ScriptRuntime.nameRef(ns, name, cx, frame.scope, flags);
        return stackTop;
    }

    private static CallFrame initFrameForNoSuchMethod(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable funThisObj, Scriptable calleeScope, NoSuchMethodShim noSuchMethodShim, InterpretedFunction ifun) {
        int shift = stackTop + 2;
        Object[] elements = new Object[indexReg];
        int i = 0;
        while (i < indexReg) {
            Object val = stack[shift];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[shift]);
            }
            elements[i] = val;
            i++;
            shift++;
        }
        Object[] argsArray = new Object[]{noSuchMethodShim.methodName, cx.newArray(calleeScope, elements)};
        CallFrame callParentFrame = frame;
        CallFrame calleeFrame = new CallFrame();
        if (op == -55) {
            callParentFrame = frame.parentFrame;
            exitFrame(cx, frame, null);
        }
        initFrame(cx, calleeScope, funThisObj, argsArray, null, 0, 2, ifun, callParentFrame, calleeFrame);
        if (op != -55) {
            frame.savedStackTop = stackTop;
            frame.savedCallOp = op;
        }
        return calleeFrame;
    }

    private static boolean doEquals(Object[] stack, double[] sDbl, int stackTop) {
        UniqueTag rhs = stack[stackTop + 1];
        UniqueTag lhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            if (lhs == UniqueTag.DOUBLE_MARK) {
                return sDbl[stackTop] == sDbl[stackTop + 1];
            } else {
                return ScriptRuntime.eqNumber(sDbl[stackTop + 1], lhs);
            }
        } else if (lhs == UniqueTag.DOUBLE_MARK) {
            return ScriptRuntime.eqNumber(sDbl[stackTop], rhs);
        } else {
            return ScriptRuntime.eq(lhs, rhs);
        }
    }

    private static boolean doShallowEquals(Object[] stack, double[] sDbl, int stackTop) {
        double rdbl;
        double ldbl;
        UniqueTag rhs = stack[stackTop + 1];
        UniqueTag lhs = stack[stackTop];
        UniqueTag DBL_MRK = UniqueTag.DOUBLE_MARK;
        if (rhs == DBL_MRK) {
            rdbl = sDbl[stackTop + 1];
            if (lhs == DBL_MRK) {
                ldbl = sDbl[stackTop];
            } else if (!(lhs instanceof Number)) {
                return false;
            } else {
                ldbl = ((Number) lhs).doubleValue();
            }
        } else if (lhs != DBL_MRK) {
            return ScriptRuntime.shallowEq(lhs, rhs);
        } else {
            ldbl = sDbl[stackTop];
            if (!(rhs instanceof Number)) {
                return false;
            }
            rdbl = ((Number) rhs).doubleValue();
        }
        if (ldbl == rdbl) {
            return true;
        }
        return false;
    }

    private static CallFrame processThrowable(Context cx, Object throwable, CallFrame frame, int indexReg, boolean instructionCounting) {
        if (indexReg >= 0) {
            if (frame.frozen) {
                frame = frame.cloneFrozen();
            }
            int[] table = frame.idata.itsExceptionTable;
            frame.pc = table[indexReg + 2];
            if (instructionCounting) {
                frame.pcPrevBranch = frame.pc;
            }
            frame.savedStackTop = frame.emptyStackTop;
            int exLocal = frame.localShift + table[indexReg + 4];
            frame.scope = (Scriptable) frame.stack[frame.localShift + table[indexReg + 5]];
            frame.stack[exLocal] = throwable;
            throwable = null;
        } else {
            ContinuationJump cjump = (ContinuationJump) throwable;
            throwable = null;
            if (cjump.branchFrame != frame) {
                Kit.codeBug();
            }
            if (cjump.capturedFrame == null) {
                Kit.codeBug();
            }
            int rewindCount = cjump.capturedFrame.frameIndex + 1;
            if (cjump.branchFrame != null) {
                rewindCount -= cjump.branchFrame.frameIndex;
            }
            int enterCount = 0;
            CallFrame[] enterFrames = null;
            CallFrame x = cjump.capturedFrame;
            for (int i = 0; i != rewindCount; i++) {
                if (!x.frozen) {
                    Kit.codeBug();
                }
                if (isFrameEnterExitRequired(x)) {
                    if (enterFrames == null) {
                        enterFrames = new CallFrame[(rewindCount - i)];
                    }
                    enterFrames[enterCount] = x;
                    enterCount++;
                }
                x = x.parentFrame;
            }
            while (enterCount != 0) {
                enterCount--;
                enterFrame(cx, enterFrames[enterCount], ScriptRuntime.emptyArgs, true);
            }
            frame = cjump.capturedFrame.cloneFrozen();
            setCallResult(frame, cjump.result, cjump.resultDbl);
        }
        frame.throwable = throwable;
        return frame;
    }

    private static Object freezeGenerator(Context cx, CallFrame frame, int stackTop, GeneratorState generatorState) {
        if (generatorState.operation == 2) {
            throw ScriptRuntime.typeError0("msg.yield.closing");
        }
        frame.frozen = true;
        frame.result = frame.stack[stackTop];
        frame.resultDbl = frame.sDbl[stackTop];
        frame.savedStackTop = stackTop;
        frame.pc--;
        ScriptRuntime.exitActivationFunction(cx);
        if (frame.result != UniqueTag.DOUBLE_MARK) {
            return frame.result;
        }
        return ScriptRuntime.wrapNumber(frame.resultDbl);
    }

    private static Object thawGenerator(CallFrame frame, int stackTop, GeneratorState generatorState, int op) {
        frame.frozen = false;
        int sourceLine = getIndex(frame.idata.itsICode, frame.pc);
        frame.pc += 2;
        if (generatorState.operation == 1) {
            return new JavaScriptException(generatorState.value, frame.idata.itsSourceFile, sourceLine);
        }
        if (generatorState.operation == 2) {
            return generatorState.value;
        }
        if (generatorState.operation != 0) {
            throw Kit.codeBug();
        }
        if (op == 72) {
            frame.stack[stackTop] = generatorState.value;
        }
        return Scriptable.NOT_FOUND;
    }

    private static CallFrame initFrameForApplyOrCall(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable calleeScope, IdFunctionObject ifun, InterpretedFunction iApplyCallable) {
        Scriptable applyThis;
        if (indexReg != 0) {
            Object obj = stack[stackTop + 2];
            if (obj == UniqueTag.DOUBLE_MARK) {
                obj = ScriptRuntime.wrapNumber(sDbl[stackTop + 2]);
            }
            applyThis = ScriptRuntime.toObjectOrNull(cx, obj, frame.scope);
        } else {
            applyThis = null;
        }
        if (applyThis == null) {
            applyThis = ScriptRuntime.getTopCallScope(cx);
        }
        if (op == -55) {
            exitFrame(cx, frame, null);
            frame = frame.parentFrame;
        } else {
            frame.savedStackTop = stackTop;
            frame.savedCallOp = op;
        }
        CallFrame calleeFrame = new CallFrame();
        if (BaseFunction.isApply(ifun)) {
            Object[] callArgs;
            if (indexReg < 2) {
                callArgs = ScriptRuntime.emptyArgs;
            } else {
                callArgs = ScriptRuntime.getApplyArguments(cx, stack[stackTop + 3]);
            }
            initFrame(cx, calleeScope, applyThis, callArgs, null, 0, callArgs.length, iApplyCallable, frame, calleeFrame);
        } else {
            for (int i = 1; i < indexReg; i++) {
                stack[(stackTop + 1) + i] = stack[(stackTop + 2) + i];
                sDbl[(stackTop + 1) + i] = sDbl[(stackTop + 2) + i];
            }
            initFrame(cx, calleeScope, applyThis, stack, sDbl, stackTop + 2, indexReg < 2 ? 0 : indexReg - 1, iApplyCallable, frame, calleeFrame);
        }
        return calleeFrame;
    }

    private static void initFrame(Context cx, Scriptable callerScope, Scriptable thisObj, Object[] args, double[] argsDbl, int argShift, int argCount, InterpretedFunction fnOrScript, CallFrame parentFrame, CallFrame frame) {
        Object args2;
        Object argsDbl2;
        Scriptable scope;
        int i;
        boolean stackReuse;
        Object[] stack;
        int[] stackAttributes;
        double[] sDbl;
        InterpreterData idata = fnOrScript.idata;
        boolean useActivation = idata.itsNeedsActivation;
        DebugFrame debuggerFrame = null;
        if (cx.debugger != null) {
            debuggerFrame = cx.debugger.getFrame(cx, idata);
            if (debuggerFrame != null) {
                useActivation = true;
            }
        }
        if (useActivation) {
            if (argsDbl2 != null) {
                args2 = getArgsArray(args, argsDbl2, argShift, argCount);
            }
            argShift = 0;
            argsDbl2 = null;
        }
        if (idata.itsFunctionType != 0) {
            scope = fnOrScript.getParentScope();
            if (useActivation) {
                scope = ScriptRuntime.createFunctionActivation(fnOrScript, scope, args2);
            }
        } else {
            scope = callerScope;
            ScriptRuntime.initScript(fnOrScript, thisObj, cx, scope, fnOrScript.idata.evalScriptFlag);
        }
        if (idata.itsNestedFunctions != null) {
            if (!(idata.itsFunctionType == 0 || idata.itsNeedsActivation)) {
                Kit.codeBug();
            }
            for (i = 0; i < idata.itsNestedFunctions.length; i++) {
                if (idata.itsNestedFunctions[i].itsFunctionType == 1) {
                    initFunction(cx, scope, fnOrScript, i);
                }
            }
        }
        int emptyStackTop = (idata.itsMaxVars + idata.itsMaxLocals) - 1;
        int maxFrameArray = idata.itsMaxFrameArray;
        if (maxFrameArray != (idata.itsMaxStack + emptyStackTop) + 1) {
            Kit.codeBug();
        }
        if (frame.stack == null || maxFrameArray > frame.stack.length) {
            stackReuse = false;
            stack = new Object[maxFrameArray];
            stackAttributes = new int[maxFrameArray];
            sDbl = new double[maxFrameArray];
        } else {
            stackReuse = true;
            stack = frame.stack;
            stackAttributes = frame.stackAttributes;
            sDbl = frame.sDbl;
        }
        int varCount = idata.getParamAndVarCount();
        for (i = 0; i < varCount; i++) {
            if (idata.getParamOrVarConst(i)) {
                stackAttributes[i] = 13;
            }
        }
        int definedArgs = idata.argCount;
        if (definedArgs > argCount) {
            definedArgs = argCount;
        }
        frame.parentFrame = parentFrame;
        frame.frameIndex = parentFrame == null ? 0 : parentFrame.frameIndex + 1;
        if (frame.frameIndex > cx.getMaximumInterpreterStackDepth()) {
            throw Context.reportRuntimeError("Exceeded maximum stack depth");
        }
        frame.frozen = false;
        frame.fnOrScript = fnOrScript;
        frame.idata = idata;
        frame.stack = stack;
        frame.stackAttributes = stackAttributes;
        frame.sDbl = sDbl;
        frame.varSource = frame;
        frame.localShift = idata.itsMaxVars;
        frame.emptyStackTop = emptyStackTop;
        frame.debuggerFrame = debuggerFrame;
        frame.useActivation = useActivation;
        frame.thisObj = thisObj;
        frame.result = Undefined.instance;
        frame.pc = 0;
        frame.pcPrevBranch = 0;
        frame.pcSourceLineStart = idata.firstLinePC;
        frame.scope = scope;
        frame.savedStackTop = emptyStackTop;
        frame.savedCallOp = 0;
        System.arraycopy(args2, argShift, stack, 0, definedArgs);
        if (argsDbl2 != null) {
            System.arraycopy(argsDbl2, argShift, sDbl, 0, definedArgs);
        }
        for (i = definedArgs; i != idata.itsMaxVars; i++) {
            stack[i] = Undefined.instance;
        }
        if (stackReuse) {
            for (i = emptyStackTop + 1; i != stack.length; i++) {
                stack[i] = null;
            }
        }
        enterFrame(cx, frame, args2, false);
    }

    private static boolean isFrameEnterExitRequired(CallFrame frame) {
        return frame.debuggerFrame != null || frame.idata.itsNeedsActivation;
    }

    private static void enterFrame(Context cx, CallFrame frame, Object[] args, boolean continuationRestart) {
        boolean usesActivation = frame.idata.itsNeedsActivation;
        boolean isDebugged = frame.debuggerFrame != null;
        if (usesActivation || isDebugged) {
            Scriptable scope = frame.scope;
            if (scope == null) {
                Kit.codeBug();
            } else if (continuationRestart) {
                while (scope instanceof NativeWith) {
                    scope = scope.getParentScope();
                    if (scope == null || (frame.parentFrame != null && frame.parentFrame.scope == scope)) {
                        Kit.codeBug();
                        break;
                    }
                }
            }
            if (isDebugged) {
                frame.debuggerFrame.onEnter(cx, scope, frame.thisObj, args);
            }
            if (usesActivation) {
                ScriptRuntime.enterActivationFunction(cx, scope);
            }
        }
    }

    private static void exitFrame(Context cx, CallFrame frame, Object throwable) {
        if (frame.idata.itsNeedsActivation) {
            ScriptRuntime.exitActivationFunction(cx);
        }
        if (frame.debuggerFrame != null) {
            try {
                if (throwable instanceof Throwable) {
                    frame.debuggerFrame.onExit(cx, true, throwable);
                    return;
                }
                Object result;
                ContinuationJump cjump = (ContinuationJump) throwable;
                if (cjump == null) {
                    result = frame.result;
                } else {
                    result = cjump.result;
                }
                if (result == UniqueTag.DOUBLE_MARK) {
                    double resultDbl;
                    if (cjump == null) {
                        resultDbl = frame.resultDbl;
                    } else {
                        resultDbl = cjump.resultDbl;
                    }
                    result = ScriptRuntime.wrapNumber(resultDbl);
                }
                frame.debuggerFrame.onExit(cx, false, result);
            } catch (Throwable ex) {
                System.err.println("RHINO USAGE WARNING: onExit terminated with exception");
                ex.printStackTrace(System.err);
            }
        }
    }

    private static void setCallResult(CallFrame frame, Object callResult, double callResultDbl) {
        if (frame.savedCallOp == 38) {
            frame.stack[frame.savedStackTop] = callResult;
            frame.sDbl[frame.savedStackTop] = callResultDbl;
        } else if (frame.savedCallOp != 30) {
            Kit.codeBug();
        } else if (callResult instanceof Scriptable) {
            frame.stack[frame.savedStackTop] = callResult;
        }
        frame.savedCallOp = 0;
    }

    public static NativeContinuation captureContinuation(Context cx) {
        if (cx.lastInterpreterFrame != null && (cx.lastInterpreterFrame instanceof CallFrame)) {
            return captureContinuation(cx, (CallFrame) cx.lastInterpreterFrame, true);
        }
        throw new IllegalStateException("Interpreter frames not found");
    }

    private static NativeContinuation captureContinuation(Context cx, CallFrame frame, boolean requireContinuationsTopFrame) {
        NativeContinuation c = new NativeContinuation();
        ScriptRuntime.setObjectProtoAndParent(c, ScriptRuntime.getTopCallScope(cx));
        CallFrame x = frame;
        CallFrame outermost = frame;
        while (x != null && !x.frozen) {
            x.frozen = true;
            for (int i = x.savedStackTop + 1; i != x.stack.length; i++) {
                x.stack[i] = null;
                x.stackAttributes[i] = 0;
            }
            if (x.savedCallOp == 38) {
                x.stack[x.savedStackTop] = null;
            } else if (x.savedCallOp != 30) {
                Kit.codeBug();
            }
            outermost = x;
            x = x.parentFrame;
        }
        if (requireContinuationsTopFrame) {
            while (outermost.parentFrame != null) {
                outermost = outermost.parentFrame;
            }
            if (!outermost.isContinuationsTopFrame) {
                throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations");
            }
        }
        c.initImplementation(frame);
        return c;
    }

    private static int stack_int32(CallFrame frame, int i) {
        Object x = frame.stack[i];
        if (x == UniqueTag.DOUBLE_MARK) {
            return ScriptRuntime.toInt32(frame.sDbl[i]);
        }
        return ScriptRuntime.toInt32(x);
    }

    private static double stack_double(CallFrame frame, int i) {
        Object x = frame.stack[i];
        if (x != UniqueTag.DOUBLE_MARK) {
            return ScriptRuntime.toNumber(x);
        }
        return frame.sDbl[i];
    }

    private static boolean stack_boolean(CallFrame frame, int i) {
        Boolean x = frame.stack[i];
        if (x == Boolean.TRUE) {
            return true;
        }
        if (x == Boolean.FALSE) {
            return false;
        }
        double d;
        if (x == UniqueTag.DOUBLE_MARK) {
            d = frame.sDbl[i];
            if (d != d || d == 0.0d) {
                return false;
            }
            return true;
        } else if (x == null || x == Undefined.instance) {
            return false;
        } else {
            if (x instanceof Number) {
                d = ((Number) x).doubleValue();
                if (d != d || d == 0.0d) {
                    return false;
                }
                return true;
            } else if (x instanceof Boolean) {
                return x.booleanValue();
            } else {
                return ScriptRuntime.toBoolean(x);
            }
        }
    }

    private static void doAdd(Object[] stack, double[] sDbl, int stackTop, Context cx) {
        double d;
        boolean leftRightOrder;
        double lDbl;
        Object rhs = stack[stackTop + 1];
        Object lhs = stack[stackTop];
        if (rhs == UniqueTag.DOUBLE_MARK) {
            d = sDbl[stackTop + 1];
            if (lhs == UniqueTag.DOUBLE_MARK) {
                sDbl[stackTop] = sDbl[stackTop] + d;
                return;
            }
            leftRightOrder = true;
        } else if (lhs == UniqueTag.DOUBLE_MARK) {
            d = sDbl[stackTop];
            UniqueTag lhs2 = rhs;
            leftRightOrder = false;
        } else if ((lhs2 instanceof Scriptable) || (rhs instanceof Scriptable)) {
            stack[stackTop] = ScriptRuntime.add(lhs2, rhs, cx);
            return;
        } else if ((lhs2 instanceof CharSequence) || (rhs instanceof CharSequence)) {
            stack[stackTop] = new ConsString(ScriptRuntime.toCharSequence(lhs2), ScriptRuntime.toCharSequence(rhs));
            return;
        } else {
            lDbl = lhs2 instanceof Number ? ((Number) lhs2).doubleValue() : ScriptRuntime.toNumber(lhs2);
            double rDbl = rhs instanceof Number ? ((Number) rhs).doubleValue() : ScriptRuntime.toNumber(rhs);
            stack[stackTop] = UniqueTag.DOUBLE_MARK;
            sDbl[stackTop] = lDbl + rDbl;
            return;
        }
        if (lhs2 instanceof Scriptable) {
            rhs = ScriptRuntime.wrapNumber(d);
            if (!leftRightOrder) {
                Object tmp = lhs2;
                lhs2 = rhs;
                rhs = tmp;
            }
            stack[stackTop] = ScriptRuntime.add(lhs2, rhs, cx);
        } else if (lhs2 instanceof CharSequence) {
            CharSequence lstr = (CharSequence) lhs2;
            CharSequence rstr = ScriptRuntime.toCharSequence(Double.valueOf(d));
            if (leftRightOrder) {
                stack[stackTop] = new ConsString(lstr, rstr);
            } else {
                stack[stackTop] = new ConsString(rstr, lstr);
            }
        } else {
            lDbl = lhs2 instanceof Number ? ((Number) lhs2).doubleValue() : ScriptRuntime.toNumber(lhs2);
            stack[stackTop] = UniqueTag.DOUBLE_MARK;
            sDbl[stackTop] = lDbl + d;
        }
    }

    private static int doArithmetic(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop) {
        double rDbl = stack_double(frame, stackTop);
        stackTop--;
        double lDbl = stack_double(frame, stackTop);
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        switch (op) {
            case 22:
                lDbl -= rDbl;
                break;
            case 23:
                lDbl *= rDbl;
                break;
            case 24:
                lDbl /= rDbl;
                break;
            case 25:
                lDbl %= rDbl;
                break;
        }
        sDbl[stackTop] = lDbl;
        return stackTop;
    }

    private static Object[] getArgsArray(Object[] stack, double[] sDbl, int shift, int count) {
        if (count == 0) {
            return ScriptRuntime.emptyArgs;
        }
        Object[] args = new Object[count];
        int i = 0;
        while (i != count) {
            Object val = stack[shift];
            if (val == UniqueTag.DOUBLE_MARK) {
                val = ScriptRuntime.wrapNumber(sDbl[shift]);
            }
            args[i] = val;
            i++;
            shift++;
        }
        return args;
    }

    private static void addInstructionCount(Context cx, CallFrame frame, int extra) {
        cx.instructionCount += (frame.pc - frame.pcPrevBranch) + extra;
        if (cx.instructionCount > cx.instructionThreshold) {
            cx.observeInstructionCount(cx.instructionCount);
            cx.instructionCount = 0;
        }
    }
}
