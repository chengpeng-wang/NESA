package javax.mail.search;

import javax.mail.Message;

public final class AndTerm extends SearchTerm {
    private static final long serialVersionUID = -3583274505380989582L;
    protected SearchTerm[] terms;

    public AndTerm(SearchTerm t1, SearchTerm t2) {
        this.terms = new SearchTerm[2];
        this.terms[0] = t1;
        this.terms[1] = t2;
    }

    public AndTerm(SearchTerm[] t) {
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
            if (!match.match(msg)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AndTerm)) {
            return false;
        }
        AndTerm at = (AndTerm) obj;
        if (at.terms.length != this.terms.length) {
            return false;
        }
        for (int i = 0; i < this.terms.length; i++) {
            if (!this.terms[i].equals(at.terms[i])) {
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
