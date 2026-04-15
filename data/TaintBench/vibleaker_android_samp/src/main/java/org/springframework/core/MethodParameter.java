package org.springframework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

public class MethodParameter {
    private final Constructor<?> constructor;
    private Type genericParameterType;
    private final Method method;
    private int nestingLevel;
    private Annotation[] parameterAnnotations;
    private final int parameterIndex;
    private String parameterName;
    private ParameterNameDiscoverer parameterNameDiscoverer;
    private Class<?> parameterType;
    Map<Integer, Integer> typeIndexesPerLevel;
    Map<TypeVariable, Type> typeVariableMap;

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        this.nestingLevel = 1;
        Assert.notNull(method, "Method must not be null");
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.constructor = null;
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex) {
        this((Constructor) constructor, parameterIndex, 1);
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        this.nestingLevel = 1;
        Assert.notNull(constructor, "Constructor must not be null");
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.method = null;
    }

    public MethodParameter(MethodParameter original) {
        this.nestingLevel = 1;
        Assert.notNull(original, "Original must not be null");
        this.method = original.method;
        this.constructor = original.constructor;
        this.parameterIndex = original.parameterIndex;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.typeVariableMap = original.typeVariableMap;
    }

    public Method getMethod() {
        return this.method;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    private Member getMember() {
        return this.method != null ? this.method : this.constructor;
    }

    private AnnotatedElement getAnnotatedElement() {
        return this.method != null ? this.method : this.constructor;
    }

    public Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    /* access modifiers changed from: 0000 */
    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getParameterType() {
        if (this.parameterType == null) {
            if (this.parameterIndex < 0) {
                this.parameterType = this.method != null ? this.method.getReturnType() : null;
            } else {
                this.parameterType = this.method != null ? this.method.getParameterTypes()[this.parameterIndex] : this.constructor.getParameterTypes()[this.parameterIndex];
            }
        }
        return this.parameterType;
    }

    public Type getGenericParameterType() {
        if (this.genericParameterType == null) {
            if (this.parameterIndex < 0) {
                this.genericParameterType = this.method != null ? this.method.getGenericReturnType() : null;
            } else {
                this.genericParameterType = this.method != null ? this.method.getGenericParameterTypes()[this.parameterIndex] : this.constructor.getGenericParameterTypes()[this.parameterIndex];
            }
        }
        return this.genericParameterType;
    }

    public Class<?> getNestedParameterType() {
        if (this.nestingLevel <= 1) {
            return getParameterType();
        }
        Type type = getGenericParameterType();
        if (type instanceof ParameterizedType) {
            Integer index = getTypeIndexForCurrentLevel();
            Type arg = ((ParameterizedType) type).getActualTypeArguments()[index != null ? index.intValue() : 0];
            if (arg instanceof Class) {
                return (Class) arg;
            }
            if (arg instanceof ParameterizedType) {
                arg = ((ParameterizedType) arg).getRawType();
                if (arg instanceof Class) {
                    return (Class) arg;
                }
            }
        }
        return Object.class;
    }

    public Annotation[] getMethodAnnotations() {
        return getAnnotatedElement().getAnnotations();
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> annotationType) {
        return getAnnotatedElement().getAnnotation(annotationType);
    }

    public Annotation[] getParameterAnnotations() {
        if (this.parameterAnnotations == null) {
            Annotation[][] annotationArray = this.method != null ? this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations();
            if (this.parameterIndex < 0 || this.parameterIndex >= annotationArray.length) {
                this.parameterAnnotations = new Annotation[0];
            } else {
                this.parameterAnnotations = annotationArray[this.parameterIndex];
            }
        }
        return this.parameterAnnotations;
    }

    public <T extends Annotation> T getParameterAnnotation(Class<T> annotationType) {
        for (Annotation ann : getParameterAnnotations()) {
            if (annotationType.isInstance(ann)) {
                return ann;
            }
        }
        return null;
    }

    public boolean hasParameterAnnotations() {
        return getParameterAnnotations().length != 0;
    }

    public <T extends Annotation> boolean hasParameterAnnotation(Class<T> annotationType) {
        return getParameterAnnotation(annotationType) != null;
    }

    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public String getParameterName() {
        if (this.parameterNameDiscoverer != null) {
            String[] parameterNames = this.method != null ? this.parameterNameDiscoverer.getParameterNames(this.method) : this.parameterNameDiscoverer.getParameterNames(this.constructor);
            if (parameterNames != null) {
                this.parameterName = parameterNames[this.parameterIndex];
            }
            this.parameterNameDiscoverer = null;
        }
        return this.parameterName;
    }

    public void increaseNestingLevel() {
        this.nestingLevel++;
    }

    public void decreaseNestingLevel() {
        getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
        this.nestingLevel--;
    }

    public int getNestingLevel() {
        return this.nestingLevel;
    }

    public void setTypeIndexForCurrentLevel(int typeIndex) {
        getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
    }

    public Integer getTypeIndexForCurrentLevel() {
        return getTypeIndexForLevel(this.nestingLevel);
    }

    public Integer getTypeIndexForLevel(int nestingLevel) {
        return (Integer) getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
    }

    private Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap(4);
        }
        return this.typeIndexesPerLevel;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof MethodParameter)) {
            return false;
        }
        MethodParameter other = (MethodParameter) obj;
        if (this.parameterIndex == other.parameterIndex && getMember().equals(other.getMember())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (getMember().hashCode() * 31) + this.parameterIndex;
    }

    public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
        if (methodOrConstructor instanceof Method) {
            return new MethodParameter((Method) methodOrConstructor, parameterIndex);
        }
        if (methodOrConstructor instanceof Constructor) {
            return new MethodParameter((Constructor) methodOrConstructor, parameterIndex);
        }
        throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
    }
}
