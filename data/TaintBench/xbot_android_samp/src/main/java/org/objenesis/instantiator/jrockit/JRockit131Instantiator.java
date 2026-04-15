package org.objenesis.instantiator.jrockit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;

public class JRockit131Instantiator implements ObjectInstantiator {
    static Class class$java$lang$Class;
    static Class class$java$lang$Object;
    static Class class$java$lang$reflect$Constructor;
    private static Method newConstructorForSerializationMethod;
    private Constructor mungedConstructor;

    private static void initialize() {
        if (newConstructorForSerializationMethod == null) {
            try {
                Class class$;
                Class cl = Class.forName("COM.jrockit.reflect.MemberAccess");
                String str = "newConstructorForSerialization";
                Class[] clsArr = new Class[2];
                if (class$java$lang$reflect$Constructor == null) {
                    class$ = class$("java.lang.reflect.Constructor");
                    class$java$lang$reflect$Constructor = class$;
                } else {
                    class$ = class$java$lang$reflect$Constructor;
                }
                clsArr[0] = class$;
                if (class$java$lang$Class == null) {
                    class$ = class$("java.lang.Class");
                    class$java$lang$Class = class$;
                } else {
                    class$ = class$java$lang$Class;
                }
                clsArr[1] = class$;
                newConstructorForSerializationMethod = cl.getDeclaredMethod(str, clsArr);
                newConstructorForSerializationMethod.setAccessible(true);
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

    public JRockit131Instantiator(Class type) {
        initialize();
        if (newConstructorForSerializationMethod != null) {
            try {
                Class cls;
                if (class$java$lang$Object == null) {
                    Class class$ = class$("java.lang.Object");
                    class$java$lang$Object = class$;
                    cls = class$;
                } else {
                    cls = class$java$lang$Object;
                }
                Constructor javaLangObjectConstructor = cls.getConstructor((Class[]) null);
                try {
                    this.mungedConstructor = (Constructor) newConstructorForSerializationMethod.invoke(null, new Object[]{javaLangObjectConstructor, type});
                } catch (Exception e) {
                    throw new ObjenesisException(e);
                }
            } catch (NoSuchMethodException e2) {
                throw new Error("Cannot find constructor for java.lang.Object!");
            }
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
