package org.objenesis.instantiator.gcj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public abstract class GCJInstantiatorBase implements ObjectInstantiator {
    static Class class$java$io$ObjectInputStream;
    static Class class$java$lang$Class;
    protected static ObjectInputStream dummyStream;
    protected static Method newObjectMethod = null;
    protected final Class type;

    private static class DummyStream extends ObjectInputStream {
    }

    public abstract Object newInstance();

    private static void initialize() {
        if (newObjectMethod == null) {
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
                String str = "newObject";
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
                newObjectMethod = cls.getDeclaredMethod(str, clsArr);
                newObjectMethod.setAccessible(true);
                dummyStream = new DummyStream();
            } catch (RuntimeException e) {
                throw new ObjenesisException(e);
            } catch (NoSuchMethodException e2) {
                throw new ObjenesisException(e2);
            } catch (IOException e22) {
                throw new ObjenesisException(e22);
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

    public GCJInstantiatorBase(Class type) {
        this.type = type;
        initialize();
    }
}
