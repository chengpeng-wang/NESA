package org.apache.log4j.net;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;

public class SMTPAppender extends AppenderSkeleton implements UnrecognizedElementHandler {
    static Class class$org$apache$log4j$spi$TriggeringEventEvaluator;
    private String bcc;
    private int bufferSize;
    protected CyclicBuffer cb;
    private String cc;
    protected TriggeringEventEvaluator evaluator;
    private String from;
    private boolean locationInfo;
    protected Message msg;
    private String replyTo;
    private boolean sendOnClose;
    private boolean smtpDebug;
    private String smtpHost;
    private String smtpPassword;
    private int smtpPort;
    private String smtpProtocol;
    private String smtpUsername;
    private String subject;
    private String to;

    static String access$000(SMTPAppender x0) {
        return x0.smtpUsername;
    }

    static String access$100(SMTPAppender x0) {
        return x0.smtpPassword;
    }

    public SMTPAppender() {
        this(new DefaultEvaluator());
    }

    public SMTPAppender(TriggeringEventEvaluator evaluator) {
        this.smtpPort = -1;
        this.smtpDebug = false;
        this.bufferSize = 512;
        this.locationInfo = false;
        this.sendOnClose = false;
        this.cb = new CyclicBuffer(this.bufferSize);
        this.evaluator = evaluator;
    }

    public void activateOptions() {
        this.msg = new MimeMessage(createSession());
        try {
            addressMessage(this.msg);
            if (this.subject != null) {
                try {
                    this.msg.setSubject(MimeUtility.encodeText(this.subject, "UTF-8", null));
                } catch (UnsupportedEncodingException ex) {
                    LogLog.error("Unable to encode SMTP subject", ex);
                }
            }
        } catch (MessagingException e) {
            LogLog.error("Could not activate SMTPAppender options.", e);
        }
        if (this.evaluator instanceof OptionHandler) {
            ((OptionHandler) this.evaluator).activateOptions();
        }
    }

    /* access modifiers changed from: protected */
    public void addressMessage(Message msg) throws MessagingException {
        if (this.from != null) {
            msg.setFrom(getAddress(this.from));
        } else {
            msg.setFrom();
        }
        if (this.replyTo != null && this.replyTo.length() > 0) {
            msg.setReplyTo(parseAddress(this.replyTo));
        }
        if (this.to != null && this.to.length() > 0) {
            msg.setRecipients(RecipientType.TO, parseAddress(this.to));
        }
        if (this.cc != null && this.cc.length() > 0) {
            msg.setRecipients(RecipientType.CC, parseAddress(this.cc));
        }
        if (this.bcc != null && this.bcc.length() > 0) {
            msg.setRecipients(RecipientType.BCC, parseAddress(this.bcc));
        }
    }

