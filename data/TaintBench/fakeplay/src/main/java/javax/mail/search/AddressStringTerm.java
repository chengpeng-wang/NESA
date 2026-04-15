package javax.mail.search;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

public abstract class AddressStringTerm extends StringTerm {
    private static final long serialVersionUID = 3086821234204980368L;

    protected AddressStringTerm(String pattern) {
        super(pattern, true);
    }

    /* access modifiers changed from: protected */
    public boolean match(Address a) {
        if (a instanceof InternetAddress) {
            return super.match(((InternetAddress) a).toUnicodeString());
        }
        return super.match(a.toString());
    }

    public boolean equals(Object obj) {
        if (obj instanceof AddressStringTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
