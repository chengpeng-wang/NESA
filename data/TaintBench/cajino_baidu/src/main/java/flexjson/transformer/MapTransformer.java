package flexjson.transformer;

import flexjson.JSONContext;
import flexjson.Path;
import flexjson.TypeContext;
import java.util.Map;

public class MapTransformer extends AbstractTransformer {
    public void transform(Object object) {
        JSONContext context = getContext();
        Path path = context.getPath();
        Map value = (Map) object;
        TypeContext typeContext = getContext().writeOpenObject();
        for (Object key : value.keySet()) {
            String obj;
            path.enqueue(key != null ? key.toString() : null);
            if (key != null) {
                obj = key.toString();
            } else {
                obj = null;
            }
            if (context.isIncluded(obj, value.get(key))) {
                TransformerWrapper transformer = (TransformerWrapper) context.getTransformer(value.get(key));
                if (!transformer.isInline().booleanValue()) {
                    if (!typeContext.isFirst()) {
                        getContext().writeComma();
                    }
                    typeContext.setFirst(false);
                    if (key != null) {
                        getContext().writeName(key.toString());
                    } else {
                        getContext().writeName(null);
                    }
                }
                if (key != null) {
                    typeContext.setPropertyName(key.toString());
                } else {
                    typeContext.setPropertyName(null);
                }
                transformer.transform(value.get(key));
            }
            path.pop();
        }
        getContext().writeCloseObject();
    }
}