    /* access modifiers changed from: protected */
    public Session createSession() {
        Properties props;
        try {
            props = new Properties(System.getProperties());
        } catch (SecurityException e) {
            props = new Properties();
        }
        String prefix = "mail.smtp";
        if (this.smtpProtocol != null) {
            props.put("mail.transport.protocol", this.smtpProtocol);
            prefix = new StringBuffer().append("mail.").append(this.smtpProtocol).toString();
        }
        if (this.smtpHost != null) {
            props.put(new StringBuffer().append(prefix).append(".host").toString(), this.smtpHost);
        }
        if (this.smtpPort > 0) {
            props.put(new StringBuffer().append(prefix).append(".port").toString(), String.valueOf(this.smtpPort));
        }
        Authenticator auth = null;
        if (!(this.smtpPassword == null || this.smtpUsername == null)) {
            props.put(new StringBuffer().append(prefix).append(".auth").toString(), "true");
            auth = new Authenticator(this) {
                private final SMTPAppender this$0;

                {
                    this.this$0 = r1;
                }

                /* access modifiers changed from: protected */
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTPAppender.access$000(this.this$0), SMTPAppender.access$100(this.this$0));
                }
            };
        }
        Session session = Session.getInstance(props, auth);
        if (this.smtpProtocol != null) {
            session.setProtocolForAddress("rfc822", this.smtpProtocol);
        }
        if (this.smtpDebug) {
            session.setDebug(this.smtpDebug);
        }
        return session;
    }

    public void append(LoggingEvent event) {
        if (checkEntryConditions()) {
            event.getThreadName();
            event.getNDC();
            event.getMDCCopy();
            if (this.locationInfo) {
                event.getLocationInformation();
            }
            event.getRenderedMessage();
            event.getThrowableStrRep();
            this.cb.add(event);
            if (this.evaluator.isTriggeringEvent(event)) {
                sendBuffer();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkEntryConditions() {
        if (this.msg == null) {
            this.errorHandler.error("Message object not configured.");
            return false;
        } else if (this.evaluator == null) {
            this.errorHandler.error(new StringBuffer().append("No TriggeringEventEvaluator is set for appender [").append(this.name).append("].").toString());
            return false;
        } else if (this.layout != null) {
            return true;
        } else {
            this.errorHandler.error(new StringBuffer().append("No layout set for appender named [").append(this.name).append("].").toString());
            return false;
        }
    }

    public synchronized void close() {
        this.closed = true;
        if (this.sendOnClose && this.cb.length() > 0) {
            sendBuffer();
        }
    }

    /* access modifiers changed from: 0000 */
    public InternetAddress getAddress(String addressStr) {
        try {
            return new InternetAddress(addressStr);
        } catch (AddressException e) {
            this.errorHandler.error(new StringBuffer().append("Could not parse address [").append(addressStr).append("].").toString(), e, 6);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public InternetAddress[] parseAddress(String addressStr) {
        try {
            return InternetAddress.parse(addressStr, true);
        } catch (AddressException e) {
            this.errorHandler.error(new StringBuffer().append("Could not parse address [").append(addressStr).append("].").toString(), e, 6);
            return null;
        }
    }

    public String getTo() {
        return this.to;
    }

    public boolean requiresLayout() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String formatBody() {
        StringBuffer sbuf = new StringBuffer();
        String t = this.layout.getHeader();
        if (t != null) {
            sbuf.append(t);
        }
        int len = this.cb.length();
        for (int i = 0; i < len; i++) {
            LoggingEvent event = this.cb.get();
            sbuf.append(this.layout.format(event));
            if (this.layout.ignoresThrowable()) {
                String[] s = event.getThrowableStrRep();
                if (s != null) {
                    for (String append : s) {
                        sbuf.append(append);
                        sbuf.append(Layout.LINE_SEP);
                    }
                }
            }
        }
        t = this.layout.getFooter();
        if (t != null) {
            sbuf.append(t);
        }
        return sbuf.toString();
    }

    /* access modifiers changed from: protected */
    public void sendBuffer() {
        try {
            int i;
            MimeBodyPart part;
            String s = formatBody();
            boolean allAscii = true;
            for (i = 0; i < s.length() && allAscii; i++) {
                allAscii = s.charAt(i) <= 127;
            }
            if (allAscii) {
                part = new MimeBodyPart();
                part.setContent(s, this.layout.getContentType());
            } else {
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Writer writer = new OutputStreamWriter(MimeUtility.encode(os, "quoted-printable"), "UTF-8");
                    writer.write(s);
                    writer.close();
                    InternetHeaders headers = new InternetHeaders();
                    headers.setHeader("Content-Type", new StringBuffer().append(this.layout.getContentType()).append("; charset=UTF-8").toString());
                    headers.setHeader("Content-Transfer-Encoding", "quoted-printable");
                    part = new MimeBodyPart(headers, os.toByteArray());
                } catch (Exception e) {
                    StringBuffer sbuf = new StringBuffer(s);
                    for (i = 0; i < sbuf.length(); i++) {
                        if (sbuf.charAt(i) >= 128) {
                            sbuf.setCharAt(i, '?');
                        }
                    }
                    part = new MimeBodyPart();
                    part.setContent(sbuf.toString(), this.layout.getContentType());
                }
            }
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(part);
            this.msg.setContent(mp);
            this.msg.setSentDate(new Date());
            Transport.send(this.msg);
        } catch (MessagingException e2) {
            LogLog.error("Error occured while sending e-mail notification.", e2);
        } catch (RuntimeException e3) {
            LogLog.error("Error occured while sending e-mail notification.", e3);
        }
    }

    public String getEvaluatorClass() {
        return this.evaluator == null ? null : this.evaluator.getClass().getName();
    }

    public String getFrom() {
        return this.from;
    }

    public String getReplyTo() {
        return this.replyTo;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setReplyTo(String addresses) {
        this.replyTo = addresses;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        this.cb.resize(bufferSize);
    }

    public void setSMTPHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSMTPHost() {
        return this.smtpHost;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setEvaluatorClass(String value) {
        Class class$;
        if (class$org$apache$log4j$spi$TriggeringEventEvaluator == null) {
            class$ = class$("org.apache.log4j.spi.TriggeringEventEvaluator");
            class$org$apache$log4j$spi$TriggeringEventEvaluator = class$;
        } else {
            class$ = class$org$apache$log4j$spi$TriggeringEventEvaluator;
        }
        this.evaluator = (TriggeringEventEvaluator) OptionConverter.instantiateByClassName(value, class$, this.evaluator);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setCc(String addresses) {
        this.cc = addresses;
    }

    public String getCc() {
        return this.cc;
    }

    public void setBcc(String addresses) {
        this.bcc = addresses;
    }

    public String getBcc() {
        return this.bcc;
    }

    public void setSMTPPassword(String password) {
        this.smtpPassword = password;
    }

    public void setSMTPUsername(String username) {
        this.smtpUsername = username;
    }

    public void setSMTPDebug(boolean debug) {
        this.smtpDebug = debug;
    }

    public String getSMTPPassword() {
        return this.smtpPassword;
    }

    public String getSMTPUsername() {
        return this.smtpUsername;
    }

    public boolean getSMTPDebug() {
        return this.smtpDebug;
    }

    public final void setEvaluator(TriggeringEventEvaluator trigger) {
        if (trigger == null) {
            throw new NullPointerException("trigger");
        }
        this.evaluator = trigger;
    }

    public final TriggeringEventEvaluator getEvaluator() {
        return this.evaluator;
    }

    public boolean parseUnrecognizedElement(Element element, Properties props) throws Exception {
        if (!"triggeringPolicy".equals(element.getNodeName())) {
            return false;
        }
        Class class$;
        if (class$org$apache$log4j$spi$TriggeringEventEvaluator == null) {
            class$ = class$("org.apache.log4j.spi.TriggeringEventEvaluator");
            class$org$apache$log4j$spi$TriggeringEventEvaluator = class$;
        } else {
            class$ = class$org$apache$log4j$spi$TriggeringEventEvaluator;
        }
        Object triggerPolicy = DOMConfigurator.parseElement(element, props, class$);
        if (triggerPolicy instanceof TriggeringEventEvaluator) {
            setEvaluator((TriggeringEventEvaluator) triggerPolicy);
        }
        return true;
    }

    public final String getSMTPProtocol() {
        return this.smtpProtocol;
    }

    public final void setSMTPProtocol(String val) {
        this.smtpProtocol = val;
    }

    public final int getSMTPPort() {
        return this.smtpPort;
    }

    public final void setSMTPPort(int val) {
        this.smtpPort = val;
    }

    public final boolean getSendOnClose() {
        return this.sendOnClose;
    }

    public final void setSendOnClose(boolean val) {
        this.sendOnClose = val;
    }
}
