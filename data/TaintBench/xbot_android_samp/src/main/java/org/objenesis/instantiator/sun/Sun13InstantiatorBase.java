package org.objenesis.instantiator.sun;

import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public abstract class Sun13InstantiatorBase implements ObjectInstantiator {
    protected static Method allocateNewObjectMethod = null;
    static Class class$java$io$ObjectInputStream;
    static Class class$java$lang$Class;
    protected final Class type;

    public abstract Object newInstance();

    private static void initialize() {
        if (allocateNewObjectMethod == null) {
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
                String str = "allocateNewObject";
                Class[] clsArr = new Class[2];
                if (class$java$lang$Class == null) {
                    class$ = class$("java.lang.Class");
                    class$java$lang$Class = class$;
                } else {
                    class$ = class$java$lang$Class;
                }
                clsArr[0] = class$;
                if (class$java$lang$Class == null) {
                    class$ = class$("java.lang.Class");
                    class$java$lang$Class = class$;
                } else {
                    class$ = class$java$lang$Class;
                }
                clsArr[1] = class$;
                allocateNewObjectMethod = cls.getDeclaredMethod(str, clsArr);
                allocateNewObjectMethod.setAccessible(true);
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

    public Sun13InstantiatorBase(Class type) {
        this.type = type;
        initialize();
    }
}
