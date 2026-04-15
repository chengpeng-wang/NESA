package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.util.SharedByteArrayInputStream;

public class MimeMessage extends Message implements MimePart {
    private static final Flags answeredFlag = new Flags(Flag.ANSWERED);
    private static MailDateFormat mailDateFormat = new MailDateFormat();
    Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected Flags flags;
    protected InternetHeaders headers;
    protected boolean modified;
    protected boolean saved;
    private boolean strict;

    public static class RecipientType extends javax.mail.Message.RecipientType {
        public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");
        private static final long serialVersionUID = -5468290701714395543L;

        protected RecipientType(String type) {
            super(type);
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("Newsgroups")) {
                return NEWSGROUPS;
            }
            return super.readResolve();
        }
    }

    public MimeMessage(Session session) {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.modified = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        initStrict();
    }

    public MimeMessage(Session session, InputStream is) throws MessagingException {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = new Flags();
        initStrict();
        parse(is);
        this.saved = true;
    }

    public MimeMessage(MimeMessage source) throws MessagingException {
        ByteArrayOutputStream bos;
        super(source.session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = source.getFlags();
        int size = source.getSize();
        if (size > 0) {
            bos = new ByteArrayOutputStream(size);
        } else {
            bos = new ByteArrayOutputStream();
        }
        try {
            this.strict = source.strict;
            source.writeTo(bos);
            bos.close();
            SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
            parse(bis);
            bis.close();
            this.saved = true;
        } catch (IOException ex) {
            throw new MessagingException("IOException while copying message", ex);
        }
    }

    protected MimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = new Flags();
        this.saved = true;
        initStrict();
    }

    protected MimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        this(folder, msgnum);
        initStrict();
        parse(is);
    }

    protected MimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        this(folder, msgnum);
        this.headers = headers;
        this.content = content;
        initStrict();
    }

    private void initStrict() {
        if (this.session != null) {
            String s = this.session.getProperty("mail.mime.address.strict");
            boolean z = s == null || !s.equalsIgnoreCase("false");
            this.strict = z;
        }
    }

    /* access modifiers changed from: protected */
    public void parse(InputStream is) throws MessagingException {
        if (!((is instanceof ByteArrayInputStream) || (is instanceof BufferedInputStream) || (is instanceof SharedInputStream))) {
            is = new BufferedInputStream(is);
        }
        this.headers = createInternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(is);
            } catch (IOException ioex) {
                throw new MessagingException("IOException", ioex);
            }
        }
        this.modified = false;
    }

    public Address[] getFrom() throws MessagingException {
        Address[] a = getAddressHeader("From");
        if (a == null) {
            return getAddressHeader("Sender");
        }
        return a;
    }

    public void setFrom(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("From");
        } else {
            setHeader("From", address.toString());
        }
    }

    public void setFrom() throws MessagingException {
        InternetAddress me = InternetAddress.getLocalAddress(this.session);
        if (me != null) {
            setFrom(me);
            return;
        }
        throw new MessagingException("No From address");
    }

    public void addFrom(Address[] addresses) throws MessagingException {
        addAddressHeader("From", addresses);
    }

    public Address getSender() throws MessagingException {
        Address[] a = getAddressHeader("Sender");
        if (a == null || a.length == 0) {
            return null;
        }
        return a[0];
    }

    public void setSender(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("Sender");
        } else {
            setHeader("Sender", address.toString());
        }
    }

    public Address[] getRecipients(javax.mail.Message.RecipientType type) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            return getAddressHeader(getHeaderName(type));
        }
        String s = getHeader("Newsgroups", ",");
        return s == null ? null : NewsAddress.parse(s);
    }

    public Address[] getAllRecipients() throws MessagingException {
        Address[] all = super.getAllRecipients();
        Address[] ng = getRecipients(RecipientType.NEWSGROUPS);
        if (ng == null) {
            return all;
        }
        if (all == null) {
            return ng;
        }
        Address[] addresses = new Address[(all.length + ng.length)];
        System.arraycopy(all, 0, addresses, 0, all.length);
        System.arraycopy(ng, 0, addresses, all.length, ng.length);
        return addresses;
    }

    public void setRecipients(javax.mail.Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(type), addresses);
        } else if (addresses == null || addresses.length == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", NewsAddress.toString(addresses));
        }
    }

    public void setRecipients(javax.mail.Message.RecipientType type, String addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
        } else if (addresses == null || addresses.length() == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", addresses);
        }
    }

    public void addRecipients(javax.mail.Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = NewsAddress.toString(addresses);
            if (s != null) {
                addHeader("Newsgroups", s);
                return;
            }
            return;
        }
        addAddressHeader(getHeaderName(type), addresses);
    }

    public void addRecipients(javax.mail.Message.RecipientType type, String addresses) throws MessagingException {
        if (type != RecipientType.NEWSGROUPS) {
            addAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
        } else if (addresses != null && addresses.length() != 0) {
            addHeader("Newsgroups", addresses);
        }
    }

    public Address[] getReplyTo() throws MessagingException {
        Address[] a = getAddressHeader("Reply-To");
        if (a == null) {
            return getFrom();
        }
        return a;
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        setAddressHeader("Reply-To", addresses);
    }

    private Address[] getAddressHeader(String name) throws MessagingException {
        String s = getHeader(name, ",");
        return s == null ? null : InternetAddress.parseHeader(s, this.strict);
    }

    private void setAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s == null) {
            removeHeader(name);
        } else {
            setHeader(name, s);
        }
    }

    private void addAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s != null) {
            addHeader(name, s);
        }
    }

    public String getSubject() throws MessagingException {
        String rawvalue = getHeader("Subject", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e) {
            return rawvalue;
        }
    }

    public void setSubject(String subject) throws MessagingException {
        setSubject(subject, null);
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        if (subject == null) {
            removeHeader("Subject");
            return;
        }
        try {
            setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(subject, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    public Date getSentDate() throws MessagingException {
        String s = getHeader("Date", null);
        if (s == null) {
            return null;
        }
        try {
            Date parse;
            synchronized (mailDateFormat) {
                parse = mailDateFormat.parse(s);
            }
            return parse;
        } catch (ParseException e) {
            return null;
        }
    }

    public void setSentDate(Date d) throws MessagingException {
        if (d == null) {
            removeHeader("Date");
            return;
        }
        synchronized (mailDateFormat) {
            setHeader("Date", mailDateFormat.format(d));
        }
    }

    public Date getReceivedDate() throws MessagingException {
        return null;
    }

    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
            } catch (IOException e) {
            }
        }
        return -1;
    }

    public int getLineCount() throws MessagingException {
        return -1;
    }

    public String getContentType() throws MessagingException {
        String s = getHeader("Content-Type", null);
        if (s == null) {
            return "text/plain";
        }
        return s;
    }

    public boolean isMimeType(String mimeType) throws MessagingException {
        return MimeBodyPart.isMimeType(this, mimeType);
    }

    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    public void setDisposition(String disposition) throws MessagingException {
        MimeBodyPart.setDisposition(this, disposition);
    }

    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
    }

    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public void setContentID(String cid) throws MessagingException {
        if (cid == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", cid);
        }
    }

    public String getContentMD5() throws MessagingException {
        return getHeader("Content-MD5", null);
    }

    public void setContentMD5(String md5) throws MessagingException {
        setHeader("Content-MD5", md5);
    }

    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        MimeBodyPart.setDescription(this, description, charset);
    }

    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    public void setContentLanguage(String[] languages) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, languages);
    }

    public String getMessageID() throws MessagingException {
        return getHeader("Message-ID", null);
    }

    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    public void setFileName(String filename) throws MessagingException {
        MimeBodyPart.setFileName(this, filename);
    }

    private String getHeaderName(javax.mail.Message.RecipientType type) throws MessagingException {
        if (type == javax.mail.Message.RecipientType.TO) {
            return "To";
        }
        if (type == javax.mail.Message.RecipientType.CC) {
            return "Cc";
        }
        if (type == javax.mail.Message.RecipientType.BCC) {
            return "Bcc";
        }
        if (type == RecipientType.NEWSGROUPS) {
            return "Newsgroups";
        }
        throw new MessagingException("Invalid Recipient Type");
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    /* access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0, -1);
        }
        if (this.content != null) {
            return new SharedByteArrayInputStream(this.content);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new DataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object c = getDataHandler().getContent();
            if (!MimeBodyPart.cacheMultipart) {
                return c;
            }
            if (!(c instanceof Multipart) && !(c instanceof Message)) {
                return c;
            }
            if (this.content == null && this.contentStream == null) {
                return c;
            }
            this.cachedContent = c;
            return c;
        } catch (FolderClosedIOException fex) {
            throw new FolderClosedException(fex.getFolder(), fex.getMessage());
        } catch (MessageRemovedIOException mex) {
            throw new MessageRemovedException(mex.getMessage());
        }
    }

    public synchronized void setDataHandler(DataHandler dh) throws MessagingException {
        this.dh = dh;
        this.cachedContent = null;
        MimeBodyPart.invalidateContentHeaders(this);
    }

    public void setContent(Object o, String type) throws MessagingException {
        if (o instanceof Multipart) {
            setContent((Multipart) o);
        } else {
            setDataHandler(new DataHandler(o, type));
        }
    }

    public void setText(String text) throws MessagingException {
        setText(text, null);
    }

    public void setText(String text, String charset) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, "plain");
    }

    public void setText(String text, String charset, String subtype) throws MessagingException {
        MimeBodyPart.setText(this, text, charset, subtype);
    }

    public void setContent(Multipart mp) throws MessagingException {
        setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    public Message reply(boolean replyToAll) throws MessagingException {
        MimeMessage reply = createMimeMessage(this.session);
        String subject = getHeader("Subject", null);
        if (subject != null) {
            if (!subject.regionMatches(true, 0, "Re: ", 0, 4)) {
                subject = "Re: " + subject;
            }
            reply.setHeader("Subject", subject);
        }
        Address[] a = getReplyTo();
        reply.setRecipients(javax.mail.Message.RecipientType.TO, a);
        if (replyToAll) {
            Vector v = new Vector();
            InternetAddress me = InternetAddress.getLocalAddress(this.session);
            if (me != null) {
                v.addElement(me);
            }
            String alternates = null;
            if (this.session != null) {
                alternates = this.session.getProperty("mail.alternates");
            }
            if (alternates != null) {
                eliminateDuplicates(v, InternetAddress.parse(alternates, false));
            }
            String replyallccStr = null;
            if (this.session != null) {
                replyallccStr = this.session.getProperty("mail.replyallcc");
            }
            boolean replyallcc = replyallccStr != null && replyallccStr.equalsIgnoreCase("true");
            eliminateDuplicates(v, a);
            a = eliminateDuplicates(v, getRecipients(javax.mail.Message.RecipientType.TO));
            if (a != null && a.length > 0) {
                if (replyallcc) {
                    reply.addRecipients(javax.mail.Message.RecipientType.CC, a);
                } else {
                    reply.addRecipients(javax.mail.Message.RecipientType.TO, a);
                }
            }
            a = eliminateDuplicates(v, getRecipients(javax.mail.Message.RecipientType.CC));
            if (a != null && a.length > 0) {
                reply.addRecipients(javax.mail.Message.RecipientType.CC, a);
            }
            a = getRecipients(RecipientType.NEWSGROUPS);
            if (a != null && a.length > 0) {
                reply.setRecipients(RecipientType.NEWSGROUPS, a);
            }
        }
        String msgId = getHeader("Message-Id", null);
        if (msgId != null) {
            reply.setHeader("In-Reply-To", msgId);
        }
        String refs = getHeader("References", " ");
        if (refs == null) {
            refs = getHeader("In-Reply-To", " ");
        }
        if (msgId != null) {
            if (refs != null) {
                refs = MimeUtility.unfold(refs) + " " + msgId;
            } else {
                refs = msgId;
            }
        }
        if (refs != null) {
            reply.setHeader("References", MimeUtility.fold(12, refs));
        }
        try {
            setFlags(answeredFlag, true);
        } catch (MessagingException e) {
        }
        return reply;
    }

    private Address[] eliminateDuplicates(Vector v, Address[] addrs) {
        if (addrs == null) {
            return null;
        }
        int i;
        int j;
        int gone = 0;
        for (i = 0; i < addrs.length; i++) {
            boolean found = false;
            for (j = 0; j < v.size(); j++) {
                if (((InternetAddress) v.elementAt(j)).equals(addrs[i])) {
                    found = true;
                    gone++;
                    addrs[i] = null;
                    break;
                }
            }
            if (!found) {
                v.addElement(addrs[i]);
            }
        }
        if (gone != 0) {
            Address[] a;
            if (addrs instanceof InternetAddress[]) {
                a = new InternetAddress[(addrs.length - gone)];
            } else {
                a = new Address[(addrs.length - gone)];
            }
            j = 0;
            for (i = 0; i < addrs.length; i++) {
                if (addrs[i] != null) {
                    int j2 = j + 1;
                    a[j] = addrs[i];
                    j = j2;
                }
            }
            addrs = a;
        }
        return addrs;
    }

    public void writeTo(OutputStream os) throws IOException, MessagingException {
        writeTo(os, null);
    }

    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        if (!this.saved) {
            saveChanges();
        }
        if (this.modified) {
            MimeBodyPart.writeTo(this, os, ignoreList);
            return;
        }
        Enumeration hdrLines = getNonMatchingHeaderLines(ignoreList);
        LineOutputStream los = new LineOutputStream(os);
        while (hdrLines.hasMoreElements()) {
            los.writeln((String) hdrLines.nextElement());
        }
        los.writeln();
        if (this.content == null) {
            InputStream is = getContentStream();
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len <= 0) {
                    break;
                }
                os.write(buf, 0, len);
            }
            is.close();
            buf = null;
        } else {
            os.write(this.content);
        }
        os.flush();
    }

    public String[] getHeader(String name) throws MessagingException {
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) throws MessagingException {
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) throws MessagingException {
        this.headers.setHeader(name, value);
    }

    public void addHeader(String name, String value) throws MessagingException {
        this.headers.addHeader(name, value);
    }

    public void removeHeader(String name) throws MessagingException {
        this.headers.removeHeader(name);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaders(names);
    }

    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaders(names);
    }

    public void addHeaderLine(String line) throws MessagingException {
        this.headers.addHeaderLine(line);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaderLines(names);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(names);
    }

    public synchronized Flags getFlags() throws MessagingException {
        return (Flags) this.flags.clone();
    }

    public synchronized boolean isSet(Flag flag) throws MessagingException {
        return this.flags.contains(flag);
    }

    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        if (set) {
            this.flags.add(flag);
        } else {
            this.flags.remove(flag);
        }
    }

    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        updateHeaders();
    }

    /* access modifiers changed from: protected */
    public void updateMessageID() throws MessagingException {
        setHeader("Message-ID", "<" + UniqueValue.getUniqueMessageIDValue(this.session) + ">");
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        setHeader("MIME-Version", "1.0");
        updateMessageID();
        if (this.cachedContent != null) {
            this.dh = new DataHandler(this.cachedContent, getContentType());
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                } catch (IOException e) {
                }
            }
            this.contentStream = null;
        }
    }

    /* access modifiers changed from: protected */
    public InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    /* access modifiers changed from: protected */
    public MimeMessage createMimeMessage(Session session) throws MessagingException {
        return new MimeMessage(session);
    }
}
