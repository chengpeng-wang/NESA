package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

final class EnumToStringConverter implements Converter<Enum<?>, String>, ConditionalConverter {
    private final ConversionService conversionService;

    public EnumToStringConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (Class<?> interfaceType : ClassUtils.getAllInterfacesForClass(sourceType.getType())) {
            if (this.conversionService.canConvert(TypeDescriptor.valueOf(interfaceType), targetType)) {
                return false;
            }
        }
        return true;
    }

    public String convert(Enum<?> source) {
        return source.name();
    }
}
