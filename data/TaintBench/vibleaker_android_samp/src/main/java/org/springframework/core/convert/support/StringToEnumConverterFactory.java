package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    private class StringToEnum<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            if (source.length() == 0) {
                return null;
            }
            return Enum.valueOf(this.enumType, source.trim());
        }
    }

    StringToEnumConverterFactory() {
    }

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        if (enumType != null) {
            return new StringToEnum(enumType);
        }
        throw new IllegalArgumentException("The target type " + targetType.getName() + " does not refer to an enum");
    }
}
