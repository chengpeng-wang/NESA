package flexjson.transformer;

import flexjson.TypeContext;

public class IterableTransformer extends AbstractTransformer {
    public void transform(Object object) {
        Iterable iterable = (Iterable) object;
        TypeContext typeContext = getContext().writeOpenArray();
        for (Object item : iterable) {
            if (!typeContext.isFirst()) {
                getContext().writeComma();
            }
            typeContext.setFirst(false);
            getContext().transform(item);
        }
        getContext().writeCloseArray();
    }
}
