package org.springframework.core.convert.converter;

import java.util.Comparator;
import java.util.Map.Entry;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.comparator.ComparableComparator;

public class ConvertingComparator<S, T> implements Comparator<S> {
    private Comparator<T> comparator;
    private Converter<S, T> converter;

    private static class ConversionServiceConverter<S, T> implements Converter<S, T> {
        private final ConversionService conversionService;
        private final Class<? extends T> targetType;

        public ConversionServiceConverter(ConversionService conversionService, Class<? extends T> targetType) {
            Assert.notNull(conversionService, "ConversionService must not be null");
            Assert.notNull(targetType, "TargetType must not be null");
            this.conversionService = conversionService;
            this.targetType = targetType;
        }

        public T convert(S source) {
            return this.conversionService.convert(source, this.targetType);
        }
    }

    public ConvertingComparator(Converter<S, T> converter) {
        this(ComparableComparator.INSTANCE, converter);
    }

    public ConvertingComparator(Comparator<T> comparator, Converter<S, T> converter) {
        Assert.notNull(comparator, "Comparator must not be null");
        Assert.notNull(converter, "Converter must not be null");
        this.comparator = comparator;
        this.converter = converter;
    }

    public ConvertingComparator(Comparator<T> comparator, ConversionService conversionService, Class<? extends T> targetType) {
        this(comparator, new ConversionServiceConverter(conversionService, targetType));
    }

    public int compare(S o1, S o2) {
        return this.comparator.compare(this.converter.convert(o1), this.converter.convert(o2));
    }

    public static <K, V> ConvertingComparator<Entry<K, V>, K> mapEntryKeys(Comparator<K> comparator) {
        return new ConvertingComparator(comparator, new Converter<Entry<K, V>, K>() {
            public K convert(Entry<K, V> source) {
                return source.getKey();
            }
        });
    }

    public static <K, V> ConvertingComparator<Entry<K, V>, V> mapEntryValues(Comparator<V> comparator) {
        return new ConvertingComparator(comparator, new Converter<Entry<K, V>, V>() {
            public V convert(Entry<K, V> source) {
                return source.getValue();
            }
        });
    }
}
