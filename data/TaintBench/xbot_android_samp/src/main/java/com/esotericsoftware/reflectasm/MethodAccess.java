package com.esotericsoftware.reflectasm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class MethodAccess {
    private String[] methodNames;
    private Class[][] parameterTypes;

    public abstract Object invoke(Object obj, int i, Object... objArr);

    public Object invoke(Object obj, String str, Object... objArr) {
        return invoke(obj, getIndex(str), objArr);
    }

    public int getIndex(String str) {
        int length = this.methodNames.length;
        for (int i = 0; i < length; i++) {
            if (this.methodNames[i].equals(str)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find public method: " + str);
    }

    public int getIndex(String str, Class... clsArr) {
        int i = 0;
        int length = this.methodNames.length;
        while (i < length) {
            if (this.methodNames[i].equals(str) && Arrays.equals(clsArr, this.parameterTypes[i])) {
                return i;
            }
            i++;
        }
        throw new IllegalArgumentException("Unable to find public method: " + str + " " + Arrays.toString(this.parameterTypes));
    }

    public String[] getMethodNames() {
        return this.methodNames;
    }

    public Class[][] getParameterTypes() {
        return this.parameterTypes;
    }

    public static MethodAccess get(Class cls) {
        Class cls2;
        int i;
        int length;
        String str;
        ArrayList arrayList = new ArrayList();
        for (cls2 = cls; cls2 != Object.class; cls2 = cls2.getSuperclass()) {
            for (Method method : cls2.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (!(Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers))) {
                    arrayList.add(method);
                }
            }
        }
        Class[][] clsArr = new Class[arrayList.size()][];
        String[] strArr = new String[arrayList.size()];
        int length2 = strArr.length;
        for (i = 0; i < length2; i++) {
            Method method2 = (Method) arrayList.get(i);
            strArr[i] = method2.getName();
            clsArr[i] = method2.getParameterTypes();
        }
        String name = cls.getName();
        String str2 = name + "MethodAccess";
        if (str2.startsWith("java.")) {
            str = "reflectasm." + str2;
        } else {
            str = str2;
        }
        AccessClassLoader accessClassLoader = AccessClassLoader.get(cls);
        synchronized (accessClassLoader) {
            try {
                cls2 = accessClassLoader.loadClass(str);
            } catch (ClassNotFoundException e) {
                String replace = str.replace('.', '/');
                String replace2 = name.replace('.', '/');
                ClassWriter classWriter = new ClassWriter(1);
                classWriter.visit(Opcodes.V1_1, 33, replace, null, "com/esotericsoftware/reflectasm/MethodAccess", null);
                MethodVisitor visitMethod = classWriter.visitMethod(1, "<init>", "()V", null, null);
                visitMethod.visitCode();
                visitMethod.visitVarInsn(25, 0);
                visitMethod.visitMethodInsn(183, "com/esotericsoftware/reflectasm/MethodAccess", "<init>", "()V");
                visitMethod.visitInsn(177);
                visitMethod.visitMaxs(0, 0);
                visitMethod.visitEnd();
                visitMethod = classWriter.visitMethod(129, "invoke", "(Ljava/lang/Object;I[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
                visitMethod.visitCode();
                if (!arrayList.isEmpty()) {
                    visitMethod.visitVarInsn(25, 1);
                    visitMethod.visitTypeInsn(192, replace2);
                    visitMethod.visitVarInsn(58, 4);
                    visitMethod.visitVarInsn(21, 2);
                    Label[] labelArr = new Label[arrayList.size()];
                    length = labelArr.length;
                    for (length2 = 0; length2 < length; length2++) {
                        labelArr[length2] = new Label();
                    }
                    Label label = new Label();
                    visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label, labelArr);
                    StringBuilder stringBuilder = new StringBuilder(128);
                    int length3 = labelArr.length;
                    for (int i2 = 0; i2 < length3; i2++) {
                        visitMethod.visitLabel(labelArr[i2]);
                        if (i2 == 0) {
                            visitMethod.visitFrame(1, 1, new Object[]{replace2}, 0, null);
                        } else {
                            visitMethod.visitFrame(3, 0, null, 0, null);
                        }
                        visitMethod.visitVarInsn(25, 4);
                        stringBuilder.setLength(0);
                        stringBuilder.append('(');
                        Method method3 = (Method) arrayList.get(i2);
                        Class[] parameterTypes = method3.getParameterTypes();
                        for (length = 0; length < parameterTypes.length; length++) {
                            visitMethod.visitVarInsn(25, 3);
                            visitMethod.visitIntInsn(16, length);
                            visitMethod.visitInsn(50);
                            Type type = Type.getType(parameterTypes[length]);
                            switch (type.getSort()) {
                                case 1:
                                    visitMethod.visitTypeInsn(192, "java/lang/Boolean");
                                    visitMethod.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z");
                                    break;
                                case 2:
                                    visitMethod.visitTypeInsn(192, "java/lang/Character");
                                    visitMethod.visitMethodInsn(182, "java/lang/Character", "charValue", "()C");
                                    break;
                                case 3:
                                    visitMethod.visitTypeInsn(192, "java/lang/Byte");
                                    visitMethod.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B");
                                    break;
                                case 4:
                                    visitMethod.visitTypeInsn(192, "java/lang/Short");
                                    visitMethod.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S");
                                    break;
                                case 5:
                                    visitMethod.visitTypeInsn(192, "java/lang/Integer");
                                    visitMethod.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I");
                                    break;
                                case 6:
                                    visitMethod.visitTypeInsn(192, "java/lang/Float");
                                    visitMethod.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F");
                                    break;
                                case 7:
                                    visitMethod.visitTypeInsn(192, "java/lang/Long");
                                    visitMethod.visitMethodInsn(182, "java/lang/Long", "longValue", "()J");
                                    break;
                                case 8:
                                    visitMethod.visitTypeInsn(192, "java/lang/Double");
                                    visitMethod.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D");
                                    break;
                                case 9:
                                    visitMethod.visitTypeInsn(192, type.getDescriptor());
                                    break;
                                case 10:
                                    visitMethod.visitTypeInsn(192, type.getInternalName());
                                    break;
                                default:
                                    break;
                            }
                            stringBuilder.append(type.getDescriptor());
                        }
                        stringBuilder.append(')');
                        stringBuilder.append(Type.getDescriptor(method3.getReturnType()));
                        visitMethod.visitMethodInsn(182, replace2, method3.getName(), stringBuilder.toString());
                        switch (Type.getType(method3.getReturnType()).getSort()) {
                            case 0:
                                visitMethod.visitInsn(1);
                                break;
                            case 1:
                                visitMethod.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
                                break;
                            case 2:
                                visitMethod.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;");
                                break;
                            case 3:
                                visitMethod.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
                                break;
                            case 4:
                                visitMethod.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;");
                                break;
                            case 5:
                                visitMethod.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
                                break;
                            case 6:
                                visitMethod.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
                                break;
                            case 7:
                                visitMethod.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;");
                                break;
                            case 8:
                                visitMethod.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
                                break;
                            default:
                                break;
                        }
                        visitMethod.visitInsn(176);
                    }
                    visitMethod.visitLabel(label);
                    visitMethod.visitFrame(3, 0, null, 0, null);
                }
                visitMethod.visitTypeInsn(187, "java/lang/IllegalArgumentException");
                visitMethod.visitInsn(89);
                visitMethod.visitTypeInsn(187, "java/lang/StringBuilder");
                visitMethod.visitInsn(89);
                visitMethod.visitLdcInsn("Method not found: ");
                visitMethod.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                visitMethod.visitVarInsn(21, 2);
                visitMethod.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
                visitMethod.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                visitMethod.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
                visitMethod.visitInsn(191);
                visitMethod.visitMaxs(0, 0);
                visitMethod.visitEnd();
                classWriter.visitEnd();
                cls2 = accessClassLoader.defineClass(str, classWriter.toByteArray());
            }
        }
        try {
            MethodAccess methodAccess = (MethodAccess) cls2.newInstance();
            methodAccess.methodNames = strArr;
            methodAccess.parameterTypes = clsArr;
            return methodAccess;
        } catch (Exception e2) {
            throw new RuntimeException("Error constructing method access class: " + str, e2);
        }
    }
}
