package javax.mail.search;

import javax.mail.Message;

public final class MessageIDTerm extends StringTerm {
    private static final long serialVersionUID = -2121096296454691963L;

    public MessageIDTerm(String msgid) {
        super(msgid);
    }

    public boolean match(Message msg) {
        try {
            String[] s = msg.getHeader("Message-ID");
            if (s == null) {
                return false;
            }
            for (String match : s) {
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
        if (obj instanceof MessageIDTerm) {
            return super.equals(obj);
        }
        return false;
    }
}
