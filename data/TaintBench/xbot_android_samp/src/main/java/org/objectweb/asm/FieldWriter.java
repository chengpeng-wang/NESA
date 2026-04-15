package org.objectweb.asm;

final class FieldWriter extends FieldVisitor {
    private final ClassWriter b;
    private final int c;
    private final int d;
    private final int e;
    private int f;
    private int g;
    private AnnotationWriter h;
    private AnnotationWriter i;
    private Attribute j;

    FieldWriter(ClassWriter classWriter, int i, String str, String str2, String str3, Object obj) {
        super(Opcodes.ASM4);
        if (classWriter.B == null) {
            classWriter.B = this;
        } else {
            classWriter.C.fv = this;
        }
        classWriter.C = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.f = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.g = classWriter.a(obj).a;
        }
    }

    /* access modifiers changed from: 0000 */
    public int a() {
        int a;
        int i = 8;
        if (this.g != 0) {
            this.b.newUTF8("ConstantValue");
            i = 16;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            this.b.newUTF8("Synthetic");
            i += 6;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            this.b.newUTF8("Deprecated");
            i += 6;
        }
        if (this.f != 0) {
            this.b.newUTF8("Signature");
            i += 8;
        }
        if (this.h != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            i += this.h.a() + 8;
        }
        if (this.i != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            a = i + (this.i.a() + 8);
        } else {
            a = i;
        }
        return this.j != null ? a + this.j.a(this.b, null, 0, -1, -1) : a;
    }

    /* access modifiers changed from: 0000 */
    public void a(ByteVector byteVector) {
        byteVector.putShort(((393216 | ((this.c & Opcodes.ASM4) / 64)) ^ -1) & this.c).putShort(this.d).putShort(this.e);
        int i = this.g != 0 ? 1 : 0;
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            i++;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            i++;
        }
        if (this.f != 0) {
            i++;
        }
        if (this.h != null) {
            i++;
        }
        if (this.i != null) {
            i++;
        }
        if (this.j != null) {
            i += this.j.a();
        }
        byteVector.putShort(i);
        if (this.g != 0) {
            byteVector.putShort(this.b.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.g);
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.f != 0) {
            byteVector.putShort(this.b.newUTF8("Signature"));
            byteVector.putInt(2).putShort(this.f);
        }
        if (this.h != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.h.a(byteVector);
        }
        if (this.i != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.i.a(byteVector);
        }
        if (this.j != null) {
            this.j.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.h;
            this.h = annotationWriter;
        } else {
            annotationWriter.g = this.i;
            this.i = annotationWriter;
        }
        return annotationWriter;
    }

    public void visitAttribute(Attribute attribute) {
        attribute.a = this.j;
        this.j = attribute;
    }

    public void visitEnd() {
    }
}
