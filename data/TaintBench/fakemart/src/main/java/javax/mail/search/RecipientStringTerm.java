package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;

public final class RecipientStringTerm extends AddressStringTerm {
    private static final long serialVersionUID = -8293562089611618849L;
    private RecipientType type;

    public RecipientStringTerm(RecipientType type, String pattern) {
        super(pattern);
        this.type = type;
    }

    public RecipientType getRecipientType() {
        return this.type;
    }

    public boolean match(Message msg) {
        try {
            Address[] recipients = msg.getRecipients(this.type);
            if (recipients == null) {
                return false;
            }
            for (Address match : recipients) {
                if (super.match(match)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if ((obj instanceof RecipientStringTerm) && ((RecipientStringTerm) obj).type.equals(this.type) && super.equals(obj)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.type.hashCode() + super.hashCode();
    }
}
