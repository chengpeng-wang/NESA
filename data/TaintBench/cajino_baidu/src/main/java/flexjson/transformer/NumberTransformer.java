package flexjson.transformer;

public class NumberTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().write(object.toString());
    }
}
