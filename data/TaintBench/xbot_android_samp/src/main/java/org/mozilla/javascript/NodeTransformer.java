package org.mozilla.javascript;

import java.util.ArrayList;
import java.util.List;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.ScriptNode;

public class NodeTransformer {
    private boolean hasFinally;
    private ObjArray loopEnds;
    private ObjArray loops;

    public final void transform(ScriptNode tree) {
        transformCompilationUnit(tree);
        for (int i = 0; i != tree.getFunctionCount(); i++) {
            transform(tree.getFunctionNode(i));
        }
    }

    private void transformCompilationUnit(ScriptNode tree) {
        boolean createScopeObjects;
        boolean z;
        boolean inStrictMode;
        this.loops = new ObjArray();
        this.loopEnds = new ObjArray();
        this.hasFinally = false;
        if (tree.getType() != 109 || ((FunctionNode) tree).requiresActivation()) {
            createScopeObjects = true;
        } else {
            createScopeObjects = false;
        }
        if (createScopeObjects) {
            z = false;
        } else {
            z = true;
        }
        tree.flattenSymbolTable(z);
        if ((tree instanceof AstRoot) && ((AstRoot) tree).isInStrictMode()) {
            inStrictMode = true;
        } else {
            inStrictMode = false;
        }
        transformCompilationUnit_r(tree, tree, tree, createScopeObjects, inStrictMode);
    }

    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a5  */
    private void transformCompilationUnit_r(org.mozilla.javascript.ast.ScriptNode r48, org.mozilla.javascript.Node r49, org.mozilla.javascript.ast.Scope r50, boolean r51, boolean r52) {
        /*
        r47 = this;
        r8 = 0;
    L_0x0001:
        r39 = 0;
        if (r8 != 0) goto L_0x000c;
    L_0x0005:
        r8 = r49.getFirstChild();
    L_0x0009:
        if (r8 != 0) goto L_0x0013;
    L_0x000b:
        return;
    L_0x000c:
        r39 = r8;
        r8 = r8.getNext();
        goto L_0x0009;
    L_0x0013:
        r44 = r8.getType();
        if (r51 == 0) goto L_0x009e;
    L_0x0019:
        r3 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r0 = r44;
        if (r0 == r3) goto L_0x002b;
    L_0x001f:
        r3 = 132; // 0x84 float:1.85E-43 double:6.5E-322;
        r0 = r44;
        if (r0 == r3) goto L_0x002b;
    L_0x0025:
        r3 = 157; // 0x9d float:2.2E-43 double:7.76E-322;
        r0 = r44;
        if (r0 != r3) goto L_0x009e;
    L_0x002b:
        r3 = r8 instanceof org.mozilla.javascript.ast.Scope;
        if (r3 == 0) goto L_0x009e;
    L_0x002f:
        r35 = r8;
        r35 = (org.mozilla.javascript.ast.Scope) r35;
        r3 = r35.getSymbolTable();
        if (r3 == 0) goto L_0x009e;
    L_0x0039:
        r30 = new org.mozilla.javascript.Node;
        r3 = 157; // 0x9d float:2.2E-43 double:7.76E-322;
        r0 = r44;
        if (r0 != r3) goto L_0x007e;
    L_0x0041:
        r3 = 158; // 0x9e float:2.21E-43 double:7.8E-322;
    L_0x0043:
        r0 = r30;
        r0.m418init(r3);
        r21 = new org.mozilla.javascript.Node;
        r3 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        r0 = r21;
        r0.m418init(r3);
        r0 = r30;
        r1 = r21;
        r0.addChildToBack(r1);
        r3 = r35.getSymbolTable();
        r3 = r3.keySet();
        r3 = r3.iterator();
    L_0x0064:
        r4 = r3.hasNext();
        if (r4 == 0) goto L_0x0081;
    L_0x006a:
        r33 = r3.next();
        r33 = (java.lang.String) r33;
        r4 = 39;
        r0 = r33;
        r4 = org.mozilla.javascript.Node.newString(r4, r0);
        r0 = r21;
        r0.addChildToBack(r4);
        goto L_0x0064;
    L_0x007e:
        r3 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        goto L_0x0043;
    L_0x0081:
        r3 = 0;
        r0 = r35;
        r0.setSymbolTable(r3);
        r37 = r8;
        r0 = r49;
        r1 = r39;
        r2 = r30;
        r8 = replaceCurrent(r0, r1, r8, r2);
        r44 = r8.getType();
        r0 = r30;
        r1 = r37;
        r0.addChildToBack(r1);
    L_0x009e:
        switch(r44) {
            case 3: goto L_0x0109;
            case 4: goto L_0x0136;
            case 7: goto L_0x0373;
            case 8: goto L_0x03dd;
            case 30: goto L_0x02af;
            case 31: goto L_0x03e4;
            case 32: goto L_0x0373;
            case 38: goto L_0x02a6;
            case 39: goto L_0x03e4;
            case 72: goto L_0x012d;
            case 81: goto L_0x00e9;
            case 114: goto L_0x00b6;
            case 120: goto L_0x0214;
            case 121: goto L_0x0214;
            case 122: goto L_0x02e5;
            case 123: goto L_0x00ca;
            case 130: goto L_0x00b6;
            case 131: goto L_0x0109;
            case 132: goto L_0x00b6;
            case 137: goto L_0x0362;
            case 153: goto L_0x02b8;
            case 154: goto L_0x02e5;
            case 155: goto L_0x03e4;
            case 158: goto L_0x02b8;
            default: goto L_0x00a1;
        };
    L_0x00a1:
        r3 = r8 instanceof org.mozilla.javascript.ast.Scope;
        if (r3 == 0) goto L_0x0479;
    L_0x00a5:
        r3 = r8;
        r3 = (org.mozilla.javascript.ast.Scope) r3;
        r9 = r3;
    L_0x00a9:
        r6 = r47;
        r7 = r48;
        r10 = r51;
        r11 = r52;
        r6.transformCompilationUnit_r(r7, r8, r9, r10, r11);
        goto L_0x0001;
    L_0x00b6:
        r0 = r47;
        r3 = r0.loops;
        r3.push(r8);
        r0 = r47;
        r4 = r0.loopEnds;
        r3 = r8;
        r3 = (org.mozilla.javascript.ast.Jump) r3;
        r3 = r3.target;
        r4.push(r3);
        goto L_0x00a1;
    L_0x00ca:
        r0 = r47;
        r3 = r0.loops;
        r3.push(r8);
        r29 = r8.getNext();
        r3 = r29.getType();
        r4 = 3;
        if (r3 == r4) goto L_0x00df;
    L_0x00dc:
        org.mozilla.javascript.Kit.codeBug();
    L_0x00df:
        r0 = r47;
        r3 = r0.loopEnds;
        r0 = r29;
        r3.push(r0);
        goto L_0x00a1;
    L_0x00e9:
        r26 = r8;
        r26 = (org.mozilla.javascript.ast.Jump) r26;
        r17 = r26.getFinally();
        if (r17 == 0) goto L_0x00a1;
    L_0x00f3:
        r3 = 1;
        r0 = r47;
        r0.hasFinally = r3;
        r0 = r47;
        r3 = r0.loops;
        r3.push(r8);
        r0 = r47;
        r3 = r0.loopEnds;
        r0 = r17;
        r3.push(r0);
        goto L_0x00a1;
    L_0x0109:
        r0 = r47;
        r3 = r0.loopEnds;
        r3 = r3.isEmpty();
        if (r3 != 0) goto L_0x00a1;
    L_0x0113:
        r0 = r47;
        r3 = r0.loopEnds;
        r3 = r3.peek();
        if (r3 != r8) goto L_0x00a1;
    L_0x011d:
        r0 = r47;
        r3 = r0.loopEnds;
        r3.pop();
        r0 = r47;
        r3 = r0.loops;
        r3.pop();
        goto L_0x00a1;
    L_0x012d:
        r3 = r48;
        r3 = (org.mozilla.javascript.ast.FunctionNode) r3;
        r3.addResumptionPoint(r8);
        goto L_0x00a1;
    L_0x0136:
        r3 = r48.getType();
        r4 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r3 != r4) goto L_0x01b8;
    L_0x013e:
        r3 = r48;
        r3 = (org.mozilla.javascript.ast.FunctionNode) r3;
        r3 = r3.isGenerator();
        if (r3 == 0) goto L_0x01b8;
    L_0x0148:
        r22 = 1;
    L_0x014a:
        if (r22 == 0) goto L_0x0152;
    L_0x014c:
        r3 = 20;
        r4 = 1;
        r8.putIntProp(r3, r4);
    L_0x0152:
        r0 = r47;
        r3 = r0.hasFinally;
        if (r3 == 0) goto L_0x00a1;
    L_0x0158:
        r46 = 0;
        r0 = r47;
        r3 = r0.loops;
        r3 = r3.size();
        r19 = r3 + -1;
    L_0x0164:
        if (r19 < 0) goto L_0x01c4;
    L_0x0166:
        r0 = r47;
        r3 = r0.loops;
        r0 = r19;
        r31 = r3.get(r0);
        r31 = (org.mozilla.javascript.Node) r31;
        r16 = r31.getType();
        r3 = 81;
        r0 = r16;
        if (r0 == r3) goto L_0x0182;
    L_0x017c:
        r3 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r16;
        if (r0 != r3) goto L_0x01b5;
    L_0x0182:
        r3 = 81;
        r0 = r16;
        if (r0 != r3) goto L_0x01bb;
    L_0x0188:
        r24 = new org.mozilla.javascript.ast.Jump;
        r3 = 135; // 0x87 float:1.89E-43 double:6.67E-322;
        r0 = r24;
        r0.m1108init(r3);
        r31 = (org.mozilla.javascript.ast.Jump) r31;
        r25 = r31.getFinally();
        r0 = r25;
        r1 = r24;
        r1.target = r0;
        r45 = r24;
    L_0x019f:
        if (r46 != 0) goto L_0x01ae;
    L_0x01a1:
        r46 = new org.mozilla.javascript.Node;
        r3 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r4 = r8.getLineno();
        r0 = r46;
        r0.m419init(r3, r4);
    L_0x01ae:
        r0 = r46;
        r1 = r45;
        r0.addChildToBack(r1);
    L_0x01b5:
        r19 = r19 + -1;
        goto L_0x0164;
    L_0x01b8:
        r22 = 0;
        goto L_0x014a;
    L_0x01bb:
        r45 = new org.mozilla.javascript.Node;
        r3 = 3;
        r0 = r45;
        r0.m418init(r3);
        goto L_0x019f;
    L_0x01c4:
        if (r46 == 0) goto L_0x00a1;
    L_0x01c6:
        r42 = r8;
        r41 = r42.getFirstChild();
        r0 = r49;
        r1 = r39;
        r2 = r46;
        r36 = replaceCurrent(r0, r1, r8, r2);
        if (r41 == 0) goto L_0x01da;
    L_0x01d8:
        if (r22 == 0) goto L_0x01e5;
    L_0x01da:
        r0 = r46;
        r1 = r42;
        r0.addChildToBack(r1);
        r8 = r36;
        goto L_0x0001;
    L_0x01e5:
        r5 = new org.mozilla.javascript.Node;
        r3 = 134; // 0x86 float:1.88E-43 double:6.6E-322;
        r0 = r41;
        r5.m420init(r3, r0);
        r0 = r46;
        r0.addChildToFront(r5);
        r42 = new org.mozilla.javascript.Node;
        r3 = 64;
        r0 = r42;
        r0.m418init(r3);
        r0 = r46;
        r1 = r42;
        r0.addChildToBack(r1);
        r3 = r47;
        r4 = r48;
        r6 = r50;
        r7 = r51;
        r8 = r52;
        r3.transformCompilationUnit_r(r4, r5, r6, r7, r8);
        r8 = r36;
        goto L_0x0001;
    L_0x0214:
        r26 = r8;
        r26 = (org.mozilla.javascript.ast.Jump) r26;
        r27 = r26.getJumpStatement();
        if (r27 != 0) goto L_0x0221;
    L_0x021e:
        org.mozilla.javascript.Kit.codeBug();
    L_0x0221:
        r0 = r47;
        r3 = r0.loops;
        r19 = r3.size();
    L_0x0229:
        if (r19 != 0) goto L_0x0230;
    L_0x022b:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x0230:
        r19 = r19 + -1;
        r0 = r47;
        r3 = r0.loops;
        r0 = r19;
        r31 = r3.get(r0);
        r31 = (org.mozilla.javascript.Node) r31;
        r0 = r31;
        r1 = r27;
        if (r0 != r1) goto L_0x025a;
    L_0x0244:
        r3 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r0 = r44;
        if (r0 != r3) goto L_0x029d;
    L_0x024a:
        r0 = r27;
        r3 = r0.target;
        r0 = r26;
        r0.target = r3;
    L_0x0252:
        r3 = 5;
        r0 = r26;
        r0.setType(r3);
        goto L_0x00a1;
    L_0x025a:
        r16 = r31.getType();
        r3 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r16;
        if (r0 != r3) goto L_0x0277;
    L_0x0264:
        r29 = new org.mozilla.javascript.Node;
        r3 = 3;
        r0 = r29;
        r0.m418init(r3);
        r0 = r49;
        r1 = r39;
        r2 = r29;
        r39 = addBeforeCurrent(r0, r1, r8, r2);
        goto L_0x0229;
    L_0x0277:
        r3 = 81;
        r0 = r16;
        if (r0 != r3) goto L_0x0229;
    L_0x027d:
        r43 = r31;
        r43 = (org.mozilla.javascript.ast.Jump) r43;
        r23 = new org.mozilla.javascript.ast.Jump;
        r3 = 135; // 0x87 float:1.89E-43 double:6.67E-322;
        r0 = r23;
        r0.m1108init(r3);
        r3 = r43.getFinally();
        r0 = r23;
        r0.target = r3;
        r0 = r49;
        r1 = r39;
        r2 = r23;
        r39 = addBeforeCurrent(r0, r1, r8, r2);
        goto L_0x0229;
    L_0x029d:
        r3 = r27.getContinue();
        r0 = r26;
        r0.target = r3;
        goto L_0x0252;
    L_0x02a6:
        r0 = r47;
        r1 = r48;
        r0.visitCall(r8, r1);
        goto L_0x00a1;
    L_0x02af:
        r0 = r47;
        r1 = r48;
        r0.visitNew(r8, r1);
        goto L_0x00a1;
    L_0x02b8:
        r12 = r8.getFirstChild();
        r3 = r12.getType();
        r4 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        if (r3 != r4) goto L_0x02e5;
    L_0x02c4:
        r3 = r48.getType();
        r4 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r3 != r4) goto L_0x02d6;
    L_0x02cc:
        r3 = r48;
        r3 = (org.mozilla.javascript.ast.FunctionNode) r3;
        r3 = r3.requiresActivation();
        if (r3 == 0) goto L_0x02e3;
    L_0x02d6:
        r13 = 1;
    L_0x02d7:
        r0 = r47;
        r1 = r49;
        r2 = r39;
        r8 = r0.visitLet(r13, r1, r2, r8);
        goto L_0x00a1;
    L_0x02e3:
        r13 = 0;
        goto L_0x02d7;
    L_0x02e5:
        r40 = new org.mozilla.javascript.Node;
        r3 = 129; // 0x81 float:1.81E-43 double:6.37E-322;
        r0 = r40;
        r0.m418init(r3);
        r14 = r8.getFirstChild();
    L_0x02f2:
        if (r14 == 0) goto L_0x0356;
    L_0x02f4:
        r31 = r14;
        r14 = r14.getNext();
        r3 = r31.getType();
        r4 = 39;
        if (r3 != r4) goto L_0x0349;
    L_0x0302:
        r3 = r31.hasChildren();
        if (r3 == 0) goto L_0x02f2;
    L_0x0308:
        r20 = r31.getFirstChild();
        r0 = r31;
        r1 = r20;
        r0.removeChild(r1);
        r3 = 49;
        r0 = r31;
        r0.setType(r3);
        r32 = new org.mozilla.javascript.Node;
        r3 = 154; // 0x9a float:2.16E-43 double:7.6E-322;
        r0 = r44;
        if (r0 != r3) goto L_0x0346;
    L_0x0322:
        r3 = 155; // 0x9b float:2.17E-43 double:7.66E-322;
    L_0x0324:
        r0 = r32;
        r1 = r31;
        r2 = r20;
        r0.m422init(r3, r1, r2);
        r31 = r32;
    L_0x032f:
        r38 = new org.mozilla.javascript.Node;
        r3 = 133; // 0x85 float:1.86E-43 double:6.57E-322;
        r4 = r8.getLineno();
        r0 = r38;
        r1 = r31;
        r0.m421init(r3, r1, r4);
        r0 = r40;
        r1 = r38;
        r0.addChildToBack(r1);
        goto L_0x02f2;
    L_0x0346:
        r3 = 8;
        goto L_0x0324;
    L_0x0349:
        r3 = r31.getType();
        r4 = 158; // 0x9e float:2.21E-43 double:7.8E-322;
        if (r3 == r4) goto L_0x032f;
    L_0x0351:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x0356:
        r0 = r49;
        r1 = r39;
        r2 = r40;
        r8 = replaceCurrent(r0, r1, r8, r2);
        goto L_0x00a1;
    L_0x0362:
        r3 = r8.getString();
        r0 = r50;
        r15 = r0.getDefiningScope(r3);
        if (r15 == 0) goto L_0x00a1;
    L_0x036e:
        r8.setScope(r15);
        goto L_0x00a1;
    L_0x0373:
        r12 = r8.getFirstChild();
        r3 = 7;
        r0 = r44;
        if (r0 != r3) goto L_0x03b7;
    L_0x037c:
        r3 = r12.getType();
        r4 = 26;
        if (r3 != r4) goto L_0x0389;
    L_0x0384:
        r12 = r12.getFirstChild();
        goto L_0x037c;
    L_0x0389:
        r3 = r12.getType();
        r4 = 12;
        if (r3 == r4) goto L_0x0399;
    L_0x0391:
        r3 = r12.getType();
        r4 = 13;
        if (r3 != r4) goto L_0x03b7;
    L_0x0399:
        r18 = r12.getFirstChild();
        r28 = r12.getLastChild();
        r3 = r18.getType();
        r4 = 39;
        if (r3 != r4) goto L_0x03c6;
    L_0x03a9:
        r3 = r18.getString();
        r4 = "undefined";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x03c6;
    L_0x03b5:
        r12 = r28;
    L_0x03b7:
        r3 = r12.getType();
        r4 = 33;
        if (r3 != r4) goto L_0x00a1;
    L_0x03bf:
        r3 = 34;
        r12.setType(r3);
        goto L_0x00a1;
    L_0x03c6:
        r3 = r28.getType();
        r4 = 39;
        if (r3 != r4) goto L_0x03b7;
    L_0x03ce:
        r3 = r28.getString();
        r4 = "undefined";
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x03b7;
    L_0x03da:
        r12 = r18;
        goto L_0x03b7;
    L_0x03dd:
        if (r52 == 0) goto L_0x03e4;
    L_0x03df:
        r3 = 73;
        r8.setType(r3);
    L_0x03e4:
        if (r51 != 0) goto L_0x00a1;
    L_0x03e6:
        r3 = 39;
        r0 = r44;
        if (r0 != r3) goto L_0x0414;
    L_0x03ec:
        r34 = r8;
    L_0x03ee:
        r3 = r34.getScope();
        if (r3 != 0) goto L_0x00a1;
    L_0x03f4:
        r33 = r34.getString();
        r0 = r50;
        r1 = r33;
        r15 = r0.getDefiningScope(r1);
        if (r15 == 0) goto L_0x00a1;
    L_0x0402:
        r0 = r34;
        r0.setScope(r15);
        r3 = 39;
        r0 = r44;
        if (r0 != r3) goto L_0x042b;
    L_0x040d:
        r3 = 55;
        r8.setType(r3);
        goto L_0x00a1;
    L_0x0414:
        r34 = r8.getFirstChild();
        r3 = r34.getType();
        r4 = 49;
        if (r3 == r4) goto L_0x03ee;
    L_0x0420:
        r3 = 31;
        r0 = r44;
        if (r0 == r3) goto L_0x00a1;
    L_0x0426:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x042b:
        r3 = 8;
        r0 = r44;
        if (r0 == r3) goto L_0x0437;
    L_0x0431:
        r3 = 73;
        r0 = r44;
        if (r0 != r3) goto L_0x0445;
    L_0x0437:
        r3 = 56;
        r8.setType(r3);
        r3 = 41;
        r0 = r34;
        r0.setType(r3);
        goto L_0x00a1;
    L_0x0445:
        r3 = 155; // 0x9b float:2.17E-43 double:7.66E-322;
        r0 = r44;
        if (r0 != r3) goto L_0x0459;
    L_0x044b:
        r3 = 156; // 0x9c float:2.19E-43 double:7.7E-322;
        r8.setType(r3);
        r3 = 41;
        r0 = r34;
        r0.setType(r3);
        goto L_0x00a1;
    L_0x0459:
        r3 = 31;
        r0 = r44;
        if (r0 != r3) goto L_0x0474;
    L_0x045f:
        r31 = new org.mozilla.javascript.Node;
        r3 = 44;
        r0 = r31;
        r0.m418init(r3);
        r0 = r49;
        r1 = r39;
        r2 = r31;
        r8 = replaceCurrent(r0, r1, r8, r2);
        goto L_0x00a1;
    L_0x0474:
        r3 = org.mozilla.javascript.Kit.codeBug();
        throw r3;
    L_0x0479:
        r9 = r50;
        goto L_0x00a9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.NodeTransformer.transformCompilationUnit_r(org.mozilla.javascript.ast.ScriptNode, org.mozilla.javascript.Node, org.mozilla.javascript.ast.Scope, boolean, boolean):void");
    }

    /* access modifiers changed from: protected */
    public void visitNew(Node node, ScriptNode tree) {
    }

    /* access modifiers changed from: protected */
    public void visitCall(Node node, ScriptNode tree) {
    }

    /* access modifiers changed from: protected */
    public Node visitLet(boolean createWith, Node parent, Node previous, Node scopeNode) {
        Node result;
        Node vars = scopeNode.getFirstChild();
        Node body = vars.getNext();
        scopeNode.removeChild(vars);
        scopeNode.removeChild(body);
        boolean isExpression = scopeNode.getType() == 158;
        Node v;
        Node body2;
        Node current;
        Node c;
        Node init;
        if (createWith) {
            result = replaceCurrent(parent, previous, scopeNode, new Node(isExpression ? 159 : 129));
            ArrayList<Object> list = new ArrayList();
            Node objectLiteral = new Node(66);
            v = vars.getFirstChild();
            body2 = body;
            while (v != null) {
                current = v;
                if (current.getType() == 158) {
                    List<?> destructuringNames = (List) current.getProp(22);
                    c = current.getFirstChild();
                    if (c.getType() != 153) {
                        throw Kit.codeBug();
                    }
                    if (isExpression) {
                        body = new Node(89, c.getNext(), body2);
                    } else {
                        body = new Node(129, new Node(133, c.getNext()), body2);
                    }
                    if (destructuringNames != null) {
                        list.addAll(destructuringNames);
                        for (int i = 0; i < destructuringNames.size(); i++) {
                            objectLiteral.addChildToBack(new Node(126, Node.newNumber(0.0d)));
                        }
                    }
                    current = c.getFirstChild();
                } else {
                    body = body2;
                }
                if (current.getType() != 39) {
                    throw Kit.codeBug();
                }
                list.add(ScriptRuntime.getIndexObject(current.getString()));
                init = current.getFirstChild();
                if (init == null) {
                    init = new Node(126, Node.newNumber(0.0d));
                }
                objectLiteral.addChildToBack(init);
                v = v.getNext();
                body2 = body;
            }
            objectLiteral.putProp(12, list.toArray());
            result.addChildToBack(new Node(2, objectLiteral));
            result.addChildToBack(new Node(123, body2));
            result.addChildToBack(new Node(3));
            body = body2;
        } else {
            result = replaceCurrent(parent, previous, scopeNode, new Node(isExpression ? 89 : 129));
            Node newVars = new Node(89);
            v = vars.getFirstChild();
            body2 = body;
            while (v != null) {
                current = v;
                if (current.getType() == 158) {
                    c = current.getFirstChild();
                    if (c.getType() != 153) {
                        throw Kit.codeBug();
                    }
                    if (isExpression) {
                        body = new Node(89, c.getNext(), body2);
                    } else {
                        body = new Node(129, new Node(133, c.getNext()), body2);
                    }
                    Scope.joinScopes((Scope) current, (Scope) scopeNode);
                    current = c.getFirstChild();
                } else {
                    body = body2;
                }
                if (current.getType() != 39) {
                    throw Kit.codeBug();
                }
                Node stringNode = Node.newString(current.getString());
                stringNode.setScope((Scope) scopeNode);
                init = current.getFirstChild();
                if (init == null) {
                    init = new Node(126, Node.newNumber(0.0d));
                }
                newVars.addChildToBack(new Node(56, stringNode, init));
                v = v.getNext();
                body2 = body;
            }
            Scope scopeParent;
            if (isExpression) {
                result.addChildToBack(newVars);
                scopeNode.setType(89);
                result.addChildToBack(scopeNode);
                scopeNode.addChildToBack(body2);
                if (body2 instanceof Scope) {
                    scopeParent = ((Scope) body2).getParentScope();
                    ((Scope) body2).setParentScope((Scope) scopeNode);
                    ((Scope) scopeNode).setParentScope(scopeParent);
                    body = body2;
                }
            } else {
                result.addChildToBack(new Node(133, newVars));
                scopeNode.setType(129);
                result.addChildToBack(scopeNode);
                scopeNode.addChildrenToBack(body2);
                if (body2 instanceof Scope) {
                    scopeParent = ((Scope) body2).getParentScope();
                    ((Scope) body2).setParentScope((Scope) scopeNode);
                    ((Scope) scopeNode).setParentScope(scopeParent);
                }
            }
            body = body2;
        }
        return result;
    }

    private static Node addBeforeCurrent(Node parent, Node previous, Node current, Node toAdd) {
        if (previous == null) {
            if (current != parent.getFirstChild()) {
                Kit.codeBug();
            }
            parent.addChildToFront(toAdd);
        } else {
            if (current != previous.getNext()) {
                Kit.codeBug();
            }
            parent.addChildAfter(toAdd, previous);
        }
        return toAdd;
    }

    private static Node replaceCurrent(Node parent, Node previous, Node current, Node replacement) {
        if (previous == null) {
            if (current != parent.getFirstChild()) {
                Kit.codeBug();
            }
            parent.replaceChild(current, replacement);
        } else if (previous.next == current) {
            parent.replaceChildAfter(previous, replacement);
        } else {
            parent.replaceChild(current, replacement);
        }
        return replacement;
    }
}
