package javax.mail.search;

import javax.mail.Message;

public final class SizeTerm extends IntegerComparisonTerm {
    private static final long serialVersionUID = -2556219451005103709L;

    public SizeTerm(int comparison, int size) {
        super(comparison, size);
    }

    public boolean match(Message msg) {
        try {
            int size = msg.getSize();
            if (size == -1) {
                return false;
            }
            return super.match(size);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof SizeTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
