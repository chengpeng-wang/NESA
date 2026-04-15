package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;

public final class RecipientTerm extends AddressTerm {
    private static final long serialVersionUID = 6548700653122680468L;
    protected RecipientType type;

    public RecipientTerm(RecipientType type, Address address) {
        super(address);
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
        if ((obj instanceof RecipientTerm) && ((RecipientTerm) obj).type.equals(this.type) && super.equals(obj)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.type.hashCode() + super.hashCode();
    }
}
