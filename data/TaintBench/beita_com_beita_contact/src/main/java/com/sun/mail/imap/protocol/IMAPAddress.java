package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import java.util.Vector;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/* compiled from: ENVELOPE */
class IMAPAddress extends InternetAddress {
    private static final long serialVersionUID = -3835822029483122232L;
    private boolean group = false;
    private InternetAddress[] grouplist;
    private String groupname;

    IMAPAddress(Response r) throws ParsingException {
        r.skipSpaces();
        if (r.readByte() != (byte) 40) {
            throw new ParsingException("ADDRESS parse error");
        }
        this.encodedPersonal = r.readString();
        r.readString();
        String mb = r.readString();
        String host = r.readString();
        if (r.readByte() != (byte) 41) {
            throw new ParsingException("ADDRESS parse error");
        } else if (host == null) {
            this.group = true;
            this.groupname = mb;
            if (this.groupname != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(this.groupname).append(':');
                Vector v = new Vector();
                while (r.peekByte() != (byte) 41) {
                    IMAPAddress a = new IMAPAddress(r);
                    if (a.isEndOfGroup()) {
                        break;
                    }
                    if (v.size() != 0) {
                        sb.append(',');
                    }
                    sb.append(a.toString());
                    v.addElement(a);
                }
                sb.append(';');
                this.address = sb.toString();
                this.grouplist = new IMAPAddress[v.size()];
                v.copyInto(this.grouplist);
            }
        } else if (mb == null || mb.length() == 0) {
            this.address = host;
        } else if (host.length() == 0) {
            this.address = mb;
        } else {
            this.address = new StringBuilder(String.valueOf(mb)).append("@").append(host).toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isEndOfGroup() {
        return this.group && this.groupname == null;
    }

    public boolean isGroup() {
        return this.group;
    }

    public InternetAddress[] getGroup(boolean strict) throws AddressException {
        if (this.grouplist == null) {
            return null;
        }
        return (InternetAddress[]) this.grouplist.clone();
    }
}
