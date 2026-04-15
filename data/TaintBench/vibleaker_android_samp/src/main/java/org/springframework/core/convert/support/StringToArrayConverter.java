package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.StringUtils;

final class StringToArrayConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public StringToArrayConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Object[].class));
    }

    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String[] fields = StringUtils.commaDelimitedListToStringArray((String) source);
        Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), fields.length);
        for (int i = 0; i < fields.length; i++) {
            Array.set(target, i, this.conversionService.convert(fields[i].trim(), sourceType, targetType.getElementTypeDescriptor()));
        }
        return target;
    }
}
