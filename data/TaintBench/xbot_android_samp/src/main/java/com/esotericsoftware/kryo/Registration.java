package com.esotericsoftware.kryo;

import com.esotericsoftware.kryo.util.Util;
import org.objenesis.instantiator.ObjectInstantiator;

public class Registration {
    private final int id;
    private ObjectInstantiator instantiator;
    private Serializer serializer;
    private final Class type;

    public Registration(Class cls, Serializer serializer, int i) {
        if (cls == null) {
            throw new IllegalArgumentException("type cannot be null.");
        } else if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        } else {
            this.type = cls;
            this.serializer = serializer;
            this.id = i;
        }
    }

    public Class getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public Serializer getSerializer() {
        return this.serializer;
    }

    public void setSerializer(Serializer serializer) {
        if (serializer == null) {
            throw new IllegalArgumentException("serializer cannot be null.");
        }
        this.serializer = serializer;
    }

    public ObjectInstantiator getInstantiator() {
        return this.instantiator;
    }

    public void setInstantiator(ObjectInstantiator objectInstantiator) {
        if (objectInstantiator == null) {
            throw new IllegalArgumentException("instantiator cannot be null.");
        }
        this.instantiator = objectInstantiator;
    }

    public String toString() {
        return "[" + this.id + ", " + Util.className(this.type) + "]";
    }
}
