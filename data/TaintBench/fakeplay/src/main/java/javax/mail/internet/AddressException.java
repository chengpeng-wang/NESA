package javax.mail.internet;

public class AddressException extends ParseException {
    private static final long serialVersionUID = 9134583443539323120L;
    protected int pos = -1;
    protected String ref = null;

    public AddressException(String s) {
        super(s);
    }

    public AddressException(String s, String ref) {
        super(s);
        this.ref = ref;
    }

    public AddressException(String s, String ref, int pos) {
        super(s);
        this.ref = ref;
        this.pos = pos;
    }

    public String getRef() {
        return this.ref;
    }

    public int getPos() {
        return this.pos;
    }

    public String toString() {
        String s = super.toString();
        if (this.ref == null) {
            return s;
        }
        s = new StringBuilder(String.valueOf(s)).append(" in string ``").append(this.ref).append("''").toString();
        if (this.pos < 0) {
            return s;
        }
        return new StringBuilder(String.valueOf(s)).append(" at position ").append(this.pos).toString();
    }
}
