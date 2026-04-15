package org.objenesis.instantiator.perc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class PercSerializationInstantiator implements ObjectInstantiator {
    static Class class$java$io$ObjectInputStream;
    static Class class$java$io$Serializable;
    static Class class$java$lang$Class;
    static Class class$java$lang$Object;
    static Class class$java$lang$String;
    private final Method newInstanceMethod;
    private Object[] typeArgs;

    public PercSerializationInstantiator(Class type) {
        Class class$;
        Class cls;
        Class unserializableType = type;
        while (true) {
            if (class$java$io$Serializable == null) {
                class$ = class$("java.io.Serializable");
                class$java$io$Serializable = class$;
            } else {
                class$ = class$java$io$Serializable;
            }
            if (class$.isAssignableFrom(unserializableType)) {
                unserializableType = unserializableType.getSuperclass();
            } else {
                try {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new ObjenesisException(e);
                } catch (NoSuchMethodException e2) {
                    throw new ObjenesisException(e2);
                } catch (InvocationTargetException e22) {
                    throw new ObjenesisException(e22);
                } catch (IllegalAccessException e222) {
                    throw new ObjenesisException(e222);
                }
            }
        }
        Class percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
        if (class$java$io$ObjectInputStream == null) {
            class$ = class$("java.io.ObjectInputStream");
            class$java$io$ObjectInputStream = class$;
            cls = class$;
        } else {
            cls = class$java$io$ObjectInputStream;
        }
        String str = "noArgConstruct";
        Class[] clsArr = new Class[3];
        if (class$java$lang$Class == null) {
            class$ = class$("java.lang.Class");
            class$java$lang$Class = class$;
        } else {
            class$ = class$java$lang$Class;
        }
        clsArr[0] = class$;
        if (class$java$lang$Object == null) {
            class$ = class$("java.lang.Object");
            class$java$lang$Object = class$;
        } else {
            class$ = class$java$lang$Object;
        }
        clsArr[1] = class$;
        clsArr[2] = percMethodClass;
        this.newInstanceMethod = cls.getDeclaredMethod(str, clsArr);
        this.newInstanceMethod.setAccessible(true);
        Class percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
        String str2 = "getPercClass";
        Class[] clsArr2 = new Class[1];
        if (class$java$lang$Class == null) {
            class$ = class$("java.lang.Class");
            class$java$lang$Class = class$;
        } else {
            class$ = class$java$lang$Class;
        }
        clsArr2[0] = class$;
        Object someObject = percClassClass.getDeclaredMethod(str2, clsArr2).invoke(null, new Object[]{unserializableType});
        cls = someObject.getClass();
        str = "findMethod";
        clsArr = new Class[1];
        if (class$java$lang$String == null) {
            class$ = class$("java.lang.String");
            class$java$lang$String = class$;
        } else {
            class$ = class$java$lang$String;
        }
        clsArr[0] = class$;
        Object percMethod = cls.getDeclaredMethod(str, clsArr).invoke(someObject, new Object[]{"<init>()V"});
        this.typeArgs = new Object[]{unserializableType, type, percMethod};
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
        } catch (IllegalAccessException e) {
            throw new ObjenesisException(e);
        } catch (InvocationTargetException e2) {
            throw new ObjenesisException(e2);
        }
    }
}
