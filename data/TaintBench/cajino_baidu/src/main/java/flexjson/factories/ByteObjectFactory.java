package flexjson.factories;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;

public class ByteObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        throw context.cannotConvertValueToTargetType(value, Byte.class);
    }
}
