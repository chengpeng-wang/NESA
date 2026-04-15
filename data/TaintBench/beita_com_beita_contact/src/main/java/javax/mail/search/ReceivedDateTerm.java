package javax.mail.search;

import java.util.Date;
import javax.mail.Message;

public final class ReceivedDateTerm extends DateTerm {
    private static final long serialVersionUID = -2756695246195503170L;

    public ReceivedDateTerm(int comparison, Date date) {
        super(comparison, date);
    }

    public boolean match(Message msg) {
        try {
            Date d = msg.getReceivedDate();
            if (d == null) {
                return false;
            }
            return super.match(d);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReceivedDateTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
