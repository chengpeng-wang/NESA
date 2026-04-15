package flexjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JSONParameterizedType implements ParameterizedType {
    private Class clazz;
    private Type[] types;

    public JSONParameterizedType(Class clazz, Type... types) {
        this.clazz = clazz;
        this.types = types;
    }

    public Type[] getActualTypeArguments() {
        return this.types;
    }

    public Type getRawType() {
        return this.clazz;
    }

    public Type getOwnerType() {
        return this.clazz;
    }
}
