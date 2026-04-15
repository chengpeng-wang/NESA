package org.springframework.core.convert.support;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.util.StringUtils;

final class StringToCollectionConverter implements ConditionalGenericConverter {
    private final ConversionService conversionService;

    public StringToCollectionConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Collection.class));
    }

    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getElementTypeDescriptor() == null || this.conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
    }

    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String[] fields = StringUtils.commaDelimitedListToStringArray((String) source);
        Object target = CollectionFactory.createCollection(targetType.getType(), fields.length);
        if (targetType.getElementTypeDescriptor() == null) {
            for (String field : fields) {
                target.add(field.trim());
            }
            return target;
        }
        for (String field2 : fields) {
            target.add(this.conversionService.convert(field2.trim(), sourceType, targetType.getElementTypeDescriptor()));
        }
        return target;
    }
}
