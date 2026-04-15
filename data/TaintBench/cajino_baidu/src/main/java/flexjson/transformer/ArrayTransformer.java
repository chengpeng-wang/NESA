package flexjson.transformer;

import flexjson.TypeContext;
import java.lang.reflect.Array;

public class ArrayTransformer extends AbstractTransformer {
    public void transform(Object object) {
        TypeContext typeContext = getContext().writeOpenArray();
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            if (!typeContext.isFirst()) {
                getContext().writeComma();
            }
            typeContext.setFirst(false);
            getContext().transform(Array.get(object, i));
        }
        getContext().writeCloseArray();
    }
}
