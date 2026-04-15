package org.objenesis.instantiator.jrockit;

import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class JRockitLegacyInstantiator implements ObjectInstantiator {
    static Class class$java$lang$Class;
    private static Method safeAllocObjectMethod = null;
    private final Class type;

    private static void initialize() {
        if (safeAllocObjectMethod == null) {
            try {
                Class class$;
                Class memSystem = Class.forName("jrockit.vm.MemSystem");
                String str = "safeAllocObject";
                Class[] clsArr = new Class[1];
                if (class$java$lang$Class == null) {
                    class$ = class$("java.lang.Class");
                    class$java$lang$Class = class$;
                } else {
                    class$ = class$java$lang$Class;
                }
                clsArr[0] = class$;
                safeAllocObjectMethod = memSystem.getDeclaredMethod(str, clsArr);
                safeAllocObjectMethod.setAccessible(true);
            } catch (RuntimeException e) {
                throw new ObjenesisException(e);
            } catch (ClassNotFoundException e2) {
                throw new ObjenesisException(e2);
            } catch (NoSuchMethodException e22) {
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

    public JRockitLegacyInstantiator(Class type) {
        initialize();
        this.type = type;
    }

    public Object newInstance() {
        try {
            return safeAllocObjectMethod.invoke(null, new Object[]{this.type});
        } catch (Exception e) {
            throw new ObjenesisException(e);
        }
    }
}
