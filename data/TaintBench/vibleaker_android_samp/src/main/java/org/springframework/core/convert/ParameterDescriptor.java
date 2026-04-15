package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;

class ParameterDescriptor extends AbstractDescriptor {
    private final MethodParameter methodParameter;

    public ParameterDescriptor(MethodParameter methodParameter) {
        super(methodParameter.getParameterType());
        if (methodParameter.getNestingLevel() != 1) {
            throw new IllegalArgumentException("MethodParameter argument must have its nestingLevel set to 1");
        }
        this.methodParameter = methodParameter;
    }

    private ParameterDescriptor(Class<?> type, MethodParameter methodParameter) {
        super(type);
        this.methodParameter = methodParameter;
    }

    public Annotation[] getAnnotations() {
        if (this.methodParameter.getParameterIndex() == -1) {
            return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getMethodAnnotations());
        }
        return TypeDescriptor.nullSafeAnnotations(this.methodParameter.getParameterAnnotations());
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
        return new ParameterDescriptor(type, methodParameter);
    }
}
