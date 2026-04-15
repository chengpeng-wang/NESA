package com.esotericsoftware.reflectasm;

import java.lang.reflect.Modifier;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class ConstructorAccess<T> {
    boolean isNonStaticMemberClass;

    public abstract T newInstance();

    public abstract T newInstance(Object obj);

    public boolean isNonStaticMemberClass() {
        return this.isNonStaticMemberClass;
    }

    public static <T> ConstructorAccess<T> get(Class<T> cls) {
        String str;
        Class loadClass;
        Class enclosingClass = cls.getEnclosingClass();
        boolean z = (enclosingClass == null || !cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) ? false : true;
        String name = cls.getName();
        String str2 = name + "ConstructorAccess";
        if (str2.startsWith("java.")) {
            str = "reflectasm." + str2;
        } else {
            str = str2;
        }
        AccessClassLoader accessClassLoader = AccessClassLoader.get(cls);
        synchronized (accessClassLoader) {
            try {
                loadClass = accessClassLoader.loadClass(str);
            } catch (Exception e) {
                throw new RuntimeException("Non-static member class cannot be created (missing enclosing class constructor): " + cls.getName());
            } catch (Exception e2) {
                throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + cls.getName());
            } catch (ClassNotFoundException e3) {
                String str3;
                String replace = str.replace('.', '/');
                String replace2 = name.replace('.', '/');
                if (z) {
                    str2 = enclosingClass.getName().replace('.', '/');
                    cls.getConstructor(new Class[]{enclosingClass});
                    str3 = str2;
                } else {
                    cls.getConstructor((Class[]) null);
                    str3 = null;
                }
                ClassWriter classWriter = new ClassWriter(0);
                classWriter.visit(Opcodes.V1_1, 33, replace, null, "com/esotericsoftware/reflectasm/ConstructorAccess", null);
                insertConstructor(classWriter);
                insertNewInstance(classWriter, replace2);
                insertNewInstanceInner(classWriter, replace2, str3);
                classWriter.visitEnd();
                loadClass = accessClassLoader.defineClass(str, classWriter.toByteArray());
            }
        }
        try {
            ConstructorAccess constructorAccess = (ConstructorAccess) loadClass.newInstance();
            constructorAccess.isNonStaticMemberClass = z;
            return constructorAccess;
        } catch (Exception e4) {
            throw new RuntimeException("Error constructing constructor access class: " + str, e4);
        }
    }

    private static void insertConstructor(ClassWriter classWriter) {
        MethodVisitor visitMethod = classWriter.visitMethod(1, "<init>", "()V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(25, 0);
        visitMethod.visitMethodInsn(183, "com/esotericsoftware/reflectasm/ConstructorAccess", "<init>", "()V");
        visitMethod.visitInsn(177);
        visitMethod.visitMaxs(1, 1);
        visitMethod.visitEnd();
    }

    static void insertNewInstance(ClassWriter classWriter, String str) {
        MethodVisitor visitMethod = classWriter.visitMethod(1, "newInstance", "()Ljava/lang/Object;", null, null);
        visitMethod.visitCode();
        visitMethod.visitTypeInsn(187, str);
        visitMethod.visitInsn(89);
        visitMethod.visitMethodInsn(183, str, "<init>", "()V");
        visitMethod.visitInsn(176);
        visitMethod.visitMaxs(2, 1);
        visitMethod.visitEnd();
    }

    static void insertNewInstanceInner(ClassWriter classWriter, String str, String str2) {
        MethodVisitor visitMethod = classWriter.visitMethod(1, "newInstance", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        visitMethod.visitCode();
        if (str2 != null) {
            visitMethod.visitTypeInsn(187, str);
            visitMethod.visitInsn(89);
            visitMethod.visitVarInsn(25, 1);
            visitMethod.visitTypeInsn(192, str2);
            visitMethod.visitInsn(89);
            visitMethod.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            visitMethod.visitInsn(87);
            visitMethod.visitMethodInsn(183, str, "<init>", "(L" + str2 + ";)V");
            visitMethod.visitInsn(176);
            visitMethod.visitMaxs(4, 2);
        } else {
            visitMethod.visitTypeInsn(187, "java/lang/UnsupportedOperationException");
            visitMethod.visitInsn(89);
            visitMethod.visitLdcInsn("Not an inner class.");
            visitMethod.visitMethodInsn(183, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
            visitMethod.visitInsn(191);
            visitMethod.visitMaxs(3, 2);
        }
        visitMethod.visitEnd();
    }
}
