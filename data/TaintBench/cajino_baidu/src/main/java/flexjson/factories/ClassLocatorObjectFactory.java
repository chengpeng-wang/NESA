package flexjson.factories;

import flexjson.ClassLocator;
import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class ClassLocatorObjectFactory implements ObjectFactory {
    private ClassLocator locator;

    public ClassLocatorObjectFactory(ClassLocator locator) {
        this.locator = locator;
    }

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        Class clazz = null;
        try {
            clazz = this.locator.locate(context, context.getCurrentPath());
            if (clazz == null) {
                return null;
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                return context.bindIntoCollection((Collection) value, (Collection) createTargetObject(clazz), targetType);
            }
            if (Map.class.isAssignableFrom(clazz)) {
                if (!(targetType instanceof ParameterizedType)) {
                    return context.bindIntoMap((Map) value, (Map) createTargetObject(clazz), null, null);
                }
                ParameterizedType ptype = (ParameterizedType) targetType;
                return context.bindIntoMap((Map) value, (Map) createTargetObject(clazz), ptype.getActualTypeArguments()[0], ptype.getActualTypeArguments()[1]);
            } else if (value instanceof Map) {
                return context.bindIntoObject((Map) value, createTargetObject(clazz), clazz);
            } else {
                return context.bindPrimitive(value, clazz);
            }
        } catch (ClassNotFoundException ex) {
            throw new JSONException(String.format("%s: Could not find class %s", new Object[]{context.getCurrentPath(), ex.getMessage()}), ex);
        } catch (IllegalAccessException e) {
            throw new JSONException(String.format("%s: Could not instantiate class %s", new Object[]{context.getCurrentPath(), clazz.getName()}), e);
        } catch (InstantiationException e2) {
            throw new JSONException(String.format("%s: Problem while instantiating class %s", new Object[]{context.getCurrentPath(), clazz.getName()}), e2);
        } catch (NoSuchMethodException e3) {
            throw new JSONException(String.format("%s: Could not find a no-arg constructor for %s", new Object[]{context.getCurrentPath(), clazz.getName()}), e3);
        } catch (InvocationTargetException e4) {
            throw new JSONException(String.format("%s: Problem while invoking the no-arg constructor for %s", new Object[]{context.getCurrentPath(), clazz.getName()}), e4);
        }
    }

    private Object createTargetObject(Class clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor constructor = clazz.getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        return constructor.newInstance(new Object[0]);
    }

    public ClassLocator getLocator() {
        return this.locator;
    }
}
