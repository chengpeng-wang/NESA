package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;

public final class FromTerm extends AddressTerm {
    private static final long serialVersionUID = 5214730291502658665L;

    public FromTerm(Address address) {
        super(address);
    }

    public boolean match(Message msg) {
        try {
            Address[] from = msg.getFrom();
            if (from == null) {
                return false;
            }
            for (Address match : from) {
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
        if (obj instanceof FromTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
