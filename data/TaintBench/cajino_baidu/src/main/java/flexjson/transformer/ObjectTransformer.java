package flexjson.transformer;

import flexjson.BeanAnalyzer;
import flexjson.BeanProperty;
import flexjson.ChainedSet;
import flexjson.JSONContext;
import flexjson.JSONException;
import flexjson.Path;
import flexjson.TypeContext;

public class ObjectTransformer extends AbstractTransformer {
    public void transform(Object object) {
        JSONContext context = getContext();
        Path path = context.getPath();
        ChainedSet visits = context.getVisits();
        try {
            if (!visits.contains(object)) {
                context.setVisits(new ChainedSet(visits));
                context.getVisits().add(object);
                BeanAnalyzer analyzer = BeanAnalyzer.analyze(resolveClass(object));
                TypeContext typeContext = context.writeOpenObject();
                for (BeanProperty prop : analyzer.getProperties()) {
                    String name = prop.getName();
                    path.enqueue(name);
                    if (context.isIncluded(prop)) {
                        Object value = prop.getValue(object);
                        if (!context.getVisits().contains(value)) {
                            TransformerWrapper transformer = (TransformerWrapper) context.getTransformer(value);
                            if (!transformer.isInline().booleanValue()) {
                                if (!typeContext.isFirst()) {
                                    context.writeComma();
                                }
                                typeContext.setFirst(false);
                                context.writeName(name);
                            }
                            typeContext.setPropertyName(name);
                            transformer.transform(value);
                        }
                    }
                    path.pop();
                }
                context.writeCloseObject();
                context.setVisits((ChainedSet) context.getVisits().getParent());
            }
        } catch (JSONException e) {
            throw e;
        } catch (Exception e2) {
            throw new JSONException("Error trying to deepSerialize", e2);
        }
    }

    /* access modifiers changed from: protected */
    public Class resolveClass(Object obj) {
        return obj.getClass();
    }
}
