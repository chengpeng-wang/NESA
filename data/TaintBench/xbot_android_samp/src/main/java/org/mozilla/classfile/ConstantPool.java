package org.mozilla.classfile;

import org.mozilla.javascript.ObjToIntMap;
import org.mozilla.javascript.UintMap;

/* compiled from: ClassFileWriter */
final class ConstantPool {
    static final byte CONSTANT_Class = (byte) 7;
    static final byte CONSTANT_Double = (byte) 6;
    static final byte CONSTANT_Fieldref = (byte) 9;
    static final byte CONSTANT_Float = (byte) 4;
    static final byte CONSTANT_Integer = (byte) 3;
    static final byte CONSTANT_InterfaceMethodref = (byte) 11;
    static final byte CONSTANT_Long = (byte) 5;
    static final byte CONSTANT_Methodref = (byte) 10;
    static final byte CONSTANT_NameAndType = (byte) 12;
    static final byte CONSTANT_String = (byte) 8;
    static final byte CONSTANT_Utf8 = (byte) 1;
    private static final int ConstantPoolSize = 256;
    private static final int MAX_UTF_ENCODING_SIZE = 65535;
    private ClassFileWriter cfw;
    private ObjToIntMap itsClassHash = new ObjToIntMap();
    private UintMap itsConstantData = new UintMap();
    private ObjToIntMap itsFieldRefHash = new ObjToIntMap();
    private ObjToIntMap itsMethodRefHash = new ObjToIntMap();
    private byte[] itsPool;
    private UintMap itsPoolTypes = new UintMap();
    private UintMap itsStringConstHash = new UintMap();
    private int itsTop;
    private int itsTopIndex;
    private ObjToIntMap itsUtf8Hash = new ObjToIntMap();

    ConstantPool(ClassFileWriter cfw) {
        this.cfw = cfw;
        this.itsTopIndex = 1;
        this.itsPool = new byte[256];
        this.itsTop = 0;
    }

    /* access modifiers changed from: 0000 */
    public int write(byte[] data, int offset) {
        offset = ClassFileWriter.putInt16((short) this.itsTopIndex, data, offset);
        System.arraycopy(this.itsPool, 0, data, offset, this.itsTop);
        return offset + this.itsTop;
    }

    /* access modifiers changed from: 0000 */
    public int getWriteSize() {
        return this.itsTop + 2;
    }

    /* access modifiers changed from: 0000 */
    public int addConstant(int k) {
        ensure(5);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = CONSTANT_Integer;
        this.itsTop = ClassFileWriter.putInt32(k, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 3);
        int i2 = this.itsTopIndex;
        this.itsTopIndex = i2 + 1;
        return (short) i2;
    }

    /* access modifiers changed from: 0000 */
    public int addConstant(long k) {
        ensure(9);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = (byte) 5;
        this.itsTop = ClassFileWriter.putInt64(k, this.itsPool, this.itsTop);
        int index = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(index, 5);
        return index;
    }

    /* access modifiers changed from: 0000 */
    public int addConstant(float k) {
        ensure(5);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = (byte) 4;
        this.itsTop = ClassFileWriter.putInt32(Float.floatToIntBits(k), this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 4);
        int i2 = this.itsTopIndex;
        this.itsTopIndex = i2 + 1;
        return i2;
    }

    /* access modifiers changed from: 0000 */
    public int addConstant(double k) {
        ensure(9);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = (byte) 6;
        this.itsTop = ClassFileWriter.putInt64(Double.doubleToLongBits(k), this.itsPool, this.itsTop);
        int index = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(index, 6);
        return index;
    }

    /* access modifiers changed from: 0000 */
    public int addConstant(String k) {
        int utf8Index = MAX_UTF_ENCODING_SIZE & addUtf8(k);
        int theIndex = this.itsStringConstHash.getInt(utf8Index, -1);
        if (theIndex == -1) {
            theIndex = this.itsTopIndex;
            this.itsTopIndex = theIndex + 1;
            ensure(3);
            byte[] bArr = this.itsPool;
            int i = this.itsTop;
            this.itsTop = i + 1;
            bArr[i] = (byte) 8;
            this.itsTop = ClassFileWriter.putInt16(utf8Index, this.itsPool, this.itsTop);
            this.itsStringConstHash.put(utf8Index, theIndex);
        }
        this.itsPoolTypes.put(theIndex, 8);
        return theIndex;
    }

