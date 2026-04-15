package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionSerializer extends Serializer<Collection> {
    private Class elementClass;
    private boolean elementsCanBeNull = true;
    private Class genericType;
    private Serializer serializer;

    public CollectionSerializer(Class cls, Serializer serializer) {
        setElementClass(cls, serializer);
    }

    public CollectionSerializer(Class cls, Serializer serializer, boolean z) {
        setElementClass(cls, serializer);
        this.elementsCanBeNull = z;
    }

    public void setElementsCanBeNull(boolean z) {
        this.elementsCanBeNull = z;
    }

    public void setElementClass(Class cls, Serializer serializer) {
        this.elementClass = cls;
        this.serializer = serializer;
    }

    public void setGenerics(Kryo kryo, Class[] clsArr) {
        if (kryo.isFinal(clsArr[0])) {
            this.genericType = clsArr[0];
        }
    }

    public void write(Kryo kryo, Output output, Collection collection) {
        output.writeInt(collection.size(), true);
        Serializer serializer = this.serializer;
        if (this.genericType != null) {
            if (serializer == null) {
                serializer = kryo.getSerializer(this.genericType);
            }
            this.genericType = null;
        }
        if (serializer == null) {
            for (Object writeClassAndObject : collection) {
                kryo.writeClassAndObject(output, writeClassAndObject);
            }
        } else if (this.elementsCanBeNull) {
            for (Object writeObjectOrNull : collection) {
                kryo.writeObjectOrNull(output, writeObjectOrNull, serializer);
            }
        } else {
            for (Object writeObjectOrNull2 : collection) {
                kryo.writeObject(output, writeObjectOrNull2, serializer);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Collection create(Kryo kryo, Input input, Class<Collection> cls) {
        return (Collection) kryo.newInstance(cls);
    }

    public Collection read(Kryo kryo, Input input, Class<Collection> cls) {
        int i = 0;
        Collection create = create(kryo, input, cls);
        kryo.reference(create);
        int readInt = input.readInt(true);
        if (create instanceof ArrayList) {
            ((ArrayList) create).ensureCapacity(readInt);
        }
        Class cls2 = this.elementClass;
        Serializer serializer = this.serializer;
        if (this.genericType != null) {
            if (serializer == null) {
                cls2 = this.genericType;
                serializer = kryo.getSerializer(this.genericType);
            }
            this.genericType = null;
        }
        if (serializer == null) {
            for (int i2 = 0; i2 < readInt; i2++) {
                create.add(kryo.readClassAndObject(input));
            }
        } else if (this.elementsCanBeNull) {
            while (i < readInt) {
                create.add(kryo.readObjectOrNull(input, cls2, serializer));
                i++;
            }
        } else {
            while (i < readInt) {
                create.add(kryo.readObject(input, cls2, serializer));
                i++;
            }
        }
        return create;
    }

    /* access modifiers changed from: protected */
    public Collection createCopy(Kryo kryo, Collection collection) {
        return (Collection) kryo.newInstance(collection.getClass());
    }

    public Collection copy(Kryo kryo, Collection collection) {
        Collection createCopy = createCopy(kryo, collection);
        kryo.reference(createCopy);
        for (Object copy : collection) {
            createCopy.add(kryo.copy(copy));
        }
        return createCopy;
    }
}
