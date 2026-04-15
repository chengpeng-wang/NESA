package org.mozilla.javascript.optimizer;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ObjArray;
import org.mozilla.javascript.ObjToIntMap;
import org.mozilla.javascript.ObjToIntMap.Iterator;
import org.mozilla.javascript.ast.Jump;

class Block {
    static final boolean DEBUG = false;
    private static int debug_blockCount;
    private int itsBlockID;
    private int itsEndNodeIndex;
    private BitSet itsLiveOnEntrySet;
    private BitSet itsLiveOnExitSet;
    private BitSet itsNotDefSet;
    private Block[] itsPredecessors;
    private int itsStartNodeIndex;
    private Block[] itsSuccessors;
    private BitSet itsUseBeforeDefSet;

    private static class FatBlock {
        private ObjToIntMap predecessors;
        Block realBlock;
        private ObjToIntMap successors;

        private FatBlock() {
            this.successors = new ObjToIntMap();
            this.predecessors = new ObjToIntMap();
        }

        private static Block[] reduceToArray(ObjToIntMap map) {
            Block[] result = null;
            if (!map.isEmpty()) {
                result = new Block[map.size()];
                int i = 0;
                Iterator iter = map.newIterator();
                iter.start();
                while (!iter.done()) {
                    int i2 = i + 1;
                    result[i] = ((FatBlock) iter.getKey()).realBlock;
                    iter.next();
                    i = i2;
                }
            }
            return result;
        }

        /* access modifiers changed from: 0000 */
        public void addSuccessor(FatBlock b) {
            this.successors.put(b, 0);
        }

        /* access modifiers changed from: 0000 */
        public void addPredecessor(FatBlock b) {
            this.predecessors.put(b, 0);
        }

        /* access modifiers changed from: 0000 */
        public Block[] getSuccessors() {
            return reduceToArray(this.successors);
        }

        /* access modifiers changed from: 0000 */
        public Block[] getPredecessors() {
            return reduceToArray(this.predecessors);
        }
    }

    Block(int startNodeIndex, int endNodeIndex) {
        this.itsStartNodeIndex = startNodeIndex;
        this.itsEndNodeIndex = endNodeIndex;
    }

    static void runFlowAnalyzes(OptFunctionNode fn, Node[] statementNodes) {
        int i;
        int paramCount = fn.fnode.getParamCount();
        int varCount = fn.fnode.getParamAndVarCount();
        int[] varTypes = new int[varCount];
        for (i = 0; i != paramCount; i++) {
            varTypes[i] = 3;
        }
        for (i = paramCount; i != varCount; i++) {
            varTypes[i] = 0;
        }
        Block[] theBlocks = buildBlocks(statementNodes);
        reachingDefDataFlow(fn, statementNodes, theBlocks, varTypes);
        typeFlow(fn, statementNodes, theBlocks, varTypes);
        for (i = paramCount; i != varCount; i++) {
            if (varTypes[i] == 1) {
                fn.setIsNumberVar(i);
            }
        }
    }

    private static Block[] buildBlocks(Node[] statementNodes) {
        int i;
        FatBlock fb;
        Map<Node, FatBlock> theTargetBlocks = new HashMap();
        ObjArray theBlocks = new ObjArray();
        int beginNodeIndex = 0;
        for (i = 0; i < statementNodes.length; i++) {
            switch (statementNodes[i].getType()) {
                case 5:
                case 6:
                case 7:
                    fb = newFatBlock(beginNodeIndex, i);
                    if (statementNodes[beginNodeIndex].getType() == 131) {
                        theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
                    }
                    theBlocks.add(fb);
                    beginNodeIndex = i + 1;
                    break;
                case 131:
                    if (i == beginNodeIndex) {
                        break;
                    }
                    fb = newFatBlock(beginNodeIndex, i - 1);
                    if (statementNodes[beginNodeIndex].getType() == 131) {
                        theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
                    }
                    theBlocks.add(fb);
                    beginNodeIndex = i;
                    break;
                default:
                    break;
            }
        }
        if (beginNodeIndex != statementNodes.length) {
            fb = newFatBlock(beginNodeIndex, statementNodes.length - 1);
            if (statementNodes[beginNodeIndex].getType() == 131) {
                theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
            }
            theBlocks.add(fb);
        }
        i = 0;
        while (i < theBlocks.size()) {
            fb = (FatBlock) theBlocks.get(i);
            Node blockEndNode = statementNodes[fb.realBlock.itsEndNodeIndex];
            int blockEndNodeType = blockEndNode.getType();
            if (blockEndNodeType != 5 && i < theBlocks.size() - 1) {
                FatBlock fallThruTarget = (FatBlock) theBlocks.get(i + 1);
                fb.addSuccessor(fallThruTarget);
                fallThruTarget.addPredecessor(fb);
            }
            if (blockEndNodeType == 7 || blockEndNodeType == 6 || blockEndNodeType == 5) {
                Node target = ((Jump) blockEndNode).target;
                FatBlock branchTargetBlock = (FatBlock) theTargetBlocks.get(target);
                target.putProp(6, branchTargetBlock.realBlock);
                fb.addSuccessor(branchTargetBlock);
                branchTargetBlock.addPredecessor(fb);
            }
            i++;
        }
        Block[] result = new Block[theBlocks.size()];
        for (i = 0; i < theBlocks.size(); i++) {
            fb = (FatBlock) theBlocks.get(i);
            Block b = fb.realBlock;
            b.itsSuccessors = fb.getSuccessors();
            b.itsPredecessors = fb.getPredecessors();
            b.itsBlockID = i;
            result[i] = b;
        }
        return result;
    }

