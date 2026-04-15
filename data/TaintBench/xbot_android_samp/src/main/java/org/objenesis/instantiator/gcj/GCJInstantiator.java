package org.objenesis.instantiator.gcj;

import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;

public class GCJInstantiator extends GCJInstantiatorBase {
    static Class class$java$lang$Object;

    public GCJInstantiator(Class type) {
        super(type);
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
            Class class$;
            Method method = newObjectMethod;
            ObjectInputStream objectInputStream = dummyStream;
            Object[] objArr = new Object[2];
            objArr[0] = this.type;
            if (class$java$lang$Object == null) {
                class$ = class$("java.lang.Object");
                class$java$lang$Object = class$;
            } else {
                class$ = class$java$lang$Object;
            }
            objArr[1] = class$;
            return method.invoke(objectInputStream, objArr);
        } catch (RuntimeException e) {
            throw new ObjenesisException(e);
        } catch (IllegalAccessException e2) {
            throw new ObjenesisException(e2);
        } catch (InvocationTargetException e22) {
            throw new ObjenesisException(e22);
        }
    }
}
