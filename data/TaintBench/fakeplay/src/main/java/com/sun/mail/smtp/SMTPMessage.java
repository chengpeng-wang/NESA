package com.sun.mail.smtp;

import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class SMTPMessage extends MimeMessage {
    public static final int NOTIFY_DELAY = 4;
    public static final int NOTIFY_FAILURE = 2;
    public static final int NOTIFY_NEVER = -1;
    public static final int NOTIFY_SUCCESS = 1;
    public static final int RETURN_FULL = 1;
    public static final int RETURN_HDRS = 2;
    private static final String[] returnOptionString;
    private boolean allow8bitMIME = false;
    private String envelopeFrom;
    private String extension = null;
    private int notifyOptions = 0;
    private int returnOption = 0;
    private boolean sendPartial = false;
    private String submitter = null;

    static {
        String[] strArr = new String[3];
        strArr[1] = "FULL";
        strArr[2] = "HDRS";
        returnOptionString = strArr;
    }

    public SMTPMessage(Session session) {
        super(session);
    }

    public SMTPMessage(Session session, InputStream is) throws MessagingException {
        super(session, is);
    }

    public SMTPMessage(MimeMessage source) throws MessagingException {
        super(source);
    }

    public void setEnvelopeFrom(String from) {
        this.envelopeFrom = from;
    }

    public String getEnvelopeFrom() {
        return this.envelopeFrom;
    }

    public void setNotifyOptions(int options) {
        if (options < -1 || options >= 8) {
            throw new IllegalArgumentException("Bad return option");
        }
        this.notifyOptions = options;
    }

    public int getNotifyOptions() {
        return this.notifyOptions;
    }

    /* access modifiers changed from: 0000 */
    public String getDSNNotify() {
        if (this.notifyOptions == 0) {
            return null;
        }
        if (this.notifyOptions == -1) {
            return "NEVER";
        }
        StringBuffer sb = new StringBuffer();
        if ((this.notifyOptions & 1) != 0) {
            sb.append("SUCCESS");
        }
        if ((this.notifyOptions & 2) != 0) {
            if (sb.length() != 0) {
                sb.append(',');
            }
            sb.append("FAILURE");
        }
        if ((this.notifyOptions & 4) != 0) {
            if (sb.length() != 0) {
                sb.append(',');
            }
            sb.append("DELAY");
        }
        return sb.toString();
    }

    public void setReturnOption(int option) {
        if (option < 0 || option > 2) {
            throw new IllegalArgumentException("Bad return option");
        }
        this.returnOption = option;
    }

    public int getReturnOption() {
        return this.returnOption;
    }

    /* access modifiers changed from: 0000 */
    public String getDSNRet() {
        return returnOptionString[this.returnOption];
    }

    public void setAllow8bitMIME(boolean allow) {
        this.allow8bitMIME = allow;
    }

    public boolean getAllow8bitMIME() {
        return this.allow8bitMIME;
    }

    public void setSendPartial(boolean partial) {
        this.sendPartial = partial;
    }

    public boolean getSendPartial() {
        return this.sendPartial;
    }

    public String getSubmitter() {
        return this.submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getMailExtension() {
        return this.extension;
    }

    public void setMailExtension(String extension) {
        this.extension = extension;
    }
}
