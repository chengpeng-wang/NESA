package org.mozilla.classfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.mozilla.javascript.ObjArray;
import org.mozilla.javascript.UintMap;

public class ClassFileWriter {
    public static final short ACC_ABSTRACT = (short) 1024;
    public static final short ACC_FINAL = (short) 16;
    public static final short ACC_NATIVE = (short) 256;
    public static final short ACC_PRIVATE = (short) 2;
    public static final short ACC_PROTECTED = (short) 4;
    public static final short ACC_PUBLIC = (short) 1;
    public static final short ACC_STATIC = (short) 8;
    public static final short ACC_SUPER = (short) 32;
    public static final short ACC_SYNCHRONIZED = (short) 32;
    public static final short ACC_TRANSIENT = (short) 128;
    public static final short ACC_VOLATILE = (short) 64;
    private static final boolean DEBUGCODE = false;
    private static final boolean DEBUGLABELS = false;
    private static final boolean DEBUGSTACK = false;
    private static final int ExceptionTableSize = 4;
    private static final int FileHeaderConstant = -889275714;
    private static final boolean GenerateStackMap;
    private static final int LineNumberTableSize = 16;
    private static final int MIN_FIXUP_TABLE_SIZE = 40;
    private static final int MIN_LABEL_TABLE_SIZE = 32;
    private static final int MajorVersion;
    private static final int MinorVersion;
    private static final int SuperBlockStartsSize = 4;
    private String generatedClassName;
    /* access modifiers changed from: private */
    public byte[] itsCodeBuffer = new byte[256];
    /* access modifiers changed from: private */
    public int itsCodeBufferTop;
    /* access modifiers changed from: private */
    public ConstantPool itsConstantPool;
    private ClassFileMethod itsCurrentMethod;
    /* access modifiers changed from: private */
    public ExceptionTableEntry[] itsExceptionTable;
    /* access modifiers changed from: private */
    public int itsExceptionTableTop;
    private ObjArray itsFields = new ObjArray();
    private long[] itsFixupTable;
    private int itsFixupTableTop;
    private short itsFlags;
    private ObjArray itsInterfaces = new ObjArray();
    /* access modifiers changed from: private */
    public UintMap itsJumpFroms = null;
    private int[] itsLabelTable;
    private int itsLabelTableTop;
    private int[] itsLineNumberTable;
    private int itsLineNumberTableTop;
    /* access modifiers changed from: private */
    public short itsMaxLocals;
    /* access modifiers changed from: private */
    public short itsMaxStack;
    private ObjArray itsMethods = new ObjArray();
    private short itsSourceFileNameIndex;
    private short itsStackTop;
    /* access modifiers changed from: private */
    public int[] itsSuperBlockStarts = null;
    /* access modifiers changed from: private */
    public int itsSuperBlockStartsTop = 0;
    private short itsSuperClassIndex;
    /* access modifiers changed from: private */
    public short itsThisClassIndex;
    private ObjArray itsVarDescriptors;
    private char[] tmpCharBuffer = new char[64];

    public static class ClassFileFormatException extends RuntimeException {
        private static final long serialVersionUID = 1263998431033790599L;

        ClassFileFormatException(String message) {
            super(message);
        }
    }

    final class StackMapTable {
        static final boolean DEBUGSTACKMAP = false;
        private int[] locals = null;
        private int localsTop = 0;
        private byte[] rawStackMap = null;
        private int rawStackMapTop = 0;
        private int[] stack = null;
        private int stackTop = 0;
        private SuperBlock[] superBlockDeps;
        private SuperBlock[] superBlocks = null;
        private boolean wide = false;
        private SuperBlock[] workList = null;
        private int workListTop = 0;

        StackMapTable() {
        }

        /* access modifiers changed from: 0000 */
        public void generate() {
            this.superBlocks = new SuperBlock[ClassFileWriter.this.itsSuperBlockStartsTop];
            int[] initialLocals = ClassFileWriter.this.createInitialLocals();
            for (int i = 0; i < ClassFileWriter.this.itsSuperBlockStartsTop; i++) {
                int end;
                int start = ClassFileWriter.this.itsSuperBlockStarts[i];
                if (i == ClassFileWriter.this.itsSuperBlockStartsTop - 1) {
                    end = ClassFileWriter.this.itsCodeBufferTop;
                } else {
                    end = ClassFileWriter.this.itsSuperBlockStarts[i + 1];
                }
                this.superBlocks[i] = new SuperBlock(i, start, end, initialLocals);
            }
            this.superBlockDeps = getSuperBlockDependencies();
            verify();
        }

        private SuperBlock getSuperBlockFromOffset(int offset) {
            int i = 0;
            while (i < this.superBlocks.length) {
                SuperBlock sb = this.superBlocks[i];
                if (sb == null) {
                    break;
                } else if (offset >= sb.getStart() && offset < sb.getEnd()) {
                    return sb;
                } else {
                    i++;
                }
            }
            throw new IllegalArgumentException("bad offset: " + offset);
        }

        private boolean isSuperBlockEnd(int opcode) {
            switch (opcode) {
                case 167:
                case 170:
                case 171:
                case 172:
                case 173:
                case 174:
                case 176:
                case 177:
                case 191:
                case ByteCode.GOTO_W /*200*/:
                    return true;
                default:
                    return false;
            }
        }

        private SuperBlock[] getSuperBlockDependencies() {
            int i;
            SuperBlock[] deps = new SuperBlock[this.superBlocks.length];
            for (i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i++) {
                ExceptionTableEntry ete = ClassFileWriter.this.itsExceptionTable[i];
                short startPC = (short) ClassFileWriter.this.getLabelPC(ete.itsStartLabel);
                deps[getSuperBlockFromOffset((short) ClassFileWriter.this.getLabelPC(ete.itsHandlerLabel)).getIndex()] = getSuperBlockFromOffset(startPC);
            }
            int[] targetPCs = ClassFileWriter.this.itsJumpFroms.getKeys();
            for (int targetPC : targetPCs) {
                deps[getSuperBlockFromOffset(targetPC).getIndex()] = getSuperBlockFromOffset(ClassFileWriter.this.itsJumpFroms.getInt(targetPC, -1));
            }
            return deps;
        }

        private SuperBlock getBranchTarget(int bci) {
            int target;
            if ((ClassFileWriter.this.itsCodeBuffer[bci] & ByteCode.IMPDEP2) == ByteCode.GOTO_W) {
                target = bci + getOperand(bci + 1, 4);
            } else {
                target = bci + ((short) getOperand(bci + 1, 2));
            }
            return getSuperBlockFromOffset(target);
        }

        private boolean isBranch(int opcode) {
            switch (opcode) {
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 198:
                case 199:
                case ByteCode.GOTO_W /*200*/:
                    return true;
                default:
                    return false;
            }
        }

        private int getOperand(int offset) {
            return getOperand(offset, 1);
        }

        private int getOperand(int start, int size) {
            int result = 0;
            if (size > 4) {
                throw new IllegalArgumentException("bad operand size");
            }
            for (int i = 0; i < size; i++) {
                result = (result << 8) | (ClassFileWriter.this.itsCodeBuffer[start + i] & ByteCode.IMPDEP2);
            }
            return result;
        }

        private void verify() {
            int[] initialLocals = ClassFileWriter.this.createInitialLocals();
            this.superBlocks[0].merge(initialLocals, initialLocals.length, new int[0], 0, ClassFileWriter.this.itsConstantPool);
            this.workList = new SuperBlock[]{this.superBlocks[0]};
            this.workListTop = 1;
            executeWorkList();
            for (SuperBlock sb : this.superBlocks) {
                if (!sb.isInitialized()) {
                    killSuperBlock(sb);
                }
            }
            executeWorkList();
        }

        private void killSuperBlock(SuperBlock sb) {
            int i;
            int[] locals = new int[0];
            int[] stack = new int[]{TypeInfo.OBJECT("java/lang/Throwable", ClassFileWriter.this.itsConstantPool)};
            for (i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i++) {
                ExceptionTableEntry ete = ClassFileWriter.this.itsExceptionTable[i];
                int eteStart = ClassFileWriter.this.getLabelPC(ete.itsStartLabel);
                int eteEnd = ClassFileWriter.this.getLabelPC(ete.itsEndLabel);
                SuperBlock handlerSB = getSuperBlockFromOffset(ClassFileWriter.this.getLabelPC(ete.itsHandlerLabel));
                if ((sb.getStart() > eteStart && sb.getStart() < eteEnd) || (eteStart > sb.getStart() && eteStart < sb.getEnd() && handlerSB.isInitialized())) {
                    locals = handlerSB.getLocals();
                    break;
                }
            }
            i = 0;
            while (i < ClassFileWriter.this.itsExceptionTableTop) {
                if (ClassFileWriter.this.getLabelPC(ClassFileWriter.this.itsExceptionTable[i].itsStartLabel) == sb.getStart()) {
                    for (int j = i + 1; j < ClassFileWriter.this.itsExceptionTableTop; j++) {
                        ClassFileWriter.this.itsExceptionTable[j - 1] = ClassFileWriter.this.itsExceptionTable[j];
                    }
                    ClassFileWriter.this.itsExceptionTableTop = ClassFileWriter.this.itsExceptionTableTop - 1;
                    i--;
                }
                i++;
            }
            sb.merge(locals, locals.length, stack, stack.length, ClassFileWriter.this.itsConstantPool);
            int end = sb.getEnd() - 1;
            ClassFileWriter.this.itsCodeBuffer[end] = (byte) -65;
            for (int bci = sb.getStart(); bci < end; bci++) {
                ClassFileWriter.this.itsCodeBuffer[bci] = (byte) 0;
            }
        }

        private void executeWorkList() {
            while (this.workListTop > 0) {
                SuperBlock[] superBlockArr = this.workList;
                int i = this.workListTop - 1;
                this.workListTop = i;
                SuperBlock work = superBlockArr[i];
                work.setInQueue(false);
                this.locals = work.getLocals();
                this.stack = work.getStack();
                this.localsTop = this.locals.length;
                this.stackTop = this.stack.length;
                executeBlock(work);
            }
        }

        private void executeBlock(SuperBlock work) {
            int bc = 0;
            short bci = work.getStart();
            while (bci < work.getEnd()) {
                int i;
                bc = ClassFileWriter.this.itsCodeBuffer[bci] & ByteCode.IMPDEP2;
                int next = execute(bci);
                if (isBranch(bc)) {
                    flowInto(getBranchTarget(bci));
                } else if (bc == 170) {
                    int switchStart = (bci + 1) + ((bci ^ -1) & 3);
                    flowInto(getSuperBlockFromOffset(bci + getOperand(switchStart, 4)));
                    int numCases = (getOperand(switchStart + 8, 4) - getOperand(switchStart + 4, 4)) + 1;
                    int caseBase = switchStart + 12;
                    for (i = 0; i < numCases; i++) {
                        flowInto(getSuperBlockFromOffset(bci + getOperand((i * 4) + caseBase, 4)));
                    }
                }
                for (i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i++) {
                    ExceptionTableEntry ete = ClassFileWriter.this.itsExceptionTable[i];
                    short endPC = (short) ClassFileWriter.this.getLabelPC(ete.itsEndLabel);
                    if (bci >= ((short) ClassFileWriter.this.getLabelPC(ete.itsStartLabel)) && bci < endPC) {
                        int exceptionType;
                        SuperBlock sb = getSuperBlockFromOffset((short) ClassFileWriter.this.getLabelPC(ete.itsHandlerLabel));
                        if (ete.itsCatchType == (short) 0) {
                            exceptionType = TypeInfo.OBJECT(ClassFileWriter.this.itsConstantPool.addClass("java/lang/Throwable"));
                        } else {
                            exceptionType = TypeInfo.OBJECT(ete.itsCatchType);
                        }
                        sb.merge(this.locals, this.localsTop, new int[]{exceptionType}, 1, ClassFileWriter.this.itsConstantPool);
                        addToWorkList(sb);
                    }
                }
                bci += next;
            }
            if (!isSuperBlockEnd(bc)) {
                int nextIndex = work.getIndex() + 1;
                if (nextIndex < this.superBlocks.length) {
                    flowInto(this.superBlocks[nextIndex]);
                }
            }
        }

