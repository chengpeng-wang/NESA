package org.springframework.core.convert.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

final class ObjectToObjectConverter implements ConditionalGenericConverter {
    ObjectToObjectConverter() {
    }

    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (sourceType.getType().equals(targetType.getType())) {
            return false;
        }
        return hasValueOfMethodOrConstructor(targetType.getType(), sourceType.getType());
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Class<?> sourceClass = sourceType.getType();
        Class<?> targetClass = targetType.getType();
        Method method = getValueOfMethodOn(targetClass, sourceClass);
        if (method != null) {
            try {
                ReflectionUtils.makeAccessible(method);
                return method.invoke(null, new Object[]{source});
            } catch (InvocationTargetException ex) {
                throw new ConversionFailedException(sourceType, targetType, source, ex.getTargetException());
            } catch (Throwable ex2) {
                ConversionFailedException conversionFailedException = new ConversionFailedException(sourceType, targetType, source, ex2);
            }
        } else {
            Constructor<?> constructor = getConstructor(targetClass, sourceClass);
            if (constructor != null) {
                return constructor.newInstance(new Object[]{source});
            }
            throw new IllegalStateException("No static valueOf(" + sourceClass.getName() + ") method or Constructor(" + sourceClass.getName() + ") exists on " + targetClass.getName());
        }
    }

    static boolean hasValueOfMethodOrConstructor(Class<?> clazz, Class<?> sourceParameterType) {
        return (getValueOfMethodOn(clazz, sourceParameterType) == null && getConstructor(clazz, sourceParameterType) == null) ? false : true;
    }

    private static Method getValueOfMethodOn(Class<?> clazz, Class<?> sourceParameterType) {
        return ClassUtils.getStaticMethod(clazz, "valueOf", sourceParameterType);
    }

    private static Constructor<?> getConstructor(Class<?> clazz, Class<?> sourceParameterType) {
        return ClassUtils.getConstructorIfAvailable(clazz, sourceParameterType);
    }
}
