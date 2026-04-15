package org.mozilla.classfile;

/* compiled from: ClassFileWriter */
final class ClassFileMethod {
    private byte[] itsCodeAttribute;
    private short itsFlags;
    private String itsName;
    private short itsNameIndex;
    private String itsType;
    private short itsTypeIndex;

    ClassFileMethod(String name, short nameIndex, String type, short typeIndex, short flags) {
        this.itsName = name;
        this.itsNameIndex = nameIndex;
        this.itsType = type;
        this.itsTypeIndex = typeIndex;
        this.itsFlags = flags;
    }

    /* access modifiers changed from: 0000 */
    public void setCodeAttribute(byte[] codeAttribute) {
        this.itsCodeAttribute = codeAttribute;
    }

    /* access modifiers changed from: 0000 */
    public int write(byte[] data, int offset) {
        offset = ClassFileWriter.putInt16(1, data, ClassFileWriter.putInt16(this.itsTypeIndex, data, ClassFileWriter.putInt16(this.itsNameIndex, data, ClassFileWriter.putInt16(this.itsFlags, data, offset))));
        System.arraycopy(this.itsCodeAttribute, 0, data, offset, this.itsCodeAttribute.length);
        return offset + this.itsCodeAttribute.length;
    }

    /* access modifiers changed from: 0000 */
    public int getWriteSize() {
        return this.itsCodeAttribute.length + 8;
    }

    /* access modifiers changed from: 0000 */
    public String getName() {
        return this.itsName;
    }

    /* access modifiers changed from: 0000 */
    public String getType() {
        return this.itsType;
    }

    /* access modifiers changed from: 0000 */
    public short getFlags() {
        return this.itsFlags;
    }
}