        private void flowInto(SuperBlock sb) {
            if (sb.merge(this.locals, this.localsTop, this.stack, this.stackTop, ClassFileWriter.this.itsConstantPool)) {
                addToWorkList(sb);
            }
        }

        private void addToWorkList(SuperBlock sb) {
            if (!sb.isInQueue()) {
                sb.setInQueue(true);
                sb.setInitialized(true);
                if (this.workListTop == this.workList.length) {
                    SuperBlock[] tmp = new SuperBlock[(this.workListTop * 2)];
                    System.arraycopy(this.workList, 0, tmp, 0, this.workListTop);
                    this.workList = tmp;
                }
                SuperBlock[] superBlockArr = this.workList;
                int i = this.workListTop;
                this.workListTop = i + 1;
                superBlockArr[i] = sb;
            }
        }

        /* JADX WARNING: Missing block: B:5:0x004c, code skipped:
            if (r20 != 0) goto L_0x005a;
     */
        /* JADX WARNING: Missing block: B:6:0x004e, code skipped:
            r20 = org.mozilla.classfile.ClassFileWriter.opcodeLength(r4, r37.wide);
     */
        /* JADX WARNING: Missing block: B:8:0x0060, code skipped:
            if (r37.wide == false) goto L_0x0070;
     */
        /* JADX WARNING: Missing block: B:10:0x0066, code skipped:
            if (r4 == 196) goto L_0x0070;
     */
        /* JADX WARNING: Missing block: B:11:0x0068, code skipped:
            r37.wide = false;
     */
        /* JADX WARNING: Missing block: B:12:0x0070, code skipped:
            return r20;
     */
        /* JADX WARNING: Missing block: B:14:0x0074, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:15:0x0077, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:19:0x008c, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:20:0x008f, code skipped:
            push(1);
     */
        /* JADX WARNING: Missing block: B:22:0x009c, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:23:0x009f, code skipped:
            push(4);
     */
        /* JADX WARNING: Missing block: B:25:0x00ac, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:26:0x00af, code skipped:
            push(2);
     */
        /* JADX WARNING: Missing block: B:28:0x00bc, code skipped:
            pop();
     */
        /* JADX WARNING: Missing block: B:29:0x00bf, code skipped:
            push(3);
     */
        /* JADX WARNING: Missing block: B:104:0x0426, code skipped:
            push(org.mozilla.classfile.TypeInfo.fromType(org.mozilla.classfile.ClassFileWriter.access$1200(((org.mozilla.classfile.FieldOrMethodRef) org.mozilla.classfile.ClassFileWriter.access$800(r37.this$0).getConstantData(getOperand(r38 + 1, 2))).getType()), org.mozilla.classfile.ClassFileWriter.access$800(r37.this$0)));
     */
        private int execute(int r38) {
            /*
            r37 = this;
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsCodeBuffer;
            r34 = r34[r38];
            r0 = r34;
            r4 = r0 & 255;
            r20 = 0;
            switch(r4) {
                case 0: goto L_0x004c;
                case 1: goto L_0x007f;
                case 2: goto L_0x008f;
                case 3: goto L_0x008f;
                case 4: goto L_0x008f;
                case 5: goto L_0x008f;
                case 6: goto L_0x008f;
                case 7: goto L_0x008f;
                case 8: goto L_0x008f;
                case 9: goto L_0x009f;
                case 10: goto L_0x009f;
                case 11: goto L_0x00af;
                case 12: goto L_0x00af;
                case 13: goto L_0x00af;
                case 14: goto L_0x00bf;
                case 15: goto L_0x00bf;
                case 16: goto L_0x008f;
                case 17: goto L_0x008f;
                case 18: goto L_0x0224;
                case 19: goto L_0x0224;
                case 20: goto L_0x0224;
                case 21: goto L_0x008f;
                case 22: goto L_0x009f;
                case 23: goto L_0x00af;
                case 24: goto L_0x00bf;
                case 25: goto L_0x019d;
                case 26: goto L_0x008f;
                case 27: goto L_0x008f;
                case 28: goto L_0x008f;
                case 29: goto L_0x008f;
                case 30: goto L_0x009f;
                case 31: goto L_0x009f;
                case 32: goto L_0x009f;
                case 33: goto L_0x009f;
                case 34: goto L_0x00af;
                case 35: goto L_0x00af;
                case 36: goto L_0x00af;
                case 37: goto L_0x00af;
                case 38: goto L_0x00bf;
                case 39: goto L_0x00bf;
                case 40: goto L_0x00bf;
                case 41: goto L_0x00bf;
                case 42: goto L_0x01bf;
                case 43: goto L_0x01bf;
                case 44: goto L_0x01bf;
                case 45: goto L_0x01bf;
                case 46: goto L_0x0089;
                case 47: goto L_0x0099;
                case 48: goto L_0x00a9;
                case 49: goto L_0x00b9;
                case 50: goto L_0x053e;
                case 51: goto L_0x0089;
                case 52: goto L_0x0089;
                case 53: goto L_0x0089;
                case 54: goto L_0x00c9;
                case 55: goto L_0x00fe;
                case 56: goto L_0x0133;
                case 57: goto L_0x0168;
                case 58: goto L_0x01ca;
                case 59: goto L_0x00ef;
                case 60: goto L_0x00ef;
                case 61: goto L_0x00ef;
                case 62: goto L_0x00ef;
                case 63: goto L_0x0124;
                case 64: goto L_0x0124;
                case 65: goto L_0x0124;
                case 66: goto L_0x0124;
                case 67: goto L_0x0159;
                case 68: goto L_0x0159;
                case 69: goto L_0x0159;
                case 70: goto L_0x0159;
                case 71: goto L_0x018e;
                case 72: goto L_0x018e;
                case 73: goto L_0x018e;
                case 74: goto L_0x018e;
                case 75: goto L_0x01ec;
                case 76: goto L_0x01ec;
                case 77: goto L_0x01ec;
                case 78: goto L_0x01ec;
                case 79: goto L_0x0071;
                case 80: goto L_0x0071;
                case 81: goto L_0x0071;
                case 82: goto L_0x0071;
                case 83: goto L_0x0071;
                case 84: goto L_0x0071;
                case 85: goto L_0x0071;
                case 86: goto L_0x0071;
                case 87: goto L_0x0077;
                case 88: goto L_0x007b;
                case 89: goto L_0x0467;
                case 90: goto L_0x047b;
                case 91: goto L_0x049a;
                case 92: goto L_0x04b9;
                case 93: goto L_0x04cd;
                case 94: goto L_0x04ec;
                case 95: goto L_0x020c;
                case 96: goto L_0x0089;
                case 97: goto L_0x0099;
                case 98: goto L_0x00a9;
                case 99: goto L_0x00b9;
                case 100: goto L_0x0089;
                case 101: goto L_0x0099;
                case 102: goto L_0x00a9;
                case 103: goto L_0x00b9;
                case 104: goto L_0x0089;
                case 105: goto L_0x0099;
                case 106: goto L_0x00a9;
                case 107: goto L_0x00b9;
                case 108: goto L_0x0089;
                case 109: goto L_0x0099;
                case 110: goto L_0x00a9;
                case 111: goto L_0x00b9;
                case 112: goto L_0x0089;
                case 113: goto L_0x0099;
                case 114: goto L_0x00a9;
                case 115: goto L_0x00b9;
                case 116: goto L_0x008c;
                case 117: goto L_0x009c;
                case 118: goto L_0x00ac;
                case 119: goto L_0x00bc;
                case 120: goto L_0x0089;
                case 121: goto L_0x0099;
                case 122: goto L_0x0089;
                case 123: goto L_0x0099;
                case 124: goto L_0x0089;
                case 125: goto L_0x0099;
                case 126: goto L_0x0089;
                case 127: goto L_0x0099;
                case 128: goto L_0x0089;
                case 129: goto L_0x0099;
                case 130: goto L_0x0089;
                case 131: goto L_0x0099;
                case 132: goto L_0x004c;
                case 133: goto L_0x009c;
                case 134: goto L_0x00ac;
                case 135: goto L_0x00bc;
                case 136: goto L_0x008c;
                case 137: goto L_0x00ac;
                case 138: goto L_0x00bc;
                case 139: goto L_0x008c;
                case 140: goto L_0x009c;
                case 141: goto L_0x00bc;
                case 142: goto L_0x008c;
                case 143: goto L_0x009c;
                case 144: goto L_0x00ac;
                case 145: goto L_0x008c;
                case 146: goto L_0x008c;
                case 147: goto L_0x008c;
                case 148: goto L_0x0089;
                case 149: goto L_0x0089;
                case 150: goto L_0x0089;
                case 151: goto L_0x0089;
                case 152: goto L_0x0089;
                case 153: goto L_0x0077;
                case 154: goto L_0x0077;
                case 155: goto L_0x0077;
                case 156: goto L_0x0077;
                case 157: goto L_0x0077;
                case 158: goto L_0x0077;
                case 159: goto L_0x0074;
                case 160: goto L_0x0074;
                case 161: goto L_0x0074;
                case 162: goto L_0x0074;
                case 163: goto L_0x0074;
                case 164: goto L_0x0074;
                case 165: goto L_0x0074;
                case 166: goto L_0x0074;
                case 167: goto L_0x004c;
                case 168: goto L_0x0015;
                case 169: goto L_0x0015;
                case 170: goto L_0x050b;
                case 171: goto L_0x0015;
                case 172: goto L_0x01f7;
                case 173: goto L_0x01f7;
                case 174: goto L_0x01f7;
                case 175: goto L_0x01f7;
                case 176: goto L_0x01f7;
                case 177: goto L_0x01f7;
                case 178: goto L_0x0426;
                case 179: goto L_0x0077;
                case 180: goto L_0x0423;
                case 181: goto L_0x0074;
                case 182: goto L_0x0360;
                case 183: goto L_0x0360;
                case 184: goto L_0x0360;
                case 185: goto L_0x0360;
                case 186: goto L_0x0015;
                case 187: goto L_0x02b6;
                case 188: goto L_0x02c3;
                case 189: goto L_0x030b;
                case 190: goto L_0x008c;
                case 191: goto L_0x01fc;
                case 192: goto L_0x0030;
                case 193: goto L_0x008c;
                case 194: goto L_0x0077;
                case 195: goto L_0x0077;
                case 196: goto L_0x059d;
                case 197: goto L_0x0015;
                case 198: goto L_0x0077;
                case 199: goto L_0x0077;
                case 200: goto L_0x004c;
                default: goto L_0x0015;
            };
        L_0x0015:
            r34 = new java.lang.IllegalArgumentException;
            r35 = new java.lang.StringBuilder;
            r35.<init>();
            r36 = "bad opcode: ";
            r35 = r35.append(r36);
            r0 = r35;
            r35 = r0.append(r4);
            r35 = r35.toString();
            r34.<init>(r35);
            throw r34;
        L_0x0030:
            r37.pop();
            r34 = r38 + 1;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r34 = r0.getOperand(r1, r2);
            r34 = org.mozilla.classfile.TypeInfo.OBJECT(r34);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
        L_0x004c:
            if (r20 != 0) goto L_0x005a;
        L_0x004e:
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            r0 = r34;
            r20 = org.mozilla.classfile.ClassFileWriter.opcodeLength(r4, r0);
        L_0x005a:
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x0070;
        L_0x0062:
            r34 = 196; // 0xc4 float:2.75E-43 double:9.7E-322;
            r0 = r34;
            if (r4 == r0) goto L_0x0070;
        L_0x0068:
            r34 = 0;
            r0 = r34;
            r1 = r37;
            r1.wide = r0;
        L_0x0070:
            return r20;
        L_0x0071:
            r37.pop();
        L_0x0074:
            r37.pop();
        L_0x0077:
            r37.pop();
            goto L_0x004c;
        L_0x007b:
            r37.pop2();
            goto L_0x004c;
        L_0x007f:
            r34 = 5;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0089:
            r37.pop();
        L_0x008c:
            r37.pop();
        L_0x008f:
            r34 = 1;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0099:
            r37.pop();
        L_0x009c:
            r37.pop();
        L_0x009f:
            r34 = 4;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x00a9:
            r37.pop();
        L_0x00ac:
            r37.pop();
        L_0x00af:
            r34 = 2;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x00b9:
            r37.pop();
        L_0x00bc:
            r37.pop();
        L_0x00bf:
            r34 = 3;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x00c9:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x00ec;
        L_0x00d3:
            r34 = 2;
        L_0x00d5:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r35 = 1;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x00ec:
            r34 = 1;
            goto L_0x00d5;
        L_0x00ef:
            r34 = r4 + -59;
            r35 = 1;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x00fe:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x0121;
        L_0x0108:
            r34 = 2;
        L_0x010a:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r35 = 4;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x0121:
            r34 = 1;
            goto L_0x010a;
        L_0x0124:
            r34 = r4 + -63;
            r35 = 4;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x0133:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x0156;
        L_0x013d:
            r34 = 2;
        L_0x013f:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x0156:
            r34 = 1;
            goto L_0x013f;
        L_0x0159:
            r34 = r4 + -67;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x0168:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x018b;
        L_0x0172:
            r34 = 2;
        L_0x0174:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r35 = 3;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x018b:
            r34 = 1;
            goto L_0x0174;
        L_0x018e:
            r34 = r4 + -71;
            r35 = 3;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r0.executeStore(r1, r2);
            goto L_0x004c;
        L_0x019d:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x01bc;
        L_0x01a7:
            r34 = 2;
        L_0x01a9:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r0 = r37;
            r1 = r34;
            r0.executeALoad(r1);
            goto L_0x004c;
        L_0x01bc:
            r34 = 1;
            goto L_0x01a9;
        L_0x01bf:
            r34 = r4 + -42;
            r0 = r37;
            r1 = r34;
            r0.executeALoad(r1);
            goto L_0x004c;
        L_0x01ca:
            r35 = r38 + 1;
            r0 = r37;
            r0 = r0.wide;
            r34 = r0;
            if (r34 == 0) goto L_0x01e9;
        L_0x01d4:
            r34 = 2;
        L_0x01d6:
            r0 = r37;
            r1 = r35;
            r2 = r34;
            r34 = r0.getOperand(r1, r2);
            r0 = r37;
            r1 = r34;
            r0.executeAStore(r1);
            goto L_0x004c;
        L_0x01e9:
            r34 = 1;
            goto L_0x01d6;
        L_0x01ec:
            r34 = r4 + -75;
            r0 = r37;
            r1 = r34;
            r0.executeAStore(r1);
            goto L_0x004c;
        L_0x01f7:
            r37.clearStack();
            goto L_0x004c;
        L_0x01fc:
            r31 = r37.pop();
            r37.clearStack();
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            goto L_0x004c;
        L_0x020c:
            r31 = r37.pop();
            r32 = r37.pop();
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            r0 = r37;
            r1 = r32;
            r0.push(r1);
            goto L_0x004c;
        L_0x0224:
            r34 = 18;
            r0 = r34;
            if (r4 != r0) goto L_0x0262;
        L_0x022a:
            r34 = r38 + 1;
            r0 = r37;
            r1 = r34;
            r14 = r0.getOperand(r1);
        L_0x0234:
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r7 = r0.getConstantType(r14);
            switch(r7) {
                case 3: goto L_0x0292;
                case 4: goto L_0x027c;
                case 5: goto L_0x0287;
                case 6: goto L_0x0271;
                case 7: goto L_0x0247;
                case 8: goto L_0x029d;
                default: goto L_0x0247;
            };
        L_0x0247:
            r34 = new java.lang.IllegalArgumentException;
            r35 = new java.lang.StringBuilder;
            r35.<init>();
            r36 = "bad const type ";
            r35 = r35.append(r36);
            r0 = r35;
            r35 = r0.append(r7);
            r35 = r35.toString();
            r34.<init>(r35);
            throw r34;
        L_0x0262:
            r34 = r38 + 1;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r14 = r0.getOperand(r1, r2);
            goto L_0x0234;
        L_0x0271:
            r34 = 3;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x027c:
            r34 = 2;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0287:
            r34 = 4;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0292:
            r34 = 1;
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x029d:
            r34 = "java/lang/String";
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r35 = r0;
            r35 = r35.itsConstantPool;
            r34 = org.mozilla.classfile.TypeInfo.OBJECT(r34, r35);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x02b6:
            r34 = org.mozilla.classfile.TypeInfo.UNINITIALIZED_VARIABLE(r38);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x02c3:
            r37.pop();
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsCodeBuffer;
            r35 = r38 + 1;
            r34 = r34[r35];
            r6 = org.mozilla.classfile.ClassFileWriter.arrayTypeToName(r34);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r35 = new java.lang.StringBuilder;
            r35.<init>();
            r36 = "[";
            r35 = r35.append(r36);
            r0 = r35;
            r35 = r0.append(r6);
            r35 = r35.toString();
            r14 = r34.addClass(r35);
            r0 = (short) r14;
            r34 = r0;
            r34 = org.mozilla.classfile.TypeInfo.OBJECT(r34);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x030b:
            r34 = r38 + 1;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r14 = r0.getOperand(r1, r2);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r5 = r0.getConstantData(r14);
            r5 = (java.lang.String) r5;
            r37.pop();
            r34 = new java.lang.StringBuilder;
            r34.<init>();
            r35 = "[L";
            r34 = r34.append(r35);
            r0 = r34;
            r34 = r0.append(r5);
            r35 = 59;
            r34 = r34.append(r35);
            r34 = r34.toString();
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r35 = r0;
            r35 = r35.itsConstantPool;
            r34 = org.mozilla.classfile.TypeInfo.OBJECT(r34, r35);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0360:
            r34 = r38 + 1;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r14 = r0.getOperand(r1, r2);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r22 = r0.getConstantData(r14);
            r22 = (org.mozilla.classfile.FieldOrMethodRef) r22;
            r24 = r22.getType();
            r23 = r22.getName();
            r34 = org.mozilla.classfile.ClassFileWriter.sizeOfParameters(r24);
            r26 = r34 >>> 16;
            r13 = 0;
        L_0x038f:
            r0 = r26;
            if (r13 >= r0) goto L_0x0399;
        L_0x0393:
            r37.pop();
            r13 = r13 + 1;
            goto L_0x038f;
        L_0x0399:
            r34 = 184; // 0xb8 float:2.58E-43 double:9.1E-322;
            r0 = r34;
            if (r4 == r0) goto L_0x03dc;
        L_0x039f:
            r15 = r37.pop();
            r30 = org.mozilla.classfile.TypeInfo.getTag(r15);
            r34 = 0;
            r34 = org.mozilla.classfile.TypeInfo.UNINITIALIZED_VARIABLE(r34);
            r0 = r30;
            r1 = r34;
            if (r0 == r1) goto L_0x03bb;
        L_0x03b3:
            r34 = 6;
            r0 = r30;
            r1 = r34;
            if (r0 != r1) goto L_0x03dc;
        L_0x03bb:
            r34 = "<init>";
            r0 = r34;
            r1 = r23;
            r34 = r0.equals(r1);
            if (r34 == 0) goto L_0x041b;
        L_0x03c7:
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsThisClassIndex;
            r25 = org.mozilla.classfile.TypeInfo.OBJECT(r34);
            r0 = r37;
            r1 = r25;
            r0.initializeTypeInfo(r15, r1);
        L_0x03dc:
            r34 = 41;
            r0 = r24;
            r1 = r34;
            r27 = r0.indexOf(r1);
            r34 = r27 + 1;
            r0 = r24;
            r1 = r34;
            r28 = r0.substring(r1);
            r28 = org.mozilla.classfile.ClassFileWriter.descriptorToInternalName(r28);
            r34 = "V";
            r0 = r28;
            r1 = r34;
            r34 = r0.equals(r1);
            if (r34 != 0) goto L_0x004c;
        L_0x0400:
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r28;
            r1 = r34;
            r34 = org.mozilla.classfile.TypeInfo.fromType(r0, r1);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x041b:
            r34 = new java.lang.IllegalStateException;
            r35 = "bad instance";
            r34.<init>(r35);
            throw r34;
        L_0x0423:
            r37.pop();
        L_0x0426:
            r34 = r38 + 1;
            r35 = 2;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r14 = r0.getOperand(r1, r2);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r10 = r0.getConstantData(r14);
            r10 = (org.mozilla.classfile.FieldOrMethodRef) r10;
            r34 = r10.getType();
            r11 = org.mozilla.classfile.ClassFileWriter.descriptorToInternalName(r34);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r34 = org.mozilla.classfile.TypeInfo.fromType(r11, r0);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x0467:
            r31 = r37.pop();
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            goto L_0x004c;
        L_0x047b:
            r31 = r37.pop();
            r32 = r37.pop();
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            r0 = r37;
            r1 = r32;
            r0.push(r1);
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            goto L_0x004c;
        L_0x049a:
            r31 = r37.pop();
            r16 = r37.pop2();
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            goto L_0x004c;
        L_0x04b9:
            r16 = r37.pop2();
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            goto L_0x004c;
        L_0x04cd:
            r16 = r37.pop2();
            r31 = r37.pop();
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            r0 = r37;
            r1 = r31;
            r0.push(r1);
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            goto L_0x004c;
        L_0x04ec:
            r16 = r37.pop2();
            r18 = r37.pop2();
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            r0 = r37;
            r1 = r18;
            r0.push2(r1);
            r0 = r37;
            r1 = r16;
            r0.push2(r1);
            goto L_0x004c;
        L_0x050b:
            r34 = r38 + 1;
            r35 = r38 ^ -1;
            r35 = r35 & 3;
            r29 = r34 + r35;
            r34 = r29 + 4;
            r35 = 4;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r21 = r0.getOperand(r1, r2);
            r34 = r29 + 8;
            r35 = 4;
            r0 = r37;
            r1 = r34;
            r2 = r35;
            r12 = r0.getOperand(r1, r2);
            r34 = r12 - r21;
            r34 = r34 + 4;
            r34 = r34 * 4;
            r34 = r34 + r29;
            r20 = r34 - r38;
            r37.pop();
            goto L_0x004c;
        L_0x053e:
            r37.pop();
            r34 = r37.pop();
            r33 = r34 >>> 8;
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r1 = r33;
            r5 = r0.getConstantData(r1);
            r5 = (java.lang.String) r5;
            r3 = r5;
            r34 = 0;
            r0 = r34;
            r34 = r3.charAt(r0);
            r35 = 91;
            r0 = r34;
            r1 = r35;
            if (r0 == r1) goto L_0x0574;
        L_0x056c:
            r34 = new java.lang.IllegalStateException;
            r35 = "bad array type";
            r34.<init>(r35);
            throw r34;
        L_0x0574:
            r34 = 1;
            r0 = r34;
            r8 = r3.substring(r0);
            r9 = org.mozilla.classfile.ClassFileWriter.descriptorToInternalName(r8);
            r0 = r37;
            r0 = org.mozilla.classfile.ClassFileWriter.this;
            r34 = r0;
            r34 = r34.itsConstantPool;
            r0 = r34;
            r33 = r0.addClass(r9);
            r34 = org.mozilla.classfile.TypeInfo.OBJECT(r33);
            r0 = r37;
            r1 = r34;
            r0.push(r1);
            goto L_0x004c;
        L_0x059d:
            r34 = 1;
            r0 = r34;
            r1 = r37;
            r1.wide = r0;
            goto L_0x004c;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mozilla.classfile.ClassFileWriter$StackMapTable.execute(int):int");
        }

        private void executeALoad(int localIndex) {
            int type = getLocal(localIndex);
            int tag = TypeInfo.getTag(type);
            if (tag == 7 || tag == 6 || tag == 8 || tag == 5) {
                push(type);
                return;
            }
            throw new IllegalStateException("bad local variable type: " + type + " at index: " + localIndex);
        }

        private void executeAStore(int localIndex) {
            setLocal(localIndex, pop());
        }

        private void executeStore(int localIndex, int typeInfo) {
            pop();
            setLocal(localIndex, typeInfo);
        }

        private void initializeTypeInfo(int prevType, int newType) {
            initializeTypeInfo(prevType, newType, this.locals, this.localsTop);
            initializeTypeInfo(prevType, newType, this.stack, this.stackTop);
        }

        private void initializeTypeInfo(int prevType, int newType, int[] data, int dataTop) {
            for (int i = 0; i < dataTop; i++) {
                if (data[i] == prevType) {
                    data[i] = newType;
                }
            }
        }

        private int getLocal(int localIndex) {
            if (localIndex < this.localsTop) {
                return this.locals[localIndex];
            }
            return 0;
        }

        private void setLocal(int localIndex, int typeInfo) {
            if (localIndex >= this.localsTop) {
                int[] tmp = new int[(localIndex + 1)];
                System.arraycopy(this.locals, 0, tmp, 0, this.localsTop);
                this.locals = tmp;
                this.localsTop = localIndex + 1;
            }
            this.locals[localIndex] = typeInfo;
        }

        private void push(int typeInfo) {
            if (this.stackTop == this.stack.length) {
                int[] tmp = new int[Math.max(this.stackTop * 2, 4)];
                System.arraycopy(this.stack, 0, tmp, 0, this.stackTop);
                this.stack = tmp;
            }
            int[] iArr = this.stack;
            int i = this.stackTop;
            this.stackTop = i + 1;
            iArr[i] = typeInfo;
        }

        private int pop() {
            int[] iArr = this.stack;
            int i = this.stackTop - 1;
            this.stackTop = i;
            return iArr[i];
        }

        private void push2(long typeInfo) {
            push((int) (typeInfo & 16777215));
            typeInfo >>>= 32;
            if (typeInfo != 0) {
                push((int) (typeInfo & 16777215));
            }
        }

        private long pop2() {
            long type = (long) pop();
            return TypeInfo.isTwoWords((int) type) ? type : (type << 32) | ((long) (pop() & 16777215));
        }

        private void clearStack() {
            this.stackTop = 0;
        }

        /* access modifiers changed from: 0000 */
        public int computeWriteSize() {
            this.rawStackMap = new byte[getWorstCaseWriteSize()];
            computeRawStackMap();
            return this.rawStackMapTop + 2;
        }

        /* access modifiers changed from: 0000 */
        public int write(byte[] data, int offset) {
            offset = ClassFileWriter.putInt16(this.superBlocks.length - 1, data, ClassFileWriter.putInt32(this.rawStackMapTop + 2, data, offset));
            System.arraycopy(this.rawStackMap, 0, data, offset, this.rawStackMapTop);
            return this.rawStackMapTop + offset;
        }

        private void computeRawStackMap() {
            int[] prevLocals = this.superBlocks[0].getTrimmedLocals();
            int prevOffset = -1;
            for (int i = 1; i < this.superBlocks.length; i++) {
                SuperBlock current = this.superBlocks[i];
                int[] currentLocals = current.getTrimmedLocals();
                int[] currentStack = current.getStack();
                int offsetDelta = (current.getStart() - prevOffset) - 1;
                if (currentStack.length == 0) {
                    int last = prevLocals.length > currentLocals.length ? currentLocals.length : prevLocals.length;
                    int delta = Math.abs(prevLocals.length - currentLocals.length);
                    int j = 0;
                    while (j < last && prevLocals[j] == currentLocals[j]) {
                        j++;
                    }
                    if (j == currentLocals.length && delta == 0) {
                        writeSameFrame(currentLocals, offsetDelta);
                    } else if (j == currentLocals.length && delta <= 3) {
                        writeChopFrame(delta, offsetDelta);
                    } else if (j != prevLocals.length || delta > 3) {
                        writeFullFrame(currentLocals, currentStack, offsetDelta);
                    } else {
                        writeAppendFrame(currentLocals, delta, offsetDelta);
                    }
                } else if (currentStack.length != 1) {
                    writeFullFrame(currentLocals, currentStack, offsetDelta);
                } else if (Arrays.equals(prevLocals, currentLocals)) {
                    writeSameLocalsOneStackItemFrame(currentLocals, currentStack, offsetDelta);
                } else {
                    writeFullFrame(currentLocals, currentStack, offsetDelta);
                }
                SuperBlock prev = current;
                prevLocals = currentLocals;
                prevOffset = current.getStart();
            }
        }

        private int getWorstCaseWriteSize() {
            return (this.superBlocks.length - 1) * (((ClassFileWriter.this.itsMaxLocals * 3) + 7) + (ClassFileWriter.this.itsMaxStack * 3));
        }

        private void writeSameFrame(int[] locals, int offsetDelta) {
            byte[] bArr;
            int i;
            if (offsetDelta <= 63) {
                bArr = this.rawStackMap;
                i = this.rawStackMapTop;
                this.rawStackMapTop = i + 1;
                bArr[i] = (byte) offsetDelta;
                return;
            }
            bArr = this.rawStackMap;
            i = this.rawStackMapTop;
            this.rawStackMapTop = i + 1;
            bArr[i] = (byte) -5;
            this.rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, this.rawStackMap, this.rawStackMapTop);
        }

