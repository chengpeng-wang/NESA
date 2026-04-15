package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer.CachedField;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class TaggedFieldSerializer<T> extends FieldSerializer<T> {
    private int[] tags;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Tag {
        int value();
    }

    public TaggedFieldSerializer(Kryo kryo, Class cls) {
        super(kryo, cls);
    }

    /* access modifiers changed from: protected */
    public void initializeCachedFields() {
        for (CachedField field : getFields()) {
            Field field2 = field.getField();
            Deprecated deprecated = (Deprecated) field2.getAnnotation(Deprecated.class);
            if (((Tag) field2.getAnnotation(Tag.class)) == null || deprecated != null) {
                super.removeField(field2.getName());
            }
        }
        CachedField[] fields = getFields();
        this.tags = new int[fields.length];
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            this.tags[i] = ((Tag) fields[i].getField().getAnnotation(Tag.class)).value();
        }
    }

    public void removeField(String str) {
        super.removeField(str);
        initializeCachedFields();
    }

    public void write(Kryo kryo, Output output, T t) {
        CachedField[] fields = getFields();
        output.writeInt(fields.length, true);
        int length = fields.length;
        for (int i = 0; i < length; i++) {
            output.writeInt(this.tags[i], true);
            fields[i].write(output, t);
        }
    }

    public T read(Kryo kryo, Input input, Class<T> cls) {
        Object newInstance = kryo.newInstance(cls);
        kryo.reference(newInstance);
        int readInt = input.readInt(true);
        int[] iArr = this.tags;
        CachedField[] fields = getFields();
        for (int i = 0; i < readInt; i++) {
            int readInt2 = input.readInt(true);
            CachedField cachedField = null;
            int length = iArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                if (iArr[i2] == readInt2) {
                    cachedField = fields[i2];
                    break;
                }
            }
            if (cachedField == null) {
                throw new KryoException("Unknown field tag: " + readInt2 + " (" + getType().getName() + ")");
            }
            cachedField.read(input, newInstance);
        }
        return newInstance;
    }
}
