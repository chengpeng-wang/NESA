package com.esotericsoftware.reflectasm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class FieldAccess {
    private String[] fieldNames;

    public abstract Object get(Object obj, int i);

    public abstract boolean getBoolean(Object obj, int i);

    public abstract byte getByte(Object obj, int i);

    public abstract char getChar(Object obj, int i);

    public abstract double getDouble(Object obj, int i);

    public abstract float getFloat(Object obj, int i);

    public abstract int getInt(Object obj, int i);

    public abstract long getLong(Object obj, int i);

    public abstract short getShort(Object obj, int i);

    public abstract String getString(Object obj, int i);

    public abstract void set(Object obj, int i, Object obj2);

    public abstract void setBoolean(Object obj, int i, boolean z);

    public abstract void setByte(Object obj, int i, byte b);

    public abstract void setChar(Object obj, int i, char c);

    public abstract void setDouble(Object obj, int i, double d);

    public abstract void setFloat(Object obj, int i, float f);

    public abstract void setInt(Object obj, int i, int i2);

    public abstract void setLong(Object obj, int i, long j);

    public abstract void setShort(Object obj, int i, short s);

    public int getIndex(String str) {
        int length = this.fieldNames.length;
        for (int i = 0; i < length; i++) {
            if (this.fieldNames[i].equals(str)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unable to find public field: " + str);
    }

    public void set(Object obj, String str, Object obj2) {
        set(obj, getIndex(str), obj2);
    }

    public Object get(Object obj, String str) {
        return get(obj, getIndex(str));
    }

    public String[] getFieldNames() {
        return this.fieldNames;
    }

    public static FieldAccess get(Class cls) {
        Class cls2;
        String str;
        int i = 0;
        ArrayList arrayList = new ArrayList();
        for (cls2 = cls; cls2 != Object.class; cls2 = cls2.getSuperclass()) {
            for (Field field : cls2.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!(Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers))) {
                    arrayList.add(field);
                }
            }
        }
        String[] strArr = new String[arrayList.size()];
        int length = strArr.length;
        while (i < length) {
            strArr[i] = ((Field) arrayList.get(i)).getName();
            i++;
        }
        String name = cls.getName();
        String str2 = name + "FieldAccess";
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
                ClassWriter classWriter = new ClassWriter(0);
                classWriter.visit(Opcodes.V1_1, 33, replace, null, "com/esotericsoftware/reflectasm/FieldAccess", null);
                insertConstructor(classWriter);
                insertGetObject(classWriter, replace2, arrayList);
                insertSetObject(classWriter, replace2, arrayList);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.BOOLEAN_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.BOOLEAN_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.BYTE_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.BYTE_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.SHORT_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.SHORT_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.INT_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.INT_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.LONG_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.LONG_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.DOUBLE_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.DOUBLE_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.FLOAT_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.FLOAT_TYPE);
                insertGetPrimitive(classWriter, replace2, arrayList, Type.CHAR_TYPE);
                insertSetPrimitive(classWriter, replace2, arrayList, Type.CHAR_TYPE);
                insertGetString(classWriter, replace2, arrayList);
                classWriter.visitEnd();
                cls2 = accessClassLoader.defineClass(str, classWriter.toByteArray());
            }
        }
        try {
            FieldAccess fieldAccess = (FieldAccess) cls2.newInstance();
            fieldAccess.fieldNames = strArr;
            return fieldAccess;
        } catch (Exception e2) {
            throw new RuntimeException("Error constructing field access class: " + str, e2);
        }
    }

    private static void insertConstructor(ClassWriter classWriter) {
        MethodVisitor visitMethod = classWriter.visitMethod(1, "<init>", "()V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(25, 0);
        visitMethod.visitMethodInsn(183, "com/esotericsoftware/reflectasm/FieldAccess", "<init>", "()V");
        visitMethod.visitInsn(177);
        visitMethod.visitMaxs(1, 1);
        visitMethod.visitEnd();
    }

    private static void insertSetObject(ClassWriter classWriter, String str, ArrayList<Field> arrayList) {
        int i;
        MethodVisitor visitMethod = classWriter.visitMethod(1, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(21, 2);
        if (arrayList.isEmpty()) {
            i = 6;
        } else {
            Label[] labelArr = new Label[arrayList.size()];
            int length = labelArr.length;
            for (i = 0; i < length; i++) {
                labelArr[i] = new Label();
            }
            Label label = new Label();
            visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label, labelArr);
            int length2 = labelArr.length;
            for (int i2 = 0; i2 < length2; i2++) {
                Field field = (Field) arrayList.get(i2);
                Type type = Type.getType(field.getType());
                visitMethod.visitLabel(labelArr[i2]);
                visitMethod.visitFrame(3, 0, null, 0, null);
                visitMethod.visitVarInsn(25, 1);
                visitMethod.visitTypeInsn(192, str);
                visitMethod.visitVarInsn(25, 3);
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
                visitMethod.visitFieldInsn(181, str, field.getName(), type.getDescriptor());
                visitMethod.visitInsn(177);
            }
            visitMethod.visitLabel(label);
            visitMethod.visitFrame(3, 0, null, 0, null);
            i = 5;
        }
        visitMethod = insertThrowExceptionForFieldNotFound(visitMethod);
        visitMethod.visitMaxs(i, 4);
        visitMethod.visitEnd();
    }

    private static void insertGetObject(ClassWriter classWriter, String str, ArrayList<Field> arrayList) {
        int i;
        MethodVisitor visitMethod = classWriter.visitMethod(1, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(21, 2);
        if (arrayList.isEmpty()) {
            i = 6;
        } else {
            Label[] labelArr = new Label[arrayList.size()];
            int length = labelArr.length;
            for (i = 0; i < length; i++) {
                labelArr[i] = new Label();
            }
            Label label = new Label();
            visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label, labelArr);
            int length2 = labelArr.length;
            for (int i2 = 0; i2 < length2; i2++) {
                Field field = (Field) arrayList.get(i2);
                visitMethod.visitLabel(labelArr[i2]);
                visitMethod.visitFrame(3, 0, null, 0, null);
                visitMethod.visitVarInsn(25, 1);
                visitMethod.visitTypeInsn(192, str);
                visitMethod.visitFieldInsn(180, str, field.getName(), Type.getDescriptor(field.getType()));
                switch (Type.getType(field.getType()).getSort()) {
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
            i = 5;
        }
        insertThrowExceptionForFieldNotFound(visitMethod);
        visitMethod.visitMaxs(i, 3);
        visitMethod.visitEnd();
    }

    private static void insertGetString(ClassWriter classWriter, String str, ArrayList<Field> arrayList) {
        int i;
        MethodVisitor visitMethod = classWriter.visitMethod(1, "getString", "(Ljava/lang/Object;I)Ljava/lang/String;", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(21, 2);
        if (arrayList.isEmpty()) {
            i = 6;
        } else {
            Label[] labelArr = new Label[arrayList.size()];
            Label label = new Label();
            Object obj = null;
            int length = labelArr.length;
            int i2 = 0;
            while (i2 < length) {
                Object obj2;
                if (((Field) arrayList.get(i2)).getType().equals(String.class)) {
                    labelArr[i2] = new Label();
                    obj2 = obj;
                } else {
                    labelArr[i2] = label;
                    obj2 = 1;
                }
                i2++;
                obj = obj2;
            }
            Label label2 = new Label();
            visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label2, labelArr);
            int length2 = labelArr.length;
            for (int i3 = 0; i3 < length2; i3++) {
                if (!labelArr[i3].equals(label)) {
                    visitMethod.visitLabel(labelArr[i3]);
                    visitMethod.visitFrame(3, 0, null, 0, null);
                    visitMethod.visitVarInsn(25, 1);
                    visitMethod.visitTypeInsn(192, str);
                    visitMethod.visitFieldInsn(180, str, ((Field) arrayList.get(i3)).getName(), "Ljava/lang/String;");
                    visitMethod.visitInsn(176);
                }
            }
            if (obj != null) {
                visitMethod.visitLabel(label);
                visitMethod.visitFrame(3, 0, null, 0, null);
                insertThrowExceptionForFieldType(visitMethod, "String");
            }
            visitMethod.visitLabel(label2);
            visitMethod.visitFrame(3, 0, null, 0, null);
            i = 5;
        }
        insertThrowExceptionForFieldNotFound(visitMethod);
        visitMethod.visitMaxs(i, 3);
        visitMethod.visitEnd();
    }

    private static void insertSetPrimitive(ClassWriter classWriter, String str, ArrayList<Field> arrayList, Type type) {
        String str2;
        int i;
        int i2;
        int i3;
        String descriptor = type.getDescriptor();
        switch (type.getSort()) {
            case 1:
                str2 = "setBoolean";
                i = 21;
                i2 = 4;
                break;
            case 2:
                str2 = "setChar";
                i = 21;
                i2 = 4;
                break;
            case 3:
                str2 = "setByte";
                i = 21;
                i2 = 4;
                break;
            case 4:
                str2 = "setShort";
                i = 21;
                i2 = 4;
                break;
            case 5:
                str2 = "setInt";
                i = 21;
                i2 = 4;
                break;
            case 6:
                str2 = "setFloat";
                i = 23;
                i2 = 4;
                break;
            case 7:
                str2 = "setLong";
                i = 22;
                i2 = 5;
                break;
            case 8:
                str2 = "setDouble";
                i = 24;
                i2 = 5;
                break;
            default:
                str2 = "set";
                i = 25;
                i2 = 4;
                break;
        }
        MethodVisitor visitMethod = classWriter.visitMethod(1, str2, "(Ljava/lang/Object;I" + descriptor + ")V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(21, 2);
        if (arrayList.isEmpty()) {
            i3 = 6;
        } else {
            Label[] labelArr = new Label[arrayList.size()];
            Label label = new Label();
            Object obj = null;
            int length = labelArr.length;
            int i4 = 0;
            while (i4 < length) {
                Object obj2;
                if (Type.getType(((Field) arrayList.get(i4)).getType()).equals(type)) {
                    labelArr[i4] = new Label();
                    obj2 = obj;
                } else {
                    labelArr[i4] = label;
                    obj2 = 1;
                }
                i4++;
                obj = obj2;
            }
            Label label2 = new Label();
            visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label2, labelArr);
            i3 = 0;
            int length2 = labelArr.length;
            while (true) {
                int i5 = i3;
                if (i5 < length2) {
                    if (!labelArr[i5].equals(label)) {
                        visitMethod.visitLabel(labelArr[i5]);
                        visitMethod.visitFrame(3, 0, null, 0, null);
                        visitMethod.visitVarInsn(25, 1);
                        visitMethod.visitTypeInsn(192, str);
                        visitMethod.visitVarInsn(i, 3);
                        visitMethod.visitFieldInsn(181, str, ((Field) arrayList.get(i5)).getName(), descriptor);
                        visitMethod.visitInsn(177);
                    }
                    i3 = i5 + 1;
                } else {
                    if (obj != null) {
                        visitMethod.visitLabel(label);
                        visitMethod.visitFrame(3, 0, null, 0, null);
                        insertThrowExceptionForFieldType(visitMethod, type.getClassName());
                    }
                    visitMethod.visitLabel(label2);
                    visitMethod.visitFrame(3, 0, null, 0, null);
                    i3 = 5;
                }
            }
        }
        visitMethod = insertThrowExceptionForFieldNotFound(visitMethod);
        visitMethod.visitMaxs(i3, i2);
        visitMethod.visitEnd();
    }

    private static void insertGetPrimitive(ClassWriter classWriter, String str, ArrayList<Field> arrayList, Type type) {
        String str2;
        int i;
        int i2;
        String descriptor = type.getDescriptor();
        switch (type.getSort()) {
            case 1:
                str2 = "getBoolean";
                i = 172;
                break;
            case 2:
                str2 = "getChar";
                i = 172;
                break;
            case 3:
                str2 = "getByte";
                i = 172;
                break;
            case 4:
                str2 = "getShort";
                i = 172;
                break;
            case 5:
                str2 = "getInt";
                i = 172;
                break;
            case 6:
                str2 = "getFloat";
                i = 174;
                break;
            case 7:
                str2 = "getLong";
                i = 173;
                break;
            case 8:
                str2 = "getDouble";
                i = 175;
                break;
            default:
                str2 = "get";
                i = 176;
                break;
        }
        MethodVisitor visitMethod = classWriter.visitMethod(1, str2, "(Ljava/lang/Object;I)" + descriptor, null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(21, 2);
        if (arrayList.isEmpty()) {
            i2 = 6;
        } else {
            Label[] labelArr = new Label[arrayList.size()];
            Label label = new Label();
            Object obj = null;
            int length = labelArr.length;
            int i3 = 0;
            while (i3 < length) {
                Object obj2;
                if (Type.getType(((Field) arrayList.get(i3)).getType()).equals(type)) {
                    labelArr[i3] = new Label();
                    obj2 = obj;
                } else {
                    labelArr[i3] = label;
                    obj2 = 1;
                }
                i3++;
                obj = obj2;
            }
            Label label2 = new Label();
            visitMethod.visitTableSwitchInsn(0, labelArr.length - 1, label2, labelArr);
            i2 = 0;
            int length2 = labelArr.length;
            while (true) {
                int i4 = i2;
                if (i4 < length2) {
                    Field field = (Field) arrayList.get(i4);
                    if (!labelArr[i4].equals(label)) {
                        visitMethod.visitLabel(labelArr[i4]);
                        visitMethod.visitFrame(3, 0, null, 0, null);
                        visitMethod.visitVarInsn(25, 1);
                        visitMethod.visitTypeInsn(192, str);
                        visitMethod.visitFieldInsn(180, str, field.getName(), descriptor);
                        visitMethod.visitInsn(i);
                    }
                    i2 = i4 + 1;
                } else {
                    if (obj != null) {
                        visitMethod.visitLabel(label);
                        visitMethod.visitFrame(3, 0, null, 0, null);
                        insertThrowExceptionForFieldType(visitMethod, type.getClassName());
                    }
                    visitMethod.visitLabel(label2);
                    visitMethod.visitFrame(3, 0, null, 0, null);
                    i2 = 5;
                }
            }
        }
        visitMethod = insertThrowExceptionForFieldNotFound(visitMethod);
        visitMethod.visitMaxs(i2, 3);
        visitMethod.visitEnd();
    }

    private static MethodVisitor insertThrowExceptionForFieldNotFound(MethodVisitor methodVisitor) {
        methodVisitor.visitTypeInsn(187, "java/lang/IllegalArgumentException");
        methodVisitor.visitInsn(89);
        methodVisitor.visitTypeInsn(187, "java/lang/StringBuilder");
        methodVisitor.visitInsn(89);
        methodVisitor.visitLdcInsn("Field not found: ");
        methodVisitor.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        methodVisitor.visitVarInsn(21, 2);
        methodVisitor.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        methodVisitor.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        methodVisitor.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        methodVisitor.visitInsn(191);
        return methodVisitor;
    }

    private static MethodVisitor insertThrowExceptionForFieldType(MethodVisitor methodVisitor, String str) {
        methodVisitor.visitTypeInsn(187, "java/lang/IllegalArgumentException");
        methodVisitor.visitInsn(89);
        methodVisitor.visitTypeInsn(187, "java/lang/StringBuilder");
        methodVisitor.visitInsn(89);
        methodVisitor.visitLdcInsn("Field not declared as " + str + ": ");
        methodVisitor.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        methodVisitor.visitVarInsn(21, 2);
        methodVisitor.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
        methodVisitor.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        methodVisitor.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
        methodVisitor.visitInsn(191);
        return methodVisitor;
    }
}
