package flexjson.transformer;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTransformer extends AbstractTransformer implements ObjectFactory {
    SimpleDateFormat simpleDateFormatter;

    public DateTransformer(String dateFormat) {
        this.simpleDateFormatter = new SimpleDateFormat(dateFormat);
    }

    public void transform(Object value) {
        getContext().writeQuoted(this.simpleDateFormatter.format(value));
    }

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        try {
            return this.simpleDateFormatter.parse(value.toString());
        } catch (ParseException e) {
            throw new JSONException(String.format("Failed to parse %s with %s pattern.", new Object[]{value, this.simpleDateFormatter.toPattern()}), e);
        }
    }
}
