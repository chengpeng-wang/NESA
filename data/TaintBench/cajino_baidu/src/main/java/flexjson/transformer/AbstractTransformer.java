package flexjson.transformer;

import flexjson.JSONContext;

public abstract class AbstractTransformer implements Transformer, Inline {
    public JSONContext getContext() {
        return JSONContext.get();
    }

    public Boolean isInline() {
        return Boolean.valueOf(false);
    }
}
