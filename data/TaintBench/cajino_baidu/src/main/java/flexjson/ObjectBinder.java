package flexjson;

import flexjson.factories.ArrayObjectFactory;
import flexjson.factories.BeanObjectFactory;
import flexjson.factories.BigDecimalFactory;
import flexjson.factories.BigIntegerFactory;
import flexjson.factories.BooleanObjectFactory;
import flexjson.factories.ByteObjectFactory;
import flexjson.factories.CharacterObjectFactory;
import flexjson.factories.ClassLocatorObjectFactory;
import flexjson.factories.DateObjectFactory;
import flexjson.factories.DoubleObjectFactory;
import flexjson.factories.EnumObjectFactory;
import flexjson.factories.FloatObjectFactory;
import flexjson.factories.IntegerObjectFactory;
import flexjson.factories.ListObjectFactory;
import flexjson.factories.LongObjectFactory;
import flexjson.factories.MapObjectFactory;
import flexjson.factories.SetObjectFactory;
import flexjson.factories.ShortObjectFactory;
import flexjson.factories.SortedSetObjectFactory;
import flexjson.factories.StringObjectFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class ObjectBinder {
    private Path currentPath = new Path();
    private Map<Class, ObjectFactory> factories = new HashMap();
    private LinkedList<Object> jsonStack = new LinkedList();
    private LinkedList<Object> objectStack = new LinkedList();
    private Map<Path, ObjectFactory> pathFactories = new HashMap();

    public ObjectBinder() {
        this.factories.put(Object.class, new BeanObjectFactory());
        this.factories.put(Collection.class, new ListObjectFactory());
        this.factories.put(List.class, new ListObjectFactory());
        this.factories.put(Set.class, new SetObjectFactory());
        this.factories.put(SortedSet.class, new SortedSetObjectFactory());
        this.factories.put(Map.class, new MapObjectFactory());
        this.factories.put(Integer.class, new IntegerObjectFactory());
        this.factories.put(Integer.TYPE, new IntegerObjectFactory());
        this.factories.put(Float.class, new FloatObjectFactory());
        this.factories.put(Float.TYPE, new FloatObjectFactory());
        this.factories.put(Double.class, new DoubleObjectFactory());
        this.factories.put(Double.TYPE, new DoubleObjectFactory());
        this.factories.put(Short.class, new ShortObjectFactory());
        this.factories.put(Short.TYPE, new ShortObjectFactory());
        this.factories.put(Long.class, new LongObjectFactory());
        this.factories.put(Long.TYPE, new LongObjectFactory());
        this.factories.put(Byte.class, new ByteObjectFactory());
        this.factories.put(Byte.TYPE, new ByteObjectFactory());
        this.factories.put(Boolean.class, new BooleanObjectFactory());
        this.factories.put(Boolean.TYPE, new BooleanObjectFactory());
        this.factories.put(Character.class, new CharacterObjectFactory());
        this.factories.put(Character.TYPE, new CharacterObjectFactory());
        this.factories.put(Enum.class, new EnumObjectFactory());
        this.factories.put(Date.class, new DateObjectFactory());
        this.factories.put(String.class, new StringObjectFactory());
        this.factories.put(Array.class, new ArrayObjectFactory());
        this.factories.put(BigDecimal.class, new BigDecimalFactory());
        this.factories.put(BigInteger.class, new BigIntegerFactory());
    }

    public ObjectBinder use(Path path, ObjectFactory factory) {
        this.pathFactories.put(path, factory);
        return this;
    }

    public ObjectBinder use(Class clazz, ObjectFactory factory) {
        this.factories.put(clazz, factory);
        return this;
    }

    public Path getCurrentPath() {
        return this.currentPath;
    }

    public Object bind(Object input) {
        return bind(input, null);
    }

    public Object bind(Object source, Object target) {
        if (target instanceof Map) {
            bindIntoMap((Map) source, (Map) target, null, null);
        } else if (target instanceof Collection) {
            bindIntoCollection((Collection) source, (Collection) target, null);
        } else {
            bindIntoObject((Map) source, target, target.getClass());
        }
        return target;
    }

    public Object bind(Object input, Type targetType) {
        this.jsonStack.add(input);
        if (input == null) {
            this.jsonStack.removeLast();
            return null;
        }
        try {
            Class targetClass = findClassName(input, getTargetClass(targetType));
            ObjectFactory factory = findFactoryFor(targetClass);
            if (factory == null) {
                throw new JSONException(this.currentPath + ": + Could not find a suitable ObjectFactory for " + targetClass);
            }
            Object instantiate = factory.instantiate(this, input, targetType, targetClass);
            return instantiate;
        } finally {
            this.jsonStack.removeLast();
        }
    }

    public <T extends Collection<Object>> T bindIntoCollection(Collection value, T target, Type targetType) {
        Type valueType = null;
        if (targetType instanceof ParameterizedType) {
            valueType = ((ParameterizedType) targetType).getActualTypeArguments()[0];
        }
        this.jsonStack.add(value);
        this.objectStack.add(target);
        getCurrentPath().enqueue("values");
        for (Object obj : value) {
            target.add(bind(obj, valueType));
        }
        getCurrentPath().pop();
        this.objectStack.removeLast();
        this.jsonStack.removeLast();
        return target;
    }

    public Object bindIntoMap(Map input, Map<Object, Object> result, Type keyType, Type valueType) {
        this.jsonStack.add(input);
        this.objectStack.add(result);
        for (Object inputKey : input.keySet()) {
            this.currentPath.enqueue("keys");
            Object key = bind(inputKey, keyType);
            this.currentPath.pop();
            this.currentPath.enqueue("values");
            Object value = bind(input.get(inputKey), valueType);
            this.currentPath.pop();
            result.put(key, value);
        }
        this.objectStack.removeLast();
        this.jsonStack.removeLast();
        return result;
    }

    public Object bindIntoObject(Map jsonOwner, Object target, Type targetType) {
        try {
            this.objectStack.add(target);
            for (BeanProperty descriptor : BeanAnalyzer.analyze(target.getClass()).getProperties()) {
                Object value = findFieldInJson(jsonOwner, descriptor);
                if (value != null) {
                    this.currentPath.enqueue(descriptor.getName());
                    Method setMethod = descriptor.getWriteMethod();
                    if (setMethod != null) {
                        Type[] types = setMethod.getGenericParameterTypes();
                        if (types.length == 1) {
                            Type paramType = types[0];
                            setMethod.invoke(this.objectStack.getLast(), new Object[]{bind(value, resolveParameterizedTypes(paramType, targetType))});
                        } else {
                            throw new JSONException(this.currentPath + ":  Expected a single parameter for method " + target.getClass().getName() + "." + setMethod.getName() + " but got " + types.length);
                        }
                    }
                    Field field = descriptor.getProperty();
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(target, bind(value, field.getGenericType()));
                    }
                    this.currentPath.pop();
                }
            }
            return this.objectStack.removeLast();
        } catch (IllegalAccessException e) {
            throw new JSONException(this.currentPath + ":  Could not access the no-arg constructor for " + target.getClass().getName(), e);
        } catch (InvocationTargetException ex) {
            throw new JSONException(this.currentPath + ":  Exception while trying to invoke setter method.", ex);
        }
    }

    public JSONException cannotConvertValueToTargetType(Object value, Class targetType) {
        return new JSONException(String.format("%s:  Can not convert %s into %s", new Object[]{this.currentPath, value.getClass().getName(), targetType.getName()}));
    }

    private Class getTargetClass(Type targetType) {
        if (targetType == null) {
            return null;
        }
        if (targetType instanceof Class) {
            return (Class) targetType;
        }
        if (targetType instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) targetType).getRawType();
        }
        if (targetType instanceof GenericArrayType) {
            return Array.class;
        }
        if (targetType instanceof WildcardType) {
            return null;
        }
        if (targetType instanceof TypeVariable) {
            return null;
        }
        throw new JSONException(this.currentPath + ":  Unknown type " + targetType);
    }

    private Type resolveParameterizedTypes(Type genericType, Type targetType) {
        if ((genericType instanceof Class) || (genericType instanceof ParameterizedType)) {
            return genericType;
        }
        if (genericType instanceof TypeVariable) {
            return targetType;
        }
        if (genericType instanceof WildcardType) {
            return targetType;
        }
        if (genericType instanceof GenericArrayType) {
            return ((GenericArrayType) genericType).getGenericComponentType();
        }
        throw new JSONException(this.currentPath + ":  Unknown generic type " + genericType + ".");
    }

    private Class findClassName(Object map, Class targetType) throws JSONException {
        if (this.pathFactories.containsKey(this.currentPath)) {
            return null;
        }
        Class mostSpecificType = useMostSpecific(targetType, map instanceof Map ? findClassInMap((Map) map, null) : null);
        if (mostSpecificType == null) {
            return map.getClass();
        }
        return mostSpecificType;
    }

    /* access modifiers changed from: protected */
    public Class useMostSpecific(Class classFromTarget, Class typeFound) {
        if (classFromTarget != null && typeFound != null) {
            return typeFound.isAssignableFrom(classFromTarget) ? classFromTarget : typeFound;
        } else {
            if (typeFound != null) {
                return typeFound;
            }
            return classFromTarget == null ? null : classFromTarget;
        }
    }

    /* access modifiers changed from: protected */
    public Class findClassInMap(Map map, Class override) {
        if (override != null) {
            return override;
        }
        String classname = (String) map.remove("class");
        if (classname == null) {
            return null;
        }
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new JSONException(String.format("%s:  Could not load %s", new Object[]{this.currentPath, classname}), e);
        }
    }

    private ObjectFactory findFactoryFor(Class targetType) {
        ObjectFactory factory = (ObjectFactory) this.pathFactories.get(this.currentPath);
        if (factory != null) {
            return factory;
        }
        if (targetType == null || !targetType.isArray()) {
            return findFactoryByTargetClass(targetType);
        }
        return (ObjectFactory) this.factories.get(Array.class);
    }

    private ObjectFactory findFactoryByTargetClass(Class targetType) {
        ObjectFactory factory = (ObjectFactory) this.factories.get(targetType);
        if (factory != null || targetType == null || targetType.getSuperclass() == null) {
            return factory;
        }
        for (Class intf : targetType.getInterfaces()) {
            factory = findFactoryByTargetClass(intf);
            if (factory != null) {
                return factory;
            }
        }
        return findFactoryByTargetClass(targetType.getSuperclass());
    }

    /* access modifiers changed from: protected */
    public Object instantiate(Class clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        } catch (InstantiationException e) {
            throw new JSONException(this.currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new JSONException(this.currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e2);
        } catch (InvocationTargetException e3) {
            throw new JSONException(this.currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e3);
        } catch (NoSuchMethodException e4) {
            throw new JSONException(this.currentPath + ": " + clazz.getName() + " lacks a no argument constructor.  Flexjson will instantiate any protected, private, or public no-arg constructor.", e4);
        }
    }

    private Object findFieldInJson(Map map, BeanProperty property) {
        Object value = map.get(property.getName());
        if (value == null) {
            return map.get(upperCase(property.getName()));
        }
        return value;
    }

    private String upperCase(String field) {
        return Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    public Object getTarget() {
        return this.objectStack.getLast();
    }

    public Object getSource() {
        return this.jsonStack.getLast();
    }

    public Object bindPrimitive(Object value, Class clazz) {
        if (value.getClass() == clazz) {
            return value;
        }
        if ((value instanceof Number) && clazz.equals(Double.class)) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if ((value instanceof Number) && clazz.equals(Integer.class)) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if ((value instanceof Number) && clazz.equals(Long.class)) {
            return Long.valueOf(((Number) value).longValue());
        }
        if ((value instanceof Number) && clazz.equals(Short.class)) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if ((value instanceof Number) && clazz.equals(Byte.class)) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        if ((value instanceof Number) && clazz.equals(Float.class)) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if ((value instanceof Boolean) && clazz.equals(Boolean.class)) {
            return value;
        }
        if ((value instanceof Long) && clazz == Date.class) {
            return new Date(((Long) value).longValue());
        }
        throw new JSONException(String.format("%s: Don't know how to bind %s into class %s.  You might need to use an ObjectFactory instead of a plain class.", new Object[]{getCurrentPath().toString(), value, clazz.getName()}));
    }

    public Class findClassAtPath(Path currentPath) throws ClassNotFoundException {
        ObjectFactory factory = (ObjectFactory) this.pathFactories.get(currentPath);
        if (factory instanceof ClassLocatorObjectFactory) {
            return ((ClassLocatorObjectFactory) factory).getLocator().locate(this, currentPath);
        }
        return null;
    }
}
