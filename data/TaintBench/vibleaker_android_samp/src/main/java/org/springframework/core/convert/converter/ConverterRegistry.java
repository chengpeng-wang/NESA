package org.springframework.core.convert.converter;

public interface ConverterRegistry {
    void addConverter(Class<?> cls, Class<?> cls2, Converter<?, ?> converter);

    void addConverter(Converter<?, ?> converter);

    void addConverter(GenericConverter genericConverter);

    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

    void removeConvertible(Class<?> cls, Class<?> cls2);
}
