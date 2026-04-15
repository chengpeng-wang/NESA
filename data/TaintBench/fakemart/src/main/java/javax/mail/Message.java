package javax.mail;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Date;
import javax.mail.Flags.Flag;
import javax.mail.search.SearchTerm;

public abstract class Message implements Part {
    protected boolean expunged = false;
    protected Folder folder = null;
    protected int msgnum = 0;
    protected Session session = null;

    public static class RecipientType implements Serializable {
        public static final RecipientType BCC = new RecipientType("Bcc");
        public static final RecipientType CC = new RecipientType("Cc");
        public static final RecipientType TO = new RecipientType("To");
        private static final long serialVersionUID = -7479791750606340008L;
        protected String type;

        protected RecipientType(String type) {
            this.type = type;
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("To")) {
                return TO;
            }
            if (this.type.equals("Cc")) {
                return CC;
            }
            if (this.type.equals("Bcc")) {
                return BCC;
            }
            throw new InvalidObjectException("Attempt to resolve unknown RecipientType: " + this.type);
        }

        public String toString() {
            return this.type;
        }
    }

    public abstract void addFrom(Address[] addressArr) throws MessagingException;

    public abstract void addRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract Flags getFlags() throws MessagingException;

    public abstract Address[] getFrom() throws MessagingException;

    public abstract Date getReceivedDate() throws MessagingException;

    public abstract Address[] getRecipients(RecipientType recipientType) throws MessagingException;

    public abstract Date getSentDate() throws MessagingException;

    public abstract String getSubject() throws MessagingException;

    public abstract Message reply(boolean z) throws MessagingException;

    public abstract void saveChanges() throws MessagingException;

    public abstract void setFlags(Flags flags, boolean z) throws MessagingException;

    public abstract void setFrom() throws MessagingException;

    public abstract void setFrom(Address address) throws MessagingException;

    public abstract void setRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract void setSentDate(Date date) throws MessagingException;

    public abstract void setSubject(String str) throws MessagingException;

    protected Message() {
    }

    protected Message(Folder folder, int msgnum) {
        this.folder = folder;
        this.msgnum = msgnum;
        this.session = folder.store.session;
    }

    protected Message(Session session) {
        this.session = session;
    }

    public Address[] getAllRecipients() throws MessagingException {
        Address[] to = getRecipients(RecipientType.TO);
        Address[] cc = getRecipients(RecipientType.CC);
        Address[] bcc = getRecipients(RecipientType.BCC);
        if (cc == null && bcc == null) {
            return to;
        }
        int length;
        int length2;
        if (to != null) {
            length = to.length;
        } else {
            length = 0;
        }
        if (cc != null) {
            length2 = cc.length;
        } else {
            length2 = 0;
        }
        length += length2;
        if (bcc != null) {
            length2 = bcc.length;
        } else {
            length2 = 0;
        }
        Address[] addresses = new Address[(length + length2)];
        int pos = 0;
        if (to != null) {
            System.arraycopy(to, 0, addresses, 0, to.length);
            pos = 0 + to.length;
        }
        if (cc != null) {
            System.arraycopy(cc, 0, addresses, pos, cc.length);
            pos += cc.length;
        }
        if (bcc != null) {
            System.arraycopy(bcc, 0, addresses, pos, bcc.length);
            pos += bcc.length;
        }
        return addresses;
    }

    public void setRecipient(RecipientType type, Address address) throws MessagingException {
        setRecipients(type, new Address[]{address});
    }

    public void addRecipient(RecipientType type, Address address) throws MessagingException {
        addRecipients(type, new Address[]{address});
    }

    public Address[] getReplyTo() throws MessagingException {
        return getFrom();
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        throw new MethodNotSupportedException("setReplyTo not supported");
    }

    public boolean isSet(Flag flag) throws MessagingException {
        return getFlags().contains(flag);
    }

    public void setFlag(Flag flag, boolean set) throws MessagingException {
        setFlags(new Flags(flag), set);
    }

    public int getMessageNumber() {
        return this.msgnum;
    }

    /* access modifiers changed from: protected */
    public void setMessageNumber(int msgnum) {
        this.msgnum = msgnum;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public boolean isExpunged() {
        return this.expunged;
    }

    /* access modifiers changed from: protected */
    public void setExpunged(boolean expunged) {
        this.expunged = expunged;
    }

    public boolean match(SearchTerm term) throws MessagingException {
        return term.match(this);
    }
}
