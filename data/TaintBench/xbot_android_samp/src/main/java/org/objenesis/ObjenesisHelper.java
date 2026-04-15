package org.objenesis;

import java.io.Serializable;
import org.objenesis.instantiator.ObjectInstantiator;

public final class ObjenesisHelper {
    private static final Objenesis OBJENESIS_SERIALIZER = new ObjenesisSerializer();
    private static final Objenesis OBJENESIS_STD = new ObjenesisStd();

    private ObjenesisHelper() {
    }

    public static Object newInstance(Class clazz) {
        return OBJENESIS_STD.newInstance(clazz);
    }

    public static Serializable newSerializableInstance(Class clazz) {
        return (Serializable) OBJENESIS_SERIALIZER.newInstance(clazz);
    }

    public static ObjectInstantiator getInstantiatorOf(Class clazz) {
        return OBJENESIS_STD.getInstantiatorOf(clazz);
    }

    public static ObjectInstantiator getSerializableObjectInstantiatorOf(Class clazz) {
        return OBJENESIS_SERIALIZER.getInstantiatorOf(clazz);
    }
}
