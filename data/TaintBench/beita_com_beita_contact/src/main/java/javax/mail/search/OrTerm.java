package javax.mail.search;

import javax.mail.Message;

public final class OrTerm extends SearchTerm {
    private static final long serialVersionUID = 5380534067523646936L;
    protected SearchTerm[] terms;

    public OrTerm(SearchTerm t1, SearchTerm t2) {
        this.terms = new SearchTerm[2];
        this.terms[0] = t1;
        this.terms[1] = t2;
    }

    public OrTerm(SearchTerm[] t) {
        this.terms = new SearchTerm[t.length];
        for (int i = 0; i < t.length; i++) {
            this.terms[i] = t[i];
        }
    }

    public SearchTerm[] getTerms() {
        return (SearchTerm[]) this.terms.clone();
    }

    public boolean match(Message msg) {
        for (SearchTerm match : this.terms) {
            if (match.match(msg)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OrTerm)) {
            return false;
        }
        OrTerm ot = (OrTerm) obj;
        if (ot.terms.length != this.terms.length) {
            return false;
        }
        for (int i = 0; i < this.terms.length; i++) {
            if (!this.terms[i].equals(ot.terms[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (Object hashCode : this.terms) {
            hash += hashCode.hashCode();
        }
        return hash;
    }
}
