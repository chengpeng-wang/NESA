package flexjson.transformer;

public class CharacterTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().writeQuoted(object.toString());
    }
}
