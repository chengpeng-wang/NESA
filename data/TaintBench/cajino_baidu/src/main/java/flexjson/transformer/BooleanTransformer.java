package flexjson.transformer;

public class BooleanTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().write(((Boolean) object).booleanValue() ? "true" : "false");
    }
}
