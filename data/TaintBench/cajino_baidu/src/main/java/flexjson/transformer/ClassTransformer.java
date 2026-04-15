package flexjson.transformer;

public class ClassTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().writeQuoted(((Class) object).getName());
    }
}
