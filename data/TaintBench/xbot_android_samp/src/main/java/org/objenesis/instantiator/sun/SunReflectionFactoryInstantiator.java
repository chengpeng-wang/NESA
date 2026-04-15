package org.objenesis.instantiator.sun;

import java.lang.reflect.Constructor;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import sun.reflect.ReflectionFactory;

public class SunReflectionFactoryInstantiator implements ObjectInstantiator {
    static Class class$java$lang$Object;
    private final Constructor mungedConstructor;

    public SunReflectionFactoryInstantiator(Class type) {
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        try {
            Class cls;
            if (class$java$lang$Object == null) {
                Class class$ = class$("java.lang.Object");
                class$java$lang$Object = class$;
                cls = class$;
            } else {
                cls = class$java$lang$Object;
            }
            this.mungedConstructor = reflectionFactory.newConstructorForSerialization(type, cls.getConstructor((Class[]) null));
            this.mungedConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new Error("Cannot find constructor for java.lang.Object!");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
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
