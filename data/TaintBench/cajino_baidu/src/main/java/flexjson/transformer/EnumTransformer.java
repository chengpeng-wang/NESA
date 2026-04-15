package flexjson.transformer;

public class EnumTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().writeQuoted(((Enum) object).name());
    }
}
