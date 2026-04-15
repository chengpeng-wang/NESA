package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public final class Property {
    private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap();
    private Annotation[] annotations;
    private final MethodParameter methodParameter;
    private final String name;
    private final Class<?> objectType;
    private final Method readMethod;
    private final Method writeMethod;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
        this(objectType, readMethod, writeMethod, null);
    }

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        if (name == null) {
            name = resolveName();
        }
        this.name = name;
    }

    public Class<?> getObjectType() {
        return this.objectType;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.methodParameter.getParameterType();
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    /* access modifiers changed from: 0000 */
    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    /* access modifiers changed from: 0000 */
    public Annotation[] getAnnotations() {
        if (this.annotations == null) {
            this.annotations = resolveAnnotations();
        }
        return this.annotations;
    }

    private String resolveName() {
        int index;
        if (this.readMethod != null) {
            index = this.readMethod.getName().indexOf("get");
            if (index != -1) {
                index += 3;
            } else {
                index = this.readMethod.getName().indexOf("is");
                if (index == -1) {
                    throw new IllegalArgumentException("Not a getter method");
                }
                index += 2;
            }
            return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
        }
        index = this.writeMethod.getName().indexOf("set") + 3;
        if (index != -1) {
            return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
        }
        throw new IllegalArgumentException("Not a setter method");
    }

    private MethodParameter resolveMethodParameter() {
        MethodParameter read = resolveReadMethodParameter();
        MethodParameter write = resolveWriteMethodParameter();
        if (write != null) {
            if (read != null) {
                Class<?> readType = read.getParameterType();
                Class<?> writeType = write.getParameterType();
                if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
                    return read;
                }
            }
            return write;
        } else if (read != null) {
            return read;
        } else {
            throw new IllegalStateException("Property is neither readable nor writeable");
        }
    }

    private MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getReadMethod(), -1));
    }

    private MethodParameter resolveWriteMethodParameter() {
        if (getWriteMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
    }

    private MethodParameter resolveParameterType(MethodParameter parameter) {
        GenericTypeResolver.resolveParameterType(parameter, getObjectType());
        return parameter;
    }

    private Annotation[] resolveAnnotations() {
        Annotation[] annotations = (Annotation[]) annotationCache.get(this);
        if (annotations != null) {
            return annotations;
        }
        Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap();
        addAnnotationsToMap(annotationMap, getReadMethod());
        addAnnotationsToMap(annotationMap, getWriteMethod());
        addAnnotationsToMap(annotationMap, getField());
        annotations = (Annotation[]) annotationMap.values().toArray(new Annotation[annotationMap.size()]);
        annotationCache.put(this, annotations);
        return annotations;
    }

    private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, AnnotatedElement object) {
        if (object != null) {
            for (Annotation annotation : object.getAnnotations()) {
                annotationMap.put(annotation.annotationType(), annotation);
            }
        }
    }

    private Field getField() {
        String name = getName();
        if (!StringUtils.hasLength(name)) {
            return null;
        }
        Class<?> declaringClass = declaringClass();
        Field field = ReflectionUtils.findField(declaringClass, name);
        if (field != null) {
            return field;
        }
        field = ReflectionUtils.findField(declaringClass, name.substring(0, 1).toLowerCase() + name.substring(1));
        if (field == null) {
            return ReflectionUtils.findField(declaringClass, name.substring(0, 1).toUpperCase() + name.substring(1));
        }
        return field;
    }

    private Class<?> declaringClass() {
        if (getReadMethod() != null) {
            return getReadMethod().getDeclaringClass();
        }
        return getWriteMethod().getDeclaringClass();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Property)) {
            return false;
        }
        Property otherProperty = (Property) other;
        if (ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) && ObjectUtils.nullSafeEquals(this.name, otherProperty.name) && ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) && ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.objectType) * 31) + ObjectUtils.nullSafeHashCode(this.name);
    }
}
