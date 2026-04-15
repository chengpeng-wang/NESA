package org.springframework.core.convert.converter;

import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

public interface GenericConverter {

    public static final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            Assert.notNull(sourceType, "Source type must not be null");
            Assert.notNull(targetType, "Target type must not be null");
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return this.sourceType;
        }

        public Class<?> getTargetType() {
            return this.targetType;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != ConvertiblePair.class) {
                return false;
            }
            ConvertiblePair other = (ConvertiblePair) obj;
            if (this.sourceType.equals(other.sourceType) && this.targetType.equals(other.targetType)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.sourceType.hashCode() * 31) + this.targetType.hashCode();
        }

        public String toString() {
            return this.sourceType.getName() + " -> " + this.targetType.getName();
        }
    }

    Object convert(Object obj, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    Set<ConvertiblePair> getConvertibleTypes();
}
