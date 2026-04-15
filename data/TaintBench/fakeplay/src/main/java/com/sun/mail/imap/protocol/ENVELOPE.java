package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import java.util.Date;
import java.util.Vector;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MailDateFormat;

public class ENVELOPE implements Item {
    private static MailDateFormat mailDateFormat = new MailDateFormat();
    static final char[] name = new char[]{'E', 'N', 'V', 'E', 'L', 'O', 'P', 'E'};
    public InternetAddress[] bcc;
    public InternetAddress[] cc;
    public Date date = null;
    public InternetAddress[] from;
    public String inReplyTo;
    public String messageId;
    public int msgno;
    public InternetAddress[] replyTo;
    public InternetAddress[] sender;
    public String subject;
    public InternetAddress[] to;

    public ENVELOPE(FetchResponse r) throws ParsingException {
        this.msgno = r.getNumber();
        r.skipSpaces();
        if (r.readByte() != (byte) 40) {
            throw new ParsingException("ENVELOPE parse error");
        }
        String s = r.readString();
        if (s != null) {
            try {
                this.date = mailDateFormat.parse(s);
            } catch (Exception e) {
            }
        }
        this.subject = r.readString();
        this.from = parseAddressList(r);
        this.sender = parseAddressList(r);
        this.replyTo = parseAddressList(r);
        this.to = parseAddressList(r);
        this.cc = parseAddressList(r);
        this.bcc = parseAddressList(r);
        this.inReplyTo = r.readString();
        this.messageId = r.readString();
        if (r.readByte() != (byte) 41) {
            throw new ParsingException("ENVELOPE parse error");
        }
    }

    private InternetAddress[] parseAddressList(Response r) throws ParsingException {
        r.skipSpaces();
        byte b = r.readByte();
        if (b == (byte) 40) {
            Vector v = new Vector();
            do {
                IMAPAddress a = new IMAPAddress(r);
                if (!a.isEndOfGroup()) {
                    v.addElement(a);
                }
            } while (r.peekByte() != (byte) 41);
            r.skip(1);
            InternetAddress[] a2 = new InternetAddress[v.size()];
            v.copyInto(a2);
            return a2;
        } else if (b == (byte) 78 || b == (byte) 110) {
            r.skip(2);
            return null;
        } else {
            throw new ParsingException("ADDRESS parse error");
        }
    }
}
