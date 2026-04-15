package flexjson.transformer;

public class TransformerWrapper extends AbstractTransformer {
    protected Boolean isInterceptorTransformer = Boolean.FALSE;
    protected Transformer transformer;

    public TransformerWrapper(Transformer transformer) {
        this.transformer = transformer;
    }

    public void transform(Object object) {
        getContext().getObjectStack().addFirst(object);
        this.transformer.transform(object);
        getContext().getObjectStack().removeFirst();
    }

    public Boolean isInline() {
        boolean z = (this.transformer instanceof Inline) && ((Inline) this.transformer).isInline().booleanValue();
        return Boolean.valueOf(z);
    }
}
