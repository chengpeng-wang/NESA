package org.objenesis.instantiator;

public class SerializationInstantiatorHelper {
    static Class class$java$io$Serializable;

    public static Class getNonSerializableSuperClass(Class type) {
        Class result = type;
        do {
            Class class$;
            if (class$java$io$Serializable == null) {
                class$ = class$("java.io.Serializable");
                class$java$io$Serializable = class$;
            } else {
                class$ = class$java$io$Serializable;
            }
            if (!class$.isAssignableFrom(result)) {
                return result;
            }
            result = result.getSuperclass();
        } while (result != null);
        throw new Error("Bad class hierarchy: No non-serializable parents");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
