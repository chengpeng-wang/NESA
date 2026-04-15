package javax.mail.search;

import java.util.Date;
import javax.mail.Message;

public final class SentDateTerm extends DateTerm {
    private static final long serialVersionUID = 5647755030530907263L;

    public SentDateTerm(int comparison, Date date) {
        super(comparison, date);
    }

    public boolean match(Message msg) {
        try {
            Date d = msg.getSentDate();
            if (d == null) {
                return false;
            }
            return super.match(d);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof SentDateTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
