package flexjson.transformer;

import java.util.Arrays;
import java.util.HashMap;

public class TypeTransformerMap extends HashMap<Class, Transformer> {
    private TypeTransformerMap parentTransformerMap;

    public TypeTransformerMap(TypeTransformerMap parentTransformerMap) {
        this.parentTransformerMap = parentTransformerMap;
    }

    public Transformer getTransformer(Object key) {
        Transformer transformer = findTransformer(key == null ? Void.TYPE : key.getClass(), key == null ? Void.TYPE : key.getClass());
        if (transformer == null && this.parentTransformerMap != null) {
            transformer = this.parentTransformerMap.getTransformer(key);
            if (transformer != null) {
                updateTransformers(key == null ? Void.TYPE : key.getClass(), transformer);
            }
        }
        return transformer;
    }

    /* access modifiers changed from: 0000 */
    public Transformer findTransformer(Class key, Class originalKey) {
        if (key == null) {
            return null;
        }
        if (containsKey(key)) {
            if (key != originalKey) {
                return updateTransformers(originalKey, (Transformer) get(key));
            }
            return (Transformer) get(key);
        } else if (key.isArray()) {
            return updateTransformers(originalKey, (Transformer) get(Arrays.class));
        } else {
            for (Class interfaze : key.getInterfaces()) {
                if (containsKey(interfaze)) {
                    return updateTransformers(originalKey, (Transformer) get(interfaze));
                }
                Transformer t = findTransformer(interfaze, originalKey);
                if (t != null) {
                    return t;
                }
            }
            return findTransformer(key.getSuperclass(), originalKey);
        }
    }

    private Transformer updateTransformers(Class key, Transformer transformer) {
        if (transformer != null) {
            put(key, transformer);
        }
        return transformer;
    }
}