        private void writeSameLocalsOneStackItemFrame(int[] locals, int[] stack, int offsetDelta) {
            byte[] bArr;
            int i;
            if (offsetDelta <= 63) {
                bArr = this.rawStackMap;
                i = this.rawStackMapTop;
                this.rawStackMapTop = i + 1;
                bArr[i] = (byte) (offsetDelta + 64);
            } else {
                bArr = this.rawStackMap;
                i = this.rawStackMapTop;
                this.rawStackMapTop = i + 1;
                bArr[i] = (byte) -9;
                this.rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, this.rawStackMap, this.rawStackMapTop);
            }
            writeType(stack[0]);
        }

        private void writeFullFrame(int[] locals, int[] stack, int offsetDelta) {
            byte[] bArr = this.rawStackMap;
            int i = this.rawStackMapTop;
            this.rawStackMapTop = i + 1;
            bArr[i] = (byte) -1;
            this.rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = ClassFileWriter.putInt16(locals.length, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = writeTypes(locals);
            this.rawStackMapTop = ClassFileWriter.putInt16(stack.length, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = writeTypes(stack);
        }

        private void writeAppendFrame(int[] locals, int localsDelta, int offsetDelta) {
            int start = locals.length - localsDelta;
            byte[] bArr = this.rawStackMap;
            int i = this.rawStackMapTop;
            this.rawStackMapTop = i + 1;
            bArr[i] = (byte) (localsDelta + 251);
            this.rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = writeTypes(locals, start);
        }

        private void writeChopFrame(int localsDelta, int offsetDelta) {
            byte[] bArr = this.rawStackMap;
            int i = this.rawStackMapTop;
            this.rawStackMapTop = i + 1;
            bArr[i] = (byte) (251 - localsDelta);
            this.rawStackMapTop = ClassFileWriter.putInt16(offsetDelta, this.rawStackMap, this.rawStackMapTop);
        }

        private int writeTypes(int[] types) {
            return writeTypes(types, 0);
        }

        private int writeTypes(int[] types, int start) {
            int startOffset = this.rawStackMapTop;
            for (int i = start; i < types.length; i++) {
                this.rawStackMapTop = writeType(types[i]);
            }
            return this.rawStackMapTop;
        }

        private int writeType(int type) {
            int tag = type & ByteCode.IMPDEP2;
            byte[] bArr = this.rawStackMap;
            int i = this.rawStackMapTop;
            this.rawStackMapTop = i + 1;
            bArr[i] = (byte) tag;
            if (tag == 7 || tag == 8) {
                this.rawStackMapTop = ClassFileWriter.putInt16(type >>> 8, this.rawStackMap, this.rawStackMapTop);
            }
            return this.rawStackMapTop;
        }
    }

    public ClassFileWriter(String className, String superClassName, String sourceFileName) {
        this.generatedClassName = className;
        this.itsConstantPool = new ConstantPool(this);
        this.itsThisClassIndex = this.itsConstantPool.addClass(className);
        this.itsSuperClassIndex = this.itsConstantPool.addClass(superClassName);
        if (sourceFileName != null) {
            this.itsSourceFileNameIndex = this.itsConstantPool.addUtf8(sourceFileName);
        }
        this.itsFlags = (short) 33;
    }

    public final String getClassName() {
        return this.generatedClassName;
    }

    public void addInterface(String interfaceName) {
        this.itsInterfaces.add(Short.valueOf(this.itsConstantPool.addClass(interfaceName)));
    }

    public void setFlags(short flags) {
        this.itsFlags = flags;
    }

    static String getSlashedForm(String name) {
        return name.replace('.', '/');
    }

    public static String classNameToSignature(String name) {
        int nameLength = name.length();
        int colonPos = nameLength + 1;
        char[] buf = new char[(colonPos + 1)];
        buf[0] = 'L';
        buf[colonPos] = ';';
        name.getChars(0, nameLength, buf, 1);
        for (int i = 1; i != colonPos; i++) {
            if (buf[i] == '.') {
                buf[i] = '/';
            }
        }
        return new String(buf, 0, colonPos + 1);
    }

    public void addField(String fieldName, String type, short flags) {
        this.itsFields.add(new ClassFileField(this.itsConstantPool.addUtf8(fieldName), this.itsConstantPool.addUtf8(type), flags));
    }

    public void addField(String fieldName, String type, short flags, int value) {
        ClassFileField field = new ClassFileField(this.itsConstantPool.addUtf8(fieldName), this.itsConstantPool.addUtf8(type), flags);
        field.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short) 0, (short) 0, this.itsConstantPool.addConstant(value));
        this.itsFields.add(field);
    }

    public void addField(String fieldName, String type, short flags, long value) {
        ClassFileField field = new ClassFileField(this.itsConstantPool.addUtf8(fieldName), this.itsConstantPool.addUtf8(type), flags);
        field.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short) 0, (short) 2, this.itsConstantPool.addConstant(value));
        this.itsFields.add(field);
    }

    public void addField(String fieldName, String type, short flags, double value) {
        ClassFileField field = new ClassFileField(this.itsConstantPool.addUtf8(fieldName), this.itsConstantPool.addUtf8(type), flags);
        field.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short) 0, (short) 2, this.itsConstantPool.addConstant(value));
        this.itsFields.add(field);
    }

    public void addVariableDescriptor(String name, String type, int startPC, int register) {
        int nameIndex = this.itsConstantPool.addUtf8(name);
        int descriptorIndex = this.itsConstantPool.addUtf8(type);
        int[] chunk = new int[]{nameIndex, descriptorIndex, startPC, register};
        if (this.itsVarDescriptors == null) {
            this.itsVarDescriptors = new ObjArray();
        }
        this.itsVarDescriptors.add(chunk);
    }

    public void startMethod(String methodName, String type, short flags) {
        this.itsCurrentMethod = new ClassFileMethod(methodName, this.itsConstantPool.addUtf8(methodName), type, this.itsConstantPool.addUtf8(type), flags);
        this.itsJumpFroms = new UintMap();
        this.itsMethods.add(this.itsCurrentMethod);
        addSuperBlockStart(0);
    }

    public void stopMethod(short maxLocals) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalStateException("No method to stop");
        }
        fixLabelGotos();
        this.itsMaxLocals = maxLocals;
        StackMapTable stackMap = null;
        if (GenerateStackMap) {
            finalizeSuperBlockStarts();
            StackMapTable stackMapTable = new StackMapTable();
            stackMapTable.generate();
        }
        int lineNumberTableLength = 0;
        if (this.itsLineNumberTable != null) {
            lineNumberTableLength = (this.itsLineNumberTableTop * 4) + 8;
        }
        int variableTableLength = 0;
        if (this.itsVarDescriptors != null) {
            variableTableLength = (this.itsVarDescriptors.size() * 10) + 8;
        }
        int stackMapTableLength = 0;
        if (stackMap != null) {
            int stackMapWriteSize = stackMap.computeWriteSize();
            if (stackMapWriteSize > 0) {
                stackMapTableLength = stackMapWriteSize + 6;
            }
        }
        int attrLength = ((((((this.itsCodeBufferTop + 14) + 2) + (this.itsExceptionTableTop * 8)) + 2) + lineNumberTableLength) + variableTableLength) + stackMapTableLength;
        if (attrLength > 65536) {
            throw new ClassFileFormatException("generated bytecode for method exceeds 64K limit.");
        }
        int i;
        byte[] codeAttribute = new byte[attrLength];
        int index = putInt32(this.itsCodeBufferTop, codeAttribute, putInt16(this.itsMaxLocals, codeAttribute, putInt16(this.itsMaxStack, codeAttribute, putInt32(attrLength - 6, codeAttribute, putInt16(this.itsConstantPool.addUtf8("Code"), codeAttribute, 0)))));
        System.arraycopy(this.itsCodeBuffer, 0, codeAttribute, index, this.itsCodeBufferTop);
        index += this.itsCodeBufferTop;
        if (this.itsExceptionTableTop > 0) {
            index = putInt16(this.itsExceptionTableTop, codeAttribute, index);
            i = 0;
            while (i < this.itsExceptionTableTop) {
                ExceptionTableEntry ete = this.itsExceptionTable[i];
                short startPC = (short) getLabelPC(ete.itsStartLabel);
                short endPC = (short) getLabelPC(ete.itsEndLabel);
                short handlerPC = (short) getLabelPC(ete.itsHandlerLabel);
                short catchType = ete.itsCatchType;
                if (startPC == (short) -1) {
                    throw new IllegalStateException("start label not defined");
                } else if (endPC == (short) -1) {
                    throw new IllegalStateException("end label not defined");
                } else if (handlerPC == (short) -1) {
                    throw new IllegalStateException("handler label not defined");
                } else {
                    index = putInt16(catchType, codeAttribute, putInt16(handlerPC, codeAttribute, putInt16(endPC, codeAttribute, putInt16(startPC, codeAttribute, index))));
                    i++;
                }
            }
        } else {
            index = putInt16(0, codeAttribute, index);
        }
        int attributeCount = 0;
        if (this.itsLineNumberTable != null) {
            attributeCount = 0 + 1;
        }
        if (this.itsVarDescriptors != null) {
            attributeCount++;
        }
        if (stackMapTableLength > 0) {
            attributeCount++;
        }
        index = putInt16(attributeCount, codeAttribute, index);
        if (this.itsLineNumberTable != null) {
            index = putInt16(this.itsLineNumberTableTop, codeAttribute, putInt32((this.itsLineNumberTableTop * 4) + 2, codeAttribute, putInt16(this.itsConstantPool.addUtf8("LineNumberTable"), codeAttribute, index)));
            for (i = 0; i < this.itsLineNumberTableTop; i++) {
                index = putInt32(this.itsLineNumberTable[i], codeAttribute, index);
            }
        }
        if (this.itsVarDescriptors != null) {
            index = putInt16(this.itsConstantPool.addUtf8("LocalVariableTable"), codeAttribute, index);
            int varCount = this.itsVarDescriptors.size();
            index = putInt16(varCount, codeAttribute, putInt32((varCount * 10) + 2, codeAttribute, index));
            for (i = 0; i < varCount; i++) {
                int[] chunk = (int[]) this.itsVarDescriptors.get(i);
                int nameIndex = chunk[0];
                int descriptorIndex = chunk[1];
                int startPC2 = chunk[2];
                int i2 = chunk[3];
                index = putInt16(i2, codeAttribute, putInt16(descriptorIndex, codeAttribute, putInt16(nameIndex, codeAttribute, putInt16(this.itsCodeBufferTop - startPC2, codeAttribute, putInt16(startPC2, codeAttribute, index)))));
            }
        }
        if (stackMapTableLength > 0) {
            int start = index;
            index = stackMap.write(codeAttribute, putInt16(this.itsConstantPool.addUtf8("StackMapTable"), codeAttribute, index));
        }
        this.itsCurrentMethod.setCodeAttribute(codeAttribute);
        this.itsExceptionTable = null;
        this.itsExceptionTableTop = 0;
        this.itsLineNumberTableTop = 0;
        this.itsCodeBufferTop = 0;
        this.itsCurrentMethod = null;
        this.itsMaxStack = (short) 0;
        this.itsStackTop = (short) 0;
        this.itsLabelTableTop = 0;
        this.itsFixupTableTop = 0;
        this.itsVarDescriptors = null;
        this.itsSuperBlockStarts = null;
        this.itsSuperBlockStartsTop = 0;
        this.itsJumpFroms = null;
    }

    public void add(int theOpCode) {
        if (opcodeCount(theOpCode) != 0) {
            throw new IllegalArgumentException("Unexpected operands");
        }
        short newStack = this.itsStackTop + stackChange(theOpCode);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        addToCodeBuffer(theOpCode);
        this.itsStackTop = (short) newStack;
        if (newStack > this.itsMaxStack) {
            this.itsMaxStack = (short) newStack;
        }
        if (theOpCode == 191) {
            addSuperBlockStart(this.itsCodeBufferTop);
        }
    }

    public void add(int theOpCode, int theOperand) {
        short newStack = this.itsStackTop + stackChange(theOpCode);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        switch (theOpCode) {
            case 16:
                if (((byte) theOperand) == theOperand) {
                    addToCodeBuffer(theOpCode);
                    addToCodeBuffer((byte) theOperand);
                    break;
                }
                throw new IllegalArgumentException("out of range byte");
            case 17:
                if (((short) theOperand) == theOperand) {
                    addToCodeBuffer(theOpCode);
                    addToCodeInt16(theOperand);
                    break;
                }
                throw new IllegalArgumentException("out of range short");
            case 18:
            case 19:
            case 20:
                if (theOperand >= 0 && theOperand < 65536) {
                    if (theOperand < 256 && theOpCode != 19 && theOpCode != 20) {
                        addToCodeBuffer(theOpCode);
                        addToCodeBuffer(theOperand);
                        break;
                    }
                    if (theOpCode == 18) {
                        addToCodeBuffer(19);
                    } else {
                        addToCodeBuffer(theOpCode);
                    }
                    addToCodeInt16(theOperand);
                    break;
                }
                throw new IllegalArgumentException("out of range index");
                break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 169:
                if (theOperand >= 0 && theOperand < 65536) {
                    if (theOperand < 256) {
                        addToCodeBuffer(theOpCode);
                        addToCodeBuffer(theOperand);
                        break;
                    }
                    addToCodeBuffer(ByteCode.WIDE);
                    addToCodeBuffer(theOpCode);
                    addToCodeInt16(theOperand);
                    break;
                }
                throw new ClassFileFormatException("out of range variable");
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 168:
            case 198:
            case 199:
                break;
            case 167:
                addSuperBlockStart(this.itsCodeBufferTop + 3);
                break;
            case 180:
            case 181:
                if (theOperand >= 0 && theOperand < 65536) {
                    addToCodeBuffer(theOpCode);
                    addToCodeInt16(theOperand);
                    break;
                }
                throw new IllegalArgumentException("out of range field");
            case 188:
                if (theOperand >= 0 && theOperand < 256) {
                    addToCodeBuffer(theOpCode);
                    addToCodeBuffer(theOperand);
                    break;
                }
                throw new IllegalArgumentException("out of range index");
                break;
            default:
                throw new IllegalArgumentException("Unexpected opcode for 1 operand");
        }
        if ((theOperand & Integer.MIN_VALUE) == Integer.MIN_VALUE || (theOperand >= 0 && theOperand <= 65535)) {
            int branchPC = this.itsCodeBufferTop;
            addToCodeBuffer(theOpCode);
            if ((theOperand & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                addToCodeInt16(theOperand);
                int target = theOperand + branchPC;
                addSuperBlockStart(target);
                this.itsJumpFroms.put(target, branchPC);
            } else {
                int targetPC = getLabelPC(theOperand);
                if (targetPC != -1) {
                    addToCodeInt16(targetPC - branchPC);
                    addSuperBlockStart(targetPC);
                    this.itsJumpFroms.put(targetPC, branchPC);
                } else {
                    addLabelFixup(theOperand, branchPC + 1);
                    addToCodeInt16(0);
                }
            }
            this.itsStackTop = (short) newStack;
            if (newStack > this.itsMaxStack) {
                this.itsMaxStack = (short) newStack;
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Bad label for branch");
    }

    public void addLoadConstant(int k) {
        switch (k) {
            case 0:
                add(3);
                return;
            case 1:
                add(4);
                return;
            case 2:
                add(5);
                return;
            case 3:
                add(6);
                return;
            case 4:
                add(7);
                return;
            case 5:
                add(8);
                return;
            default:
                add(18, this.itsConstantPool.addConstant(k));
                return;
        }
    }

    public void addLoadConstant(long k) {
        add(20, this.itsConstantPool.addConstant(k));
    }

    public void addLoadConstant(float k) {
        add(18, this.itsConstantPool.addConstant(k));
    }

    public void addLoadConstant(double k) {
        add(20, this.itsConstantPool.addConstant(k));
    }

    public void addLoadConstant(String k) {
        add(18, this.itsConstantPool.addConstant(k));
    }

    public void add(int theOpCode, int theOperand1, int theOperand2) {
        short newStack = this.itsStackTop + stackChange(theOpCode);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        if (theOpCode == 132) {
            if (theOperand1 < 0 || theOperand1 >= 65536) {
                throw new ClassFileFormatException("out of range variable");
            } else if (theOperand2 < 0 || theOperand2 >= 65536) {
                throw new ClassFileFormatException("out of range increment");
            } else if (theOperand1 > ByteCode.IMPDEP2 || theOperand2 < -128 || theOperand2 > 127) {
                addToCodeBuffer(ByteCode.WIDE);
                addToCodeBuffer(132);
                addToCodeInt16(theOperand1);
                addToCodeInt16(theOperand2);
            } else {
                addToCodeBuffer(132);
                addToCodeBuffer(theOperand1);
                addToCodeBuffer(theOperand2);
            }
        } else if (theOpCode != 197) {
            throw new IllegalArgumentException("Unexpected opcode for 2 operands");
        } else if (theOperand1 < 0 || theOperand1 >= 65536) {
            throw new IllegalArgumentException("out of range index");
        } else if (theOperand2 < 0 || theOperand2 >= 256) {
            throw new IllegalArgumentException("out of range dimensions");
        } else {
            addToCodeBuffer(197);
            addToCodeInt16(theOperand1);
            addToCodeBuffer(theOperand2);
        }
        this.itsStackTop = (short) newStack;
        if (newStack > this.itsMaxStack) {
            this.itsMaxStack = (short) newStack;
        }
    }

    public void add(int theOpCode, String className) {
        short newStack = this.itsStackTop + stackChange(theOpCode);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        switch (theOpCode) {
            case 187:
            case 189:
            case 192:
            case 193:
                short classIndex = this.itsConstantPool.addClass(className);
                addToCodeBuffer(theOpCode);
                addToCodeInt16(classIndex);
                this.itsStackTop = (short) newStack;
                if (newStack > this.itsMaxStack) {
                    this.itsMaxStack = (short) newStack;
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("bad opcode for class reference");
        }
    }

    public void add(int theOpCode, String className, String fieldName, String fieldType) {
        short newStack;
        int newStack2 = this.itsStackTop + stackChange(theOpCode);
        char fieldTypeChar = fieldType.charAt(0);
        int fieldSize = (fieldTypeChar == 'J' || fieldTypeChar == 'D') ? 2 : 1;
        switch (theOpCode) {
            case 178:
            case 180:
                newStack = newStack2 + fieldSize;
                break;
            case 179:
            case 181:
                newStack = newStack2 - fieldSize;
                break;
            default:
                throw new IllegalArgumentException("bad opcode for field reference");
        }
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        short fieldRefIndex = this.itsConstantPool.addFieldRef(className, fieldName, fieldType);
        addToCodeBuffer(theOpCode);
        addToCodeInt16(fieldRefIndex);
        this.itsStackTop = (short) newStack;
        if (newStack > this.itsMaxStack) {
            this.itsMaxStack = (short) newStack;
        }
    }

    public void addInvoke(int theOpCode, String className, String methodName, String methodType) {
        int parameterInfo = sizeOfParameters(methodType);
        int parameterCount = parameterInfo >>> 16;
        short newStack = (this.itsStackTop + ((short) parameterInfo)) + stackChange(theOpCode);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        switch (theOpCode) {
            case 182:
            case 183:
            case 184:
            case 185:
                addToCodeBuffer(theOpCode);
                if (theOpCode == 185) {
                    addToCodeInt16(this.itsConstantPool.addInterfaceMethodRef(className, methodName, methodType));
                    addToCodeBuffer(parameterCount + 1);
                    addToCodeBuffer(0);
                } else {
                    addToCodeInt16(this.itsConstantPool.addMethodRef(className, methodName, methodType));
                }
                this.itsStackTop = (short) newStack;
                if (newStack > this.itsMaxStack) {
                    this.itsMaxStack = (short) newStack;
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("bad opcode for method reference");
        }
    }

    public void addPush(int k) {
        if (((byte) k) == k) {
            if (k == -1) {
                add(2);
            } else if (k < 0 || k > 5) {
                add(16, (byte) k);
            } else {
                add((byte) (k + 3));
            }
        } else if (((short) k) == k) {
            add(17, (short) k);
        } else {
            addLoadConstant(k);
        }
    }

    public void addPush(boolean k) {
        add(k ? 4 : 3);
    }

    public void addPush(long k) {
        int ik = (int) k;
        if (((long) ik) == k) {
            addPush(ik);
            add(133);
            return;
        }
        addLoadConstant(k);
    }

    public void addPush(double k) {
        if (k == 0.0d) {
            add(14);
            if (1.0d / k < 0.0d) {
                add(119);
            }
        } else if (k == 1.0d || k == -1.0d) {
            add(15);
            if (k < 0.0d) {
                add(119);
            }
        } else {
            addLoadConstant(k);
        }
    }

    public void addPush(String k) {
        int length = k.length();
        int limit = this.itsConstantPool.getUtfEncodingLimit(k, 0, length);
        if (limit == length) {
            addLoadConstant(k);
            return;
        }
        String SB = "java/lang/StringBuilder";
        add(187, "java/lang/StringBuilder");
        add(89);
        addPush(length);
        addInvoke(183, "java/lang/StringBuilder", "<init>", "(I)V");
        int cursor = 0;
        while (true) {
            add(89);
            addLoadConstant(k.substring(cursor, limit));
            addInvoke(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            add(87);
            if (limit == length) {
                addInvoke(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                return;
            } else {
                cursor = limit;
                limit = this.itsConstantPool.getUtfEncodingLimit(k, limit, length);
            }
        }
    }

    public boolean isUnderStringSizeLimit(String k) {
        return this.itsConstantPool.isUnderUtfEncodingLimit(k);
    }

    public void addIStore(int local) {
        xop(59, 54, local);
    }

    public void addLStore(int local) {
        xop(63, 55, local);
    }

    public void addFStore(int local) {
        xop(67, 56, local);
    }

    public void addDStore(int local) {
        xop(71, 57, local);
    }

    public void addAStore(int local) {
        xop(75, 58, local);
    }

    public void addILoad(int local) {
        xop(26, 21, local);
    }

    public void addLLoad(int local) {
        xop(30, 22, local);
    }

    public void addFLoad(int local) {
        xop(34, 23, local);
    }

    public void addDLoad(int local) {
        xop(38, 24, local);
    }

    public void addALoad(int local) {
        xop(42, 25, local);
    }

    public void addLoadThis() {
        add(42);
    }

    private void xop(int shortOp, int op, int local) {
        switch (local) {
            case 0:
                add(shortOp);
                return;
            case 1:
                add(shortOp + 1);
                return;
            case 2:
                add(shortOp + 2);
                return;
            case 3:
                add(shortOp + 3);
                return;
            default:
                add(op, local);
                return;
        }
    }

    public int addTableSwitch(int low, int high) {
        if (low > high) {
            throw new ClassFileFormatException("Bad bounds: " + low + ' ' + high);
        }
        short newStack = this.itsStackTop + stackChange(170);
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        int padSize = (this.itsCodeBufferTop ^ -1) & 3;
        int addReservedCodeSpace = addReservedCodeSpace((padSize + 1) + ((((high - low) + 1) + 3) * 4));
        int switchStart = addReservedCodeSpace;
        int N = addReservedCodeSpace + 1;
        this.itsCodeBuffer[addReservedCodeSpace] = (byte) -86;
        while (padSize != 0) {
            addReservedCodeSpace = N + 1;
            this.itsCodeBuffer[N] = (byte) 0;
            padSize--;
            N = addReservedCodeSpace;
        }
        putInt32(high, this.itsCodeBuffer, putInt32(low, this.itsCodeBuffer, N + 4));
        this.itsStackTop = (short) newStack;
        if (newStack > this.itsMaxStack) {
            this.itsMaxStack = (short) newStack;
        }
        return switchStart;
    }

    public final void markTableSwitchDefault(int switchStart) {
        addSuperBlockStart(this.itsCodeBufferTop);
        this.itsJumpFroms.put(this.itsCodeBufferTop, switchStart);
        setTableSwitchJump(switchStart, -1, this.itsCodeBufferTop);
    }

    public final void markTableSwitchCase(int switchStart, int caseIndex) {
        addSuperBlockStart(this.itsCodeBufferTop);
        this.itsJumpFroms.put(this.itsCodeBufferTop, switchStart);
        setTableSwitchJump(switchStart, caseIndex, this.itsCodeBufferTop);
    }

    public final void markTableSwitchCase(int switchStart, int caseIndex, int stackTop) {
        if (stackTop < 0 || stackTop > this.itsMaxStack) {
            throw new IllegalArgumentException("Bad stack index: " + stackTop);
        }
        this.itsStackTop = (short) stackTop;
        addSuperBlockStart(this.itsCodeBufferTop);
        this.itsJumpFroms.put(this.itsCodeBufferTop, switchStart);
        setTableSwitchJump(switchStart, caseIndex, this.itsCodeBufferTop);
    }

    public void setTableSwitchJump(int switchStart, int caseIndex, int jumpTarget) {
        if (jumpTarget < 0 || jumpTarget > this.itsCodeBufferTop) {
            throw new IllegalArgumentException("Bad jump target: " + jumpTarget);
        } else if (caseIndex < -1) {
            throw new IllegalArgumentException("Bad case index: " + caseIndex);
        } else {
            int caseOffset;
            int padSize = (switchStart ^ -1) & 3;
            if (caseIndex < 0) {
                caseOffset = (switchStart + 1) + padSize;
            } else {
                caseOffset = ((switchStart + 1) + padSize) + ((caseIndex + 3) * 4);
            }
            if (switchStart < 0 || switchStart > ((this.itsCodeBufferTop - 16) - padSize) - 1) {
                throw new IllegalArgumentException(switchStart + " is outside a possible range of tableswitch" + " in already generated code");
            } else if ((this.itsCodeBuffer[switchStart] & ByteCode.IMPDEP2) != 170) {
                throw new IllegalArgumentException(switchStart + " is not offset of tableswitch statement");
            } else if (caseOffset < 0 || caseOffset + 4 > this.itsCodeBufferTop) {
                throw new ClassFileFormatException("Too big case index: " + caseIndex);
            } else {
                putInt32(jumpTarget - switchStart, this.itsCodeBuffer, caseOffset);
            }
        }
    }

    public int acquireLabel() {
        int top = this.itsLabelTableTop;
        if (this.itsLabelTable == null || top == this.itsLabelTable.length) {
            if (this.itsLabelTable == null) {
                this.itsLabelTable = new int[32];
            } else {
                int[] tmp = new int[(this.itsLabelTable.length * 2)];
                System.arraycopy(this.itsLabelTable, 0, tmp, 0, top);
                this.itsLabelTable = tmp;
            }
        }
        this.itsLabelTableTop = top + 1;
        this.itsLabelTable[top] = -1;
        return Integer.MIN_VALUE | top;
    }

    public void markLabel(int label) {
        if (label >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        label &= Integer.MAX_VALUE;
        if (label > this.itsLabelTableTop) {
            throw new IllegalArgumentException("Bad label");
        } else if (this.itsLabelTable[label] != -1) {
            throw new IllegalStateException("Can only mark label once");
        } else {
            this.itsLabelTable[label] = this.itsCodeBufferTop;
        }
    }

    public void markLabel(int label, short stackTop) {
        markLabel(label);
        this.itsStackTop = stackTop;
    }

    public void markHandler(int theLabel) {
        this.itsStackTop = (short) 1;
        markLabel(theLabel);
    }

    public int getLabelPC(int label) {
        if (label >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        label &= Integer.MAX_VALUE;
        if (label < this.itsLabelTableTop) {
            return this.itsLabelTable[label];
        }
        throw new IllegalArgumentException("Bad label");
    }

    private void addLabelFixup(int label, int fixupSite) {
        if (label >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        label &= Integer.MAX_VALUE;
        if (label >= this.itsLabelTableTop) {
            throw new IllegalArgumentException("Bad label");
        }
        int top = this.itsFixupTableTop;
        if (this.itsFixupTable == null || top == this.itsFixupTable.length) {
            if (this.itsFixupTable == null) {
                this.itsFixupTable = new long[40];
            } else {
                long[] tmp = new long[(this.itsFixupTable.length * 2)];
                System.arraycopy(this.itsFixupTable, 0, tmp, 0, top);
                this.itsFixupTable = tmp;
            }
        }
        this.itsFixupTableTop = top + 1;
        this.itsFixupTable[top] = (((long) label) << 32) | ((long) fixupSite);
    }

    private void fixLabelGotos() {
        byte[] codeBuffer = this.itsCodeBuffer;
        for (int i = 0; i < this.itsFixupTableTop; i++) {
            long fixup = this.itsFixupTable[i];
            int fixupSite = (int) fixup;
            int pc = this.itsLabelTable[(int) (fixup >> 32)];
            if (pc == -1) {
                throw new RuntimeException();
            }
            addSuperBlockStart(pc);
            this.itsJumpFroms.put(pc, fixupSite - 1);
            short offset = pc - (fixupSite - 1);
            if (((short) offset) != offset) {
                throw new ClassFileFormatException("Program too complex: too big jump offset");
            }
            codeBuffer[fixupSite] = (byte) (offset >> 8);
            codeBuffer[fixupSite + 1] = (byte) offset;
        }
        this.itsFixupTableTop = 0;
    }

    public int getCurrentCodeOffset() {
        return this.itsCodeBufferTop;
    }

    public short getStackTop() {
        return this.itsStackTop;
    }

    public void setStackTop(short n) {
        this.itsStackTop = n;
    }

    public void adjustStackTop(int delta) {
        short newStack = this.itsStackTop + delta;
        if (newStack < (short) 0 || Short.MAX_VALUE < newStack) {
            badStack(newStack);
        }
        this.itsStackTop = (short) newStack;
        if (newStack > this.itsMaxStack) {
            this.itsMaxStack = (short) newStack;
        }
    }

    private void addToCodeBuffer(int b) {
        this.itsCodeBuffer[addReservedCodeSpace(1)] = (byte) b;
    }

    private void addToCodeInt16(int value) {
        putInt16(value, this.itsCodeBuffer, addReservedCodeSpace(2));
    }

    private int addReservedCodeSpace(int size) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalArgumentException("No method to add to");
        }
        int oldTop = this.itsCodeBufferTop;
        int newTop = oldTop + size;
        if (newTop > this.itsCodeBuffer.length) {
            int newSize = this.itsCodeBuffer.length * 2;
            if (newTop > newSize) {
                newSize = newTop;
            }
            byte[] tmp = new byte[newSize];
            System.arraycopy(this.itsCodeBuffer, 0, tmp, 0, oldTop);
            this.itsCodeBuffer = tmp;
        }
        this.itsCodeBufferTop = newTop;
        return oldTop;
    }

    public void addExceptionHandler(int startLabel, int endLabel, int handlerLabel, String catchClassName) {
        if ((startLabel & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad startLabel");
        } else if ((endLabel & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad endLabel");
        } else if ((handlerLabel & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad handlerLabel");
        } else {
            short catch_type_index;
            if (catchClassName == null) {
                catch_type_index = (short) 0;
            } else {
                catch_type_index = this.itsConstantPool.addClass(catchClassName);
            }
            ExceptionTableEntry newEntry = new ExceptionTableEntry(startLabel, endLabel, handlerLabel, catch_type_index);
            int N = this.itsExceptionTableTop;
            if (N == 0) {
                this.itsExceptionTable = new ExceptionTableEntry[4];
            } else if (N == this.itsExceptionTable.length) {
                ExceptionTableEntry[] tmp = new ExceptionTableEntry[(N * 2)];
                System.arraycopy(this.itsExceptionTable, 0, tmp, 0, N);
                this.itsExceptionTable = tmp;
            }
            this.itsExceptionTable[N] = newEntry;
            this.itsExceptionTableTop = N + 1;
        }
    }

    public void addLineNumberEntry(short lineNumber) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalArgumentException("No method to stop");
        }
        int N = this.itsLineNumberTableTop;
        if (N == 0) {
            this.itsLineNumberTable = new int[16];
        } else if (N == this.itsLineNumberTable.length) {
            int[] tmp = new int[(N * 2)];
            System.arraycopy(this.itsLineNumberTable, 0, tmp, 0, N);
            this.itsLineNumberTable = tmp;
        }
        this.itsLineNumberTable[N] = (this.itsCodeBufferTop << 16) + lineNumber;
        this.itsLineNumberTableTop = N + 1;
    }

    /* access modifiers changed from: private|static */
    public static char arrayTypeToName(int type) {
        switch (type) {
            case 4:
                return 'Z';
            case 5:
                return 'C';
            case 6:
                return 'F';
            case 7:
                return 'D';
            case 8:
                return 'B';
            case 9:
                return 'S';
            case 10:
                return 'I';
            case 11:
                return 'J';
            default:
                throw new IllegalArgumentException("bad operand");
        }
    }

    private static String classDescriptorToInternalName(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1);
    }

    /* access modifiers changed from: private|static */
    public static String descriptorToInternalName(String descriptor) {
        switch (descriptor.charAt(0)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
            case '[':
                return descriptor;
            case 'L':
                return classDescriptorToInternalName(descriptor);
            default:
                throw new IllegalArgumentException("bad descriptor:" + descriptor);
        }
    }

    /* access modifiers changed from: private */
    public int[] createInitialLocals() {
        int localsTop;
        int[] initialLocals = new int[this.itsMaxLocals];
        int localsTop2 = 0;
        if ((this.itsCurrentMethod.getFlags() & 8) == 0) {
            if ("<init>".equals(this.itsCurrentMethod.getName())) {
                localsTop = 0 + 1;
                initialLocals[0] = 6;
                localsTop2 = localsTop;
            } else {
                localsTop = 0 + 1;
                initialLocals[0] = TypeInfo.OBJECT(this.itsThisClassIndex);
                localsTop2 = localsTop;
            }
        }
        String type = this.itsCurrentMethod.getType();
        int lParenIndex = type.indexOf(40);
        int rParenIndex = type.indexOf(41);
        if (lParenIndex != 0 || rParenIndex < 0) {
            throw new IllegalArgumentException("bad method type");
        }
        int start = lParenIndex + 1;
        StringBuilder paramType = new StringBuilder();
        localsTop = localsTop2;
        while (start < rParenIndex) {
            switch (type.charAt(start)) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    paramType.append(type.charAt(start));
                    start++;
                    break;
                case 'L':
                    int end = type.indexOf(59, start) + 1;
                    paramType.append(type.substring(start, end));
                    start = end;
                    break;
                case '[':
                    paramType.append('[');
                    start++;
                    continue;
            }
            int typeInfo = TypeInfo.fromType(descriptorToInternalName(paramType.toString()), this.itsConstantPool);
            localsTop2 = localsTop + 1;
            initialLocals[localsTop] = typeInfo;
            if (TypeInfo.isTwoWords(typeInfo)) {
                localsTop2++;
            }
            paramType.setLength(0);
            localsTop = localsTop2;
        }
        return initialLocals;
    }

    public void write(OutputStream oStream) throws IOException {
        oStream.write(toByteArray());
    }

    private int getWriteSize() {
        int i;
        if (this.itsSourceFileNameIndex != (short) 0) {
            this.itsConstantPool.addUtf8("SourceFile");
        }
        int size = 0 + 8;
        size = ((((((this.itsConstantPool.getWriteSize() + 8) + 2) + 2) + 2) + 2) + (this.itsInterfaces.size() * 2)) + 2;
        for (i = 0; i < this.itsFields.size(); i++) {
            size += ((ClassFileField) this.itsFields.get(i)).getWriteSize();
        }
        size += 2;
        for (i = 0; i < this.itsMethods.size(); i++) {
            size += ((ClassFileMethod) this.itsMethods.get(i)).getWriteSize();
        }
        if (this.itsSourceFileNameIndex != (short) 0) {
            return (((size + 2) + 2) + 4) + 2;
        }
        return size + 2;
    }

    public byte[] toByteArray() {
        int i;
        int dataSize = getWriteSize();
        byte[] data = new byte[dataSize];
        short sourceFileAttributeNameIndex = (short) 0;
        if (this.itsSourceFileNameIndex != (short) 0) {
            sourceFileAttributeNameIndex = this.itsConstantPool.addUtf8("SourceFile");
        }
        int offset = putInt16(this.itsInterfaces.size(), data, putInt16(this.itsSuperClassIndex, data, putInt16(this.itsThisClassIndex, data, putInt16(this.itsFlags, data, this.itsConstantPool.write(data, putInt16(MajorVersion, data, putInt16(MinorVersion, data, putInt32(FileHeaderConstant, data, 0))))))));
        for (i = 0; i < this.itsInterfaces.size(); i++) {
            offset = putInt16(((Short) this.itsInterfaces.get(i)).shortValue(), data, offset);
        }
        offset = putInt16(this.itsFields.size(), data, offset);
        for (i = 0; i < this.itsFields.size(); i++) {
            offset = ((ClassFileField) this.itsFields.get(i)).write(data, offset);
        }
        offset = putInt16(this.itsMethods.size(), data, offset);
        for (i = 0; i < this.itsMethods.size(); i++) {
            offset = ((ClassFileMethod) this.itsMethods.get(i)).write(data, offset);
        }
        if (this.itsSourceFileNameIndex != (short) 0) {
            offset = putInt16(this.itsSourceFileNameIndex, data, putInt32(2, data, putInt16(sourceFileAttributeNameIndex, data, putInt16(1, data, offset))));
        } else {
            offset = putInt16(0, data, offset);
        }
        if (offset == dataSize) {
            return data;
        }
        throw new RuntimeException();
    }

    static int putInt64(long value, byte[] array, int offset) {
        return putInt32((int) value, array, putInt32((int) (value >>> 32), array, offset));
    }

    private static void badStack(int value) {
        String s;
        if (value < 0) {
            s = "Stack underflow: " + value;
        } else {
            s = "Too big stack: " + value;
        }
        throw new IllegalStateException(s);
    }

    /* access modifiers changed from: private|static */
    /* JADX WARNING: Missing block: B:21:0x0043, code skipped:
            r7 = r7 - 1;
            r1 = r1 + 1;
            r2 = r2 + 1;
     */
    /* JADX WARNING: Missing block: B:29:0x0067, code skipped:
            r7 = r7 - 1;
            r1 = r1 + 1;
            r2 = r2 + 1;
            r6 = r11.indexOf(59, r2);
     */
    /* JADX WARNING: Missing block: B:30:0x0075, code skipped:
            if ((r2 + 1) > r6) goto L_0x0079;
     */
    /* JADX WARNING: Missing block: B:31:0x0077, code skipped:
            if (r6 < r5) goto L_0x007b;
     */
    /* JADX WARNING: Missing block: B:32:0x0079, code skipped:
            r4 = false;
     */
    /* JADX WARNING: Missing block: B:33:0x007b, code skipped:
            r2 = r6 + 1;
     */
    public static int sizeOfParameters(java.lang.String r11) {
        /*
        r3 = r11.length();
        r8 = 41;
        r5 = r11.lastIndexOf(r8);
        r8 = 3;
        if (r8 > r3) goto L_0x0083;
    L_0x000d:
        r8 = 0;
        r8 = r11.charAt(r8);
        r9 = 40;
        if (r8 != r9) goto L_0x0083;
    L_0x0016:
        r8 = 1;
        if (r8 > r5) goto L_0x0083;
    L_0x0019:
        r8 = r5 + 1;
        if (r8 >= r3) goto L_0x0083;
    L_0x001d:
        r4 = 1;
        r2 = 1;
        r7 = 0;
        r1 = 0;
    L_0x0021:
        if (r2 == r5) goto L_0x002b;
    L_0x0023:
        r8 = r11.charAt(r2);
        switch(r8) {
            case 66: goto L_0x0043;
            case 67: goto L_0x0043;
            case 68: goto L_0x0041;
            case 70: goto L_0x0043;
            case 73: goto L_0x0043;
            case 74: goto L_0x0041;
            case 76: goto L_0x0067;
            case 83: goto L_0x0043;
            case 90: goto L_0x0043;
            case 91: goto L_0x004a;
            default: goto L_0x002a;
        };
    L_0x002a:
        r4 = 0;
    L_0x002b:
        if (r4 == 0) goto L_0x0083;
    L_0x002d:
        r8 = r5 + 1;
        r8 = r11.charAt(r8);
        switch(r8) {
            case 66: goto L_0x0080;
            case 67: goto L_0x0080;
            case 68: goto L_0x007e;
            case 69: goto L_0x0036;
            case 70: goto L_0x0080;
            case 71: goto L_0x0036;
            case 72: goto L_0x0036;
            case 73: goto L_0x0080;
            case 74: goto L_0x007e;
            case 75: goto L_0x0036;
            case 76: goto L_0x0080;
            case 77: goto L_0x0036;
            case 78: goto L_0x0036;
            case 79: goto L_0x0036;
            case 80: goto L_0x0036;
            case 81: goto L_0x0036;
            case 82: goto L_0x0036;
            case 83: goto L_0x0080;
            case 84: goto L_0x0036;
            case 85: goto L_0x0036;
            case 86: goto L_0x0037;
            case 87: goto L_0x0036;
            case 88: goto L_0x0036;
            case 89: goto L_0x0036;
            case 90: goto L_0x0080;
            case 91: goto L_0x0080;
            default: goto L_0x0036;
        };
    L_0x0036:
        r4 = 0;
    L_0x0037:
        if (r4 == 0) goto L_0x0083;
    L_0x0039:
        r8 = r1 << 16;
        r9 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r9 = r9 & r7;
        r8 = r8 | r9;
        return r8;
    L_0x0041:
        r7 = r7 + -1;
    L_0x0043:
        r7 = r7 + -1;
        r1 = r1 + 1;
        r2 = r2 + 1;
        goto L_0x0021;
    L_0x004a:
        r2 = r2 + 1;
        r0 = r11.charAt(r2);
    L_0x0050:
        r8 = 91;
        if (r0 != r8) goto L_0x005b;
    L_0x0054:
        r2 = r2 + 1;
        r0 = r11.charAt(r2);
        goto L_0x0050;
    L_0x005b:
        switch(r0) {
            case 66: goto L_0x0060;
            case 67: goto L_0x0060;
            case 68: goto L_0x0060;
            case 70: goto L_0x0060;
            case 73: goto L_0x0060;
            case 74: goto L_0x0060;
            case 76: goto L_0x0067;
            case 83: goto L_0x0060;
            case 90: goto L_0x0060;
            default: goto L_0x005e;
        };
    L_0x005e:
        r4 = 0;
        goto L_0x002b;
    L_0x0060:
        r7 = r7 + -1;
        r1 = r1 + 1;
        r2 = r2 + 1;
        goto L_0x0021;
    L_0x0067:
        r7 = r7 + -1;
        r1 = r1 + 1;
        r2 = r2 + 1;
        r8 = 59;
        r6 = r11.indexOf(r8, r2);
        r8 = r2 + 1;
        if (r8 > r6) goto L_0x0079;
    L_0x0077:
        if (r6 < r5) goto L_0x007b;
    L_0x0079:
        r4 = 0;
        goto L_0x002b;
    L_0x007b:
        r2 = r6 + 1;
        goto L_0x0021;
    L_0x007e:
        r7 = r7 + 1;
    L_0x0080:
        r7 = r7 + 1;
        goto L_0x0037;
    L_0x0083:
        r8 = new java.lang.IllegalArgumentException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "Bad parameter signature: ";
        r9 = r9.append(r10);
        r9 = r9.append(r11);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.classfile.ClassFileWriter.sizeOfParameters(java.lang.String):int");
    }

    static int putInt16(int value, byte[] array, int offset) {
        array[offset + 0] = (byte) (value >>> 8);
        array[offset + 1] = (byte) value;
        return offset + 2;
    }

    static int putInt32(int value, byte[] array, int offset) {
        array[offset + 0] = (byte) (value >>> 24);
        array[offset + 1] = (byte) (value >>> 16);
        array[offset + 2] = (byte) (value >>> 8);
        array[offset + 3] = (byte) value;
        return offset + 4;
    }

    static int opcodeLength(int opcode, boolean wide) {
        switch (opcode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 190:
            case 191:
            case 194:
            case 195:
            case ByteCode.WIDE /*196*/:
            case ByteCode.BREAKPOINT /*202*/:
            case ByteCode.IMPDEP1 /*254*/:
            case ByteCode.IMPDEP2 /*255*/:
                return 1;
            case 16:
            case 18:
            case 188:
                return 2;
            case 17:
            case 19:
            case 20:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 187:
            case 189:
            case 192:
            case 193:
            case 198:
            case 199:
                return 3;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 169:
                if (wide) {
                    return 3;
                }
                return 2;
            case 132:
                if (wide) {
                    return 5;
                }
                return 3;
            case 185:
            case ByteCode.GOTO_W /*200*/:
            case ByteCode.JSR_W /*201*/:
                return 5;
            case 197:
                return 4;
            default:
                throw new IllegalArgumentException("Bad opcode: " + opcode);
        }
    }

    static int opcodeCount(int opcode) {
        switch (opcode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 190:
            case 191:
            case 194:
            case 195:
            case ByteCode.WIDE /*196*/:
            case ByteCode.BREAKPOINT /*202*/:
            case ByteCode.IMPDEP1 /*254*/:
            case ByteCode.IMPDEP2 /*255*/:
                return 0;
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
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 187:
            case 188:
            case 189:
            case 192:
            case 193:
            case 198:
            case 199:
            case ByteCode.GOTO_W /*200*/:
            case ByteCode.JSR_W /*201*/:
                return 1;
            case 132:
            case 197:
                return 2;
            case 170:
            case 171:
                return -1;
            default:
                throw new IllegalArgumentException("Bad opcode: " + opcode);
        }
    }

    static int stackChange(int opcode) {
        switch (opcode) {
            case 0:
            case 47:
            case 49:
            case 95:
            case 116:
            case 117:
            case 118:
            case 119:
            case 132:
            case 134:
            case 138:
            case 139:
            case 143:
            case 145:
            case 146:
            case 147:
            case 167:
            case 169:
            case 177:
            case 178:
            case 179:
            case 184:
            case 188:
            case 189:
            case 190:
            case 192:
            case 193:
            case ByteCode.WIDE /*196*/:
            case ByteCode.GOTO_W /*200*/:
            case ByteCode.BREAKPOINT /*202*/:
            case ByteCode.IMPDEP1 /*254*/:
            case ByteCode.IMPDEP2 /*255*/:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 12:
            case 13:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 34:
            case 35:
            case 36:
            case 37:
            case 42:
            case 43:
            case 44:
            case 45:
            case 89:
            case 90:
            case 91:
            case 133:
            case 135:
            case 140:
            case 141:
            case 168:
            case 187:
            case 197:
            case ByteCode.JSR_W /*201*/:
                return 1;
            case 9:
            case 10:
            case 14:
            case 15:
            case 20:
            case 22:
            case 24:
            case 30:
            case 31:
            case 32:
            case 33:
            case 38:
            case 39:
            case 40:
            case 41:
            case 92:
            case 93:
            case 94:
                return 2;
            case 46:
            case 48:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 67:
            case 68:
            case 69:
            case 70:
            case 75:
            case 76:
            case 77:
            case 78:
            case 87:
            case 96:
            case 98:
            case 100:
            case 102:
            case 104:
            case 106:
            case 108:
            case 110:
            case 112:
            case 114:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 128:
            case 130:
            case 136:
            case 137:
            case 142:
            case 144:
            case 149:
            case 150:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 180:
            case 181:
            case 182:
            case 183:
            case 185:
            case 191:
            case 194:
            case 195:
            case 198:
            case 199:
                return -1;
            case 55:
            case 57:
            case 63:
            case 64:
            case 65:
            case 66:
            case 71:
            case 72:
            case 73:
            case 74:
            case 88:
            case 97:
            case 99:
            case 101:
            case 103:
            case 105:
            case 107:
            case 109:
            case 111:
            case 113:
            case 115:
            case 127:
            case 129:
            case 131:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175:
                return -2;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
            case 148:
            case 151:
            case 152:
                return -3;
            case 80:
            case 82:
                return -4;
            default:
                throw new IllegalArgumentException("Bad opcode: " + opcode);
        }
    }

    private static String bytecodeStr(int code) {
        return "";
    }

    /* access modifiers changed from: final */
    public final char[] getCharBuffer(int minimalSize) {
        if (minimalSize > this.tmpCharBuffer.length) {
            int newSize = this.tmpCharBuffer.length * 2;
            if (minimalSize > newSize) {
                newSize = minimalSize;
            }
            this.tmpCharBuffer = new char[newSize];
        }
        return this.tmpCharBuffer;
    }

    private void addSuperBlockStart(int pc) {
        if (GenerateStackMap) {
            if (this.itsSuperBlockStarts == null) {
                this.itsSuperBlockStarts = new int[4];
            } else if (this.itsSuperBlockStarts.length == this.itsSuperBlockStartsTop) {
                int[] tmp = new int[(this.itsSuperBlockStartsTop * 2)];
                System.arraycopy(this.itsSuperBlockStarts, 0, tmp, 0, this.itsSuperBlockStartsTop);
                this.itsSuperBlockStarts = tmp;
            }
            int[] iArr = this.itsSuperBlockStarts;
            int i = this.itsSuperBlockStartsTop;
            this.itsSuperBlockStartsTop = i + 1;
            iArr[i] = pc;
        }
    }

    private void finalizeSuperBlockStarts() {
        if (GenerateStackMap) {
            int i;
            for (i = 0; i < this.itsExceptionTableTop; i++) {
                addSuperBlockStart((short) getLabelPC(this.itsExceptionTable[i].itsHandlerLabel));
            }
            Arrays.sort(this.itsSuperBlockStarts, 0, this.itsSuperBlockStartsTop);
            int prev = this.itsSuperBlockStarts[0];
            int copyTo = 1;
            for (i = 1; i < this.itsSuperBlockStartsTop; i++) {
                int curr = this.itsSuperBlockStarts[i];
                if (prev != curr) {
                    if (copyTo != i) {
                        this.itsSuperBlockStarts[copyTo] = curr;
                    }
                    copyTo++;
                    prev = curr;
                }
            }
            this.itsSuperBlockStartsTop = copyTo;
            if (this.itsSuperBlockStarts[copyTo - 1] == this.itsCodeBufferTop) {
                this.itsSuperBlockStartsTop--;
            }
        }
    }

    static {
        boolean z = true;
        InputStream is = null;
        int minor = 0;
        try {
            is = ClassFileWriter.class.getResourceAsStream("ClassFileWriter.class");
            if (is == null) {
                is = ClassLoader.getSystemResourceAsStream("org/mozilla/classfile/ClassFileWriter.class");
            }
            byte[] header = new byte[8];
            int read = 0;
            while (read < 8) {
                int c = is.read(header, read, 8 - read);
                if (c < 0) {
                    throw new IOException();
                }
                read += c;
            }
            minor = (header[4] << 8) | (header[5] & ByteCode.IMPDEP2);
            int major = (header[6] << 8) | (header[7] & ByteCode.IMPDEP2);
            MinorVersion = minor;
            MajorVersion = major;
            if (major < 50) {
                z = false;
            }
            GenerateStackMap = z;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e2) {
            MinorVersion = minor;
            MajorVersion = 48;
            if (48 < 50) {
                z = false;
            }
            GenerateStackMap = z;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            MinorVersion = minor;
            MajorVersion = 48;
            if (48 < 50) {
                z = false;
            }
            GenerateStackMap = z;
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }
    }
}
