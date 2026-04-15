package org.springframework.core.convert;

public interface ConversionService {
    boolean canConvert(Class<?> cls, Class<?> cls2);

    boolean canConvert(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    <T> T convert(Object obj, Class<T> cls);

    Object convert(Object obj, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
