package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import org.springframework.util.Assert;

abstract class AbstractDescriptor {
    private final Class<?> type;

    public abstract Annotation[] getAnnotations();

    public abstract AbstractDescriptor nested(Class<?> cls, int i);

    public abstract Class<?> resolveCollectionElementType();

    public abstract Class<?> resolveMapKeyType();

    public abstract Class<?> resolveMapValueType();

    protected AbstractDescriptor(Class<?> type) {
        Assert.notNull(type, "Type must not be null");
        this.type = type;
    }

    public Class<?> getType() {
        return this.type;
    }

    public TypeDescriptor getElementTypeDescriptor() {
        if (isCollection()) {
            Class<?> elementType = resolveCollectionElementType();
            if (elementType != null) {
                return new TypeDescriptor(nested(elementType, 0));
            }
            return null;
        } else if (isArray()) {
            return new TypeDescriptor(nested(getType().getComponentType(), 0));
        } else {
            return null;
        }
    }

    public TypeDescriptor getMapKeyTypeDescriptor() {
        if (!isMap()) {
            return null;
        }
        Class<?> keyType = resolveMapKeyType();
        if (keyType != null) {
            return new TypeDescriptor(nested(keyType, 0));
        }
        return null;
    }

    public TypeDescriptor getMapValueTypeDescriptor() {
        if (!isMap()) {
            return null;
        }
        Class<?> valueType = resolveMapValueType();
        if (valueType != null) {
            return new TypeDescriptor(nested(valueType, 1));
        }
        return null;
    }

    public AbstractDescriptor nested() {
        if (isCollection()) {
            Class<?> elementType = resolveCollectionElementType();
            if (elementType != null) {
                return nested(elementType, 0);
            }
            return null;
        } else if (isArray()) {
            return nested(getType().getComponentType(), 0);
        } else {
            if (isMap()) {
                Class<?> mapValueType = resolveMapValueType();
                if (mapValueType != null) {
                    return nested(mapValueType, 1);
                }
                return null;
            } else if (Object.class.equals(getType())) {
                return this;
            } else {
                throw new IllegalStateException("Not a collection, array, or map: cannot resolve nested value types");
            }
        }
    }

    private boolean isCollection() {
        return Collection.class.isAssignableFrom(getType());
    }

    private boolean isArray() {
        return getType().isArray();
    }

    private boolean isMap() {
        return Map.class.isAssignableFrom(getType());
    }
}
