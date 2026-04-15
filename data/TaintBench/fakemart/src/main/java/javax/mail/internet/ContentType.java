package javax.mail.internet;

import javax.mail.internet.HeaderTokenizer.Token;

public class ContentType {
    private ParameterList list;
    private String primaryType;
    private String subType;

    public ContentType(String primaryType, String subType, ParameterList list) {
        this.primaryType = primaryType;
        this.subType = subType;
        this.list = list;
    }

    public ContentType(String s) throws ParseException {
        HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
        Token tk = h.next();
        if (tk.getType() != -1) {
            throw new ParseException();
        }
        this.primaryType = tk.getValue();
        if (((char) h.next().getType()) != '/') {
            throw new ParseException();
        }
        tk = h.next();
        if (tk.getType() != -1) {
            throw new ParseException();
        }
        this.subType = tk.getValue();
        String rem = h.getRemainder();
        if (rem != null) {
            this.list = new ParameterList(rem);
        }
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public String getBaseType() {
        return this.primaryType + '/' + this.subType;
    }

    public String getParameter(String name) {
        if (this.list == null) {
            return null;
        }
        return this.list.get(name);
    }

    public ParameterList getParameterList() {
        return this.list;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void setParameter(String name, String value) {
        if (this.list == null) {
            this.list = new ParameterList();
        }
        this.list.set(name, value);
    }

    public void setParameterList(ParameterList list) {
        this.list = list;
    }

    public String toString() {
        if (this.primaryType == null || this.subType == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(this.primaryType).append('/').append(this.subType);
        if (this.list != null) {
            sb.append(this.list.toString(sb.length() + 14));
        }
        return sb.toString();
    }

    public boolean match(ContentType cType) {
        if (!this.primaryType.equalsIgnoreCase(cType.getPrimaryType())) {
            return false;
        }
        String sType = cType.getSubType();
        if (this.subType.charAt(0) == '*' || sType.charAt(0) == '*') {
            return true;
        }
        if (this.subType.equalsIgnoreCase(sType)) {
            return true;
        }
        return false;
    }

    public boolean match(String s) {
        try {
            return match(new ContentType(s));
        } catch (ParseException e) {
            return false;
        }
    }
}
