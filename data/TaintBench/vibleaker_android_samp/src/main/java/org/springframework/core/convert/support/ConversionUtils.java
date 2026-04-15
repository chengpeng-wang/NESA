package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

abstract class ConversionUtils {
    ConversionUtils() {
    }

    public static Object invokeConverter(GenericConverter converter, Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            return converter.convert(source, sourceType, targetType);
        } catch (ConversionFailedException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new ConversionFailedException(sourceType, targetType, source, ex2);
        }
    }

    public static boolean canConvertElements(TypeDescriptor sourceElementType, TypeDescriptor targetElementType, ConversionService conversionService) {
        if (targetElementType == null || sourceElementType == null || conversionService.canConvert(sourceElementType, targetElementType) || sourceElementType.getType().isAssignableFrom(targetElementType.getType())) {
            return true;
        }
        return false;
    }
}