    private static FatBlock newFatBlock(int startNodeIndex, int endNodeIndex) {
        FatBlock fb = new FatBlock();
        fb.realBlock = new Block(startNodeIndex, endNodeIndex);
        return fb;
    }

    private static String toString(Block[] blockList, Node[] statementNodes) {
        return null;
    }

    private static void reachingDefDataFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes) {
        for (Block initLiveOnEntrySets : theBlocks) {
            initLiveOnEntrySets.initLiveOnEntrySets(fn, statementNodes);
        }
        boolean[] visit = new boolean[theBlocks.length];
        boolean[] doneOnce = new boolean[theBlocks.length];
        int vIndex = theBlocks.length - 1;
        boolean needRescan = false;
        visit[vIndex] = true;
        while (true) {
            if (visit[vIndex] || !doneOnce[vIndex]) {
                doneOnce[vIndex] = true;
                visit[vIndex] = false;
                if (theBlocks[vIndex].doReachedUseDataFlow()) {
                    Block[] pred = theBlocks[vIndex].itsPredecessors;
                    if (pred != null) {
                        for (Block initLiveOnEntrySets2 : pred) {
                            int i;
                            int index = initLiveOnEntrySets2.itsBlockID;
                            visit[index] = true;
                            if (index > vIndex) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            needRescan |= i;
                        }
                    }
                }
            }
            if (vIndex != 0) {
                vIndex--;
            } else if (needRescan) {
                vIndex = theBlocks.length - 1;
                needRescan = false;
            } else {
                theBlocks[0].markAnyTypeVariables(varTypes);
                return;
            }
        }
    }

    private static void typeFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes) {
        boolean[] visit = new boolean[theBlocks.length];
        boolean[] doneOnce = new boolean[theBlocks.length];
        int vIndex = 0;
        boolean needRescan = false;
        visit[0] = true;
        while (true) {
            if (visit[vIndex] || !doneOnce[vIndex]) {
                doneOnce[vIndex] = true;
                visit[vIndex] = false;
                if (theBlocks[vIndex].doTypeFlow(fn, statementNodes, varTypes)) {
                    Block[] succ = theBlocks[vIndex].itsSuccessors;
                    if (succ != null) {
                        for (Block block : succ) {
                            int i;
                            int index = block.itsBlockID;
                            visit[index] = true;
                            if (index < vIndex) {
                                i = 1;
                            } else {
                                i = 0;
                            }
                            needRescan |= i;
                        }
                    }
                }
            }
            if (vIndex != theBlocks.length - 1) {
                vIndex++;
            } else if (needRescan) {
                vIndex = 0;
                needRescan = false;
            } else {
                return;
            }
        }
    }

    private static boolean assignType(int[] varTypes, int index, int type) {
        int prev = varTypes[index];
        int i = varTypes[index] | type;
        varTypes[index] = i;
        return prev != i;
    }

    private void markAnyTypeVariables(int[] varTypes) {
        for (int i = 0; i != varTypes.length; i++) {
            if (this.itsLiveOnEntrySet.get(i)) {
                assignType(varTypes, i, 3);
            }
        }
    }

    private void lookForVariableAccess(OptFunctionNode fn, Node n) {
        int varIndex;
        Node child;
        switch (n.getType()) {
            case 55:
                varIndex = fn.getVarIndex(n);
                if (!this.itsNotDefSet.get(varIndex)) {
                    this.itsUseBeforeDefSet.set(varIndex);
                    return;
                }
                return;
            case 56:
            case 156:
                lookForVariableAccess(fn, n.getFirstChild().getNext());
                this.itsNotDefSet.set(fn.getVarIndex(n));
                return;
            case 106:
            case 107:
                child = n.getFirstChild();
                if (child.getType() == 55) {
                    varIndex = fn.getVarIndex(child);
                    if (!this.itsNotDefSet.get(varIndex)) {
                        this.itsUseBeforeDefSet.set(varIndex);
                    }
                    this.itsNotDefSet.set(varIndex);
                    return;
                }
                lookForVariableAccess(fn, child);
                return;
            case 137:
                varIndex = fn.fnode.getIndexForNameNode(n);
                if (varIndex > -1 && !this.itsNotDefSet.get(varIndex)) {
                    this.itsUseBeforeDefSet.set(varIndex);
                    return;
                }
                return;
            default:
                for (child = n.getFirstChild(); child != null; child = child.getNext()) {
                    lookForVariableAccess(fn, child);
                }
                return;
        }
    }

    private void initLiveOnEntrySets(OptFunctionNode fn, Node[] statementNodes) {
        int listLength = fn.getVarCount();
        this.itsUseBeforeDefSet = new BitSet(listLength);
        this.itsNotDefSet = new BitSet(listLength);
        this.itsLiveOnEntrySet = new BitSet(listLength);
        this.itsLiveOnExitSet = new BitSet(listLength);
        for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; i++) {
            lookForVariableAccess(fn, statementNodes[i]);
        }
        this.itsNotDefSet.flip(0, listLength);
    }

    private boolean doReachedUseDataFlow() {
        this.itsLiveOnExitSet.clear();
        if (this.itsSuccessors != null) {
            for (Block block : this.itsSuccessors) {
                this.itsLiveOnExitSet.or(block.itsLiveOnEntrySet);
            }
        }
        return updateEntrySet(this.itsLiveOnEntrySet, this.itsLiveOnExitSet, this.itsUseBeforeDefSet, this.itsNotDefSet);
    }

    private boolean updateEntrySet(BitSet entrySet, BitSet exitSet, BitSet useBeforeDef, BitSet notDef) {
        int card = entrySet.cardinality();
        entrySet.or(exitSet);
        entrySet.and(notDef);
        entrySet.or(useBeforeDef);
        return entrySet.cardinality() != card;
    }

    private static int findExpressionType(OptFunctionNode fn, Node n, int[] varTypes) {
        Node child;
        switch (n.getType()) {
            case 8:
            case 35:
            case 37:
            case 56:
            case 89:
            case 156:
                return findExpressionType(fn, n.getLastChild(), varTypes);
            case 9:
            case 10:
            case 11:
            case 18:
            case 19:
            case 20:
            case 22:
            case 23:
            case 24:
            case 25:
            case 27:
            case 28:
            case 29:
            case 40:
            case 106:
            case 107:
                return 1;
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 26:
            case 31:
            case 44:
            case 45:
            case 46:
            case 47:
            case 52:
            case 53:
            case 69:
                return 3;
            case 21:
                child = n.getFirstChild();
                return findExpressionType(fn, child, varTypes) | findExpressionType(fn, child.getNext(), varTypes);
            case 30:
            case 38:
            case 70:
                return 3;
            case 32:
            case 41:
            case 137:
                return 3;
            case 33:
            case 36:
            case 39:
            case 43:
                return 3;
            case 42:
            case 48:
            case 65:
            case 66:
            case 157:
                return 3;
            case 55:
                return varTypes[fn.getVarIndex(n)];
            case 102:
                Node ifTrue = n.getFirstChild().getNext();
                return findExpressionType(fn, ifTrue, varTypes) | findExpressionType(fn, ifTrue.getNext(), varTypes);
            case 104:
            case 105:
                child = n.getFirstChild();
                return findExpressionType(fn, child, varTypes) | findExpressionType(fn, child.getNext(), varTypes);
            case 126:
                return 3;
            default:
                return 3;
        }
    }

    private static boolean findDefPoints(OptFunctionNode fn, Node n, int[] varTypes) {
        boolean result = false;
        Node first = n.getFirstChild();
        for (Node next = first; next != null; next = next.getNext()) {
            result |= findDefPoints(fn, next, varTypes);
        }
        int i;
        switch (n.getType()) {
            case 56:
            case 156:
                int theType = findExpressionType(fn, first.getNext(), varTypes);
                i = fn.getVarIndex(n);
                if (n.getType() == 56 && fn.fnode.getParamAndVarConst()[i]) {
                    return result;
                }
                return result | assignType(varTypes, i, theType);
            case 106:
            case 107:
                if (first.getType() != 55) {
                    return result;
                }
                i = fn.getVarIndex(first);
                if (fn.fnode.getParamAndVarConst()[i]) {
                    return result;
                }
                return result | assignType(varTypes, i, 1);
            default:
                return result;
        }
    }

    private boolean doTypeFlow(OptFunctionNode fn, Node[] statementNodes, int[] varTypes) {
        boolean changed = false;
        for (int i = this.itsStartNodeIndex; i <= this.itsEndNodeIndex; i++) {
            Node n = statementNodes[i];
            if (n != null) {
                changed |= findDefPoints(fn, n, varTypes);
            }
        }
        return changed;
    }

    private void printLiveOnEntrySet(OptFunctionNode fn) {
    }
}
