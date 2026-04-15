package org.springframework.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

public class TypeDescriptor implements Serializable {
    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    private static final Map<Class<?>, TypeDescriptor> typeDescriptorCache = new HashMap();
    private final Annotation[] annotations;
    private final TypeDescriptor elementTypeDescriptor;
    private final TypeDescriptor mapKeyTypeDescriptor;
    private final TypeDescriptor mapValueTypeDescriptor;
    private final Class<?> type;

    static {
        typeDescriptorCache.put(Boolean.TYPE, new TypeDescriptor(Boolean.TYPE));
        typeDescriptorCache.put(Boolean.class, new TypeDescriptor(Boolean.class));
        typeDescriptorCache.put(Byte.TYPE, new TypeDescriptor(Byte.TYPE));
        typeDescriptorCache.put(Byte.class, new TypeDescriptor(Byte.class));
        typeDescriptorCache.put(Character.TYPE, new TypeDescriptor(Character.TYPE));
        typeDescriptorCache.put(Character.class, new TypeDescriptor(Character.class));
        typeDescriptorCache.put(Short.TYPE, new TypeDescriptor(Short.TYPE));
        typeDescriptorCache.put(Short.class, new TypeDescriptor(Short.class));
        typeDescriptorCache.put(Integer.TYPE, new TypeDescriptor(Integer.TYPE));
        typeDescriptorCache.put(Integer.class, new TypeDescriptor(Integer.class));
        typeDescriptorCache.put(Long.TYPE, new TypeDescriptor(Long.TYPE));
        typeDescriptorCache.put(Long.class, new TypeDescriptor(Long.class));
        typeDescriptorCache.put(Float.TYPE, new TypeDescriptor(Float.TYPE));
        typeDescriptorCache.put(Float.class, new TypeDescriptor(Float.class));
        typeDescriptorCache.put(Double.TYPE, new TypeDescriptor(Double.TYPE));
        typeDescriptorCache.put(Double.class, new TypeDescriptor(Double.class));
        typeDescriptorCache.put(String.class, new TypeDescriptor(String.class));
    }

    public TypeDescriptor(MethodParameter methodParameter) {
        this(new ParameterDescriptor(methodParameter));
    }

    public TypeDescriptor(Field field) {
        this(new FieldDescriptor(field));
    }

    public TypeDescriptor(Property property) {
        this(new BeanPropertyDescriptor(property));
    }

    public static TypeDescriptor valueOf(Class<?> type) {
        TypeDescriptor desc = (TypeDescriptor) typeDescriptorCache.get(type);
        return desc != null ? desc : new TypeDescriptor((Class) type);
    }

