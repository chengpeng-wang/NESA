package org.objenesis.strategy;

import java.io.NotSerializableException;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.basic.ObjectStreamClassInstantiator;
import org.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import org.objenesis.instantiator.perc.PercSerializationInstantiator;
import org.objenesis.instantiator.sun.Sun13SerializationInstantiator;

public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy {
    static Class class$java$io$Serializable;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public ObjectInstantiator newInstantiatorOf(Class type) {
        Class class$;
        if (class$java$io$Serializable == null) {
            class$ = class$("java.io.Serializable");
            class$java$io$Serializable = class$;
        } else {
            class$ = class$java$io$Serializable;
        }
        if (class$.isAssignableFrom(type)) {
            if (JVM_NAME.startsWith("Java HotSpot")) {
                if (VM_VERSION.startsWith("1.3")) {
                    return new Sun13SerializationInstantiator(type);
                }
            } else if (JVM_NAME.startsWith("GNU libgcj")) {
                return new GCJSerializationInstantiator(type);
            } else {
                if (JVM_NAME.startsWith("PERC")) {
                    return new PercSerializationInstantiator(type);
                }
            }
            return new ObjectStreamClassInstantiator(type);
        }
        throw new ObjenesisException(new NotSerializableException(new StringBuffer().append(type).append(" not serializable").toString()));
    }
}
