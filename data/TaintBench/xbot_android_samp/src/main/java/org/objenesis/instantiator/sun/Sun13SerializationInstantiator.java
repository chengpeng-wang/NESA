package org.objenesis.instantiator.sun;

import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.SerializationInstantiatorHelper;

public class Sun13SerializationInstantiator extends Sun13InstantiatorBase {
    private final Class superType;

    public Sun13SerializationInstantiator(Class type) {
        super(type);
        this.superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
    }

    public Object newInstance() {
        try {
            return allocateNewObjectMethod.invoke(null, new Object[]{this.type, this.superType});
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
