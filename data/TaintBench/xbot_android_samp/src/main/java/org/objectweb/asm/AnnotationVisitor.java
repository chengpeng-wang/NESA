package org.objectweb.asm;

public abstract class AnnotationVisitor {
    protected final int api;
    protected AnnotationVisitor av;

    public AnnotationVisitor(int i) {
        this(i, null);
    }

    public AnnotationVisitor(int i, AnnotationVisitor annotationVisitor) {
        this.api = i;
        this.av = annotationVisitor;
    }

    public void visit(String str, Object obj) {
        if (this.av != null) {
            this.av.visit(str, obj);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, String str2) {
        return this.av != null ? this.av.visitAnnotation(str, str2) : null;
    }

    public AnnotationVisitor visitArray(String str) {
        return this.av != null ? this.av.visitArray(str) : null;
    }

    public void visitEnd() {
        if (this.av != null) {
            this.av.visitEnd();
        }
    }

    public void visitEnum(String str, String str2, String str3) {
        if (this.av != null) {
            this.av.visitEnum(str, str2, str3);
        }
    }
}
