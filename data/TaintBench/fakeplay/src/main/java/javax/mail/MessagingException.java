package javax.mail;

import com.googleprojects.mm.MMMailContentUtil;

public class MessagingException extends Exception {
    private static final long serialVersionUID = -7569192289819959253L;
    private Exception next;

    public MessagingException() {
        initCause(null);
    }

    public MessagingException(String s) {
        super(s);
        initCause(null);
    }

    public MessagingException(String s, Exception e) {
        super(s);
        this.next = e;
        initCause(null);
    }

    public synchronized Exception getNextException() {
        return this.next;
    }

    public synchronized Throwable getCause() {
        return this.next;
    }

    public synchronized boolean setNextException(Exception ex) {
        boolean z;
        Exception theEnd = this;
        while ((theEnd instanceof MessagingException) && ((MessagingException) theEnd).next != null) {
            theEnd = ((MessagingException) theEnd).next;
        }
        if (theEnd instanceof MessagingException) {
            ((MessagingException) theEnd).next = ex;
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized String toString() {
        String s;
        s = super.toString();
        Exception n = this.next;
        if (n != null) {
            if (s == null) {
                s = MMMailContentUtil.MM_MESSAGE_SUBJECT;
            }
            StringBuffer sb = new StringBuffer(s);
            while (n != null) {
                sb.append(";\n  nested exception is:\n\t");
                if (n instanceof MessagingException) {
                    MessagingException mex = (MessagingException) n;
                    sb.append(mex.superToString());
                    n = mex.next;
                } else {
                    sb.append(n.toString());
                    n = null;
                }
            }
            s = sb.toString();
        }
        return s;
    }

    private final String superToString() {
        return super.toString();
    }
}
