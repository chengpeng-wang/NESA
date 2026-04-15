package org.objenesis.instantiator.sun;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.SerializationInstantiatorHelper;
import sun.reflect.ReflectionFactory;

public class SunReflectionFactorySerializationInstantiator implements ObjectInstantiator {
    private final Constructor mungedConstructor;

    public SunReflectionFactorySerializationInstantiator(Class type) {
        try {
            this.mungedConstructor = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(type, SerializationInstantiatorHelper.getNonSerializableSuperClass(type).getConstructor((Class[]) null));
            this.mungedConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ObjenesisException(new NotSerializableException(new StringBuffer().append(type).append(" has no suitable superclass constructor").toString()));
        }
    }

    public Object newInstance() {
        try {
            return this.mungedConstructor.newInstance((Object[]) null);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
