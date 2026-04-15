package org.objenesis.instantiator.basic;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class ObjectStreamClassInstantiator implements ObjectInstantiator {
    static Class class$java$io$ObjectStreamClass;
    private static Method newInstanceMethod;
    private final ObjectStreamClass objStreamClass;

    private static void initialize() {
        if (newInstanceMethod == null) {
            try {
                Class class$;
                if (class$java$io$ObjectStreamClass == null) {
                    class$ = class$("java.io.ObjectStreamClass");
                    class$java$io$ObjectStreamClass = class$;
                } else {
                    class$ = class$java$io$ObjectStreamClass;
                }
                newInstanceMethod = class$.getDeclaredMethod("newInstance", new Class[0]);
                newInstanceMethod.setAccessible(true);
            } catch (RuntimeException e) {
                throw new ObjenesisException(e);
            } catch (NoSuchMethodException e2) {
                throw new ObjenesisException(e2);
            }
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public ObjectStreamClassInstantiator(Class type) {
        initialize();
        this.objStreamClass = ObjectStreamClass.lookup(type);
    }

    public Object newInstance() {
        try {
            return newInstanceMethod.invoke(this.objStreamClass, new Object[0]);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
