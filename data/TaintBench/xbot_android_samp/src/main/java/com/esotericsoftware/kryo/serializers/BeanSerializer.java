package com.esotericsoftware.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.reflectasm.MethodAccess;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BeanSerializer<T> extends Serializer<T> {
    static final Object[] noArgs = new Object[0];
    Object access;
    private final Kryo kryo;
    private CachedProperty[] properties;

    class CachedProperty<X> {
        Method getMethod;
        int getterAccessIndex;
        String name;
        Serializer serializer;
        Method setMethod;
        Class setMethodType;
        int setterAccessIndex;

        CachedProperty() {
        }

        public String toString() {
            return this.name;
        }

        /* access modifiers changed from: 0000 */
        public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
            if (BeanSerializer.this.access != null) {
                return ((MethodAccess) BeanSerializer.this.access).invoke(obj, this.getterAccessIndex, new Object[0]);
            }
            return this.getMethod.invoke(obj, BeanSerializer.noArgs);
        }

        /* access modifiers changed from: 0000 */
        public void set(Object obj, Object obj2) throws IllegalAccessException, InvocationTargetException {
            if (BeanSerializer.this.access != null) {
                ((MethodAccess) BeanSerializer.this.access).invoke(obj, this.setterAccessIndex, obj2);
                return;
            }
            this.setMethod.invoke(obj, new Object[]{obj2});
        }
    }

    public BeanSerializer(Kryo kryo, Class cls) {
        int i = 0;
        this.kryo = kryo;
        try {
            int length;
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(cls).getPropertyDescriptors();
            Arrays.sort(propertyDescriptors, new Comparator<PropertyDescriptor>() {
                public int compare(PropertyDescriptor propertyDescriptor, PropertyDescriptor propertyDescriptor2) {
                    return propertyDescriptor.getName().compareTo(propertyDescriptor2.getName());
                }
            });
            ArrayList arrayList = new ArrayList(propertyDescriptors.length);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String name = propertyDescriptor.getName();
                if (!name.equals("class")) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    if (!(readMethod == null || writeMethod == null)) {
                        Serializer serializer = null;
                        Class returnType = readMethod.getReturnType();
                        if (kryo.isFinal(returnType)) {
                            serializer = kryo.getRegistration(returnType).getSerializer();
                        }
                        CachedProperty cachedProperty = new CachedProperty();
                        cachedProperty.name = name;
                        cachedProperty.getMethod = readMethod;
                        cachedProperty.setMethod = writeMethod;
                        cachedProperty.serializer = serializer;
                        cachedProperty.setMethodType = writeMethod.getParameterTypes()[0];
                        arrayList.add(cachedProperty);
                    }
                }
            }
            this.properties = (CachedProperty[]) arrayList.toArray(new CachedProperty[arrayList.size()]);
            try {
                this.access = MethodAccess.get(cls);
                length = this.properties.length;
                while (i < length) {
                    CachedProperty cachedProperty2 = this.properties[i];
                    cachedProperty2.getterAccessIndex = ((MethodAccess) this.access).getIndex(cachedProperty2.getMethod.getName());
                    cachedProperty2.setterAccessIndex = ((MethodAccess) this.access).getIndex(cachedProperty2.setMethod.getName());
                    i++;
                }
            } catch (Throwable th) {
            }
        } catch (IntrospectionException e) {
            throw new KryoException("Error getting bean info.", e);
        }
    }

    public void write(Kryo kryo, Output output, T t) {
        Class cls = t.getClass();
        int i = 0;
        int length = this.properties.length;
        while (i < length) {
            Object obj = this.properties[i];
            try {
                Object obj2 = obj.get(t);
                Serializer serializer = obj.serializer;
                if (serializer != null) {
                    kryo.writeObjectOrNull(output, obj2, serializer);
                } else {
                    kryo.writeClassAndObject(output, obj2);
                }
                i++;
            } catch (IllegalAccessException e) {
                throw new KryoException("Error accessing getter method: " + obj + " (" + cls.getName() + ")", e);
            } catch (InvocationTargetException e2) {
                throw new KryoException("Error invoking getter method: " + obj + " (" + cls.getName() + ")", e2);
            } catch (KryoException e3) {
                e3.addTrace(obj + " (" + cls.getName() + ")");
                throw e3;
            } catch (RuntimeException e4) {
                KryoException kryoException = new KryoException(e4);
                kryoException.addTrace(obj + " (" + cls.getName() + ")");
                throw kryoException;
            }
        }
    }

    public T read(Kryo kryo, Input input, Class<T> cls) {
        Object newInstance = kryo.newInstance(cls);
        kryo.reference(newInstance);
        int length = this.properties.length;
        int i = 0;
        while (i < length) {
            Object obj = this.properties[i];
            try {
                Object readObjectOrNull;
                Serializer serializer = obj.serializer;
                if (serializer != null) {
                    readObjectOrNull = kryo.readObjectOrNull(input, obj.setMethodType, serializer);
                } else {
                    readObjectOrNull = kryo.readClassAndObject(input);
                }
                obj.set(newInstance, readObjectOrNull);
                i++;
            } catch (IllegalAccessException e) {
                throw new KryoException("Error accessing setter method: " + obj + " (" + newInstance.getClass().getName() + ")", e);
            } catch (InvocationTargetException e2) {
                throw new KryoException("Error invoking setter method: " + obj + " (" + newInstance.getClass().getName() + ")", e2);
            } catch (KryoException e3) {
                e3.addTrace(obj + " (" + newInstance.getClass().getName() + ")");
                throw e3;
            } catch (RuntimeException e4) {
                KryoException kryoException = new KryoException(e4);
                kryoException.addTrace(obj + " (" + newInstance.getClass().getName() + ")");
                throw kryoException;
            }
        }
        return newInstance;
    }

    public T copy(Kryo kryo, T t) {
        Object newInstance = kryo.newInstance(t.getClass());
        int i = 0;
        int length = this.properties.length;
        while (i < length) {
            Object obj = this.properties[i];
            try {
                obj.set(newInstance, obj.get(t));
                i++;
            } catch (KryoException e) {
                e.addTrace(obj + " (" + newInstance.getClass().getName() + ")");
                throw e;
            } catch (RuntimeException e2) {
                KryoException kryoException = new KryoException(e2);
                kryoException.addTrace(obj + " (" + newInstance.getClass().getName() + ")");
                throw kryoException;
            } catch (Exception e3) {
                throw new KryoException("Error copying bean property: " + obj + " (" + newInstance.getClass().getName() + ")", e3);
            }
        }
        return newInstance;
    }
}
