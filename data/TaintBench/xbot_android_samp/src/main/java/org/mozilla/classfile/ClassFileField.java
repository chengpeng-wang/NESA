package org.mozilla.classfile;

/* compiled from: ClassFileWriter */
final class ClassFileField {
    private short itsAttr1;
    private short itsAttr2;
    private short itsAttr3;
    private short itsFlags;
    private boolean itsHasAttributes = false;
    private int itsIndex;
    private short itsNameIndex;
    private short itsTypeIndex;

    ClassFileField(short nameIndex, short typeIndex, short flags) {
        this.itsNameIndex = nameIndex;
        this.itsTypeIndex = typeIndex;
        this.itsFlags = flags;
    }

    /* access modifiers changed from: 0000 */
    public void setAttributes(short attr1, short attr2, short attr3, int index) {
        this.itsHasAttributes = true;
        this.itsAttr1 = attr1;
        this.itsAttr2 = attr2;
        this.itsAttr3 = attr3;
        this.itsIndex = index;
    }

    /* access modifiers changed from: 0000 */
    public int write(byte[] data, int offset) {
        offset = ClassFileWriter.putInt16(this.itsTypeIndex, data, ClassFileWriter.putInt16(this.itsNameIndex, data, ClassFileWriter.putInt16(this.itsFlags, data, offset)));
        if (!this.itsHasAttributes) {
            return ClassFileWriter.putInt16(0, data, offset);
        }
        return ClassFileWriter.putInt16(this.itsIndex, data, ClassFileWriter.putInt16(this.itsAttr3, data, ClassFileWriter.putInt16(this.itsAttr2, data, ClassFileWriter.putInt16(this.itsAttr1, data, ClassFileWriter.putInt16(1, data, offset)))));
    }

    /* access modifiers changed from: 0000 */
    public int getWriteSize() {
        if (this.itsHasAttributes) {
            return 6 + 10;
        }
        return 6 + 2;
    }
}
