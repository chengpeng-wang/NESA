package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import org.springframework.core.GenericCollectionTypeResolver;

class ClassDescriptor extends AbstractDescriptor {
    ClassDescriptor(Class<?> type) {
        super(type);
    }

    public Annotation[] getAnnotations() {
        return TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveCollectionElementType() {
        return GenericCollectionTypeResolver.getCollectionType(getType());
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapKeyType() {
        return GenericCollectionTypeResolver.getMapKeyType(getType());
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapValueType() {
        return GenericCollectionTypeResolver.getMapValueType(getType());
    }

    /* access modifiers changed from: protected */
    public AbstractDescriptor nested(Class<?> type, int typeIndex) {
        return new ClassDescriptor(type);
    }
}
