package flexjson.factories;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (targetType == null || !(targetType instanceof ParameterizedType)) {
            return context.bindIntoMap((Map) value, new HashMap(), null, null);
        }
        ParameterizedType ptype = (ParameterizedType) targetType;
        return context.bindIntoMap((Map) value, new HashMap(), ptype.getActualTypeArguments()[0], ptype.getActualTypeArguments()[1]);
    }
}
