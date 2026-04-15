package flexjson.transformer;

import java.lang.reflect.InvocationTargetException;

public class HibernateTransformer extends ObjectTransformer {
    /* access modifiers changed from: protected */
    public Class resolveClass(Object obj) {
        return findBeanClass(obj);
    }

    public Class<?> findBeanClass(Object object) {
        try {
            Object initializer = object.getClass().getMethod("getHibernateLazyInitializer", new Class[0]).invoke(object, new Object[0]);
            return (Class) initializer.getClass().getMethod("getPersistentClass", new Class[0]).invoke(initializer, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return object.getClass();
        }
    }
}
