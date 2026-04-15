package org.objenesis.instantiator.perc;

import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class PercInstantiator implements ObjectInstantiator {
    static Class class$java$io$ObjectInputStream;
    static Class class$java$lang$Class;
    private final Method newInstanceMethod;
    private final Object[] typeArgs = new Object[]{null, Boolean.FALSE};

    public PercInstantiator(Class type) {
        this.typeArgs[0] = type;
        try {
            Class class$;
            Class cls;
            if (class$java$io$ObjectInputStream == null) {
                class$ = class$("java.io.ObjectInputStream");
                class$java$io$ObjectInputStream = class$;
                cls = class$;
            } else {
                cls = class$java$io$ObjectInputStream;
            }
            String str = "newInstance";
            Class[] clsArr = new Class[2];
            if (class$java$lang$Class == null) {
                class$ = class$("java.lang.Class");
                class$java$lang$Class = class$;
            } else {
                class$ = class$java$lang$Class;
            }
            clsArr[0] = class$;
            clsArr[1] = Boolean.TYPE;
            this.newInstanceMethod = cls.getDeclaredMethod(str, clsArr);
            this.newInstanceMethod.setAccessible(true);
        } catch (RuntimeException e) {
            throw new ObjenesisException(e);
        } catch (NoSuchMethodException e2) {
            throw new ObjenesisException(e2);
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
            return this.newInstanceMethod.invoke(null, this.typeArgs);
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
