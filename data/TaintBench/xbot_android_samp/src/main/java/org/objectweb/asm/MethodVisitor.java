package org.objectweb.asm;

public abstract class MethodVisitor {
    protected final int api;
    protected MethodVisitor mv;

    public MethodVisitor(int i) {
        this(i, null);
    }

    public MethodVisitor(int i, MethodVisitor methodVisitor) {
        this.api = i;
        this.mv = methodVisitor;
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        return this.mv != null ? this.mv.visitAnnotation(str, z) : null;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        return this.mv != null ? this.mv.visitAnnotationDefault() : null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.mv != null) {
            this.mv.visitAttribute(attribute);
        }
    }

    public void visitCode() {
        if (this.mv != null) {
            this.mv.visitCode();
        }
    }

    public void visitEnd() {
        if (this.mv != null) {
            this.mv.visitEnd();
        }
    }

    public void visitFieldInsn(int i, String str, String str2, String str3) {
        if (this.mv != null) {
            this.mv.visitFieldInsn(i, str, str2, str3);
        }
    }

    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        if (this.mv != null) {
            this.mv.visitFrame(i, i2, objArr, i3, objArr2);
        }
    }

    public void visitIincInsn(int i, int i2) {
        if (this.mv != null) {
            this.mv.visitIincInsn(i, i2);
        }
    }

    public void visitInsn(int i) {
        if (this.mv != null) {
            this.mv.visitInsn(i);
        }
    }

    public void visitIntInsn(int i, int i2) {
        if (this.mv != null) {
            this.mv.visitIntInsn(i, i2);
        }
    }

    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(str, str2, handle, objArr);
        }
    }

    public void visitJumpInsn(int i, Label label) {
        if (this.mv != null) {
            this.mv.visitJumpInsn(i, label);
        }
    }

    public void visitLabel(Label label) {
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
    }

    public void visitLdcInsn(Object obj) {
        if (this.mv != null) {
            this.mv.visitLdcInsn(obj);
        }
    }

    public void visitLineNumber(int i, Label label) {
        if (this.mv != null) {
            this.mv.visitLineNumber(i, label);
        }
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        if (this.mv != null) {
            this.mv.visitLocalVariable(str, str2, str3, label, label2, i);
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(label, iArr, labelArr);
        }
    }

    public void visitMaxs(int i, int i2) {
        if (this.mv != null) {
            this.mv.visitMaxs(i, i2);
        }
    }

    public void visitMethodInsn(int i, String str, String str2, String str3) {
        if (this.mv != null) {
            this.mv.visitMethodInsn(i, str, str2, str3);
        }
    }

    public void visitMultiANewArrayInsn(String str, int i) {
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(str, i);
        }
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        return this.mv != null ? this.mv.visitParameterAnnotation(i, str, z) : null;
    }

    public void visitTableSwitchInsn(int i, int i2, Label label, Label... labelArr) {
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(i, i2, label, labelArr);
        }
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        if (this.mv != null) {
            this.mv.visitTryCatchBlock(label, label2, label3, str);
        }
    }

    public void visitTypeInsn(int i, String str) {
        if (this.mv != null) {
            this.mv.visitTypeInsn(i, str);
        }
    }

    public void visitVarInsn(int i, int i2) {
        if (this.mv != null) {
            this.mv.visitVarInsn(i, i2);
        }
    }
}
