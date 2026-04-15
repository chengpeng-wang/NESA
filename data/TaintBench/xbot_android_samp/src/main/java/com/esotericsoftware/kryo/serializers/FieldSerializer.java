package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.NotNull;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.IntArray;
import com.esotericsoftware.kryo.util.ObjectMap;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.reflectasm.FieldAccess;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FieldSerializer<T> extends Serializer<T> implements Comparator<CachedField> {
    Object access;
    private CachedField[] fields = new CachedField[0];
    private boolean fieldsCanBeNull = true;
    private boolean fixedFieldTypes;
    private boolean ignoreSyntheticFields = true;
    final Kryo kryo;
    private boolean setFieldsAsAccessible = true;
    final Class type;

    public abstract class CachedField<X> {
        int accessIndex = -1;
        boolean canBeNull;
        Field field;
        Serializer serializer;
        Class valueClass;

        public abstract void copy(Object obj, Object obj2);

        public abstract void read(Input input, Object obj);

        public abstract void write(Output output, Object obj);

        public void setClass(Class cls) {
            this.valueClass = cls;
            this.serializer = null;
        }

        public void setClass(Class cls, Serializer serializer) {
            this.valueClass = cls;
            this.serializer = serializer;
        }

        public void setSerializer(Serializer serializer) {
            this.serializer = serializer;
        }

        public void setCanBeNull(boolean z) {
            this.canBeNull = z;
        }

        public Field getField() {
            return this.field;
        }

        public String toString() {
            return this.field.getName();
        }
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Optional {
        String value();
    }

    abstract class AsmCachedField extends CachedField {
        FieldAccess access = ((FieldAccess) FieldSerializer.this.access);

        AsmCachedField() {
            super();
        }
    }

    class ObjectField extends CachedField {
        Class[] generics;

        ObjectField() {
            super();
        }

        public void write(Output output, Object obj) {
            try {
                Object obj2;
                if (this.accessIndex != -1) {
                    obj2 = ((FieldAccess) FieldSerializer.this.access).get(obj, this.accessIndex);
                } else {
                    obj2 = this.field.get(obj);
                }
                Serializer serializer = this.serializer;
                if (this.valueClass != null) {
                    if (serializer == null) {
                        serializer = FieldSerializer.this.kryo.getSerializer(this.valueClass);
                        this.serializer = serializer;
                    }
                    if (this.generics != null) {
                        serializer.setGenerics(FieldSerializer.this.kryo, this.generics);
                    }
                    if (this.canBeNull) {
                        FieldSerializer.this.kryo.writeObjectOrNull(output, obj2, serializer);
                    } else if (obj2 == null) {
                        throw new KryoException("Field value is null but canBeNull is false: " + this + " (" + obj.getClass().getName() + ")");
                    } else {
                        FieldSerializer.this.kryo.writeObject(output, obj2, serializer);
                    }
                } else if (obj2 == null) {
                    FieldSerializer.this.kryo.writeClass(output, null);
                } else {
                    Registration writeClass = FieldSerializer.this.kryo.writeClass(output, obj2.getClass());
                    if (serializer == null) {
                        serializer = writeClass.getSerializer();
                    }
                    if (this.generics != null) {
                        serializer.setGenerics(FieldSerializer.this.kryo, this.generics);
                    }
                    FieldSerializer.this.kryo.writeObject(output, obj2, serializer);
                }
            } catch (IllegalAccessException e) {
                throw new KryoException("Error accessing field: " + this + " (" + obj.getClass().getName() + ")", e);
            } catch (KryoException e2) {
                e2.addTrace(this + " (" + obj.getClass().getName() + ")");
                throw e2;
            } catch (RuntimeException e3) {
                KryoException kryoException = new KryoException(e3);
                kryoException.addTrace(this + " (" + obj.getClass().getName() + ")");
                throw kryoException;
            }
        }

        public void read(Input input, Object obj) {
            try {
                Object obj2;
                Class cls = this.valueClass;
                Serializer serializer = this.serializer;
                if (cls == null) {
                    Object obj3;
                    Registration readClass = FieldSerializer.this.kryo.readClass(input);
                    if (readClass == null) {
                        obj3 = null;
                    } else {
                        if (serializer == null) {
                            serializer = readClass.getSerializer();
                        }
                        if (this.generics != null) {
                            serializer.setGenerics(FieldSerializer.this.kryo, this.generics);
                        }
                        obj3 = FieldSerializer.this.kryo.readObject(input, readClass.getType(), serializer);
                    }
                    obj2 = obj3;
                } else {
                    if (serializer == null) {
                        serializer = FieldSerializer.this.kryo.getSerializer(this.valueClass);
                        this.serializer = serializer;
                    }
                    if (this.generics != null) {
                        serializer.setGenerics(FieldSerializer.this.kryo, this.generics);
                    }
                    if (this.canBeNull) {
                        obj2 = FieldSerializer.this.kryo.readObjectOrNull(input, cls, serializer);
                    } else {
                        obj2 = FieldSerializer.this.kryo.readObject(input, cls, serializer);
                    }
                }
                if (this.accessIndex != -1) {
                    ((FieldAccess) FieldSerializer.this.access).set(obj, this.accessIndex, obj2);
                } else {
                    this.field.set(obj, obj2);
                }
            } catch (IllegalAccessException e) {
                throw new KryoException("Error accessing field: " + this + " (" + FieldSerializer.this.type.getName() + ")", e);
            } catch (KryoException e2) {
                e2.addTrace(this + " (" + FieldSerializer.this.type.getName() + ")");
                throw e2;
            } catch (RuntimeException e3) {
                KryoException kryoException = new KryoException(e3);
                kryoException.addTrace(this + " (" + FieldSerializer.this.type.getName() + ")");
                throw kryoException;
            }
        }

        public void copy(Object obj, Object obj2) {
            try {
                if (this.accessIndex != -1) {
                    FieldAccess fieldAccess = (FieldAccess) FieldSerializer.this.access;
                    fieldAccess.set(obj2, this.accessIndex, FieldSerializer.this.kryo.copy(fieldAccess.get(obj, this.accessIndex)));
                    return;
                }
                this.field.set(obj2, FieldSerializer.this.kryo.copy(this.field.get(obj)));
            } catch (IllegalAccessException e) {
                throw new KryoException("Error accessing field: " + this + " (" + FieldSerializer.this.type.getName() + ")", e);
            } catch (KryoException e2) {
                e2.addTrace(this + " (" + FieldSerializer.this.type.getName() + ")");
                throw e2;
            } catch (RuntimeException e3) {
                KryoException kryoException = new KryoException(e3);
                kryoException.addTrace(this + " (" + FieldSerializer.this.type.getName() + ")");
                throw kryoException;
            }
        }
    }

    class BooleanField extends AsmCachedField {
        BooleanField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeBoolean(this.access.getBoolean(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setBoolean(obj, this.accessIndex, input.readBoolean());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setBoolean(obj2, this.accessIndex, this.access.getBoolean(obj, this.accessIndex));
        }
    }

    class ByteField extends AsmCachedField {
        ByteField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeByte(this.access.getByte(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setByte(obj, this.accessIndex, input.readByte());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setByte(obj2, this.accessIndex, this.access.getByte(obj, this.accessIndex));
        }
    }

    class CharField extends AsmCachedField {
        CharField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeChar(this.access.getChar(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setChar(obj, this.accessIndex, input.readChar());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setChar(obj2, this.accessIndex, this.access.getChar(obj, this.accessIndex));
        }
    }

    class DoubleField extends AsmCachedField {
        DoubleField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeDouble(this.access.getDouble(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setDouble(obj, this.accessIndex, input.readDouble());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setDouble(obj2, this.accessIndex, this.access.getDouble(obj, this.accessIndex));
        }
    }

    class FloatField extends AsmCachedField {
        FloatField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeFloat(this.access.getFloat(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setFloat(obj, this.accessIndex, input.readFloat());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setFloat(obj2, this.accessIndex, this.access.getFloat(obj, this.accessIndex));
        }
    }

    class IntField extends AsmCachedField {
        IntField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeInt(this.access.getInt(obj, this.accessIndex), false);
        }

        public void read(Input input, Object obj) {
            this.access.setInt(obj, this.accessIndex, input.readInt(false));
        }

        public void copy(Object obj, Object obj2) {
            this.access.setInt(obj2, this.accessIndex, this.access.getInt(obj, this.accessIndex));
        }
    }

    class LongField extends AsmCachedField {
        LongField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeLong(this.access.getLong(obj, this.accessIndex), false);
        }

        public void read(Input input, Object obj) {
            this.access.setLong(obj, this.accessIndex, input.readLong(false));
        }

        public void copy(Object obj, Object obj2) {
            this.access.setLong(obj2, this.accessIndex, this.access.getLong(obj, this.accessIndex));
        }
    }

    class ShortField extends AsmCachedField {
        ShortField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeShort(this.access.getShort(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.setShort(obj, this.accessIndex, input.readShort());
        }

        public void copy(Object obj, Object obj2) {
            this.access.setShort(obj2, this.accessIndex, this.access.getShort(obj, this.accessIndex));
        }
    }

    class StringField extends AsmCachedField {
        StringField() {
            super();
        }

        public void write(Output output, Object obj) {
            output.writeString(this.access.getString(obj, this.accessIndex));
        }

        public void read(Input input, Object obj) {
            this.access.set(obj, this.accessIndex, input.readString());
        }

        public void copy(Object obj, Object obj2) {
            this.access.set(obj2, this.accessIndex, this.access.getString(obj, this.accessIndex));
        }
    }

    public FieldSerializer(Kryo kryo, Class cls) {
        this.kryo = kryo;
        this.type = cls;
        rebuildCachedFields();
    }

    private void rebuildCachedFields() {
        int i = 0;
        if (this.type.isInterface()) {
            this.fields = new CachedField[0];
            return;
        }
        Field field;
        ArrayList arrayList = new ArrayList();
        for (Class cls = this.type; cls != Object.class; cls = cls.getSuperclass()) {
            Collections.addAll(arrayList, cls.getDeclaredFields());
        }
        ObjectMap context = this.kryo.getContext();
        IntArray intArray = new IntArray();
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            field = (Field) arrayList.get(i2);
            int modifiers = field.getModifiers();
            if (!(Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers) || (field.isSynthetic() && this.ignoreSyntheticFields))) {
                if (!field.isAccessible()) {
                    if (this.setFieldsAsAccessible) {
                        try {
                            field.setAccessible(true);
                        } catch (AccessControlException e) {
                        }
                    }
                }
                Optional optional = (Optional) field.getAnnotation(Optional.class);
                if (optional == null || context.containsKey(optional.value())) {
                    arrayList2.add(field);
                    int i3 = (!Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers) && Modifier.isPublic(field.getType().getModifiers())) ? 1 : 0;
                    intArray.add(i3);
                }
            }
        }
        if (!(Util.isAndroid || !Modifier.isPublic(this.type.getModifiers()) || intArray.indexOf(1) == -1)) {
            try {
                this.access = FieldAccess.get(this.type);
            } catch (RuntimeException e2) {
            }
        }
        ArrayList arrayList3 = new ArrayList(arrayList2.size());
        int size2 = arrayList2.size();
        while (i < size2) {
            int i4;
            field = (Field) arrayList2.get(i);
            if (this.access == null || intArray.get(i) != 1) {
                i4 = -1;
            } else {
                i4 = ((FieldAccess) this.access).getIndex(field.getName());
            }
            arrayList3.add(newCachedField(field, arrayList3.size(), i4));
            i++;
        }
        Collections.sort(arrayList3, this);
        this.fields = (CachedField[]) arrayList3.toArray(new CachedField[arrayList3.size()]);
        initializeCachedFields();
    }

    /* access modifiers changed from: protected */
    public void initializeCachedFields() {
    }

    private CachedField newCachedField(Field field, int i, int i2) {
        CachedField objectField;
        Class type = field.getType();
        if (i2 == -1) {
            objectField = new ObjectField();
            ((ObjectField) objectField).generics = Kryo.getGenerics(field.getGenericType());
        } else if (type.isPrimitive()) {
            if (type == Boolean.TYPE) {
                objectField = new BooleanField();
            } else if (type == Byte.TYPE) {
                objectField = new ByteField();
            } else if (type == Character.TYPE) {
                objectField = new CharField();
            } else if (type == Short.TYPE) {
                objectField = new ShortField();
            } else if (type == Integer.TYPE) {
                objectField = new IntField();
            } else if (type == Long.TYPE) {
                objectField = new LongField();
            } else if (type == Float.TYPE) {
                objectField = new FloatField();
            } else if (type == Double.TYPE) {
                objectField = new DoubleField();
            } else {
                objectField = new ObjectField();
            }
        } else if (type != String.class || (this.kryo.getReferences() && this.kryo.getReferenceResolver().useReferences(String.class))) {
            objectField = new ObjectField();
        } else {
            objectField = new StringField();
        }
        objectField.field = field;
        objectField.accessIndex = i2;
        boolean z = (!this.fieldsCanBeNull || type.isPrimitive() || field.isAnnotationPresent(NotNull.class)) ? false : true;
        objectField.canBeNull = z;
        if (this.kryo.isFinal(type) || this.fixedFieldTypes) {
            objectField.valueClass = type;
        }
        return objectField;
    }

    public int compare(CachedField cachedField, CachedField cachedField2) {
        return cachedField.field.getName().compareTo(cachedField2.field.getName());
    }

    public void setFieldsCanBeNull(boolean z) {
        this.fieldsCanBeNull = z;
        rebuildCachedFields();
    }

    public void setFieldsAsAccessible(boolean z) {
        this.setFieldsAsAccessible = z;
        rebuildCachedFields();
    }

    public void setIgnoreSyntheticFields(boolean z) {
        this.ignoreSyntheticFields = z;
        rebuildCachedFields();
    }

    public void setFixedFieldTypes(boolean z) {
        this.fixedFieldTypes = z;
        rebuildCachedFields();
    }

    public void write(Kryo kryo, Output output, T t) {
        for (CachedField write : this.fields) {
            write.write(output, t);
        }
    }

    public T read(Kryo kryo, Input input, Class<T> cls) {
        Object create = create(kryo, input, cls);
        kryo.reference(create);
        for (CachedField read : this.fields) {
            read.read(input, create);
        }
        return create;
    }

    /* access modifiers changed from: protected */
    public T create(Kryo kryo, Input input, Class<T> cls) {
        return kryo.newInstance(cls);
    }

    public CachedField getField(String str) {
        for (CachedField cachedField : this.fields) {
            if (cachedField.field.getName().equals(str)) {
                return cachedField;
            }
        }
        throw new IllegalArgumentException("Field \"" + str + "\" not found on class: " + this.type.getName());
    }

    public void removeField(String str) {
        for (int i = 0; i < this.fields.length; i++) {
            if (this.fields[i].field.getName().equals(str)) {
                CachedField[] cachedFieldArr = new CachedField[(this.fields.length - 1)];
                System.arraycopy(this.fields, 0, cachedFieldArr, 0, i);
                System.arraycopy(this.fields, i + 1, cachedFieldArr, i, cachedFieldArr.length - i);
                this.fields = cachedFieldArr;
                return;
            }
        }
        throw new IllegalArgumentException("Field \"" + str + "\" not found on class: " + this.type.getName());
    }

    public CachedField[] getFields() {
        return this.fields;
    }

    public Class getType() {
        return this.type;
    }

    /* access modifiers changed from: protected */
    public T createCopy(Kryo kryo, T t) {
        return kryo.newInstance(t.getClass());
    }

    public T copy(Kryo kryo, T t) {
        Object createCopy = createCopy(kryo, t);
        kryo.reference(createCopy);
        for (CachedField copy : this.fields) {
            copy.copy(t, createCopy);
        }
        return createCopy;
    }
}
