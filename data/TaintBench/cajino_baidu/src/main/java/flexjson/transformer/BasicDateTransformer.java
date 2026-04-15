package flexjson.transformer;

import java.util.Date;

public class BasicDateTransformer extends AbstractTransformer {
    public void transform(Object object) {
        getContext().write(String.valueOf(((Date) object).getTime()));
    }
}
