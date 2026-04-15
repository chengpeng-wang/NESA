package javax.mail.search;

import java.util.Locale;
import javax.mail.Message;

public final class HeaderTerm extends StringTerm {
    private static final long serialVersionUID = 8342514650333389122L;
    protected String headerName;

    public HeaderTerm(String headerName, String pattern) {
        super(pattern);
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public boolean match(Message msg) {
        try {
            String[] headers = msg.getHeader(this.headerName);
            if (headers == null) {
                return false;
            }
            for (String match : headers) {
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
        if (!(obj instanceof HeaderTerm)) {
            return false;
        }
        HeaderTerm ht = (HeaderTerm) obj;
        if (ht.headerName.equalsIgnoreCase(this.headerName) && super.equals(ht)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.headerName.toLowerCase(Locale.ENGLISH).hashCode() + super.hashCode();
    }
}