    /* access modifiers changed from: 0000 */
    public boolean isUnderUtfEncodingLimit(String s) {
        int strLen = s.length();
        if (strLen * 3 <= MAX_UTF_ENCODING_SIZE) {
            return true;
        }
        if (strLen > MAX_UTF_ENCODING_SIZE) {
            return false;
        }
        if (strLen != getUtfEncodingLimit(s, 0, strLen)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public int getUtfEncodingLimit(String s, int start, int end) {
        if ((end - start) * 3 <= MAX_UTF_ENCODING_SIZE) {
            return end;
        }
        int limit = MAX_UTF_ENCODING_SIZE;
        for (int i = start; i != end; i++) {
            int c = s.charAt(i);
            if (c != 0 && c <= 127) {
                limit--;
            } else if (c < 2047) {
                limit -= 2;
            } else {
                limit -= 3;
            }
            if (limit < 0) {
                return i;
            }
        }
        return end;
    }

    /* access modifiers changed from: 0000 */
    public short addUtf8(String k) {
        int theIndex = this.itsUtf8Hash.get(k, -1);
        if (theIndex == -1) {
            boolean tooBigString;
            int strLen = k.length();
            if (strLen > MAX_UTF_ENCODING_SIZE) {
                tooBigString = true;
            } else {
                tooBigString = false;
                ensure((strLen * 3) + 3);
                int top = this.itsTop;
                int top2 = top + 1;
                this.itsPool[top] = (byte) 1;
                top = top2 + 2;
                char[] chars = this.cfw.getCharBuffer(strLen);
                k.getChars(0, strLen, chars, 0);
                int i = 0;
                top2 = top;
                while (i != strLen) {
                    int c = chars[i];
                    if (c != 0 && c <= 127) {
                        top = top2 + 1;
                        this.itsPool[top2] = (byte) c;
                    } else if (c > 2047) {
                        top = top2 + 1;
                        this.itsPool[top2] = (byte) ((c >> 12) | 224);
                        top2 = top + 1;
                        this.itsPool[top] = (byte) (((c >> 6) & 63) | 128);
                        top = top2 + 1;
                        this.itsPool[top2] = (byte) ((c & 63) | 128);
                    } else {
                        top = top2 + 1;
                        this.itsPool[top2] = (byte) ((c >> 6) | 192);
                        top2 = top + 1;
                        this.itsPool[top] = (byte) ((c & 63) | 128);
                        top = top2;
                    }
                    i++;
                    top2 = top;
                }
                int utfLen = top2 - ((this.itsTop + 1) + 2);
                if (utfLen > MAX_UTF_ENCODING_SIZE) {
                    tooBigString = true;
                } else {
                    this.itsPool[this.itsTop + 1] = (byte) (utfLen >>> 8);
                    this.itsPool[this.itsTop + 2] = (byte) utfLen;
                    this.itsTop = top2;
                    theIndex = this.itsTopIndex;
                    this.itsTopIndex = theIndex + 1;
                    this.itsUtf8Hash.put(k, theIndex);
                }
            }
            if (tooBigString) {
                throw new IllegalArgumentException("Too big string");
            }
        }
        setConstantData(theIndex, k);
        this.itsPoolTypes.put(theIndex, 1);
        return (short) theIndex;
    }

    private short addNameAndType(String name, String type) {
        short nameIndex = addUtf8(name);
        short typeIndex = addUtf8(type);
        ensure(5);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = CONSTANT_NameAndType;
        this.itsTop = ClassFileWriter.putInt16(nameIndex, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(typeIndex, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 12);
        int i2 = this.itsTopIndex;
        this.itsTopIndex = i2 + 1;
        return (short) i2;
    }

    /* access modifiers changed from: 0000 */
    public short addClass(String className) {
        int theIndex = this.itsClassHash.get(className, -1);
        if (theIndex == -1) {
            String slashed = className;
            if (className.indexOf(46) > 0) {
                slashed = ClassFileWriter.getSlashedForm(className);
                theIndex = this.itsClassHash.get(slashed, -1);
                if (theIndex != -1) {
                    this.itsClassHash.put(className, theIndex);
                }
            }
            if (theIndex == -1) {
                int utf8Index = addUtf8(slashed);
                ensure(3);
                byte[] bArr = this.itsPool;
                int i = this.itsTop;
                this.itsTop = i + 1;
                bArr[i] = (byte) 7;
                this.itsTop = ClassFileWriter.putInt16(utf8Index, this.itsPool, this.itsTop);
                theIndex = this.itsTopIndex;
                this.itsTopIndex = theIndex + 1;
                this.itsClassHash.put(slashed, theIndex);
                if (className != slashed) {
                    this.itsClassHash.put(className, theIndex);
                }
            }
        }
        setConstantData(theIndex, className);
        this.itsPoolTypes.put(theIndex, 7);
        return (short) theIndex;
    }

    /* access modifiers changed from: 0000 */
    public short addFieldRef(String className, String fieldName, String fieldType) {
        FieldOrMethodRef ref = new FieldOrMethodRef(className, fieldName, fieldType);
        int theIndex = this.itsFieldRefHash.get(ref, -1);
        if (theIndex == -1) {
            short ntIndex = addNameAndType(fieldName, fieldType);
            short classIndex = addClass(className);
            ensure(5);
            byte[] bArr = this.itsPool;
            int i = this.itsTop;
            this.itsTop = i + 1;
            bArr[i] = (byte) 9;
            this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex;
            this.itsTopIndex = theIndex + 1;
            this.itsFieldRefHash.put(ref, theIndex);
        }
        setConstantData(theIndex, ref);
        this.itsPoolTypes.put(theIndex, 9);
        return (short) theIndex;
    }

    /* access modifiers changed from: 0000 */
    public short addMethodRef(String className, String methodName, String methodType) {
        FieldOrMethodRef ref = new FieldOrMethodRef(className, methodName, methodType);
        int theIndex = this.itsMethodRefHash.get(ref, -1);
        if (theIndex == -1) {
            short ntIndex = addNameAndType(methodName, methodType);
            short classIndex = addClass(className);
            ensure(5);
            byte[] bArr = this.itsPool;
            int i = this.itsTop;
            this.itsTop = i + 1;
            bArr[i] = (byte) 10;
            this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex;
            this.itsTopIndex = theIndex + 1;
            this.itsMethodRefHash.put(ref, theIndex);
        }
        setConstantData(theIndex, ref);
        this.itsPoolTypes.put(theIndex, 10);
        return (short) theIndex;
    }

    /* access modifiers changed from: 0000 */
    public short addInterfaceMethodRef(String className, String methodName, String methodType) {
        short ntIndex = addNameAndType(methodName, methodType);
        short classIndex = addClass(className);
        ensure(5);
        byte[] bArr = this.itsPool;
        int i = this.itsTop;
        this.itsTop = i + 1;
        bArr[i] = (byte) 11;
        this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
        setConstantData(this.itsTopIndex, new FieldOrMethodRef(className, methodName, methodType));
        this.itsPoolTypes.put(this.itsTopIndex, 11);
        int i2 = this.itsTopIndex;
        this.itsTopIndex = i2 + 1;
        return (short) i2;
    }

    /* access modifiers changed from: 0000 */
    public Object getConstantData(int index) {
        return this.itsConstantData.getObject(index);
    }

    /* access modifiers changed from: 0000 */
    public void setConstantData(int index, Object data) {
        this.itsConstantData.put(index, data);
    }

    /* access modifiers changed from: 0000 */
    public byte getConstantType(int index) {
        return (byte) this.itsPoolTypes.getInt(index, 0);
    }

    /* access modifiers changed from: 0000 */
    public void ensure(int howMuch) {
        if (this.itsTop + howMuch > this.itsPool.length) {
            int newCapacity = this.itsPool.length * 2;
            if (this.itsTop + howMuch > newCapacity) {
                newCapacity = this.itsTop + howMuch;
            }
            byte[] tmp = new byte[newCapacity];
            System.arraycopy(this.itsPool, 0, tmp, 0, this.itsTop);
            this.itsPool = tmp;
        }
    }
}
