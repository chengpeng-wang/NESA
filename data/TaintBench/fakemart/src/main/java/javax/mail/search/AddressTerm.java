package javax.mail.search;

import javax.mail.Address;

public abstract class AddressTerm extends SearchTerm {
    private static final long serialVersionUID = 2005405551929769980L;
    protected Address address;

    protected AddressTerm(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    /* access modifiers changed from: protected */
    public boolean match(Address a) {
        return a.equals(this.address);
    }

    public boolean equals(Object obj) {
        if (obj instanceof AddressTerm) {
            return ((AddressTerm) obj).address.equals(this.address);
        }
        return false;
    }

    public int hashCode() {
        return this.address.hashCode();
    }
}
