package flexjson.factories;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;

public class EnumObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof String) {
            return Enum.valueOf((Class) targetType, value.toString());
        }
        throw new JSONException(String.format("%s:  Don't know how to convert %s to enumerated constant of %s", new Object[]{context.getCurrentPath(), value, targetType}));
    }
}
