package org.objectweb.asm;

public abstract class ClassVisitor {
    protected final int api;
    protected ClassVisitor cv;

    public ClassVisitor(int i) {
        this(i, null);
    }

    public ClassVisitor(int i, ClassVisitor classVisitor) {
        this.api = i;
        this.cv = classVisitor;
    }

    public void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        if (this.cv != null) {
            this.cv.visit(i, i2, str, str2, str3, strArr);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        return this.cv != null ? this.cv.visitAnnotation(str, z) : null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute(attribute);
        }
    }

    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }

    public FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return this.cv != null ? this.cv.visitField(i, str, str2, str3, obj) : null;
    }

    public void visitInnerClass(String str, String str2, String str3, int i) {
        if (this.cv != null) {
            this.cv.visitInnerClass(str, str2, str3, i);
        }
    }

    public MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return this.cv != null ? this.cv.visitMethod(i, str, str2, str3, strArr) : null;
    }

    public void visitOuterClass(String str, String str2, String str3) {
        if (this.cv != null) {
            this.cv.visitOuterClass(str, str2, str3);
        }
    }

    public void visitSource(String str, String str2) {
        if (this.cv != null) {
            this.cv.visitSource(str, str2);
        }
    }
}