    public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
        if (Collection.class.isAssignableFrom(collectionType)) {
            return new TypeDescriptor(collectionType, elementTypeDescriptor);
        }
        throw new IllegalArgumentException("collectionType must be a java.util.Collection");
    }

    public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
        if (Map.class.isAssignableFrom(mapType)) {
            return new TypeDescriptor(mapType, keyTypeDescriptor, valueTypeDescriptor);
        }
        throw new IllegalArgumentException("mapType must be a java.util.Map");
    }

    public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor) {
        if (elementTypeDescriptor == null) {
            return null;
        }
        return new TypeDescriptor(Array.newInstance(elementTypeDescriptor.getType(), 0).getClass(), elementTypeDescriptor, null, null, elementTypeDescriptor.getAnnotations());
    }

    public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
        if (methodParameter.getNestingLevel() == 1) {
            return nested(new ParameterDescriptor(methodParameter), nestingLevel);
        }
        throw new IllegalArgumentException("methodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
    }

    public static TypeDescriptor nested(Field field, int nestingLevel) {
        return nested(new FieldDescriptor(field), nestingLevel);
    }

    public static TypeDescriptor nested(Property property, int nestingLevel) {
        return nested(new BeanPropertyDescriptor(property), nestingLevel);
    }

    public static TypeDescriptor forObject(Object source) {
        return source != null ? valueOf(source.getClass()) : null;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Class<?> getObjectType() {
        return ClassUtils.resolvePrimitiveIfNecessary(getType());
    }

    public TypeDescriptor narrow(Object value) {
        return value == null ? this : new TypeDescriptor(value.getClass(), this.elementTypeDescriptor, this.mapKeyTypeDescriptor, this.mapValueTypeDescriptor, this.annotations);
    }

    public TypeDescriptor upcast(Class<?> superType) {
        if (superType == null) {
            return null;
        }
        Assert.isAssignable(superType, getType());
        return new TypeDescriptor(superType, this.elementTypeDescriptor, this.mapKeyTypeDescriptor, this.mapValueTypeDescriptor, this.annotations);
    }

    public String getName() {
        return ClassUtils.getQualifiedName(getType());
    }

    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        for (T annotation : this.annotations) {
            if (annotation.annotationType().equals(annotationType)) {
                return annotation;
            }
        }
        for (Annotation metaAnn : this.annotations) {
            T ann = metaAnn.annotationType().getAnnotation(annotationType);
            if (ann != null) {
                return ann;
            }
        }
        return null;
    }

    public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
        if (!typeDescriptor.getObjectType().isAssignableFrom(getObjectType())) {
            return false;
        }
        if (isArray() && typeDescriptor.isArray()) {
            return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
        }
        if (isCollection() && typeDescriptor.isCollection()) {
            return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
        }
        if (!isMap() || !typeDescriptor.isMap()) {
            return true;
        }
        if (isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor())) {
            return true;
        }
        return false;
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(getType());
    }

    public boolean isArray() {
        return getType().isArray();
    }

    public TypeDescriptor getElementTypeDescriptor() {
        assertCollectionOrArray();
        return this.elementTypeDescriptor;
    }

    public TypeDescriptor elementTypeDescriptor(Object element) {
        return narrow(element, getElementTypeDescriptor());
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(getType());
    }

    public TypeDescriptor getMapKeyTypeDescriptor() {
        assertMap();
        return this.mapKeyTypeDescriptor;
    }

    public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
        return narrow(mapKey, getMapKeyTypeDescriptor());
    }

    public TypeDescriptor getMapValueTypeDescriptor() {
        assertMap();
        return this.mapValueTypeDescriptor;
    }

    public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
        return narrow(mapValue, getMapValueTypeDescriptor());
    }

    @Deprecated
    public Class<?> getElementType() {
        return getElementTypeDescriptor().getType();
    }

    @Deprecated
    public Class<?> getMapKeyType() {
        return getMapKeyTypeDescriptor().getType();
    }

    @Deprecated
    public Class<?> getMapValueType() {
        return getMapValueTypeDescriptor().getType();
    }

    TypeDescriptor(AbstractDescriptor descriptor) {
        this.type = descriptor.getType();
        this.elementTypeDescriptor = descriptor.getElementTypeDescriptor();
        this.mapKeyTypeDescriptor = descriptor.getMapKeyTypeDescriptor();
        this.mapValueTypeDescriptor = descriptor.getMapValueTypeDescriptor();
        this.annotations = descriptor.getAnnotations();
    }

    static Annotation[] nullSafeAnnotations(Annotation[] annotations) {
        return annotations != null ? annotations : EMPTY_ANNOTATION_ARRAY;
    }

    private TypeDescriptor(Class<?> type) {
        this(new ClassDescriptor(type));
    }

    private TypeDescriptor(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
        this(collectionType, elementTypeDescriptor, null, null, EMPTY_ANNOTATION_ARRAY);
    }

    private TypeDescriptor(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
        this(mapType, null, keyTypeDescriptor, valueTypeDescriptor, EMPTY_ANNOTATION_ARRAY);
    }

    private TypeDescriptor(Class<?> type, TypeDescriptor elementTypeDescriptor, TypeDescriptor mapKeyTypeDescriptor, TypeDescriptor mapValueTypeDescriptor, Annotation[] annotations) {
        this.type = type;
        this.elementTypeDescriptor = elementTypeDescriptor;
        this.mapKeyTypeDescriptor = mapKeyTypeDescriptor;
        this.mapValueTypeDescriptor = mapValueTypeDescriptor;
        this.annotations = annotations;
    }

    private static TypeDescriptor nested(AbstractDescriptor descriptor, int nestingLevel) {
        for (int i = 0; i < nestingLevel; i++) {
            descriptor = descriptor.nested();
            if (descriptor == null) {
                return null;
            }
        }
        return new TypeDescriptor(descriptor);
    }

    private void assertCollectionOrArray() {
        if (!isCollection() && !isArray()) {
            throw new IllegalStateException("Not a java.util.Collection or Array");
        }
    }

    private void assertMap() {
        if (!isMap()) {
            throw new IllegalStateException("Not a java.util.Map");
        }
    }

    private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor) {
        if (typeDescriptor != null) {
            return typeDescriptor.narrow(value);
        }
        if (value == null) {
            return null;
        }
        return new TypeDescriptor(value.getClass(), null, null, null, this.annotations);
    }

    private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor) {
        if (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null) {
            return true;
        }
        return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
    }

    private String wildcard(TypeDescriptor typeDescriptor) {
        return typeDescriptor != null ? typeDescriptor.toString() : "?";
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TypeDescriptor)) {
            return false;
        }
        TypeDescriptor other = (TypeDescriptor) obj;
        if (!ObjectUtils.nullSafeEquals(this.type, other.type) || this.annotations.length != other.annotations.length) {
            return false;
        }
        for (Annotation ann : this.annotations) {
            if (other.getAnnotation(ann.annotationType()) == null) {
                return false;
            }
        }
        if (isCollection() || isArray()) {
            return ObjectUtils.nullSafeEquals(this.elementTypeDescriptor, other.elementTypeDescriptor);
        }
        if (!isMap()) {
            return true;
        }
        if (!(ObjectUtils.nullSafeEquals(this.mapKeyTypeDescriptor, other.mapKeyTypeDescriptor) && ObjectUtils.nullSafeEquals(this.mapValueTypeDescriptor, other.mapValueTypeDescriptor))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return getType().hashCode();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Annotation ann : this.annotations) {
            builder.append("@").append(ann.annotationType().getName()).append(' ');
        }
        builder.append(ClassUtils.getQualifiedName(getType()));
        if (isMap()) {
            builder.append("<").append(wildcard(this.mapKeyTypeDescriptor));
            builder.append(", ").append(wildcard(this.mapValueTypeDescriptor)).append(">");
        } else if (isCollection()) {
            builder.append("<").append(wildcard(this.elementTypeDescriptor)).append(">");
        }
        return builder.toString();
    }
}
