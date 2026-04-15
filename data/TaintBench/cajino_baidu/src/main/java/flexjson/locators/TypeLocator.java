package flexjson.locators;

import flexjson.ClassLocator;
import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.Path;
import java.util.HashMap;
import java.util.Map;

public class TypeLocator<T> implements ClassLocator {
    private String fieldname;
    private Map<T, Class> types = new HashMap();

    public TypeLocator(String fieldname) {
        this.fieldname = fieldname;
    }

    public TypeLocator add(T value, Class type) {
        this.types.put(value, type);
        return this;
    }

    public Class locate(ObjectBinder context, Path currentPath) throws ClassNotFoundException {
        Map source = context.getSource();
        if (source instanceof Map) {
            return (Class) this.types.get(source.get(this.fieldname));
        }
        throw new JSONException(String.format("%s: Don't know how to locate types for source %s using fieldname %s.  TypeLocator requires the source object be a java.util.Map in order to work.", new Object[]{context.getCurrentPath(), source.getClass(), this.fieldname}));
    }
}
