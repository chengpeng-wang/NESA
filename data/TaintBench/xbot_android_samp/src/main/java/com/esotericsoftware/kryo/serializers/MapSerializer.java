package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.Map;
import java.util.Map.Entry;

public class MapSerializer extends Serializer<Map> {
    private Class keyClass;
    private Class keyGenericType;
    private Serializer keySerializer;
    private boolean keysCanBeNull = true;
    private Class valueClass;
    private Class valueGenericType;
    private Serializer valueSerializer;
    private boolean valuesCanBeNull = true;

    public void setKeysCanBeNull(boolean z) {
        this.keysCanBeNull = z;
    }

    public void setKeyClass(Class cls, Serializer serializer) {
        this.keyClass = cls;
        this.keySerializer = serializer;
    }

    public void setValueClass(Class cls, Serializer serializer) {
        this.valueClass = cls;
        this.valueSerializer = serializer;
    }

    public void setValuesCanBeNull(boolean z) {
        this.valuesCanBeNull = z;
    }

    public void setGenerics(Kryo kryo, Class[] clsArr) {
        if (clsArr[0] != null && kryo.isFinal(clsArr[0])) {
            this.keyGenericType = clsArr[0];
        }
        if (clsArr[1] != null && kryo.isFinal(clsArr[1])) {
            this.valueGenericType = clsArr[1];
        }
    }

    public void write(Kryo kryo, Output output, Map map) {
        output.writeInt(map.size(), true);
        Serializer serializer = this.keySerializer;
        if (this.keyGenericType != null) {
            if (serializer == null) {
                serializer = kryo.getSerializer(this.keyGenericType);
            }
            this.keyGenericType = null;
        }
        Serializer serializer2 = serializer;
        serializer = this.valueSerializer;
        if (this.valueGenericType != null) {
            if (serializer == null) {
                serializer = kryo.getSerializer(this.valueGenericType);
            }
            this.valueGenericType = null;
        }
        Serializer serializer3 = serializer;
        for (Entry entry : map.entrySet()) {
            if (serializer2 == null) {
                kryo.writeClassAndObject(output, entry.getKey());
            } else if (this.keysCanBeNull) {
                kryo.writeObjectOrNull(output, entry.getKey(), serializer2);
            } else {
                kryo.writeObject(output, entry.getKey(), serializer2);
            }
            if (serializer3 == null) {
                kryo.writeClassAndObject(output, entry.getValue());
            } else if (this.valuesCanBeNull) {
                kryo.writeObjectOrNull(output, entry.getValue(), serializer3);
            } else {
                kryo.writeObject(output, entry.getValue(), serializer3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Map create(Kryo kryo, Input input, Class<Map> cls) {
        return (Map) kryo.newInstance(cls);
    }

    public Map read(Kryo kryo, Input input, Class<Map> cls) {
        Map create = create(kryo, input, cls);
        int readInt = input.readInt(true);
        Class cls2 = this.keyClass;
        Class cls3 = this.valueClass;
        Serializer serializer = this.keySerializer;
        if (this.keyGenericType != null) {
            cls2 = this.keyGenericType;
            if (serializer == null) {
                serializer = kryo.getSerializer(cls2);
            }
            this.keyGenericType = null;
        }
        Serializer serializer2 = this.valueSerializer;
        if (this.valueGenericType != null) {
            cls3 = this.valueGenericType;
            if (serializer2 == null) {
                serializer2 = kryo.getSerializer(cls3);
            }
            this.valueGenericType = null;
        }
        kryo.reference(create);
        for (int i = 0; i < readInt; i++) {
            Object readClassAndObject;
            Object readClassAndObject2;
            if (serializer == null) {
                readClassAndObject = kryo.readClassAndObject(input);
            } else if (this.keysCanBeNull) {
                readClassAndObject = kryo.readObjectOrNull(input, cls2, serializer);
            } else {
                readClassAndObject = kryo.readObject(input, cls2, serializer);
            }
            if (serializer2 == null) {
                readClassAndObject2 = kryo.readClassAndObject(input);
            } else if (this.valuesCanBeNull) {
                readClassAndObject2 = kryo.readObjectOrNull(input, cls3, serializer2);
            } else {
                readClassAndObject2 = kryo.readObject(input, cls3, serializer2);
            }
            create.put(readClassAndObject, readClassAndObject2);
        }
        return create;
    }

    /* access modifiers changed from: protected */
    public Map createCopy(Kryo kryo, Map map) {
        return (Map) kryo.newInstance(map.getClass());
    }

    public Map copy(Kryo kryo, Map map) {
        Map createCopy = createCopy(kryo, map);
        for (Entry entry : map.entrySet()) {
            createCopy.put(kryo.copy(entry.getKey()), kryo.copy(entry.getValue()));
        }
        return createCopy;
    }
}
