package org.mozilla.javascript;

import org.mozilla.classfile.ByteCode;
import org.mozilla.javascript.ObjToIntMap.Iterator;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.ScriptNode;

class CodeGenerator extends Icode {
    private static final int ECF_TAIL = 1;
    private static final int MIN_FIXUP_TABLE_SIZE = 40;
    private static final int MIN_LABEL_TABLE_SIZE = 32;
    private CompilerEnvirons compilerEnv;
    private int doubleTableTop;
    private int exceptionTableTop;
    private long[] fixupTable;
    private int fixupTableTop;
    private int iCodeTop;
    private InterpreterData itsData;
    private boolean itsInFunctionFlag;
    private boolean itsInTryFlag;
    private int[] labelTable;
    private int labelTableTop;
    private int lineNumber;
    private ObjArray literalIds = new ObjArray();
    private int localTop;
    private ScriptNode scriptOrFn;
    private int stackDepth;
    private ObjToIntMap strings = new ObjToIntMap(20);

    CodeGenerator() {
    }

    public InterpreterData compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction) {
        this.compilerEnv = compilerEnv;
        new NodeTransformer().transform(tree);
        if (returnFunction) {
            this.scriptOrFn = tree.getFunctionNode(0);
        } else {
            this.scriptOrFn = tree;
        }
        this.itsData = new InterpreterData(compilerEnv.getLanguageVersion(), this.scriptOrFn.getSourceName(), encodedSource, ((AstRoot) tree).isInStrictMode());
        this.itsData.topLevel = true;
        if (returnFunction) {
            generateFunctionICode();
        } else {
            generateICodeFromTree(this.scriptOrFn);
        }
        return this.itsData;
    }

    private void generateFunctionICode() {
        this.itsInFunctionFlag = true;
        FunctionNode theFunction = this.scriptOrFn;
        this.itsData.itsFunctionType = theFunction.getFunctionType();
        this.itsData.itsNeedsActivation = theFunction.requiresActivation();
        if (theFunction.getFunctionName() != null) {
            this.itsData.itsName = theFunction.getName();
        }
        if (theFunction.isGenerator()) {
            addIcode(-62);
            addUint16(theFunction.getBaseLineno() & 65535);
        }
        generateICodeFromTree(theFunction.getLastChild());
    }

    private void generateICodeFromTree(Node tree) {
        generateNestedFunctions();
        generateRegExpLiterals();
        visitStatement(tree, 0);
        fixLabelGotos();
        if (this.itsData.itsFunctionType == 0) {
            addToken(64);
        }
        if (this.itsData.itsICode.length != this.iCodeTop) {
            byte[] tmp = new byte[this.iCodeTop];
            System.arraycopy(this.itsData.itsICode, 0, tmp, 0, this.iCodeTop);
            this.itsData.itsICode = tmp;
        }
        if (this.strings.size() == 0) {
            this.itsData.itsStringTable = null;
        } else {
            this.itsData.itsStringTable = new String[this.strings.size()];
            Iterator iter = this.strings.newIterator();
            iter.start();
            while (!iter.done()) {
                String str = (String) iter.getKey();
                int index = iter.getValue();
                if (this.itsData.itsStringTable[index] != null) {
                    Kit.codeBug();
                }
                this.itsData.itsStringTable[index] = str;
                iter.next();
            }
        }
        if (this.doubleTableTop == 0) {
            this.itsData.itsDoubleTable = null;
        } else if (this.itsData.itsDoubleTable.length != this.doubleTableTop) {
            double[] tmp2 = new double[this.doubleTableTop];
            System.arraycopy(this.itsData.itsDoubleTable, 0, tmp2, 0, this.doubleTableTop);
            this.itsData.itsDoubleTable = tmp2;
        }
        if (!(this.exceptionTableTop == 0 || this.itsData.itsExceptionTable.length == this.exceptionTableTop)) {
            int[] tmp3 = new int[this.exceptionTableTop];
            System.arraycopy(this.itsData.itsExceptionTable, 0, tmp3, 0, this.exceptionTableTop);
            this.itsData.itsExceptionTable = tmp3;
        }
        this.itsData.itsMaxVars = this.scriptOrFn.getParamAndVarCount();
        this.itsData.itsMaxFrameArray = (this.itsData.itsMaxVars + this.itsData.itsMaxLocals) + this.itsData.itsMaxStack;
        this.itsData.argNames = this.scriptOrFn.getParamAndVarNames();
        this.itsData.argIsConst = this.scriptOrFn.getParamAndVarConst();
        this.itsData.argCount = this.scriptOrFn.getParamCount();
        this.itsData.encodedSourceStart = this.scriptOrFn.getEncodedSourceStart();
        this.itsData.encodedSourceEnd = this.scriptOrFn.getEncodedSourceEnd();
        if (this.literalIds.size() != 0) {
            this.itsData.literalIds = this.literalIds.toArray();
        }
    }

    private void generateNestedFunctions() {
        int functionCount = this.scriptOrFn.getFunctionCount();
        if (functionCount != 0) {
            InterpreterData[] array = new InterpreterData[functionCount];
            for (int i = 0; i != functionCount; i++) {
                FunctionNode fn = this.scriptOrFn.getFunctionNode(i);
                CodeGenerator gen = new CodeGenerator();
                gen.compilerEnv = this.compilerEnv;
                gen.scriptOrFn = fn;
                gen.itsData = new InterpreterData(this.itsData);
                gen.generateFunctionICode();
                array[i] = gen.itsData;
            }
            this.itsData.itsNestedFunctions = array;
        }
    }

    private void generateRegExpLiterals() {
        int N = this.scriptOrFn.getRegexpCount();
        if (N != 0) {
            Context cx = Context.getContext();
            RegExpProxy rep = ScriptRuntime.checkRegExpProxy(cx);
            Object[] array = new Object[N];
            for (int i = 0; i != N; i++) {
                array[i] = rep.compileRegExp(cx, this.scriptOrFn.getRegexpString(i), this.scriptOrFn.getRegexpFlags(i));
            }
            this.itsData.itsRegExpLiterals = array;
        }
    }

    private void updateLineNumber(Node node) {
        int lineno = node.getLineno();
        if (lineno != this.lineNumber && lineno >= 0) {
            if (this.itsData.firstLinePC < 0) {
                this.itsData.firstLinePC = lineno;
            }
            this.lineNumber = lineno;
            addIcode(-26);
            addUint16(65535 & lineno);
        }
    }

    private RuntimeException badTree(Node node) {
        throw new RuntimeException(node.toString());
    }

    /* JADX WARNING: Missing block: B:11:0x005a, code skipped:
            if (r33.stackDepth == r35) goto L_0x034d;
     */
    /* JADX WARNING: Missing block: B:13:0x0060, code skipped:
            throw org.mozilla.javascript.Kit.codeBug();
     */
    /* JADX WARNING: Missing block: B:19:0x006e, code skipped:
            if (r19 == null) goto L_0x0054;
     */
    /* JADX WARNING: Missing block: B:20:0x0070, code skipped:
            visitStatement(r19, r35);
            r19 = r19.getNext();
     */
    /* JADX WARNING: Missing block: B:74:0x034d, code skipped:
            return;
     */
    private void visitStatement(org.mozilla.javascript.Node r34, int r35) {
        /*
        r33 = this;
        r32 = r34.getType();
        r19 = r34.getFirstChild();
        switch(r32) {
            case -62: goto L_0x0054;
            case 2: goto L_0x007e;
            case 3: goto L_0x0093;
            case 4: goto L_0x02e1;
            case 5: goto L_0x0166;
            case 6: goto L_0x0145;
            case 7: goto L_0x0145;
            case 50: goto L_0x02aa;
            case 51: goto L_0x02d1;
            case 57: goto L_0x0267;
            case 58: goto L_0x0332;
            case 59: goto L_0x0332;
            case 60: goto L_0x0332;
            case 64: goto L_0x0326;
            case 81: goto L_0x01e1;
            case 109: goto L_0x0010;
            case 114: goto L_0x00d2;
            case 123: goto L_0x006b;
            case 125: goto L_0x018c;
            case 128: goto L_0x006b;
            case 129: goto L_0x006b;
            case 130: goto L_0x006b;
            case 131: goto L_0x0140;
            case 132: goto L_0x006b;
            case 133: goto L_0x01c0;
            case 134: goto L_0x01c0;
            case 135: goto L_0x0179;
            case 136: goto L_0x006e;
            case 141: goto L_0x009a;
            case 160: goto L_0x00ca;
            default: goto L_0x000b;
        };
    L_0x000b:
        r3 = r33.badTree(r34);
        throw r3;
    L_0x0010:
        r3 = 1;
        r0 = r34;
        r22 = r0.getExistingIntProp(r3);
        r0 = r33;
        r3 = r0.scriptOrFn;
        r0 = r22;
        r3 = r3.getFunctionNode(r0);
        r23 = r3.getFunctionType();
        r3 = 3;
        r0 = r23;
        if (r0 != r3) goto L_0x0061;
    L_0x002a:
        r3 = -20;
        r0 = r33;
        r1 = r22;
        r0.addIndexOp(r3, r1);
    L_0x0033:
        r0 = r33;
        r3 = r0.itsInFunctionFlag;
        if (r3 != 0) goto L_0x0054;
    L_0x0039:
        r3 = -19;
        r0 = r33;
        r1 = r22;
        r0.addIndexOp(r3, r1);
        r3 = 1;
        r0 = r33;
        r0.stackChange(r3);
        r3 = -5;
        r0 = r33;
        r0.addIcode(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
    L_0x0054:
        r0 = r33;
        r3 = r0.stackDepth;
        r0 = r35;
        if (r3 == r0) goto L_0x034d;
    L_0x005c:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x0061:
        r3 = 1;
        r0 = r23;
        if (r0 == r3) goto L_0x0033;
    L_0x0066:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x006b:
        r33.updateLineNumber(r34);
    L_0x006e:
        if (r19 == 0) goto L_0x0054;
    L_0x0070:
        r0 = r33;
        r1 = r19;
        r2 = r35;
        r0.visitStatement(r1, r2);
        r19 = r19.getNext();
        goto L_0x006e;
    L_0x007e:
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r3 = 2;
        r0 = r33;
        r0.addToken(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x0093:
        r3 = 3;
        r0 = r33;
        r0.addToken(r3);
        goto L_0x0054;
    L_0x009a:
        r24 = r33.allocLocal();
        r3 = 2;
        r0 = r34;
        r1 = r24;
        r0.putIntProp(r3, r1);
        r33.updateLineNumber(r34);
    L_0x00a9:
        if (r19 == 0) goto L_0x00b9;
    L_0x00ab:
        r0 = r33;
        r1 = r19;
        r2 = r35;
        r0.visitStatement(r1, r2);
        r19 = r19.getNext();
        goto L_0x00a9;
    L_0x00b9:
        r3 = -56;
        r0 = r33;
        r1 = r24;
        r0.addIndexOp(r3, r1);
        r0 = r33;
        r1 = r24;
        r0.releaseLocal(r1);
        goto L_0x0054;
    L_0x00ca:
        r3 = -64;
        r0 = r33;
        r0.addIcode(r3);
        goto L_0x0054;
    L_0x00d2:
        r33.updateLineNumber(r34);
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r17 = r19.getNext();
        r17 = (org.mozilla.javascript.ast.Jump) r17;
    L_0x00e3:
        if (r17 == 0) goto L_0x0132;
    L_0x00e5:
        r3 = r17.getType();
        r6 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        if (r3 == r6) goto L_0x00f6;
    L_0x00ed:
        r0 = r33;
        r1 = r17;
        r3 = r0.badTree(r1);
        throw r3;
    L_0x00f6:
        r30 = r17.getFirstChild();
        r3 = -1;
        r0 = r33;
        r0.addIcode(r3);
        r3 = 1;
        r0 = r33;
        r0.stackChange(r3);
        r3 = 0;
        r0 = r33;
        r1 = r30;
        r0.visitExpression(r1, r3);
        r3 = 46;
        r0 = r33;
        r0.addToken(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        r0 = r17;
        r3 = r0.target;
        r6 = -6;
        r0 = r33;
        r0.addGoto(r3, r6);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        r17 = r17.getNext();
        r17 = (org.mozilla.javascript.ast.Jump) r17;
        goto L_0x00e3;
    L_0x0132:
        r3 = -4;
        r0 = r33;
        r0.addIcode(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x0140:
        r33.markTargetLabel(r34);
        goto L_0x0054;
    L_0x0145:
        r34 = (org.mozilla.javascript.ast.Jump) r34;
        r0 = r34;
        r0 = r0.target;
        r29 = r0;
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r0 = r33;
        r1 = r29;
        r2 = r32;
        r0.addGoto(r1, r2);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x0166:
        r34 = (org.mozilla.javascript.ast.Jump) r34;
        r0 = r34;
        r0 = r0.target;
        r29 = r0;
        r0 = r33;
        r1 = r29;
        r2 = r32;
        r0.addGoto(r1, r2);
        goto L_0x0054;
    L_0x0179:
        r34 = (org.mozilla.javascript.ast.Jump) r34;
        r0 = r34;
        r0 = r0.target;
        r29 = r0;
        r3 = -23;
        r0 = r33;
        r1 = r29;
        r0.addGoto(r1, r3);
        goto L_0x0054;
    L_0x018c:
        r3 = 1;
        r0 = r33;
        r0.stackChange(r3);
        r20 = r33.getLocalBlockRef(r34);
        r3 = -24;
        r0 = r33;
        r1 = r20;
        r0.addIndexOp(r3, r1);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
    L_0x01a5:
        if (r19 == 0) goto L_0x01b5;
    L_0x01a7:
        r0 = r33;
        r1 = r19;
        r2 = r35;
        r0.visitStatement(r1, r2);
        r19 = r19.getNext();
        goto L_0x01a5;
    L_0x01b5:
        r3 = -25;
        r0 = r33;
        r1 = r20;
        r0.addIndexOp(r3, r1);
        goto L_0x0054;
    L_0x01c0:
        r33.updateLineNumber(r34);
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r3 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        r0 = r32;
        if (r0 != r3) goto L_0x01df;
    L_0x01d1:
        r3 = -4;
    L_0x01d2:
        r0 = r33;
        r0.addIcode(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x01df:
        r3 = -5;
        goto L_0x01d2;
    L_0x01e1:
        r31 = r34;
        r31 = (org.mozilla.javascript.ast.Jump) r31;
        r0 = r33;
        r1 = r31;
        r8 = r0.getLocalBlockRef(r1);
        r9 = r33.allocLocal();
        r3 = -13;
        r0 = r33;
        r0.addIndexOp(r3, r9);
        r0 = r33;
        r4 = r0.iCodeTop;
        r0 = r33;
        r0 = r0.itsInTryFlag;
        r27 = r0;
        r3 = 1;
        r0 = r33;
        r0.itsInTryFlag = r3;
    L_0x0207:
        if (r19 == 0) goto L_0x0217;
    L_0x0209:
        r0 = r33;
        r1 = r19;
        r2 = r35;
        r0.visitStatement(r1, r2);
        r19 = r19.getNext();
        goto L_0x0207;
    L_0x0217:
        r0 = r27;
        r1 = r33;
        r1.itsInTryFlag = r0;
        r0 = r31;
        r0 = r0.target;
        r18 = r0;
        if (r18 == 0) goto L_0x023a;
    L_0x0225:
        r0 = r33;
        r3 = r0.labelTable;
        r0 = r33;
        r1 = r18;
        r6 = r0.getTargetLabel(r1);
        r5 = r3[r6];
        r7 = 0;
        r3 = r33;
        r6 = r5;
        r3.addExceptionHandler(r4, r5, r6, r7, r8, r9);
    L_0x023a:
        r21 = r31.getFinally();
        if (r21 == 0) goto L_0x0259;
    L_0x0240:
        r0 = r33;
        r3 = r0.labelTable;
        r0 = r33;
        r1 = r21;
        r6 = r0.getTargetLabel(r1);
        r12 = r3[r6];
        r14 = 1;
        r10 = r33;
        r11 = r4;
        r13 = r12;
        r15 = r8;
        r16 = r9;
        r10.addExceptionHandler(r11, r12, r13, r14, r15, r16);
    L_0x0259:
        r3 = -56;
        r0 = r33;
        r0.addIndexOp(r3, r9);
        r0 = r33;
        r0.releaseLocal(r9);
        goto L_0x0054;
    L_0x0267:
        r25 = r33.getLocalBlockRef(r34);
        r3 = 14;
        r0 = r34;
        r28 = r0.getExistingIntProp(r3);
        r26 = r19.getString();
        r19 = r19.getNext();
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r0 = r33;
        r1 = r26;
        r0.addStringPrefix(r1);
        r0 = r33;
        r1 = r25;
        r0.addIndexPrefix(r1);
        r3 = 57;
        r0 = r33;
        r0.addToken(r3);
        if (r28 == 0) goto L_0x02a8;
    L_0x029a:
        r3 = 1;
    L_0x029b:
        r0 = r33;
        r0.addUint8(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x02a8:
        r3 = 0;
        goto L_0x029b;
    L_0x02aa:
        r33.updateLineNumber(r34);
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r3 = 50;
        r0 = r33;
        r0.addToken(r3);
        r0 = r33;
        r3 = r0.lineNumber;
        r6 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r3 = r3 & r6;
        r0 = r33;
        r0.addUint16(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x02d1:
        r33.updateLineNumber(r34);
        r3 = 51;
        r6 = r33.getLocalBlockRef(r34);
        r0 = r33;
        r0.addIndexOp(r3, r6);
        goto L_0x0054;
    L_0x02e1:
        r33.updateLineNumber(r34);
        r3 = 20;
        r6 = 0;
        r0 = r34;
        r3 = r0.getIntProp(r3, r6);
        if (r3 == 0) goto L_0x0305;
    L_0x02ef:
        r3 = -63;
        r0 = r33;
        r0.addIcode(r3);
        r0 = r33;
        r3 = r0.lineNumber;
        r6 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r3 = r3 & r6;
        r0 = r33;
        r0.addUint16(r3);
        goto L_0x0054;
    L_0x0305:
        if (r19 == 0) goto L_0x031d;
    L_0x0307:
        r3 = 1;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r3 = 4;
        r0 = r33;
        r0.addToken(r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x031d:
        r3 = -22;
        r0 = r33;
        r0.addIcode(r3);
        goto L_0x0054;
    L_0x0326:
        r33.updateLineNumber(r34);
        r3 = 64;
        r0 = r33;
        r0.addToken(r3);
        goto L_0x0054;
    L_0x0332:
        r3 = 0;
        r0 = r33;
        r1 = r19;
        r0.visitExpression(r1, r3);
        r3 = r33.getLocalBlockRef(r34);
        r0 = r33;
        r1 = r32;
        r0.addIndexOp(r1, r3);
        r3 = -1;
        r0 = r33;
        r0.stackChange(r3);
        goto L_0x0054;
    L_0x034d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.CodeGenerator.visitStatement(org.mozilla.javascript.Node, int):void");
    }

    private void visitExpression(Node node, int contextFlags) {
        int type = node.getType();
        Node child = node.getFirstChild();
        int savedStackDepth = this.stackDepth;
        String name;
        int index;
        switch (type) {
            case 8:
            case 73:
                name = child.getString();
                visitExpression(child, 0);
                visitExpression(child.getNext(), 0);
                addStringOp(type, name);
                stackChange(-1);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 36:
            case 46:
            case 47:
            case 52:
            case 53:
                visitExpression(child, 0);
                visitExpression(child.getNext(), 0);
                addToken(type);
                stackChange(-1);
                break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 32:
            case 126:
                visitExpression(child, 0);
                if (type != 126) {
                    addToken(type);
                    break;
                }
                addIcode(-4);
                addIcode(-50);
                break;
            case 30:
            case 38:
            case 70:
                if (type == 30) {
                    visitExpression(child, 0);
                } else {
                    generateCallFunAndThis(child);
                }
                int argCount = 0;
                while (true) {
                    child = child.getNext();
                    if (child == null) {
                        int callType = node.getIntProp(10, 0);
                        if (type == 70 || callType == 0) {
                            if (!(type != 38 || (contextFlags & 1) == 0 || this.compilerEnv.isGenerateDebugInfo() || this.itsInTryFlag)) {
                                type = -55;
                            }
                            addIndexOp(type, argCount);
                        } else {
                            addIndexOp(-21, argCount);
                            addUint8(callType);
                            addUint8(type == 30 ? 1 : 0);
                            addUint16(this.lineNumber & 65535);
                        }
                        if (type == 30) {
                            stackChange(-argCount);
                        } else {
                            stackChange(-1 - argCount);
                        }
                        if (argCount > this.itsData.itsMaxCalleeArgs) {
                            this.itsData.itsMaxCalleeArgs = argCount;
                            break;
                        }
                    }
                    visitExpression(child, 0);
                    argCount++;
                }
                break;
            case 31:
                boolean isName = child.getType() == 49;
                visitExpression(child, 0);
                visitExpression(child.getNext(), 0);
                if (isName) {
                    addIcode(0);
                } else {
                    addToken(31);
                }
                stackChange(-1);
                break;
            case 33:
            case 34:
                visitExpression(child, 0);
                addStringOp(type, child.getNext().getString());
                break;
            case 35:
            case 139:
                visitExpression(child, 0);
                child = child.getNext();
                String property = child.getString();
                child = child.getNext();
                if (type == 139) {
                    addIcode(-1);
                    stackChange(1);
                    addStringOp(33, property);
                    stackChange(-1);
                }
                visitExpression(child, 0);
                addStringOp(35, property);
                stackChange(-1);
                break;
            case 37:
            case 140:
                visitExpression(child, 0);
                child = child.getNext();
                visitExpression(child, 0);
                child = child.getNext();
                if (type == 140) {
                    addIcode(-2);
                    stackChange(2);
                    addToken(36);
                    stackChange(-1);
                    stackChange(-1);
                }
                visitExpression(child, 0);
                addToken(37);
                stackChange(-2);
                break;
            case 39:
            case 41:
            case 49:
                addStringOp(type, node.getString());
                stackChange(1);
                break;
            case 40:
                double num = node.getDouble();
                short inum = (int) num;
                if (((double) inum) != num) {
                    addIndexOp(40, getDoubleIndex(num));
                } else if (inum == (short) 0) {
                    addIcode(-51);
                    if (1.0d / num < 0.0d) {
                        addToken(29);
                    }
                } else if (inum == (short) 1) {
                    addIcode(-52);
                } else if (((short) inum) == inum) {
                    addIcode(-27);
                    addUint16(65535 & inum);
                } else {
                    addIcode(-28);
                    addInt(inum);
                }
                stackChange(1);
                break;
            case 42:
            case 43:
            case 44:
            case 45:
            case 63:
                addToken(type);
                stackChange(1);
                break;
            case 48:
                addIndexOp(48, node.getExistingIntProp(4));
                stackChange(1);
                break;
            case 54:
                addIndexOp(54, getLocalBlockRef(node));
                stackChange(1);
                break;
            case 55:
                if (this.itsData.itsNeedsActivation) {
                    Kit.codeBug();
                }
                addVarOp(55, this.scriptOrFn.getIndexForNameNode(node));
                stackChange(1);
                break;
            case 56:
                if (this.itsData.itsNeedsActivation) {
                    Kit.codeBug();
                }
                index = this.scriptOrFn.getIndexForNameNode(child);
                visitExpression(child.getNext(), 0);
                addVarOp(56, index);
                break;
            case 61:
            case 62:
                addIndexOp(type, getLocalBlockRef(node));
                stackChange(1);
                break;
            case 65:
            case 66:
                visitLiteral(node, child);
                break;
            case 67:
            case 69:
                visitExpression(child, 0);
                addToken(type);
                break;
            case 68:
            case 142:
                visitExpression(child, 0);
                child = child.getNext();
                if (type == 142) {
                    addIcode(-1);
                    stackChange(1);
                    addToken(67);
                    stackChange(-1);
                }
                visitExpression(child, 0);
                addToken(68);
                stackChange(-1);
                break;
            case 71:
                visitExpression(child, 0);
                addStringOp(type, (String) node.getProp(17));
                break;
            case 72:
                if (child != null) {
                    visitExpression(child, 0);
                } else {
                    addIcode(-50);
                    stackChange(1);
                }
                addToken(72);
                addUint16(node.getLineno() & 65535);
                break;
            case 74:
            case 75:
            case 76:
                visitExpression(child, 0);
                addToken(type);
                break;
            case 77:
            case 78:
            case 79:
            case 80:
                int memberTypeFlags = node.getIntProp(16, 0);
                int childCount = 0;
                do {
                    visitExpression(child, 0);
                    childCount++;
                    child = child.getNext();
                } while (child != null);
                addIndexOp(type, memberTypeFlags);
                stackChange(1 - childCount);
                break;
            case 89:
                Node lastChild = node.getLastChild();
                while (child != lastChild) {
                    visitExpression(child, 0);
                    addIcode(-4);
                    stackChange(-1);
                    child = child.getNext();
                }
                visitExpression(child, contextFlags & 1);
                break;
            case 102:
                Node ifThen = child.getNext();
                Node ifElse = ifThen.getNext();
                visitExpression(child, 0);
                int elseJumpStart = this.iCodeTop;
                addGotoOp(7);
                stackChange(-1);
                visitExpression(ifThen, contextFlags & 1);
                int afterElseJumpStart = this.iCodeTop;
                addGotoOp(5);
                resolveForwardGoto(elseJumpStart);
                this.stackDepth = savedStackDepth;
                visitExpression(ifElse, contextFlags & 1);
                resolveForwardGoto(afterElseJumpStart);
                break;
            case 104:
            case 105:
                visitExpression(child, 0);
                addIcode(-1);
                stackChange(1);
                int afterSecondJumpStart = this.iCodeTop;
                addGotoOp(type == 105 ? 7 : 6);
                stackChange(-1);
                addIcode(-4);
                stackChange(-1);
                visitExpression(child.getNext(), contextFlags & 1);
                resolveForwardGoto(afterSecondJumpStart);
                break;
            case 106:
            case 107:
                visitIncDec(node, child);
                break;
            case 109:
                int fnIndex = node.getExistingIntProp(1);
                if (this.scriptOrFn.getFunctionNode(fnIndex).getFunctionType() == 2) {
                    addIndexOp(-19, fnIndex);
                    stackChange(1);
                    break;
                }
                throw Kit.codeBug();
            case 137:
                index = -1;
                if (this.itsInFunctionFlag && !this.itsData.itsNeedsActivation) {
                    index = this.scriptOrFn.getIndexForNameNode(node);
                }
                if (index != -1) {
                    addVarOp(55, index);
                    stackChange(1);
                    addToken(32);
                    break;
                }
                addStringOp(-14, node.getString());
                stackChange(1);
                break;
            case 138:
                stackChange(1);
                break;
            case 146:
                updateLineNumber(node);
                visitExpression(child, 0);
                addIcode(-53);
                stackChange(-1);
                int queryPC = this.iCodeTop;
                visitExpression(child.getNext(), 0);
                addBackwardGoto(-54, queryPC);
                break;
            case 155:
                name = child.getString();
                visitExpression(child, 0);
                visitExpression(child.getNext(), 0);
                addStringOp(-59, name);
                stackChange(-1);
                break;
            case 156:
                if (this.itsData.itsNeedsActivation) {
                    Kit.codeBug();
                }
                index = this.scriptOrFn.getIndexForNameNode(child);
                visitExpression(child.getNext(), 0);
                addVarOp(156, index);
                break;
            case 157:
                visitArrayComprehension(node, child, child.getNext());
                break;
            case 159:
                Node enterWith = node.getFirstChild();
                Node with = enterWith.getNext();
                visitExpression(enterWith.getFirstChild(), 0);
                addToken(2);
                stackChange(-1);
                visitExpression(with.getFirstChild(), 0);
                addToken(3);
                break;
            default:
                throw badTree(node);
        }
        if (savedStackDepth + 1 != this.stackDepth) {
            Kit.codeBug();
        }
    }

    private void generateCallFunAndThis(Node left) {
        int type = left.getType();
        switch (type) {
            case 33:
            case 36:
                Node target = left.getFirstChild();
                visitExpression(target, 0);
                Node id = target.getNext();
                if (type == 33) {
                    addStringOp(-16, id.getString());
                    stackChange(1);
                    return;
                }
                visitExpression(id, 0);
                addIcode(-17);
                return;
            case 39:
                addStringOp(-15, left.getString());
                stackChange(2);
                return;
            default:
                visitExpression(left, 0);
                addIcode(-18);
                stackChange(1);
                return;
        }
    }

    private void visitIncDec(Node node, Node child) {
        int incrDecrMask = node.getExistingIntProp(13);
        Node object;
        switch (child.getType()) {
            case 33:
                object = child.getFirstChild();
                visitExpression(object, 0);
                addStringOp(-9, object.getNext().getString());
                addUint8(incrDecrMask);
                return;
            case 36:
                object = child.getFirstChild();
                visitExpression(object, 0);
                visitExpression(object.getNext(), 0);
                addIcode(-10);
                addUint8(incrDecrMask);
                stackChange(-1);
                return;
            case 39:
                addStringOp(-8, child.getString());
                addUint8(incrDecrMask);
                stackChange(1);
                return;
            case 55:
                if (this.itsData.itsNeedsActivation) {
                    Kit.codeBug();
                }
                addVarOp(-7, this.scriptOrFn.getIndexForNameNode(child));
                addUint8(incrDecrMask);
                stackChange(1);
                return;
            case 67:
                visitExpression(child.getFirstChild(), 0);
                addIcode(-11);
                addUint8(incrDecrMask);
                return;
            default:
                throw badTree(node);
        }
    }

    private void visitLiteral(Node node, Node child) {
        int count;
        int type = node.getType();
        Object[] propertyIds = null;
        if (type == 65) {
            count = 0;
            for (Node n = child; n != null; n = n.getNext()) {
                count++;
            }
        } else if (type == 66) {
            propertyIds = (Object[]) node.getProp(12);
            count = propertyIds.length;
        } else {
            throw badTree(node);
        }
        addIndexOp(-29, count);
        stackChange(2);
        while (child != null) {
            int childType = child.getType();
            if (childType == 151) {
                visitExpression(child.getFirstChild(), 0);
                addIcode(-57);
            } else if (childType == 152) {
                visitExpression(child.getFirstChild(), 0);
                addIcode(-58);
            } else {
                visitExpression(child, 0);
                addIcode(-30);
            }
            stackChange(-1);
            child = child.getNext();
        }
        int index;
        if (type == 65) {
            int[] skipIndexes = (int[]) node.getProp(11);
            if (skipIndexes == null) {
                addToken(65);
            } else {
                index = this.literalIds.size();
                this.literalIds.add(skipIndexes);
                addIndexOp(-31, index);
            }
        } else {
            index = this.literalIds.size();
            this.literalIds.add(propertyIds);
            addIndexOp(66, index);
        }
        stackChange(-1);
    }

    private void visitArrayComprehension(Node node, Node initStmt, Node expr) {
        visitStatement(initStmt, this.stackDepth);
        visitExpression(expr, 0);
    }

    private int getLocalBlockRef(Node node) {
        return ((Node) node.getProp(3)).getExistingIntProp(2);
    }

    private int getTargetLabel(Node target) {
        int label = target.labelId();
        if (label != -1) {
            return label;
        }
        label = this.labelTableTop;
        if (this.labelTable == null || label == this.labelTable.length) {
            if (this.labelTable == null) {
                this.labelTable = new int[32];
            } else {
                int[] tmp = new int[(this.labelTable.length * 2)];
                System.arraycopy(this.labelTable, 0, tmp, 0, label);
                this.labelTable = tmp;
            }
        }
        this.labelTableTop = label + 1;
        this.labelTable[label] = -1;
        target.labelId(label);
        return label;
    }

    private void markTargetLabel(Node target) {
        int label = getTargetLabel(target);
        if (this.labelTable[label] != -1) {
            Kit.codeBug();
        }
        this.labelTable[label] = this.iCodeTop;
    }

    private void addGoto(Node target, int gotoOp) {
        int label = getTargetLabel(target);
        if (label >= this.labelTableTop) {
            Kit.codeBug();
        }
        int targetPC = this.labelTable[label];
        if (targetPC != -1) {
            addBackwardGoto(gotoOp, targetPC);
            return;
        }
        int gotoPC = this.iCodeTop;
        addGotoOp(gotoOp);
        int top = this.fixupTableTop;
        if (this.fixupTable == null || top == this.fixupTable.length) {
            if (this.fixupTable == null) {
                this.fixupTable = new long[40];
            } else {
                long[] tmp = new long[(this.fixupTable.length * 2)];
                System.arraycopy(this.fixupTable, 0, tmp, 0, top);
                this.fixupTable = tmp;
            }
        }
        this.fixupTableTop = top + 1;
        this.fixupTable[top] = (((long) label) << 32) | ((long) gotoPC);
    }

    private void fixLabelGotos() {
        for (int i = 0; i < this.fixupTableTop; i++) {
            long fixup = this.fixupTable[i];
            int jumpSource = (int) fixup;
            int pc = this.labelTable[(int) (fixup >> 32)];
            if (pc == -1) {
                throw Kit.codeBug();
            }
            resolveGoto(jumpSource, pc);
        }
        this.fixupTableTop = 0;
    }

    private void addBackwardGoto(int gotoOp, int jumpPC) {
        int fromPC = this.iCodeTop;
        if (fromPC <= jumpPC) {
            throw Kit.codeBug();
        }
        addGotoOp(gotoOp);
        resolveGoto(fromPC, jumpPC);
    }

    private void resolveForwardGoto(int fromPC) {
        if (this.iCodeTop < fromPC + 3) {
            throw Kit.codeBug();
        }
        resolveGoto(fromPC, this.iCodeTop);
    }

    private void resolveGoto(int fromPC, int jumpPC) {
        int offset = jumpPC - fromPC;
        if (offset < 0 || offset > 2) {
            int offsetSite = fromPC + 1;
            if (offset != ((short) offset)) {
                if (this.itsData.longJumps == null) {
                    this.itsData.longJumps = new UintMap();
                }
                this.itsData.longJumps.put(offsetSite, jumpPC);
                offset = 0;
            }
            byte[] array = this.itsData.itsICode;
            array[offsetSite] = (byte) (offset >> 8);
            array[offsetSite + 1] = (byte) offset;
            return;
        }
        throw Kit.codeBug();
    }

    private void addToken(int token) {
        if (Icode.validTokenCode(token)) {
            addUint8(token);
            return;
        }
        throw Kit.codeBug();
    }

    private void addIcode(int icode) {
        if (Icode.validIcode(icode)) {
            addUint8(icode & ByteCode.IMPDEP2);
            return;
        }
        throw Kit.codeBug();
    }

    private void addUint8(int value) {
        if ((value & -256) != 0) {
            throw Kit.codeBug();
        }
        byte[] array = this.itsData.itsICode;
        int top = this.iCodeTop;
        if (top == array.length) {
            array = increaseICodeCapacity(1);
        }
        array[top] = (byte) value;
        this.iCodeTop = top + 1;
    }

    private void addUint16(int value) {
        if ((-65536 & value) != 0) {
            throw Kit.codeBug();
        }
        byte[] array = this.itsData.itsICode;
        int top = this.iCodeTop;
        if (top + 2 > array.length) {
            array = increaseICodeCapacity(2);
        }
        array[top] = (byte) (value >>> 8);
        array[top + 1] = (byte) value;
        this.iCodeTop = top + 2;
    }

    private void addInt(int i) {
        byte[] array = this.itsData.itsICode;
        int top = this.iCodeTop;
        if (top + 4 > array.length) {
            array = increaseICodeCapacity(4);
        }
        array[top] = (byte) (i >>> 24);
        array[top + 1] = (byte) (i >>> 16);
        array[top + 2] = (byte) (i >>> 8);
        array[top + 3] = (byte) i;
        this.iCodeTop = top + 4;
    }

    private int getDoubleIndex(double num) {
        int index = this.doubleTableTop;
        if (index == 0) {
            this.itsData.itsDoubleTable = new double[64];
        } else if (this.itsData.itsDoubleTable.length == index) {
            double[] na = new double[(index * 2)];
            System.arraycopy(this.itsData.itsDoubleTable, 0, na, 0, index);
            this.itsData.itsDoubleTable = na;
        }
        this.itsData.itsDoubleTable[index] = num;
        this.doubleTableTop = index + 1;
        return index;
    }

    private void addGotoOp(int gotoOp) {
        byte[] array = this.itsData.itsICode;
        int top = this.iCodeTop;
        if (top + 3 > array.length) {
            array = increaseICodeCapacity(3);
        }
        array[top] = (byte) gotoOp;
        this.iCodeTop = (top + 1) + 2;
    }

    private void addVarOp(int op, int varIndex) {
        switch (op) {
            case -7:
                break;
            case 55:
            case 56:
                if (varIndex < 128) {
                    addIcode(op == 55 ? -48 : -49);
                    addUint8(varIndex);
                    return;
                }
                break;
            case 156:
                if (varIndex < 128) {
                    addIcode(-61);
                    addUint8(varIndex);
                    return;
                }
                addIndexOp(-60, varIndex);
                return;
            default:
                throw Kit.codeBug();
        }
        addIndexOp(op, varIndex);
    }

    private void addStringOp(int op, String str) {
        addStringPrefix(str);
        if (Icode.validIcode(op)) {
            addIcode(op);
        } else {
            addToken(op);
        }
    }

    private void addIndexOp(int op, int index) {
        addIndexPrefix(index);
        if (Icode.validIcode(op)) {
            addIcode(op);
        } else {
            addToken(op);
        }
    }

    private void addStringPrefix(String str) {
        int index = this.strings.get(str, -1);
        if (index == -1) {
            index = this.strings.size();
            this.strings.put(str, index);
        }
        if (index < 4) {
            addIcode(-41 - index);
        } else if (index <= ByteCode.IMPDEP2) {
            addIcode(-45);
            addUint8(index);
        } else if (index <= 65535) {
            addIcode(-46);
            addUint16(index);
        } else {
            addIcode(-47);
            addInt(index);
        }
    }

    private void addIndexPrefix(int index) {
        if (index < 0) {
            Kit.codeBug();
        }
        if (index < 6) {
            addIcode(-32 - index);
        } else if (index <= ByteCode.IMPDEP2) {
            addIcode(-38);
            addUint8(index);
        } else if (index <= 65535) {
            addIcode(-39);
            addUint16(index);
        } else {
            addIcode(-40);
            addInt(index);
        }
    }

    private void addExceptionHandler(int icodeStart, int icodeEnd, int handlerStart, boolean isFinally, int exceptionObjectLocal, int scopeLocal) {
        int i = 0;
        int top = this.exceptionTableTop;
        int[] table = this.itsData.itsExceptionTable;
        if (table == null) {
            if (top != 0) {
                Kit.codeBug();
            }
            table = new int[12];
            this.itsData.itsExceptionTable = table;
        } else if (table.length == top) {
            table = new int[(table.length * 2)];
            System.arraycopy(this.itsData.itsExceptionTable, 0, table, 0, top);
            this.itsData.itsExceptionTable = table;
        }
        table[top + 0] = icodeStart;
        table[top + 1] = icodeEnd;
        table[top + 2] = handlerStart;
        int i2 = top + 3;
        if (isFinally) {
            i = 1;
        }
        table[i2] = i;
        table[top + 4] = exceptionObjectLocal;
        table[top + 5] = scopeLocal;
        this.exceptionTableTop = top + 6;
    }

    private byte[] increaseICodeCapacity(int extraSize) {
        int capacity = this.itsData.itsICode.length;
        int top = this.iCodeTop;
        if (top + extraSize <= capacity) {
            throw Kit.codeBug();
        }
        capacity *= 2;
        if (top + extraSize > capacity) {
            capacity = top + extraSize;
        }
        byte[] array = new byte[capacity];
        System.arraycopy(this.itsData.itsICode, 0, array, 0, top);
        this.itsData.itsICode = array;
        return array;
    }

    private void stackChange(int change) {
        if (change <= 0) {
            this.stackDepth += change;
            return;
        }
        int newDepth = this.stackDepth + change;
        if (newDepth > this.itsData.itsMaxStack) {
            this.itsData.itsMaxStack = newDepth;
        }
        this.stackDepth = newDepth;
    }

    private int allocLocal() {
        int localSlot = this.localTop;
        this.localTop++;
        if (this.localTop > this.itsData.itsMaxLocals) {
            this.itsData.itsMaxLocals = this.localTop;
        }
        return localSlot;
    }

    private void releaseLocal(int localSlot) {
        this.localTop--;
        if (localSlot != this.localTop) {
            Kit.codeBug();
        }
    }
}
