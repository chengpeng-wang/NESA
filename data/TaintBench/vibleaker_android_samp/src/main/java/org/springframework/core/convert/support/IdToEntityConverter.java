package org.springframework.core.convert.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

final class IdToEntityConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public IdToEntityConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Method finder = getFinder(targetType.getType());
        if (finder == null || !this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]))) {
            return false;
        }
        return true;
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Method finder = getFinder(targetType.getType());
        Object id = this.conversionService.convert(source, sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
        return ReflectionUtils.invokeMethod(finder, source, id);
    }

    private Method getFinder(Class<?> entityClass) {
        String finderMethod = "find" + getEntityName(entityClass);
        Method[] methods;
        boolean localOnlyFiltered;
        try {
            methods = entityClass.getDeclaredMethods();
            localOnlyFiltered = true;
        } catch (SecurityException e) {
            methods = entityClass.getMethods();
            localOnlyFiltered = false;
        }
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(finderMethod) && method.getParameterTypes().length == 1 && method.getReturnType().equals(entityClass) && (localOnlyFiltered || method.getDeclaringClass().equals(entityClass))) {
                return method;
            }
        }
        return null;
    }

    private String getEntityName(Class<?> entityClass) {
        String shortName = ClassUtils.getShortName((Class) entityClass);
        int lastDot = shortName.lastIndexOf(46);
        if (lastDot != -1) {
            return shortName.substring(lastDot + 1);
        }
        return shortName;
    }
}
