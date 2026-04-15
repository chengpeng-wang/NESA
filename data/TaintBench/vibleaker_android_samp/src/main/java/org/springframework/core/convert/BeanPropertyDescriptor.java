package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;

class BeanPropertyDescriptor extends AbstractDescriptor {
    private final Annotation[] annotations;
    private final MethodParameter methodParameter;
    private final Property property;

    public BeanPropertyDescriptor(Property property) {
        super(property.getType());
        this.property = property;
        this.methodParameter = property.getMethodParameter();
        this.annotations = property.getAnnotations();
    }

    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveCollectionElementType() {
        return GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapKeyType() {
        return GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapValueType() {
        return GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
    }

    /* access modifiers changed from: protected */
    public AbstractDescriptor nested(Class<?> type, int typeIndex) {
        MethodParameter methodParameter = new MethodParameter(this.methodParameter);
        methodParameter.increaseNestingLevel();
        methodParameter.setTypeIndexForCurrentLevel(typeIndex);
        return new BeanPropertyDescriptor(type, this.property, methodParameter, this.annotations);
    }

    private BeanPropertyDescriptor(Class<?> type, Property propertyDescriptor, MethodParameter methodParameter, Annotation[] annotations) {
        super(type);
        this.property = propertyDescriptor;
        this.methodParameter = methodParameter;
        this.annotations = annotations;
    }
}
