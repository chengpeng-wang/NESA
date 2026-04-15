package org.objenesis.instantiator.gcj;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.SerializationInstantiatorHelper;

public class GCJSerializationInstantiator extends GCJInstantiatorBase {
    private Class superType;

    public GCJSerializationInstantiator(Class type) {
        super(type);
        this.superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
    }

    public Object newInstance() {
        try {
            return newObjectMethod.invoke(dummyStream, new Object[]{this.type, this.superType});
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
