package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.GenericCollectionTypeResolver;

class FieldDescriptor extends AbstractDescriptor {
    private final Field field;
    private final int nestingLevel;
    private Map<Integer, Integer> typeIndexesPerLevel;

    public FieldDescriptor(Field field) {
        super(field.getType());
        this.field = field;
        this.nestingLevel = 1;
    }

    private FieldDescriptor(Class<?> type, Field field, int nestingLevel, int typeIndex, Map<Integer, Integer> typeIndexesPerLevel) {
        super(type);
        this.field = field;
        this.nestingLevel = nestingLevel;
        this.typeIndexesPerLevel = typeIndexesPerLevel;
        this.typeIndexesPerLevel.put(Integer.valueOf(nestingLevel), Integer.valueOf(typeIndex));
    }

    public Annotation[] getAnnotations() {
        return TypeDescriptor.nullSafeAnnotations(this.field.getAnnotations());
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveCollectionElementType() {
        return GenericCollectionTypeResolver.getCollectionFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapKeyType() {
        return GenericCollectionTypeResolver.getMapKeyFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveMapValueType() {
        return GenericCollectionTypeResolver.getMapValueFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
    }

    /* access modifiers changed from: protected */
    public AbstractDescriptor nested(Class<?> type, int typeIndex) {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap(4);
        }
        return new FieldDescriptor(type, this.field, this.nestingLevel + 1, typeIndex, this.typeIndexesPerLevel);
    }
}
