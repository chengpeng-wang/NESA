package flexjson.factories;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

public class ArrayObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        List list = (List) value;
        context.getCurrentPath().enqueue("values");
        try {
            Type memberClass = targetClass.getComponentType() != null ? targetClass.getComponentType() : context.findClassAtPath(context.getCurrentPath());
            if (memberClass == null) {
                throw new JSONException("Missing concrete class for array.  You might require a use() method.");
            }
            Object array = Array.newInstance(memberClass, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, context.bind(list.get(i), memberClass));
            }
            context.getCurrentPath().pop();
            return array;
        } catch (ClassNotFoundException ex) {
            throw new JSONException(String.format("%s: Could not find class %s", new Object[]{context.getCurrentPath(), ex.getMessage()}), ex);
        } catch (Throwable th) {
            context.getCurrentPath().pop();
        }
    }
}
