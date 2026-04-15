package flexjson.factories;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class ListObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof Collection) {
            return context.bindIntoCollection((Collection) value, new ArrayList(), targetType);
        }
        Object set = new ArrayList();
        set.add(context.bind(value));
        return set;
    }
}
