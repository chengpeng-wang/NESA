package flexjson;

import java.lang.reflect.Type;

public interface ObjectFactory {
    Object instantiate(ObjectBinder objectBinder, Object obj, Type type, Class cls);
}
