package javax.mail.search;

import javax.mail.Message;

public final class NotTerm extends SearchTerm {
    private static final long serialVersionUID = 7152293214217310216L;
    protected SearchTerm term;

    public NotTerm(SearchTerm t) {
        this.term = t;
    }

    public SearchTerm getTerm() {
        return this.term;
    }

    public boolean match(Message msg) {
        return !this.term.match(msg);
    }

    public boolean equals(Object obj) {
        if (obj instanceof NotTerm) {
            return ((NotTerm) obj).term.equals(this.term);
        }
        return false;
    }

    public int hashCode() {
        return this.term.hashCode() << 1;
    }
}
