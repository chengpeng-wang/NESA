package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class GenericConversionService implements ConfigurableConversionService {
    private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
    private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
    private final Map<ConverterCacheKey, GenericConverter> converterCache = new ConcurrentHashMap(64);
    private final Converters converters = new Converters();

    private final class ConverterAdapter implements ConditionalGenericConverter {
        private final Converter<Object, Object> converter;
        private final ConvertiblePair typeInfo;

        public ConverterAdapter(Converter<?, ?> converter, ConvertiblePair typeInfo) {
            this.converter = converter;
            this.typeInfo = typeInfo;
        }

        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(this.typeInfo);
        }

        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (!this.typeInfo.getTargetType().equals(targetType.getObjectType())) {
                return false;
            }
            if (this.converter instanceof ConditionalConverter) {
                return ((ConditionalConverter) this.converter).matches(sourceType, targetType);
            }
            return true;
        }

        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null) {
                return GenericConversionService.this.convertNullSource(sourceType, targetType);
            }
            return this.converter.convert(source);
        }

        public String toString() {
            return this.typeInfo + " : " + this.converter;
        }
    }

    private static final class ConverterCacheKey {
        private final TypeDescriptor sourceType;
        private final TypeDescriptor targetType;

        public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ConverterCacheKey)) {
                return false;
            }
            ConverterCacheKey otherKey = (ConverterCacheKey) other;
            if (ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType) && ObjectUtils.nullSafeEquals(this.targetType, otherKey.targetType)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (ObjectUtils.nullSafeHashCode(this.sourceType) * 29) + ObjectUtils.nullSafeHashCode(this.targetType);
        }

        public String toString() {
            return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
        }
    }

    private final class ConverterFactoryAdapter implements ConditionalGenericConverter {
        private final ConverterFactory<Object, Object> converterFactory;
        private final ConvertiblePair typeInfo;

        public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, ConvertiblePair typeInfo) {
            this.converterFactory = converterFactory;
            this.typeInfo = typeInfo;
        }

        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(this.typeInfo);
        }

        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            boolean matches = true;
            if (this.converterFactory instanceof ConditionalConverter) {
                matches = ((ConditionalConverter) this.converterFactory).matches(sourceType, targetType);
            }
            if (!matches) {
                return matches;
            }
            Converter<?, ?> converter = this.converterFactory.getConverter(targetType.getType());
            if (converter instanceof ConditionalConverter) {
                return ((ConditionalConverter) converter).matches(sourceType, targetType);
            }
            return matches;
        }

        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            if (source == null) {
                return GenericConversionService.this.convertNullSource(sourceType, targetType);
            }
            return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
        }

        public String toString() {
            return this.typeInfo + " : " + this.converterFactory;
        }
    }

    private static class Converters {
        private final Map<ConvertiblePair, ConvertersForPair> converters;
        private final Set<GenericConverter> globalConverters;

        private Converters() {
            this.globalConverters = new LinkedHashSet();
            this.converters = new LinkedHashMap(36);
        }

        public void add(GenericConverter converter) {
            Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
            if (convertibleTypes == null) {
                Assert.state(converter instanceof ConditionalConverter, "Only conditional converters may return null convertible types");
                this.globalConverters.add(converter);
                return;
            }
            for (ConvertiblePair convertiblePair : convertibleTypes) {
                getMatchableConverters(convertiblePair).add(converter);
            }
        }

        private ConvertersForPair getMatchableConverters(ConvertiblePair convertiblePair) {
            ConvertersForPair convertersForPair = (ConvertersForPair) this.converters.get(convertiblePair);
            if (convertersForPair != null) {
                return convertersForPair;
            }
            convertersForPair = new ConvertersForPair();
            this.converters.put(convertiblePair, convertersForPair);
            return convertersForPair;
        }

        public void remove(Class<?> sourceType, Class<?> targetType) {
            this.converters.remove(new ConvertiblePair(sourceType, targetType));
        }

        public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
            List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
            List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
            for (Class<?> sourceCandidate : sourceCandidates) {
                for (Class<?> targetCandidate : targetCandidates) {
                    GenericConverter converter = getRegisteredConverter(sourceType, targetType, new ConvertiblePair(sourceCandidate, targetCandidate));
                    if (converter != null) {
                        return converter;
                    }
                }
            }
            return null;
        }

        private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertiblePair) {
            ConvertersForPair convertersForPair = (ConvertersForPair) this.converters.get(convertiblePair);
            if (convertersForPair != null) {
                GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
                if (converter != null) {
                    return converter;
                }
            }
            for (GenericConverter globalConverter : this.globalConverters) {
                if (((ConditionalConverter) globalConverter).matches(sourceType, targetType)) {
                    return globalConverter;
                }
            }
            return null;
        }

        private List<Class<?>> getClassHierarchy(Class<?> type) {
            List<Class<?>> hierarchy = new ArrayList(20);
            Set<Class<?>> visited = new HashSet(20);
            addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
            boolean array = type.isArray();
            for (int i = 0; i < hierarchy.size(); i++) {
                Class<?> candidate = (Class) hierarchy.get(i);
                candidate = array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate);
                Class<?> superclass = candidate.getSuperclass();
                if (!(candidate.getSuperclass() == null || superclass == Object.class)) {
                    addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
                }
                for (Class<?> implementedInterface : candidate.getInterfaces()) {
                    addToClassHierarchy(hierarchy.size(), implementedInterface, array, hierarchy, visited);
                }
            }
            addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
            addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
            return hierarchy;
        }

        private void addToClassHierarchy(int index, Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
            if (asArray) {
                type = Array.newInstance(type, 0).getClass();
            }
            if (visited.add(type)) {
                hierarchy.add(index, type);
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("ConversionService converters =\n");
            for (String converterString : getConverterStrings()) {
                builder.append(9).append(converterString).append(10);
            }
            return builder.toString();
        }

        private List<String> getConverterStrings() {
            List<String> converterStrings = new ArrayList();
            for (ConvertersForPair convertersForPair : this.converters.values()) {
                converterStrings.add(convertersForPair.toString());
            }
            Collections.sort(converterStrings);
            return converterStrings;
        }
    }

    private static class ConvertersForPair {
        private final LinkedList<GenericConverter> converters;

        private ConvertersForPair() {
            this.converters = new LinkedList();
        }

        public void add(GenericConverter converter) {
            this.converters.addFirst(converter);
        }

        public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
            Iterator i$ = this.converters.iterator();
            while (i$.hasNext()) {
                GenericConverter converter = (GenericConverter) i$.next();
                if (!(converter instanceof ConditionalGenericConverter)) {
                    return converter;
                }
                if (((ConditionalGenericConverter) converter).matches(sourceType, targetType)) {
                    return converter;
                }
            }
            return null;
        }

        public String toString() {
            return StringUtils.collectionToCommaDelimitedString(this.converters);
        }
    }

    private static class NoOpConverter implements GenericConverter {
        private final String name;

        public NoOpConverter(String name) {
            this.name = name;
        }

        public Set<ConvertiblePair> getConvertibleTypes() {
            return null;
        }

        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            return source;
        }

        public String toString() {
            return this.name;
        }
    }

    public void addConverter(Converter<?, ?> converter) {
        ConvertiblePair typeInfo = getRequiredTypeInfo(converter, Converter.class);
        Assert.notNull(typeInfo, "Unable to the determine sourceType <S> and targetType <T> which your Converter<S, T> converts between; declare these generic types.");
        addConverter(new ConverterAdapter(converter, typeInfo));
    }

    public void addConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
        addConverter(new ConverterAdapter(converter, new ConvertiblePair(sourceType, targetType)));
    }

    public void addConverter(GenericConverter converter) {
        this.converters.add(converter);
        invalidateCache();
    }

    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory, ConverterFactory.class);
        if (typeInfo == null) {
            throw new IllegalArgumentException("Unable to the determine sourceType <S> and targetRangeType R which your ConverterFactory<S, R> converts between; declare these generic types.");
        }
        addConverter(new ConverterFactoryAdapter(converterFactory, typeInfo));
    }

    public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
        this.converters.remove(sourceType, targetType);
        invalidateCache();
    }

    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        Assert.notNull(targetType, "targetType to convert to cannot be null");
        return canConvert(sourceType != null ? TypeDescriptor.valueOf(sourceType) : null, TypeDescriptor.valueOf(targetType));
    }

    public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Assert.notNull(targetType, "targetType to convert to cannot be null");
        if (sourceType != null && getConverter(sourceType, targetType) == null) {
            return false;
        }
        return true;
    }

    public boolean canBypassConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Assert.notNull(targetType, "The targetType to convert to cannot be null");
        if (sourceType == null || getConverter(sourceType, targetType) == NO_OP_CONVERTER) {
            return true;
        }
        return false;
    }

    public <T> T convert(Object source, Class<T> targetType) {
        Assert.notNull(targetType, "The targetType to convert to cannot be null");
        return convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Assert.notNull(targetType, "The targetType to convert to cannot be null");
        if (sourceType == null) {
            Assert.isTrue(source == null, "The source must be [null] if sourceType == [null]");
            return handleResult(sourceType, targetType, convertNullSource(sourceType, targetType));
        } else if (source == null || sourceType.getObjectType().isInstance(source)) {
            GenericConverter converter = getConverter(sourceType, targetType);
            if (converter != null) {
                return handleResult(sourceType, targetType, ConversionUtils.invokeConverter(converter, source, sourceType, targetType));
            }
            return handleConverterNotFound(source, sourceType, targetType);
        } else {
            throw new IllegalArgumentException("The source to convert from must be an instance of " + sourceType + "; instead it was a " + source.getClass().getName());
        }
    }

    public Object convert(Object source, TypeDescriptor targetType) {
        return convert(source, TypeDescriptor.forObject(source), targetType);
    }

    public String toString() {
        return this.converters.toString();
    }

    /* access modifiers changed from: protected */
    public Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return null;
    }

    /* access modifiers changed from: protected */
    public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
        GenericConverter converter = (GenericConverter) this.converterCache.get(key);
        if (converter == null) {
            converter = this.converters.find(sourceType, targetType);
            if (converter == null) {
                converter = getDefaultConverter(sourceType, targetType);
            }
            if (converter != null) {
                this.converterCache.put(key, converter);
                return converter;
            }
            this.converterCache.put(key, NO_MATCH);
            return null;
        } else if (converter != NO_MATCH) {
            return converter;
        } else {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
    }

    private ConvertiblePair getRequiredTypeInfo(Object converter, Class<?> genericIfc) {
        Class<?>[] args = GenericTypeResolver.resolveTypeArguments(converter.getClass(), genericIfc);
        return args != null ? new ConvertiblePair(args[0], args[1]) : null;
    }

    private void invalidateCache() {
        this.converterCache.clear();
    }

    private Object handleConverterNotFound(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            assertNotPrimitiveTargetType(sourceType, targetType);
        } else if (!(sourceType.isAssignableTo(targetType) && targetType.getObjectType().isInstance(source))) {
            throw new ConverterNotFoundException(sourceType, targetType);
        }
        return source;
    }

    private Object handleResult(TypeDescriptor sourceType, TypeDescriptor targetType, Object result) {
        if (result == null) {
            assertNotPrimitiveTargetType(sourceType, targetType);
        }
        return result;
    }

    private void assertNotPrimitiveTargetType(TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (targetType.isPrimitive()) {
            throw new ConversionFailedException(sourceType, targetType, null, new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
        }
    }
}
