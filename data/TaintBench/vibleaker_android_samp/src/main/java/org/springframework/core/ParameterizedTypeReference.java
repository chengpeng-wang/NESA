package org.springframework.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.util.Assert;

public abstract class ParameterizedTypeReference<T> {
    private final Type type;

    protected ParameterizedTypeReference() {
        boolean z = true;
        Type type = findParameterizedTypeReferenceSubclass(getClass()).getGenericSuperclass();
        Assert.isInstanceOf(ParameterizedType.class, type);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (parameterizedType.getActualTypeArguments().length != 1) {
            z = false;
        }
        Assert.isTrue(z);
        this.type = parameterizedType.getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof ParameterizedTypeReference) && this.type.equals(((ParameterizedTypeReference) obj).type));
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    public String toString() {
        return "ParameterizedTypeReference<" + this.type + ">";
    }

    private static Class<?> findParameterizedTypeReferenceSubclass(Class<?> child) {
        Class<?> parent = child.getSuperclass();
        if (!Object.class.equals(parent)) {
            return ParameterizedTypeReference.class.equals(parent) ? child : findParameterizedTypeReferenceSubclass(parent);
        } else {
            throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
        }
    }
}
